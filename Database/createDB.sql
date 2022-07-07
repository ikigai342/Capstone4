SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE if exists customer;
DROP TABLE if exists architect;
DROP TABLE if exists project_manager;
DROP TABLE if exists structural_engineer;
DROP TABLE if exists project;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE customer (
	customerID int(3) AUTO_INCREMENT,
	customerName varchar(16),
	customerEmail varchar(24),
	customerAddress varchar(255),
	customerNumber varchar(10),
	PRIMARY KEY (customerID)
	);
CREATE TABLE architect (
	architectID int(3) AUTO_INCREMENT,
	architectName varchar(16),
	architectEmail varchar(24),
	architectAddress varchar(255),
	architectNumber varchar(10),
	PRIMARY KEY (architectID)
	);
CREATE TABLE project_manager (
	managerID int(3) AUTO_INCREMENT,
	managerName varchar(16),
	managerEmail varchar(24),
	managerAddress varchar(255),
	managerNumber varchar(10),
	PRIMARY KEY (managerID)
	);
CREATE TABLE structural_engineer (
	engineerID int(3) AUTO_INCREMENT,
	engineerName varchar(16),
	engineerEmail varchar(24),
	engineerAddress varchar(255),
	engineerNumber varchar(10),
	PRIMARY KEY (engineerID)
	);

create table project (
	projectNumber int(5) AUTO_INCREMENT,
	projectName varchar(16), projectCost DECIMAL(10,4),
	amountPaid DECIMAL(10,4), deadline DATE,
	dateOfCompletion DATE,
	isCompleted TINYINT(1),
	erfNum int(5),
	buildingType varchar(16),
	projectAddress varchar(255),
	customerID int(3),
	architectID int(3),
	managerID int(3),
	engineerID int(3),
	PRIMARY KEY (projectNumber),
	FOREIGN KEY (customerID) REFERENCES customer(customerID),
	FOREIGN KEY (architectID) REFERENCES architect(architectID),
	FOREIGN KEY (managerID) REFERENCES project_manager(managerID),
	FOREIGN KEY (engineerID) REFERENCES structural_engineer(engineerID)
	);

INSERT INTO customer VALUES (1, 'John Doe', 'johndoe@gmail.com', '25 ladidae drive, last, 5000', '0711234567');
INSERT INTO customer VALUES (2, 'Eli Robertson', 'elirobertson@gmail.com', '50 ladidae drive, last, 5000', '0721234567');
INSERT INTO architect VALUES (1, 'Bryan Ron', 'bryan@poised.co.za', '35 dankin drive, last, 5005', '0731234567');
INSERT INTO architect VALUES (2, 'Roger Benterson', 'roger@poised.co.za', '60 dankin drive, last, 5005', '0741234567');
INSERT INTO project_manager VALUES (1, 'Joshua Graham', 'joshua@poised.co.za', '42 dankin drive, last, 5005', '0751234567');
INSERT INTO project_manager VALUES (2, 'Roger Benterson', 'roger@poised.co.za', '44 dankin drive, last, 5005', '0761234567');
INSERT INTO structural_engineer VALUES (1, 'Michael Reeves', 'michael@poised.co.za', '75 dankin drive, last, 5005', '0771234567');
INSERT INTO structural_engineer VALUES (2, 'Peles Johnson', 'peles@poised.co.za', '80 dankin drive, last, 5005', '0781234567');
INSERT INTO project VALUES (1, 'House John', 250000.00, 75000.00, '2022-10-10', null, 0, 25, 'House', '25 ladidae drive, last, 5000', 1, 1, 1, 1);
INSERT INTO project VALUES (2, 'Flat Eli', 80000.00, 20000.0, '2022-09-4', null, 0, 50, 'Flat', '50 ladidae drive, last, 5000', 2, 2, 2, 2);
