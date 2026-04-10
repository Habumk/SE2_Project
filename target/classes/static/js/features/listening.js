// ================= LOAD DATA =================
async function loadListening(lessonId) {
    try {
        // Lưu ý: Đảm bảo API trả về đúng cấu trúc JSON
        const res = await fetch(`/api/lessons/${lessonId}/listening`);
        if (!res.ok) throw new Error("Lỗi tải dữ liệu");

        const data = await res.json();
        state.listeningData = data || [];
        state.listeningIndex = 0;
        state.listeningAnswers = {};

        const container = document.getElementById('listeningQuestions');
        if (state.listeningData.length === 0) {
            if (container) container.innerHTML = "<p class='text-center text-muted'>Chưa có dữ liệu bài nghe</p>";
            return;
        }
        renderListening();
    } catch (err) {
        console.error("Lỗi load listening:", err);
    }
}

// ================= RENDER UI =================
function renderListening() {
    const l = state.listeningData[state.listeningIndex];
    if (!l) return;

    // Cập nhật Progress
    const progressPct = ((state.listeningIndex + 1) / state.listeningData.length) * 100;
    const progressFill = document.getElementById('listeningProgressBar');
    if (progressFill) progressFill.style.width = `${progressPct}%`;

    const progressPctText = document.getElementById('listeningProgressPct');
    if (progressPctText) progressPctText.innerText = `${Math.round(progressPct)}%`;

    const progressText = document.getElementById('listeningProgress');
    if (progressText) progressText.innerText = `Bài ${state.listeningIndex + 1}/${state.listeningData.length}`;

    // Render Questions
    const questionsContainer = document.getElementById('listeningQuestions');
    if (questionsContainer) {
        questionsContainer.innerHTML = l.questions.map((q, qi) => `
            <div class="question-card mb-4 p-3 border rounded bg-white shadow-sm">
                <p class="fw-bold">${qi + 1}. ${q.q}</p>
                <div class="d-grid gap-2">
                    ${q.options.map((opt, oi) => `
                        <button class="answer-option btn btn-outline-secondary text-start"
                            data-qi="${qi}" data-oi="${oi}"
                            onclick="selectListening(${qi}, ${oi})">
                            ${opt}
                        </button>
                    `).join('')}
                </div>
            </div>
        `).join('');
    }

    // Reset UI state
    document.getElementById('listeningResult').style.display = 'none';
    document.getElementById('listeningNextBtn').style.display = 'none';
    document.getElementById('btnCheckListening').style.display = 'inline-flex';
    document.querySelector('.playing-label').style.opacity = 0;
}

// ================= LOGIC NGHIỆP VỤ (SỬ DỤNG TTS) =================
function playListeningAudio() {
    const l = state.listeningData[state.listeningIndex];
    // Ở đây l.audioUrl sẽ đóng vai trò là "Văn bản tiếng Hàn" cần đọc
    const textToRead = l.audioUrl;
    const label = document.querySelector('.playing-label');

    if (!textToRead) {
        alert("Không có nội dung văn bản để đọc!");
        return;
    }

    if ('speechSynthesis' in window) {
        // Hủy các yêu cầu đọc cũ nếu đang đọc dở
        window.speechSynthesis.cancel();

        const msg = new SpeechSynthesisUtterance();
        msg.text = textToRead;
        msg.lang = 'ko-KR'; // Ngôn ngữ Tiếng Hàn
        msg.rate = 0.8;      // Tốc độ hơi chậm cho người mới học sơ cấp

        msg.onstart = () => {
            label.style.opacity = 1;
        };

        msg.onend = () => {
            label.style.opacity = 0;
        };

        msg.onerror = (e) => {
            console.error("TTS Error:", e);
            label.style.opacity = 0;
        };

        window.speechSynthesis.speak(msg);
    } else {
        alert("Trình duyệt của bạn không hỗ trợ công cụ đọc văn bản.");
    }
}

function selectListening(qi, oi) {
    state.listeningAnswers[qi] = oi;
    document.querySelectorAll(`.answer-option[data-qi="${qi}"]`)
        .forEach(btn => btn.classList.remove('selected'));

    const selectedBtn = document.querySelector(`.answer-option[data-qi="${qi}"][data-oi="${oi}"]`);
    if (selectedBtn) selectedBtn.classList.add('selected');
}

function checkListeningAnswers() {
    const currentEx = state.listeningData[state.listeningIndex];
    if (!currentEx) return;

    let totalQuestions = currentEx.questions.length;
    let correctCount = 0;

    currentEx.questions.forEach((q, qi) => {
        const userAnswer = state.listeningAnswers[qi];
        const correctAnswer = q.answer;

        document.querySelectorAll(`.answer-option[data-qi="${qi}"]`).forEach(btn => {
            const oi = parseInt(btn.dataset.oi);
            btn.classList.remove('selected', 'btn-outline-secondary');

            if (oi === correctAnswer) btn.classList.add('correct');
            if (userAnswer === oi && oi !== correctAnswer) btn.classList.add('incorrect');
            btn.style.pointerEvents = 'none';
        });

        if (userAnswer === correctAnswer) correctCount++;
    });

    const resultBox = document.getElementById('listeningResult');
    const resultText = document.getElementById('listeningResultText');
    if (resultBox) {
        resultBox.style.display = 'block';
        resultText.innerText = `Bạn đã trả lời đúng ${correctCount}/${totalQuestions} câu hỏi.`;
    }

    document.getElementById('listeningNextBtn').style.display = 'inline-flex';
    document.getElementById('btnCheckListening').style.display = 'none';
}

function nextListening() {
    state.listeningIndex++;
    if (state.listeningIndex < state.listeningData.length) {
        state.listeningAnswers = {};
        renderListening();
    } else {
        alert("Chúc mừng! Bạn đã hoàn thành tất cả bài tập nghe của bài học này. 🎉");
    }
}

// Export functions to window scope
window.selectListening = selectListening;
window.checkListeningAnswers = checkListeningAnswers;
window.nextListening = nextListening;
window.playListeningAudio = playListeningAudio;
window.loadListening = loadListening;

