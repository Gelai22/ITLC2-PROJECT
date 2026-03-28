CREATE DATABASE  IF NOT EXISTS `parkup_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `parkup_db`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: parkup_db
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `Customer_Id` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(100) NOT NULL,
  `Email_Address` varchar(255) NOT NULL,
  `Customer_Password` varchar(255) NOT NULL,
  `age` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `Home_Address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Customer_Id`),
  UNIQUE KEY `Email_Address` (`Email_Address`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'SigmaClient','sigma.client@gmail.com','$2a$10$ny4kmjOv.jXTNMxNP1dKROjxml4ohNk411WHs8QBdrVBesB07PSNi',NULL,NULL,NULL),(2,'Charm','charmaine@gmail.com','$2a$10$Y64993IvDRY0j9VNbeROSu9BJ4SjkDiKAbkqG7ywiUGULyFPyt1Ky',NULL,NULL,NULL),(4,'sigma','client.sigma@gmail.com','Sylphiette',NULL,NULL,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `Employee_Id` bigint NOT NULL AUTO_INCREMENT,
  `Employee_Name` varchar(255) DEFAULT NULL,
  `Email_Address` varchar(255) NOT NULL,
  `Employee_Password` varchar(255) NOT NULL,
  `age` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `Home_Address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Employee_Id`),
  UNIQUE KEY `Email_Address` (`Email_Address`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Admin','admin@gmail.com','$2a$10$qPQJGTsZB/QHn7jEuL9j/.5/NVfcJVADxiAUXO2zOYKvJi/1V9Vaq',NULL,NULL,NULL),(2,'SigmaAdmin','sigma.admin@gmail.com','$2a$10$tD6YqEShi/q1VYmWx79UO..qb1Mtrr1nD8eOdDKwV0Ad0ZhLgm.xy',NULL,NULL,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_log`
--

DROP TABLE IF EXISTS `maintenance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_log` (
  `maintenance_id` bigint NOT NULL AUTO_INCREMENT,
  `date_back_actual` date DEFAULT NULL,
  `date_back_estimate` date DEFAULT NULL,
  `date_out` date DEFAULT NULL,
  `estimated_cost` decimal(10,2) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `service_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `vehicle_id` bigint DEFAULT NULL,
  PRIMARY KEY (`maintenance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_log`
--

LOCK TABLES `maintenance_log` WRITE;
/*!40000 ALTER TABLE `maintenance_log` DISABLE KEYS */;
INSERT INTO `maintenance_log` VALUES (1,'2025-11-05','2025-11-12','2025-11-05',4200.00,'sample','Oil Change','COMPLETED',2),(2,'2025-11-05','2025-11-06','2025-11-05',4200.00,'','fender benders','COMPLETED',1),(3,'2025-11-06','2025-11-07','2025-11-06',4200.00,'sample','Engine Repair','COMPLETED',3),(4,'2025-11-06','2025-11-07','2025-11-06',4200.00,'sample','Engine Repair','COMPLETED',3),(5,'2025-11-24','2025-11-26','2025-11-24',4200.00,'uaahha','oil change','COMPLETED',1),(6,'2025-11-24','2025-11-26','2025-11-24',422000.00,'ewan ko','Oil Change','COMPLETED',5);
/*!40000 ALTER TABLE `maintenance_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `Reservation_Id` bigint NOT NULL AUTO_INCREMENT,
  `Reservation_Date` datetime DEFAULT NULL,
  `Pickup_Location` varchar(255) DEFAULT NULL,
  `Pickup_Date` datetime DEFAULT NULL,
  `Return_Date` datetime DEFAULT NULL,
  `Payment_Date` date DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `Total_Cost` decimal(10,2) DEFAULT NULL,
  `Employee_Id` bigint DEFAULT NULL,
  `Vehicle_Id` bigint NOT NULL,
  `Customer_Id` bigint NOT NULL,
  `return_location` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `Rating` varchar(255) DEFAULT NULL,
  `client_feedback` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Reservation_Id`),
  KEY `Employee_Id` (`Employee_Id`),
  KEY `Vehicle_Id` (`Vehicle_Id`),
  KEY `Customer_Id` (`Customer_Id`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`Employee_Id`) REFERENCES `employee` (`Employee_Id`),
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`Vehicle_Id`) REFERENCES `vehicle` (`Vehicle_Id`),
  CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`Customer_Id`) REFERENCES `customer` (`Customer_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'2025-11-06 02:36:00','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','4','very good car'),(2,'2025-11-06 02:42:34','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','sample'),(3,'2025-11-06 02:46:20','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','samplessssssssssssssssssssssssssssssssssssssssssssssssss'),(4,'2025-11-06 03:01:24','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','nice'),(5,'2025-11-06 03:08:31','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',999.00,NULL,4,1,NULL,'COMPLETED','5','good'),(6,'2025-11-06 03:11:23','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,2,NULL,'COMPLETED','5','nice'),(7,'2025-11-06 07:14:35','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','tanginamo'),(8,'2025-11-06 07:14:57','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','4','kupal'),(9,'2025-11-06 07:22:26','MAIN_OFFICE','2025-11-05 16:00:00','2025-11-06 16:00:00','2025-11-06',NULL,'PAID',999.00,NULL,4,1,NULL,'COMPLETED','5','good'),(10,'2025-11-10 12:22:56','MAIN_OFFICE','2025-11-09 16:00:00','2025-11-10 16:00:00','2025-11-10',NULL,'PAID',899.00,NULL,1,2,NULL,'COMPLETED','4','good'),(11,'2025-11-10 12:28:33','MAIN_OFFICE','2025-11-09 16:00:00','2025-11-10 16:00:00','2025-11-10',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','great'),(12,'2025-11-10 12:30:06','MAIN_OFFICE','2025-11-09 16:00:00','2025-11-10 16:00:00','2025-11-10',NULL,'PAID',899.00,NULL,1,2,NULL,'COMPLETED','5','good'),(13,'2025-11-12 06:42:48','MAIN_OFFICE','2025-11-11 16:00:00','2025-11-12 16:00:00','2025-11-12',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED','5','good car'),(14,'2025-11-17 15:36:10','MAIN_OFFICE','2025-11-16 16:00:00','2025-11-17 16:00:00','2025-11-17',NULL,'PAID',899.00,NULL,10,1,NULL,'COMPLETED',NULL,'MAIN_OFFICE'),(15,'2025-11-24 03:16:22','MAIN_OFFICE','2025-11-24 00:00:00','2025-11-28 00:00:00',NULL,NULL,NULL,3596.00,NULL,1,4,'MAIN_OFFICE','PENDING_PAYMENT',NULL,NULL),(16,'2025-11-24 03:17:04','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00',NULL,NULL,NULL,3897.00,NULL,2,4,'MAIN_OFFICE','PENDING_PAYMENT',NULL,NULL),(17,'2025-11-24 03:35:19','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00',NULL,NULL,NULL,4797.00,NULL,3,4,'MAIN_OFFICE','PENDING_PAYMENT',NULL,NULL),(18,'2025-11-24 03:42:56','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00',NULL,NULL,NULL,2997.00,NULL,4,4,'MAIN_OFFICE','PENDING_PAYMENT',NULL,NULL),(19,'2025-11-23 19:45:12','MAIN_OFFICE','2025-11-23 16:00:00','2025-11-24 16:00:00','2025-11-24',NULL,'PAID',899.00,NULL,1,1,NULL,'COMPLETED',NULL,'MAIN_OFFICE'),(20,'2025-11-23 19:45:33','MAIN_OFFICE','2025-11-23 16:00:00','2025-11-24 16:00:00','2025-11-24',NULL,'PAID',1599.00,NULL,3,1,NULL,'COMPLETED','5','hahaha'),(21,'2025-11-24 07:41:25','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00','2025-11-24',NULL,'PAID',2997.00,NULL,4,1,'MAIN_OFFICE','COMPLETED','5','hahaha'),(22,'2025-11-24 09:32:54','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00','2025-11-24',NULL,'PAID',4797.00,NULL,3,1,'MAIN_OFFICE','COMPLETED','5','hahaha'),(23,'2025-11-24 09:37:22','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00','2025-11-24',NULL,'PAID',2697.00,NULL,1,1,'MAIN_OFFICE','COMPLETED','5','ahahaha'),(24,'2025-11-24 09:47:02','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00','2025-11-24',NULL,'PAID',2697.00,NULL,1,1,'MAIN_OFFICE','COMPLETED','4','hahaha'),(25,'2025-11-24 10:00:07','MAIN_OFFICE','2025-11-25 00:00:00','2025-11-28 00:00:00','2025-11-24',NULL,'PAID',2697.00,NULL,1,1,'MAIN_OFFICE','COMPLETED','1','tanginang kotse yan'),(26,'2025-11-24 04:54:56','MAIN_OFFICE','2025-11-23 16:00:00','2025-11-24 16:00:00','2025-11-24',NULL,'PAID',899.00,NULL,10,1,NULL,'COMPLETED','5','sjdhjshjdhjhsdhsjdhsjhdgsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss'),(27,'2025-11-24 04:56:08','MAIN_OFFICE','2025-11-23 16:00:00','2025-11-24 16:00:00','2025-11-24',NULL,'PAID',1599.00,NULL,3,1,NULL,'COMPLETED','5','wow\r\n');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `Vehicle_Id` bigint NOT NULL AUTO_INCREMENT,
  `Vehicle_Model` varchar(255) DEFAULT NULL,
  `Rental_Price` decimal(10,2) DEFAULT NULL,
  `Plate_Number` varchar(50) NOT NULL,
  `Vehicle_Year` int DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `number_of_seats` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `vehicle_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Vehicle_Id`),
  UNIQUE KEY `Plate_Number` (`Plate_Number`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,'toyota supra',899.00,'JKJK-1111',2004,'/images/vehicles/cc027ef4-ae6b-4d14-9d9d-21439a10c227.jpg',2,'AVAILABLE','SEDAN'),(2,'nissan silvia s15',1299.00,'SRSR-2121',2001,'/images/vehicles/23949cae-2e71-4842-aca5-fadc4baff105.jpg',2,'AVAILABLE','COUPE'),(3,'lamborghini urus',1599.00,'JHJH-4545',2020,'/images/vehicles/fd52a3e1-4a68-4686-8f83-71186f8d8edf.jpg',5,'AVAILABLE','SUV'),(4,'mitsubishi lancer',999.00,'KJKJ-5757',2004,'/images/vehicles/d2d88110-a408-41fe-a0ad-48bdbb63e95b.jpg',4,'AVAILABLE','SEDAN'),(5,'mazda rx7',499.00,'DFDF-2121',2006,'/images/vehicles/fee899e4-1b81-4e44-923e-5b0ce344933e.jpg',2,'AVAILABLE','COUPE'),(6,'nissan skyline gtr',1699.00,'RFRF-3322',2016,'/images/vehicles/3d98ae78-214f-47f8-bf0e-1e67de5c854f.jpg',2,'ARCHIVED','COUPE'),(7,'nissan skyline gtr',899.00,'FFGG-4433',2016,'/images/vehicles/4e062a96-0f4e-44ca-a403-fb4d246dfe62.jpg',2,'ARCHIVED','COUPE'),(10,'nissan skyline gtr',899.00,'RRFF-3333',2016,'/images/vehicles/141e4bf6-0c28-4f4b-b5b1-ee62216f618a.jpg',2,'AVAILABLE','COUPE');
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-28  1:07:36
