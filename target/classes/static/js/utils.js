// ---- TTS ----
function playKorean(text) {
    if (!('speechSynthesis' in window)) {
        showToast('Trình duyệt không hỗ trợ phát âm', 'error');
        return;
    }

    window.speechSynthesis.cancel();

    const u = new SpeechSynthesisUtterance(text);
    u.lang = 'ko-KR';
    u.rate = 0.8;

    window.speechSynthesis.speak(u);
}

// Backward-compatible alias
function speakKorean(text) {
    playKorean(text);
}

// ---- Utils ----
function shuffle(arr) {
    const a = [...arr];
    for (let i = a.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
}

// EXPORT GLOBAL
window.playKorean = playKorean;
window.speakKorean = speakKorean;
window.shuffle = shuffle;