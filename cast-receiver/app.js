const context = cast.framework.CastReceiverContext.getInstance();
const playerManager = context.getPlayerManager();

const bgEl = document.getElementById('bg');
const playerEl = document.getElementById('player');
const idleEl = document.getElementById('idle');
const artworkEl = document.getElementById('artwork');
const titleEl = document.getElementById('title');
const artistEl = document.getElementById('artist');
const progressFillEl = document.getElementById('progressFill');
const currentTimeEl = document.getElementById('currentTime');
const durationEl = document.getElementById('duration');

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

playerManager.addEventListener(cast.framework.events.EventType.MEDIA_STATUS, () => {
  const mediaStatus = playerManager.getMediaStatus();
  if (!mediaStatus || !mediaStatus.media) {
    showIdle();
    return;
  }
  showPlayer();
  updateMetadata(mediaStatus.media);
});

playerManager.addEventListener(cast.framework.events.EventType.TIME_UPDATE, () => {
  const mediaStatus = playerManager.getMediaStatus();
  if (!mediaStatus || !mediaStatus.media) return;

  const duration = mediaStatus.media.duration || 0;
  const current = playerManager.getCurrentTimeSec ? playerManager.getCurrentTimeSec() : mediaStatus.currentTime || 0;
  const pct = duration > 0 ? (current / duration) * 100 : 0;

  progressFillEl.style.width = `${pct}%`;
  currentTimeEl.textContent = formatTime(current);
  durationEl.textContent = formatTime(duration);
});

playerManager.addEventListener(cast.framework.events.EventType.ENDED, showIdle);

const options = new cast.framework.CastReceiverOptions();
options.maxInactivity = 3600;
context.start(options);
