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
  VerificationCode VARCHAR(255) COLLATE utf8mb4_unicode_ci NULL,
  IsVerified BOOLEAN NULL,
  CreatedBy INT NULL,
  CreatedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  ModifiedBy INT NULL,
  ModifiedOn DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  IsActive BOOLEAN NOT NULL DEFAULT 1,
  IsDeleted BOOLEAN NOT NULL DEFAULT 0,
  CONSTRAINT PK_ID PRIMARY KEY(ID)
)

CREATE TABLE `securechat`.`message` ( 
`ID` INT NOT NULL AUTO_INCREMENT , 
`From` INT NOT NULL , 
`To` INT NOT NULL , 
`Message` VARCHAR(10000) NOT NULL , 
`Status` TINYINT NOT NULL DEFAULT '1' , 
`SendOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP , 
PRIMARY KEY (`ID`)
) ENGINE = MyISAM
--------------------------------------------------XXX----------------------------------------

---------------------------- Adding Constraints on tables -----------------------------------

------------------------- Constraints on User table -------------------------------------
ALTER TABLE User
ADD CONSTRAINT FK_USR_Country_TCM_ID
	FOREIGN KEY (Country) REFERENCES TBL_COUNTRY_MST(ID)

ALTER TABLE User
ADD CONSTRAINT FK_USR_CreatedBy_USR_ID
	FOREIGN KEY (CreatedBy) REFERENCES User (ID)
	
ALTER TABLE User
ADD CONSTRAINT FK_USR_ModifiedBy_USR_ID
	FOREIGN KEY (ModifiedBy) REFERENCES User (ID)

ALTER TABLE User
ADD CONSTRAINT UK_PhoneNumber UNIQUE(PhoneNumber)
----------------------------------------------------------------------------------------------

------------------------- Constraints on message table -------------------------------------
ALTER TABLE message
ADD CONSTRAINT FK_MSG_From_USR_ID
	FOREIGN KEY (From) REFERENCES User (ID)
	
ALTER TABLE message
ADD CONSTRAINT FK_MSG_To_USR_ID
	FOREIGN KEY (To) REFERENCES User (ID)

----------------------------------------------------------------------------------------------
