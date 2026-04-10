// ---- Grammar ----
function renderGrammar() {
    document.getElementById('grammarList').innerHTML = GRAMMAR_DATA.map((g, i) => `
        <div class="grammar-card" onclick="showGrammarModal(${i})">
            <div class="grammar-card-header">
                <span class="grammar-title">${g.title}</span>
                <span class="grammar-level">${g.level}</span>
            </div>
            <p class="grammar-desc">${g.desc}</p>
        </div>
    `).join('');
}

function showGrammarModal(index) {
    const g = GRAMMAR_DATA[index];
    const examplesHTML = g.examples.map(e => `<div class="grammar-example"><div class="kr">${e.kr}</div><div class="vi">${e.vi}</div></div>`).join('');
    document.getElementById('grammarModalBody').innerHTML = `<h3>${g.title}</h3>${g.content}${examplesHTML}`;
    document.getElementById('grammarModal').style.display = 'flex';
    state.lessonsCompleted = Math.max(state.lessonsCompleted, index + 1);
    saveState();
}

function closeGrammarModal() { document.getElementById('grammarModal').style.display = 'none'; }

window.renderGrammar = renderGrammar;
