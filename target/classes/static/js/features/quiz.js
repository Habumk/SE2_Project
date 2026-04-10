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
        </button>`
    ).join('');
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
        if (o.querySelector('span:last-child').textContent === q.answer) {
            o.classList.add('correct');
        }
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

    const circle = document.getElementById('scoreCircle');
    const circumference = 2 * Math.PI * 54;
    const offset = circumference - (pct / 100) * circumference;

    setTimeout(() => {
        circle.style.transition = 'stroke-dashoffset 1s ease';
        circle.style.strokeDashoffset = offset;
    }, 100);
}

function restartQuiz() {
    startQuiz(state.quizType);
}

function backToQuizSetup() {
    resetQuizUI();
}

// ===== MOCK DATA FOR QUIZ =====

// ---- VOCAB ----
const VOCAB_DATA = [
    { kr: "안녕하세요", vi: "Xin chào" },
    { kr: "감사합니다", vi: "Cảm ơn" },
    { kr: "사랑", vi: "Tình yêu" },
    { kr: "학교", vi: "Trường học" },
    { kr: "학생", vi: "Học sinh" },
    { kr: "선생님", vi: "Giáo viên" },
    { kr: "물", vi: "Nước" },
    { kr: "밥", vi: "Cơm" },
    { kr: "친구", vi: "Bạn bè" },
    { kr: "가족", vi: "Gia đình" }
];

// ---- HANGUL ----
const HANGUL_DATA = {
    consonants: [
        { char: "ㄱ", roman: "g/k" },
        { char: "ㄴ", roman: "n" },
        { char: "ㄷ", roman: "d/t" },
        { char: "ㄹ", roman: "r/l" },
        { char: "ㅁ", roman: "m" },
        { char: "ㅂ", roman: "b/p" },
        { char: "ㅅ", roman: "s" },
        { char: "ㅇ", roman: "ng" },
        { char: "ㅈ", roman: "j" },
        { char: "ㅎ", roman: "h" }
    ],
    vowels: [
        { char: "ㅏ", roman: "a" },
        { char: "ㅓ", roman: "eo" },
        { char: "ㅗ", roman: "o" },
        { char: "ㅜ", roman: "u" },
        { char: "ㅡ", roman: "eu" },
        { char: "ㅣ", roman: "i" }
    ]
};

// ---- LISTENING ----
const LISTENING_DATA = [
    {
        text: "안녕하세요",
        options: ["Xin chào", "Tạm biệt", "Cảm ơn", "Xin lỗi"],
        answer: "Xin chào"
    },
    {
        text: "감사합니다",
        options: ["Xin lỗi", "Cảm ơn", "Xin chào", "Không có gì"],
        answer: "Cảm ơn"
    },
    {
        text: "사랑해요",
        options: ["Tôi ghét bạn", "Tôi yêu bạn", "Tôi nhớ bạn", "Tôi thích bạn"],
        answer: "Tôi yêu bạn"
    },
    {
        text: "학교에 가요",
        options: ["Đi học", "Ăn cơm", "Ngủ", "Đi chơi"],
        answer: "Đi học"
    },
    {
        text: "물을 마셔요",
        options: ["Uống nước", "Ăn cơm", "Chạy", "Đọc sách"],
        answer: "Uống nước"
    }
];

// ---- UTILS ----

// shuffle array
function shuffle(arr) {
    return arr.sort(() => Math.random() - 0.5);
}

window.startQuiz = startQuiz;
window.playQuizAudio = playQuizAudio;
window.checkQuizAnswer = checkQuizAnswer;
window.nextQuizQuestion = nextQuizQuestion;
window.restartQuiz = restartQuiz;
window.backToQuizSetup = backToQuizSetup;
