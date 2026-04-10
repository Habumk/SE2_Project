// ---- LocalStorage ----
function loadState() {
    const saved = localStorage.getItem('klearn_state');
    if (saved) {
        const s = JSON.parse(saved);
        state.hangulLearned = s.hangulLearned || [];
        state.vocabLearned = s.vocabLearned || [];
        state.streak = s.streak || 0;
        state.lastStudyDate = s.lastStudyDate || null;
        state.lessonsCompleted = s.lessonsCompleted || 0;
        state.quizScores = s.quizScores || [];
        state.studyMinutes = s.studyMinutes || 0;
    }
}

function saveState() {
    localStorage.setItem('klearn_state', JSON.stringify({
        hangulLearned: state.hangulLearned,
        vocabLearned: state.vocabLearned,
        streak: state.streak,
        lastStudyDate: state.lastStudyDate,
        lessonsCompleted: state.lessonsCompleted,
        quizScores: state.quizScores,
        studyMinutes: state.studyMinutes,
    }));
}

function updateStreak() {
    const today = new Date().toDateString();
    if (state.lastStudyDate === today) return;
    const yesterday = new Date(Date.now() - 86400000).toDateString();
    if (state.lastStudyDate === yesterday) {
        state.streak++;
    } else if (state.lastStudyDate !== today) {
        state.streak = 1;
    }
    state.lastStudyDate = today;
    saveState();
}

window.loadState = loadState;
window.saveState = saveState;
window.updateStreak = updateStreak;