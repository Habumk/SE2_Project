// ====== K-Learn Application Logic ======

// ---- State ----
let state = {
    currentPage: 'dashboard',
    hangulTab: 'consonants',
    hangulLearned: [],
    vocabLearned: [],
    streak: 0,
    lastStudyDate: null,
    lessonsCompleted: 0,
    quizScores: [],
    studyMinutes: 0,
    // Listening
    listeningIndex: 0,
    listeningQuestions: [],
    // Speaking
    speakingIndex: 0,
    isRecording: false,
    // Reading
    readingIndex: 0,
    readingAnswers: {},
    // Writing
    writingCharIndex: 0,
    writingTab: 'draw',
    translateIndex: 0,
    // Flashcard
    fcIndex: 0,
    fcDeck: [],
    fcCategory: 'all',
    fcResults: { easy: 0, medium: 0, hard: 0 },
    // Quiz
    quizType: '',
    quizQuestions: [],
    quizIndex: 0,
    quizCorrect: 0,
    // Canvas
    canvasHistory: [],
    isDrawing: false,
};

// ---- Init ----
// In Spring Boot MVC, pages are rendered by Thymeleaf. No need to load partials.
document.addEventListener('DOMContentLoaded', () => {
    initApp();
});

async function fetchInitialData() {
    try {
        const [hangul, vocab, categories, grammar, listening, speaking, reading] = await Promise.all([
            fetch('/api/hangul').then(r => r.json()),
            fetch('/api/vocab').then(r => r.json()),
            fetch('/api/vocab/categories').then(r => r.json()),
            fetch('/api/grammar').then(r => r.json()),
            fetch('/api/listening').then(r => r.json()),
            fetch('/api/speaking').then(r => r.json()),
            fetch('/api/reading').then(r => r.json())
        ]);
        
        HANGUL_DATA = hangul;
        VOCAB_DATA = vocab;
        CATEGORY_LABELS = categories;
        GRAMMAR_DATA = grammar;
        LISTENING_DATA = listening.map(l => ({...l, options: JSON.parse(l.options)}));
        SPEAKING_DATA = speaking;
        READING_DATA = reading.map(r => ({...r, questions: JSON.parse(r.questions)}));
        
        return true;
    } catch (e) {
        console.error("Lỗi khi tải dữ liệu từ API:", e);
        return false;
    }
}

async function initApp() {
    // Read user info from DOM (injected by Thymeleaf)
    const nameEl = document.getElementById('profileDropdownName');
    const emailEl = document.getElementById('profileDropdownEmail');
    state._currentUser = {
        name: nameEl ? nameEl.textContent : 'Học viên',
        email: emailEl ? emailEl.textContent : '',
        role: 'user'
    };

    // Show loading indicator
    const loadingToast = document.createElement('div');
    loadingToast.innerHTML = 'Đang tải dữ liệu học tập...';
    loadingToast.style.cssText = 'position:fixed; bottom:20px; left:50%; transform:translateX(-50%); background:#3b82f6; color:white; padding:10px 20px; border-radius:8px; z-index:9999;';
    document.body.appendChild(loadingToast);

    // Fetch data from API
    const dataLoaded = await fetchInitialData();
    loadingToast.remove();

    if (!dataLoaded) {
        showToast("Lỗi tải dữ liệu. Vui lòng tải lại trang.", "error");
        return;
    }

    // Init client-side features
    loadState();
    updateStreak();
    initSidebar();
    initCanvas();
    renderDashboard();
    initStudyRoom();

    // Init page-specific features based on current page
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

    setInterval(() => { state.studyMinutes++; saveState(); }, 60000);
}

// Auth tabs are in the separate login.html page (Thymeleaf)
// switchAuthTab is defined inline in auth/login.html

// Auth is now handled by Spring Security (server-side)
// handleLogin, handleRegister, handleLogout are NOT needed in JS

function handleLogout() {
    document.getElementById('logoutModal').style.display = 'flex';
}

function closeLogoutModal() {
    document.getElementById('logoutModal').style.display = 'none';
}

function isGuest() {
    return false; // No guest mode in Spring Boot version
}

// Profile Dropdown
function toggleProfileDropdown() {
    const dropdown = document.getElementById('profileDropdown');
    if (dropdown) dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
}

// Close dropdown when clicking outside
document.addEventListener('click', (e) => {
    const wrap = document.getElementById('headerAvatarWrap');
    const dropdown = document.getElementById('profileDropdown');
    if (wrap && dropdown && dropdown.style.display === 'block') {
        if (!wrap.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    }
});

// User UI is now rendered by Thymeleaf on the server side

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

// ---- Sidebar ----
function initSidebar() {
    document.getElementById('menuToggle').addEventListener('click', () => {
        document.getElementById('sidebar').classList.toggle('open');
        document.getElementById('sidebarOverlay').classList.toggle('open');
    });
    document.getElementById('sidebarOverlay').addEventListener('click', () => {
        document.getElementById('sidebar').classList.remove('open');
        document.getElementById('sidebarOverlay').classList.remove('open');
    });
}

// ---- Toast ----
function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<span>${type === 'success' ? '✅' : type === 'error' ? '❌' : 'ℹ️'}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => { toast.classList.add('toast-out'); setTimeout(() => toast.remove(), 300); }, 3000);
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

// ---- Dashboard ----
function renderDashboard() {
    document.getElementById('streakCount').textContent = state.streak;
    document.getElementById('headerStreak').textContent = '🔥 ' + state.streak;
    document.getElementById('statWordsLearned').textContent = state.vocabLearned.length;
    document.getElementById('statLessonsCompleted').textContent = state.lessonsCompleted;
    const avgScore = state.quizScores.length ? Math.round(state.quizScores.reduce((a, b) => a + b, 0) / state.quizScores.length) : 0;
    document.getElementById('statQuizScore').textContent = avgScore + '%';
    const hours = Math.floor(state.studyMinutes / 60);
    document.getElementById('statStudyTime').textContent = hours > 0 ? hours + 'h' : state.studyMinutes + 'm';

    // Daily word
    const dayIndex = new Date().getDate() % DAILY_WORDS.length;
    const dw = DAILY_WORDS[dayIndex];
    document.getElementById('dwKorean').textContent = dw.kr;
    document.getElementById('dwRomanization').textContent = dw.roman;
    document.getElementById('dwMeaning').textContent = dw.vi;

    // Progress
    const totalHangul = HANGUL_DATA.consonants.length + HANGUL_DATA.vowels.length + HANGUL_DATA.double.length + HANGUL_DATA.compound.length;
    setProgress('Hangul', state.hangulLearned.length, totalHangul);
    setProgress('Vocab', state.vocabLearned.length, VOCAB_DATA.length);
    setProgress('Grammar', Math.min(state.lessonsCompleted, GRAMMAR_DATA.length), GRAMMAR_DATA.length);
    setProgress('Listening', 0, 100);
    setProgress('Reading', 0, 100);
}

function setProgress(name, current, total) {
    const pct = total > 0 ? Math.round((current / total) * 100) : 0;
    const el = document.getElementById('progress' + name);
    const fill = document.getElementById('fill' + name);
    if (el) el.textContent = pct + '%';
    if (fill) fill.style.width = pct + '%';
}

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

// ---- Vocabulary ----
function renderVocabulary() {
    // Render category buttons
    const catContainer = document.getElementById('vocabCategories');
    catContainer.innerHTML = `<button class="category-btn active" data-category="all" onclick="filterVocab('all')">Tất cả</button>` +
        Object.entries(CATEGORY_LABELS).map(([key, label]) =>
            `<button class="category-btn" data-category="${key}" onclick="filterVocab('${key}')">${label}</button>`
        ).join('');
    filterVocab('all');
}

function filterVocab(category) {
    document.querySelectorAll('.category-btn').forEach(b => b.classList.toggle('active', b.dataset.category === category));
    const filtered = category === 'all' ? VOCAB_DATA : VOCAB_DATA.filter(v => v.category === category);
    const list = document.getElementById('vocabList');
    list.innerHTML = filtered.map((v, i) => {
        const learned = state.vocabLearned.includes(v.kr);
        return `<div class="vocab-item ${learned ? 'learned' : ''}">
            <span class="vocab-korean">${v.kr}</span>
            <span class="vocab-roman">${v.roman}</span>
            <span class="vocab-meaning">${v.vi}</span>
            <span class="vocab-category-tag">${CATEGORY_LABELS[v.category] || v.category}</span>
            <button class="vocab-speak-btn" onclick="event.stopPropagation(); speakKorean('${v.kr}')">🔊</button>
            <button class="vocab-learn-btn ${learned ? 'marked' : ''}" onclick="event.stopPropagation(); toggleVocabLearned('${v.kr}')">${learned ? '✅' : '📌'}</button>
        </div>`;
    }).join('');
}

function toggleVocabLearned(kr) {
    const idx = state.vocabLearned.indexOf(kr);
    if (idx >= 0) state.vocabLearned.splice(idx, 1);
    else state.vocabLearned.push(kr);
    saveState();
    filterVocab(document.querySelector('.category-btn.active')?.dataset.category || 'all');
}

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

// ---- Listening ----
function initListening() {
    state.listeningQuestions = shuffle([...LISTENING_DATA]).slice(0, 5);
    state.listeningIndex = 0;
    renderListeningQuestion();
}

function renderListeningQuestion() {
    const q = state.listeningQuestions[state.listeningIndex];
    if (!q) return;
    document.getElementById('listeningProgress').textContent = `Câu ${state.listeningIndex + 1}/${state.listeningQuestions.length}`;
    document.getElementById('listeningProgressBar').style.width = ((state.listeningIndex + 1) / state.listeningQuestions.length * 100) + '%';
    document.getElementById('listeningResult').style.display = 'none';
    document.getElementById('listeningNextBtn').style.display = 'none';
    document.getElementById('listeningHint').textContent = 'Nghe câu và chọn nghĩa đúng';
    const shuffled = shuffle([...q.options]);
    document.getElementById('listeningAnswers').innerHTML = shuffled.map((opt, i) =>
        `<button class="answer-option" onclick="checkListeningAnswer(this, '${opt.replace(/'/g, "\\'")}')">
            <span class="answer-letter">${String.fromCharCode(65 + i)}</span><span>${opt}</span>
        </button>`
    ).join('');
}

function playListeningAudio() {
    const q = state.listeningQuestions[state.listeningIndex];
    if (q) speakKorean(q.text);
}

function checkListeningAnswer(el, answer) {
    const q = state.listeningQuestions[state.listeningIndex];
    const correct = answer === q.answer;
    document.querySelectorAll('.answer-option').forEach(o => {
        o.classList.add('disabled');
        if (o.querySelector('span:last-child').textContent === q.answer) o.classList.add('correct');
    });
    if (!correct) el.classList.add('incorrect');
    document.getElementById('listeningResult').style.display = 'block';
    document.getElementById('listeningResultIcon').textContent = correct ? '✅' : '❌';
    document.getElementById('listeningResultText').textContent = correct ? 'Chính xác!' : 'Sai rồi!';
    document.getElementById('listeningResultExplanation').textContent = `"${q.text}" = ${q.answer}`;
    document.getElementById('listeningNextBtn').style.display = 'inline-flex';
}

function nextListeningQuestion() {
    state.listeningIndex++;
    if (state.listeningIndex >= state.listeningQuestions.length) {
        showToast('Hoàn thành bài nghe! 🎉', 'success');
        initListening();
    } else {
        renderListeningQuestion();
    }
}

// ---- Speaking ----
function initSpeaking() {
    state.speakingIndex = Math.floor(Math.random() * SPEAKING_DATA.length);
    renderSpeakingPrompt();
}

function renderSpeakingPrompt() {
    const s = SPEAKING_DATA[state.speakingIndex % SPEAKING_DATA.length];
    document.getElementById('speakingTextKr').textContent = s.kr;
    document.getElementById('speakingTextRoman').textContent = s.roman;
    document.getElementById('speakingTextVi').textContent = s.vi;
    document.getElementById('speakingResult').style.display = 'none';
    document.getElementById('recordingIndicator').style.display = 'none';
    document.getElementById('recordBtn').classList.remove('recording');
    document.getElementById('recordText').textContent = 'Nhấn để ghi âm';
}

function nextSpeakingPrompt() {
    state.speakingIndex = (state.speakingIndex + 1) % SPEAKING_DATA.length;
    renderSpeakingPrompt();
}

function toggleRecording() {
    if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
        showToast('Trình duyệt không hỗ trợ nhận diện giọng nói. Hãy dùng Chrome!', 'error');
        return;
    }
    if (state.isRecording) {
        state.recognition.stop();
        return;
    }
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    state.recognition = new SpeechRecognition();
    state.recognition.lang = 'ko-KR';
    state.recognition.interimResults = false;
    state.recognition.onstart = () => {
        state.isRecording = true;
        document.getElementById('recordBtn').classList.add('recording');
        document.getElementById('recordText').textContent = 'Đang nghe...';
        document.getElementById('recordingIndicator').style.display = 'flex';
    };
    state.recognition.onresult = (event) => {
        const result = event.results[0][0].transcript;
        const target = SPEAKING_DATA[state.speakingIndex % SPEAKING_DATA.length].kr;
        document.getElementById('speakingResultText').textContent = result;
        document.getElementById('speakingResult').style.display = 'block';
        const similarity = calculateSimilarity(result, target);
        const scoreEl = document.getElementById('speakingScore');
        if (similarity > 0.7) { scoreEl.innerHTML = `<span style="color:var(--success)">🎉 Tuyệt vời! (${Math.round(similarity * 100)}%)</span>`; }
        else if (similarity > 0.4) { scoreEl.innerHTML = `<span style="color:var(--warning)">👍 Khá tốt! (${Math.round(similarity * 100)}%)</span>`; }
        else { scoreEl.innerHTML = `<span style="color:var(--danger)">💪 Thử lại nhé! (${Math.round(similarity * 100)}%)</span>`; }
    };
    state.recognition.onend = () => {
        state.isRecording = false;
        document.getElementById('recordBtn').classList.remove('recording');
        document.getElementById('recordText').textContent = 'Nhấn để ghi âm';
        document.getElementById('recordingIndicator').style.display = 'none';
    };
    state.recognition.onerror = () => {
        state.isRecording = false;
        document.getElementById('recordBtn').classList.remove('recording');
        document.getElementById('recordText').textContent = 'Nhấn để ghi âm';
        document.getElementById('recordingIndicator').style.display = 'none';
        showToast('Không nhận diện được. Hãy thử lại!', 'error');
    };
    state.recognition.start();
}

function calculateSimilarity(a, b) {
    a = a.replace(/\s/g, ''); b = b.replace(/\s/g, '');
    if (a === b) return 1;
    const longer = a.length > b.length ? a : b;
    const shorter = a.length > b.length ? b : a;
    if (longer.length === 0) return 1;
    let matches = 0;
    for (let i = 0; i < shorter.length; i++) { if (longer.includes(shorter[i])) matches++; }
    return matches / longer.length;
}

// ---- Reading ----
function initReading() {
    state.readingIndex = state.readingIndex % READING_DATA.length;
    state.readingAnswers = {};
    renderReadingPassage();
}

function renderReadingPassage() {
    const r = READING_DATA[state.readingIndex];
    document.getElementById('readingLevel').textContent = 'Cấp độ: ' + r.level;
    document.getElementById('readingTextKr').textContent = r.text;
    document.getElementById('readingTextVi').textContent = r.translation;
    document.getElementById('readingTextVi').style.display = 'none';
    document.getElementById('readingToggleTranslation').textContent = '👁️ Hiện bản dịch';
    document.getElementById('readingCheckBtn').style.display = 'inline-flex';
    document.getElementById('readingNextBtn').style.display = 'none';

    document.getElementById('readingQuestions').innerHTML = r.questions.map((q, qi) => `
        <div class="reading-question">
            <p>${qi + 1}. ${q.q}</p>
            <div class="reading-options">
                ${q.options.map((opt, oi) => `
                    <button class="reading-option" data-qi="${qi}" data-oi="${oi}" onclick="selectReadingOption(${qi}, ${oi})">${opt}</button>
                `).join('')}
            </div>
        </div>
    `).join('');
}

function toggleReadingTranslation() {
    const el = document.getElementById('readingTextVi');
    const btn = document.getElementById('readingToggleTranslation');
    if (el.style.display === 'none') { el.style.display = 'block'; btn.textContent = '🙈 Ẩn bản dịch'; }
    else { el.style.display = 'none'; btn.textContent = '👁️ Hiện bản dịch'; }
}

function selectReadingOption(qi, oi) {
    state.readingAnswers[qi] = oi;
    document.querySelectorAll(`[data-qi="${qi}"]`).forEach(o => o.classList.remove('selected'));
    document.querySelector(`[data-qi="${qi}"][data-oi="${oi}"]`).classList.add('selected');
}

function checkReadingAnswers() {
    const r = READING_DATA[state.readingIndex];
    let correct = 0;
    r.questions.forEach((q, qi) => {
        document.querySelectorAll(`[data-qi="${qi}"]`).forEach(o => {
            const oi = parseInt(o.dataset.oi);
            if (oi === q.answer) o.classList.add('correct');
            if (state.readingAnswers[qi] === oi && oi !== q.answer) o.classList.add('incorrect');
            o.style.pointerEvents = 'none';
        });
        if (state.readingAnswers[qi] === q.answer) correct++;
    });
    showToast(`Kết quả: ${correct}/${r.questions.length} câu đúng`, correct === r.questions.length ? 'success' : 'info');
    document.getElementById('readingCheckBtn').style.display = 'none';
    document.getElementById('readingNextBtn').style.display = 'inline-flex';
}

function nextReadingPassage() {
    state.readingIndex = (state.readingIndex + 1) % READING_DATA.length;
    state.readingAnswers = {};
    renderReadingPassage();
}

// ---- Writing ----
function initWriting() {
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
    const char = WRITING_CHARS[state.writingCharIndex % WRITING_CHARS.length];
    document.getElementById('writingTargetChar').textContent = char;
    document.getElementById('writingTargetInfo').textContent = '';
    clearCanvas();
}

function nextWritingChar() {
    state.writingCharIndex = (state.writingCharIndex + 1) % WRITING_CHARS.length;
    renderWritingChar();
}

function renderTranslation() {
    const t = TRANSLATE_DATA[state.translateIndex % TRANSLATE_DATA.length];
    document.getElementById('translateSource').textContent = t.vi;
    document.getElementById('translateInput').value = '';
    document.getElementById('translateResult').style.display = 'none';
}

function checkTranslation() {
    const t = TRANSLATE_DATA[state.translateIndex % TRANSLATE_DATA.length];
    const input = document.getElementById('translateInput').value.trim();
    document.getElementById('translateAnswer').textContent = t.kr;
    document.getElementById('translateResult').style.display = 'block';
    if (input === t.kr) showToast('Chính xác! 🎉', 'success');
    else showToast('Xem đáp án bên dưới', 'info');
}

function nextTranslation() {
    state.translateIndex = (state.translateIndex + 1) % TRANSLATE_DATA.length;
    renderTranslation();
}

// ---- Canvas ----
function initCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const rect = canvas.getBoundingClientRect();

    function getPos(e) {
        const touch = e.touches ? e.touches[0] : e;
        const r = canvas.getBoundingClientRect();
        return { x: (touch.clientX - r.left) * (canvas.width / r.width), y: (touch.clientY - r.top) * (canvas.height / r.height) };
    }

    function startDraw(e) {
        e.preventDefault();
        state.isDrawing = true;
        const pos = getPos(e);
        ctx.beginPath();
        ctx.moveTo(pos.x, pos.y);
    }

    function draw(e) {
        if (!state.isDrawing) return;
        e.preventDefault();
        const pos = getPos(e);
        ctx.lineWidth = 4;
        ctx.lineCap = 'round';
        ctx.strokeStyle = '#60a5fa';
        ctx.lineTo(pos.x, pos.y);
        ctx.stroke();
    }

    function endDraw(e) {
        if (state.isDrawing) {
            state.isDrawing = false;
            state.canvasHistory.push(canvas.toDataURL());
        }
    }

    canvas.addEventListener('mousedown', startDraw);
    canvas.addEventListener('mousemove', draw);
    canvas.addEventListener('mouseup', endDraw);
    canvas.addEventListener('mouseleave', endDraw);
    canvas.addEventListener('touchstart', startDraw, { passive: false });
    canvas.addEventListener('touchmove', draw, { passive: false });
    canvas.addEventListener('touchend', endDraw);
}

function clearCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    state.canvasHistory = [];
}

function undoCanvas() {
    const canvas = document.getElementById('writingCanvas');
    if (!canvas || state.canvasHistory.length === 0) return;
    state.canvasHistory.pop();
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    if (state.canvasHistory.length > 0) {
        const img = new Image();
        img.onload = () => ctx.drawImage(img, 0, 0);
        img.src = state.canvasHistory[state.canvasHistory.length - 1];
    }
}

// ---- Flashcards ----
function initFlashcards() {
    // Render category buttons
    const catContainer = document.getElementById('flashcardCategories');
    catContainer.innerHTML = `<button class="category-btn active" data-category="all" onclick="filterFlashcards('all')">Tất cả</button>` +
        Object.entries(CATEGORY_LABELS).map(([key, label]) =>
            `<button class="category-btn" data-category="${key}" onclick="filterFlashcards('${key}')">${label}</button>`
        ).join('');
    filterFlashcards('all');
}

function filterFlashcards(category) {
    state.fcCategory = category;
    document.querySelectorAll('#flashcardCategories .category-btn').forEach(b => b.classList.toggle('active', b.dataset.category === category));
    state.fcDeck = shuffle(category === 'all' ? [...VOCAB_DATA] : VOCAB_DATA.filter(v => v.category === category));
    state.fcIndex = 0;
    state.fcResults = { easy: 0, medium: 0, hard: 0 };
    document.getElementById('flashcardSummary').style.display = 'none';
    document.querySelector('.flashcard-container').style.display = 'block';
    renderFlashcard();
}

function renderFlashcard() {
    if (state.fcIndex >= state.fcDeck.length) { showFlashcardSummary(); return; }
    const card = state.fcDeck[state.fcIndex];
    document.getElementById('fcKorean').textContent = card.kr;
    document.getElementById('fcRomanization').textContent = card.roman;
    document.getElementById('fcMeaning').textContent = card.vi;
    document.getElementById('fcExample').textContent = card.example || '';
    document.getElementById('flashcardCount').textContent = `${state.fcIndex + 1} / ${state.fcDeck.length}`;
    document.getElementById('flashcard').classList.remove('flipped');
}

function flipFlashcard() {
    document.getElementById('flashcard').classList.toggle('flipped');
}

function flashcardAction(difficulty) {
    state.fcResults[difficulty]++;
    state.fcIndex++;
    renderFlashcard();
}

function showFlashcardSummary() {
    document.querySelector('.flashcard-container').style.display = 'none';
    document.getElementById('flashcardSummary').style.display = 'block';
    document.getElementById('fcEasyCount').textContent = state.fcResults.easy;
    document.getElementById('fcMediumCount').textContent = state.fcResults.medium;
    document.getElementById('fcHardCount').textContent = state.fcResults.hard;
}

function restartFlashcards() { filterFlashcards(state.fcCategory); }

// ---- Quiz ----
function resetQuizUI() {
    document.getElementById('quizSetup').style.display = 'block';
    document.getElementById('quizInProgress').style.display = 'none';
    document.getElementById('quizSummary').style.display = 'none';
}

function startQuiz(type) {
    state.quizType = type;
    state.quizIndex = 0;
    state.quizCorrect = 0;
    state.quizQuestions = generateQuizQuestions(type);
    document.getElementById('quizSetup').style.display = 'none';
    document.getElementById('quizInProgress').style.display = 'block';
    document.getElementById('quizSummary').style.display = 'none';
    renderQuizQuestion();
}

function generateQuizQuestions(type) {
    let questions = [];
    const vocabPool = shuffle([...VOCAB_DATA]);

    if (type === 'vocab' || type === 'mixed') {
        vocabPool.slice(0, 5).forEach(v => {
            const wrongAnswers = shuffle(VOCAB_DATA.filter(x => x.kr !== v.kr)).slice(0, 3).map(x => x.vi);
            questions.push({
                text: `"${v.kr}" có nghĩa là gì?`,
                options: shuffle([v.vi, ...wrongAnswers]),
                answer: v.vi,
                audio: null
            });
        });
    }
    if (type === 'hangul' || type === 'mixed') {
        const allHangul = [...HANGUL_DATA.consonants, ...HANGUL_DATA.vowels];
        shuffle(allHangul).slice(0, type === 'mixed' ? 2 : 5).forEach(h => {
            const wrongAnswers = shuffle(allHangul.filter(x => x.char !== h.char)).slice(0, 3).map(x => x.roman);
            questions.push({
                text: `Ký tự "${h.char}" phát âm là gì?`,
                options: shuffle([h.roman, ...wrongAnswers]),
                answer: h.roman,
                audio: null
            });
        });
    }
    if (type === 'listening' || type === 'mixed') {
        shuffle([...LISTENING_DATA]).slice(0, type === 'mixed' ? 2 : 5).forEach(l => {
            questions.push({
                text: 'Nghe và chọn nghĩa đúng:',
                options: shuffle([...l.options]),
                answer: l.answer,
                audio: l.text
            });
        });
    }
    if (type === 'grammar' || type === 'mixed') {
        const grammarQuiz = [
            { text: 'Điền vào chỗ trống: 저는 학생___. (Tôi là học sinh)', options: ['입니다', '합니다', '있습니다', '없습니다'], answer: '입니다' },
            { text: 'Điền trợ từ: 밥___ 먹어요. (Ăn cơm)', options: ['을', '를', '이', '가'], answer: '을' },
            { text: 'Điền vào: 학교___ 가요. (Đi đến trường)', options: ['에', '을', '는', '가'], answer: '에' },
        ];
        questions.push(...shuffle(grammarQuiz).slice(0, type === 'mixed' ? 1 : 3).map(q => ({ ...q, audio: null })));
    }
    return shuffle(questions).slice(0, 10);
}

function renderQuizQuestion() {
    const q = state.quizQuestions[state.quizIndex];
    if (!q) return;
    document.getElementById('quizQuestionNum').textContent = `Câu ${state.quizIndex + 1}/${state.quizQuestions.length}`;
    document.getElementById('quizScore').textContent = `Điểm: ${state.quizCorrect}`;
    document.getElementById('quizProgressBar').style.width = ((state.quizIndex + 1) / state.quizQuestions.length * 100) + '%';
    document.getElementById('quizQText').textContent = q.text;
    document.getElementById('quizQAudio').style.display = q.audio ? 'block' : 'none';
    document.getElementById('quizResult').style.display = 'none';
    document.getElementById('quizNextBtn').style.display = 'none';

    document.getElementById('quizAnswers').innerHTML = q.options.map((opt, i) =>
        `<button class="quiz-answer" onclick="checkQuizAnswer(this, '${opt.replace(/'/g, "\\'")}')">
            <span class="answer-letter">${String.fromCharCode(65 + i)}</span><span>${opt}</span>
        </button>`).join('');
}

function playQuizAudio() {
    const q = state.quizQuestions[state.quizIndex];
    if (q && q.audio) speakKorean(q.audio);
}

function checkQuizAnswer(el, answer) {
    const q = state.quizQuestions[state.quizIndex];
    const correct = answer === q.answer;
    if (correct) state.quizCorrect++;

    document.querySelectorAll('.quiz-answer').forEach(o => {
        o.classList.add('disabled');
        if (o.querySelector('span:last-child').textContent === q.answer) o.classList.add('correct');
    });
    if (!correct) el.classList.add('incorrect');

    document.getElementById('quizResult').style.display = 'block';
    document.getElementById('quizResultIcon').textContent = correct ? '✅' : '❌';
    document.getElementById('quizResultText').textContent = correct ? 'Chính xác!' : `Sai! Đáp án: ${q.answer}`;
    document.getElementById('quizScore').textContent = `Điểm: ${state.quizCorrect}`;
    document.getElementById('quizNextBtn').style.display = 'inline-flex';
}

function nextQuizQuestion() {
    state.quizIndex++;
    if (state.quizIndex >= state.quizQuestions.length) {
        showQuizSummary();
    } else {
        renderQuizQuestion();
    }
}

function showQuizSummary() {
    const pct = Math.round((state.quizCorrect / state.quizQuestions.length) * 100);
    state.quizScores.push(pct);
    saveState();

    document.getElementById('quizInProgress').style.display = 'none';
    document.getElementById('quizSummary').style.display = 'flex';
    document.getElementById('qsScoreText').textContent = pct + '%';
    document.getElementById('qsDetail').textContent = `${state.quizCorrect}/${state.quizQuestions.length} câu đúng`;

    if (pct >= 80) { document.getElementById('qsSummaryIcon').textContent = '🎉'; document.getElementById('qsSummaryTitle').textContent = 'Tuyệt vời!'; }
    else if (pct >= 50) { document.getElementById('qsSummaryIcon').textContent = '👍'; document.getElementById('qsSummaryTitle').textContent = 'Khá tốt!'; }
    else { document.getElementById('qsSummaryIcon').textContent = '💪'; document.getElementById('qsSummaryTitle').textContent = 'Cố gắng thêm!'; }

    // Animate score circle
    const circle = document.getElementById('scoreCircle');
    const circumference = 2 * Math.PI * 54;
    const offset = circumference - (pct / 100) * circumference;
    setTimeout(() => { circle.style.transition = 'stroke-dashoffset 1s ease'; circle.style.strokeDashoffset = offset; }, 100);
}

function restartQuiz() { startQuiz(state.quizType); }
function backToQuizSetup() { resetQuizUI(); }

// ---- Utils ----
function shuffle(arr) {
    const a = [...arr];
    for (let i = a.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
}

// ======================================================
//  STUDY ROOM — Online Room Feature (BroadcastChannel)
// ======================================================

let roomChannel = null;
let currentRoom = null;
let roomFcIndex = 0;
let roomFcDeck = [];
let roomQuizIndex = 0;
let roomQuizData = [];
let roomQuizScore = 0;

function generateRoomCode() {
    return Math.random().toString(36).substring(2, 8).toUpperCase();
}

function getRooms() {
    return JSON.parse(localStorage.getItem('klearn_rooms') || '[]');
}

function saveRooms(rooms) {
    localStorage.setItem('klearn_rooms', JSON.stringify(rooms));
}

function initStudyRoom() {
    renderRoomList();
    // Listen for storage events (cross-tab room updates)
    window.addEventListener('storage', (e) => {
        if (e.key === 'klearn_rooms') renderRoomList();
    });
}

function renderRoomList() {
    const rooms = getRooms();
    const grid = document.getElementById('roomGrid');
    const empty = document.getElementById('roomEmpty');
    if (!grid) return;

    if (rooms.length === 0) {
        grid.innerHTML = '';
        empty.style.display = 'block';
        return;
    }
    empty.style.display = 'none';

    const modeLabels = { chat: '💬 Chat', flashcards: '🃏 Flashcards', quiz: '🏆 Quiz' };
    grid.innerHTML = rooms.map(r => `
        <div class="room-card" onclick="joinRoom('${r.id}')">
            <div class="room-card-header">
                <span class="room-card-name">${r.name}</span>
                <span class="room-card-mode">${modeLabels[r.mode] || r.mode}</span>
            </div>
            <div class="room-card-desc">${r.desc || 'Không có mô tả'}</div>
            <div class="room-card-footer">
                <span class="room-card-members">👥 ${(r.members || []).length} thành viên</span>
                <span class="room-card-code">${r.id}</span>
            </div>
        </div>
    `).join('');
}

function openCreateRoomModal() {
    document.getElementById('createRoomModal').style.display = 'flex';
}

function closeCreateRoomModal() {
    document.getElementById('createRoomModal').style.display = 'none';
}

function createRoom(e) {
    e.preventDefault();
    const name = document.getElementById('roomName').value.trim();
    const desc = document.getElementById('roomDesc').value.trim();
    const mode = document.querySelector('input[name="roomMode"]:checked').value;
    if (!name) return;

    const user = state._currentUser || { name: 'Học viên', email: 'guest' };
    const room = {
        id: generateRoomCode(),
        name,
        desc,
        mode,
        owner: user.email,
        ownerName: user.name,
        members: [{ name: user.name, email: user.email }],
        messages: [],
        createdAt: Date.now()
    };

    const rooms = getRooms();
    rooms.push(room);
    saveRooms(rooms);

    // Reset form
    document.getElementById('roomName').value = '';
    document.getElementById('roomDesc').value = '';
    closeCreateRoomModal();

    showToast(`Phòng "${name}" đã được tạo! Mã: ${room.id}`);
    joinRoom(room.id);
}

function joinRoom(roomId) {
    const rooms = getRooms();
    const room = rooms.find(r => r.id === roomId);
    if (!room) {
        showToast('Không tìm thấy phòng!');
        return;
    }

    const user = state._currentUser || { name: 'Học viên', email: 'guest' };

    // Add member if not already in
    if (!room.members.find(m => m.email === user.email)) {
        room.members.push({ name: user.name, email: user.email });
        room.messages.push({
            type: 'system',
            text: `${user.name} đã tham gia phòng`,
            time: Date.now()
        });
        saveRooms(rooms);
    }

    currentRoom = room;

    // Setup BroadcastChannel for this room
    if (roomChannel) roomChannel.close();
    roomChannel = new BroadcastChannel(`klearn_room_${roomId}`);
    roomChannel.onmessage = handleRoomMessage;

    // Broadcast join
    roomChannel.postMessage({ type: 'member_join', user: { name: user.name, email: user.email } });

    // Show room view
    document.getElementById('roomListView').style.display = 'none';
    document.getElementById('roomView').style.display = 'block';

    // Populate room info
    document.getElementById('roomViewName').textContent = room.name;
    document.getElementById('roomViewCode').textContent = room.id;
    document.getElementById('roomCodeCopy').textContent = room.id + ' 📋';
    document.getElementById('roomOwnerName').textContent = room.ownerName;
    document.getElementById('roomModeName').textContent = { chat: 'Trò chuyện', flashcards: 'Flashcards', quiz: 'Quiz' }[room.mode] || room.mode;
    document.getElementById('roomDescText').textContent = room.desc || '';

    // Show delete button only for owner
    const deleteBtn = document.getElementById('deleteRoomBtn');
    deleteBtn.style.display = (room.owner === user.email) ? 'inline-flex' : 'none';

    renderMembers();
    renderChatMessages();

    // Switch to default tab based on room mode
    switchRoomTab(room.mode === 'chat' ? 'chat' : room.mode);
}

function joinRoomByCode() {
    const code = document.getElementById('joinRoomCode').value.trim().toUpperCase();
    if (!code) return;
    joinRoom(code);
    document.getElementById('joinRoomCode').value = '';
}

function leaveRoom() {
    if (!currentRoom) return;
    const user = state._currentUser || { name: 'Học viên', email: 'guest' };

    // Stop Media Streams
    if (localStream) {
        localStream.getTracks().forEach(t => t.stop());
        localStream = null;
    }
    isVideoOn = false;
    isMicOn = false;
    const vbtn = document.getElementById('toggleVideoBtn');
    const mbtn = document.getElementById('toggleMicBtn');
    if(vbtn) { vbtn.textContent = '🎥 Bật Camera'; vbtn.className = 'btn btn-sm btn-secondary'; }
    if(mbtn) { mbtn.textContent = '🎤 Bật Mic'; mbtn.className = 'btn btn-sm btn-secondary'; }
    removeLocalVideo();

    // Clear interval timers
    if(fcTimerInterval) clearInterval(fcTimerInterval);
    if(quizTimerInterval) clearInterval(quizTimerInterval);

    // Remove member from room
    const rooms = getRooms();
    const room = rooms.find(r => r.id === currentRoom.id);
    if (room) {
        room.members = room.members.filter(m => m.email !== user.email);
        room.messages.push({
            type: 'system',
            text: `${user.name} đã rời phòng`,
            time: Date.now()
        });
        saveRooms(rooms);
    }

    // Broadcast leave
    if (roomChannel) {
        roomChannel.postMessage({ type: 'member_leave', user: { name: user.name, email: user.email } });
        roomChannel.close();
        roomChannel = null;
    }

    currentRoom = null;
    document.getElementById('roomListView').style.display = 'block';
    document.getElementById('roomView').style.display = 'none';
    renderRoomList();
}

function deleteRoom() {
    if (!currentRoom) return;
    const user = state._currentUser || { email: 'guest' };
    if (currentRoom.owner !== user.email) return;

    if (!confirm('Bạn có chắc muốn xoá phòng này?')) return;

    const rooms = getRooms().filter(r => r.id !== currentRoom.id);
    saveRooms(rooms);

    if (roomChannel) {
        roomChannel.postMessage({ type: 'room_deleted' });
        roomChannel.close();
        roomChannel = null;
    }

    currentRoom = null;
    document.getElementById('roomListView').style.display = 'block';
    document.getElementById('roomView').style.display = 'none';
    renderRoomList();
    showToast('Phòng đã được xoá');
}

function copyRoomCode() {
    if (!currentRoom) return;
    navigator.clipboard.writeText(currentRoom.id).then(() => {
        showToast('Đã sao chép mã phòng: ' + currentRoom.id);
    });
}

// ---- BroadcastChannel message handler ----
function handleRoomMessage(e) {
    const data = e.data;
    switch (data.type) {
        case 'chat_message':
            appendChatMessage(data.message);
            break;
        case 'member_join':
        case 'member_leave':
            // Refresh room data
            const rooms = getRooms();
            const room = rooms.find(r => r.id === currentRoom.id);
            if (room) {
                currentRoom = room;
                renderMembers();
            }
            if (data.type === 'member_join') {
                appendSystemMessage(`${data.user.name} đã tham gia phòng`);
            } else {
                appendSystemMessage(`${data.user.name} đã rời phòng`);
            }
            break;
        case 'room_deleted':
            showToast('Phòng đã bị xoá bởi chủ phòng');
            currentRoom = null;
            document.getElementById('roomListView').style.display = 'block';
            document.getElementById('roomView').style.display = 'none';
            renderRoomList();
            break;
        case 'quiz_score':
            // Update leaderboard with other player's score
            updateLeaderboard(data.user, data.score, data.total);
            break;
    }
}

// ---- Room Tabs ----
function switchRoomTab(tab) {
    document.querySelectorAll('.room-tab').forEach(t => t.classList.remove('active'));
    document.querySelector(`.room-tab[data-room-tab="${tab}"]`).classList.add('active');
    document.querySelectorAll('.room-tab-content').forEach(c => { c.classList.remove('active'); c.style.display = 'none'; });
    const content = document.getElementById(`room${tab.charAt(0).toUpperCase() + tab.slice(1)}Tab`);
    content.style.display = 'flex';
    content.classList.add('active');
}

// ---- Chat ----
function renderChatMessages() {
    const el = document.getElementById('chatMessages');
    if (!currentRoom || !currentRoom.messages || currentRoom.messages.length === 0) {
        el.innerHTML = '<div class="chat-welcome"><span>👋</span><p>Chào mừng đến phòng học! Hãy bắt đầu trò chuyện.</p></div>';
        return;
    }
    const user = state._currentUser || { email: 'guest' };
    el.innerHTML = currentRoom.messages.map(m => {
        if (m.type === 'system') {
            return `<div class="chat-system">${m.text}</div>`;
        }
        const isOwn = m.email === user.email;
        const initial = (m.name || '?').charAt(0).toUpperCase();
        const time = new Date(m.time).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
        return `<div class="chat-message ${isOwn ? 'own' : ''}">
            <div class="chat-avatar">${initial}</div>
            <div class="chat-bubble">
                ${!isOwn ? `<div class="chat-bubble-name">${m.name}</div>` : ''}
                ${m.text}
                <div class="chat-bubble-time">${time}</div>
            </div>
        </div>`;
    }).join('');
    el.scrollTop = el.scrollHeight;
}

function sendMessage() {
    const input = document.getElementById('chatInput');
    const text = input.value.trim();
    if (!text || !currentRoom) return;

    const user = state._currentUser || { name: 'Học viên', email: 'guest' };
    const msg = { name: user.name, email: user.email, text, time: Date.now() };

    // Save to localStorage
    const rooms = getRooms();
    const room = rooms.find(r => r.id === currentRoom.id);
    if (room) {
        room.messages.push(msg);
        saveRooms(rooms);
        currentRoom = room;
    }

    appendChatMessage(msg);
    input.value = '';

    // Broadcast to other tabs
    if (roomChannel) roomChannel.postMessage({ type: 'chat_message', message: msg });
}

function appendChatMessage(msg) {
    const el = document.getElementById('chatMessages');
    // Remove welcome message if present
    const welcome = el.querySelector('.chat-welcome');
    if (welcome) welcome.remove();

    const user = state._currentUser || { email: 'guest' };
    const isOwn = msg.email === user.email;
    const initial = (msg.name || '?').charAt(0).toUpperCase();
    const time = new Date(msg.time).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });

    const div = document.createElement('div');
    div.className = `chat-message ${isOwn ? 'own' : ''}`;
    div.innerHTML = `
        <div class="chat-avatar">${initial}</div>
        <div class="chat-bubble">
            ${!isOwn ? `<div class="chat-bubble-name">${msg.name}</div>` : ''}
            ${msg.text}
            <div class="chat-bubble-time">${time}</div>
        </div>
    `;
    el.appendChild(div);
    el.scrollTop = el.scrollHeight;
}

function appendSystemMessage(text) {
    const el = document.getElementById('chatMessages');
    const welcome = el.querySelector('.chat-welcome');
    if (welcome) welcome.remove();
    const div = document.createElement('div');
    div.className = 'chat-system';
    div.textContent = text;
    el.appendChild(div);
    el.scrollTop = el.scrollHeight;
}

function renderMembers() {
    if (!currentRoom) return;
    const list = document.getElementById('roomMembersList');
    document.getElementById('roomMemberCount').textContent = currentRoom.members.length;
    list.innerHTML = currentRoom.members.map(m => {
        const initial = (m.name || '?').charAt(0).toUpperCase();
        const isOwner = m.email === currentRoom.owner;
        return `<div class="room-member">
            <div class="room-member-avatar">${initial}</div>
            <span class="room-member-name">${m.name}</span>
            ${isOwner ? '<span class="room-member-badge">Chủ phòng</span>' : ''}
            <div class="room-member-status"></div>
        </div>`;
    }).join('');
}

// ---- Mic / Video (MediaStream API) ----
let localStream = null;
let isVideoOn = false;
let isMicOn = false;

async function toggleVideo() {
    const btn = document.getElementById('toggleVideoBtn');
    if (!isVideoOn) {
        try {
            if(!localStream) localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: isMicOn });
            else {
                const vs = await navigator.mediaDevices.getUserMedia({ video: true });
                vs.getVideoTracks().forEach(t => localStream.addTrack(t));
            }
            isVideoOn = true;
            btn.textContent = '🚫 Tắt Camera';
            btn.classList.replace('btn-secondary', 'btn-primary');
            showLocalVideo();
        } catch(e) {
            showToast('Lỗi truy cập Camera', 'error');
        }
    } else {
        if(localStream) {
            localStream.getVideoTracks().forEach(t => { t.stop(); localStream.removeTrack(t); });
        }
        isVideoOn = false;
        btn.textContent = '🎥 Bật Camera';
        btn.classList.replace('btn-primary', 'btn-secondary');
        removeLocalVideo();
    }
}

async function toggleMic() {
    const btn = document.getElementById('toggleMicBtn');
    if (!isMicOn) {
        try {
            if(!localStream) localStream = await navigator.mediaDevices.getUserMedia({ audio: true, video: isVideoOn });
            else {
                const as = await navigator.mediaDevices.getUserMedia({ audio: true });
                as.getAudioTracks().forEach(t => localStream.addTrack(t));
            }
            isMicOn = true;
            btn.textContent = '🔇 Tắt Mic';
            btn.classList.replace('btn-secondary', 'btn-danger');
        } catch(e) {
            showToast('Lỗi truy cập Mic', 'error');
        }
    } else {
        if(localStream) {
            localStream.getAudioTracks().forEach(t => { t.stop(); localStream.removeTrack(t); });
        }
        isMicOn = false;
        btn.textContent = '🎤 Bật Mic';
        btn.classList.replace('btn-danger', 'btn-secondary');
    }
}

function showLocalVideo() {
    const grid = document.getElementById('roomVideoGrid');
    grid.style.display = 'grid';
    let container = document.getElementById('localVideoContainer');
    if(!container) {
        container = document.createElement('div');
        container.id = 'localVideoContainer';
        container.className = 'video-container';
        container.innerHTML = `
            <video id="localVideo" autoplay muted playsinline></video>
            <div class="video-name-badge">${state._currentUser ? state._currentUser.name : 'Bạn'}</div>
            <div class="video-mute-icon" id="localMuteIcon" style="display:none;">🔇</div>
        `;
        grid.prepend(container);
    }
    const video = document.getElementById('localVideo');
    if(video && localStream) video.srcObject = localStream;
}

function removeLocalVideo() {
    const container = document.getElementById('localVideoContainer');
    if(container) container.remove();
    const grid = document.getElementById('roomVideoGrid');
    if(grid.children.length === 0) grid.style.display = 'none';
}


// ---- Competitive Flashcards (Quizlet Style) ----
roomFcDeck = [];
roomFcIndex = 0;
let roomFcData = [];
let fcTimerInterval = null;
let fcTimeLeft = 10;
const roomFcScores = {};

function startCompetitiveFlashcards() {
    roomFcScores[state._currentUser ? state._currentUser.email : 'guest'] = 0;
    roomFcIndex = 0;
    const vocabCopy = shuffle([...VOCAB_DATA]).slice(0, 10);
    roomFcData = vocabCopy.map(word => {
        const wrong = shuffle(VOCAB_DATA.filter(w => w.kr !== word.kr)).slice(0, 3);
        return {
            kr: word.kr,
            roman: word.roman,
            correct: word.vi,
            options: shuffle([word.vi, ...wrong.map(w => w.vi)])
        };
    });

    document.getElementById('roomFcControls').style.display = 'none';
    document.getElementById('roomFcArena').style.display = 'flex';
    updateFcLiveScores();
    showCompetitiveFlashcard();
}

function showCompetitiveFlashcard() {
    if(fcTimerInterval) clearInterval(fcTimerInterval);
    
    if (roomFcIndex >= roomFcData.length) {
        document.getElementById('roomFcArena').style.display = 'none';
        document.getElementById('roomFcControls').style.display = 'flex';
        document.getElementById('roomFcStatus').innerHTML = `🎉 Đua xong! Bạn được ${roomFcScores[state._currentUser ? state._currentUser.email : 'guest']} điểm.`;
        return;
    }

    const card = roomFcData[roomFcIndex];
    document.getElementById('roomFcProgress').textContent = `${roomFcIndex + 1} / ${roomFcData.length}`;
    document.getElementById('roomFcKorean').textContent = card.kr;
    document.getElementById('roomFcRoman').textContent = card.roman;
    
    document.getElementById('roomFcOptions').innerHTML = card.options.map((opt, i) => `
        <button class="fc-option-btn" onclick="answerCompetitiveFlashcard(${i})">${opt}</button>
    `).join('');

    fcTimeLeft = 10;
    const timerEl = document.getElementById('roomFcTimer');
    timerEl.textContent = `⏳ ${fcTimeLeft}s`;
    timerEl.classList.remove('danger');

    fcTimerInterval = setInterval(() => {
        fcTimeLeft--;
        timerEl.textContent = `⏳ ${fcTimeLeft}s`;
        if (fcTimeLeft <= 3) timerEl.classList.add('danger');
        if (fcTimeLeft <= 0) {
            clearInterval(fcTimerInterval);
            answerCompetitiveFlashcard(-1); // Timeout
        }
    }, 1000);
}

function answerCompetitiveFlashcard(idx) {
    if(fcTimerInterval) clearInterval(fcTimerInterval);
    const card = roomFcData[roomFcIndex];
    const btns = document.querySelectorAll('#roomFcOptions .fc-option-btn');
    const selected = idx >= 0 ? card.options[idx] : null;

    btns.forEach((btn, i) => {
        btn.classList.add('disabled');
        if (card.options[i] === card.correct) btn.classList.add('correct');
        if (i === idx && selected !== card.correct) btn.classList.add('wrong');
    });

    if (selected === card.correct) {
        roomFcScores[state._currentUser ? state._currentUser.email : 'guest']++;
        updateFcLiveScores();
    }

    setTimeout(() => {
        roomFcIndex++;
        showCompetitiveFlashcard();
    }, 1500);
}

function updateFcLiveScores() {
    const el = document.getElementById('roomFcLiveScores');
    // Mock other players having scores for visual demo
    const mockScores = { ...roomFcScores };
    if (currentRoom && currentRoom.members.length > 1) {
        currentRoom.members.forEach(m => {
            if(!mockScores[m.email]) mockScores[m.email] = Math.floor(Math.random() * (roomFcIndex+1));
        });
    }

    const entries = Object.entries(mockScores).map(([email, score]) => {
        let name = 'Học viên';
        if(state._currentUser && email === state._currentUser.email) name = state._currentUser.name + ' (Bạn)';
        else if(currentRoom) {
            const mem = currentRoom.members.find(x => x.email === email);
            if(mem) name = mem.name;
        }
        return { name, score };
    }).sort((a,b) => b.score - a.score);

    el.innerHTML = entries.map(e => `
        <div class="fc-score-pill">
            <span class="sc-name">${e.name}</span>
            <span class="sc-val">${e.score}</span>
        </div>
    `).join('');
}


// ---- Timed Room Quiz ----
const roomLeaderboard = {};
roomQuizData = [];
roomQuizIndex = 0;
roomQuizScore = 0;
let quizTimerInterval = null;
let quizTimeLeft = 15;

function startCompetitiveQuiz() {
    roomQuizScore = 0;
    roomQuizIndex = 0;
    const vocabCopy = shuffle([...VOCAB_DATA]).slice(0, 10);
    roomQuizData = vocabCopy.map(word => {
        const wrong = shuffle(VOCAB_DATA.filter(w => w.kr !== word.kr)).slice(0, 3);
        return {
            question: `"${word.kr}" (${word.roman}) nghĩa là gì?`,
            correct: word.vi,
            options: shuffle([word.vi, ...wrong.map(w => w.vi)])
        };
    });

    document.getElementById('roomQuizControls').style.display = 'none';
    document.getElementById('roomQuizArea').style.display = 'block';
    document.getElementById('roomQuizResult').style.display = 'none';
    showRoomQuizQuestion();
}

function showRoomQuizQuestion() {
    if(quizTimerInterval) clearInterval(quizTimerInterval);
    
    if (roomQuizIndex >= roomQuizData.length) {
        finishRoomQuiz();
        return;
    }
    const q = roomQuizData[roomQuizIndex];
    document.getElementById('roomQuizProgress').textContent = `Câu ${roomQuizIndex + 1}/${roomQuizData.length}`;
    document.getElementById('roomQuizQuestion').textContent = q.question;
    document.getElementById('roomQuizAnswers').innerHTML = q.options.map((opt, i) => `
        <div class="answer-option" onclick="answerRoomQuiz(${i})">
            <span class="answer-letter">${String.fromCharCode(65 + i)}</span>
            <span>${opt}</span>
        </div>
    `).join('');

    quizTimeLeft = 15;
    const timerEl = document.getElementById('roomQuizTimer');
    timerEl.textContent = `⏳ ${quizTimeLeft}s`;
    timerEl.classList.remove('danger');

    quizTimerInterval = setInterval(() => {
        quizTimeLeft--;
        timerEl.textContent = `⏳ ${quizTimeLeft}s`;
        if (quizTimeLeft <= 5) timerEl.classList.add('danger');
        if (quizTimeLeft <= 0) {
            clearInterval(quizTimerInterval);
            answerRoomQuiz(-1);
        }
    }, 1000);
}

function answerRoomQuiz(idx) {
    if(quizTimerInterval) clearInterval(quizTimerInterval);
    const q = roomQuizData[roomQuizIndex];
    const options = document.querySelectorAll('#roomQuizAnswers .answer-option');
    const selected = idx >= 0 ? q.options[idx] : null;

    options.forEach((opt, i) => {
        opt.classList.add('disabled');
        if (q.options[i] === q.correct) opt.classList.add('correct');
        if (i === idx && selected !== q.correct) opt.classList.add('incorrect');
    });

    if (selected === q.correct) roomQuizScore++;

    setTimeout(() => {
        roomQuizIndex++;
        showRoomQuizQuestion();
    }, 1500); // Wait bit longer so they can see correct answer
}

function finishRoomQuiz() {
    const user = state._currentUser || { name: 'Học viên', email: 'guest' };
    document.getElementById('roomQuizArea').style.display = 'none';
    document.getElementById('roomQuizResult').style.display = 'block';
    document.getElementById('roomQuizControls').style.display = 'flex';
    document.getElementById('roomQuizStatus').textContent = 'Đã hoàn thành';

    // Add own score to leaderboard
    roomLeaderboard[user.email] = { name: user.name, score: roomQuizScore, total: roomQuizData.length };

    // Broadcast score
    if (roomChannel) {
        roomChannel.postMessage({
            type: 'quiz_score',
            user: { name: user.name, email: user.email },
            score: roomQuizScore,
            total: roomQuizData.length
        });
    }

    renderLeaderboard();
}

function updateLeaderboard(user, score, total) {
    roomLeaderboard[user.email] = { name: user.name, score, total };
    renderLeaderboard();
}

function renderLeaderboard() {
    const el = document.getElementById('roomLeaderboard');
    const entries = Object.values(roomLeaderboard).sort((a, b) => b.score - a.score);
    const rankClasses = ['gold', 'silver', 'bronze'];
    if (foundLesson) {
                    <h3>${level.title}</h3>
        resultsEl.style.display = 'none';
        return;
    }

    // Show loading
    resultsEl.style.display = 'block';
    resultsEl.innerHTML = '<div class="search-loading">Đang tra từ...</div>';
    if (searchWrap && resultsEl && resultsEl.style.display === 'block') {
        if (!searchWrap.contains(e.target)) {
            resultsEl.style.display = 'none';
        }
    }
});

// ======================================
// Phase 3: Reading Highlight Dictionary Popup
// ======================================
document.addEventListener('mouseup', async (e) => {
    // Only apply if we are on the reading page
    const readingPage = document.getElementById('page-reading');
    if (!readingPage || !readingPage.classList.contains('active')) return;

    // Check if click was inside an existing popup
    const existingPopup = document.getElementById('readingDictPopup');
    if (existingPopup && existingPopup.contains(e.target)) return;

    const selection = window.getSelection();
    const text = selection.toString().trim();

    // If no text, or text is too long (e.g., > 5 words limit for simple dictionary lookup), hide popup
    if (!text || text.split(/\\s+/).length > 5) {
        if (existingPopup) existingPopup.remove();
        return;
    }

    // Only translate if text contains Korean characters
    const hasKorean = /[\\u3131-\\uD79D]/ugi.test(text);
    if (!hasKorean) {
        if (existingPopup) existingPopup.remove();
        return;
    }

    // Remove existing popup before creating a new one
    if (existingPopup) existingPopup.remove();

    // Calculate position (centered above the selected text)
    const range = selection.getRangeAt(0);
    const rect = range.getBoundingClientRect();

    const popup = document.createElement('div');
    popup.id = 'readingDictPopup';
    popup.className = 'highlight-dict-popup';
    
    // Position the popup
    const popupWidth = 200; // estimated width
    let leftPos = rect.left + window.scrollX + (rect.width / 2) - (popupWidth / 2);
    // Boundary checks
    if (leftPos < 10) leftPos = 10;
    if (leftPos + popupWidth > window.innerWidth - 10) leftPos = window.innerWidth - popupWidth - 10;
    
    popup.style.left = `${leftPos}px`;
    popup.style.top = `${rect.top + window.scrollY - 70}px`; // 70px above the text
    
    popup.innerHTML = `
        <div class="dict-source">${text}</div>
        <div class="dict-loading">Đang dịch...</div>
    `;
    
    document.body.appendChild(popup);

    // Call MyMemory API
    try {
        const response = await fetch(`https://api.mymemory.translated.net/get?q=${encodeURIComponent(text)}&langpair=ko|vi`);
        const data = await response.json();
        
        if (data && data.responseData && data.responseData.translatedText) {
            const translated = data.responseData.translatedText;
            popup.innerHTML = `
                <div class="dict-source">${text}</div>
                <div class="dict-target">${translated}</div>
            `;
        } else {
            popup.innerHTML = `<div class="dict-loading">Không tìm thấy dữ liệu.</div>`;
        }
    } catch (err) {
        console.error("Lỗi popup từ điển:", err);
        popup.innerHTML = `<div class="dict-loading">Lỗi kết nối.</div>`;
    }
});

// ======================================
// Word Detail Modal (Phase 3 Extension)
// ======================================

function openWordDetailModal(word, meaning) {
    const modal = document.getElementById('wordDetailModal');
    if (!modal) return;
    
    document.getElementById('wdmWord').innerText = word;
    document.getElementById('wdmMeaning').innerText = meaning;
    const examplesEl = document.getElementById('wdmExamples');
    
    // Reset state
    examplesEl.innerHTML = `
        <div style="display:flex; align-items:center; gap:8px;">
            <div class="spinner" style="width:16px; height:16px; border-width:2px;"></div>
            <span>Đang tạo ví dụ và kiến thức liên quan bằng AI...</span>
        </div>
    `;
    
    modal.style.display = 'flex';
    
    // Hide any open search dropdowns
    const dictResults = document.getElementById('dictSearchResults');
    if (dictResults) dictResults.style.display = 'none';
    const pageResults = document.getElementById('pageVocabSearchResults');
    if (pageResults) pageResults.style.display = 'none';
    const readingPopup = document.getElementById('readingDictPopup');
    if (readingPopup) readingPopup.remove();
    
    // Call Gemini API to get examples
    loadWordDetailsFromAI(word, meaning);
}

function closeWordDetailModal() {
    const modal = document.getElementById('wordDetailModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

async function loadWordDetailsFromAI(word, meaning) {
    const examplesEl = document.getElementById('wdmExamples');
    
    const prompt = `Bạn là một từ điển tiếng Hàn chuyên nghiệp. Người dùng đang tra cứu từ "${word}" có nghĩa là "${meaning}". Hãy cung cấp 2 câu ví dụ sử dụng từ này trong giao tiếp thực tế (kèm theo phiên âm tiếng Hàn nếu có và bản dịch tiếng Việt). Nếu là ngữ pháp, hãy giải thích ngắn gọn cách chia. Trình bày ngắn gọn, dễ hiểu.`;
    
    try {
        const apiKey = localStorage.getItem('gemini_api_key');
        if (!apiKey) {
            examplesEl.innerHTML = `
                <div style="color:var(--text-secondary); font-style:italic;">
                    Vui lòng cung cấp Gemini API Key trong Cài đặt để sử dụng tính năng "Từ điển AI".<br><br>
                    <strong>Ví dụ minh họa mẫu:</strong><br>
                    - 제가 내일 <strong>${word}</strong> 할게요.<br>
                    (Tôi sẽ ${meaning} vào ngày mai.)
                </div>
            `;
            return;
        }
        
        const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=${apiKey}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                contents: [{ parts: [{ text: prompt }] }]
            })
        });
        
        if (!response.ok) {
            throw new Error('API Request failed');
        }
        
        const data = await response.json();
        if (data.candidates && data.candidates.length > 0) {
            let aiText = data.candidates[0].content.parts[0].text;
            // Basic formatting
            aiText = aiText.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
            aiText = aiText.replace(/\*(.*?)\*/g, '<em>$1</em>');
            aiText = aiText.replace(/\n/g, '<br>');
            
            examplesEl.innerHTML = aiText;
        } else {
            examplesEl.innerHTML = "Không thể tạo ví dụ lúc này.";
        }
    } catch (e) {
        console.error("Lỗi tải chi tiết từ vựng AI:", e);
        examplesEl.innerHTML = `<span style="color:var(--danger)">Lỗi kết nối AI. Vui lòng thử lại sau.</span>`;
    }
}

async function saveToMyVocab() {
    const word = document.getElementById('wdmWord').innerText;
    const meaning = document.getElementById('wdmMeaning').innerText;
    
    const btn = document.getElementById('wdmSaveBtn');
    const originalText = btn.innerHTML;
    
    btn.innerHTML = `<div class="spinner" style="width:16px;height:16px;border-width:2px;display:inline-block;vertical-align:middle;margin-right:8px;"></div> Đang lưu...`;
    btn.disabled = true;
    
    try {
        const response = await fetch('/api/vocab', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                word: word,
                meaning: meaning,
                topic: 'My Vocab'
            })
        });
        
        if (response.ok) {
            btn.innerHTML = `✔️ Đã lưu!`;
            btn.classList.add('btn-success');
            setTimeout(() => {
                btn.innerHTML = originalText;
                btn.classList.remove('btn-success');
                btn.disabled = false;
            }, 2000);
        } else {
            const err = await response.text();
            throw new Error(err);
        }
    } catch (error) {
        console.error("Lỗi khi lưu từ vựng:", error);
        alert(error.message.includes('Please login') ? 'Vui lòng đăng nhập để lưu từ vựng!' : 'Có lỗi xảy ra khi lưu từ vựng.');
        btn.innerHTML = `❌ Lỗi`;
        setTimeout(() => {
            btn.innerHTML = originalText;
            btn.disabled = false;
        }, 2000);
    }
}
