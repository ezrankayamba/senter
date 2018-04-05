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
-- Table structure for table tbl_permission
--

--DROP TABLE IF EXISTS tbl_permission;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_permission (
  id integer PRIMARY KEY NOT NULL,
  name varchar(50) DEFAULT NULL,
  description varchar(100) DEFAULT NULL,
  parent integer DEFAULT NULL REFERENCES tbl_permission (id)
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_role
--

--DROP TABLE IF EXISTS tbl_role;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_role (
  id integer PRIMARY KEY NOT NULL,
  name varchar(50) DEFAULT NULL,
  description varchar(100) DEFAULT NULL
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_role_permission
--

--DROP TABLE IF EXISTS tbl_role_permission;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_role_permission (
  id integer PRIMARY KEY NOT NULL,
  permission_id integer NOT NULL REFERENCES tbl_permission (id),
  role_id integer NOT NULL REFERENCES tbl_role (id),
  status smallint DEFAULT '0'
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_user
--

--DROP TABLE IF EXISTS tbl_user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_user (
  id integer NOT NULL PRIMARY KEY,
  username varchar(50) DEFAULT NULL UNIQUE,
  password varchar(100) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  full_name varchar(100) DEFAULT NULL,
  role_id integer NOT NULL REFERENCES tbl_role (id),
  enabled smallint DEFAULT '0',
  reset_on smallint DEFAULT '0'
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view vw_data_setup
--

--
-- Table structure for table tbl_audit
--

--DROP TABLE IF EXISTS tbl_audit;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_audit (
  id bigint PRIMARY KEY NOT NULL,
  task varchar(45) DEFAULT NULL,
  user_id integer NOT NULL REFERENCES tbl_user (id),
  role_id integer NOT NULL REFERENCES tbl_role (id),
  record_date timestamp NOT NULL,
  last_update timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status smallint DEFAULT NULL,
  http_code varchar(10) DEFAULT NULL
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_data_type
--

--DROP TABLE IF EXISTS tbl_data_type;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_data_type (
  position smallint NOT NULL DEFAULT '0',
  name varchar(100) PRIMARY KEY NOT NULL,
  type varchar(50) DEFAULT NULL,
  last_update timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  status smallint DEFAULT '0'
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_form
--

--DROP TABLE IF EXISTS tbl_form;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_form (
  id integer PRIMARY KEY NOT NULL,
  name varchar(100) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  filepath varchar(255) DEFAULT NULL,
  status varchar(45) DEFAULT NULL,
  last_update timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version varchar(45) DEFAULT NULL,
  json text NOT NULL,
  display varchar(100) DEFAULT NULL
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table tbl_form_instance
--

--DROP TABLE IF EXISTS tbl_form_instance;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE IF NOT EXISTS tbl_form_instance (
  id integer PRIMARY KEY NOT NULL,
  uuid varchar(100) DEFAULT NULL UNIQUE,
  name varchar(100) DEFAULT NULL,
  record_date timestamp DEFAULT NULL,
  status smallint DEFAULT NULL,
  last_update timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  json text,
  form_id integer NOT NULL REFERENCES tbl_form (id),
  recorded_by integer NOT NULL REFERENCES tbl_user (id)
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view vw_data_setup
--

/*!50001 DROP VIEW IF EXISTS vw_data_setup*/;
/*!50001 CREATE VIEW vw_data_setup AS select setups.uuid AS uuid,setups.name AS name,setups.type AS type,setups.last_update AS last_update from (select fi.uuid AS uuid,fi.name AS name,'Sites' AS type,fi.last_update AS last_update from (powergen.tbl_form_instance fi left join powergen.tbl_form f on((fi.form_id = f.id))) where (f.name like 'Site%')) setups */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-04 19:46:32
