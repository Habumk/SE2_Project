// Speaking.js - Web Speech API for Korean pronunciation
// UC-09: Speaking Exercise with Web Speech API

class SpeakingManager {
    constructor() {
        this.recognition = null;
        this.isRecording = false;
        this.onResult = null;
        this.onError = null;
        this.init();
    }

    init() {
        if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
            console.warn('Speech Recognition not supported in this browser');
            return;
        }

        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        this.recognition = new SpeechRecognition();
        
        // Configure for Korean
        this.recognition.lang = 'ko-KR';
        this.recognition.continuous = false;
        this.recognition.interimResults = true;
        this.recognition.maxAlternatives = 1;

        this.recognition.onstart = () => {
            this.isRecording = true;
            console.log('Speech recognition started');
        };

        this.recognition.onresult = (event) => {
            let finalTranscript = '';
            let interimTranscript = '';

            for (let i = event.resultIndex; i < event.results.length; i++) {
                const transcript = event.results[i][0].transcript;
                if (event.results[i].isFinal) {
                    finalTranscript += transcript;
                } else {
                    interimTranscript += transcript;
                }
            }

            if (this.onResult) {
                this.onResult({
                    final: finalTranscript,
                    interim: interimTranscript,
                    confidence: event.results[event.results.length - 1] ? event.results[event.results.length - 1][0].confidence : 0
                });
            }
        };

        this.recognition.onerror = (event) => {
            console.error('Speech recognition error:', event.error);
            this.isRecording = false;
            if (this.onError) {
                this.onError(event.error);
            }
        };

        this.recognition.onend = () => {
            this.isRecording = false;
            console.log('Speech recognition ended');
        };
    }

    start() {
        if (!this.recognition) {
            throw new Error('Speech recognition not available');
        }
        
        if (this.isRecording) {
            this.stop();
        }
        
        this.recognition.start();
    }

    stop() {
        if (this.recognition && this.isRecording) {
            this.recognition.stop();
        }
    }

    isSupported() {
        return !!(window.SpeechRecognition || window.webkitSpeechRecognition);
    }
}

/**
 * D4: Play Korean pronunciation.
 * Priority 1: audio file (audioUrl param)
 * Priority 2: Web Speech Synthesis fallback (ko-KR)
 */
function playKorean(text, audioUrl, options = {}) {
    // Priority 1: real audio file
    if (audioUrl && audioUrl.trim() !== '' && audioUrl !== 'null') {
        const audio = new Audio(audioUrl);
        audio.play().catch(() => {
            // Fallback to Speech Synthesis if file fails
            speakKorean(text, options);
        });
        return;
    }
    // Priority 2: Web Speech Synthesis fallback
    speakKorean(text, options);
}

function speakKorean(text, options = {}) {
    if (!('speechSynthesis' in window)) {
        console.warn('Speech synthesis not supported');
        return;
    }
    window.speechSynthesis.cancel();
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'ko-KR';
    utterance.rate = options.rate || 0.8;
    utterance.pitch = options.pitch || 1.0;
    utterance.volume = options.volume || 1.0;
    const voices = window.speechSynthesis.getVoices();
    const koreanVoice = voices.find(v => v.lang.includes('ko') || v.name.includes('Korean'));
    if (koreanVoice) utterance.voice = koreanVoice;
    window.speechSynthesis.speak(utterance);
}

// Initialize voices when they're loaded
if ('speechSynthesis' in window) {
    if (speechSynthesis.onvoiceschanged !== undefined) {
        speechSynthesis.onvoiceschanged = () => {
            // Voices are now loaded
        };
    }
}

// Expose functions globally
window.SpeakingManager = SpeakingManager;
window.playKorean = playKorean;

// Auto-initialize speaking buttons
document.addEventListener('DOMContentLoaded', () => {
    // Add click handlers to audio buttons with data-audio-korean
    document.querySelectorAll('[data-audio-korean]').forEach(button => {
        button.addEventListener('click', (e) => {
            const text = e.currentTarget.getAttribute('data-audio-korean');
            const audioUrl = e.currentTarget.getAttribute('data-audio-url') || null;
            if (text) playKorean(text, audioUrl);
        });
    });

    // Initialize recording buttons
    document.querySelectorAll('[data-record-speaking]').forEach(button => {
        const manager = new SpeakingManager();
        
        button.addEventListener('click', () => {
            if (manager.isRecording) {
                manager.stop();
                button.classList.remove('recording');
                button.textContent = '🎙️ Ghi âm';
            } else {
                manager.start();
                button.classList.add('recording');
                button.textContent = '⏹️ Dừng';
            }
        });

        manager.onResult = (result) => {
            const targetId = button.getAttribute('data-target');
            const targetElement = document.getElementById(targetId);
            if (targetElement) {
                targetElement.value = result.final || result.interim;
            }
        };

        manager.onError = (error) => {
            console.error('Recording error:', error);
            alert('Lỗi ghi âm: ' + error + '. Vui lòng thử lại.');
            button.classList.remove('recording');
            button.textContent = '🎙️ Ghi âm';
        };
    });
});
