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
    listeningData: [],
    listeningAnswers: {},
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
    writingChars: [],
    translateData: [],
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

window.state = state;