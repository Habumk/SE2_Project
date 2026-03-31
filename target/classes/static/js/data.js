// ====== K-Learn Data (API Placeholders) ======

// These will be populated from the /api/ endpoints on page load in app.js
let HANGUL_DATA = { consonants: [], vowels: [], double: [], compound: [] };
let VOCAB_DATA = [];
let CATEGORY_LABELS = {};
let GRAMMAR_DATA = [];
let LISTENING_DATA = [];
let SPEAKING_DATA = [];
let READING_DATA = [];


// ====== Hardcoded Static Data ======
// Data that is too small or static to need database seeding (Phase 1)
const TRANSLATE_DATA = [
    { vi: 'Xin chào', kr: '안녕하세요' },
    { vi: 'Cảm ơn', kr: '감사합니다' },
    { vi: 'Tôi là học sinh', kr: '저는 학생입니다' },
    { vi: 'Tôi thích Hàn Quốc', kr: '한국을 좋아해요' },
    { vi: 'Tôi yêu bạn', kr: '사랑해요' },
    { vi: 'Thời tiết đẹp', kr: '날씨가 좋아요' },
    { vi: 'Tôi muốn ăn cơm', kr: '밥을 먹고 싶어요' },
    { vi: 'Xin lỗi', kr: '죄송합니다' },
];

const WRITING_CHARS = ['가', '나', '다', '라', '마', '바', '사', '아', '자', '차', '카', '타', '파', '하',
    '고', '노', '도', '로', '모', '보', '소', '오', '조', '코', '토', '포', '호',
    '구', '누', '두', '루', '무', '부', '수', '우', '주', '쿠', '투', '푸', '후'];

const DAILY_WORDS = [
    { kr: '사랑', roman: 'sarang', vi: 'Tình yêu' },
    { kr: '행복', roman: 'haengbok', vi: 'Hạnh phúc' },
    { kr: '꿈', roman: 'kkum', vi: 'Giấc mơ' },
    { kr: '별', roman: 'byeol', vi: 'Ngôi sao' },
    { kr: '하늘', roman: 'haneul', vi: 'Bầu trời' },
    { kr: '바다', roman: 'bada', vi: 'Biển' },
    { kr: '친구', roman: 'chingu', vi: 'Bạn bè' },
    { kr: '음악', roman: 'eumak', vi: 'Âm nhạc' },
    { kr: '희망', roman: 'huimang', vi: 'Hy vọng' },
    { kr: '미소', roman: 'miso', vi: 'Nụ cười' }
];

const ROADMAP_DATA = {
    basic: {
        title: "Giáo trình 1: Cơ bản → Nâng cao (26 bài)",
        levels: [
            {
                id: 'lv1',
                title: 'Nền tảng — Lv.1',
                desc: 'Xây dựng nền tảng bảng chữ cái và giao tiếp cơ bản.',
                lessons: [
                    { id: 'b1', title: 'Hangul — Nguyên âm', titleKr: '모음', completeCount: 0 },
                    { id: 'b2', title: 'Hangul — Phụ âm', titleKr: '자음', completeCount: 0 },
                    { id: 'b3', title: 'Ghép vần & đọc cơ bản', titleKr: '읽기', completeCount: 0 },
                    { id: 'b4', title: 'Chào hỏi & giới thiệu', titleKr: '인사', completeCount: 0 },
                    { id: 'b5', title: 'Số đếm thuần Hàn & Hán', titleKr: '숫자', completeCount: 0 },
                    { id: 'b6', title: 'Ngày, tháng, thời gian', titleKr: '시간', completeCount: 0 },
                    { id: 'b7', title: 'Gia đình', titleKr: '가족', completeCount: 0 },
                    { id: 'b8', title: 'Ôn tập Level 1', titleKr: '복습 1', completeCount: 0 }
                ]
            },
            {
                id: 'lv2',
                title: 'Căn bản — Lv.2',
                desc: 'Mở rộng từ vựng và chủ đề đời sống hàng ngày.',
                lessons: [
                    { id: 'b9', title: 'Nghề nghiệp', titleKr: '직업', completeCount: 0 },
                    { id: 'b10', title: 'Vị trí & địa điểm', titleKr: '위치', completeCount: 0 },
                    { id: 'b11', title: 'Thức ăn & gọi món', titleKr: '음식', completeCount: 0 },
                    { id: 'b12', title: 'Mua sắm', titleKr: '쇼핑', completeCount: 0 },
                    { id: 'b13', title: 'Giao thông', titleKr: '교통', completeCount: 0 },
                    { id: 'b14', title: 'Thời tiết', titleKr: '날씨', completeCount: 0 },
                    { id: 'b15', title: 'Sở thích', titleKr: '취미', completeCount: 0 },
                    { id: 'b16', title: 'Sức khỏe', titleKr: '건강', completeCount: 0 },
                    { id: 'b17', title: 'Động vật', titleKr: '동물', completeCount: 0 },
                    { id: 'b18', title: 'Ôn tập Level 2', titleKr: '복습 2', completeCount: 0 }
                ]
            },
            {
                id: 'lv3',
                title: 'Nâng cao — Lv.3',
                desc: 'Giao tiếp tình huống nâng cao và văn hóa Hàn.',
                lessons: [
                    { id: 'b19', title: 'Tại bệnh viện', titleKr: '병원', completeCount: 0 },
                    { id: 'b20', title: 'Tại trường học', titleKr: '학교', completeCount: 0 },
                    { id: 'b21', title: 'Phỏng vấn xin việc', titleKr: '면접', completeCount: 0 },
                    { id: 'b22', title: 'Viết email & tin nhắn', titleKr: '이메일', completeCount: 0 },
                    { id: 'b23', title: 'Du lịch Hàn Quốc', titleKr: '여행', completeCount: 0 },
                    { id: 'b24', title: 'Văn hóa Hàn Quốc', titleKr: '문화', completeCount: 0 },
                    { id: 'b25', title: 'Nghe hiểu K-Pop', titleKr: '케이팝', completeCount: 0 },
                    { id: 'b26', title: 'Ôn tập Level 3', titleKr: '복습 3', completeCount: 0 }
                ]
            }
        ]
    },
    eps: {
        title: "Giáo trình 2: EPS-TOPIK (50 bài)",
        levels: [
            {
                id: 'eps1',
                title: 'Phần 1: Giao tiếp & Đời sống (Bài 1-30)',
                desc: 'Chủ đề sinh hoạt, mua sắm, giao thông và sức khỏe.',
                lessons: [
                    { id: 'e1_10', title: 'Bài 1-10: Chào hỏi, sinh hoạt', titleKr: '기본 생활', completeCount: 0 },
                    { id: 'e11_20', title: 'Bài 11-20: Mua sắm, giao thông', titleKr: '쇼핑, 교통', completeCount: 0 },
                    { id: 'e21_30', title: 'Bài 21-30: Công việc, nhà máy', titleKr: '직장 생활', completeCount: 0 }
                ]
            },
            {
                id: 'eps2',
                title: 'Phần 2: Môi trường làm việc & Luật (Bài 31-50)',
                desc: 'An toàn lao động, văn hóa công sở, luật lao động.',
                lessons: [
                    { id: 'e31_40', title: 'Bài 31-40: Văn hóa, luật lệ', titleKr: '한국 문화와 법', completeCount: 0 },
                    { id: 'e41_50', title: 'Bài 41-50: Ôn tổng hợp, đề thi', titleKr: '종합 복습', completeCount: 0 }
                ]
            }
        ]
    }
};
