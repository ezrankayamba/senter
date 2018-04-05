-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: powergen
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO,POSTGRESQL' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Dumping data for table "tbl_permission"
--

--LOCK TABLES "tbl_permission" WRITE;
/*!40000 ALTER TABLE "tbl_permission" DISABLE KEYS */;
/*
INSERT INTO "tbl_permission" VALUES (1,'manageUsers','Create admin user2',NULL),(2,'managePermissions','Manage permissions',NULL),(3,'manageRoles','Manage User Roles',NULL),(4,'Dummy','Dummy Desc',NULL),(9,'Dummy2','Dummmmmmy2',4),(11,'deletePermission','Delete permission',2),(12,'Survey','Survey',NULL),(13,'manageFormInstances','manageFormInstances',NULL),(14,'deleteFormInstances','deleteFormInstances',13),(15,'viewFormInstances','viewFormInstances',13),(16,'viewPermissions','viewPermissions',2),(17,'createPermissions','createPermissions',2),(18,'editPermissions','editPermissions',2),(19,'manageSurveys','manageSurveys',NULL),(20,'viewSurveyForms','viewSurveyForms',19),(21,'submitSurveyForms','submitSurveyForms',19),(22,'manageFormRepos','manageFormRepos',NULL),(23,'deleteFormRepos','deleteFormRepos',22),(24,'createFormRepos','createFormRepos',22),(25,'viewFormRepos','viewFormRepos',22),(26,'editFormRepos','editFormRepos',22),(27,'deleteUsers','deleteUsers',1),(28,'createUsers','createUsers',1),(29,'viewUsers','viewUsers',1),(30,'editUsers','editUsers',1),(31,'createRoleMatrix','createRoleMatrix',3),(32,'viewRoles','viewRoles',3),(33,'editRoles','editRoles',3),(34,'createRoles','createRoles',3),(35,'viewRoleMatrix','viewRoleMatrix',3),(36,'changePassword','changePassword',NULL),(37,'makeMyOwnPasswordReset','makeMyOwnPasswordReset',NULL),(38,'makePasswordReset','makePasswordReset',NULL),(39,'querySetups','querySetups',19),(40,'queryDataTypes','queryDataTypes',NULL),(41,'downloadSurveyForms','Download my own submitted forms for local sync e.g. when re-installed the app',12);
*/
/*!40000 ALTER TABLE "tbl_permission" ENABLE KEYS */;
--UNLOCK TABLES;

--
-- Dumping data for table "tbl_role"
--

--LOCK TABLES "tbl_role" WRITE;
/*!40000 ALTER TABLE "tbl_role" DISABLE KEYS */;
/*INSERT INTO "tbl_role" VALUES (1,'Root','Root user role'),(2,'Admin','Administrator'),(5,'Surveyor','Surveyor'),(6,'SurveyAdmin','Survey Admin');
*/
/*!40000 ALTER TABLE "tbl_role" ENABLE KEYS */;
--UNLOCK TABLES;

--
-- Dumping data for table "tbl_role_permission"
--

--LOCK TABLES "tbl_role_permission" WRITE;
/*!40000 ALTER TABLE "tbl_role_permission" DISABLE KEYS */;
/*INSERT INTO "tbl_role_permission" VALUES (1,1,1,0),(2,1,2,0),(3,2,1,0),(4,3,1,0),(5,9,2,0),(6,11,1,0),(7,12,5,0),(8,13,1,0),(9,14,1,0),(10,15,1,0),(11,16,1,0),(12,17,1,0),(13,18,1,0),(14,19,1,0),(15,20,1,0),(16,21,1,0),(17,22,1,0),(18,23,1,0),(19,24,1,0),(20,25,1,0),(21,26,1,0),(22,27,1,0),(23,28,1,0),(24,29,1,0),(25,30,1,0),(26,31,1,0),(27,32,1,0),(28,33,1,0),(29,34,1,0),(30,35,1,0),(31,13,6,0),(32,22,6,0),(33,19,6,0),(34,21,6,0),(35,12,6,0),(36,15,6,0),(37,25,6,0),(38,20,6,0),(39,12,1,0),(40,36,1,0),(41,37,1,0),(42,38,1,0),(43,36,6,0),(44,37,6,0),(45,19,5,0),(46,21,5,0),(47,20,5,0),(48,39,1,0),(49,39,5,0),(50,37,5,0),(51,36,5,0),(53,40,5,0),(54,41,5,0);
*/
/*!40000 ALTER TABLE "tbl_role_permission" ENABLE KEYS */;
--UNLOCK TABLES;

--
-- Dumping data for table "tbl_user"
--

--LOCK TABLES "tbl_user" WRITE;
/*!40000 ALTER TABLE "tbl_user" DISABLE KEYS */;
/*INSERT INTO "tbl_user" VALUES (29,'admin2','$2a$10$0SNLmY9vm8/8nP03WX6NAOnv/A5ujIPNI/wZnmcpdJy52mU7F3pne','ezrankayamba@gmail.com','Ezra Nkayamba',1,1,0),(495,'mtwe.zakayo','$2a$10$VyICMbqi7DG8Q0wd6xEtGO/8JsO8QoKAtBGXC.TeNw/OI33wXV4t.','jimmyzakayo@gmail.com','Mtwe Zakayo',6,1,0),(559,'surveyor1','$2a$10$ZjTWzFu1LnbfSJAjSlDJA.XZBmKFdU7LC/2KvLI0UHL7OqwDLo3Aq','ezrankayamba@gmail.com','Test Surveyor',5,1,0),(560,'surveyor2','$2a$10$FR6QfNsVI3xunU2oB39aquOyC/8lYzU6APj/IknPyjmCmf3LZaxUa','ezrankayamba@gmail.com','Test Surveyor',5,1,0);
*/
/*!40000 ALTER TABLE "tbl_user" ENABLE KEYS */;
--UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-04 19:54:17
