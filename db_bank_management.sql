-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: bank_management
-- ------------------------------------------------------
-- Server version	8.4.0

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
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `admin_Id` bigint NOT NULL,
  PRIMARY KEY (`admin_Id`),
  CONSTRAINT `administrator_ibfk_1` FOREIGN KEY (`admin_Id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank` (
  `bank_Id` bigint NOT NULL AUTO_INCREMENT,
  `bankName` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`bank_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
INSERT INTO `bank` VALUES (1,'Nombre del Banco','Dirección del Banco',NULL);
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bankloan`
--

DROP TABLE IF EXISTS `bankloan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bankloan` (
  `loan_Id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `interest_rate` decimal(38,2) DEFAULT NULL,
  `duration` bigint DEFAULT NULL,
  `customer_Id` bigint DEFAULT NULL,
  `bank_Id` bigint DEFAULT NULL,
  PRIMARY KEY (`loan_Id`),
  KEY `customer_Id` (`customer_Id`),
  KEY `bank_Id` (`bank_Id`),
  CONSTRAINT `bankloan_ibfk_1` FOREIGN KEY (`customer_Id`) REFERENCES `customer` (`customer_Id`),
  CONSTRAINT `bankloan_ibfk_2` FOREIGN KEY (`bank_Id`) REFERENCES `bank` (`bank_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankloan`
--

LOCK TABLES `bankloan` WRITE;
/*!40000 ALTER TABLE `bankloan` DISABLE KEYS */;
INSERT INTO `bankloan` VALUES (1,15000.00,8.50,36,1,1),(2,300.00,15.00,1,1,1),(6,100.00,1.00,1,1,1),(7,300.00,15.00,1,1,1),(8,1000.00,1.00,1,1,1),(12,1000.00,1.00,1,NULL,NULL),(23,1.00,1.00,1,NULL,NULL),(24,1.00,1.00,1,NULL,NULL),(25,567.00,11.00,5,NULL,NULL);
/*!40000 ALTER TABLE `bankloan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_payment`
--

DROP TABLE IF EXISTS `card_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_payment` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `card_id` bigint DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FK8pvp4hb41cg8swg9ftvekji5q` (`card_id`),
  CONSTRAINT `FK8pvp4hb41cg8swg9ftvekji5q` FOREIGN KEY (`card_id`) REFERENCES `credit_card` (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_payment`
--

LOCK TABLES `card_payment` WRITE;
/*!40000 ALTER TABLE `card_payment` DISABLE KEYS */;
INSERT INTO `card_payment` VALUES (1,150.00,'2024-05-12 18:00:00.000000',NULL),(2,45.00,'2024-05-15 18:00:00.000000',NULL);
/*!40000 ALTER TABLE `card_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cardpayment`
--

DROP TABLE IF EXISTS `cardpayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cardpayment` (
  `payment_Id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `card_Id` bigint DEFAULT NULL,
  PRIMARY KEY (`payment_Id`),
  KEY `card_Id` (`card_Id`),
  CONSTRAINT `cardpayment_ibfk_1` FOREIGN KEY (`card_Id`) REFERENCES `creditcard` (`card_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cardpayment`
--

LOCK TABLES `cardpayment` WRITE;
/*!40000 ALTER TABLE `cardpayment` DISABLE KEYS */;
/*!40000 ALTER TABLE `cardpayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_card`
--

DROP TABLE IF EXISTS `credit_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_card` (
  `card_id` bigint NOT NULL AUTO_INCREMENT,
  `annual_fee` decimal(38,2) DEFAULT NULL,
  `credit_limit` decimal(38,2) DEFAULT NULL,
  `bank_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`card_id`),
  KEY `FK42at2rvmuuo6jur5f6yhkewad` (`bank_id`),
  KEY `FKsuvn58jmqe7oucg43j8g020ss` (`customer_id`),
  CONSTRAINT `FK42at2rvmuuo6jur5f6yhkewad` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`bank_Id`),
  CONSTRAINT `FKsuvn58jmqe7oucg43j8g020ss` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_card`
--

LOCK TABLES `credit_card` WRITE;
/*!40000 ALTER TABLE `credit_card` DISABLE KEYS */;
INSERT INTO `credit_card` VALUES (1,30.00,2000.00,NULL,NULL),(2,30.00,1500.00,NULL,NULL);
/*!40000 ALTER TABLE `credit_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditcard`
--

DROP TABLE IF EXISTS `creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `creditcard` (
  `card_Id` bigint NOT NULL AUTO_INCREMENT,
  `creditLimit` decimal(10,2) DEFAULT NULL,
  `annualFee` decimal(10,2) DEFAULT NULL,
  `customer_Id` bigint DEFAULT NULL,
  `bank_Id` bigint DEFAULT NULL,
  PRIMARY KEY (`card_Id`),
  KEY `customer_Id` (`customer_Id`),
  KEY `bank_Id` (`bank_Id`),
  CONSTRAINT `creditcard_ibfk_1` FOREIGN KEY (`customer_Id`) REFERENCES `customer` (`customer_Id`),
  CONSTRAINT `creditcard_ibfk_2` FOREIGN KEY (`bank_Id`) REFERENCES `bank` (`bank_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_Id` bigint NOT NULL AUTO_INCREMENT,
  `balance` decimal(38,2) DEFAULT NULL,
  `id` bigint DEFAULT NULL,
  `customer_type` varchar(255) DEFAULT NULL,
  `account_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customer_Id`),
  KEY `FKmntl8vwlaaovqqitcpbq5g0fd` (`id`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`customer_Id`) REFERENCES `people` (`id`),
  CONSTRAINT `FKmntl8vwlaaovqqitcpbq5g0fd` FOREIGN KEY (`id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,1000.00,NULL,NULL,NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loanpayment`
--

DROP TABLE IF EXISTS `loanpayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loanpayment` (
  `payment_Id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `loan_Id` bigint DEFAULT NULL,
  PRIMARY KEY (`payment_Id`),
  KEY `loan_Id` (`loan_Id`),
  CONSTRAINT `loanpayment_ibfk_1` FOREIGN KEY (`loan_Id`) REFERENCES `bankloan` (`loan_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loanpayment`
--

LOCK TABLES `loanpayment` WRITE;
/*!40000 ALTER TABLE `loanpayment` DISABLE KEYS */;
INSERT INTO `loanpayment` VALUES (1,123.00,'2024-05-06',1),(2,123.00,'2024-05-06',1);
/*!40000 ALTER TABLE `loanpayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `people`
--

DROP TABLE IF EXISTS `people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `people` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `people`
--

LOCK TABLES `people` WRITE;
/*!40000 ALTER TABLE `people` DISABLE KEYS */;
INSERT INTO `people` VALUES (1,'Gustavo','Montoya','Dirección','Contacto','123','admin');
/*!40000 ALTER TABLE `people` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `transaction_Id` bigint NOT NULL AUTO_INCREMENT,
  `type` enum('deposit','withdrawal','payment') DEFAULT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `customer_Id` bigint DEFAULT NULL,
  PRIMARY KEY (`transaction_Id`),
  KEY `customer_Id` (`customer_Id`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`customer_Id`) REFERENCES `customer` (`customer_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-15 16:35:28
