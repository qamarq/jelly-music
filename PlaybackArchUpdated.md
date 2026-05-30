Elovaire playback is currently built on **AndroidX Media3 / ExoPlayer 1.10.1** using `media3-exoplayer`, `media3-extractor`, `media3-ui`, and `media3-session`. The app does not ship optional Media3 software decoder extensions, so real-world codec coverage comes from a mix of Media3 container support and Android platform decoders on the device.

The playback stack is centered on a custom `PlaybackManager` plus a custom `ElovaireRenderersFactory` backed by `DefaultAudioSink`. The player enables decoder fallback, prefers extension renderers if any are bundled, enables constant-bitrate seeking for formats such as MP3 and ADTS AAC, sets `WAKE_MODE_LOCAL`, and handles noisy-route disconnects. It also sets `setAudioAttributes(..., false)`, which means audio focus is handled by Elovaire's own `AudioFocusRequest` logic rather than by Media3 automatically.

```kotlin
private val extractorsFactory = DefaultExtractorsFactory()
    .setConstantBitrateSeekingEnabled(true)

private fun createPlayer(enableSignalProcessing: Boolean): ExoPlayer {
    return ExoPlayer.Builder(appContext)
        .setRenderersFactory(
            ElovaireRenderersFactory(
                appContext,
                if (enableSignalProcessing) audioProcessorsProvider() else emptyArray(),
            )
                .setEnableAudioFloatOutput(false)
                .setEnableDecoderFallback(true)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER),
        )
        .setMediaSourceFactory(DefaultMediaSourceFactory(appContext, extractorsFactory))
        .setAudioAttributes(playbackAudioAttributes, false)
        .setWakeMode(C.WAKE_MODE_LOCAL)
        .setHandleAudioBecomingNoisy(true)
        .build()
}
```

### Current playback-effect architecture

Elovaire's signal processing is currently **in-app DSP**, not a chain of platform audio-session effects. `PlaybackEffectsController` exposes a single `EqualizerAudioProcessor`, and that processor owns the current effect stack:

- 18-band equalizer
- bass shaping
- treble shaping
- spaciousness amount and mode
- mono downmix
- reverb

`PlaybackEffectsController.updateAudioSessionId(...)` is intentionally a no-op, which is a good shorthand for the current architecture: the app no longer depends on attaching Android audio effects to a player audio session.

### Direct / bit-perfect playback path

Elovaire also contains a separate direct-playback path for eligible USB routes. This path is not always active.

- The app's current `minSdk` is **29** (`Android 10`), not 27.
- Bit-perfect/direct playback routing logic is present, but it only becomes eligible on **Android 13+** where the app can query direct playback support through `AudioManager.getDirectPlaybackSupport(...)`.
- If signal-altering effects are active, Elovaire marks the route as `EffectsActive` and does **not** use the direct path.
- When direct playback is eligible, `PlaybackManager` rebuilds the player with `enableSignalProcessing = false`, which removes the app's audio processors for that route.

That means the direct/bit-perfect path should be documented as a conditional route optimization, not as the default playback mode for every device or output.

### Supported local playback formats

The table below reflects the **current application build**. Because Elovaire now targets `minSdk = 29`, Android 10 level decoder guarantees matter more than the older Android 8.1 assumptions in the previous draft.

| Format / common file type | Current build support | Practical baseline for Elovaire-supported devices | Notes |
| --- | --- | --- | --- |
| **MP3** (`.mp3`) | **Supported** | Platform MP3 decoder support is standard on Android; Media3 plays MP3 directly. | Elovaire enables constant-bitrate seeking to improve seeking on applicable files. |
| **AAC-LC / HE-AAC / HE-AACv2** (`.m4a`, `.mp4`, `.aac` ADTS) | **Supported** | Supported across the app's API range. | ADTS AAC benefits from constant-bitrate seeking. |
| **xHE-AAC** (`.m4a`, `.mp4`) | **Supported on all currently supported Android versions** | Elovaire's `minSdk = 29` already clears Android 9's platform requirement for xHE-AAC decoding. | The earlier "not guaranteed on API 27" warning is no longer applicable. |
| **AAC-ELD** | **Supported by platform decoder** | Available on supported Android versions. | Mostly relevant to low-delay audio rather than typical music libraries. |
| **FLAC** (`.flac`) | **Supported** | Android platform FLAC decode is available across Elovaire's supported API range. | The app relies on Media3 extraction plus platform decoding; it does not bundle a software FLAC decoder. |
| **WAV / linear PCM** (`.wav`) | **Supported** | Standard PCM WAV playback is supported. | Elovaire explicitly leaves Media3 float output disabled, so this should not be documented as a universal high-resolution float-output path. |
| **Ogg Vorbis** (`.ogg`) | **Supported** | Supported through Media3 Ogg container support plus platform Vorbis decode. | |
| **Opus** (`.ogg`, `.webm`) | **Supported** | Elovaire's Android 10+ floor is already within the supported decoder range for Opus playback. | Ogg/WebM container handling comes from Media3. |
| **AMR-NB / AMR-WB** (`.amr`, `.3gp`) | **Technically supported** | Platform support exists, but these are not normal music-library formats. | Media3 seeking caveats for AMR still apply. |

### Formats that should not be documented as guaranteed

| Format | Status | Reason |
| --- | --- | --- |
| **ALAC / Apple Lossless** (`.m4a`) | **Not guaranteed** | The app does not bundle optional decoder extensions that would make ALAC a documented app-level capability, and Android does not list ALAC as a baseline platform audio format. |
| **AIFF** (`.aiff`, `.aif`) | **Not guaranteed** | No app-specific decoder or documented Media3/Android baseline guarantee is declared in the current playback stack. |
| **32-bit / 96-384 kHz FLAC or WAV** | **Device-dependent; not guaranteed** | Current code does not establish a universal high-resolution guarantee across all devices and routes. |
| **DSD, WavPack, APE/Monkey's Audio, Musepack** | **Not declared / not guaranteed** | No bundled decoder implementation or extension support is declared for them. |

### High-resolution and USB-DAC note

Elovaire does include USB-DAC handling, preferred-device routing, hardware-volume integration for some USB routes, and a conditional direct-playback path. None of that should be described as a blanket codec or resolution guarantee. High-resolution success still depends on the specific device, Android build, route, and attached DAC.

### Current UI state for playback effects

The current UI is split across two places:

- The **full Equalizer screen** currently contains the EQ graph, presets, tone-shaping controls, and the **Spaciousness** card with mode selection plus the **Effect strength** slider.
- The **Reverb** controls are **still in Settings**, where the user can choose a `Dry` or `Wet` profile and adjust reverb duration.

That means documentation should **not** say that reverb already lives inside the full Equalizer screen, because that is not the current shipped UI.

### Reverb UI and effect refinement

If the product direction is to consolidate audio effects into the dedicated EQ experience, the reverb UI should be moved into the **Spaciousness** card on the full Equalizer screen and placed directly under the **Effect strength** slider. Once that change ships, Settings should stop presenting reverb as a separate top-level effect control.

### Implementation notes

- `DefaultExtractorsFactory.setConstantBitrateSeekingEnabled(true)` is enabled.
- `DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER` is enabled, but no optional decoder extensions are currently declared by Gradle.
- `setEnableAudioFloatOutput(false)` is enabled.
- `PlaybackManager` can rebuild the player to remove audio processors when direct playback is available and effects are inactive.
- `PlaybackEffectsController` currently routes DSP through one in-app `EqualizerAudioProcessor`.

### Source anchors in the current app

- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `app/src/main/java/com/elovaire/music/data/playback/PlaybackManager.kt`
- `app/src/main/java/com/elovaire/music/data/playback/ElovaireRenderersFactory.kt`
- `app/src/main/java/com/elovaire/music/data/playback/PlaybackEffectsController.kt`
- `app/src/main/java/com/elovaire/music/data/playback/EqualizerAudioProcessor.kt`
- `app/src/main/java/com/elovaire/music/data/playback/BitPerfectUsbManager.kt`
- `app/src/main/java/com/elovaire/music/ui/screens/ElovaireRoot.kt`
