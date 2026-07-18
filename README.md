# Health Plus - Warehouse Management System

A Java/JavaFX desktop application for managing a pharmaceutical warehouse. Built for the COMP333 Database Systems course at Birzeit University.

## Project Overview

Health Plus digitizes the day-to-day operations of a pharmaceutical distributor: tracking products and batches across multiple warehouses, managing suppliers and purchase orders, taking client sale orders, recording payments, and giving admins a live dashboard of the business. The app is backed by a normalized MySQL schema and accessed through three role-specific JavaFX portals.

## Features

Three user portals, gated behind a single login screen:

- **Admin Portal** — full CRUD over every entity (categories, products, warehouses, batches, suppliers, clients, employees, supplier-product links, inventory transactions), a KPI dashboard with revenue/cost/stock charts, and a cart-style Purchase Order builder for restocking from suppliers.
- **Employee Portal** — day-to-day operations scoped to the employee's assigned warehouse: approving and delivering sale orders, creating purchase orders, and receiving stock.
- **Client Portal** — a shopping-cart experience for pharmacies/clinics to browse the product catalog, place sale orders, pay online, and track order history.

## Tech Stack

- **Java** (JDK 17+)
- **JavaFX** — desktop UI toolkit
- **MySQL** — relational database
- **JDBC** — database connectivity (MySQL Connector/J)

## Database

15 tables, normalized to 3NF: `Category`, `Product`, `Warehouse`, `Batch`, `Supplier`, `SupplierProduct`, `Client`, `Employee`, `PurchaseOrder`, `PurchaseOrderItem`, `SaleOrder`, `SaleOrderItem`, `Payment`, `InventoryTransaction`, `UserAccount`.

## How to Run

**Requirements:** MySQL 8+, Java 17+, [JavaFX SDK](https://openjfx.io/) 17+.

1. Create the `healthplus` database in MySQL and load the schema (see your course-provided SQL script / ER diagram).
2. Copy the credentials template and fill in your own MySQL user/password:
   ```
   cp db.properties.example db.properties
   ```
   Edit `db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/healthplus?useSSL=false&serverTimezone=UTC
   db.user=root
   db.password=YOUR_PASSWORD_HERE
   ```
   `db.properties` is gitignored — every developer keeps their own local copy, so no password is ever committed.
3. Compile and run with the JavaFX SDK on the module path, e.g.:
   ```
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -d out -cp mysql-connector-j.jar src/application/*.java
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -cp "out;mysql-connector-j.jar" application.Main
   ```
   (Or open the project in IntelliJ IDEA with the JavaFX and MySQL Connector/J libraries attached, and run `Main`.)

## Team

- Lara Daifallah — 1230239
- Shatha Abualrub — 1231279

## Course

COMP333 — Database Systems, Dr. Bassem Sayrafi, Birzeit University.
