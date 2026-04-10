// ---- Flashcards ----

class FlashcardManager {
    constructor(container, vocabularies) {
        this.container = container;
        this.vocabularies = vocabularies;
        this.currentIndex = 0;
        this.isFlipped = false;

        this.handleKeyDown = this.handleKeyDown.bind(this);

        this.init();
    }

    init() {
        this.render();
        this.bindEvents();
    }

    render() {
        const vocab = this.vocabularies[this.currentIndex];
        const progressFill = document.getElementById('progressFill');
        const progressText = document.getElementById('progressText');

        if (progressFill && progressText) {
            const percent = ((this.currentIndex + 1) / this.vocabularies.length) * 100;
            progressFill.style.width = percent + '%';
            progressText.innerText = `${this.currentIndex + 1} / ${this.vocabularies.length}`;
        }

        if (!vocab) {
            this.showSummary();
            return;
        }

        const progressPercent = ((this.currentIndex + 1) / this.vocabularies.length) * 100;

        this.container.innerHTML = `
            <div class="flashcard-wrapper">

                <!-- CARD -->
                <div class="flashcard ${this.isFlipped ? 'flipped' : ''}" id="flashcard">

                    <div class="flashcard-inner">

                        <div class="flashcard-face front">
                            <h2 class="hangul-text">${vocab.hangul}</h2>
                            <p class="romanization">${vocab.romanization || ''}</p>

                            <button class="audio-btn" onclick="playKorean('${vocab.hangul}')">
                                🔊
                            </button>

                            <span class="hint">Nhấn để xem nghĩa</span>
                        </div>

                        <div class="flashcard-face back">
                            <h3 class="meaning">${vocab.meaning}</h3>

                            ${vocab.exampleSentence ? `
                                <p class="example">${vocab.exampleSentence}</p>
                            ` : ''}

                            ${vocab.audioUrl ? `
                                <button class="audio-btn" onclick="playAudio('${vocab.audioUrl}')">
                                    🔊
                                </button>
                            ` : ''}
                        </div>

                    </div>
                </div>

            </div>
        `;
    }

    bindEvents() {
        const card = document.getElementById('flashcard');

        if (card) {
            card.addEventListener('click', (e) => {
                if (!e.target.classList.contains('audio-btn')) {
                    this.flip();
                }
            });
        }

        document.removeEventListener('keydown', this.handleKeyDown);
        document.addEventListener('keydown', this.handleKeyDown);
    }

    handleKeyDown(e) {
        if (e.key === ' ') {
            e.preventDefault();
            this.flip();
        } else if (e.key === 'ArrowLeft') {
            this.prev();
        } else if (e.key === 'ArrowRight') {
            this.next();
        }
    }

    flip() {
        this.isFlipped = !this.isFlipped;

        const wrapper = this.container.querySelector('.flashcard');
        if (wrapper) {
            wrapper.classList.toggle('flipped');
        }
    }

    next() {
        if (this.currentIndex < this.vocabularies.length - 1) {
            this.currentIndex++;
            this.isFlipped = false;
            this.render();
            this.bindEvents();
        }
    }

    prev() {
        if (this.currentIndex > 0) {
            this.currentIndex--;
            this.isFlipped = false;
            this.render();
            this.bindEvents();
        }
    }

    showSummary() {
        this.container.innerHTML = `
            <div class="summary">
                <h2>🎉 Hoàn thành</h2>
                <button onclick="flashcardManager.restart()">Học lại</button>
            </div>
        `;
    }

    restart() {
        this.currentIndex = 0;
        this.isFlipped = false;
        this.render();
        this.bindEvents();
    }
}

// ---- AUDIO ----

// FIX: thiếu function này
function playKorean(text) {
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = "ko-KR";
    speechSynthesis.speak(utterance);
}

function playAudio(url) {
    if (!url) return;
    new Audio(url).play().catch(() => {});
}

// ---- LOAD LESSONS ----

document.addEventListener('DOMContentLoaded', () => {
    loadLessons();
});

// load danh sách lesson
function loadLessons() {
    fetch('/api/courses/1/lessons')
        .then(res => res.json())
        .then(res => {
            const select = document.getElementById('lessonSelect');

            select.innerHTML = '';

            res.data.forEach((lesson, index) => {
                const option = document.createElement('option');
                option.value = lesson.lessonId;
                option.textContent = lesson.title;

                // ✅ mặc định chọn bài đầu
                if (index === 0) {
                    option.selected = true;
                }

                select.appendChild(option);
            });

            // 🔥 AUTO LOAD LESSON 1
            loadFlashcardsByLesson();
        })
        .catch(err => console.error(err));
}

// load flashcard theo lesson
function loadFlashcardsByLesson() {
    const lessonId = document.getElementById('lessonSelect').value;
    const container = document.getElementById('flashcard-container');

    fetch(`/api/lessons/${lessonId}/vocabulary`)
        .then(res => res.json())
        .then(res => {
            if (!res.success || !res.data) {
                container.innerHTML = `<p>Không có dữ liệu</p>`;
                return;
            }

            window.flashcardManager = new FlashcardManager(container, res.data);
        })
        .catch(err => {
            console.error(err);
            container.innerHTML = `<p>Lỗi tải dữ liệu</p>`;
        });
}

window.playAudio = playAudio;
window.playKorean = playKorean;