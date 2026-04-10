// ---- Writing ----
function initWriting() {
    renderWritingChar();
    renderTranslation();
}

async function loadWriting(lessonId) {
    const [charsRes, transRes] = await Promise.all([
        fetch(`/api/lessons/${lessonId}/writing/chars`),
        fetch(`/api/lessons/${lessonId}/writing/translate`)
    ]);

    state.writingChars = await charsRes.json();
    state.translateData = await transRes.json();

    state.writingCharIndex = 0;
    state.translateIndex = 0;

    renderWritingChar();
    renderTranslation();
}

function switchWritingTab(tab) {
    state.writingTab = tab;
    document.querySelectorAll('#page-writing .tab-btn').forEach((b, i) => b.classList.toggle('active', (i === 0 && tab === 'draw') || (i === 1 && tab === 'translate')));
    document.getElementById('writingDrawTab').style.display = tab === 'draw' ? 'block' : 'none';
    document.getElementById('writingDrawTab').classList.toggle('active', tab === 'draw');
    document.getElementById('writingTranslateTab').style.display = tab === 'translate' ? 'block' : 'none';
}

function renderWritingChar() {
    const data = state.writingChars || [];
    if (data.length === 0) return;

    const char = data[state.writingCharIndex % data.length];

    document.getElementById('writingTargetChar').textContent = char.hangul;
    document.getElementById('writingTargetInfo').textContent = char.roman || '';

    clearCanvas();
}

function nextWritingChar() {
    const data = state.writingChars || [];
    if (data.length === 0) return;

    state.writingCharIndex = (state.writingCharIndex + 1) % data.length;
    renderWritingChar();
}

function renderTranslation() {
    const data = state.translateData || [];
    if (data.length === 0) return;

    const t = data[state.translateIndex % data.length];

    document.getElementById('translateSource').textContent = t.vi;
    document.getElementById('translateInput').value = '';
    document.getElementById('translateResult').style.display = 'none';
}

function checkTranslation() {
    const data = state.translateData || [];
    if (data.length === 0) return;

    const t = data[state.translateIndex % data.length];
    const input = document.getElementById('translateInput').value.trim();

    document.getElementById('translateAnswer').textContent = t.kr;
    document.getElementById('translateResult').style.display = 'block';

    if (input === t.kr) showToast('Chính xác! 🎉', 'success');
    else showToast('Xem đáp án bên dưới', 'info');
}

function nextTranslation() {
    const data = state.translateData || [];
    if (data.length === 0) return;

    state.translateIndex = (state.translateIndex + 1) % data.length;
    renderTranslation();
}

document.addEventListener('DOMContentLoaded', () => {
    const el = document.getElementById('page-writing');
    const lessonId = el?.getAttribute('data-lesson-id');

    console.log("Lesson ID:", lessonId);

    if (lessonId) loadWriting(lessonId);
});

window.initWriting = initWriting;
window.switchWritingTab = switchWritingTab;
window.nextWritingChar = nextWritingChar;
window.checkTranslation = checkTranslation;
window.nextTranslation = nextTranslation;
window.renderWritingChar = renderWritingChar;
window.renderTranslation = renderTranslation;
