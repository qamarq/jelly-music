const bgEl = document.getElementById('bg');
const playerEl = document.getElementById('player');
const idleEl = document.getElementById('idle');
const artworkEl = document.getElementById('artwork');
const titleEl = document.getElementById('title');
const artistEl = document.getElementById('artist');
const progressFillEl = document.getElementById('progressFill');
const currentTimeEl = document.getElementById('currentTime');
const durationEl = document.getElementById('duration');
const errorBannerEl = document.getElementById('errorBanner');
const errorTextEl = document.getElementById('errorText');
const playIconEl = document.getElementById('playIcon');
const pauseIconEl = document.getElementById('pauseIcon');

function showError(label, detail) {
  const message = `[${new Date().toISOString()}] ${label}\n${detail}`;
  errorTextEl.textContent = `${errorTextEl.textContent}\n\n${message}`.trim();
  errorBannerEl.classList.remove('hidden');
  errorBannerEl.classList.add('flex');
  // eslint-disable-next-line no-console
  console.error(label, detail);
}

window.addEventListener('error', (event) => {
  showError('Uncaught error', `${event.message}\n${event.filename}:${event.lineno}:${event.colno}\n${event.error && event.error.stack ? event.error.stack : ''}`);
});

window.addEventListener('unhandledrejection', (event) => {
  showError('Unhandled promise rejection', event.reason && event.reason.stack ? event.reason.stack : String(event.reason));
});

function guarded(label, fn) {
  return (...args) => {
    try {
      fn(...args);
    } catch (err) {
      showError(label, err && err.stack ? err.stack : String(err));
    }
  };
}

const context = cast.framework.CastReceiverContext.getInstance();
const playerManager = context.getPlayerManager();

function formatTime(seconds) {
  if (!isFinite(seconds) || seconds < 0) seconds = 0;
  const m = Math.floor(seconds / 60);
  const s = Math.floor(seconds % 60);
  return `${m}:${s.toString().padStart(2, '0')}`;
}

function showPlayer() {
  idleEl.classList.add('hidden');
  playerEl.classList.remove('hidden');
  playerEl.classList.add('flex');
}

function showIdle() {
  playerEl.classList.add('hidden');
  playerEl.classList.remove('flex');
  idleEl.classList.remove('hidden');
}

function updateMetadata(mediaInformation) {
  if (!mediaInformation) return;
  const metadata = mediaInformation.metadata || {};

  const title = metadata.title || mediaInformation.contentId || 'Unknown title';
  const artist = metadata.artist || metadata.subtitle || '';
  const images = metadata.images || [];
  const artUrl = images.length > 0 ? images[0].url : '';

  titleEl.textContent = title;
  artistEl.textContent = artist;

  if (artUrl) {
    if (artworkEl.src !== artUrl) {
      artworkEl.src = artUrl;
      bgEl.style.backgroundImage = `url("${artUrl}")`;
    }
  } else {
    artworkEl.removeAttribute('src');
    bgEl.style.backgroundImage = '';
  }
}

function isCurrentlyPlaying() {
  return playerManager.getPlayerState() === cast.framework.messages.PlayerState.PLAYING;
}

function updatePlayPauseIcon() {
  const isPlaying = isCurrentlyPlaying();
  playIconEl.classList.toggle('hidden', isPlaying);
  pauseIconEl.classList.toggle('hidden', !isPlaying);
}

playerManager.addEventListener(
  cast.framework.events.EventType.MEDIA_STATUS,
  guarded('MEDIA_STATUS handler', () => {
    const mediaInformation = playerManager.getMediaInformation();
    if (!mediaInformation) {
      showIdle();
      return;
    }
    showPlayer();
    updateMetadata(mediaInformation);
    updatePlayPauseIcon();
  }),
);

playerManager.addEventListener(
  cast.framework.events.EventType.TIME_UPDATE,
  guarded('TIME_UPDATE handler', () => {
    const mediaInformation = playerManager.getMediaInformation();
    if (!mediaInformation) return;

    const duration = playerManager.getDurationSec() || 0;
    const current = playerManager.getCurrentTimeSec() || 0;
    const pct = duration > 0 ? (current / duration) * 100 : 0;

    progressFillEl.style.width = `${pct}%`;
    currentTimeEl.textContent = formatTime(current);
    durationEl.textContent = formatTime(duration);
    updatePlayPauseIcon();
  }),
);

playerManager.addEventListener(cast.framework.events.EventType.ENDED, guarded('ENDED handler', showIdle));

playerManager.addEventListener(
  cast.framework.events.EventType.ERROR,
  guarded('PlayerManager ERROR event', (event) => {
    showError('Player error', JSON.stringify(event, null, 2));
  }),
);

context.addEventListener(
  cast.framework.system.EventType.ERROR,
  guarded('CastReceiverContext ERROR event', (event) => {
    showError('Receiver error', JSON.stringify(event, null, 2));
  }),
);

context.addEventListener(
  cast.framework.system.EventType.SENDER_DISCONNECTED,
  guarded('SENDER_DISCONNECTED', (event) => {
    showError('Sender disconnected', JSON.stringify(event, null, 2));
  }),
);

const options = new cast.framework.CastReceiverOptions();
options.maxInactivity = 3600;

try {
  context.start(options);
} catch (err) {
  showError('context.start() threw', err && err.stack ? err.stack : String(err));
}
