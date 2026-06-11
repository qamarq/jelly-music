Task: please cleanly remove all attempted or functional support for these audio formats:

- WMA
    
- APE / Monkey’s Audio
    
- DSF / DSD Stream File
    
- DFF / DSD Interchange File Format
    

These formats are no longer desired. The app should no longer scan, index, display, classify, advertise, or attempt playback of these formats.

Do this safely and completely without breaking support for the remaining formats.

---

## Goal

Remove support for:

```text
.wma
.ape
.dsf
.dff
```

Keep support for the existing desired formats:

```text
.mp3
.m4a
.aac
.flac
.wav
.ogg
.opus
.amr
.3gp
.mp4
.mka
```

Do not remove support for any desired format.

---

## Current known source target

Start by inspecting the library scanning code, especially:

```text
app/src/main/java/com/elovaire/music/data/library/LibraryRepository.kt
app/src/main/java/com/elovaire/music/data/library/MediaStoreScanner.kt
```

The current scanner-supported extension list includes unwanted formats such as:

```kotlin
"wma",
"ape",
"dsf",
"dff",
```

Remove those from the supported-extension path.

---

## Required scope

Search the full project for all references to:

```text
wma
ape
dsf
dff
WMA
APE
DSF
DFF
Monkey
Monkey's Audio
DSD
Direct Stream Digital
```

Check:

- Kotlin source files.
    
- XML resources.
    
- String resources.
    
- Drawable/resource names.
    
- Tests.
    
- Documentation-like in-app text if any.
    
- File format labels.
    
- Media quality display code.
    
- Metadata extraction logic.
    
- Format-detection helpers.
    
- Library filtering logic.
    
- Search/filter UI.
    
- Error messages.
    
- Any hardcoded supported-format display text.
    
- Any changelog/about text embedded in source, if it affects current UI.
    

Do not rely only on the extension list. Remove all app-facing attempted support paths for these formats.

---

## Required behavior after removal

After the change:

1. Files ending in `.wma`, `.ape`, `.dsf`, or `.dff` should not be imported into the music library.
    
2. They should not appear in song lists, albums, artists, genres, playlists, search results, or recent playback.
    
3. They should not be selectable as supported audio files.
    
4. They should not be advertised or described as supported.
    
5. Existing indexed entries for these formats should disappear after a library refresh/rescan.
    
6. Existing playlists/favorites/recent entries should not crash if they previously referenced removed-format songs.
    
7. The app should fail safely if an old persisted object points to one of these files.
    
8. The app should not crash when the filesystem contains these files.
    
9. The app should continue scanning and playing all remaining supported formats normally.
    

---

## Cleanup requirements

Remove or update:

- Supported extension lists.
    
- Format name/label mapping.
    
- Any metadata parsing special cases for these formats.
    
- Any UI labels that claim support.
    
- Any tests or test data expecting support.
    
- Any fallback logic that exists only for these formats.
    
- Any dead helper functions that become unused after removal.
    
- Any unused imports/constants caused by this removal.
    

Do not remove broader code that is still needed for supported formats.

---

## Important compatibility handling

If the app persists library snapshots, playlists, favorites, recent playback, play counts, or search history containing old WMA/APE/DSF/DFF songs, handle that gracefully.

The preferred behavior is:

- On next scan/refresh, removed-format songs are filtered out.
    
- If a playlist references a removed-format song ID that no longer exists in the library, the app should ignore the missing song safely.
    
- If recent playback references a removed-format song ID, the app should not crash.
    
- If favorites reference a removed-format song ID, the app should not crash.
    
- Do not add a complex migration unless the current persistence model requires it.
    
- Do not delete user playlists or unrelated data.
    

---

## Validation requirements

Run/check the relevant verification directly. Do not create separate test-plan documentation.

Verify:

- Debug build succeeds.
    
- Lint/tests succeed if available.
    
- Library scan still imports:
    
    - MP3
        
    - M4A
        
    - AAC
        
    - FLAC
        
    - WAV
        
    - OGG
        
    - OPUS
        
    - AMR
        
    - 3GP
        
    - MP4
        
    - MKA
        
- Library scan ignores:
    
    - WMA
        
    - APE
        
    - DSF
        
    - DFF
        
- Search does not show ignored formats.
    
- Albums/artists/genres do not include ignored-format songs.
    
- Playlists do not crash if they reference a removed song.
    
- Favorites do not crash if they reference a removed song.
    
- Recent playback does not crash if it references a removed song.
    
- Manual refresh/rescan removes previously indexed ignored-format songs.
    
- Playback of all remaining supported formats still works.
    
- File deletion, folder switching, and MediaStore refresh behavior still work.
    

---

## Change discipline

Keep the change small and focused.

Do not:

- rewrite the whole scanner;
    
- change playback architecture;
    
- alter supported formats unrelated to WMA/APE/DSF/DFF;
    
- change UI design;
    
- change playlist behavior beyond safe missing-song handling;
    
- add bundled decoders;
    
- add FFmpeg;
    
- add new dependencies;
    
- create documentation files.
    

Do:

- remove the unwanted extensions cleanly;
    
- remove dead code created by the removal;
    
- preserve all desired formats;
    
- preserve app stability;
    
- keep the diff easy to review.
    

---

## Final expected result

The app should no longer support or attempt to support WMA, APE, DSF or DFF.

The remaining supported playback/import list should effectively be:

```text
MP3
M4A
AAC
FLAC
WAV
OGG
OPUS
AMR
3GP
MP4
MKA
```

The user experience should improve by avoiding unsupported or device-dependent tracks appearing in the library and later failing during playback.
