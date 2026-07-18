# 💊 Health Plus — Warehouse Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-Desktop_UI-orange?style=flat)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-Data_Access-2E6DA4?style=flat)
![Status](https://img.shields.io/badge/Status-Complete-3fb950?style=flat)

> Java/JavaFX desktop app for running a pharmaceutical warehouse — COMP333 Database Systems course project @ Birzeit University 🇵🇸

---

## 📦 Overview

Health Plus digitizes a pharmaceutical distributor's operations: products and batches across multiple warehouses, suppliers and purchase orders, client sale orders, payments, and a live admin dashboard. Backed by a 3NF-normalized MySQL schema and three role-specific JavaFX portals.

---

## 🖥️ Features

- 🔐 Single login screen, routed by role (Admin / Employee / Client)
- 🛠️ **Admin Portal** — full CRUD on every entity via dropdown-driven forms (no typed-in IDs), KPI dashboard with revenue/cost/stock charts, cart-style Purchase Order builder
- 👷 **Employee Portal** — warehouse-scoped sale order approval & delivery, purchase order creation, stock receiving
- 🛒 **Client Portal** — shopping-cart UX: browse catalog, search/filter, place & pay for sale orders, track order history
- 📅 Calendar date pickers throughout (no manual `yyyy-mm-dd` typing)
- 📊 Canvas-drawn bar/pie charts for revenue, category sales, warehouse stock, top clients & products

---

## 🏗️ Architecture

| Layer | Tech | Role |
|-------|------|------|
| UI | JavaFX (Application, Scene, TabPane, TableView, Canvas) | Login screen + 3 portals |
| Data access | JDBC + hand-written DAOs | One DAO per entity, plain SQL/PreparedStatement |
| Database | MySQL, 15 tables, 3NF | `Category` · `Product` · `Warehouse` · `Batch` · `Supplier` · `SupplierProduct` · `Client` · `Employee` · `PurchaseOrder` · `PurchaseOrderItem` · `SaleOrder` · `SaleOrderItem` · `Payment` · `InventoryTransaction` · `UserAccount` |

---

## 📁 Project structure

```
src/application/
├── Main.java                  Entry point → LoginScreen
├── LoginScreen.java           Auth, routes to a portal by role
├── DBConnection.java          Loads db.properties, opens JDBC connections
├── ProductApp.java            Admin portal (all CRUD tabs + dashboard + charts)
├── EmployeePortal.java        Employee portal
├── ClientPortal.java          Client portal (shop / cart / orders / payments)
│
├── Batch.java · Category.java · Client.java · Employee.java
├── InventoryTransaction.java · Payment.java · Product.java
├── PurchaseOrder.java · PurchaseOrderItem.java · SaleOrder.java
├── SaleOrderItem.java · Supplier.java · SupplierProduct.java
├── UserAccount.java · Warehouse.java              Entity classes
│
└── *DAO.java                                       One DAO per entity
```

---

## 🚀 Running locally

**Requirements:** MySQL 8+, Java 17+, [JavaFX SDK](https://openjfx.io/) 17+.

1. Create the `healthplus` database in MySQL and load the schema (course-provided SQL script / ER diagram).
2. Copy the credentials template and fill in your own MySQL user/password — it's gitignored, so nothing gets committed:
   ```bash
   cp db.properties.example db.properties
   ```
   ```properties
   db.url=jdbc:mysql://localhost:3306/healthplus?useSSL=false&serverTimezone=UTC
   db.user=root
   db.password=YOUR_PASSWORD_HERE
   ```
3. Compile & run with the JavaFX SDK on the module path:
   ```bash
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
         -d out -cp mysql-connector-j.jar src/application/*.java

   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
        -cp "out;mysql-connector-j.jar" application.Main
   ```
   Or open the project in IntelliJ IDEA with the JavaFX and MySQL Connector/J libraries attached and run `Main`.

---

## 👥 Team

**Lara Daifallah** (1230239) · **Shatha Abualrub** (1231279)
COMP333 Database Systems · Dr. Bassem Sayrafi · Birzeit University 🇵🇸

[![GitHub](https://img.shields.io/badge/GitHub-Holmes--99-181717?style=flat&logo=github)](https://github.com/Holmes-99)
[![GitHub](https://img.shields.io/badge/GitHub-LaraDaifallah-181717?style=flat&logo=github)](https://github.com/LaraDaifallah)
