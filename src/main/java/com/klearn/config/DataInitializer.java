package com.klearn.config;

import com.klearn.model.*;
import com.klearn.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with initial learning data from data.js on first run.
 * Only inserts data if tables are empty (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final HangulCharacterRepository hangulRepo;
    private final VocabWordRepository vocabRepo;
    private final GrammarLessonRepository grammarRepo;
    private final ListeningExerciseRepository listeningRepo;
    private final SpeakingExerciseRepository speakingRepo;
    private final ReadingPassageRepository readingRepo;
    private final BadgeRepository badgeRepo;

    @Override
    public void run(String... args) {
        if (hangulRepo.count() == 0) seedHangul();
        if (vocabRepo.count() == 0) seedVocab();
        if (grammarRepo.count() == 0) seedGrammar();
        if (listeningRepo.count() == 0) seedListening();
        if (speakingRepo.count() == 0) seedSpeaking();
        if (readingRepo.count() == 0) seedReading();
        if (badgeRepo.count() == 0) seedBadges();
        log.info("✅ Data seeding complete!");
    }

    private void seedHangul() {
        log.info("🔤 Seeding Hangul data...");
        // Consonants
        hangulRepo.saveAll(List.of(
            h("ㄱ", "기역 (giyeok)", "g/k", "Phát âm như \"g\" đứng đầu, \"k\" đứng cuối", "가방 (gabang) - Cặp sách|국 (guk) - Canh", "consonants"),
            h("ㄴ", "니은 (nieun)", "n", "Phát âm như \"n\"", "나 (na) - Tôi|눈 (nun) - Mắt/Tuyết", "consonants"),
            h("ㄷ", "디귿 (digeut)", "d/t", "Phát âm như \"d\" đứng đầu, \"t\" đứng cuối", "다리 (dari) - Cầu/Chân|돈 (don) - Tiền", "consonants"),
            h("ㄹ", "리을 (rieul)", "r/l", "Phát âm giữa \"r\" và \"l\"", "라면 (ramyeon) - Mì|달 (dal) - Mặt trăng", "consonants"),
            h("ㅁ", "미음 (mieum)", "m", "Phát âm như \"m\"", "마음 (maeum) - Trái tim|엄마 (eomma) - Mẹ", "consonants"),
            h("ㅂ", "비읍 (bieup)", "b/p", "Phát âm như \"b\" đứng đầu, \"p\" đứng cuối", "바다 (bada) - Biển|밥 (bap) - Cơm", "consonants"),
            h("ㅅ", "시옷 (siot)", "s", "Phát âm như \"s\"", "사랑 (sarang) - Tình yêu|소 (so) - Bò", "consonants"),
            h("ㅇ", "이응 (ieung)", "ng/silent", "Câm khi đứng đầu, \"ng\" khi đứng cuối", "아이 (ai) - Trẻ em|강 (gang) - Sông", "consonants"),
            h("ㅈ", "지읒 (jieut)", "j", "Phát âm như \"j\"", "자다 (jada) - Ngủ|점심 (jeomsim) - Bữa trưa", "consonants"),
            h("ㅊ", "치읓 (chieut)", "ch", "Phát âm như \"ch\"", "차 (cha) - Trà/Xe|친구 (chingu) - Bạn bè", "consonants"),
            h("ㅋ", "키읔 (kieuk)", "k", "Phát âm như \"k\" bật hơi", "커피 (keopi) - Cà phê|코 (ko) - Mũi", "consonants"),
            h("ㅌ", "티읕 (tieut)", "t", "Phát âm như \"t\" bật hơi", "토끼 (tokki) - Thỏ|타다 (tada) - Đi/Cưỡi", "consonants"),
            h("ㅍ", "피읖 (pieup)", "p", "Phát âm như \"p\" bật hơi", "포도 (podo) - Nho|피 (pi) - Máu", "consonants"),
            h("ㅎ", "히읗 (hieut)", "h", "Phát âm như \"h\"", "하늘 (haneul) - Bầu trời|학교 (hakgyo) - Trường", "consonants")
        ));
        // Vowels
        hangulRepo.saveAll(List.of(
            h("ㅏ", "아 (a)", "a", "Âm \"a\" như trong \"ba\"", "아버지 (abeoji) - Bố", "vowels"),
            h("ㅑ", "야 (ya)", "ya", "Âm \"ya\"", "야구 (yagu) - Bóng chày", "vowels"),
            h("ㅓ", "어 (eo)", "eo", "Âm \"ơ\" như trong tiếng Việt", "어머니 (eomeoni) - Mẹ", "vowels"),
            h("ㅕ", "여 (yeo)", "yeo", "Âm \"yơ\"", "여자 (yeoja) - Phụ nữ", "vowels"),
            h("ㅗ", "오 (o)", "o", "Âm \"o\" như trong \"bo\"", "오빠 (oppa) - Anh trai", "vowels"),
            h("ㅛ", "요 (yo)", "yo", "Âm \"yo\"", "요리 (yori) - Nấu ăn", "vowels"),
            h("ㅜ", "우 (u)", "u", "Âm \"u\" như trong \"bu\"", "우유 (uyu) - Sữa", "vowels"),
            h("ㅠ", "유 (yu)", "yu", "Âm \"yu\"", "유학 (yuhak) - Du học", "vowels"),
            h("ㅡ", "으 (eu)", "eu", "Âm \"ư\" như trong tiếng Việt", "으른 (eureun) - Người lớn", "vowels"),
            h("ㅣ", "이 (i)", "i", "Âm \"i\" như trong \"bi\"", "이름 (ireum) - Tên", "vowels")
        ));
        // Double consonants
        hangulRepo.saveAll(List.of(
            h("ㄲ", "쌍기역 (ssang-giyeok)", "kk", "Phụ âm đôi \"kk\"", "까치 (kkachi) - Chim ác là", "double"),
            h("ㄸ", "쌍디귿 (ssang-digeut)", "tt", "Phụ âm đôi \"tt\"", "딸 (ttal) - Con gái", "double"),
            h("ㅃ", "쌍비읍 (ssang-bieup)", "pp", "Phụ âm đôi \"pp\"", "빵 (ppang) - Bánh mì", "double"),
            h("ㅆ", "쌍시옷 (ssang-siot)", "ss", "Phụ âm đôi \"ss\"", "쓰다 (sseuda) - Viết", "double"),
            h("ㅉ", "쌍지읒 (ssang-jieut)", "jj", "Phụ âm đôi \"jj\"", "짜다 (jjada) - Mặn", "double")
        ));
        // Compound vowels
        hangulRepo.saveAll(List.of(
            h("ㅐ", "애 (ae)", "ae", "Âm \"e\" như \"pet\"", "개 (gae) - Con chó", "compound"),
            h("ㅒ", "얘 (yae)", "yae", "Âm \"ye\"", "얘기 (yaegi) - Câu chuyện", "compound"),
            h("ㅔ", "에 (e)", "e", "Âm \"ê\"", "세계 (segye) - Thế giới", "compound"),
            h("ㅖ", "예 (ye)", "ye", "Âm \"yê\"", "예쁘다 (yeppeuda) - Đẹp", "compound"),
            h("ㅘ", "와 (wa)", "wa", "Âm \"wa\"", "과일 (gwail) - Trái cây", "compound"),
            h("ㅙ", "왜 (wae)", "wae", "Âm \"we\"", "왜 (wae) - Tại sao", "compound"),
            h("ㅚ", "외 (oe)", "oe", "Âm \"we\"", "외국 (oeguk) - Nước ngoài", "compound"),
            h("ㅝ", "워 (wo)", "wo", "Âm \"wơ\"", "원 (won) - Won (tiền)", "compound"),
            h("ㅞ", "웨 (we)", "we", "Âm \"we\"", "웨딩 (weding) - Đám cưới", "compound"),
            h("ㅟ", "위 (wi)", "wi", "Âm \"wi\"", "위 (wi) - Trên", "compound"),
            h("ㅢ", "의 (ui)", "ui", "Âm \"ưi\"", "의사 (uisa) - Bác sĩ", "compound")
        ));
    }

    private void seedVocab() {
        log.info("📚 Seeding Vocabulary data...");
        vocabRepo.saveAll(List.of(
            // Giao tiếp cơ bản (communication)
            v("안녕하세요", "annyeonghaseyo", "Xin chào (formal)", "Giao tiếp", "안녕하세요! 만나서 반갑습니다."),
            v("감사합니다", "gamsahamnida", "Cảm ơn (formal)", "Giao tiếp", "도와주셔서 감사합니다."),
            v("죄송합니다", "joesonghamnida", "Xin lỗi (formal)", "Giao tiếp", "늦어서 죄송합니다."),
            v("네", "ne", "Vâng / Dạ", "Giao tiếp", "네, 알겠습니다."),
            v("아니요", "aniyo", "Không", "Giao tiếp", "아니요, 괜찮습니다."),
            v("안녕히 가세요", "annyeonghi gaseyo", "Tạm biệt (chào người đi)", "Giao tiếp", "안녕히 가세요, 조심히 가세요."),
            v("안녕히 계세요", "annyeonghi gyeseyo", "Tạm biệt (chào người ở lại)", "Giao tiếp", ""),
            v("만나서 반갑습니다", "mannaseo bangapseumnida", "Rất vui được gặp bạn", "Giao tiếp", ""),
            v("이름이 뭐예요?", "ireumi mwoyeyo?", "Tên bạn là gì?", "Giao tiếp", ""),
            v("어디에서 왔어요?", "eodieseo wasseoyo?", "Bạn đến từ đâu?", "Giao tiếp", "베트남에서 왔어요."),
            
            // Gia đình (family)
            v("가족", "gajok", "Gia đình", "Gia đình", "우리 가족은 네 명입니다."),
            v("아버지", "abeoji", "Bố", "Gia đình", "아버지는 회사에 가십니다."),
            v("어머니", "eomeoni", "Mẹ", "Gia đình", "어머니는 요리를 잘합니다."),
            v("형", "hyeong", "Anh trai (nam gọi)", "Gia đình", "형은 대학생입니다."),
            v("오빠", "oppa", "Anh trai (nữ gọi)", "Gia đình", "오빠, 밥 먹었어요?"),
            v("누나", "nuna", "Chị gái (nam gọi)", "Gia đình", "누나는 예뻐요."),
            v("언니", "eonni", "Chị gái (nữ gọi)", "Gia đình", "언니하고 같이 쇼핑해요."),
            v("동생", "dongsaeng", "Em", "Gia đình", "내 동생은 귀여워요."),
            
            // Trường học (school)
            v("학교", "hakgyo", "Trường học", "Trường học", "학교에 가요."),
            v("학생", "haksaeng", "Học sinh", "Trường học", "저는 학생입니다."),
            v("선생님", "seonsaengnim", "Giáo viên", "Trường học", "선생님, 안녕하세요?"),
            v("교실", "gyosil", "Phòng học", "Trường học", "교실에 학생이 많아요."),
            v("도서관", "doseogwan", "Thư viện", "Trường học", "도서관에서 책을 읽어요."),
            v("시험", "siheom", "Kỳ thi / Bài kiểm tra", "Trường học", "내일 시험이 있어요."),
            v("숙제", "sukje", "Bài tập về nhà", "Trường học", "숙제를 다 했어요."),
            v("책", "chaek", "Sách", "Trường học", "한국어 책을 샀어요."),
            
            // Mua sắm (shopping)
            v("얼마예요?", "eolmayeyo?", "Bao nhiêu tiền?", "Mua sắm", "이 사과 얼마예요?"),
            v("비싸요", "bissayo", "Mắc / Đắt", "Mua sắm", "너무 비싸요."),
            v("싸요", "ssayo", "Rẻ", "Mua sắm", "이 옷은 정말 싸요."),
            v("조금 깎아 주세요", "jogeum kkakka juseyo", "Giảm giá một chút đi", "Mua sắm", ""),
            v("카드", "kadeu", "Thẻ tín dụng", "Mua sắm", "카드로 계산할게요."),
            v("현금", "hyeongeum", "Tiền mặt", "Mua sắm", "현금 있으세요?"),
            v("옷", "ot", "Quần áo", "Mua sắm", "옷 가게에서 옷을 샀어요."),
            v("사이즈", "saijeu", "Kích cỡ", "Mua sắm", "다른 사이즈 있나요?"),
            
            // Du lịch (travel)
            v("여행", "yeohaeng", "Du lịch", "Du lịch", "한국으로 여행을 갑니다."),
            v("공항", "gonghang", "Sân bay", "Du lịch", "인천 공항에 도착했어요."),
            v("비행기", "bihaenggi", "Máy bay", "Du lịch", "비행기를 탔어요."),
            v("여권", "yeogwon", "Hộ chiếu", "Du lịch", "여권을 보여주세요."),
            v("호텔", "hotel", "Khách sạn", "Du lịch", "호텔을 예약했어요."),
            v("지하철", "jihacheol", "Tàu điện ngầm", "Du lịch", "지하철역이 어디에 있어요?"),
            v("택시", "taeksi", "Taxi", "Du lịch", "택시를 타고 가요."),
            v("지도", "jido", "Bản đồ", "Du lịch", "지도를 보세요."),
            v("어디예요?", "eodiyeyo?", "Ở đâu?", "Du lịch", "화장실이 어디예요?"),
            
            // Number & Time (Số đếm & Thời gian) 
            v("하나", "hana", "Một", "Khác", "커피 하나 주세요."),
            v("둘", "dul", "Hai", "Khác", "맥주 두 병 주세요."),
            v("오늘", "oneul", "Hôm nay", "Khác", "오늘은 날씨가 따뜻해요."),
            v("내일", "naeil", "Ngày mai", "Khác", "내일 만나요."),
            v("지금", "jigeum", "Bây giờ", "Khác", "지금 몇 시예요?")
        ));
    }

    private void seedGrammar() {
        log.info("📝 Seeding Grammar data...");
        grammarRepo.saveAll(List.of(
            GrammarLesson.builder()
                .title("-입니다 / -이에요/예요").level("Sơ cấp").desc("Đuôi kết thúc câu - \"là\"")
                .content("<h4>Cấu trúc</h4><p>Danh từ + 입니다 (formal) / 이에요·예요 (informal)</p><h4>Giải thích</h4><p>Dùng để nói \"A là B\". Nếu danh từ kết thúc bằng phụ âm dùng <strong>이에요</strong>, nguyên âm dùng <strong>예요</strong>.</p><h4>Ví dụ</h4>")
                .examples("[{\"kr\":\"저는 학생입니다.\",\"vi\":\"Tôi là học sinh. (formal)\"},{\"kr\":\"이것은 책이에요.\",\"vi\":\"Đây là quyển sách. (informal)\"},{\"kr\":\"한국 사람이에요.\",\"vi\":\"Là người Hàn Quốc.\"}]")
                .build(),
            GrammarLesson.builder()
                .title("-을/를 (Object particle)").level("Sơ cấp").desc("Trợ từ tân ngữ")
                .content("<h4>Cấu trúc</h4><p>Danh từ (phụ âm) + 을 / Danh từ (nguyên âm) + 를</p><h4>Giải thích</h4><p>Đánh dấu tân ngữ (đối tượng chịu hành động) trong câu.</p><h4>Ví dụ</h4>")
                .examples("[{\"kr\":\"밥을 먹어요.\",\"vi\":\"Ăn cơm.\"},{\"kr\":\"커피를 마셔요.\",\"vi\":\"Uống cà phê.\"},{\"kr\":\"한국어를 공부해요.\",\"vi\":\"Học tiếng Hàn.\"}]")
                .build(),
            GrammarLesson.builder()
                .title("-에 가다/오다 (Place)").level("Sơ cấp").desc("Đi đến / Đến từ một nơi")
                .content("<h4>Cấu trúc</h4><p>Địa điểm + 에 + 가다 (đi) / 오다 (đến)</p><h4>Giải thích</h4><p>Trợ từ 에 chỉ hướng di chuyển khi đi kèm với 가다 hoặc 오다.</p><h4>Ví dụ</h4>")
                .examples("[{\"kr\":\"학교에 가요.\",\"vi\":\"Đi đến trường.\"},{\"kr\":\"집에 와요.\",\"vi\":\"Về nhà.\"},{\"kr\":\"한국에 가고 싶어요.\",\"vi\":\"Muốn đi Hàn Quốc.\"}]")
                .build(),
            GrammarLesson.builder()
                .title("-고 싶다 (Want to)").level("Sơ cấp").desc("Muốn làm gì đó")
                .content("<h4>Cấu trúc</h4><p>Gốc động từ + 고 싶다</p><h4>Giải thích</h4><p>Diễn tả mong muốn của người nói. Thêm 고 싶다 vào gốc động từ.</p><h4>Ví dụ</h4>")
                .examples("[{\"kr\":\"먹고 싶어요.\",\"vi\":\"Muốn ăn.\"},{\"kr\":\"여행하고 싶어요.\",\"vi\":\"Muốn đi du lịch.\"},{\"kr\":\"한국어를 배우고 싶어요.\",\"vi\":\"Muốn học tiếng Hàn.\"}]")
                .build(),
            GrammarLesson.builder()
                .title("-았/었/였어요 (Past tense)").level("Trung cấp").desc("Thì quá khứ")
                .content("<h4>Cấu trúc</h4><p>Gốc ĐT (ㅏ,ㅗ) + 았어요 / Gốc ĐT (khác) + 었어요 / 하다 → 했어요</p><h4>Giải thích</h4><p>Dùng để diễn tả hành động đã xảy ra trong quá khứ.</p><h4>Ví dụ</h4>")
                .examples("[{\"kr\":\"먹었어요.\",\"vi\":\"Đã ăn.\"},{\"kr\":\"갔어요.\",\"vi\":\"Đã đi.\"},{\"kr\":\"공부했어요.\",\"vi\":\"Đã học.\"}]")
                .build()
        ));
    }

    private void seedListening() {
        log.info("🎧 Seeding Listening data...");
        listeningRepo.saveAll(List.of(
            li("안녕하세요", "Xin chào", "[\"Xin chào\",\"Tạm biệt\",\"Cảm ơn\",\"Xin lỗi\"]"),
            li("감사합니다", "Cảm ơn", "[\"Xin lỗi\",\"Cảm ơn\",\"Xin chào\",\"Tạm biệt\"]"),
            li("밥을 먹어요", "Ăn cơm", "[\"Uống nước\",\"Ăn cơm\",\"Đi học\",\"Ngủ\"]"),
            li("학교에 가요", "Đi đến trường", "[\"Về nhà\",\"Đi chơi\",\"Đi đến trường\",\"Đi làm\"]"),
            li("물을 주세요", "Cho tôi nước", "[\"Cho tôi cơm\",\"Cho tôi nước\",\"Cho tôi tiền\",\"Cho tôi sách\"]"),
            li("이름이 뭐예요?", "Tên bạn là gì?", "[\"Bạn bao nhiêu tuổi?\",\"Tên bạn là gì?\",\"Bạn ở đâu?\",\"Bạn khỏe không?\"]"),
            li("커피를 마셔요", "Uống cà phê", "[\"Uống trà\",\"Uống bia\",\"Uống cà phê\",\"Uống sữa\"]"),
            li("날씨가 좋아요", "Thời tiết đẹp", "[\"Thời tiết xấu\",\"Thời tiết đẹp\",\"Trời mưa\",\"Trời lạnh\"]")
        ));
    }

    private void seedSpeaking() {
        log.info("🗣️ Seeding Speaking data...");
        speakingRepo.saveAll(List.of(
            sp("안녕하세요", "annyeonghaseyo", "Xin chào"),
            sp("감사합니다", "gamsahamnida", "Cảm ơn"),
            sp("죄송합니다", "joesonghamnida", "Xin lỗi"),
            sp("사랑해요", "saranghaeyo", "Tôi yêu bạn"),
            sp("맛있어요", "masisseoyo", "Ngon"),
            sp("한국어를 공부해요", "hangugeo-reul gongbuhaeyo", "Tôi học tiếng Hàn"),
            sp("이름이 뭐예요?", "ireumi mwoyeyo?", "Tên bạn là gì?"),
            sp("만나서 반갑습니다", "mannaseo bangapseumnida", "Rất vui được gặp bạn")
        ));
    }

    private void seedReading() {
        log.info("📖 Seeding Reading data...");
        readingRepo.saveAll(List.of(
            ReadingPassage.builder()
                .level("Sơ cấp")
                .text("저는 학생입니다. 저는 베트남 사람입니다. 한국어를 공부합니다. 한국 음식을 좋아합니다. 특히 김치와 비빔밥을 좋아합니다.")
                .translation("Tôi là học sinh. Tôi là người Việt Nam. Tôi học tiếng Hàn. Tôi thích món ăn Hàn Quốc. Đặc biệt tôi thích kimchi và cơm trộn.")
                .questions("[{\"q\":\"이 사람은 누구입니까? (Người này là ai?)\",\"options\":[\"선생님 (Giáo viên)\",\"학생 (Học sinh)\",\"의사 (Bác sĩ)\"],\"answer\":1},{\"q\":\"이 사람은 무엇을 좋아합니까? (Người này thích gì?)\",\"options\":[\"일본 음식\",\"중국 음식\",\"한국 음식\"],\"answer\":2}]")
                .build(),
            ReadingPassage.builder()
                .level("Sơ cấp")
                .text("오늘은 토요일입니다. 날씨가 좋습니다. 저는 친구와 같이 공원에 갑니다. 공원에서 산책을 합니다. 그리고 커피를 마십니다.")
                .translation("Hôm nay là thứ 7. Thời tiết đẹp. Tôi cùng bạn đi công viên. Chúng tôi đi dạo ở công viên. Và uống cà phê.")
                .questions("[{\"q\":\"오늘은 무슨 요일입니까? (Hôm nay là thứ mấy?)\",\"options\":[\"일요일 (Chủ nhật)\",\"토요일 (Thứ 7)\",\"금요일 (Thứ 6)\"],\"answer\":1},{\"q\":\"어디에 갑니까? (Đi đâu?)\",\"options\":[\"학교 (Trường)\",\"공원 (Công viên)\",\"집 (Nhà)\"],\"answer\":1}]")
                .build()
        ));
    }

    private void seedBadges() {
        log.info("🏅 Seeding Badge data...");
        badgeRepo.saveAll(List.of(
            b("First Step", "Hoàn thành lesson đầu tiên", null, "lesson_complete", 1),
            b("Lesson x5", "Hoàn thành 5 lessons", null, "lesson_complete", 5),
            b("Lesson x10", "Hoàn thành 10 lessons", null, "lesson_complete", 10),
            b("Course Complete", "Hoàn thành 1 course", null, "course_complete", 1),
            b("Getting Started", "Streak 3 ngày", null, "streak", 3),
            b("Week Warrior", "Streak 7 ngày", null, "streak", 7),
            b("Dedicated Learner", "Streak 30 ngày", null, "streak", 30),
            b("XP Rookie", "Đạt 100 XP", null, "xp_milestone", 100),
            b("XP Pro", "Đạt 500 XP", null, "xp_milestone", 500),
            b("Perfect Speaker", "Speaking score 100%", null, "perfect_speaking", 1)
        ));
    }

    private Badge b(String name, String description, String iconUrl, String conditionType, int conditionValue) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setIconUrl(iconUrl);
        badge.setConditionType(conditionType);
        badge.setConditionValue(conditionValue);
        return badge;
    }

    // === Builder helpers ===
    private HangulCharacter h(String ch, String name, String roman, String desc, String examples, String type) {
        return HangulCharacter.builder().charValue(ch).name(name).roman(roman).desc(desc).examples(examples).type(type).build();
    }

    private VocabWord v(String kr, String roman, String vi, String category, String example) {
        return VocabWord.builder().kr(kr).roman(roman).vi(vi).category(category).example(example).build();
    }

    private ListeningExercise li(String text, String answer, String options) {
        return ListeningExercise.builder().text(text).answer(answer).options(options).build();
    }

    private SpeakingExercise sp(String kr, String roman, String vi) {
        return SpeakingExercise.builder().kr(kr).roman(roman).vi(vi).build();
    }
}
