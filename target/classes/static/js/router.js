// ---- Router ----
function initRouter() {
    window.addEventListener('hashchange', () => {
        const page = location.hash.slice(1) || 'dashboard';
        navigateTo(page);
    });
    const hash = location.hash.slice(1);
    if (hash) navigateTo(hash);
}

function navigateTo(page) {
    state.currentPage = page;
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));

    const target = document.getElementById('page-' + page);
    if (!target) return;

    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    const activeLink = document.querySelector(`[data-page="${page}"]`);
    if (activeLink) activeLink.classList.add('active');

    location.hash = page;

    // Close mobile sidebar
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('sidebarOverlay').classList.remove('open');

    // Guest Route Protection
    const restricted = ['listening', 'speaking', 'reading', 'writing', 'flashcards', 'quiz', 'studyroom', 'roadmap'];
    const isRestricted = restricted.includes(page);

    if (isGuest() && isRestricted) {
        target.classList.add('active');

        // Ensure overlay exists
        let overlay = target.querySelector('.guest-lock-overlay');
        if (!overlay) {
            overlay = document.createElement('div');
            overlay.className = 'guest-lock-overlay';
            overlay.innerHTML = `
                <div class="guest-lock-icon">🔒</div>
                <div class="guest-lock-text">Tính năng yêu cầu đăng nhập</div>
                <div class="guest-lock-sub">Bạn cần tạo tài khoản để sử dụng tính năng này và lưu lại tiến độ học tập.</div>
                <button class="btn btn-primary" onclick="showAuthFromApp()" style="margin-top: 12px; position:relative; z-index:51;">Đăng nhập / Đăng ký</button>
            `;
            target.appendChild(overlay);
        }
    } else {
        // Remove overlay if logged in
        const overlay = target.querySelector('.guest-lock-overlay');
        if (overlay) overlay.remove();

        target.classList.add('active');

        // Render page content
        switch (page) {
            case 'dashboard': renderDashboard(); break;
            case 'hangul': renderHangul(); break;
            case 'vocabulary': renderVocabulary(); break;
            case 'grammar': renderGrammar(); break;
            case 'listening': initListening(); break;
            case 'speaking': initSpeaking(); break;
            case 'reading': initReading(); break;
            case 'writing': initWriting(); break;
            case 'flashcards': initFlashcards(); break;
            case 'quiz': resetQuizUI(); break;
        }
    }
}

window.initRouter = initRouter;
window.navigateTo = navigateTo;