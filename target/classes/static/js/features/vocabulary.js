function renderVocabulary() {
    // Chỉ cần bind filter
    filterVocab('all');
}

function filterVocab(category) {
    document.querySelectorAll('.category-btn').forEach(b =>
        b.classList.toggle('active', b.dataset.category === category)
    );

    const items = document.querySelectorAll('.vocab-item');

    items.forEach(item => {
        const itemCategory = item.dataset.category;

        if (category === 'all' || itemCategory === category) {
            item.style.display = 'flex';
        } else {
            item.style.display = 'none';
        }
    });
}

function toggleVocabLearned(btn) {
    const kr = btn.dataset.kr;

    const idx = state.vocabLearned.indexOf(kr);

    if (idx >= 0) {
        state.vocabLearned.splice(idx, 1);
        btn.classList.remove('marked');
        btn.textContent = '📌';
    } else {
        state.vocabLearned.push(kr);
        btn.classList.add('marked');
        btn.textContent = '✅';
    }

    saveState();
}

// Init learned state
function initVocabLearnedUI() {
    document.querySelectorAll('.vocab-learn-btn').forEach(btn => {
        const kr = btn.dataset.kr;
        if (state.vocabLearned.includes(kr)) {
            btn.classList.add('marked');
            btn.textContent = '✅';
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("page-vocabulary")) {
        renderVocabulary();
        initVocabLearnedUI();
    }
});

window.renderVocabulary = renderVocabulary;
window.filterVocab = filterVocab;
window.toggleVocabLearned = toggleVocabLearned;
