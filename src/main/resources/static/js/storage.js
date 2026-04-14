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
    // Streak is now managed server-side by StreakService
    // Client-side streak tracking removed to avoid conflicts
    // Server updates streak on login and lesson completion
}

window.loadState = loadState;
window.saveState = saveState;
window.updateStreak = updateStreak;