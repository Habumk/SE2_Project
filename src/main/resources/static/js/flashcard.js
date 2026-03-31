// Flashcard.js - UC-14 Flashcard Mode Implementation
class FlashcardManager {
    constructor(container, vocabularies) {
        this.container = container;
        this.vocabularies = vocabularies;
        this.currentIndex = 0;
        this.knownWords = new Set();
        this.unknownWords = new Set();
        this.isFlipped = false;
        this.init();
    }

    init() {
        this.render();
        this.bindEvents();
    }

    render() {
        const vocab = this.vocabularies[this.currentIndex];
        if (!vocab) {
            this.showSummary();
            return;
        }

        this.container.innerHTML = `
            <div class="flashcard-container">
                <div class="flashcard-progress">
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: ${(this.currentIndex / this.vocabularies.length) * 100}%"></div>
                    </div>
                    <span class="progress-text">${this.currentIndex + 1} / ${this.vocabularies.length}</span>
                </div>
                
                <div class="flashcard ${this.isFlipped ? 'flipped' : ''}" id="flashcard">
                    <div class="flashcard-face front">
                        <div class="flashcard-content">
                            <h2 class="hangul-text">${vocab.hangul}</h2>
                            <button class="audio-btn" onclick="playKorean('${vocab.hangul}')" title="Phát âm">
                                🔊
                            </button>
                        </div>
                        <div class="flashcard-hint">Nhấp để xem nghĩa</div>
                    </div>
                    
                    <div class="flashcard-face back">
                        <div class="flashcard-content">
                            <div class="romanization">${vocab.romanization || ''}</div>
                            <div class="meaning">${vocab.meaning}</div>
                            ${vocab.exampleSentence ? `
                                <div class="example">
                                    <strong>Ví dụ:</strong> ${vocab.exampleSentence}
                                </div>
                            ` : ''}
                            ${vocab.audioUrl ? `
                                <button class="audio-btn" onclick="playAudio('${vocab.audioUrl}')" title="Phát âm mẫu">
                                    🔊
                                </button>
                            ` : ''}
                        </div>
                    </div>
                </div>
                
                <div class="flashcard-actions">
                    <button class="btn btn-danger" onclick="flashcardManager.markUnknown()">
                        ❌ Chưa biết
                    </button>
                    <button class="btn btn-success" onclick="flashcardManager.markKnown()">
                        ✅ Đã biết
                    </button>
                </div>
            </div>
        `;
    }

    bindEvents() {
        const flashcard = document.getElementById('flashcard');
        if (flashcard) {
            flashcard.addEventListener('click', (e) => {
                if (!e.target.classList.contains('audio-btn')) {
                    this.flip();
                }
            });
        }

        // Keyboard shortcuts
        document.addEventListener('keydown', (e) => {
            if (e.key === ' ' || e.key === 'Enter') {
                e.preventDefault();
                this.flip();
            } else if (e.key === 'ArrowLeft') {
                this.markUnknown();
            } else if (e.key === 'ArrowRight') {
                this.markKnown();
            }
        });
    }

    flip() {
        this.isFlipped = !this.isFlipped;
        const flashcard = document.getElementById('flashcard');
        if (flashcard) {
            flashcard.classList.toggle('flipped');
        }
    }

    markKnown() {
        this.knownWords.add(this.currentIndex);
        this.next();
    }

    markUnknown() {
        this.unknownWords.add(this.currentIndex);
        this.next();
    }

    next() {
        this.currentIndex++;
        this.isFlipped = false;
        this.render();
        this.bindEvents();
    }

    showSummary() {
        const knownCount = this.knownWords.size;
        const unknownCount = this.unknownWords.size;
        const totalCount = this.vocabularies.length;

        this.container.innerHTML = `
            <div class="flashcard-summary">
                <h2>🎉 Hoàn thành!</h2>
                <div class="summary-stats">
                    <div class="stat">
                        <span class="stat-number">${knownCount}</span>
                        <span class="stat-label">Đã biết</span>
                    </div>
                    <div class="stat">
                        <span class="stat-number">${unknownCount}</span>
                        <span class="stat-label">Chưa biết</span>
                    </div>
                    <div class="stat">
                        <span class="stat-number">${Math.round((knownCount / totalCount) * 100)}%</span>
                        <span class="stat-label">Hoàn thành</span>
                    </div>
                </div>
                
                ${unknownCount > 0 ? `
                    <div class="summary-actions">
                        <button class="btn btn-primary" onclick="flashcardManager.reviewUnknown()">
                            🔄 Ôn lại từ chưa biết (${unknownCount})
                        </button>
                        <button class="btn btn-secondary" onclick="flashcardManager.restart()">
                            🔄 Làm lại từ đầu
                        </button>
                    </div>
                ` : `
                    <div class="summary-actions">
                        <button class="btn btn-success" onclick="flashcardManager.restart()">
                            🎉 Tuyệt vời! Làm lại
                        </button>
                    </div>
                `}
            </div>
        `;
    }

    reviewUnknown() {
        const unknownVocabs = Array.from(this.unknownWords).map(index => this.vocabularies[index]);
        this.vocabularies = unknownVocabs;
        this.currentIndex = 0;
        this.knownWords.clear();
        this.unknownWords.clear();
        this.isFlipped = false;
        this.render();
        this.bindEvents();
    }

    restart() {
        this.currentIndex = 0;
        this.knownWords.clear();
        this.unknownWords.clear();
        this.isFlipped = false;
        this.render();
        this.bindEvents();
    }
}

// Helper function to play audio from URL
function playAudio(url) {
    const audio = new Audio(url);
    audio.play().catch(e => console.error('Audio play failed:', e));
}

// Initialize flashcards when page loads
document.addEventListener('DOMContentLoaded', () => {
    const flashcardContainer = document.getElementById('flashcard-container');
    if (flashcardContainer) {
        // Load vocabulary data from API
        const lessonId = flashcardContainer.getAttribute('data-lesson-id');
        if (lessonId) {
            fetch(`/api/lessons/${lessonId}/vocabulary`)
                .then(response => response.json())
                .then(vocabularies => {
                    if (vocabularies && vocabularies.length > 0) {
                        window.flashcardManager = new FlashcardManager(flashcardContainer, vocabularies);
                    } else {
                        flashcardContainer.innerHTML = `
                            <div class="empty-state">
                                <p>Chưa có từ vựng cho bài học này.</p>
                            </div>
                        `;
                    }
                })
                .catch(error => {
                    console.error('Failed to load vocabulary:', error);
                    flashcardContainer.innerHTML = `
                        <div class="error-state">
                            <p>Không thể tải từ vựng. Vui lòng thử lại.</p>
                        </div>
                    `;
                });
        }
    }
});
