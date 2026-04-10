async function loadReading(lessonId) {
    try {
        const res = await fetch(`/api/lessons/${lessonId}/reading`);

        if (!res.ok) throw new Error("API error");

        const data = await res.json();

        state.readingData = data || [];
        state.readingIndex = 0;
        state.readingAnswers = {};

        console.log("Reading data:", state.readingData);

        renderReadingPassage();
    } catch (e) {
        console.error(e);
        document.getElementById('readingQuestions').innerHTML =
            "<p>Không tải được bài đọc.</p>";
    }
}

// ================= INIT =================
function initReading() {
    const lessonId = document.body.getAttribute("data-lesson-id");

    if (!lessonId) {
        console.error("Không tìm thấy lessonId");
        return;
    }

    loadReading(lessonId);
}

// ================= RENDER =================
function renderReadingPassage() {
    const r = state.readingData[state.readingIndex];

    if (!r) {
        document.getElementById('readingQuestions').innerHTML =
            "<p>Không có bài đọc.</p>";
        return;
    }

    const questions = r.questions || [];

    document.getElementById('readingLevel').textContent =
        'Cấp độ: ' + (r.level || '');

    document.getElementById('readingTextKr').textContent =
        r.text || '';

    document.getElementById('readingTextVi').textContent =
        r.translation || '';

    document.getElementById('readingTextVi').style.display = 'none';
    document.getElementById('readingToggleTranslation').textContent =
        '👁️ Hiện bản dịch';

    document.getElementById('readingCheckBtn').style.display = 'inline-flex';
    document.getElementById('readingNextBtn').style.display = 'none';

    document.getElementById('readingQuestions').innerHTML = questions.map((q, qi) => `
        <div class="reading-question">
            <p>${qi + 1}. ${q.q}</p>
            <div class="reading-options">
                ${(q.options || []).map((opt, oi) => `
                    <button class="reading-option"
                        data-qi="${qi}"
                        data-oi="${oi}"
                        onclick="selectReadingOption(${qi}, ${oi})">
                        ${opt}
                    </button>
                `).join('')}
            </div>
        </div>
    `).join('');
}

// ================= TRANSLATION =================
function toggleReadingTranslation() {
    const el = document.getElementById('readingTextVi');
    const btn = document.getElementById('readingToggleTranslation');

    if (el.style.display === 'none') {
        el.style.display = 'block';
        btn.textContent = '🙈 Ẩn bản dịch';
    } else {
        el.style.display = 'none';
        btn.textContent = '👁️ Hiện bản dịch';
    }
}

// ================= SELECT =================
function selectReadingOption(qi, oi) {
    state.readingAnswers[qi] = oi;

    document.querySelectorAll(`[data-qi="${qi}"]`)
        .forEach(o => o.classList.remove('selected'));

    const selected = document.querySelector(
        `[data-qi="${qi}"][data-oi="${oi}"]`
    );

    if (selected) selected.classList.add('selected');
}

// ================= CHECK =================
function checkReadingAnswers() {
    const r = state.readingData[state.readingIndex];
    if (!r) return;

    if (Object.keys(state.readingAnswers).length === 0) {
        showToast("Hãy chọn đáp án trước", "info");
        return;
    }

    let correct = 0;

    (r.questions || []).forEach((q, qi) => {
        document.querySelectorAll(`[data-qi="${qi}"]`).forEach(o => {
            const oi = parseInt(o.dataset.oi);

            if (oi === q.answer) o.classList.add('correct');

            if (state.readingAnswers[qi] === oi && oi !== q.answer)
                o.classList.add('incorrect');

            o.style.pointerEvents = 'none';
        });

        if (state.readingAnswers[qi] === q.answer) correct++;
    });

    showToast(
        `Kết quả: ${correct}/${r.questions.length} câu đúng`,
        correct === r.questions.length ? 'success' : 'info'
    );

    document.getElementById('readingCheckBtn').style.display = 'none';
    document.getElementById('readingNextBtn').style.display = 'inline-flex';
}

// ================= NEXT =================
function nextReadingPassage() {
    state.readingIndex++;

    if (state.readingIndex >= state.readingData.length) {
        showToast('Hoàn thành bài đọc! 🎉', 'success');
        initReading();
    } else {
        state.readingAnswers = {};
        window.scrollTo({ top: 0, behavior: 'smooth' });
        renderReadingPassage();
    }
}

// ================= AUTO INIT =================
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("page-reading")) {
        initReading();
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

//window.state = window.state || {
//    readingData: [],
//    readingIndex: 0,
//    readingAnswers: {}
//};

window.initReading = initReading;
window.toggleReadingTranslation = toggleReadingTranslation;
window.selectReadingOption = selectReadingOption;
window.checkReadingAnswers = checkReadingAnswers;
window.nextReadingPassage = nextReadingPassage;
window.openWordDetailModal = openWordDetailModal;
window.closeWordDetailModal = closeWordDetailModal;
window.saveToMyVocab = saveToMyVocab;
