-- Health Plus — database schema and starter data
-- Generated from the live schema used by the Java/JavaFX app (15 tables, 3NF).
-- Run this whole script against a MySQL 8+ server to get a working local database.

DROP DATABASE IF EXISTS healthplus;
CREATE DATABASE healthplus;
USE healthplus;

SET FOREIGN_KEY_CHECKS = 0;

-- ── Independent / reference tables ─────────────────────────────

CREATE TABLE Category (
    CategoryID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description TEXT
);

CREATE TABLE Supplier (
    SupplierID INT AUTO_INCREMENT PRIMARY KEY,
    SupplierName VARCHAR(150) NOT NULL,
    ContactPerson VARCHAR(100),
    Phone VARCHAR(30),
    Email VARCHAR(100),
    City VARCHAR(80)
);

CREATE TABLE Warehouse (
    WarehouseID INT AUTO_INCREMENT PRIMARY KEY,
    WarehouseName VARCHAR(150) NOT NULL,
    Address VARCHAR(200),
    City VARCHAR(80),
    Phone VARCHAR(30),
    Capacity INT
);

CREATE TABLE Client (
    ClientID INT AUTO_INCREMENT PRIMARY KEY,
    ClientName VARCHAR(150) NOT NULL,
    ClientType ENUM('Pharmacy','Clinic') NOT NULL,
    Phone VARCHAR(30),
    City VARCHAR(80),
    CreditLimit DECIMAL(12,2) DEFAULT 5000.00
);

-- ── Tables with a single FK ─────────────────────────────────────

CREATE TABLE Employee (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(80) NOT NULL,
    LastName VARCHAR(80) NOT NULL,
    Role VARCHAR(80) NOT NULL,
    HireDate DATE NOT NULL,
    Phone VARCHAR(30),
    Salary DECIMAL(10,2),
    WarehouseID INT,
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse(WarehouseID) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Product (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(150) NOT NULL,
    Description TEXT,
    UnitPrice DECIMAL(10,2) NOT NULL,
    ReorderLevel INT NOT NULL DEFAULT 10,
    CategoryID INT NOT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ── Tables with multiple FKs ─────────────────────────────────────

CREATE TABLE SupplierProduct (
    SupplierID INT NOT NULL,
    ProductID INT NOT NULL,
    UnitCost DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (SupplierID, ProductID),
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Batch (
    BatchID INT AUTO_INCREMENT PRIMARY KEY,
    ProductID INT NOT NULL,
    WarehouseID INT NOT NULL,
    QtyInStock INT NOT NULL DEFAULT 0,
    ExpiryDate DATE,
    StorageLocation VARCHAR(80),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse(WarehouseID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PurchaseOrder (
    PONumber INT AUTO_INCREMENT PRIMARY KEY,
    SupplierID INT NOT NULL,
    EmployeeID INT NOT NULL,
    OrderDate DATE NOT NULL,
    ExpDeliveryDate DATE,
    Status ENUM('Pending','Delivered','Cancelled') DEFAULT 'Pending',
    TotalAmount DECIMAL(12,2) DEFAULT 0.00,
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PurchaseOrderItem (
    POItemID INT AUTO_INCREMENT,
    PONumber INT NOT NULL,
    ProductID INT NOT NULL,
    QtyOrdered INT NOT NULL,
    UnitCost DECIMAL(10,2) NOT NULL,
    QtyReceived INT DEFAULT 0,
    PRIMARY KEY (POItemID, PONumber),
    FOREIGN KEY (PONumber) REFERENCES PurchaseOrder(PONumber) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SaleOrder (
    SaleOrderID INT AUTO_INCREMENT PRIMARY KEY,
    ClientID INT NOT NULL,
    EmployeeID INT NOT NULL,
    OrderDate DATE NOT NULL,
    DeliveryDate DATE,
    Status ENUM('Pending','Approved','Delivered','Cancelled') DEFAULT 'Pending',
    TotalAmount DECIMAL(12,2) DEFAULT 0.00,
    PaymentStatus ENUM('Paid','Unpaid','Partial') DEFAULT 'Unpaid',
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SaleOrderItem (
    SaleItemID INT AUTO_INCREMENT,
    SaleOrderID INT NOT NULL,
    BatchID INT NOT NULL,
    QtyOrdered INT NOT NULL,
    UnitPrice DECIMAL(10,2) NOT NULL,
    Discount DECIMAL(5,2) DEFAULT 0.00,
    PRIMARY KEY (SaleItemID, SaleOrderID),
    FOREIGN KEY (SaleOrderID) REFERENCES SaleOrder(SaleOrderID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (BatchID) REFERENCES Batch(BatchID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Payment (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    SaleOrderID INT,
    PONumber INT,
    PaymentDate DATE NOT NULL,
    Amount DECIMAL(12,2) NOT NULL,
    PaymentMethod ENUM('Cash','BankTransfer','Cheque') NOT NULL,
    Direction ENUM('Incoming','Outgoing') NOT NULL,
    FOREIGN KEY (SaleOrderID) REFERENCES SaleOrder(SaleOrderID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (PONumber) REFERENCES PurchaseOrder(PONumber) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE InventoryTransaction (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    BatchID INT NOT NULL,
    EmployeeID INT NOT NULL,
    TxnType ENUM('Receipt','Dispatch','Adjustment') NOT NULL,
    Quantity INT NOT NULL,
    TxnDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ReferenceID INT,
    FOREIGN KEY (BatchID) REFERENCES Batch(BatchID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE UserAccount (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Password VARCHAR(50) NOT NULL,
    Role ENUM('Admin','Employee','Client') NOT NULL,
    EmployeeID INT,
    ClientID INT,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID) ON DELETE CASCADE ON UPDATE CASCADE
);

SET FOREIGN_KEY_CHECKS = 1;

-- ── Starter data ──────────────────────────────────────────────
-- Reference/catalog data only — no purchase/sale order history, so you start
-- with a clean transaction slate and can generate your own from the app.

INSERT INTO Category (CategoryID, Name, Description) VALUES
(1,'Medications','Medicines and treatments'),
(2,'Skincare','Skin care products'),
(3,'Childrens Care','Products for children'),
(4,'Shampoos','Hair care products'),
(5,'Personal Care','General personal care products'),
(6,'Vitamins','Vitamin and mineral supplements'),
(7,'Medical Devices','Health monitoring devices'),
(8,'Dental Care','Dental hygiene products'),
(9,'Baby Care','Baby feeding and care accessories'),
(10,'First Aid','Emergency and wound care supplies');

INSERT INTO Supplier (SupplierID, SupplierName, ContactPerson, Phone, Email, City) VALUES
(1,'Al-Quds Pharma','Ahmad Hassan','+970-2-2961000','ahmad@quds-pharma.ps','Ramallah'),
(2,'Jordan Medical Dist.','Sara Khalil','+962-6-5001234','sara@jmd.jo','Amman'),
(3,'Palestine FMCG Co.','Rami Nasser','+970-2-2408000','rami@pfmcg.ps','Nablus'),
(4,'Euro Pharma Imports','Laila Barakat','+972-3-5671234','laila@europharma.co','Ramallah'),
(5,'Gulf Medical Supplies','Faisal Al-Rashid','+971-4-3091000','faisal@gulfmed.ae','Dubai'),
(6,'MedLine Palestine','Nour Haddad','+970-2-2774400','nour@medlinepal.ps','Bethlehem');

INSERT INTO Warehouse (WarehouseID, WarehouseName, Address, City, Phone, Capacity) VALUES
(1,'Health Plus Main Warehouse','Abu Qash Main St.','Ramallah','+970-2-2811034',10000),
(2,'West Bank Distribution Hub','Industrial Zone, St. 7','Nablus','+970-9-2381055',8000),
(3,'Northern Storage Facility','Al-Quds Rd, Block 3','Jenin','+970-4-2501200',5000);

INSERT INTO Employee (EmployeeID, FirstName, LastName, Role, HireDate, Phone, Salary, WarehouseID) VALUES
(1,'Khaled','Mansour','Warehouse Manager','2020-01-15','+970-59-1000001',3500.00,1),
(2,'Rania','Yousef','Sales Officer','2021-03-01','+970-59-1000002',2800.00,1),
(3,'Tariq','Saleh','Procurement Officer','2019-06-20','+970-59-1000003',2900.00,1),
(4,'Hana','Khalil','Sales Officer','2022-07-10','+970-59-2000004',2750.00,2),
(5,'Sameer','Odeh','Procurement Officer','2023-01-05','+970-59-2000005',2850.00,2),
(6,'Maya','Nasser','Warehouse Manager','2021-09-12','+970-59-3000006',3400.00,2),
(7,'Omar','Haddad','Sales Officer','2022-11-03','+970-59-3000007',2700.00,2),
(8,'Lina','Barakat','Procurement Officer','2020-04-18','+970-59-3000008',2950.00,3),
(9,'Yazan','Darwish','Warehouse Manager','2018-08-25','+970-59-3000009',3600.00,3),
(10,'Nour','Awad','Sales Officer','2023-02-14','+970-59-3000010',2650.00,3),
(11,'Dalia','Hamdan','Procurement Officer','2024-01-10','+970-59-3000011',2300.00,1),
(12,'Fadi','Saleh','Sales Officer','2024-03-22','+970-59-3000012',2250.00,2);

INSERT INTO Client (ClientID, ClientName, ClientType, Phone, City, CreditLimit) VALUES
(1,'Betunia Grand Pharmacy','Pharmacy','+970-2-2950001','Ramallah',8000.00),
(2,'Birzeit Clinic','Clinic','+970-2-2960002','Birzeit',5000.00),
(3,'Al-Hayat Pharmacy','Pharmacy','+970-9-2380003','Nablus',6000.00),
(4,'Ramallah Central Clinic','Clinic','+970-2-2981100','Ramallah',7000.00),
(5,'Nablus Health Pharmacy','Pharmacy','+970-9-2395500','Nablus',4500.00),
(6,'Jenin Medical Center','Clinic','+970-4-2503300','Jenin',6000.00),
(7,'Al-Bireh Pharmacy','Pharmacy','+970-2-2406600','Ramallah',5500.00),
(8,'Tulkarm Care Clinic','Clinic','+970-9-2671800','Tulkarm',3000.00),
(9,'Hebron Central Pharmacy','Pharmacy','+970-2-2220011','Hebron',6500.00),
(10,'Qalqilya Health Clinic','Clinic','+970-9-2941122','Qalqilya',4000.00),
(11,'Beit Jala Medical Center','Clinic','+970-2-2741133','Bethlehem',5500.00),
(12,'Salfit Pharmacy','Pharmacy','+970-9-2511144','Salfit',3500.00),
(13,'Jericho Care Pharmacy','Pharmacy','+970-2-2321155','Jericho',4800.00);

INSERT INTO Product (ProductID, ProductName, Description, UnitPrice, ReorderLevel, CategoryID) VALUES
(1,'Panadol Extra','Pain relief tablets',4.50,50,1),(2,'Voltaren Gel','Anti-inflammatory gel',12.00,20,1),(3,'Augmentin 625mg','Antibiotic tablets',18.00,20,1),(4,'Flagyl 500mg','Antibiotic medication',9.50,20,1),(5,'Brufen 600mg','Pain relief tablets',5.50,40,1),(6,'Aspirin 100mg','Blood thinner',3.50,30,1),(7,'Cetirizine 10mg','Allergy medication',7.50,30,1),(8,'Loratadine','Antihistamine',8.00,25,1),(9,'Omeprazole 20mg','Acid reflux treatment',10.00,30,1),(10,'Nexium 40mg','Stomach medication',16.00,20,1),
(11,'Paracetamol Syrup','Children fever medicine',6.00,20,1),(12,'Amoxil Syrup','Children antibiotic',12.00,15,1),(13,'Zyrtec Syrup','Allergy syrup',11.00,15,1),(14,'Cataflam 50mg','Pain relief medicine',6.50,30,1),(15,'Diclofenac Injection','Pain injection',9.00,15,1),(16,'Vitamin C 1000mg','Supplement',15.00,20,1),(17,'Vitamin D3','Supplement',18.00,20,1),(18,'Omega 3 Capsules','Supplement',25.00,15,1),(19,'Iron Tablets','Iron supplement',8.50,20,1),(20,'Calcium Tablets','Bone supplement',12.50,20,1),
(21,'Nivea Soft Cream','Moisturizer',12.00,20,2),(22,'Nivea Creme','Moisturizer',13.00,20,2),(23,'Eucerin Lotion','Skin moisturizer',22.00,15,2),(24,'Cetaphil Moisturizer','Sensitive skin lotion',28.00,10,2),(25,'Cetaphil Cleanser','Facial cleanser',25.00,15,2),(26,'Bioderma Sensibio','Micellar water',30.00,10,2),(27,'La Roche Posay Cleanser','Face cleanser',40.00,10,2),(28,'La Roche Moisturizer','Face moisturizer',45.00,10,2),(29,'Vaseline Petroleum Jelly','Skin protection',8.00,20,2),(30,'Vaseline Cocoa Butter','Body lotion',14.00,15,2),
(31,'Aloe Vera Gel','Skin soothing gel',10.00,15,2),(32,'Lip Balm Cherry','Lip care',5.00,25,2),(33,'Lip Balm Original','Lip care',5.00,25,2),(34,'Hand Cream','Moisturizing cream',7.00,20,2),(35,'Body Lotion','Body moisturizer',12.00,20,2),(36,'Baby Shampoo','Gentle shampoo',9.50,15,3),(37,'Baby Lotion','Baby moisturizer',14.00,15,3),(38,'Baby Oil','Baby massage oil',11.00,15,3),(39,'Baby Powder','Baby powder',8.00,20,3),(40,'Baby Wipes Small','Wet wipes',6.00,30,3),
(41,'Baby Wipes Large','Wet wipes',10.00,25,3),(42,'Baby Diapers Size 1','Diapers',40.00,20,3),(43,'Baby Diapers Size 2','Diapers',42.00,20,3),(44,'Baby Diapers Size 3','Diapers',45.00,20,3),(45,'Baby Diapers Size 4','Diapers',48.00,20,3),(46,'Baby Diapers Size 5','Diapers',50.00,20,3),(47,'Baby Feeding Bottle','Bottle',15.00,10,3),(48,'Baby Pacifier','Pacifier',7.00,15,3),(49,'Baby Rash Cream','Skin cream',9.00,15,3),(50,'Baby Bath Soap','Baby soap',6.50,20,3),
(51,'Head & Shoulders','Anti-dandruff shampoo',11.00,25,4),(52,'Pantene Shampoo','Hair care shampoo',13.00,20,4),(53,'Clear Shampoo','Anti-dandruff shampoo',12.00,20,4),(54,'Dove Shampoo','Hair repair shampoo',13.00,20,4),(55,'Sunsilk Shampoo','Hair care shampoo',10.00,20,4),(56,'Herbal Essences Shampoo','Herbal shampoo',16.00,15,4),(57,'Tresemme Shampoo','Professional shampoo',18.00,15,4),(58,'Johnson Baby Shampoo','Baby shampoo',9.00,20,4),(59,'Anti Hair Loss Shampoo','Hair strengthening',20.00,10,4),(60,'Keratin Shampoo','Hair treatment',22.00,10,4),
(61,'Argan Oil Shampoo','Hair care',19.00,10,4),(62,'Mint Shampoo','Refreshing shampoo',12.00,15,4),(63,'Aloe Vera Shampoo','Natural shampoo',13.00,15,4),(64,'Color Protect Shampoo','Colored hair shampoo',17.00,10,4),(65,'Daily Use Shampoo','General shampoo',9.00,20,4),(66,'Dettol Hand Wash','Hand wash',6.80,30,5),(67,'Dove Soap','Soap',4.00,40,5),(68,'Lux Soap','Soap',3.50,40,5),(69,'Palmolive Soap','Soap',3.75,40,5),(70,'Toothpaste Colgate','Toothpaste',6.00,30,5),
(71,'Toothpaste Sensodyne','Sensitive teeth',9.00,20,5),(72,'Toothbrush Soft','Toothbrush',4.00,30,5),(73,'Toothbrush Medium','Toothbrush',4.00,30,5),(74,'Mouth Wash','Oral hygiene',12.00,15,5),(75,'Dental Floss','Dental care',7.00,20,5),(76,'Shaving Cream','Shaving product',8.00,20,5),(77,'Disposable Razors','Razors',10.00,15,5),(78,'Deodorant Men','Personal care',14.00,15,5),(79,'Deodorant Women','Personal care',14.00,15,5),(80,'Cotton Swabs','Personal hygiene',4.50,30,5),
(81,'Cotton Pads','Personal hygiene',5.00,30,5),(82,'Hand Sanitizer','Sanitizer',7.00,25,5),(83,'Wet Wipes','Cleaning wipes',6.00,25,5),(84,'Facial Tissues','Tissues',3.00,40,5),(85,'Paper Towels','Paper towels',5.00,30,5),(86,'Multivitamin Tablets','Daily vitamin supplement',18.00,20,6),(87,'Vitamin B Complex','Vitamin supplement',15.00,20,6),(88,'Zinc Tablets','Immune support supplement',12.00,20,6),(89,'Magnesium Capsules','Mineral supplement',16.00,15,6),(90,'Folic Acid','Pregnancy supplement',8.00,20,6),
(91,'Digital Thermometer','Body temperature monitor',35.00,10,7),(92,'Blood Pressure Monitor','Blood pressure device',120.00,5,7),(93,'Glucometer','Blood sugar measuring device',95.00,5,7),(94,'Pulse Oximeter','Oxygen saturation monitor',60.00,10,7),(95,'Nebulizer Machine','Respiratory treatment device',180.00,3,7),(96,'Dental Floss Premium','Dental cleaning floss',8.00,20,8),(97,'Whitening Toothpaste','Teeth whitening toothpaste',10.00,20,8),(98,'Children Toothbrush','Kids toothbrush',5.00,30,8),(99,'Electric Toothbrush Heads','Replacement heads',25.00,15,8),(100,'Alcohol-Free Mouthwash','Mouth rinse',14.00,20,8),
(101,'Baby Formula Milk','Infant nutrition formula',45.00,15,9),(102,'Baby Feeding Spoon Set','Feeding accessories',8.00,15,9),(103,'Baby Bib','Baby feeding bib',6.00,20,9),(104,'Baby Teething Ring','Infant teether',7.00,20,9),(105,'Baby Blanket','Infant blanket',20.00,10,9),(106,'Sterile Gauze','Medical dressing',5.00,50,10),(107,'Medical Tape','Adhesive tape',4.00,50,10),(108,'Elastic Bandage','Support bandage',8.00,40,10),(109,'First Aid Kit','Emergency first aid kit',55.00,10,10),(110,'Antiseptic Solution','Wound cleaning solution',9.00,25,10);

INSERT INTO SupplierProduct (SupplierID, ProductID, UnitCost) VALUES
(1,1,2.00),(1,2,5.50),(1,7,3.20),(1,10,4.00),(1,37,9.00),(1,47,11.00),
(2,3,8.00),(2,5,7.50),(2,8,4.20),(2,11,7.00),(2,38,8.00),(2,46,38.00),
(3,4,6.50),(3,6,4.50),(3,9,5.80),(3,39,5.00),(3,45,36.00),
(4,12,13.00),(4,13,6.00),(4,15,8.00),(4,17,15.00),(4,40,4.00),(4,44,34.00),(4,50,5.50),
(5,14,4.80),(5,16,7.50),(5,18,1.80),(5,21,2.90),(5,41,7.50),(5,43,30.00),(5,49,4.00),
(6,19,6.00),(6,20,9.00),(6,22,5.50),(6,29,3.90),(6,42,28.00),(6,48,5.00);

INSERT INTO Batch (BatchID, ProductID, WarehouseID, QtyInStock, ExpiryDate, StorageLocation) VALUES
(1,1,1,200,'2026-12-31','Rack A-1'),(2,2,1,80,'2027-01-01','Rack A-2'),(3,3,1,50,'2026-07-01','Rack A-3'),(4,4,1,40,'2026-07-15','Rack A-4'),(5,5,1,70,'2027-06-30','Rack A-5'),(6,6,1,60,'2028-11-30','Rack A-6'),(7,7,1,120,'2027-08-31','Rack A-7'),(8,8,1,90,'2027-09-30','Rack A-8'),(9,9,1,75,'2027-11-30','Rack A-9'),(10,10,1,65,'2028-01-15','Rack A-10'),
(11,11,1,150,'2027-05-20','Rack B-1'),(12,12,1,80,'2027-06-15','Rack B-2'),(13,13,1,95,'2027-10-10','Rack B-3'),(14,14,1,130,'2027-12-01','Rack B-4'),(15,15,1,55,'2026-11-25','Rack B-5'),(16,16,1,70,'2028-02-28','Rack C-1'),(17,17,1,60,'2028-03-30','Rack C-2'),(18,18,1,45,'2028-04-30','Rack C-3'),(19,19,1,100,'2027-07-30','Rack C-4'),(20,20,1,85,'2027-08-15','Rack C-5'),
(21,21,1,110,'2028-01-01','Rack D-1'),(22,22,1,105,'2028-01-20','Rack D-2'),(23,23,1,50,'2027-12-31','Rack D-3'),(24,24,1,40,'2027-09-15','Rack D-4'),(25,25,1,35,'2027-10-15','Rack D-5'),(26,26,1,30,'2028-05-01','Rack E-1'),(27,27,1,25,'2028-06-01','Rack E-2'),(28,28,1,25,'2028-06-30','Rack E-3'),(29,29,1,140,'2027-04-30','Rack E-4'),(30,30,1,115,'2027-05-30','Rack E-5'),
(31,31,1,90,'2027-08-30','Rack F-1'),(32,32,1,160,'2029-01-01','Rack F-2'),(33,33,1,155,'2029-01-15','Rack F-3'),(34,34,1,100,'2028-07-15','Rack F-4'),(35,35,1,80,'2028-08-15','Rack F-5'),(36,36,1,70,'2028-09-10','Rack G-1'),(37,5,1,8,'2026-08-10','Rack A-5'),(38,9,1,5,'2026-07-20','Rack A-19'),(39,12,1,3,'2026-08-01','Rack B-24'),(40,15,1,10,'2026-07-30','Rack B-5'),
(41,21,1,7,'2026-07-25','Rack D-1'),(42,37,2,120,'2028-02-10','Rack A-1'),(43,38,2,95,'2028-03-15','Rack A-2'),(44,39,2,60,'2028-04-20','Rack B-1'),(45,40,2,35,'2027-11-30','Rack B-2'),(46,41,2,25,'2027-12-15','Rack C-1'),(47,42,2,0,'2026-09-01','Rack C-2'),(48,43,2,18,'2026-10-01','Rack C-3'),(49,44,3,70,'2028-05-10','Rack A-1'),(50,45,3,65,'2028-06-01','Rack A-2'),
(51,46,3,50,'2028-07-20','Rack B-1'),(52,47,3,30,'2027-10-30','Rack B-2'),(53,48,3,22,'2027-09-25','Rack C-1'),(54,49,3,8,'2026-08-05','Rack C-2'),(55,50,3,10,'2026-09-10','Rack C-3');

-- Bootstrap admin login — the app has no hardcoded credentials, so at least one
-- UserAccount row is required before anyone can sign in. CHANGE THIS PASSWORD
-- (or delete/recreate the account from the Admin > Employees tab) before any
-- real deployment; it's here only so a fresh clone is immediately usable.
INSERT INTO UserAccount (UserID, Password, Role, EmployeeID, ClientID) VALUES
(1, 'admin123', 'Admin', NULL, NULL);

-- No other UserAccount rows are seeded on purpose — once logged in as the demo
-- admin above, use "Add + Create Account" on the Employees/Clients tabs in the
-- Admin portal to create real, unique logins for each employee and client.

SELECT 'Health Plus database created successfully!' AS Message;
