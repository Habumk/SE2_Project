CREATE DATABASE  IF NOT EXISTS `klearn` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `klearn`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: klearn
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `answer_id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `explanation` text,
  `is_correct` bit(1) DEFAULT NULL,
  `question_id` bigint NOT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `FK8frr4bcabmmeyyu60qt7iiblo` (`question_id`),
  CONSTRAINT `FK8frr4bcabmmeyyu60qt7iiblo` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,'Hàn Quốc',NULL,_binary '',4),(2,'Nhật Bản',NULL,_binary '\0',4),(3,'Trung Quốc',NULL,_binary '\0',4),(4,'Việt Nam',NULL,_binary '\0',4),(5,'Hàn Quốc',NULL,_binary '',6),(6,'Việt Nam',NULL,_binary '\0',6),(7,'Nhật Bản',NULL,_binary '\0',6);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `badge`
--

DROP TABLE IF EXISTS `badge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `badge` (
  `badge_id` bigint NOT NULL AUTO_INCREMENT,
  `condition_type` varchar(50) DEFAULT NULL,
  `condition_value` int DEFAULT NULL,
  `description` text,
  `icon_url` varchar(500) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`badge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `badge`
--

LOCK TABLES `badge` WRITE;
/*!40000 ALTER TABLE `badge` DISABLE KEYS */;
INSERT INTO `badge` VALUES (1,'lesson_complete',1,'Hoàn thành lesson đầu tiên',NULL,'First Step'),(2,'lesson_complete',5,'Hoàn thành 5 lessons',NULL,'Lesson x5'),(3,'lesson_complete',10,'Hoàn thành 10 lessons',NULL,'Lesson x10'),(4,'course_complete',1,'Hoàn thành 1 course',NULL,'Course Complete'),(5,'streak',3,'Streak 3 ngày',NULL,'Getting Started'),(6,'streak',7,'Streak 7 ngày',NULL,'Week Warrior'),(7,'streak',30,'Streak 30 ngày',NULL,'Dedicated Learner'),(8,'xp_milestone',100,'Đạt 100 XP',NULL,'XP Rookie'),(9,'xp_milestone',500,'Đạt 500 XP',NULL,'XP Pro'),(10,'perfect_speaking',1,'Speaking score 100%',NULL,'Perfect Speaker');
/*!40000 ALTER TABLE `badge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `description` text,
  `level` varchar(50) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Khóa học cơ bản dành cho người mới bắt đầu','Sơ cấp 1','Tiếng Hàn Sơ cấp 1'),(2,'Tiếp tục ôn tập và mở rộng ngữ pháp sơ cấp','Sơ cấp 2','Tiếng Hàn Sơ cấp 2'),(3,'Chuyển tiếp lên trình độ trung cấp: ngữ pháp và từ vựng','Trung cấp 1','Tiếng Hàn Trung cấp 1'),(4,'Mở rộng giao tiếp và đọc hiểu văn bản','Trung cấp 2','Tiếng Hàn Trung cấp 2');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercise`
--

DROP TABLE IF EXISTS `exercise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise` (
  `exercise_id` bigint NOT NULL AUTO_INCREMENT,
  `audio_url` varchar(500) DEFAULT NULL,
  `type` enum('listening','speaking','reading','writing') NOT NULL,
  `lesson_id` bigint NOT NULL,
  PRIMARY KEY (`exercise_id`),
  KEY `FKoohf1t296xtx5kt0waihyna1k` (`lesson_id`),
  CONSTRAINT `FKoohf1t296xtx5kt0waihyna1k` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise`
--

LOCK TABLES `exercise` WRITE;
/*!40000 ALTER TABLE `exercise` DISABLE KEYS */;
INSERT INTO `exercise` VALUES (19,NULL,'listening',1),(20,'audio/q1.mp3','listening',1),(21,'audio/q1.mp3','listening',1),(22,NULL,'reading',1),(23,NULL,'reading',1);
/*!40000 ALTER TABLE `exercise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grammar_lessons`
--

DROP TABLE IF EXISTS `grammar_lessons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grammar_lessons` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `description` varchar(500) NOT NULL,
  `examples` text,
  `level` varchar(50) NOT NULL,
  `title` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grammar_lessons`
--

LOCK TABLES `grammar_lessons` WRITE;
/*!40000 ALTER TABLE `grammar_lessons` DISABLE KEYS */;
INSERT INTO `grammar_lessons` VALUES (1,'<h4>Cấu trúc</h4><p>Danh từ + 입니다 (formal) / 이에요·예요 (informal)</p><h4>Giải thích</h4><p>Dùng để nói \"A là B\". Nếu danh từ kết thúc bằng phụ âm dùng <strong>이에요</strong>, nguyên âm dùng <strong>예요</strong>.</p><h4>Ví dụ</h4>','Đuôi kết thúc câu - \"là\"','[{\"kr\":\"저는 학생입니다.\",\"vi\":\"Tôi là học sinh. (formal)\"},{\"kr\":\"이것은 책이에요.\",\"vi\":\"Đây là quyển sách. (informal)\"},{\"kr\":\"한국 사람이에요.\",\"vi\":\"Là người Hàn Quốc.\"}]','Sơ cấp','-입니다 / -이에요/예요'),(2,'<h4>Cấu trúc</h4><p>Danh từ (phụ âm) + 을 / Danh từ (nguyên âm) + 를</p><h4>Giải thích</h4><p>Đánh dấu tân ngữ (đối tượng chịu hành động) trong câu.</p><h4>Ví dụ</h4>','Trợ từ tân ngữ','[{\"kr\":\"밥을 먹어요.\",\"vi\":\"Ăn cơm.\"},{\"kr\":\"커피를 마셔요.\",\"vi\":\"Uống cà phê.\"},{\"kr\":\"한국어를 공부해요.\",\"vi\":\"Học tiếng Hàn.\"}]','Sơ cấp','-을/를 (Object particle)'),(3,'<h4>Cấu trúc</h4><p>Địa điểm + 에 + 가다 (đi) / 오다 (đến)</p><h4>Giải thích</h4><p>Trợ từ 에 chỉ hướng di chuyển khi đi kèm với 가다 hoặc 오다.</p><h4>Ví dụ</h4>','Đi đến / Đến từ một nơi','[{\"kr\":\"학교에 가요.\",\"vi\":\"Đi đến trường.\"},{\"kr\":\"집에 와요.\",\"vi\":\"Về nhà.\"},{\"kr\":\"한국에 가고 싶어요.\",\"vi\":\"Muốn đi Hàn Quốc.\"}]','Sơ cấp','-에 가다/오다 (Place)'),(4,'<h4>Cấu trúc</h4><p>Gốc động từ + 고 싶다</p><h4>Giải thích</h4><p>Diễn tả mong muốn của người nói. Thêm 고 싶다 vào gốc động từ.</p><h4>Ví dụ</h4>','Muốn làm gì đó','[{\"kr\":\"먹고 싶어요.\",\"vi\":\"Muốn ăn.\"},{\"kr\":\"여행하고 싶어요.\",\"vi\":\"Muốn đi du lịch.\"},{\"kr\":\"한국어를 배우고 싶어요.\",\"vi\":\"Muốn học tiếng Hàn.\"}]','Sơ cấp','-고 싶다 (Want to)'),(5,'<h4>Cấu trúc</h4><p>Gốc ĐT (ㅏ,ㅗ) + 았어요 / Gốc ĐT (khác) + 었어요 / 하다 → 했어요</p><h4>Giải thích</h4><p>Dùng để diễn tả hành động đã xảy ra trong quá khứ.</p><h4>Ví dụ</h4>','Thì quá khứ','[{\"kr\":\"먹었어요.\",\"vi\":\"Đã ăn.\"},{\"kr\":\"갔어요.\",\"vi\":\"Đã đi.\"},{\"kr\":\"공부했어요.\",\"vi\":\"Đã học.\"}]','Trung cấp','-았/었/였어요 (Past tense)');
/*!40000 ALTER TABLE `grammar_lessons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hangul_characters`
--

DROP TABLE IF EXISTS `hangul_characters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hangul_characters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `char_value` varchar(10) NOT NULL,
  `description` varchar(500) NOT NULL,
  `examples` varchar(1000) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `roman` varchar(20) NOT NULL,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hangul_characters`
--

LOCK TABLES `hangul_characters` WRITE;
/*!40000 ALTER TABLE `hangul_characters` DISABLE KEYS */;
INSERT INTO `hangul_characters` VALUES (1,'ㄱ','Phát âm như \"g\" đứng đầu, \"k\" đứng cuối','가방 (gabang) - Cặp sách|국 (guk) - Canh','기역 (giyeok)','g/k','consonants'),(2,'ㄴ','Phát âm như \"n\"','나 (na) - Tôi|눈 (nun) - Mắt/Tuyết','니은 (nieun)','n','consonants'),(3,'ㄷ','Phát âm như \"d\" đứng đầu, \"t\" đứng cuối','다리 (dari) - Cầu/Chân|돈 (don) - Tiền','디귿 (digeut)','d/t','consonants'),(4,'ㄹ','Phát âm giữa \"r\" và \"l\"','라면 (ramyeon) - Mì|달 (dal) - Mặt trăng','리을 (rieul)','r/l','consonants'),(5,'ㅁ','Phát âm như \"m\"','마음 (maeum) - Trái tim|엄마 (eomma) - Mẹ','미음 (mieum)','m','consonants'),(6,'ㅂ','Phát âm như \"b\" đứng đầu, \"p\" đứng cuối','바다 (bada) - Biển|밥 (bap) - Cơm','비읍 (bieup)','b/p','consonants'),(7,'ㅅ','Phát âm như \"s\"','사랑 (sarang) - Tình yêu|소 (so) - Bò','시옷 (siot)','s','consonants'),(8,'ㅇ','Câm khi đứng đầu, \"ng\" khi đứng cuối','아이 (ai) - Trẻ em|강 (gang) - Sông','이응 (ieung)','ng/silent','consonants'),(9,'ㅈ','Phát âm như \"j\"','자다 (jada) - Ngủ|점심 (jeomsim) - Bữa trưa','지읒 (jieut)','j','consonants'),(10,'ㅊ','Phát âm như \"ch\"','차 (cha) - Trà/Xe|친구 (chingu) - Bạn bè','치읓 (chieut)','ch','consonants'),(11,'ㅋ','Phát âm như \"k\" bật hơi','커피 (keopi) - Cà phê|코 (ko) - Mũi','키읔 (kieuk)','k','consonants'),(12,'ㅌ','Phát âm như \"t\" bật hơi','토끼 (tokki) - Thỏ|타다 (tada) - Đi/Cưỡi','티읕 (tieut)','t','consonants'),(13,'ㅍ','Phát âm như \"p\" bật hơi','포도 (podo) - Nho|피 (pi) - Máu','피읖 (pieup)','p','consonants'),(14,'ㅎ','Phát âm như \"h\"','하늘 (haneul) - Bầu trời|학교 (hakgyo) - Trường','히읗 (hieut)','h','consonants'),(15,'ㅏ','Âm \"a\" như trong \"ba\"','아버지 (abeoji) - Bố','아 (a)','a','vowels'),(16,'ㅑ','Âm \"ya\"','야구 (yagu) - Bóng chày','야 (ya)','ya','vowels'),(17,'ㅓ','Âm \"ơ\" như trong tiếng Việt','어머니 (eomeoni) - Mẹ','어 (eo)','eo','vowels'),(18,'ㅕ','Âm \"yơ\"','여자 (yeoja) - Phụ nữ','여 (yeo)','yeo','vowels'),(19,'ㅗ','Âm \"o\" như trong \"bo\"','오빠 (oppa) - Anh trai','오 (o)','o','vowels'),(20,'ㅛ','Âm \"yo\"','요리 (yori) - Nấu ăn','요 (yo)','yo','vowels'),(21,'ㅜ','Âm \"u\" như trong \"bu\"','우유 (uyu) - Sữa','우 (u)','u','vowels'),(22,'ㅠ','Âm \"yu\"','유학 (yuhak) - Du học','유 (yu)','yu','vowels'),(23,'ㅡ','Âm \"ư\" như trong tiếng Việt','으른 (eureun) - Người lớn','으 (eu)','eu','vowels'),(24,'ㅣ','Âm \"i\" như trong \"bi\"','이름 (ireum) - Tên','이 (i)','i','vowels'),(25,'ㄲ','Phụ âm đôi \"kk\"','까치 (kkachi) - Chim ác là','쌍기역 (ssang-giyeok)','kk','double'),(26,'ㄸ','Phụ âm đôi \"tt\"','딸 (ttal) - Con gái','쌍디귿 (ssang-digeut)','tt','double'),(27,'ㅃ','Phụ âm đôi \"pp\"','빵 (ppang) - Bánh mì','쌍비읍 (ssang-bieup)','pp','double'),(28,'ㅆ','Phụ âm đôi \"ss\"','쓰다 (sseuda) - Viết','쌍시옷 (ssang-siot)','ss','double'),(29,'ㅉ','Phụ âm đôi \"jj\"','짜다 (jjada) - Mặn','쌍지읒 (ssang-jieut)','jj','double'),(30,'ㅐ','Âm \"e\" như \"pet\"','개 (gae) - Con chó','애 (ae)','ae','compound'),(31,'ㅒ','Âm \"ye\"','얘기 (yaegi) - Câu chuyện','얘 (yae)','yae','compound'),(32,'ㅔ','Âm \"ê\"','세계 (segye) - Thế giới','에 (e)','e','compound'),(33,'ㅖ','Âm \"yê\"','예쁘다 (yeppeuda) - Đẹp','예 (ye)','ye','compound'),(34,'ㅘ','Âm \"wa\"','과일 (gwail) - Trái cây','와 (wa)','wa','compound'),(35,'ㅙ','Âm \"we\"','왜 (wae) - Tại sao','왜 (wae)','wae','compound'),(36,'ㅚ','Âm \"we\"','외국 (oeguk) - Nước ngoài','외 (oe)','oe','compound'),(37,'ㅝ','Âm \"wơ\"','원 (won) - Won (tiền)','워 (wo)','wo','compound'),(38,'ㅞ','Âm \"we\"','웨딩 (weding) - Đám cưới','웨 (we)','we','compound'),(39,'ㅟ','Âm \"wi\"','위 (wi) - Trên','위 (wi)','wi','compound'),(40,'ㅢ','Âm \"ưi\"','의사 (uisa) - Bác sĩ','의 (ui)','ui','compound');
/*!40000 ALTER TABLE `hangul_characters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson` (
  `lesson_id` bigint NOT NULL AUTO_INCREMENT,
  `order_index` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `xp_reward` int DEFAULT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`lesson_id`),
  KEY `FKjs3c7skmg8bvdddok5lc7s807` (`course_id`),
  CONSTRAINT `FKjs3c7skmg8bvdddok5lc7s807` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
INSERT INTO `lesson` VALUES (1,1,'소개 — Giới thiệu',100,1),(2,2,'학교 — Trường học',100,1),(3,3,'일상생활 — Sinh hoạt hàng ngày',100,1),(4,4,'날짜와 요일 — Ngày và thứ',100,1),(5,5,'하루 일과 — Công việc trong ngày',100,1),(6,6,'주말 — Cuối tuần',100,1),(7,7,'물건 사기 (1) — Mua sắm (1)',100,1),(8,8,'음식 — Thức ăn',100,1),(9,9,'집 — Nhà cửa',100,1),(10,10,'가족 — Gia đình',100,1),(11,11,'날씨 — Thời tiết',100,1),(12,12,'전화 (1) — Điện thoại (1)',100,1),(13,13,'생일 — Sinh nhật',100,1),(14,14,'취미 — Sở thích',100,1),(15,15,'교통 (1) — Giao thông (1)',100,1),(16,16,'만남 — Gặp gỡ',100,2),(17,17,'약속 — Hẹn gặp',100,2),(18,18,'물건 사기 (2) — Mua sắm (2)',100,2),(19,19,'병원 — Bệnh viện',100,2),(20,20,'편지 — Thư tín',100,2),(21,21,'교통 (2) — Giao thông (2)',100,2),(22,22,'전화 (2) — Điện thoại (2)',100,2);
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_result`
--

DROP TABLE IF EXISTS `lesson_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_at` datetime(6) DEFAULT NULL,
  `listening_score` int DEFAULT NULL,
  `reading_score` int DEFAULT NULL,
  `speaking_score` int DEFAULT NULL,
  `total_score` int DEFAULT NULL,
  `writing_score` int DEFAULT NULL,
  `xp_earned` int DEFAULT NULL,
  `lesson_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6v384kr82nlpyaji1lg0v2fsg` (`lesson_id`),
  KEY `FK4bodigtqy7gjpiattbst7llge` (`user_id`),
  CONSTRAINT `FK4bodigtqy7gjpiattbst7llge` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK6v384kr82nlpyaji1lg0v2fsg` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_result`
--

LOCK TABLES `lesson_result` WRITE;
/*!40000 ALTER TABLE `lesson_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `lesson_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listening_exercises`
--

DROP TABLE IF EXISTS `listening_exercises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listening_exercises` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `answer` varchar(200) NOT NULL,
  `options` varchar(1000) NOT NULL,
  `text` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listening_exercises`
--

LOCK TABLES `listening_exercises` WRITE;
/*!40000 ALTER TABLE `listening_exercises` DISABLE KEYS */;
INSERT INTO `listening_exercises` VALUES (9,'Hàn Quốc','[\"Hàn Quốc\",\"Việt Nam\",\"Nhật Bản\",\"Mỹ\"]','한국'),(10,'Việt Nam','[\"Hàn Quốc\",\"Việt Nam\",\"Trung Quốc\",\"Thái Lan\"]','베트남'),(11,'Nhật Bản','[\"Nhật Bản\",\"Hàn Quốc\",\"Mỹ\",\"Anh\"]','일본'),(12,'Mỹ','[\"Anh\",\"Mỹ\",\"Pháp\",\"Đức\"]','미국'),(13,'Trung Quốc','[\"Nhật Bản\",\"Trung Quốc\",\"Hàn Quốc\",\"Nga\"]','중국'),(14,'Xin chào','[\"Xin chào\",\"Tạm biệt\",\"Cảm ơn\",\"Xin lỗi\"]','안녕하세요'),(15,'Tạm biệt (người đi)','[\"Xin chào\",\"Tạm biệt (người đi)\",\"Tạm biệt (ở lại)\",\"Chúc ngủ ngon\"]','안녕히 가세요'),(16,'Tạm biệt (ở lại)','[\"Xin chào\",\"Tạm biệt (người đi)\",\"Tạm biệt (ở lại)\",\"Cảm ơn\"]','안녕히 계세요'),(17,'Rất vui được gặp','[\"Xin lỗi\",\"Rất vui được gặp\",\"Tạm biệt\",\"Không sao\"]','반갑습니다'),(18,'Học sinh','[\"Học sinh\",\"Giáo viên\",\"Bác sĩ\",\"Nhân viên\"]','학생'),(19,'Giáo viên','[\"Học sinh\",\"Giáo viên\",\"Bác sĩ\",\"Nhân viên\"]','선생님'),(20,'Nhân viên công ty','[\"Giáo viên\",\"Nhân viên công ty\",\"Bác sĩ\",\"Sinh viên\"]','회사원'),(21,'Bác sĩ','[\"Bác sĩ\",\"Dược sĩ\",\"Giáo viên\",\"Nhân viên\"]','의사'),(22,'Dược sĩ','[\"Bác sĩ\",\"Dược sĩ\",\"Giáo viên\",\"Sinh viên\"]','약사'),(23,'Tên','[\"Tên\",\"Địa chỉ\",\"Số điện thoại\",\"Tuổi\"]','이름'),(24,'Điện thoại','[\"Địa chỉ\",\"Điện thoại\",\"Tên\",\"Email\"]','전화'),(25,'Địa chỉ','[\"Tên\",\"Địa chỉ\",\"Email\",\"Số điện thoại\"]','주소'),(26,'Email','[\"Email\",\"Điện thoại\",\"Địa chỉ\",\"Tên\"]','이메일'),(27,'Nghề nghiệp','[\"Tên\",\"Nghề nghiệp\",\"Tuổi\",\"Địa chỉ\"]','직업');
/*!40000 ALTER TABLE `listening_exercises` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` bigint NOT NULL AUTO_INCREMENT,
  `audio_url` varchar(500) DEFAULT NULL,
  `content` text NOT NULL,
  `expected_text` varchar(500) DEFAULT NULL,
  `exercise_id` bigint NOT NULL,
  PRIMARY KEY (`question_id`),
  KEY `FKcw89k10abejup8p3hif0kjpw` (`exercise_id`),
  CONSTRAINT `FKcw89k10abejup8p3hif0kjpw` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`exercise_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (4,NULL,'한국 là quốc gia nào?',NULL,21),(6,NULL,'Câu hỏi: 민수는 어디 사람입니까?','안녕하세요. 저는 민수입니다. 저는 한국 사람입니다.',23);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reading_passages`
--

DROP TABLE IF EXISTS `reading_passages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reading_passages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `level` varchar(50) NOT NULL,
  `questions` text NOT NULL,
  `text` text NOT NULL,
  `translation` text NOT NULL,
  `lesson_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reading_lesson` (`lesson_id`),
  CONSTRAINT `fk_reading_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reading_passages`
--

LOCK TABLES `reading_passages` WRITE;
/*!40000 ALTER TABLE `reading_passages` DISABLE KEYS */;
INSERT INTO `reading_passages` VALUES (1,'TOPIK 1 - Bài 1','[\n        {\n            \"q\": \"Người trong đoạn văn đến từ đâu?\",\n            \"options\": [\"Hàn Quốc\", \"Việt Nam\", \"Nhật Bản\", \"Mỹ\"],\n            \"answer\": 1\n        },\n        {\n            \"q\": \"Người đó làm nghề gì?\",\n            \"options\": [\"Giáo viên\", \"Bác sĩ\", \"Học sinh\", \"Nhân viên công ty\"],\n            \"answer\": 2\n        },\n        {\n            \"q\": \"Người đó đang học gì?\",\n            \"options\": [\"Tiếng Anh\", \"Tiếng Nhật\", \"Tiếng Trung\", \"Tiếng Hàn\"],\n            \"answer\": 3\n        },\n        {\n            \"q\": \"Câu nào là lời chào trong đoạn?\",\n            \"options\": [\"안녕하세요\", \"학생입니다\", \"베트남 사람입니다\", \"공부합니다\"],\n            \"answer\": 0\n        }\n    ]','안녕하세요. 저는 베트남 사람입니다. 저는 학생입니다. 한국어를 공부합니다. 반갑습니다.','Xin chào. Tôi là người Việt Nam. Tôi là học sinh. Tôi học tiếng Hàn. Rất vui được gặp bạn.',1);
/*!40000 ALTER TABLE `reading_passages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_participant`
--

DROP TABLE IF EXISTS `room_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_participant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `joined_at` datetime(6) DEFAULT NULL,
  `room_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3p3q16i2md68n0ltwsw48u8kk` (`room_id`),
  KEY `FK9odfaeswdei72f8niqjwuuyef` (`user_id`),
  CONSTRAINT `FK3p3q16i2md68n0ltwsw48u8kk` FOREIGN KEY (`room_id`) REFERENCES `speaking_room` (`room_id`),
  CONSTRAINT `FK9odfaeswdei72f8niqjwuuyef` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_participant`
--

LOCK TABLES `room_participant` WRITE;
/*!40000 ALTER TABLE `room_participant` DISABLE KEYS */;
INSERT INTO `room_participant` VALUES (1,'2026-04-05 13:22:52.701287',1,1);
/*!40000 ALTER TABLE `room_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `speaking_exercises`
--

DROP TABLE IF EXISTS `speaking_exercises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `speaking_exercises` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `kr` varchar(200) NOT NULL,
  `roman` varchar(200) NOT NULL,
  `vi` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `speaking_exercises`
--

LOCK TABLES `speaking_exercises` WRITE;
/*!40000 ALTER TABLE `speaking_exercises` DISABLE KEYS */;
INSERT INTO `speaking_exercises` VALUES (1,'안녕하세요','annyeonghaseyo','Xin chào'),(2,'감사합니다','gamsahamnida','Cảm ơn'),(3,'죄송합니다','joesonghamnida','Xin lỗi'),(4,'사랑해요','saranghaeyo','Tôi yêu bạn'),(5,'맛있어요','masisseoyo','Ngon'),(6,'한국어를 공부해요','hangugeo-reul gongbuhaeyo','Tôi học tiếng Hàn'),(7,'이름이 뭐예요?','ireumi mwoyeyo?','Tên bạn là gì?'),(8,'만나서 반갑습니다','mannaseo bangapseumnida','Rất vui được gặp bạn');
/*!40000 ALTER TABLE `speaking_exercises` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `speaking_room`
--

DROP TABLE IF EXISTS `speaking_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `speaking_room` (
  `room_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `max_participants` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `created_by` bigint NOT NULL,
  PRIMARY KEY (`room_id`),
  KEY `FKn15quhhulj5fehyr875usj7sr` (`created_by`),
  CONSTRAINT `FKn15quhhulj5fehyr875usj7sr` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `speaking_room`
--

LOCK TABLES `speaking_room` WRITE;
/*!40000 ALTER TABLE `speaking_room` DISABLE KEYS */;
INSERT INTO `speaking_room` VALUES (1,'2026-04-05 13:22:52.629458','uu',_binary '',10,'hello',1);
/*!40000 ALTER TABLE `speaking_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theory`
--

DROP TABLE IF EXISTS `theory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theory` (
  `theory_id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `lesson_id` bigint NOT NULL,
  PRIMARY KEY (`theory_id`),
  KEY `FKnn9pe4r8s3su4hnk8ol4qubg7` (`lesson_id`),
  CONSTRAINT `FKnn9pe4r8s3su4hnk8ol4qubg7` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theory`
--

LOCK TABLES `theory` WRITE;
/*!40000 ALTER TABLE `theory` DISABLE KEYS */;
INSERT INTO `theory` VALUES (1,'### A. Từ vựng Tiếng Hàn tổng hợp sơ cấp 1 bài 1\n\n**1. Quốc gia (국가):**\n* 한국: Hàn Quốc (Hán gốc: 韓國 - Hàn Quốc)\n* 베트남: Việt Nam\n* 일본: Nhật Bản (Hán gốc: 日本 - Nhật Bản)\n* 미국: Mỹ (Hán gốc: 美國 - Mỹ Quốc)\n* 중국: Trung Quốc (Hán gốc: 中國 - Trung Quốc)\n* 영국: Anh (Hán gốc: 英國 - Anh Quốc)\n* 독일: Đức (Hán gốc: 獨逸 - Độc Dật)\n* 태국: Thái Lan / 호주: Úc / 몽골: Mông Cổ / 러시아: Nga / Pháp: 프랑스\n\n**2. Chào hỏi (인사말):**\n* 안녕하세요 / 안녕하십니까: Xin chào.\n* 안녕히 가세요: Tạm biệt (người đi).\n* 안녕히 계세요: Tạm biệt (người ở lại).\n* 처음 뵙겠습니다: Rất hân hạnh (Lần đầu gặp mặt).\n* 반갑습니다: Rất vui được gặp bạn.\n\n**3. Nghề nghiệp & Trường học (직업 & 학교):**\n* 학생: Học sinh / 대학생: Sinh viên.\n* 선생님: Giáo viên.\n* 회사원: Nhân viên công ty / 공무원: Công chức.\n* 의사: Bác sĩ / 약사: Dược sĩ.\n* 은행원: Nhân viên ngân hàng.\n* 대학교: Trường đại học / 학과: Khoa / 학번: Mã số sinh viên.\n\n---\n\n### B. Ngữ pháp Tiếng Hàn tổng hợp sơ cấp 1 bài 1\n\n**1. Cấu trúc 입니다 và 입니까?**\n* **N + 입니다:** Có nghĩa là \"là N\". Đây là đuôi câu trần thuật trang trọng.\n  * *Ví dụ:* 저는 하 입니다 (Tôi là Hà).\n* **N + 입니까?:** Có nghĩa là \"là N phải không?\". Đây là hình thức nghi vấn.\n  * *Ví dụ:* 하 입니까? (Bạn là Hà phải không?)\n  * *Trả lời:* 네, ...입니다 (Vâng là...) / 아니요, ...아닙니다 (Không, không phải là...).\n\n**2. Tiểu từ chủ ngữ 은/는**\nGắn vào sau danh từ để chỉ định danh từ đó là chủ thể của hành động hoặc chủ đề của câu.\n* **Danh từ có phụ âm cuối (Batchim) + 은.**\n  * *Ví dụ:* 이름 -> 이름은 (이름은 안입니다 - Tên tôi là An).\n* **Danh từ không có phụ âm cuối (Batchim) + 는.**\n  * *Ví dụ:* 조민재 씨 -> 조민재 씨는 (조민재 씨는 회사원입니다 - Jo Min Jae là nhân viên công ty).',1),(2,'### A. Từ vựng Bài 2: Trường học (학교)\n\n**1. Địa điểm (장소):**\n* 학교: Trường học / 도서관: Thư viện\n* 식당: Nhà ăn / 호텔: Khách sạn\n* 병원: Bệnh viện / 은행: Ngân hàng\n* 백화점: Cửa hàng bách hóa / 약국: Hiệu thuốc\n\n**2. Cơ sở vật chất (시설):**\n* 교실: Phòng học / 강의실: Giảng đường\n* 화장실: Nhà vệ sinh / 휴게실: Phòng nghỉ\n* 운동장: Sân vận động / 체육관: Nhà thi đấu\n\n**3. Đồ vật trong lớp (교실 물건):**\n* 책상: Bàn / 의자: Ghế\n* 칠판: Bảng / 시계: Đồng hồ\n* 책: Sách / 공책: Vở / 사전: Từ điển\n* 가방: Cặp / 컴퓨터: Máy tính\n\n---\n\n### B. Ngữ pháp Bài 2\n\n**1. Đại từ chỉ định địa điểm: 여기, 거기, 저기**\n- **여기**: Đây (gần người nói).\n- **거기**: Đó (gần người nghe).\n- **저기**: Kia (xa cả hai).\n* *Ví dụ:* 여기는 도서관입니다 (Đây là thư viện).\n\n**2. Đại từ chỉ định đồ vật: 이것, 그것, 저것**\n- **이것**: Cái này (gần người nói).\n- **그것**: Cái đó (gần người nghe).\n- **저것**: Cái kia (xa cả hai).\n* *Ví dụ:* 이것은 책입니다 (Cái này là quyển sách).\n\n**3. Tiểu từ chủ ngữ 이/가**\n- Danh từ có Batchim + **이**.\n- Danh từ không có Batchim + **가**.\n- Luôn dùng với: **있다** (Có), **없다** (Không có), **많다** (Nhiều).\n* *Ví dụ:* 책이 있습니다 (Có sách).\n\n**4. Cấu trúc 에 있습니다/없습니다 (Có/Không có ở...)**\nDiễn tả vị trí của người hoặc vật.\n* *Ví dụ:* 교실에 컴퓨터가 있습니다 (Trong phòng học có máy tính).\n\n**5. Cấu trúc 이/가 아닙니다 (Không phải là...)**\nLà dạng phủ định của \"입니다\".\n* *Ví dụ:* 아니요, 사전이 아닙니다 (Không, không phải là từ điển).\');',2),(3,'-- Bài 03 — 일상생활 (Sinh hoạt hàng ngày)\n\nGiới thiệu ngắn:\nBài này giúp người học mô tả các hoạt động thường ngày như ăn uống, học tập và nghỉ ngơi.\n\nMục tiêu:\n- Nói về thói quen hàng ngày.\n- Sử dụng động từ ở dạng lịch sự hiện tại.\n\nNgữ pháp/Kỹ năng chính:\n1) Chia động từ dạng lịch sự: 아/어/여요\n Ví dụ: 공부하다 → 공부해요 (học)\n\n2) Trạng từ chỉ thời gian: 아침, 점심, 저녁\n Ví dụ: 아침에 공부해요. (Tôi học vào buổi sáng.)\n\n3) Trợ từ 에 dùng cho thời gian\n\nTừ vựng chính:\n- 일어나다 — thức dậy\n- 먹다 — ăn\n- 공부하다 — học\n- 자다 — ngủ\n- 쉬다 — nghỉ\n\nPhát âm/Chú ý phát âm:\n- Động từ kết thúc bằng nguyên âm sẽ nối âm tự nhiên.\n\nVí dụ mẫu:\n1) 저는 아침에 공부해요.\n (Jeoneun achime gongbuhaeyo.) — Tôi học vào buổi sáng.\n2) 저녁에 밥을 먹어요.\n (Jeonyeoge babeul meogeoyo.) — Tôi ăn cơm buổi tối.\n\nGhi chú văn hóa:\n- Người Hàn thường có lịch sinh hoạt rất đều đặn.\n',3),(4,'-- Bài 04 — 날짜와 요일 (Ngày và thứ)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về ngày tháng và thứ trong tuần.\n\nMục tiêu:\n- Biết cách nói ngày tháng.\n- Hỏi và trả lời về lịch hẹn.\n\nNgữ pháp/Kỹ năng chính:\n1) Trợ từ 에 (chỉ thời gian)\n Ví dụ: 월요일에 만나요. (Gặp vào thứ hai.)\n\n2) Câu hỏi 언제 (khi nào)\n Ví dụ: 언제 만나요? (Khi nào gặp?)\n\n3) Số đếm ngày tháng (Hán Hàn)\n\nTừ vựng chính:\n- 월요일 — thứ hai\n- 화요일 — thứ ba\n- 날짜 — ngày\n- 오늘 — hôm nay\n- 내일 — ngày mai\n\nPhát âm/Chú ý phát âm:\n- Âm 요일 đọc nối liền nhanh trong hội thoại.\n\nVí dụ mẫu:\n1) 오늘은 월요일이에요.\n (Oneureun woryoirieyo.) — Hôm nay là thứ hai.\n2) 내일 만나요.\n (Naeil mannayo.) — Gặp ngày mai nhé.\n\nGhi chú văn hóa:\n- Lịch Hàn Quốc thường bắt đầu tuần từ Chủ nhật.\n',4),(5,'-- Bài 05 — 하루 일과 (Công việc trong ngày)\n\nGiới thiệu ngắn:\nBài này giúp người học mô tả các hoạt động diễn ra trong một ngày từ sáng đến tối.\n\nMục tiêu:\n- Mô tả lịch trình hàng ngày.\n- Sử dụng trạng từ chỉ thời gian.\n\nNgữ pháp/Kỹ năng chính:\n1) Trạng từ thời gian: 아침, 점심, 저녁\n Ví dụ: 아침에 일어나요.\n\n2) Động từ dạng hiện tại lịch sự\n\nTừ vựng chính:\n- 일어나다 — thức dậy\n- 출근하다 — đi làm\n- 공부하다 — học\n- 자다 — ngủ\n\nVí dụ mẫu:\n1) 저는 아침에 일어나요.\n2) 저녁에 공부해요.\n\nGhi chú văn hóa:\n- Người Hàn có thói quen sinh hoạt rất đúng giờ.\n',5),(6,'-- Bài 06 — 주말 (Cuối tuần)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về hoạt động cuối tuần.\n\nMục tiêu:\n- Nói về kế hoạch và sở thích cuối tuần.\n\nNgữ pháp/Kỹ năng chính:\n1) Động từ + 고 싶어요 (muốn)\n Ví dụ: 영화 보고 싶어요.\n\nTừ vựng chính:\n- 주말 — cuối tuần\n- 영화 — phim\n- 친구 — bạn\n\nVí dụ mẫu:\n1) 주말에 뭐 해요?\n2) 친구를 만나요.\n\nGhi chú văn hóa:\n- Cuối tuần là thời gian nghỉ ngơi và gặp gỡ bạn bè.\n',6),(7,'-- Bài 07 — 물건 사기 (1) (Mua sắm)\n\nGiới thiệu ngắn:\nBài này giúp người học giao tiếp cơ bản khi mua hàng.\n\nMục tiêu:\n- Hỏi giá và chọn hàng.\n\nNgữ pháp/Kỹ năng chính:\n1) 얼마예요? (bao nhiêu tiền)\n2) 주세요 (làm ơn đưa tôi)\n\nTừ vựng chính:\n- 물건 — đồ vật\n- 가격 — giá\n- 싸다 — rẻ\n\nVí dụ mẫu:\n1) 이거 얼마예요?\n2) 이거 주세요.\n\nGhi chú văn hóa:\n- Người Hàn thường thanh toán nhanh, ít mặc cả.\n',7),(8,'-- Bài 08 — 음식 (Thức ăn)\n\nGiới thiệu ngắn:\nBài này giúp người học gọi món và nói về sở thích ăn uống.\n\nMục tiêu:\n- Gọi món trong nhà hàng.\n\nNgữ pháp/Kỹ năng chính:\n1) 좋아하다 (thích)\n Ví dụ: 김치를 좋아해요.\n\nTừ vựng chính:\n- 음식 — đồ ăn\n- 밥 — cơm\n- 물 — nước\n\nVí dụ mẫu:\n1) 김치를 좋아해요.\n2) 물 주세요.\n\nGhi chú văn hóa:\n- Bữa ăn Hàn Quốc thường có nhiều món ăn kèm.\n',8),(9,'-- Bài 09 — 집 (Nhà cửa)\n\nGiới thiệu ngắn:\nBài này giúp người học mô tả nhà và vị trí đồ vật.\n\nMục tiêu:\n- Mô tả không gian sống.\n\nNgữ pháp/Kỹ năng chính:\n1) 있다/없다 (có/không có)\n\nTừ vựng chính:\n- 집 — nhà\n- 방 — phòng\n- 부엌 — bếp\n\nVí dụ mẫu:\n1) 집이 커요.\n2) 방이 있어요.\n\nGhi chú văn hóa:\n- Nhà Hàn thường có hệ thống sưởi dưới sàn.\n',9),(10,'-- Bài 10 — 가족 (Gia đình)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về các thành viên trong gia đình.\n\nMục tiêu:\n- Giới thiệu gia đình.\n\nNgữ pháp/Kỹ năng chính:\n1) 있다 (có)\n\nTừ vựng chính:\n- 가족 — gia đình\n- 아버지 — bố\n- 어머니 — mẹ\n\nVí dụ mẫu:\n1) 가족이 있어요.\n2) 아버지가 있어요.\n\nGhi chú văn hóa:\n- Gia đình là yếu tố rất quan trọng trong xã hội Hàn.\n',10),(11,'-- Bài 11 — 날씨 (Thời tiết)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về thời tiết.\n\nMục tiêu:\n- Hỏi và mô tả thời tiết.\n\nNgữ pháp/Kỹ năng chính:\n1) 형용사 + 아/어요\n\nTừ vựng chính:\n- 날씨 — thời tiết\n- 덥다 — nóng\n- 춥다 — lạnh\n\nVí dụ mẫu:\n1) 날씨가 좋아요.\n2) 오늘 더워요.\n\nGhi chú văn hóa:\n- Hàn Quốc có 4 mùa rõ rệt.\n',11),(12,'-- Bài 12 — 전화 (1) (Điện thoại)\n\nGiới thiệu ngắn:\nBài này giúp người học giao tiếp qua điện thoại.\n\nMục tiêu:\n- Bắt đầu và kết thúc cuộc gọi.\n\nNgữ pháp/Kỹ năng chính:\n1) 여보세요 (alo)\n\nTừ vựng chính:\n- 전화 — điện thoại\n- 번호 — số\n\nVí dụ mẫu:\n1) 여보세요?\n2) 다시 전화할게요.\n\nGhi chú văn hóa:\n- Luôn dùng lời chào lịch sự khi gọi điện.\n',12),(13,'-- Bài 13 — 생일 (Sinh nhật)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về sinh nhật.\n\nMục tiêu:\n- Hỏi và trả lời về ngày sinh.\n\nNgữ pháp/Kỹ năng chính:\n1) 언제 (khi nào)\n\nTừ vựng chính:\n- 생일 — sinh nhật\n- 선물 — quà\n\nVí dụ mẫu:\n1) 생일이 언제예요?\n2) 축하해요!\n\nGhi chú văn hóa:\n- Sinh nhật thường được tổ chức cùng gia đình.\n',13),(14,'-- Bài 14 — 취미 (Sở thích)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về sở thích cá nhân.\n\nMục tiêu:\n- Diễn đạt sở thích.\n\nNgữ pháp/Kỹ năng chính:\n1) 좋아하다 (thích)\n\nTừ vựng chính:\n- 취미 — sở thích\n- 운동 — thể thao\n\nVí dụ mẫu:\n1) 운동을 좋아해요.\n2) 음악을 들어요.\n\nGhi chú văn hóa:\n- Người Hàn thích các hoạt động nhóm.\n',14),(15,'-- Bài 15 — 교통 (1) (Giao thông)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về phương tiện di chuyển.\n\nMục tiêu:\n- Nói cách di chuyển.\n\nNgữ pháp/Kỹ năng chính:\n1) 타다 (đi phương tiện)\n\nTừ vựng chính:\n- 버스 — xe buýt\n- 지하철 — tàu điện\n\nVí dụ mẫu:\n1) 버스를 타요.\n2) 지하철을 타요.\n\nGhi chú văn hóa:\n- Giao thông công cộng Hàn rất tiện lợi.\n',15),(16,'-- Bài 01 — 만남 (Gặp gỡ)\n\nGiới thiệu ngắn:\nBài này giúp người học giao tiếp khi gặp gỡ người khác, hỏi thăm và phản ứng trong các tình huống xã giao cơ bản.\n\nMục tiêu:\n- Biết cách chào hỏi và phản hồi khi gặp người quen.\n- Sử dụng câu hỏi đơn giản trong hội thoại.\n\nNgữ pháp/Kỹ năng chính:\n1) Câu hỏi với 뭐 (gì)\n Ví dụ: 뭐 해요? (Bạn đang làm gì?)\n2) Câu hỏi với 누구 (ai)\n Ví dụ: 누구예요? (Đó là ai?)\n\nTừ vựng chính:\n- 만나다 — gặp\n- 친구 — bạn\n- 사람 — người\n\nVí dụ mẫu:\n1) 오랜만이에요! — Lâu rồi không gặp!\n2) 잘 지냈어요? — Bạn có khỏe không?\n\nGhi chú văn hóa:\n- Người Hàn rất coi trọng phép lịch sự khi gặp gỡ lần đầu.\n',16),(17,'-- Bài 02 — 약속 (Hẹn gặp)\n\nGiới thiệu ngắn:\nBài này giúp người học đặt lịch hẹn và trao đổi về thời gian.\n\nMục tiêu:\n- Biết cách hẹn gặp và xác nhận thời gian.\n- Sử dụng giờ giấc trong câu.\n\nNgữ pháp/Kỹ năng chính:\n1) Cấu trúc 시간 + 에 (vào lúc)\n Ví dụ: 3시에 만나요. (Gặp lúc 3 giờ)\n2) Đề nghị: -ㄹ까요?\n Ví dụ: 만날까요? (Chúng ta gặp nhé?)\n\nTừ vựng chính:\n- 약속 — cuộc hẹn\n- 시간 — thời gian\n- 오늘 — hôm nay\n\nVí dụ mẫu:\n1) 몇 시에 만나요? — Mấy giờ gặp?\n2) 내일 만나요. — Gặp ngày mai nhé.\n\nGhi chú văn hóa:\n- Người Hàn thường đúng giờ, trễ hẹn bị coi là thiếu tôn trọng.\n',17),(18,'-- Bài 03 — 물건 사기 (2) (Mua sắm)\n\nGiới thiệu ngắn:\nBài này giúp người học giao tiếp khi mua hàng và hỏi giá.\n\nMục tiêu:\n- Hỏi giá và số lượng.\n- Sử dụng số đếm cơ bản.\n\nNgữ pháp/Kỹ năng chính:\n1) 얼마예요? (Bao nhiêu tiền?)\n2) Số + 개 (cái)\n\nTừ vựng chính:\n- 물건 — đồ vật\n- 가격 — giá\n- 싸다 — rẻ\n\nVí dụ mẫu:\n1) 이거 얼마예요? — Cái này bao nhiêu tiền?\n2) 두 개 주세요. — Cho tôi 2 cái.\n\nGhi chú văn hóa:\n- Có thể mặc cả nhẹ ở chợ truyền thống.\n',18),(19,'-- Bài 04 — 병원 (Bệnh viện)\n\nGiới thiệu ngắn:\nBài này giúp người học nói về tình trạng sức khỏe và đi khám bệnh.\n\nMục tiêu:\n- Mô tả triệu chứng cơ bản.\n- Giao tiếp với bác sĩ.\n\nNgữ pháp/Kỹ năng chính:\n1) 아/어/여요 để mô tả trạng thái\n Ví dụ: 아파요 (Đau)\n\nTừ vựng chính:\n- 병원 — bệnh viện\n- 의사 — bác sĩ\n- 약 — thuốc\n\nVí dụ mẫu:\n1) 머리가 아파요. — Tôi đau đầu.\n2) 병원에 가요. — Tôi đi bệnh viện.\n\nGhi chú văn hóa:\n- Người Hàn thường đi khám ngay khi có triệu chứng.\n',19),(20,'-- Bài 05 — 편지 (Thư tín)\n\nGiới thiệu ngắn:\nBài này giúp người học viết thư và email cơ bản.\n\nMục tiêu:\n- Viết lời chào đầu và kết thư.\n- Sử dụng câu lịch sự.\n\nNgữ pháp/Kỹ năng chính:\n1) Kết thúc câu lịch sự -습니다\n\nTừ vựng chính:\n- 편지 — thư\n- 이메일 — email\n\nVí dụ mẫu:\n1) 잘 지내세요? — Bạn khỏe không?\n2) 감사합니다. — Cảm ơn.\n\nGhi chú văn hóa:\n- Văn phong lịch sự rất quan trọng trong thư từ.\n',20),(21,'-- Bài 06 — 교통 (2) (Giao thông)\n\nGiới thiệu ngắn:\nBài này giúp người học hỏi đường và sử dụng phương tiện giao thông.\n\nMục tiêu:\n- Hỏi và chỉ đường.\n- Nói về phương tiện.\n\nNgữ pháp/Kỹ năng chính:\n1) 어디에 있어요? (Ở đâu?)\n\nTừ vựng chính:\n- 버스 — xe buýt\n- 지하철 — tàu điện\n\nVí dụ mẫu:\n1) 어디에 가요? — Bạn đi đâu?\n2) 버스를 타요. — Đi xe buýt.\n\nGhi chú văn hóa:\n- Giao thông công cộng ở Hàn rất phát triển.\n',21),(22,'-- Bài 07 — 전화 (2) (Điện thoại)\n\nGiới thiệu ngắn:\nBài này giúp người học giao tiếp qua điện thoại.\n\nMục tiêu:\n- Bắt máy và kết thúc cuộc gọi.\n\nNgữ pháp/Kỹ năng chính:\n1) 여보세요 (Alo)\n\nTừ vựng chính:\n- 전화 — điện thoại\n- 번호 — số\n\nVí dụ mẫu:\n1) 여보세요? — Alo?\n2) 나중에 다시 전화할게요. — Tôi gọi lại sau.\n\nGhi chú văn hóa:\n- Luôn dùng lời chào lịch sự khi gọi điện.\n',22);
/*!40000 ALTER TABLE `theory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `current_level` int DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `is_locked` bit(1) DEFAULT NULL,
  `last_active_date` date DEFAULT NULL,
  `last_login_at` datetime(6) DEFAULT NULL,
  `lock_until` datetime(6) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(20) DEFAULT NULL,
  `streak_count` int DEFAULT NULL,
  `theme` varchar(10) DEFAULT NULL,
  `total_xp` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2026-04-04 07:21:11.025742',1,'bvanh004@gmail.com',_binary '\0','2026-04-06','2026-04-06 01:09:59.482737',NULL,'Bùi Việt Anh','$2a$10$EcuYHm4ZH7L.Mh3MfPm7SecUNs2Ekz8yxTWsIZwxPYh830Cqits1G','ROLE_LEARNER',3,'dark',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_answer`
--

DROP TABLE IF EXISTS `user_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_answer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_correct` bit(1) DEFAULT NULL,
  `pronunciation_score` decimal(5,2) DEFAULT NULL,
  `stt_result` text,
  `submitted_at` datetime(6) DEFAULT NULL,
  `written_response` text,
  `question_id` bigint NOT NULL,
  `selected_answer_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpsk90eok3ounaet92hku3gny1` (`question_id`),
  KEY `FKkic8j4t9owhi3adaugdsbj8co` (`selected_answer_id`),
  KEY `FK7kw17ur9w2egkc51xua3yh0ux` (`user_id`),
  CONSTRAINT `FK7kw17ur9w2egkc51xua3yh0ux` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKkic8j4t9owhi3adaugdsbj8co` FOREIGN KEY (`selected_answer_id`) REFERENCES `answer` (`answer_id`),
  CONSTRAINT `FKpsk90eok3ounaet92hku3gny1` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_answer`
--

LOCK TABLES `user_answer` WRITE;
/*!40000 ALTER TABLE `user_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_badge`
--

DROP TABLE IF EXISTS `user_badge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_badge` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `earned_at` datetime(6) DEFAULT NULL,
  `badge_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_badge` (`user_id`,`badge_id`),
  KEY `FKjqx9n26pk9mqf1qo8f7xvvoq9` (`badge_id`),
  CONSTRAINT `FK2jw9fpotmmbda07k27qc9t2ul` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKjqx9n26pk9mqf1qo8f7xvvoq9` FOREIGN KEY (`badge_id`) REFERENCES `badge` (`badge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_badge`
--

LOCK TABLES `user_badge` WRITE;
/*!40000 ALTER TABLE `user_badge` DISABLE KEYS */;
INSERT INTO `user_badge` VALUES (1,'2026-04-05 17:10:57.715512',5,1);
/*!40000 ALTER TABLE `user_badge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_progress`
--

DROP TABLE IF EXISTS `user_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `score` int DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `lesson_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_lesson` (`user_id`,`lesson_id`),
  KEY `FK4ru0nd5qoi60ak3bdoeweluwv` (`lesson_id`),
  CONSTRAINT `FK4ru0nd5qoi60ak3bdoeweluwv` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`),
  CONSTRAINT `FKdpcn9k9uoj0uh6eenim54gvng` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_progress`
--

LOCK TABLES `user_progress` WRITE;
/*!40000 ALTER TABLE `user_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vocab_words`
--

DROP TABLE IF EXISTS `vocab_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vocab_words` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `example` varchar(500) DEFAULT NULL,
  `kr` varchar(100) NOT NULL,
  `roman` varchar(100) NOT NULL,
  `vi` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vocab_words`
--

LOCK TABLES `vocab_words` WRITE;
/*!40000 ALTER TABLE `vocab_words` DISABLE KEYS */;
INSERT INTO `vocab_words` VALUES (1,'Giao tiếp','안녕하세요! 만나서 반갑습니다.','안녕하세요','annyeonghaseyo','Xin chào (formal)'),(2,'Giao tiếp','도와주셔서 감사합니다.','감사합니다','gamsahamnida','Cảm ơn (formal)'),(3,'Giao tiếp','늦어서 죄송합니다.','죄송합니다','joesonghamnida','Xin lỗi (formal)'),(4,'Giao tiếp','네, 알겠습니다.','네','ne','Vâng / Dạ'),(5,'Giao tiếp','아니요, 괜찮습니다.','아니요','aniyo','Không'),(6,'Giao tiếp','안녕히 가세요, 조심히 가세요.','안녕히 가세요','annyeonghi gaseyo','Tạm biệt (chào người đi)'),(7,'Giao tiếp','','안녕히 계세요','annyeonghi gyeseyo','Tạm biệt (chào người ở lại)'),(8,'Giao tiếp','','만나서 반갑습니다','mannaseo bangapseumnida','Rất vui được gặp bạn'),(9,'Giao tiếp','','이름이 뭐예요?','ireumi mwoyeyo?','Tên bạn là gì?'),(10,'Giao tiếp','베트남에서 왔어요.','어디에서 왔어요?','eodieseo wasseoyo?','Bạn đến từ đâu?'),(11,'Gia đình','우리 가족은 네 명입니다.','가족','gajok','Gia đình'),(12,'Gia đình','아버지는 회사에 가십니다.','아버지','abeoji','Bố'),(13,'Gia đình','어머니는 요리를 잘합니다.','어머니','eomeoni','Mẹ'),(14,'Gia đình','형은 대학생입니다.','형','hyeong','Anh trai (nam gọi)'),(15,'Gia đình','오빠, 밥 먹었어요?','오빠','oppa','Anh trai (nữ gọi)'),(16,'Gia đình','누나는 예뻐요.','누나','nuna','Chị gái (nam gọi)'),(17,'Gia đình','언니하고 같이 쇼핑해요.','언니','eonni','Chị gái (nữ gọi)'),(18,'Gia đình','내 동생은 귀여워요.','동생','dongsaeng','Em'),(19,'Trường học','학교에 가요.','학교','hakgyo','Trường học'),(20,'Trường học','저는 학생입니다.','학생','haksaeng','Học sinh'),(21,'Trường học','선생님, 안녕하세요?','선생님','seonsaengnim','Giáo viên'),(22,'Trường học','교실에 학생이 많아요.','교실','gyosil','Phòng học'),(23,'Trường học','도서관에서 책을 읽어요.','도서관','doseogwan','Thư viện'),(24,'Trường học','내일 시험이 있어요.','시험','siheom','Kỳ thi / Bài kiểm tra'),(25,'Trường học','숙제를 다 했어요.','숙제','sukje','Bài tập về nhà'),(26,'Trường học','한국어 책을 샀어요.','책','chaek','Sách'),(27,'Mua sắm','이 사과 얼마예요?','얼마예요?','eolmayeyo?','Bao nhiêu tiền?'),(28,'Mua sắm','너무 비싸요.','비싸요','bissayo','Mắc / Đắt'),(29,'Mua sắm','이 옷은 정말 싸요.','싸요','ssayo','Rẻ'),(30,'Mua sắm','','조금 깎아 주세요','jogeum kkakka juseyo','Giảm giá một chút đi'),(31,'Mua sắm','카드로 계산할게요.','카드','kadeu','Thẻ tín dụng'),(32,'Mua sắm','현금 있으세요?','현금','hyeongeum','Tiền mặt'),(33,'Mua sắm','옷 가게에서 옷을 샀어요.','옷','ot','Quần áo'),(34,'Mua sắm','다른 사이즈 있나요?','사이즈','saijeu','Kích cỡ'),(35,'Du lịch','한국으로 여행을 갑니다.','여행','yeohaeng','Du lịch'),(36,'Du lịch','인천 공항에 도착했어요.','공항','gonghang','Sân bay'),(37,'Du lịch','비행기를 탔어요.','비행기','bihaenggi','Máy bay'),(38,'Du lịch','여권을 보여주세요.','여권','yeogwon','Hộ chiếu'),(39,'Du lịch','호텔을 예약했어요.','호텔','hotel','Khách sạn'),(40,'Du lịch','지하철역이 어디에 있어요?','지하철','jihacheol','Tàu điện ngầm'),(41,'Du lịch','택시를 타고 가요.','택시','taeksi','Taxi'),(42,'Du lịch','지도를 보세요.','지도','jido','Bản đồ'),(43,'Du lịch','화장실이 어디예요?','어디예요?','eodiyeyo?','Ở đâu?'),(44,'Khác','커피 하나 주세요.','하나','hana','Một'),(45,'Khác','맥주 두 병 주세요.','둘','dul','Hai'),(46,'Khác','오늘은 날씨가 따뜻해요.','오늘','oneul','Hôm nay'),(47,'Khác','내일 만나요.','내일','naeil','Ngày mai'),(48,'Khác','지금 몇 시예요?','지금','jigeum','Bây giờ');
/*!40000 ALTER TABLE `vocab_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vocabulary`
--

DROP TABLE IF EXISTS `vocabulary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vocabulary` (
  `vocab_id` bigint NOT NULL AUTO_INCREMENT,
  `audio_url` varchar(500) DEFAULT NULL,
  `example_sentence` text,
  `hangul` varchar(100) NOT NULL,
  `meaning` varchar(255) NOT NULL,
  `romanization` varchar(100) DEFAULT NULL,
  `lesson_id` bigint NOT NULL,
  PRIMARY KEY (`vocab_id`),
  KEY `FK1k8kfdy98mb19db9pu5pnmt3o` (`lesson_id`),
  CONSTRAINT `FK1k8kfdy98mb19db9pu5pnmt3o` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`lesson_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vocabulary`
--

LOCK TABLES `vocabulary` WRITE;
/*!40000 ALTER TABLE `vocabulary` DISABLE KEYS */;
INSERT INTO `vocabulary` VALUES (1,NULL,'저는 한국 사람입니다. (Tôi là người Hàn Quốc.)','한국','Hàn Quốc','hanguk',1),(2,NULL,'베트남 사람입니까? (Bạn là người Việt Nam phải không?)','베트남','Việt Nam','beteunam',1),(3,NULL,'저는 학생입니다. (Tôi là học sinh.)','말레이시아','Malaysia','malleisia',1),(4,NULL,'일본에서 왔습니다. (Tôi đến từ Nhật Bản.)','일본','Nhật Bản','ilbon',1),(5,NULL,'미국 사람입니다. (Tôi là người Mỹ.)','미국','Mỹ','miguk',1),(6,NULL,'중국 사람입니까? (Bạn là người Trung Quốc à?)','중국','Trung Quốc','jungguk',1),(7,NULL,'저는 학생입니다. (Tôi là học sinh.)','태국','Thái Lan','taeguk',1),(8,NULL,'저는 학생입니다. (Tôi là học sinh.)','호주','Úc','hoju',1),(9,NULL,'저는 학생입니다. (Tôi là học sinh.)','몽골','Mông Cổ','monggol',1),(10,NULL,'저는 학생입니다. (Tôi là học sinh.)','인도네시아','Indonesia','indonesia',1),(11,NULL,'저는 학생입니다. (Tôi là học sinh.)','필리핀','Philippines','pillipin',1),(12,NULL,'저는 학생입니다. (Tôi là học sinh.)','인도','Ấn Độ','indo',1),(13,NULL,'저는 학생입니다. (Tôi là học sinh.)','영국','Anh','yeongguk',1),(14,NULL,'저는 학생입니다. (Tôi là học sinh.)','프랑스','Pháp','peurangseu',1),(15,NULL,'저는 학생입니다. (Tôi là học sinh.)','독일','Đức','dogil',1),(16,NULL,'저는 학생입니다. (Tôi là học sinh.)','러시아','Nga','reosia',1),(17,NULL,'안녕하세요? 제 이름 là Lan입니다. (Xin chào? Tên tôi là Lan.)','안녕하세요','Xin chào','annyeong-haseyo',1),(18,NULL,'안녕하십니까? 처음 뵙겠습니다. (Xin chào? Rất hân hạnh được gặp.)','안녕하십니까','Xin chào (trịnh trọng)','annyeong-hasimnikka',1),(19,NULL,'저는 학생입니다. (Tôi là học sinh.)','안녕히 가세요','Xin tạm biệt (Đi về bình an)','annyeonghi gaseyo',1),(20,NULL,'저는 학생입니다. (Tôi là học sinh.)','안녕히 계세요','Xin tạm biệt (Ở lại bình an)','annyeonghi gyeseyo',1),(21,NULL,'저는 학생입니다. (Tôi là học sinh.)','처음 뵙겠습니다','Rất hân hạnh (Lần đầu gặp mặt)','cheoeum boepgetseumnida',1),(22,NULL,'저는 학생입니다. (Tôi là học sinh.)','반갑습니다','Rất vui được gặp','bangapseumnida',1),(23,NULL,'저는 학생입니다. (Tôi là học sinh.)','국어국문학과','Khoa Ngữ văn','gugeogukmunhak-gwa',1),(24,NULL,'국적이 어디입니까? (Quốc tịch của bạn ở đâu/là gì?)','국적','Quốc tịch','gukjeok',1),(25,NULL,'저는 학생입니다. (Tôi là học sinh.)','네','Vâng','ne',1),(26,NULL,'여기는 대학교입니다. (Đây là trường đại học.)','대학교','Trường đại học','daehakkyo',1),(27,NULL,'저는 대학생입니다. (Tôi là sinh viên đại học.)','대학생','Sinh viên','daehaksaeng',1),(28,NULL,'저는 학생입니다. (Tôi là học sinh.)','보기','Mẫu, ví dụ','bogi',1),(29,NULL,'어느 나라 사람입니까? (Bạn là người nước nào?)','사람','Người','saram',1),(30,NULL,'저는 학생입니다. (Tôi là học sinh.)','씨','Bạn/Anh/Chị','ssi',1),(31,NULL,'저는 학생입니다. (Tôi là học sinh.)','아니요','Không','aniyo',1),(32,NULL,'저는 학생입니다. (Tôi là học sinh.)','은행','Ngân hàng','eunhaeng',1),(33,NULL,'학생입니까? (Bạn là học sinh phải không?)','학생','Học sinh','haksaeng',1),(34,NULL,'저는 학생입니다. (Tôi là học sinh.)','이','Này','i',1),(35,NULL,'그 사람은 회사원입니다. (Người đó là nhân viên công ty.)','회사원','Nhân viên công ty','hoesawon',1),(36,NULL,'이름이 무엇입니까? (Tên bạn là gì?)','이름','Tên','ireum',1),(37,NULL,'저는 학생입니다. (Tôi là học sinh.)','은행원','Nhân viên ngân hàng','eunhaeng-won',1),(38,NULL,'저는 학생입니다. (Tôi là học sinh.)','이메일','Email','imeil',1),(39,NULL,'이분은 선생님입니다. (Vị này là giáo viên.)','선생님','Giáo viên','seonsaengnim',1),(40,NULL,'저는 학생입니다. (Tôi là học sinh.)','저','Tôi','jeo',1),(41,NULL,'저는 학생입니다. (Tôi là học sinh.)','공무원','Công chức','gongmuwon',1),(42,NULL,'저는 학생입니다. (Tôi là học sinh.)','전화','Điện thoại','jeonhwa',1),(43,NULL,'형은 의사입니다. (Anh trai là bác sĩ.)','의사','Bác sĩ','uisa',1),(44,NULL,'저는 학생입니다. (Tôi là học sinh.)','제','Của tôi','je',1),(45,NULL,'저는 학생입니다. (Tôi là học sinh.)','관광 가이드','Hướng dẫn viên du lịch','gwan-gwang gaideu',1),(46,NULL,'저는 학생입니다. (Tôi là học sinh.)','주소','Địa chỉ','juso',1),(47,NULL,'저는 학생입니다. (Tôi là học sinh.)','주부','Nội trợ','jubu',1),(48,NULL,'직업이 무엇입니까? (Nghề nghiệp của bạn là gì?)','직업','Nghề nghiệp','jigeop',1),(49,NULL,'저는 학생입니다. (Tôi là học sinh.)','약사','Dược sĩ','yaksa',1),(50,NULL,'저는 학생입니다. (Tôi là học sinh.)','학과','Bộ môn/Khoa','hak-gwa',1),(51,NULL,'저는 학생입니다. (Tôi là học sinh.)','운전기사','Tài xế lái xe','unjeon-gisa',1),(52,NULL,'저는 학생입니다. (Tôi là học sinh.)','학번','Mã số sinh viên','hakbeon',1),(53,NULL,'한국어 선생님입니다. (Là giáo viên tiếng Hàn.)','한국어','Tiếng Hàn','han-gugeo',1),(54,NULL,'여기 학생증이 있습니다. (Có thẻ sinh viên ở đây.)','학생증','Thẻ sinh viên','haksaengjeung',1),(55,NULL,'저는 학생입니다. (Tôi là học sinh.)','한국어과','Khoa Hàn ngữ','han-gugeogwa',1);
/*!40000 ALTER TABLE `vocabulary` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-06 12:50:02
