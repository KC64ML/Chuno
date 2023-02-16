CREATE DATABASE  IF NOT EXISTS `chuno` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `chuno`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: i8d208.p.ssafy.io    Database: chuno
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `friend_id` bigint NOT NULL AUTO_INCREMENT,
  `mod_dt` datetime DEFAULT NULL,
  `reg_dt` datetime NOT NULL,
  `from_user_id` bigint DEFAULT NULL,
  `to_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`friend_id`),
  KEY `FKjm3q0fcaidpd7kre0n2ey89ye` (`from_user_id`),
  KEY `FK73xxjsnuk72k5w3cy33gpudrk` (`to_user_id`),
  CONSTRAINT `FK73xxjsnuk72k5w3cy33gpudrk` FOREIGN KEY (`to_user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKjm3q0fcaidpd7kre0n2ey89ye` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `inventory_key` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`inventory_key`),
  KEY `FKem4n7umseo46fdpsowncsbwac` (`item_id`),
  KEY `FK6s70ikopm646wy54vwowsnp6d` (`user_id`),
  CONSTRAINT `FK6s70ikopm646wy54vwowsnp6d` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKem4n7umseo46fdpsowncsbwac` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `for_runner` int NOT NULL,
  `img_path` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.',1,'item/item1.png','천리안',1500),(2,'추노꾼이 자신을 잡을 수 있는 범위를 축소한다.',1,'item/item2.png','위장',2000),(3,'진짜 노비문서의 위치를 확인할 수 있다.',1,'item/item3.png','확실한\n정보통',1000),(4,'먹물을 뿌려 내 화면을 가릴 수 있다.',1,'item/item4.png','먹물탄',1300),(5,'30초간 노비의 위치를 지도에 표시할 수 있다.',0,'item/item5.png','조명탄',1500),(6,'자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.',0,'item/item6.png','긴\n오랏줄',2000),(7,'노비 문서의 위치를 셔플할 수 있다.',0,'item/item7.png','거짓\n정보통',1000),(8,'연기를 피워 내 화면을 가릴 수 있다.',0,'item/item8.png','연막탄',1300);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pushes`
--

DROP TABLE IF EXISTS `pushes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pushes` (
  `push_id` bigint NOT NULL,
  `mod_dt` datetime DEFAULT NULL,
  `reg_dt` datetime NOT NULL,
  `room_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`push_id`),
  KEY `FKmqk5d2uytd0vkm8ffnjdf7lxb` (`room_id`),
  KEY `FK91gmw2kdpli771yly02hxmakf` (`user_id`),
  CONSTRAINT `FK91gmw2kdpli771yly02hxmakf` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKmqk5d2uytd0vkm8ffnjdf7lxb` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pushes`
--

LOCK TABLES `pushes` WRITE;
/*!40000 ALTER TABLE `pushes` DISABLE KEYS */;
/*!40000 ALTER TABLE `pushes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `room_id` bigint NOT NULL AUTO_INCREMENT,
  `current_players` int NOT NULL DEFAULT '1',
  `day` int NOT NULL,
  `hour` int NOT NULL,
  `minute` int NOT NULL,
  `month` int NOT NULL,
  `year` int NOT NULL,
  `is_public` bit(1) NOT NULL DEFAULT b'1',
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `max_players` int NOT NULL DEFAULT '10',
  `password` varchar(255) DEFAULT NULL,
  `radius` double NOT NULL,
  `title` varchar(255) NOT NULL,
  `host_id` bigint NOT NULL,
  PRIMARY KEY (`room_id`),
  KEY `FKoxd2xovikhdpk4si9ffhyb83i` (`host_id`),
  CONSTRAINT `FKoxd2xovikhdpk4si9ffhyb83i` FOREIGN KEY (`host_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,0,17,2,3,2,2023,_binary '',36.1052767,128.4213997,4,NULL,750,'ㄴㅅㄷ',2),(2,0,17,2,3,2,2023,_binary '',36.1161986,128.4350372,4,NULL,750,'cvxcvz',17),(3,0,17,5,3,2,2023,_binary '',36.10526887148899,128.42145411362708,4,NULL,750,'eueh',2);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `mod_dt` datetime DEFAULT NULL,
  `reg_dt` datetime NOT NULL,
  `catch_count` int NOT NULL DEFAULT '0',
  `chaser_play_count` int NOT NULL DEFAULT '0',
  `chaser_win_count` int NOT NULL DEFAULT '0',
  `email` varchar(40) NOT NULL,
  `exp` int NOT NULL DEFAULT '0',
  `is_deleted` bit(1) DEFAULT b'0',
  `is_manager` bit(1) NOT NULL DEFAULT b'0',
  `level` int NOT NULL DEFAULT '1',
  `money` int NOT NULL DEFAULT '0',
  `nickname` varchar(7) DEFAULT NULL,
  `paper_count` int NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `save_name` varchar(255) DEFAULT NULL,
  `runner_play_count` int NOT NULL DEFAULT '0',
  `runner_win_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_2ty1xmrrgtn89xt7kyxx6ta7h` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'2023-02-16 10:26:42',0,11,7,'ljc9393@nate.com',0,_binary '\0',_binary '\0',1,0,'큰개님',0,'01051411025','','',30,15),(2,NULL,'2023-02-16 10:26:42',0,11,7,'opi6@hanmail.net',200,_binary '\0',_binary '\0',1,2000,'인의동큰손',0,'01026896940','','',30,15),(3,NULL,'2023-02-16 10:26:42',0,11,7,'lce511@naver.com',0,_binary '\0',_binary '\0',1,85900,'채은짱님',0,'01084054759','','',30,15),(4,NULL,'2023-02-16 10:26:42',0,11,7,'souk0712@naver.com',0,_binary '\0',_binary '\0',1,80000,'모카',0,'01082860799','','',30,15),(5,NULL,'2023-02-16 10:26:42',0,11,7,'asdf@naver.com',0,_binary '\0',_binary '\0',1,0,'아무',0,'01515498451','','',13,15),(6,NULL,'2023-02-16 10:26:42',0,11,7,'qwer@naver.com',0,_binary '\0',_binary '\0',1,524,'이름',0,'01059519858','','',51,15),(7,NULL,'2023-02-16 10:26:42',0,11,7,'zxcv@naver.com',0,_binary '\0',_binary '\0',1,5000,'짓기',0,'01098751351','','',30,15),(8,NULL,'2023-02-16 10:26:42',0,11,7,'grg@naver.com',0,_binary '\0',_binary '\0',1,800,'귀찮아죽겠음',0,'01849878512','','',30,15),(9,NULL,'2023-02-16 10:26:42',0,11,7,'tnth@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지',0,'01012168849','','',30,15),(10,NULL,'2023-02-16 10:26:42',0,11,7,'tnth2@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지2',0,'01012168849','','',30,15),(11,NULL,'2023-02-16 10:26:42',0,11,7,'tnth3@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지3',0,'01012168849','','',30,15),(12,NULL,'2023-02-16 10:26:42',0,11,7,'tnth4@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지4',0,'01012168849','','',30,15),(13,NULL,'2023-02-16 10:26:42',0,11,7,'tnth5@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지5',0,'01012168849','','',30,15),(14,NULL,'2023-02-16 10:26:42',0,11,7,'tnth6@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지6',0,'01012168849','','',30,15),(15,NULL,'2023-02-16 10:26:42',0,11,7,'tnth7@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지7',0,'01012168849','','',30,15),(16,NULL,'2023-02-16 10:26:42',0,11,7,'tnth8@naver.com',0,_binary '\0',_binary '\0',1,7000,'닉넴뭐하지8',0,'01012168849','','',30,15),(17,'2023-02-16 10:36:39','2023-02-16 10:36:39',4,4,4,'apatocin4869@naver.com',1600,_binary '\0',_binary '\0',1,5600,'asef',0,'shshs','','',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-16 19:45:15
