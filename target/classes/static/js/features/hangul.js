// ---- Hangul ----
function renderHangul() {
    const data = HANGUL_DATA[state.hangulTab] || HANGUL_DATA.consonants;
    const grid = document.getElementById('hangulGrid');
    grid.innerHTML = data.map((h, i) => `
        <div class="hangul-cell ${state.hangulLearned.includes(h.char) ? 'learned' : ''}" onclick="showHangulDetail(${i})">
            <div class="hangul-char">${h.char}</div>
            <div class="hangul-roman">${h.roman}</div>
        </div>
    `).join('');
    document.getElementById('hangulDetail').style.display = 'none';
}

function switchHangulTab(tab) {
    state.hangulTab = tab;
    document.querySelectorAll('#page-hangul .tab-btn').forEach(b => b.classList.toggle('active', b.dataset.tab === tab));
    renderHangul();
}

function showHangulDetail(index) {
    const data = HANGUL_DATA[state.hangulTab];
    const h = data[index];
    state._currentHangulChar = h.char;
    document.getElementById('hangulDetailChar').textContent = h.char;
    document.getElementById('hangulDetailName').textContent = h.name;
    document.getElementById('hangulDetailRoman').textContent = h.roman;
    document.getElementById('hangulDetailDesc').textContent = h.desc;
    document.getElementById('hangulDetailExamples').innerHTML = h.examples.map(e => `<span class="hangul-example-tag">${e}</span>`).join('');
    document.getElementById('hangulDetail').style.display = 'block';
    document.getElementById('hangulDetail').scrollIntoView({ behavior: 'smooth' });
}

function closeHangulDetail() { document.getElementById('hangulDetail').style.display = 'none'; }

function markHangulLearned() {
    const char = state._currentHangulChar;
    if (char && !state.hangulLearned.includes(char)) {
        state.hangulLearned.push(char);
        saveState();
        showToast(`Đã học xong: ${char}`, 'success');
    }
    renderHangul();
}

window.renderHangul = renderHangul;
window.switchHangulTab = switchHangulTab;
window.showHangulDetail = showHangulDetail;
window.closeHangulDetail = closeHangulDetail;
window.markHangulLearned = markHangulLearned;
