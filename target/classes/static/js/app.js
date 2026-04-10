// ====== K-Learn Application Logic ======



// ---- Init ----
// In Spring Boot MVC, pages are rendered by Thymeleaf. No need to load partials.
document.addEventListener('DOMContentLoaded', () => {
    // 1. Lấy lessonId từ thuộc tính data của body (đã định nghĩa ở file HTML)
    const lessonId = document.body.getAttribute("data-lesson-id");

    // 2. Kiểm tra nếu đang ở trang Listening thì mới chạy loadListening
    if (document.getElementById("page-listening")) {
        if (lessonId) {
            loadListening(lessonId);
        } else {
            console.error("Không tìm thấy lessonId để tải bài nghe!");
        }
    }

    // Gọi các hàm khởi tạo chung khác của bạn
    initApp();
});

async function initApp() {

    // User info
    const nameEl = document.getElementById('profileDropdownName');
    const emailEl = document.getElementById('profileDropdownEmail');
    state._currentUser = {
        name: nameEl ? nameEl.textContent : 'Học viên',
        email: emailEl ? emailEl.textContent : '',
        role: 'user'
    };

    // Init client features
    loadState();
    updateStreak();
    initSidebar();
    initCanvas();
    renderDashboard();
    initStudyRoom();

    // Init page-specific
    const pageId = document.querySelector('.page.active');
    if (pageId) {
        const page = pageId.id.replace('page-', '');
        switch (page) {
            case 'hangul': renderHangul(); break;
            case 'vocabulary': renderVocabulary(); break;
            case 'grammar': renderGrammar(); break;
            case 'listening': initListening(); break;
            case 'speaking': initSpeaking(); break;
            case 'reading': initReading(); break;
            case 'writing': initWriting(); break;
            case 'flashcards': initFlashcards(); break;
            case 'quiz': resetQuizUI(); break;
            case 'roadmap': if(typeof renderRoadmap === 'function') renderRoadmap(); break;
        }
    }

    setInterval(() => {
        state.studyMinutes++;
        saveState();
    }, 60000);
}


// ---- TTS ----
function playKorean(text) {
    if (!('speechSynthesis' in window)) { showToast('Trình duyệt không hỗ trợ phát âm', 'error'); return; }
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




