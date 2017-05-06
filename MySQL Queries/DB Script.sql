----------------------------- Create table Scripts --------------------------------------------------------
CREATE TABLE User(
  ID INT NOT NULL AUTO_INCREMENT,
  FirstName VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  LastName VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  UserName VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  Salt VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  EncryptedPassword VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  Country INT NOT NULL,
  PhoneNumber VARCHAR(50) COLLATE utf8mb4_unicode_ci NULL,
  EmailID VARCHAR(50) COLLATE utf8mb4_unicode_ci NULL,
  CreatedBy INT NULL,
  CreatedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  ModifiedBy INT NULL,
  ModifiedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  IsActive BOOLEAN NOT NULL DEFAULT 1,
  IsDeleted BOOLEAN NOT NULL DEFAULT 0,
  CONSTRAINT PK_ID PRIMARY KEY(ID)
)

CREATE TABLE TBL_LOOKUP(
  ID INT NOT NULL AUTO_INCREMENT,
  Type INT NOT NULL,
  ParentKey INT NULL,
  Code VARCHAR(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  Description VARCHAR(250) COLLATE utf8mb4_unicode_ci NULL,
  SortOrder INT NULL,
  CreatedBy INT NULL,
  CreatedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  ModifiedBy INT NULL,
  ModifiedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  IsActive BOOLEAN NOT NULL DEFAULT 1,
  IsDeleted BOOLEAN NOT NULL DEFAULT 0,
  CONSTRAINT PK_ID PRIMARY KEY(ID)
)

CREATE TABLE TBL_COUNTRY_MST(
  	ID INT NOT NULL AUTO_INCREMENT,
    ISOCode VARCHAR(3) COLLATE utf8mb4_unicode_ci NOT NULL,
    CountryCode VARCHAR(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  	CreatedBy INT NULL,
  	CreatedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  	ModifiedBy INT NULL,
  	ModifiedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  	IsActive BOOLEAN NOT NULL DEFAULT 1,
  	IsDeleted BOOLEAN NOT NULL DEFAULT 0,
  	CONSTRAINT PK_ID PRIMARY KEY(ID)
)

CREATE TABLE TBL_LOG(
  ID INT NOT NULL AUTO_INCREMENT,
  ErrorCode VARCHAR(20) COLLATE utf8mb4_unicode_ci NULL,
  ErrorMsg VARCHAR(500) COLLATE utf8mb4_unicode_ci NULL,
  ModuleName VARCHAR(50) COLLATE utf8mb4_unicode_ci NULL,
  PageName VARCHAR(50) COLLATE utf8mb4_unicode_ci NULL,
  FunctionName VARCHAR(50) COLLATE utf8mb4_unicode_ci NULL,
  LogDateTime DATETIME NULL,
  CreatedBy INT NULL,
  CreatedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  ModifiedBy INT NULL,
  ModifiedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  IsActive BOOLEAN NOT NULL DEFAULT 1,
  IsDeleted BOOLEAN NOT NULL DEFAULT 0,
  CONSTRAINT PK_ID PRIMARY KEY(ID)
)

CREATE TABLE Challenge(
	Challenge VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
	PhoneNumber VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
	ChlngTimeStamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE `securechat`.`message` ( 
`ID` INT NOT NULL AUTO_INCREMENT , 
`From` INT NOT NULL , `To` INT NOT NULL , 
`Message` VARCHAR(10000) NOT NULL , 
`Status` TINYINT NOT NULL DEFAULT '1' , 
`SendOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP , 
PRIMARY KEY (`ID`)
) ENGINE = MyISAM
--------------------------------------------------XXX----------------------------------------

---------------------------- Adding Constraints on tables -----------------------------------

------------------------- Constraints on TBL_USER table -------------------------------------
ALTER TABLE TBL_USER
ADD CONSTRAINT FK_USR_Country_TCM_ID
	FOREIGN KEY (Country) REFERENCES TBL_COUNTRY_MST(ID)

ALTER TABLE TBL_USER
ADD CONSTRAINT FK_USR_CreatedBy_USR_ID
	FOREIGN KEY (CreatedBy) REFERENCES TBL_USER (ID)
	
ALTER TABLE TBL_USER
ADD CONSTRAINT FK_USR_ModifiedBy_USR_ID
	FOREIGN KEY (ModifiedBy) REFERENCES TBL_USER (ID)

ALTER TABLE TBL_USER
ADD CONSTRAINT UK_PhoneNumber UNIQUE(PhoneNumber)
----------------------------------------------------------------------------------------------

------------------------- Constraints on TBL_LOOKUP table -------------------------------------
ALTER TABLE TBL_LOOKUP
ADD CONSTRAINT UK_Code UNIQUE(Code)

ALTER TABLE TBL_LOOKUP
ADD CONSTRAINT FK_LKP_CreatedBy_USR_ID
	FOREIGN KEY (CreatedBy) REFERENCES TBL_USER (ID)
	
ALTER TABLE TBL_LOOKUP
ADD CONSTRAINT FK_LKP_ModifiedBy_USR_ID
	FOREIGN KEY (ModifiedBy) REFERENCES TBL_USER (ID)
----------------------------------------------------------------------------------------------

------------------------- Constraints on TBL_COUNTRY_MST table -------------------------------------
ALTER TABLE TBL_COUNTRY_MST
ADD CONSTRAINT FK_TCM_CreatedBy_USR_ID
	FOREIGN KEY (CreatedBy) REFERENCES TBL_USER (ID)
	
ALTER TABLE TBL_COUNTRY_MST
ADD CONSTRAINT FK_TCM_ModifiedBy_USR_ID
	FOREIGN KEY (ModifiedBy) REFERENCES TBL_USER (ID)
	
ALTER TABLE TBL_COUNTRY_MST
ADD CONSTRAINT FK_TCM_ISOCode_LKP_Code
	FOREIGN KEY (ISOCode) REFERENCES TBL_LOOKUP (Code)
----------------------------------------------------------------------------------------------

------------------------- Constraints on TBL_LOG table -------------------------------------
ALTER TABLE TBL_LOG
ADD CONSTRAINT FK_LOG_CreatedBy_USR_ID
	FOREIGN KEY (CreatedBy) REFERENCES TBL_USER (ID)
	
ALTER TABLE TBL_LOG
ADD CONSTRAINT FK_LOG_ModifiedBy_USR_ID
	FOREIGN KEY (ModifiedBy) REFERENCES TBL_USER (ID)
----------------------------------------------------------------------------------------------

------------------------------------------XXX-------------------------------------------------

------------------------------- INSERT QUERIES FOR MASTER TABLES -----------------------------
INSERT INTO TBL_LOOKUP
(Type, Code, Description )
VALUES
(1,'AU', 'Australia'),
(1,'BR', 'Brazil'),
(1,'CA', 'Canada'),
(1,'CN', 'China'),
(1,'DE', 'Germany'),
(1,'IN', 'India'),
(1,'AE', 'United Arab Emirates'),
(1,'GB', 'United Kingdom'),
(1,'US', 'United States')

INSERT INTO TBL_COUNTRY_MST
(ISOCode, CountryCode)
VALUES
('AU','61'),
('BR','55'),
('CA','1'),
('CN','86'),
('DE','49'),
('IN','91'),
('AE','971'),
('GB','44'),
('US','1')
------------------------------------------XXX----------------------------------------------------