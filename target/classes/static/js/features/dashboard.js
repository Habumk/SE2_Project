// ---- Dashboard ----
async function renderDashboard() {
    try {
        // ---- Elements ----
        const elStreakCount = document.getElementById('streakCount');
        const elHeaderStreak = document.getElementById('headerStreak');
        const elWordsLearned = document.getElementById('statWordsLearned');
        const elLessonsCompleted = document.getElementById('statLessonsCompleted');
        const elQuizScore = document.getElementById('statQuizScore');
        const elStudyTime = document.getElementById('statStudyTime');
        const elDwKorean = document.getElementById('dwKorean');
        const elDwRoman = document.getElementById('dwRomanization');
        const elDwMeaning = document.getElementById('dwMeaning');

        // ---- Stats ----
        if (elStreakCount) elStreakCount.textContent = state.streak;
        if (elHeaderStreak) elHeaderStreak.textContent = '🔥 ' + state.streak;
        if (elWordsLearned) elWordsLearned.textContent = state.vocabLearned.length;
        if (elLessonsCompleted) elLessonsCompleted.textContent = state.lessonsCompleted;

        const avgScore = state.quizScores.length
            ? Math.round(state.quizScores.reduce((a, b) => a + b, 0) / state.quizScores.length)
            : 0;
        if (elQuizScore) elQuizScore.textContent = avgScore + '%';

        const hours = Math.floor(state.studyMinutes / 60);
        if (elStudyTime) elStudyTime.textContent = hours > 0 ? hours + 'h' : state.studyMinutes + 'm';

        // ---- Daily word (random) ----
        if (elDwKorean && elDwRoman && elDwMeaning && DAILY_WORDS.length) {
            const randomIndex = Math.floor(Math.random() * DAILY_WORDS.length);
            const dw = DAILY_WORDS[randomIndex];
            elDwKorean.textContent = dw.kr;
            elDwRoman.textContent = dw.roman;
            elDwMeaning.textContent = dw.vi;
        }

        // ---- Hangul progress from DB ----
        // gọi API lấy tất cả từ vựng Hangul đã học
        const res = await fetch('/api/vocabulary/hangul'); // backend trả về JSON array [{hangul,...}]
        const hangulData = await res.json();
        const totalHangul = hangulData.length;
        const learnedHangul = state.hangulLearned.length;

        if (typeof setProgress === 'function') {
            const totalVocab = document.querySelectorAll('.vocab-item').length;

            setProgress('Hangul', learnedHangul, totalHangul);
            setProgress('Vocab', state.vocabLearned.length, totalVocab);
            setProgress('Grammar', Math.min(state.lessonsCompleted, 0), 0); // tạm thời
            setProgress('Listening', 0, 100);
            setProgress('Reading', 0, 100);
        }
    } catch (err) {
        console.error('Lỗi renderDashboard:', err);
    }
}

function setProgress(name, current, total) {
    const pct = total > 0 ? Math.round((current / total) * 100) : 0;
    const el = document.getElementById('progress' + name);
    const fill = document.getElementById('fill' + name);
    if (el) el.textContent = pct + '%';
    if (fill) fill.style.width = pct + '%';
}

window.renderDashboard = renderDashboard; // Make it globally accessible
window.setProgress = setProgress;

