//Group 27 | Lara Daifallah 1230239 & Shatha Abualrub 1231279
package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProductApp extends Application {

    private static final String DARK = "#1B3A6B";
    private static final String MID = "#2E6DA4";
    private static final String GREEN = "#16A34A";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#D97706";
    private static final String GRAY = "#F5F7FA";

    private Label statusBar;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Health Plus - Admin Portal");
        statusBar = new Label("Ready");
        statusBar.setMaxWidth(Double.MAX_VALUE);
        statusBar.setPadding(new Insets(8, 16, 8, 16));
        statusBar.setFont(Font.font("Arial", 12));
        statusBar.setStyle("-fx-background-color:#F0FDF4; -fx-text-fill:#16A34A;" +
                " -fx-border-color:#BBF7D0; -fx-border-width:1 0 0 0;");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setStyle("-fx-background-color:" + DARK + ";");
        Label logo = new Label("HP");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo.setTextFill(Color.WHITE);
        VBox titleBox = new VBox(2);
        Label title = new Label("Health Plus - Admin Portal");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);
        Label subtitle = new Label("Full system access | Group 27");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#A8C4E0"));
        titleBox.getChildren().addAll(title, subtitle);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:#A8C4E0; -fx-border-color:#A8C4E0;" +
                " -fx-border-radius:6; -fx-background-radius:6; -fx-cursor:hand; -fx-padding:6 14;");
        logoutBtn.setOnAction(e -> {
            stage.close();
            try {
                new LoginScreen().start(new Stage());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        header.getChildren().addAll(logo, titleBox, sp, logoutBtn);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
                tab("Dashboard", buildDashboard()), tab("Categories", buildCategoryTab()), tab("Products", buildProductTab()),
                tab("Warehouses", buildWarehouseTab()), tab("Batches", buildBatchTab()), tab("Suppliers", buildSupplierTab()),
                tab("Clients", buildClientTab()), tab("Employees", buildEmployeeTab()), tab("Sup-Product", buildSupplierProductTab()),
                tab("Purchase Orders", buildPurchaseTab()), tab("Sale Orders", buildSaleTab()), tab("Payments", buildPaymentTab()),
                tab("Inventory", buildInventoryTab()), tab("Charts & Stats", buildChartsTab())
        );
        VBox root = new VBox(0, header, tabs, statusBar);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        Scene scene = new Scene(root, 1050, 750);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildDashboard() {
        VBox pane = pane();
        sectionTitle(pane, "Dashboard - Overview");

        HBox kpiRow = new HBox(12);
        kpiRow.setAlignment(Pos.CENTER_LEFT);
        try {
            Connection conn = DBConnection.connect();
            ResultSet r1 = conn.createStatement().executeQuery(
                    "SELECT COALESCE(SUM(Amount),0) AS Rev FROM Payment" +
                            " WHERE Direction='Incoming'");
            double revenue = r1.next() ? r1.getDouble("Rev") : 0;
            ResultSet r2 = conn.createStatement().executeQuery(
                    "SELECT COALESCE(SUM(soi.QtyOrdered * sp.UnitCost), 0) AS Cost " +
                            "FROM SaleOrderItem soi " +
                            "JOIN Batch b ON soi.BatchID = b.BatchID " +
                            "JOIN SupplierProduct sp ON b.ProductID = sp.ProductID");
            double cost = r2.next() ? r2.getDouble("Cost") : 0;
            ResultSet r3 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) AS C FROM SaleOrder" +
                            " WHERE Status='Pending'");
            int pending = r3.next() ? r3.getInt("C") : 0;
            ResultSet r4 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) AS C FROM Batch" +
                            " WHERE QtyInStock < (SELECT ReorderLevel " +
                            "FROM Product p" +
                            " WHERE p.ProductID=Batch.ProductID)");
            int lowStock = r4.next() ? r4.getInt("C") : 0;
            ResultSet r5 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) AS C FROM Client");
            int clients = r5.next() ? r5.getInt("C") : 0;
            ResultSet r6 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) AS C FROM SaleOrder" +
                            " WHERE PaymentStatus='Unpaid' OR PaymentStatus='Partial'");
            int unpaid = r6.next() ? r6.getInt("C") : 0;
            conn.close();
            kpiRow.getChildren().addAll(
                    kpi("Revenue (Paid)", String.format("%.0f NIS", revenue), GREEN),
                    kpi("Cost of Sales", String.format("%.0f NIS", cost), RED),
                    kpi("Gross Profit", String.format("%.0f NIS", revenue - cost), revenue >= cost ? GREEN : RED),
                    kpi("Pending Orders", String.valueOf(pending), ORANGE),
                    kpi("Low Stock", String.valueOf(lowStock), lowStock > 0 ? RED : GREEN),
                    kpi("Unpaid Orders", String.valueOf(unpaid), unpaid > 0 ? ORANGE : GREEN),
                    kpi("Clients", String.valueOf(clients), MID)
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        pane.getChildren().add(kpiRow);
        pane.getChildren().add(new Separator());

        Label soTitle = new Label("Recent Sale Orders");
        soTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        soTitle.setTextFill(Color.web(DARK));
        pane.getChildren().add(soTitle);
        TableView<ObservableList<String>> soTable = new TableView<>();
        soTable.setPrefHeight(180);
        soTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        soTable.setPlaceholder(new Label("No orders."));
        addCols(soTable, new String[]{"SO#", "Client", "Date", "Status", "Payment", "Total (NIS)"}, new int[]{55, 150, 90, 90, 80, 90});

        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT so.SaleOrderID, cl.ClientName, so.OrderDate, so.Status, so.PaymentStatus, so.TotalAmount " +
                            "FROM SaleOrder so JOIN Client cl ON so.ClientID=cl.ClientID ORDER BY so.OrderDate DESC LIMIT 10");
            while (rs.next()) {
                soTable.getItems().add(FXCollections.observableArrayList(
                        String.valueOf(rs.getInt("SaleOrderID")),
                        rs.getString("ClientName"), rs.getString("OrderDate"),
                        rs.getString("Status"), rs.getString("PaymentStatus"),
                        String.format("%.2f", rs.getDouble("TotalAmount"))));
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        pane.getChildren().add(soTable);
        pane.getChildren().add(new Separator());

        HBox midRow = new HBox(16);

        VBox bestBox = new VBox(8);
        bestBox.setPrefWidth(460);
        Label bestTitle = new Label("Top 5 Best-Selling Products");
        bestTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        bestTitle.setTextFill(Color.web(DARK));
        TableView<ObservableList<String>> bestTable = new TableView<>();
        bestTable.setPrefHeight(160);
        bestTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        addCols(bestTable, new String[]{"Product", "Category", "Total Sold"}, new int[]{200, 120, 90});
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT p.ProductName, c.Name AS Cat, SUM(soi.QtyOrdered) AS TotalSold " +
                            "FROM SaleOrderItem soi" +
                            " JOIN Batch b ON soi.BatchID=b.BatchID " +
                            "JOIN Product p ON b.ProductID=p.ProductID" +
                            " JOIN Category c ON p.CategoryID=c.CategoryID " +
                            "GROUP BY p.ProductID" +
                            " ORDER BY TotalSold DESC LIMIT 5");
            while (rs.next()) {
                bestTable.getItems().add(FXCollections.observableArrayList(
                        rs.getString("ProductName"), rs.getString("Cat"), String.valueOf(rs.getInt("TotalSold"))));
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        bestBox.getChildren().addAll(bestTitle, bestTable);

        VBox lowBox = new VBox(8);
        lowBox.setPrefWidth(460);
        Label lowTitle = new Label("Low Stock Alerts");
        lowTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lowTitle.setTextFill(Color.web(RED));
        TableView<ObservableList<String>> lowTable = new TableView<>();
        lowTable.setPrefHeight(160);
        lowTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        addCols(lowTable, new String[]{"Product", "Warehouse", "Qty", "Reorder"}, new int[]{160, 140, 60, 70});
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT p.ProductName, w.WarehouseName, b.QtyInStock, p.ReorderLevel " +
                            "FROM Batch b JOIN Product p ON b.ProductID=p.ProductID " +
                            "JOIN Warehouse w ON b.WarehouseID=w.WarehouseID " +
                            "WHERE b.QtyInStock < p.ReorderLevel " +
                            "ORDER BY b.QtyInStock ASC");
            while (rs.next()) {
                lowTable.getItems().add(FXCollections.observableArrayList(
                        rs.getString("ProductName"), rs.getString("WarehouseName"),
                        String.valueOf(rs.getInt("QtyInStock")), String.valueOf(rs.getInt("ReorderLevel"))));
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        lowTable.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(ObservableList<String> row, boolean empty) {
                super.updateItem(row, empty);
                setStyle(row != null && !empty ? "-fx-background-color:#FEE2E2;" : "");
            }
        });
        lowBox.getChildren().addAll(lowTitle, lowTable);
        midRow.getChildren().addAll(bestBox, lowBox);
        HBox.setHgrow(bestBox, Priority.ALWAYS);
        HBox.setHgrow(lowBox, Priority.ALWAYS);
        pane.getChildren().add(midRow);

        pane.getChildren().add(new Separator());
        Label whTitle = new Label("Warehouse Capacity");
        whTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        whTitle.setTextFill(Color.web(DARK));
        pane.getChildren().add(whTitle);
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT w.WarehouseName, w.Capacity, COALESCE(SUM(b.QtyInStock),0) AS Used " +
                            "FROM Warehouse w LEFT JOIN Batch b ON w.WarehouseID=b.WarehouseID " +
                            "GROUP BY w.WarehouseID");
            while (rs.next()) {
                String name = rs.getString("WarehouseName");
                int cap = rs.getInt("Capacity");
                int used = rs.getInt("Used");
                double pct = cap > 0 ? (double)used/cap*100 : 0;
                HBox whRow = new HBox(12);
                whRow.setAlignment(Pos.CENTER_LEFT);
                Label nameLbl = new Label(name);
                nameLbl.setPrefWidth(240);
                nameLbl.setStyle("-fx-font-size:12px; -fx-font-weight:bold;");
                ProgressBar bar = new ProgressBar(pct / 100.0);
                bar.setPrefWidth(300);
                bar.setPrefHeight(18);
                bar.setStyle(pct > 80 ? "-fx-accent:" + RED + ";" : pct > 50 ? "-fx-accent:" + ORANGE + ";" : "-fx-accent:" + GREEN + ";");
                Label pctLbl = new Label(String.format("%.1f%% (%d / %d)", pct, used, cap));
                pctLbl.setStyle("-fx-font-size:12px; -fx-text-fill:#64748B;");
                whRow.getChildren().addAll(nameLbl, bar, pctLbl);
                pane.getChildren().add(whRow);
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return pane;
    }

    private VBox buildCategoryTab() {
        VBox pane = pane();
        sectionTitle(pane, "Categories");

        TextField nameField = field("Category Name");
        TextField descField = field("Description");

        // Dropdown for update/delete — no more typing IDs
        ComboBox<String> catPicker = new ComboBox<>();
        catPicker.setPromptText("Select category to update/delete...");
        catPicker.setStyle("-fx-font-size:13px;");
        catPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> catIdMap = new java.util.LinkedHashMap<>();

        pane.getChildren().addAll(row(nameField, descField), catPicker);

        Button btnAdd = btn("Add", GREEN);
        Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Name","Description"}, new int[]{60,180,320});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear();
            catPicker.getItems().clear();
            catIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Category ORDER BY Name");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("CategoryID")), rs.getString("Name"), rs.getString("Description")));
                    catPicker.getItems().add(rs.getString("Name"));
                    catIdMap.put(rs.getString("Name"), rs.getInt("CategoryID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        // Click row -> fill form and select in dropdown
        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                nameField.setText(row.get(1));
                descField.setText(row.get(2));
                catPicker.setValue(row.get(1));
            }
        });

        // Select dropdown -> fill form
        catPicker.setOnAction(e -> {
            String sel = catPicker.getValue();
            if (sel == null) return;
            for (ObservableList<String> row : table.getItems()) {
                if (row.get(1).equals(sel)) { nameField.setText(row.get(1)); descField.setText(row.get(2)); break; }
            }
        });

        btnAdd.setOnAction(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            if (name.isEmpty()) { err("Name required."); return; }
            CategoryDAO.addCategory(new Category(name, desc));
            ok("Category added!");
            nameField.clear(); descField.clear(); catPicker.setValue(null);
            refresh.run();
        });

        btnUpd.setOnAction(e -> {
            String sel = catPicker.getValue();
            if (sel == null) { err("Select a category from the dropdown."); return; }
            int id = catIdMap.get(sel);
            String name = nameField.getText().trim().isEmpty() ? sel : nameField.getText().trim();
            String desc = descField.getText().trim();
            CategoryDAO.updateCategory(new Category(id, name, desc));
            ok("Category updated!");
            nameField.clear(); descField.clear(); catPicker.setValue(null);
            refresh.run();
        });

        btnDel.setOnAction(e -> {
            String sel = catPicker.getValue();
            if (sel == null) { err("Select a category from the dropdown."); return; }
            if (!confirmDelete("category '" + sel + "'")) return;
            boolean ok = CategoryDAO.deleteCategory(catIdMap.get(sel));
            if (ok) { ok("Category deleted!"); nameField.clear(); descField.clear(); catPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked products.");
        });

        btnRef.setOnAction(e -> { nameField.clear(); descField.clear(); catPicker.setValue(null); refresh.run(); });
        return pane;
    }

    private VBox buildProductTab() {
        VBox pane = pane();
        sectionTitle(pane, "Products");

        TextField nameField  = field("Product Name");
        TextField descField  = field("Description");
        TextField priceField = field("Unit Price (NIS)");
        TextField reordField = field("Reorder Level");

        // Category dropdown
        ComboBox<String> catBox = new ComboBox<>();
        catBox.setPromptText("Select category...");
        catBox.setStyle("-fx-font-size:13px;");
        catBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> catMap = new java.util.LinkedHashMap<>();

        // Product dropdown for update/delete
        ComboBox<String> prodPicker = new ComboBox<>();
        prodPicker.setPromptText("Select product to update/delete...");
        prodPicker.setStyle("-fx-font-size:13px;");
        prodPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> prodIdMap = new java.util.LinkedHashMap<>();

        Label catLabel  = new Label("Category:"); catLabel.setStyle("-fx-font-weight:bold;");
        Label selLabel  = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");

        pane.getChildren().addAll(
                row(nameField, descField),
                row(priceField, reordField),
                new VBox(4, catLabel, catBox),
                new VBox(4, selLabel, prodPicker)
        );

        Button btnAdd = btn("Add", GREEN);
        Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Name","Description","Price (NIS)","Reorder","Category"}, new int[]{55,160,180,80,70,120});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear();
            catBox.getItems().clear(); catMap.clear();
            prodPicker.getItems().clear(); prodIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsCat = conn.createStatement().executeQuery("SELECT * FROM Category ORDER BY Name");
                while (rsCat.next()) { catMap.put(rsCat.getString("Name"), rsCat.getInt("CategoryID")); catBox.getItems().add(rsCat.getString("Name")); }
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT p.ProductID, p.ProductName, p.Description, p.UnitPrice, p.ReorderLevel, c.Name AS Cat " +
                                "FROM Product p JOIN Category c ON p.CategoryID=c.CategoryID ORDER BY p.ProductName");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("ProductID")), rs.getString("ProductName"),
                            rs.getString("Description"), String.format("%.2f", rs.getDouble("UnitPrice")),
                            String.valueOf(rs.getInt("ReorderLevel")), rs.getString("Cat")));
                    prodPicker.getItems().add(rs.getString("ProductName"));
                    prodIdMap.put(rs.getString("ProductName"), rs.getInt("ProductID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        // Click row -> fill form
        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                nameField.setText(row.get(1)); descField.setText(row.get(2));
                priceField.setText(row.get(3)); reordField.setText(row.get(4));
                catBox.setValue(row.get(5)); prodPicker.setValue(row.get(1));
            }
        });

        prodPicker.setOnAction(e -> {
            String sel = prodPicker.getValue();
            if (sel == null) return;
            for (ObservableList<String> row : table.getItems()) {
                if (row.get(1).equals(sel)) {
                    nameField.setText(row.get(1)); descField.setText(row.get(2));
                    priceField.setText(row.get(3)); reordField.setText(row.get(4));
                    catBox.setValue(row.get(5)); break;
                }
            }
        });

        btnAdd.setOnAction(e -> {
            try {
                if (catBox.getValue() == null) { err("Select a category."); return; }
                boolean ok = ProductDAO.addProduct(new Product(
                        nameField.getText().trim(), descField.getText().trim(),
                        Double.parseDouble(priceField.getText().trim()),
                        Integer.parseInt(reordField.getText().trim()),
                        catMap.get(catBox.getValue())));
                if (ok) { ok("Product added!"); nameField.clear(); descField.clear(); priceField.clear(); reordField.clear(); catBox.setValue(null); prodPicker.setValue(null); refresh.run(); }
                else err("Failed.");
            } catch (Exception ex) { err("Invalid price or reorder level."); }
        });

        btnUpd.setOnAction(e -> {
            String sel = prodPicker.getValue();
            if (sel == null) { err("Select a product from the dropdown."); return; }
            try {
                int id = prodIdMap.get(sel);
                Product ex = ProductDAO.getProductById(id);
                if (ex == null) { err("Product not found."); return; }
                ProductDAO.updateProduct(new Product(id,
                        nameField.getText().trim().isEmpty()  ? ex.getName()         : nameField.getText().trim(),
                        descField.getText().trim().isEmpty()  ? ex.getDescription()  : descField.getText().trim(),
                        priceField.getText().trim().isEmpty() ? ex.getPrice()        : Double.parseDouble(priceField.getText().trim()),
                        reordField.getText().trim().isEmpty() ? ex.getReorderLevel() : Integer.parseInt(reordField.getText().trim()),
                        catBox.getValue() == null             ? ex.getCategoryId()   : catMap.get(catBox.getValue())));
                ok("Product updated!"); nameField.clear(); descField.clear(); priceField.clear(); reordField.clear(); catBox.setValue(null); prodPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = prodPicker.getValue();
            if (sel == null) { err("Select a product from the dropdown."); return; }
            if (!confirmDelete("product '" + sel + "'")) return;
            boolean ok = ProductDAO.deleteProduct(prodIdMap.get(sel));
            if (ok) { ok("Product deleted!"); prodPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked batches or orders.");
        });

        btnRef.setOnAction(e -> { nameField.clear(); descField.clear(); priceField.clear(); reordField.clear(); catBox.setValue(null); prodPicker.setValue(null); refresh.run(); });
        return pane;
    }

    private VBox buildWarehouseTab() {
        VBox pane = pane();
        sectionTitle(pane, "Warehouses");

        TextField nameField = field("Warehouse Name");
        TextField addrField = field("Address");
        TextField cityField = field("City");
        TextField phoneField = field("Phone");
        TextField capField  = field("Capacity (units)");

        ComboBox<String> whPicker = new ComboBox<>();
        whPicker.setPromptText("Select warehouse to update/delete...");
        whPicker.setStyle("-fx-font-size:13px;"); whPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> whIdMap = new java.util.LinkedHashMap<>();

        Label selLabel = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");
        pane.getChildren().addAll(
                row(nameField, addrField), row(cityField, phoneField, capField),
                new VBox(4, selLabel, whPicker));

        Button btnAdd = btn("Add", GREEN); Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED); Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Name","Address","City","Phone","Capacity"}, new int[]{55,180,160,100,120,80});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear(); whPicker.getItems().clear(); whIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Warehouse ORDER BY WarehouseName");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("WarehouseID")), rs.getString("WarehouseName"),
                            rs.getString("Address"), rs.getString("City"), rs.getString("Phone"),
                            String.valueOf(rs.getInt("Capacity"))));
                    whPicker.getItems().add(rs.getString("WarehouseName"));
                    whIdMap.put(rs.getString("WarehouseName"), rs.getInt("WarehouseID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                nameField.setText(row.get(1)); addrField.setText(row.get(2));
                cityField.setText(row.get(3)); phoneField.setText(row.get(4));
                capField.setText(row.get(5)); whPicker.setValue(row.get(1));
            }
        });

        btnAdd.setOnAction(e -> {
            try {
                boolean ok = WarehouseDAO.addWarehouse(new Warehouse(
                        nameField.getText().trim(), addrField.getText().trim(),
                        cityField.getText().trim(), phoneField.getText().trim(),
                        Integer.parseInt(capField.getText().trim())));
                if (ok) { ok("Warehouse added!"); nameField.clear(); addrField.clear(); cityField.clear(); phoneField.clear(); capField.clear(); refresh.run(); }
                else err("Failed.");
            } catch (Exception ex) { err("Invalid capacity."); }
        });

        btnUpd.setOnAction(e -> {
            String sel = whPicker.getValue();
            if (sel == null) { err("Select a warehouse from the dropdown."); return; }
            try {
                int id = whIdMap.get(sel);
                Warehouse ex = WarehouseDAO.getWarehouseById(id);
                if (ex == null) { err("Not found."); return; }
                WarehouseDAO.updateWarehouse(new Warehouse(id,
                        nameField.getText().trim().isEmpty()  ? ex.getName()     : nameField.getText().trim(),
                        addrField.getText().trim().isEmpty()  ? ex.getAddress()  : addrField.getText().trim(),
                        cityField.getText().trim().isEmpty()  ? ex.getCity()     : cityField.getText().trim(),
                        phoneField.getText().trim().isEmpty() ? ex.getPhone()    : phoneField.getText().trim(),
                        capField.getText().trim().isEmpty()   ? ex.getCapacity() : Integer.parseInt(capField.getText().trim())));
                ok("Warehouse updated!"); nameField.clear(); addrField.clear(); cityField.clear(); phoneField.clear(); capField.clear(); whPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = whPicker.getValue();
            if (sel == null) { err("Select a warehouse from the dropdown."); return; }
            if (!confirmDelete("warehouse '" + sel + "'")) return;
            boolean ok = WarehouseDAO.deleteWarehouse(whIdMap.get(sel));
            if (ok) { ok("Warehouse deleted!"); whPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked batches.");
        });

        btnRef.setOnAction(e -> { nameField.clear(); addrField.clear(); cityField.clear(); phoneField.clear(); capField.clear(); whPicker.setValue(null); refresh.run(); });
        return pane;
    }

    private VBox buildBatchTab() {
        VBox pane = pane();
        sectionTitle(pane, "Batches");

        // Product dropdown
        ComboBox<String> prodBox = new ComboBox<>();
        prodBox.setPromptText("Select product...");
        prodBox.setStyle("-fx-font-size:13px;"); prodBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> prodMap = new java.util.LinkedHashMap<>();

        // Warehouse dropdown
        ComboBox<String> whBox = new ComboBox<>();
        whBox.setPromptText("Select warehouse...");
        whBox.setStyle("-fx-font-size:13px;"); whBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> whMap = new java.util.LinkedHashMap<>();

        TextField qtyField = field("Quantity in Stock");

        // DatePicker for expiry
        DatePicker expPicker = new DatePicker();
        expPicker.setPromptText("Expiry Date");
        expPicker.setStyle("-fx-font-size:13px;"); expPicker.setMaxWidth(Double.MAX_VALUE);

        TextField locField = field("Storage Location (e.g. Rack A-1)");

        // Batch picker for update/delete
        ComboBox<String> batchPicker = new ComboBox<>();
        batchPicker.setPromptText("Select batch to update/delete...");
        batchPicker.setStyle("-fx-font-size:13px;"); batchPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> batchIdMap = new java.util.LinkedHashMap<>();

        Label prodLabel  = new Label("Product:");  prodLabel.setStyle("-fx-font-weight:bold;");
        Label whLabel    = new Label("Warehouse:"); whLabel.setStyle("-fx-font-weight:bold;");
        Label expLabel   = new Label("Expiry Date:"); expLabel.setStyle("-fx-font-weight:bold;");
        Label selLabel   = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");

        HBox dropRow = new HBox(10,
                new VBox(4, prodLabel, prodBox),
                new VBox(4, whLabel, whBox));
        HBox.setHgrow(dropRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(dropRow.getChildren().get(1), Priority.ALWAYS);

        pane.getChildren().addAll(
                dropRow,
                row(qtyField, locField),
                new VBox(4, expLabel, expPicker),
                new VBox(4, selLabel, batchPicker)
        );

        Button btnAdd = btn("Add Batch", GREEN);
        Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Product","Warehouse","Qty","Expiry","Location"}, new int[]{55,170,160,60,95,90});
        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(ObservableList<String> row, boolean empty) {
                super.updateItem(row, empty);
                if (row == null || empty) { setStyle(""); return; }
                try {
                    String exp = row.get(4);
                    if (!exp.equals("-") && LocalDate.parse(exp).isBefore(LocalDate.now().plusDays(60)))
                        setStyle("-fx-background-color:#FEE2E2;");
                    else setStyle("");
                } catch (Exception ex) { setStyle(""); }
            }
        });
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear();
            prodBox.getItems().clear(); prodMap.clear();
            whBox.getItems().clear(); whMap.clear();
            batchPicker.getItems().clear(); batchIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsProd = conn.createStatement().executeQuery("SELECT ProductID, ProductName FROM Product ORDER BY ProductName");
                while (rsProd.next()) { prodMap.put(rsProd.getString("ProductName"), rsProd.getInt("ProductID")); prodBox.getItems().add(rsProd.getString("ProductName")); }
                ResultSet rsWh = conn.createStatement().executeQuery("SELECT WarehouseID, WarehouseName FROM Warehouse ORDER BY WarehouseName");
                while (rsWh.next()) { whMap.put(rsWh.getString("WarehouseName"), rsWh.getInt("WarehouseID")); whBox.getItems().add(rsWh.getString("WarehouseName")); }
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT b.BatchID, p.ProductName, w.WarehouseName, b.QtyInStock, b.ExpiryDate, b.StorageLocation " +
                                "FROM Batch b JOIN Product p ON b.ProductID=p.ProductID JOIN Warehouse w ON b.WarehouseID=w.WarehouseID ORDER BY p.ProductName");
                while (rs.next()) {
                    String label = "Batch #" + rs.getInt("BatchID") + " - " + rs.getString("ProductName");
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("BatchID")), rs.getString("ProductName"), rs.getString("WarehouseName"),
                            String.valueOf(rs.getInt("QtyInStock")),
                            rs.getString("ExpiryDate") != null ? rs.getString("ExpiryDate") : "-",
                            rs.getString("StorageLocation") != null ? rs.getString("StorageLocation") : "-"));
                    batchPicker.getItems().add(label);
                    batchIdMap.put(label, rs.getInt("BatchID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        // Click row -> fill form
        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                prodBox.setValue(row.get(1)); whBox.setValue(row.get(2));
                qtyField.setText(row.get(3));
                if (!row.get(4).equals("-")) expPicker.setValue(LocalDate.parse(row.get(4)));
                locField.setText(row.get(5));
                String bLabel = "Batch #" + row.get(0) + " - " + row.get(1);
                batchPicker.setValue(bLabel);
            }
        });

        btnAdd.setOnAction(e -> {
            try {
                if (prodBox.getValue() == null) { err("Select a product."); return; }
                if (whBox.getValue() == null) { err("Select a warehouse."); return; }
                int qty = Integer.parseInt(qtyField.getText().trim());
                int whId = whMap.get(whBox.getValue());
                if (!WarehouseDAO.hasEnoughCapacity(whId, qty)) { err("Not enough warehouse capacity."); return; }
                String expiry = expPicker.getValue() != null ? expPicker.getValue().toString() : null;
                boolean ok = BatchDAO.addBatch(new Batch(prodMap.get(prodBox.getValue()), whId, qty, expiry, locField.getText().trim()));
                if (ok) { ok("Batch added!"); prodBox.setValue(null); whBox.setValue(null); qtyField.clear(); expPicker.setValue(null); locField.clear(); refresh.run(); }
                else err("Failed.");
            } catch (Exception ex) { err("Invalid quantity."); }
        });

        btnUpd.setOnAction(e -> {
            String sel = batchPicker.getValue();
            if (sel == null) { err("Select a batch from the dropdown."); return; }
            try {
                int id = batchIdMap.get(sel);
                Batch ex = BatchDAO.getBatchById(id);
                if (ex == null) { err("Batch not found."); return; }
                BatchDAO.updateBatch(new Batch(id,
                        prodBox.getValue() == null    ? ex.getProductId()       : prodMap.get(prodBox.getValue()),
                        whBox.getValue() == null      ? ex.getWarehouseId()     : whMap.get(whBox.getValue()),
                        qtyField.getText().trim().isEmpty() ? ex.getQuantity()  : Integer.parseInt(qtyField.getText().trim()),
                        expPicker.getValue() != null  ? expPicker.getValue().toString() : ex.getExpiryDate(),
                        locField.getText().trim().isEmpty() ? ex.getStorageLocation() : locField.getText().trim()));
                ok("Batch updated!"); prodBox.setValue(null); whBox.setValue(null); qtyField.clear(); expPicker.setValue(null); locField.clear(); batchPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = batchPicker.getValue();
            if (sel == null) { err("Select a batch from the dropdown."); return; }
            if (!confirmDelete("batch '" + sel + "'")) return;
            boolean ok = BatchDAO.deleteBatch(batchIdMap.get(sel));
            if (ok) { ok("Batch deleted!"); batchPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked transactions.");
        });

        btnRef.setOnAction(e -> { prodBox.setValue(null); whBox.setValue(null); qtyField.clear(); expPicker.setValue(null); locField.clear(); batchPicker.setValue(null); refresh.run(); });
        return pane;
    }
    private VBox buildSupplierTab() {
        VBox pane = pane();
        sectionTitle(pane, "Suppliers");

        TextField nameField  = field("Supplier Name");
        TextField contField  = field("Contact Person");
        TextField phoneField = field("Phone");
        TextField emailField = field("Email");
        TextField cityField  = field("City");

        ComboBox<String> supPicker = new ComboBox<>();
        supPicker.setPromptText("Select supplier to update/delete...");
        supPicker.setStyle("-fx-font-size:13px;"); supPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> supIdMap = new java.util.LinkedHashMap<>();

        Label selLabel = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");
        pane.getChildren().addAll(
                row(nameField, contField), row(phoneField, emailField, cityField),
                new VBox(4, selLabel, supPicker));

        Button btnAdd = btn("Add", GREEN); Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED); Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Name","Contact","Phone","Email","City"}, new int[]{55,150,120,110,160,100});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear(); supPicker.getItems().clear(); supIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Supplier ORDER BY SupplierName");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("SupplierID")), rs.getString("SupplierName"),
                            rs.getString("ContactPerson"), rs.getString("Phone"), rs.getString("Email"), rs.getString("City")));
                    supPicker.getItems().add(rs.getString("SupplierName"));
                    supIdMap.put(rs.getString("SupplierName"), rs.getInt("SupplierID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                nameField.setText(row.get(1)); contField.setText(row.get(2));
                phoneField.setText(row.get(3)); emailField.setText(row.get(4));
                cityField.setText(row.get(5)); supPicker.setValue(row.get(1));
            }
        });

        btnAdd.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { err("Name required."); return; }
            boolean ok = SupplierDAO.addSupplier(new Supplier(name, contField.getText().trim(), phoneField.getText().trim(), emailField.getText().trim(), cityField.getText().trim()));
            if (ok) { ok("Supplier added!"); nameField.clear(); contField.clear(); phoneField.clear(); emailField.clear(); cityField.clear(); refresh.run(); }
            else err("Failed.");
        });

        btnUpd.setOnAction(e -> {
            String sel = supPicker.getValue();
            if (sel == null) { err("Select a supplier from the dropdown."); return; }
            try {
                int id = supIdMap.get(sel);
                Supplier ex = SupplierDAO.getSupplierById(id);
                if (ex == null) { err("Not found."); return; }
                SupplierDAO.updateSupplier(new Supplier(id,
                        nameField.getText().trim().isEmpty()  ? ex.getName()          : nameField.getText().trim(),
                        contField.getText().trim().isEmpty()  ? ex.getContactPerson() : contField.getText().trim(),
                        phoneField.getText().trim().isEmpty() ? ex.getPhone()         : phoneField.getText().trim(),
                        emailField.getText().trim().isEmpty() ? ex.getEmail()         : emailField.getText().trim(),
                        cityField.getText().trim().isEmpty()  ? ex.getCity()          : cityField.getText().trim()));
                ok("Supplier updated!"); nameField.clear(); contField.clear(); phoneField.clear(); emailField.clear(); cityField.clear(); supPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = supPicker.getValue();
            if (sel == null) { err("Select a supplier from the dropdown."); return; }
            if (!confirmDelete("supplier '" + sel + "'")) return;
            boolean ok = SupplierDAO.deleteSupplier(supIdMap.get(sel));
            if (ok) { ok("Supplier deleted!"); supPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked orders.");
        });

        btnRef.setOnAction(e -> { nameField.clear(); contField.clear(); phoneField.clear(); emailField.clear(); cityField.clear(); supPicker.setValue(null); refresh.run(); });
        return pane;
    }


    private VBox buildClientTab() {
        VBox pane = pane();
        sectionTitle(pane, "Clients");

        TextField nameField   = field("Client Name");
        TextField typeField   = field("Type: Pharmacy or Clinic");
        TextField phoneField  = field("Phone");
        TextField cityField   = field("City");
        TextField creditField = field("Credit Limit (NIS)");
        TextField passField   = field("Password (for new account)");

        ComboBox<String> cliPicker = new ComboBox<>();
        cliPicker.setPromptText("Select client to update/delete...");
        cliPicker.setStyle("-fx-font-size:13px;"); cliPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> cliIdMap = new java.util.LinkedHashMap<>();

        Label selLabel = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");
        pane.getChildren().addAll(
                row(nameField, typeField), row(phoneField, cityField, creditField),
                passField, new VBox(4, selLabel, cliPicker));

        Button btnAdd = btn("Add + Create Account", GREEN); Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED); Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","Name","Type","Phone","City","Credit Limit (NIS)"}, new int[]{55,180,90,120,100,120});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear(); cliPicker.getItems().clear(); cliIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Client ORDER BY ClientName");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("ClientID")), rs.getString("ClientName"),
                            rs.getString("ClientType"), rs.getString("Phone"), rs.getString("City"),
                            String.format("%.2f", rs.getDouble("CreditLimit"))));
                    cliPicker.getItems().add(rs.getString("ClientName"));
                    cliIdMap.put(rs.getString("ClientName"), rs.getInt("ClientID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                nameField.setText(row.get(1)); typeField.setText(row.get(2));
                phoneField.setText(row.get(3)); cityField.setText(row.get(4));
                creditField.setText(row.get(5)); cliPicker.setValue(row.get(1));
            }
        });

        btnAdd.setOnAction(e -> {
            try {
                String pass = passField.getText().trim();
                if (pass.isEmpty()) { err("Password required."); return; }
                boolean ok = ClientDAO.addClient(new Client(
                        nameField.getText().trim(), typeField.getText().trim(),
                        phoneField.getText().trim(), cityField.getText().trim(),
                        Double.parseDouble(creditField.getText().trim())));
                if (ok) {
                    int newId = getLastId("SELECT MAX(ClientID) FROM Client");
                    UserAccountDAO.addUser(pass, "Client", null, newId);
                    int uid = getLastId("SELECT MAX(UserID) FROM UserAccount");
                    ok("Client added! Login User ID: " + uid);
                    nameField.clear(); typeField.clear(); phoneField.clear();
                    cityField.clear(); creditField.clear(); passField.clear();
                    cliPicker.setValue(null); refresh.run();
                } else err("Failed. Type must be Pharmacy or Clinic.");
            } catch (Exception ex) { err("Invalid credit limit."); }
        });

        btnUpd.setOnAction(e -> {
            String sel = cliPicker.getValue();
            if (sel == null) { err("Select a client from the dropdown."); return; }
            try {
                int id = cliIdMap.get(sel);
                Client ex = ClientDAO.getClientById(id);
                if (ex == null) { err("Not found."); return; }
                ClientDAO.updateClient(new Client(id,
                        nameField.getText().trim().isEmpty()   ? ex.getName()        : nameField.getText().trim(),
                        typeField.getText().trim().isEmpty()   ? ex.getType()        : typeField.getText().trim(),
                        phoneField.getText().trim().isEmpty()  ? ex.getPhone()       : phoneField.getText().trim(),
                        cityField.getText().trim().isEmpty()   ? ex.getCity()        : cityField.getText().trim(),
                        creditField.getText().trim().isEmpty() ? ex.getCreditLimit() : Double.parseDouble(creditField.getText().trim())));
                ok("Client updated!");
                nameField.clear(); typeField.clear(); phoneField.clear();
                cityField.clear(); creditField.clear(); passField.clear();
                cliPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = cliPicker.getValue();
            if (sel == null) { err("Select a client from the dropdown."); return; }
            if (!confirmDelete("client '" + sel + "'")) return;
            boolean ok = ClientDAO.deleteClient(cliIdMap.get(sel));
            if (ok) { ok("Client deleted!"); cliPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked orders.");
        });

        btnRef.setOnAction(e -> {
            nameField.clear(); typeField.clear(); phoneField.clear();
            cityField.clear(); creditField.clear(); passField.clear();
            cliPicker.setValue(null); refresh.run();
        });
        return pane;
    }

    private VBox buildEmployeeTab() {
        VBox pane = pane();
        sectionTitle(pane, "Employees");

        TextField firstField = field("First Name"); TextField lastField = field("Last Name");
        TextField roleField  = field("Role");
        DatePicker hirePicker = new DatePicker();
        hirePicker.setPromptText("Hire Date"); hirePicker.setStyle("-fx-font-size:13px;"); hirePicker.setMaxWidth(Double.MAX_VALUE);
        TextField phoneField = field("Phone"); TextField salField = field("Salary");
        TextField passField  = field("Password (for new account)");

        // Warehouse dropdown (optional assignment)
        ComboBox<String> whBox = new ComboBox<>();
        whBox.setPromptText("Assigned warehouse (optional)...");
        whBox.setStyle("-fx-font-size:13px;"); whBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> whMap = new java.util.LinkedHashMap<>();

        ComboBox<String> empPicker = new ComboBox<>();
        empPicker.setPromptText("Select employee to update/delete...");
        empPicker.setStyle("-fx-font-size:13px;"); empPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> empIdMap = new java.util.LinkedHashMap<>();

        Label hireLabel = new Label("Hire Date:"); hireLabel.setStyle("-fx-font-weight:bold;");
        Label whLabel   = new Label("Warehouse:"); whLabel.setStyle("-fx-font-weight:bold;");
        Label selLabel  = new Label("Update/Delete:"); selLabel.setStyle("-fx-font-weight:bold;");
        pane.getChildren().addAll(
                row(firstField, lastField, roleField),
                row(new VBox(4, hireLabel, hirePicker), phoneField, salField),
                passField, new VBox(4, whLabel, whBox), new VBox(4, selLabel, empPicker));

        Button btnAdd = btn("Add + Create Account", GREEN); Button btnUpd = btn("Update Selected", MID);
        Button btnDel = btn("Delete Selected", RED); Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));

        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID","First Name","Last Name","Role","Hire Date","Phone","Salary","Warehouse"}, new int[]{55,100,100,140,95,120,90,140});
        pane.getChildren().add(table);

        Runnable refresh = () -> {
            table.getItems().clear(); empPicker.getItems().clear(); empIdMap.clear();
            whBox.getItems().clear(); whMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsWh = conn.createStatement().executeQuery("SELECT WarehouseID, WarehouseName FROM Warehouse ORDER BY WarehouseName");
                while (rsWh.next()) { whMap.put(rsWh.getString("WarehouseName"), rsWh.getInt("WarehouseID")); whBox.getItems().add(rsWh.getString("WarehouseName")); }
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT emp.*, w.WarehouseName FROM Employee emp " +
                                "LEFT JOIN Warehouse w ON emp.WarehouseID=w.WarehouseID ORDER BY emp.FirstName");
                while (rs.next()) {
                    String label = rs.getString("FirstName") + " " + rs.getString("LastName") + " (" + rs.getString("Role") + ")";
                    String whName = rs.getString("WarehouseName");
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("EmployeeID")), rs.getString("FirstName"), rs.getString("LastName"),
                            rs.getString("Role"), rs.getString("HireDate"), rs.getString("Phone"),
                            String.format("%.2f", rs.getDouble("Salary")), whName != null ? whName : "-"));
                    empPicker.getItems().add(label);
                    empIdMap.put(label, rs.getInt("EmployeeID"));
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };
        refresh.run();

        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                firstField.setText(row.get(1)); lastField.setText(row.get(2)); roleField.setText(row.get(3));
                try { hirePicker.setValue(LocalDate.parse(row.get(4))); } catch (Exception ex2) { hirePicker.setValue(null); }
                phoneField.setText(row.get(5)); salField.setText(row.get(6));
                whBox.setValue(row.get(7).equals("-") ? null : row.get(7));
                empPicker.setValue(row.get(1) + " " + row.get(2) + " (" + row.get(3) + ")");
            }
        });

        btnAdd.setOnAction(e -> {
            try {
                String pass = passField.getText().trim();
                if (pass.isEmpty()) { err("Password required."); return; }
                if (hirePicker.getValue() == null) { err("Select a hire date."); return; }
                boolean ok = EmployeeDAO.addEmployee(new Employee(
                        firstField.getText().trim(), lastField.getText().trim(), roleField.getText().trim(),
                        hirePicker.getValue().toString(), phoneField.getText().trim(), Double.parseDouble(salField.getText().trim()),
                        whBox.getValue() == null ? null : whMap.get(whBox.getValue())));
                if (ok) {
                    int newId = getLastId("SELECT MAX(EmployeeID) FROM Employee");
                    UserAccountDAO.addUser(pass, "Employee", newId, null);
                    int uid = getLastId("SELECT MAX(UserID) FROM UserAccount");
                    ok("Employee added! Login User ID: " + uid);
                    firstField.clear(); lastField.clear(); roleField.clear();
                    hirePicker.setValue(null); phoneField.clear(); salField.clear(); passField.clear();
                    whBox.setValue(null); empPicker.setValue(null); refresh.run();
                } else err("Failed.");
            } catch (Exception ex) { err("Invalid salary."); }
        });

        btnUpd.setOnAction(e -> {
            String sel = empPicker.getValue();
            if (sel == null) { err("Select an employee from the dropdown."); return; }
            try {
                int id = empIdMap.get(sel);
                Employee ex = EmployeeDAO.getEmployeeById(id);
                if (ex == null) { err("Not found."); return; }
                EmployeeDAO.updateEmployee(new Employee(id,
                        firstField.getText().trim().isEmpty() ? ex.getFirstName() : firstField.getText().trim(),
                        lastField.getText().trim().isEmpty()  ? ex.getLastName()  : lastField.getText().trim(),
                        roleField.getText().trim().isEmpty()  ? ex.getRole()      : roleField.getText().trim(),
                        hirePicker.getValue() == null         ? ex.getHireDate()  : hirePicker.getValue().toString(),
                        phoneField.getText().trim().isEmpty() ? ex.getPhone()     : phoneField.getText().trim(),
                        salField.getText().trim().isEmpty()   ? ex.getSalary()    : Double.parseDouble(salField.getText().trim()),
                        whBox.getValue() == null ? ex.getWarehouseID() : whMap.get(whBox.getValue())));
                ok("Employee updated!");
                firstField.clear(); lastField.clear(); roleField.clear();
                hirePicker.setValue(null); phoneField.clear(); salField.clear(); passField.clear();
                whBox.setValue(null); empPicker.setValue(null); refresh.run();
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnDel.setOnAction(e -> {
            String sel = empPicker.getValue();
            if (sel == null) { err("Select an employee from the dropdown."); return; }
            if (!confirmDelete("employee '" + sel + "'")) return;
            boolean ok = EmployeeDAO.deleteEmployee(empIdMap.get(sel));
            if (ok) { ok("Employee deleted!"); empPicker.setValue(null); refresh.run(); }
            else err("Cannot delete — has linked orders.");
        });

        btnRef.setOnAction(e -> { firstField.clear(); lastField.clear(); roleField.clear(); hirePicker.setValue(null); phoneField.clear(); salField.clear(); passField.clear(); whBox.setValue(null); empPicker.setValue(null); refresh.run(); });
        return pane;
    }

    private VBox buildSupplierProductTab() {
        VBox pane = pane();
        sectionTitle(pane, "Supplier - Product Links");

        ComboBox<String> supBox = new ComboBox<>();
        supBox.setPromptText("Select supplier...");
        supBox.setStyle("-fx-font-size:13px;"); supBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> supMap = new java.util.LinkedHashMap<>();

        ComboBox<String> prodBox = new ComboBox<>();
        prodBox.setPromptText("Select product...");
        prodBox.setStyle("-fx-font-size:13px;"); prodBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> prodMap = new java.util.LinkedHashMap<>();

        TextField costField = field("Unit Cost");

        Label supLabel = new Label("Supplier:"); supLabel.setStyle("-fx-font-weight:bold;");
        Label prodLabel = new Label("Product:"); prodLabel.setStyle("-fx-font-weight:bold;");
        HBox pickRow = new HBox(10, new VBox(4, supLabel, supBox), new VBox(4, prodLabel, prodBox));
        HBox.setHgrow(pickRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(pickRow.getChildren().get(1), Priority.ALWAYS);
        pane.getChildren().addAll(pickRow, costField);

        Button btnAdd = btn("Link", GREEN);
        Button btnUpd = btn("Update Cost", MID);
        Button btnDel = btn("Remove Link", RED);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnUpd, btnDel, btnRef));
        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"Supplier", "Product", "Unit Cost (NIS)"}, new int[]{200, 200, 120});
        pane.getChildren().add(table);
        Runnable refresh = () -> {
            table.getItems().clear();
            supBox.getItems().clear(); supMap.clear();
            prodBox.getItems().clear(); prodMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsSup = conn.createStatement().executeQuery("SELECT SupplierID, SupplierName FROM Supplier ORDER BY SupplierName");
                while (rsSup.next()) { supMap.put(rsSup.getString("SupplierName"), rsSup.getInt("SupplierID")); supBox.getItems().add(rsSup.getString("SupplierName")); }
                ResultSet rsProd = conn.createStatement().executeQuery("SELECT ProductID, ProductName FROM Product ORDER BY ProductName");
                while (rsProd.next()) { prodMap.put(rsProd.getString("ProductName"), rsProd.getInt("ProductID")); prodBox.getItems().add(rsProd.getString("ProductName")); }
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT s.SupplierName, p.ProductName, sp.UnitCost, sp.SupplierID, sp.ProductID " +
                                "FROM SupplierProduct sp " +
                                "JOIN Supplier s ON sp.SupplierID=s.SupplierID " +
                                "JOIN Product p ON sp.ProductID=p.ProductID ORDER BY s.SupplierName");
                while (rs.next()) {
                    table.getItems().add(FXCollections.observableArrayList(
                            rs.getString("SupplierName"), rs.getString("ProductName"),
                            String.format("%.2f", rs.getDouble("UnitCost")),
                            String.valueOf(rs.getInt("SupplierID")), String.valueOf(rs.getInt("ProductID"))));
                }
                conn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        refresh.run();
        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null && row.size() >= 5) {
                supBox.setValue(row.get(0));
                prodBox.setValue(row.get(1));
                costField.setText(row.get(2));
            }
        });
        btnAdd.setOnAction(e -> {
            if (supBox.getValue() == null) { err("Select a supplier."); return; }
            if (prodBox.getValue() == null) { err("Select a product."); return; }
            try {
                boolean ok = SupplierProductDAO.addSupplierProduct(new SupplierProduct(supMap.get(supBox.getValue()),
                        prodMap.get(prodBox.getValue()), Double.parseDouble(costField.getText().trim())));
                if (ok) {
                    ok("Link created!");
                    supBox.setValue(null);
                    prodBox.setValue(null);
                    costField.clear();
                    refresh.run();
                }
                else {
                    err("Failed or already exists.");
                }
            }
            catch (Exception ex) {
                err("Invalid values.");
            }
        });
        btnUpd.setOnAction(e -> {
            if (supBox.getValue() == null) { err("Select a supplier."); return; }
            if (prodBox.getValue() == null) { err("Select a product."); return; }
            try {
                boolean ok = SupplierProductDAO.updateSupplierProduct(new SupplierProduct(supMap.get(supBox.getValue()),
                        prodMap.get(prodBox.getValue()), Double.parseDouble(costField.getText().trim())));
                if (ok) {
                    ok("Cost updated!");
                    supBox.setValue(null);
                    prodBox.setValue(null);
                    costField.clear();
                    refresh.run();
                }
                else {
                    err("Link not found.");
                }
            }
            catch (Exception ex) {
                err("Invalid values.");
            }
        });
        btnDel.setOnAction(e -> {
            if (supBox.getValue() == null) { err("Select a supplier."); return; }
            if (prodBox.getValue() == null) { err("Select a product."); return; }
            if (!confirmDelete("supplier-product link")) {
                return;
            }
            try {
                boolean ok = SupplierProductDAO.deleteSupplierProduct(supMap.get(supBox.getValue()), prodMap.get(prodBox.getValue()));
                if (ok) {
                    ok("Link removed!");
                    supBox.setValue(null);
                    prodBox.setValue(null);
                    costField.clear();
                    refresh.run();
                }
                else {
                    err("Not found.");
                }
            }
            catch (Exception ex) {
                err("Invalid values.");
            }
        });
        btnRef.setOnAction(e -> {
            supBox.setValue(null);
            prodBox.setValue(null);
            costField.clear();
            refresh.run();
        });
        return pane;
    }

    private static class POCatalogRow {
        final int productId; final String productName; final String category; final double unitCost;
        POCatalogRow(int productId, String productName, String category, double unitCost) {
            this.productId = productId; this.productName = productName; this.category = category; this.unitCost = unitCost;
        }
    }

    private static class POCartLine {
        final int productId; final String productName; final double unitCost; final int qty;
        POCartLine(int productId, String productName, double unitCost, int qty) {
            this.productId = productId; this.productName = productName; this.unitCost = unitCost; this.qty = qty;
        }
        double getSubtotal() { return qty * unitCost; }
    }

    private VBox buildPOProductCard(POCatalogRow row, ObservableList<POCartLine> cart, Runnable onChange) {
        VBox card = new VBox(8);
        card.setPrefWidth(200);
        card.setPadding(new Insets(14));
        card.setStyle(
                "-fx-background-color:white; -fx-background-radius:12;" +
                        "-fx-border-color:#E2E8F0; -fx-border-radius:12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        Label catLbl = new Label(row.category.toUpperCase());
        catLbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        catLbl.setTextFill(Color.web(MID));

        Label nameLbl = new Label(row.productName);
        nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameLbl.setTextFill(Color.web(DARK));
        nameLbl.setWrapText(true);

        Label costLbl = new Label(String.format("%.2f NIS", row.unitCost));
        costLbl.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        costLbl.setTextFill(Color.web(GREEN));

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        HBox controls = new HBox(8);
        controls.setAlignment(Pos.CENTER_LEFT);

        boolean alreadyInCart = cart.stream().anyMatch(ci -> ci.productId == row.productId);
        if (alreadyInCart) {
            Label inCartLbl = new Label("In Cart");
            inCartLbl.setStyle("-fx-background-color:#E8F2FF; -fx-text-fill:" + DARK + "; -fx-font-weight:bold;" +
                    " -fx-font-size:12px; -fx-padding:6 14; -fx-background-radius:6;");
            controls.getChildren().add(inCartLbl);
        } else {
            Spinner<Integer> qtySpinner = new Spinner<>(1, 9999, 1);
            qtySpinner.setPrefWidth(72);
            Button addBtn = new Button("+ Add");
            addBtn.setStyle("-fx-background-color:" + GREEN + "; -fx-text-fill:white; -fx-background-radius:6;" +
                    " -fx-cursor:hand; -fx-font-size:12px; -fx-font-weight:bold; -fx-padding:6 14;");
            addBtn.setOnAction(e -> {
                cart.add(new POCartLine(row.productId, row.productName, row.unitCost, qtySpinner.getValue()));
                onChange.run();
            });
            controls.getChildren().addAll(qtySpinner, addBtn);
        }

        card.getChildren().addAll(catLbl, nameLbl, costLbl, grow, controls);
        return card;
    }

    private VBox buildPurchaseTab() {
        VBox pane = pane();
        sectionTitle(pane, "Purchase Orders - Order from Suppliers");

        Label hint = new Label(
                "Pick a supplier, browse the products they supply, add quantities to your cart, " +
                        "then submit the whole order at once. Receive stock from an existing order below."
        );
        hint.setStyle("-fx-text-fill:#64748B; -fx-font-size:12px; -fx-font-style:italic;");
        hint.setWrapText(true);
        pane.getChildren().add(hint);

        // ── Order Builder ──────────────────────────────────────
        Label builderTitle = sub("Build a New Purchase Order");
        pane.getChildren().add(builderTitle);

        Label supLabel = new Label("Supplier:"); supLabel.setStyle("-fx-font-weight:bold; -fx-font-size:13px;");
        ComboBox<String> supBox = new ComboBox<>();
        supBox.setPromptText("Choose a supplier to browse their products...");
        supBox.setStyle("-fx-font-size:13px;"); supBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> supplierMap = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery("SELECT SupplierID, SupplierName FROM Supplier ORDER BY SupplierName");
            while (rs.next()) { supplierMap.put(rs.getString("SupplierName"), rs.getInt("SupplierID")); supBox.getItems().add(rs.getString("SupplierName")); }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        Label empLabel = new Label("Handled By:"); empLabel.setStyle("-fx-font-weight:bold; -fx-font-size:13px;");
        ComboBox<String> empBox = new ComboBox<>();
        empBox.setPromptText("Choose employee...");
        empBox.setStyle("-fx-font-size:13px;"); empBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> empMap = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT EmployeeID, CONCAT(FirstName,' ',LastName,' (',Role,')') AS Name FROM Employee ORDER BY FirstName");
            while (rs.next()) { empMap.put(rs.getString("Name"), rs.getInt("EmployeeID")); empBox.getItems().add(rs.getString("Name")); }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        Label dateLabel = new Label("Order Date:"); dateLabel.setStyle("-fx-font-weight:bold;");
        DatePicker poDatePicker = new DatePicker(LocalDate.now());
        poDatePicker.setStyle("-fx-font-size:13px;"); poDatePicker.setMaxWidth(Double.MAX_VALUE);
        Label delLabel = new Label("Expected Delivery:"); delLabel.setStyle("-fx-font-weight:bold;");
        DatePicker poDelPicker = new DatePicker(LocalDate.now().plusDays(7));
        poDelPicker.setStyle("-fx-font-size:13px;"); poDelPicker.setMaxWidth(Double.MAX_VALUE);

        HBox headerRow1 = new HBox(10, new VBox(4, supLabel, supBox), new VBox(4, empLabel, empBox));
        HBox.setHgrow(headerRow1.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(headerRow1.getChildren().get(1), Priority.ALWAYS);
        HBox headerRow2 = new HBox(10, new VBox(4, dateLabel, poDatePicker), new VBox(4, delLabel, poDelPicker));
        HBox.setHgrow(headerRow2.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(headerRow2.getChildren().get(1), Priority.ALWAYS);
        pane.getChildren().addAll(headerRow1, headerRow2);

        TextField searchField = new TextField();
        searchField.setPromptText("Search this supplier's products...");
        searchField.setStyle("-fx-background-radius:20; -fx-border-radius:20; -fx-border-color:#CBD5E1;" +
                " -fx-padding:8 14; -fx-font-size:13px;");
        pane.getChildren().add(searchField);

        FlowPane productGrid = new FlowPane();
        productGrid.setHgap(14); productGrid.setVgap(14);
        productGrid.setPadding(new Insets(10, 0, 10, 0));
        pane.getChildren().add(productGrid);

        ObservableList<POCartLine> cart = FXCollections.observableArrayList();
        ObservableList<POCatalogRow> catalog = FXCollections.observableArrayList();
        Runnable[] populateGridRef = new Runnable[1];
        Runnable[] updateCartTotalRef = new Runnable[1];

        pane.getChildren().add(new Separator());
        Label cartTitle = sub("Cart");
        pane.getChildren().add(cartTitle);

        TableView<POCartLine> cartTable = new TableView<>(cart);
        cartTable.setPrefHeight(180);
        cartTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        cartTable.setPlaceholder(new Label("Cart is empty — add products above."));

        TableColumn<POCartLine, String> ccName = new TableColumn<>("Product");
        ccName.setPrefWidth(220);
        ccName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().productName));
        TableColumn<POCartLine, String> ccQty = new TableColumn<>("Qty");
        ccQty.setPrefWidth(70);
        ccQty.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().qty)));
        TableColumn<POCartLine, String> ccCost = new TableColumn<>("Unit Cost (NIS)");
        ccCost.setPrefWidth(110);
        ccCost.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.2f", d.getValue().unitCost)));
        TableColumn<POCartLine, String> ccSub = new TableColumn<>("Subtotal (NIS)");
        ccSub.setPrefWidth(110);
        ccSub.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.2f", d.getValue().getSubtotal())));
        TableColumn<POCartLine, Void> ccRemove = new TableColumn<>("Remove");
        ccRemove.setPrefWidth(90);
        ccRemove.setCellFactory(c -> new TableCell<>() {
            final Button rmBtn = new Button("Remove");
            {
                rmBtn.setStyle("-fx-background-color:" + RED + "; -fx-text-fill:white; -fx-background-radius:6;" +
                        " -fx-cursor:hand; -fx-font-size:11px; -fx-padding:5 10;");
                rmBtn.setOnAction(e -> {
                    cart.remove(getTableView().getItems().get(getIndex()));
                    updateCartTotalRef[0].run();
                    populateGridRef[0].run();
                });
            }
            @Override protected void updateItem(Void v, boolean empty) { super.updateItem(v, empty); setGraphic(empty ? null : rmBtn); }
        });
        cartTable.getColumns().addAll(ccName, ccQty, ccCost, ccSub, ccRemove);
        pane.getChildren().add(cartTable);

        Label cartTotalLabel = new Label("Total: 0.00 NIS");
        cartTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cartTotalLabel.setTextFill(Color.web(DARK));
        HBox totalRow = new HBox(cartTotalLabel);
        totalRow.setAlignment(Pos.CENTER_RIGHT);
        pane.getChildren().add(totalRow);

        Button btnCreatePO = btn("Create Purchase Order", GREEN);
        Label poCreatedLabel = new Label("");
        poCreatedLabel.setStyle("-fx-text-fill:#16A34A; -fx-font-weight:bold; -fx-font-size:13px;");
        pane.getChildren().addAll(new HBox(10, btnCreatePO), poCreatedLabel);
        pane.getChildren().add(new Separator());

        // ── Existing Purchase Orders ─────────────────────────
        Label step3 = sub("Existing Purchase Orders");
        Button btnRefPO = btn("Refresh", DARK);
        pane.getChildren().addAll(step3, btnRefPO);

        TableView<ObservableList<String>> poTable = table();
        addCols(poTable, new String[]{"PO#","Supplier","Handled By","Date","Exp. Delivery","Status","Total (NIS)"}, new int[]{55,150,140,90,100,90,90});

        TableColumn<ObservableList<String>, Void> actCol = new TableColumn<>("Action");
        actCol.setPrefWidth(130);
        actCol.setCellFactory(c -> new TableCell<>() {
            final Button rcvBtn = new Button("Receive Stock");
            {
                rcvBtn.setStyle("-fx-background-color:#16A34A; -fx-text-fill:white; -fx-background-radius:6; -fx-cursor:hand; -fx-font-size:11px; -fx-padding:5 10;");
                rcvBtn.setOnAction(e -> {
                    ObservableList<String> row = getTableView().getItems().get(getIndex());
                    int poNum = Integer.parseInt(row.get(0));
                    showReceivePODialog(poNum, () -> refreshPoTable(poTable));
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); return; }
                String status = getTableView().getItems().get(getIndex()).get(5);
                setGraphic(status.equals("Pending") ? rcvBtn : null);
            }
        });
        poTable.getColumns().add(actCol);
        pane.getChildren().add(poTable);

        // ── Wire up actions ───────────────────────────────────
        populateGridRef[0] = () -> {
            productGrid.getChildren().clear();
            if (supBox.getValue() == null) {
                Label empty = new Label("Select a supplier above to browse their products.");
                empty.setStyle("-fx-text-fill:#94A3B8; -fx-font-size:13px; -fx-padding:20;");
                productGrid.getChildren().add(empty);
                return;
            }
            String kw = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
            java.util.List<POCatalogRow> shown = new java.util.ArrayList<>();
            for (POCatalogRow row : catalog) {
                if (kw.isEmpty() || row.productName.toLowerCase().contains(kw)) shown.add(row);
            }
            if (shown.isEmpty()) {
                Label empty = new Label(catalog.isEmpty() ? "This supplier has no linked products yet — add some in Sup-Product tab." : "No products match your search.");
                empty.setStyle("-fx-text-fill:#94A3B8; -fx-font-size:13px; -fx-padding:20;");
                empty.setWrapText(true);
                productGrid.getChildren().add(empty);
                return;
            }
            for (POCatalogRow row : shown) {
                productGrid.getChildren().add(buildPOProductCard(row, cart, () -> {
                    updateCartTotalRef[0].run();
                    populateGridRef[0].run();
                }));
            }
        };

        updateCartTotalRef[0] = () -> {
            double total = cart.stream().mapToDouble(POCartLine::getSubtotal).sum();
            cartTotalLabel.setText("Total: " + String.format("%.2f", total) + " NIS");
        };

        supBox.setOnAction(e -> {
            String sel = supBox.getValue();
            cart.clear();
            catalog.clear();
            if (sel != null) {
                int supId = supplierMap.get(sel);
                try {
                    Connection conn = DBConnection.connect();
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT p.ProductID, p.ProductName, c.Name AS Category, sp.UnitCost " +
                                    "FROM SupplierProduct sp JOIN Product p ON sp.ProductID=p.ProductID " +
                                    "JOIN Category c ON p.CategoryID=c.CategoryID WHERE sp.SupplierID=? ORDER BY c.Name, p.ProductName");
                    ps.setInt(1, supId);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) catalog.add(new POCatalogRow(
                            rs.getInt("ProductID"), rs.getString("ProductName"), rs.getString("Category"), rs.getDouble("UnitCost")));
                    conn.close();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
            updateCartTotalRef[0].run();
            populateGridRef[0].run();
        });
        searchField.textProperty().addListener((obs, ov, nv) -> populateGridRef[0].run());

        btnCreatePO.setOnAction(e -> {
            if (supBox.getValue() == null) { err("Select a supplier."); return; }
            if (empBox.getValue() == null) { err("Select an employee."); return; }
            if (poDatePicker.getValue() == null || poDelPicker.getValue() == null) { err("Select order and delivery dates."); return; }
            if (cart.isEmpty()) { err("Cart is empty — add products first."); return; }
            try {
                int supId = supplierMap.get(supBox.getValue());
                int empId = empMap.get(empBox.getValue());
                boolean ok = PurchaseOrderDAO.addPurchaseOrder(new PurchaseOrder(
                        supId, empId, poDatePicker.getValue().toString(), poDelPicker.getValue().toString(), "Pending", 0.0));
                if (!ok) { err("Failed to create PO."); return; }
                int newPO = getLastId("SELECT MAX(PONumber) FROM PurchaseOrder");
                for (POCartLine line : cart) {
                    PurchaseOrderDAO.addPurchaseOrderItem(new PurchaseOrderItem(newPO, line.productId, line.qty, line.unitCost, 0));
                }
                updatePOTotal(newPO);
                poCreatedLabel.setText("Purchase Order #" + newPO + " created with " + cart.size() + " item(s)!");
                ok("Purchase Order #" + newPO + " created!");
                cart.clear();
                updateCartTotalRef[0].run();
                populateGridRef[0].run();
                refreshPoTable(poTable);
            } catch (Exception ex) { err("Something went wrong creating the order."); }
        });

        btnRefPO.setOnAction(e -> refreshPoTable(poTable));
        populateGridRef[0].run();
        refreshPoTable(poTable);
        return pane;
    }

    private void refreshPoTable(TableView<ObservableList<String>> poTable) {
        poTable.getItems().clear();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT po.PONumber, s.SupplierName, CONCAT(e.FirstName,' ',e.LastName) AS Emp, " +
                            "po.OrderDate, po.ExpDeliveryDate, po.Status, po.TotalAmount " +
                            "FROM PurchaseOrder po JOIN Supplier s ON po.SupplierID=s.SupplierID " +
                            "JOIN Employee e ON po.EmployeeID=e.EmployeeID ORDER BY po.OrderDate DESC");
            while (rs.next()) poTable.getItems().add(FXCollections.observableArrayList(
                    String.valueOf(rs.getInt("PONumber")), rs.getString("SupplierName"), rs.getString("Emp"),
                    rs.getString("OrderDate"),
                    rs.getString("ExpDeliveryDate") != null ? rs.getString("ExpDeliveryDate") : "-",
                    rs.getString("Status"), String.format("%.2f", rs.getDouble("TotalAmount"))));
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updatePOTotal(int poNum) {
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE PurchaseOrder SET TotalAmount = " +
                            "(SELECT COALESCE(SUM(QtyOrdered * UnitCost),0) FROM PurchaseOrderItem WHERE PONumber=?) " +
                            "WHERE PONumber=?");
            ps.setInt(1, poNum); ps.setInt(2, poNum);
            ps.executeUpdate(); conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showReceivePODialog(int poNum, Runnable onDone) {
        java.util.List<int[]> meta = new java.util.ArrayList<>();
        java.util.List<TextField> batchFields = new java.util.ArrayList<>();
        java.util.List<TextField> qtyFields   = new java.util.ArrayList<>();

        VBox content = new VBox(12);
        content.setPadding(new Insets(16));
        content.setPrefWidth(480);

        Label topNote = new Label(
                "For each item below, enter the Batch ID it goes into and how many units arrived."
        );
        topNote.setStyle("-fx-background-color:#E8F2FF; -fx-padding:10; -fx-background-radius:6; -fx-font-size:12px;");
        topNote.setWrapText(true);
        content.getChildren().add(topNote);

        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT poi.ProductID, p.ProductName, poi.QtyOrdered, poi.QtyReceived " +
                            "FROM PurchaseOrderItem poi JOIN Product p ON poi.ProductID=p.ProductID WHERE poi.PONumber=?");
            ps.setInt(1, poNum);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ordered   = rs.getInt("QtyOrdered");
                int received  = rs.getInt("QtyReceived");
                int remaining = Math.max(ordered - received, 0);
                Label itemLbl = new Label(rs.getString("ProductName") +
                        "  (ordered: " + ordered + "  |  received: " + received + "  |  remaining: " + remaining + ")");
                itemLbl.setStyle("-fx-font-weight:bold; -fx-text-fill:#1B3A6B;");
                TextField bField = new TextField();
                bField.setPromptText("Batch ID");
                bField.setPrefWidth(90);
                bField.setStyle("-fx-background-radius:6; -fx-border-radius:6; -fx-border-color:#CBD5E1; -fx-padding:6 10;");
                TextField qField = new TextField(remaining > 0 ? String.valueOf(remaining) : "");
                qField.setPromptText("Qty arrived");
                qField.setPrefWidth(90);
                qField.setStyle("-fx-background-radius:6; -fx-border-radius:6; -fx-border-color:#CBD5E1; -fx-padding:6 10;");
                HBox row = new HBox(10, new Label("Batch:"), bField, new Label("Qty:"), qField);
                row.setAlignment(Pos.CENTER_LEFT);
                content.getChildren().addAll(itemLbl, row, new Separator());
                meta.add(new int[]{rs.getInt("ProductID"), ordered, received});
                batchFields.add(bField);
                qtyFields.add(qField);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        if (meta.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No items found on PO #" + poNum + ". Add items first.", ButtonType.OK).showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Receive Stock - PO #" + poNum);
        dialog.setHeaderText("Log incoming stock for Purchase Order #" + poNum);
        ScrollPane scroller = new ScrollPane(content);
        scroller.setFitToWidth(true);
        scroller.setPrefHeight(360);
        dialog.getDialogPane().setContent(scroller);
        dialog.getDialogPane().setMinWidth(520);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            boolean any = false;
            for (int i = 0; i < meta.size(); i++) {
                String bt_str = batchFields.get(i).getText().trim();
                String qt_str = qtyFields.get(i).getText().trim();
                if (bt_str.isEmpty() || qt_str.isEmpty()) continue;
                try {
                    int batchId = Integer.parseInt(bt_str);
                    int qty     = Integer.parseInt(qt_str);
                    if (qty <= 0) continue;
                    // Verify batch exists and belongs to this product
                    Connection conn = DBConnection.connect();
                    PreparedStatement chk = conn.prepareStatement("SELECT 1 FROM Batch WHERE BatchID=? AND ProductID=?");
                    chk.setInt(1, batchId); chk.setInt(2, meta.get(i)[0]);
                    if (!chk.executeQuery().next()) { conn.close(); ok("Batch #" + batchId + " skipped (wrong product)."); continue; }
                    // Update batch stock
                    PreparedStatement upd = conn.prepareStatement("UPDATE Batch SET QtyInStock=QtyInStock+? WHERE BatchID=?");
                    upd.setInt(1, qty); upd.setInt(2, batchId); upd.executeUpdate();
                    // Update qty received on PO item
                    PreparedStatement updPOI = conn.prepareStatement("UPDATE PurchaseOrderItem SET QtyReceived=QtyReceived+? WHERE PONumber=? AND ProductID=?");
                    updPOI.setInt(1, qty); updPOI.setInt(2, poNum); updPOI.setInt(3, meta.get(i)[0]); updPOI.executeUpdate();
                    conn.close();
                    any = true;
                } catch (Exception ex) { ex.printStackTrace(); }
            }
            // Check if fully received -> mark Delivered
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement("SELECT SUM(QtyOrdered) AS O, SUM(QtyReceived) AS R FROM PurchaseOrderItem WHERE PONumber=?");
                ps.setInt(1, poNum);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt("O") > 0 && rs.getInt("R") >= rs.getInt("O")) {
                    PreparedStatement upd = conn.prepareStatement("UPDATE PurchaseOrder SET Status='Delivered' WHERE PONumber=?");
                    upd.setInt(1, poNum); upd.executeUpdate();
                    ok("PO #" + poNum + " fully received - marked Delivered!");
                } else if (any) {
                    ok("Partial stock received for PO #" + poNum + ".");
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
            onDone.run();
        });
    }

    private VBox buildSaleTab() {
        VBox pane = pane();
        sectionTitle(pane, "Sale Orders - Manage Client Orders");

        Label hint = new Label(
                "View and manage all client orders. Approve pending orders, mark them delivered, and record payments."
        );
        hint.setStyle("-fx-text-fill:#64748B; -fx-font-size:12px; -fx-font-style:italic;");
        hint.setWrapText(true);
        pane.getChildren().add(hint);

        // ── All Orders Table with Actions ─────────────────────
        Label ordersLabel = sub("All Sale Orders");
        Button btnRefresh = btn("Refresh", DARK);
        pane.getChildren().addAll(ordersLabel, btnRefresh);

        TableView<ObservableList<String>> soTable = table();
        soTable.setPrefHeight(300);
        addCols(soTable, new String[]{"SO#","Client","Handled By","Date","Delivery","Status","Payment","Total (NIS)"}, new int[]{55,140,130,90,90,90,80,90});

        soTable.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(ObservableList<String> row, boolean empty) {
                super.updateItem(row, empty);
                if (row == null || empty) { setStyle(""); return; }
                String st = row.get(5);
                setStyle(st.equals("Pending")  ? "-fx-background-color:#FEF9C3;" :
                        st.equals("Approved") ? "-fx-background-color:#DCFCE7;" : "");
            }
        });

        // Colour payment column
        TableColumn<ObservableList<String>, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(220);
        actionCol.setCellFactory(c -> new TableCell<>() {
            final Button approveBtn = new Button("Approve + Record Payment");
            final Button deliverBtn = new Button("Mark Delivered");
            final Button payBtn     = new Button("Record Payment");
            {
                approveBtn.setStyle("-fx-background-color:#16A34A; -fx-text-fill:white; -fx-background-radius:6; -fx-cursor:hand; -fx-font-size:11px; -fx-padding:5 8;");
                deliverBtn.setStyle("-fx-background-color:#2E6DA4; -fx-text-fill:white; -fx-background-radius:6; -fx-cursor:hand; -fx-font-size:11px; -fx-padding:5 8;");
                payBtn.setStyle("-fx-background-color:#D97706; -fx-text-fill:white; -fx-background-radius:6; -fx-cursor:hand; -fx-font-size:11px; -fx-padding:5 8;");

                approveBtn.setOnAction(e -> {
                    ObservableList<String> row = getTableView().getItems().get(getIndex());
                    showApproveDialog(Integer.parseInt(row.get(0)), row.get(1), Double.parseDouble(row.get(7)), () -> refreshSoTable(soTable));
                });
                deliverBtn.setOnAction(e -> {
                    ObservableList<String> row = getTableView().getItems().get(getIndex());
                    int soId = Integer.parseInt(row.get(0));
                    try {
                        Connection conn = DBConnection.connect();
                        PreparedStatement ps = conn.prepareStatement("UPDATE SaleOrder SET Status='Delivered' WHERE SaleOrderID=?");
                        ps.setInt(1, soId); ps.executeUpdate(); conn.close();
                        ok("Order #" + soId + " marked Delivered!");
                        refreshSoTable(soTable);
                    } catch (Exception ex) { err("Failed."); }
                });
                payBtn.setOnAction(e -> {
                    ObservableList<String> row = getTableView().getItems().get(getIndex());
                    showPaymentDialog(Integer.parseInt(row.get(0)), row.get(1), Double.parseDouble(row.get(7)), () -> refreshSoTable(soTable));
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); return; }
                ObservableList<String> row = getTableView().getItems().get(getIndex());
                String status  = row.get(5);
                String payment = row.get(6);
                VBox btns = new VBox(4);
                if (status.equals("Pending"))  btns.getChildren().add(approveBtn);
                if (status.equals("Approved")) btns.getChildren().add(deliverBtn);
                if (!payment.equals("Paid") && !status.equals("Pending")) btns.getChildren().add(payBtn);
                setGraphic(btns.getChildren().isEmpty() ? null : btns);
            }
        });
        soTable.getColumns().add(actionCol);
        pane.getChildren().add(soTable);
        pane.getChildren().add(new Separator());

        // ── Create Manual Order ───────────────────────────────
        Label createLabel = sub("Create Sale Order Manually");
        Label createHint  = new Label("Use this if a client places an order by phone/in-person.");
        createHint.setStyle("-fx-text-fill:#64748B; -fx-font-size:12px; -fx-font-style:italic;");
        pane.getChildren().addAll(createLabel, createHint);

        // Client dropdown
        ComboBox<String> clientBox = new ComboBox<>();
        clientBox.setPromptText("Select client...");
        clientBox.setStyle("-fx-font-size:13px;");
        clientBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> clientMap = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery("SELECT ClientID, ClientName FROM Client ORDER BY ClientName");
            while (rs.next()) { clientMap.put(rs.getString("ClientName"), rs.getInt("ClientID")); clientBox.getItems().add(rs.getString("ClientName")); }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        // Employee dropdown
        ComboBox<String> soEmpBox = new ComboBox<>();
        soEmpBox.setPromptText("Handled by...");
        soEmpBox.setStyle("-fx-font-size:13px;");
        soEmpBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> soEmpMap = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT EmployeeID, CONCAT(FirstName,' ',LastName,' (',Role,')') AS Name FROM Employee ORDER BY FirstName");
            while (rs.next()) { soEmpMap.put(rs.getString("Name"), rs.getInt("EmployeeID")); soEmpBox.getItems().add(rs.getString("Name")); }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        DatePicker soDatePicker = new DatePicker(LocalDate.now());
        soDatePicker.setStyle("-fx-font-size:13px;"); soDatePicker.setMaxWidth(Double.MAX_VALUE);
        Label soDateLabel = new Label("Order Date:"); soDateLabel.setStyle("-fx-font-weight:bold;");

        HBox soRow1 = new HBox(10,
                new VBox(4, new Label("Client:") {{ setStyle("-fx-font-weight:bold;"); }}, clientBox),
                new VBox(4, new Label("Employee:") {{ setStyle("-fx-font-weight:bold;"); }}, soEmpBox));
        HBox.setHgrow(soRow1.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(soRow1.getChildren().get(1), Priority.ALWAYS);

        Button btnCreateSO  = btn("Create Sale Order", GREEN);
        Label  soCreatedLbl = new Label("");
        soCreatedLbl.setStyle("-fx-text-fill:#16A34A; -fx-font-weight:bold;");
        pane.getChildren().addAll(soRow1, new VBox(4, soDateLabel, soDatePicker), new HBox(10, btnCreateSO), soCreatedLbl);
        pane.getChildren().add(new Separator());

        // ── Add Items to SO ───────────────────────────────────
        Label itemLabel = sub("Add Items to Sale Order");
        pane.getChildren().add(itemLabel);

        ComboBox<String> soItemBox = new ComboBox<>();
        soItemBox.setPromptText("Select Sale Order (from above)...");
        soItemBox.setStyle("-fx-font-size:13px;");
        soItemBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> soItemMap = new java.util.LinkedHashMap<>();
        Runnable refreshSoItemBox = () -> {
            String prevSel = soItemBox.getValue();
            soItemBox.getItems().clear(); soItemMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT so.SaleOrderID, cl.ClientName, so.Status FROM SaleOrder so " +
                                "JOIN Client cl ON so.ClientID=cl.ClientID ORDER BY so.SaleOrderID DESC");
                while (rs.next()) {
                    String label = "SO #" + rs.getInt("SaleOrderID") + " - " + rs.getString("ClientName") + " (" + rs.getString("Status") + ")";
                    soItemMap.put(label, rs.getInt("SaleOrderID"));
                    soItemBox.getItems().add(label);
                }
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
            if (prevSel != null && soItemBox.getItems().contains(prevSel)) soItemBox.setValue(prevSel);
        };
        refreshSoItemBox.run();

        // Batch/product picker
        ComboBox<String> batchBox = new ComboBox<>();
        batchBox.setPromptText("Select product/batch...");
        batchBox.setStyle("-fx-font-size:13px;");
        batchBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, int[]> batchMap = new java.util.LinkedHashMap<>(); // name -> [batchID, productID]
        java.util.Map<String, Double> batchPriceMap = new java.util.LinkedHashMap<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT b.BatchID, p.ProductName, b.QtyInStock, p.UnitPrice, w.WarehouseName " +
                            "FROM Batch b JOIN Product p ON b.ProductID=p.ProductID JOIN Warehouse w ON b.WarehouseID=w.WarehouseID " +
                            "WHERE b.QtyInStock > 0 ORDER BY p.ProductName");
            while (rs.next()) {
                String label = rs.getString("ProductName") + " [Batch #" + rs.getInt("BatchID") + ", " + rs.getString("WarehouseName") + ", qty=" + rs.getInt("QtyInStock") + "]";
                batchMap.put(label, new int[]{rs.getInt("BatchID")});
                batchPriceMap.put(label, rs.getDouble("UnitPrice"));
                batchBox.getItems().add(label);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        TextField soQtyField   = field("Quantity");
        TextField soPriceField = field("Unit Price (NIS)");
        TextField soDiscField  = field("Discount % (0 if none)");
        soDiscField.setText("0");

        // Auto-fill price
        batchBox.setOnAction(e -> {
            String sel = batchBox.getValue();
            if (sel != null && batchPriceMap.containsKey(sel))
                soPriceField.setText(String.format("%.2f", batchPriceMap.get(sel)));
        });

        HBox batchRow = new HBox(10,
                new VBox(4, new Label("Sale Order:") {{ setStyle("-fx-font-weight:bold;"); }}, soItemBox),
                new VBox(4, new Label("Product/Batch:") {{ setStyle("-fx-font-weight:bold;"); }}, batchBox));
        HBox.setHgrow(batchRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(batchRow.getChildren().get(1), Priority.ALWAYS);

        Button btnAddSOItem = btn("Add Item", GREEN);
        TableView<ObservableList<String>> soItemTable = table();
        addCols(soItemTable, new String[]{"SO#","Batch#","Product","Qty","Price (NIS)","Disc%"}, new int[]{55,60,180,60,90,60});

        pane.getChildren().addAll(
                batchRow,
                row(soQtyField, soPriceField, soDiscField),
                new HBox(10, btnAddSOItem),
                soItemTable
        );

        // ── Wire up ───────────────────────────────────────────
        Runnable refreshSOItems = () -> {
            soItemTable.getItems().clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT soi.SaleOrderID, soi.BatchID, p.ProductName, soi.QtyOrdered, soi.UnitPrice, soi.Discount " +
                                "FROM SaleOrderItem soi JOIN Batch b ON soi.BatchID=b.BatchID JOIN Product p ON b.ProductID=p.ProductID " +
                                "ORDER BY soi.SaleOrderID DESC LIMIT 50");
                while (rs.next()) soItemTable.getItems().add(FXCollections.observableArrayList(
                        String.valueOf(rs.getInt("SaleOrderID")), String.valueOf(rs.getInt("BatchID")),
                        rs.getString("ProductName"), String.valueOf(rs.getInt("QtyOrdered")),
                        String.format("%.2f", rs.getDouble("UnitPrice")), String.format("%.1f", rs.getDouble("Discount"))));
                conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        };

        btnCreateSO.setOnAction(e -> {
            String clientName = clientBox.getValue();
            String empName    = soEmpBox.getValue();
            if (clientName == null || empName == null) { err("Select client and employee."); return; }
            if (soDatePicker.getValue() == null) { err("Select an order date."); return; }
            try {
                SaleOrder so = new SaleOrder(
                        clientMap.get(clientName), soEmpMap.get(empName),
                        soDatePicker.getValue().toString(),
                        soDatePicker.getValue().plusDays(3).toString(),
                        "Pending", 0.0, "Unpaid");
                boolean ok = SaleOrderDAO.addSaleOrder(so);
                if (ok) {
                    int newSO = getLastId("SELECT MAX(SaleOrderID) FROM SaleOrder");
                    soCreatedLbl.setText("Sale Order #" + newSO + " created! Now add items below.");
                    ok("Sale Order #" + newSO + " created for " + clientName + "!");
                    refreshSoTable(soTable);
                    refreshSoItemBox.run();
                    soItemBox.getItems().stream().filter(l -> l.startsWith("SO #" + newSO + " ")).findFirst().ifPresent(soItemBox::setValue);
                } else err("Failed.");
            } catch (Exception ex) { err("Invalid date format."); }
        });

        btnAddSOItem.setOnAction(e -> {
            String batchLabel = batchBox.getValue();
            if (batchLabel == null) { err("Select a product/batch."); return; }
            if (soItemBox.getValue() == null) { err("Select a sale order."); return; }
            try {
                int soId   = soItemMap.get(soItemBox.getValue());
                int batchId = batchMap.get(batchLabel)[0];
                int qty    = Integer.parseInt(soQtyField.getText().trim());
                double price = Double.parseDouble(soPriceField.getText().trim());
                double disc  = Double.parseDouble(soDiscField.getText().trim());
                boolean ok = SaleOrderDAO.addSaleOrderItem(new SaleOrderItem(soId, batchId, qty, price, disc));
                if (ok) {
                    // Deduct stock and update SO total
                    Connection conn = DBConnection.connect();
                    PreparedStatement ps1 = conn.prepareStatement("UPDATE Batch SET QtyInStock=QtyInStock-? WHERE BatchID=? AND QtyInStock>=?");
                    ps1.setInt(1, qty); ps1.setInt(2, batchId); ps1.setInt(3, qty);
                    int rows = ps1.executeUpdate();
                    if (rows == 0) { conn.close(); err("Not enough stock in batch #" + batchId); return; }
                    PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE SaleOrder SET TotalAmount=(SELECT COALESCE(SUM(QtyOrdered*UnitPrice*(1-Discount/100)),0) FROM SaleOrderItem WHERE SaleOrderID=?) WHERE SaleOrderID=?");
                    ps2.setInt(1, soId); ps2.setInt(2, soId); ps2.executeUpdate();
                    conn.close();
                    ok("Item added and stock deducted!");
                    soQtyField.clear(); batchBox.setValue(null); soPriceField.clear(); soDiscField.setText("0");
                    refreshSOItems.run(); refreshSoTable(soTable);
                } else err("Failed.");
            } catch (Exception ex) { err("Invalid values."); }
        });

        btnRefresh.setOnAction(e -> { refreshSoTable(soTable); refreshSOItems.run(); refreshSoItemBox.run(); });
        refreshSoTable(soTable);
        refreshSOItems.run();
        return pane;
    }

    private void refreshSoTable(TableView<ObservableList<String>> soTable) {
        soTable.getItems().clear();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT so.SaleOrderID, cl.ClientName, CONCAT(e.FirstName,' ',e.LastName) AS Emp, " +
                            "so.OrderDate, so.DeliveryDate, so.Status, so.PaymentStatus, so.TotalAmount " +
                            "FROM SaleOrder so JOIN Client cl ON so.ClientID=cl.ClientID " +
                            "JOIN Employee e ON so.EmployeeID=e.EmployeeID ORDER BY so.OrderDate DESC");
            while (rs.next()) soTable.getItems().add(FXCollections.observableArrayList(
                    String.valueOf(rs.getInt("SaleOrderID")), rs.getString("ClientName"), rs.getString("Emp"),
                    rs.getString("OrderDate"),
                    rs.getString("DeliveryDate") != null ? rs.getString("DeliveryDate") : "-",
                    rs.getString("Status"), rs.getString("PaymentStatus"),
                    String.format("%.2f", rs.getDouble("TotalAmount"))));
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showApproveDialog(int soId, String clientName, double total, Runnable onDone) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Approve Order #" + soId);
        dialog.setHeaderText("Record payment received from: " + clientName);

        VBox content = new VBox(12);
        content.setPadding(new Insets(16));
        content.setPrefWidth(380);

        Label totalLbl = new Label("Order Total: " + String.format("%.2f", total) + " NIS");
        totalLbl.setStyle("-fx-font-weight:bold; -fx-font-size:14px; -fx-text-fill:#1B3A6B;");

        Label methodLbl = new Label("Payment Method:");
        methodLbl.setStyle("-fx-font-weight:bold;");
        ComboBox<String> methodBox = new ComboBox<>();
        methodBox.getItems().addAll("Cash", "BankTransfer", "Cheque");
        methodBox.setValue("Cash");
        methodBox.setMaxWidth(Double.MAX_VALUE);

        Label amtLbl = new Label("Amount received (0 if not yet paid):");
        amtLbl.setStyle("-fx-font-weight:bold;");
        TextField amtField = new TextField(String.format("%.2f", total));

        content.getChildren().addAll(totalLbl, methodLbl, methodBox, amtLbl, amtField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                double amt = Double.parseDouble(amtField.getText().trim());
                String payStatus = amt >= total ? "Paid" : (amt > 0 ? "Partial" : "Unpaid");
                String today = java.time.LocalDate.now().toString();
                if (amt > 0) PaymentDAO.addPayment(new Payment(soId, null, today, amt, methodBox.getValue(), "Incoming"));
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement("UPDATE SaleOrder SET Status='Approved', PaymentStatus=? WHERE SaleOrderID=?");
                ps.setString(1, payStatus); ps.setInt(2, soId); ps.executeUpdate(); conn.close();
                ok("Order #" + soId + " approved! Payment: " + payStatus);
                onDone.run();
            } catch (Exception ex) { err("Invalid amount."); }
        });
    }

    private void showPaymentDialog(int soId, String clientName, double total, Runnable onDone) {
        double alreadyPaid = 0;
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(SUM(Amount),0) AS Paid FROM Payment WHERE SaleOrderID=?");
            ps.setInt(1, soId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) alreadyPaid = rs.getDouble("Paid");
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        double remaining = Math.max(total - alreadyPaid, 0);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Record Payment - Order #" + soId);
        dialog.setHeaderText("Payment from: " + clientName);

        VBox content = new VBox(10);
        content.setPadding(new Insets(16));
        Label totalLbl   = new Label("Total: " + String.format("%.2f", total) + " NIS"); totalLbl.setStyle("-fx-font-weight:bold;");
        Label paidLbl    = new Label("Already paid: " + String.format("%.2f", alreadyPaid) + " NIS"); paidLbl.setStyle("-fx-text-fill:#16A34A; -fx-font-weight:bold;");
        Label remainLbl  = new Label("Remaining: " + String.format("%.2f", remaining) + " NIS"); remainLbl.setStyle("-fx-text-fill:#DC2626; -fx-font-weight:bold;");
        ComboBox<String> methodBox = new ComboBox<>();
        methodBox.getItems().addAll("Cash", "BankTransfer", "Cheque"); methodBox.setValue("Cash"); methodBox.setMaxWidth(Double.MAX_VALUE);
        TextField amtField = new TextField(String.format("%.2f", remaining));
        content.getChildren().addAll(totalLbl, paidLbl, remainLbl, new Label("Method:") {{ setStyle("-fx-font-weight:bold;"); }}, methodBox, new Label("Amount:") {{ setStyle("-fx-font-weight:bold;"); }}, amtField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final double finalAlreadyPaid = alreadyPaid;
        dialog.showAndWait().ifPresent(bt -> {
            if (bt != ButtonType.OK) return;
            try {
                double amt = Double.parseDouble(amtField.getText().trim());
                double newTotal = finalAlreadyPaid + amt;
                String payStatus = newTotal >= total ? "Paid" : "Partial";
                String today = java.time.LocalDate.now().toString();
                PaymentDAO.addPayment(new Payment(soId, null, today, amt, methodBox.getValue(), "Incoming"));
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement("UPDATE SaleOrder SET PaymentStatus=? WHERE SaleOrderID=?");
                ps.setString(1, payStatus); ps.setInt(2, soId); ps.executeUpdate(); conn.close();
                ok("Payment of " + String.format("%.2f", amt) + " NIS recorded. Status: " + payStatus);
                onDone.run();
            } catch (Exception ex) { err("Invalid amount."); }
        });
    }


    private VBox buildPaymentTab() {
        VBox pane = pane();
        sectionTitle(pane, "Payments");

        // Sale Order dropdown (optional — for incoming payments)
        ComboBox<String> soBox = new ComboBox<>();
        soBox.setPromptText("Sale Order (leave empty for outgoing)...");
        soBox.setStyle("-fx-font-size:13px;"); soBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> soMap = new java.util.LinkedHashMap<>();

        // Purchase Order dropdown (optional — for outgoing payments)
        ComboBox<String> poBox = new ComboBox<>();
        poBox.setPromptText("Purchase Order (leave empty for incoming)...");
        poBox.setStyle("-fx-font-size:13px;"); poBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> poMap = new java.util.LinkedHashMap<>();

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-font-size:13px;"); datePicker.setMaxWidth(Double.MAX_VALUE);
        TextField amtField = field("Amount");
        TextField methodField = field("Method: Cash / BankTransfer / Cheque");
        TextField dirField = field("Direction: Incoming / Outgoing");

        // Payment picker for delete
        ComboBox<String> payPicker = new ComboBox<>();
        payPicker.setPromptText("Select payment to delete...");
        payPicker.setStyle("-fx-font-size:13px;"); payPicker.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> payIdMap = new java.util.LinkedHashMap<>();

        Label soLabel = new Label("Sale Order:"); soLabel.setStyle("-fx-font-weight:bold;");
        Label poLabel = new Label("Purchase Order:"); poLabel.setStyle("-fx-font-weight:bold;");
        Label dateLabel = new Label("Payment Date:"); dateLabel.setStyle("-fx-font-weight:bold;");
        Label selLabel = new Label("Delete:"); selLabel.setStyle("-fx-font-weight:bold;");
        HBox linkRow = new HBox(10, new VBox(4, soLabel, soBox), new VBox(4, poLabel, poBox));
        HBox.setHgrow(linkRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(linkRow.getChildren().get(1), Priority.ALWAYS);
        pane.getChildren().addAll(linkRow, new VBox(4, dateLabel, datePicker), row(amtField, methodField, dirField), new VBox(4, selLabel, payPicker));

        Button btnAdd = btn("Add", GREEN);
        Button btnDel = btn("Delete", RED);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnDel, btnRef));
        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID", "SO#", "PO#", "Date", "Amount (NIS)", "Method", "Direction"}, new int[]{55, 60, 60, 100, 110, 120, 90});
        table.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(ObservableList<String> row, boolean empty) {
                super.updateItem(row, empty);
                if (row == null || empty) {
                    setStyle("");
                    return;
                }
                setStyle(row.get(6).equals("Incoming") ? "-fx-background-color:#F0FDF4;" : "-fx-background-color:#FEF2F2;");
            }
        });
        pane.getChildren().add(table);
        Runnable refresh = () -> {
            table.getItems().clear();
            soBox.getItems().clear(); soMap.clear();
            poBox.getItems().clear(); poMap.clear();
            payPicker.getItems().clear(); payIdMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsSo = conn.createStatement().executeQuery(
                        "SELECT so.SaleOrderID, cl.ClientName FROM SaleOrder so JOIN Client cl ON so.ClientID=cl.ClientID ORDER BY so.SaleOrderID DESC");
                while (rsSo.next()) {
                    String label = "SO #" + rsSo.getInt("SaleOrderID") + " - " + rsSo.getString("ClientName");
                    soMap.put(label, rsSo.getInt("SaleOrderID")); soBox.getItems().add(label);
                }
                ResultSet rsPo = conn.createStatement().executeQuery(
                        "SELECT po.PONumber, s.SupplierName FROM PurchaseOrder po JOIN Supplier s ON po.SupplierID=s.SupplierID ORDER BY po.PONumber DESC");
                while (rsPo.next()) {
                    String label = "PO #" + rsPo.getInt("PONumber") + " - " + rsPo.getString("SupplierName");
                    poMap.put(label, rsPo.getInt("PONumber")); poBox.getItems().add(label);
                }
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Payment ORDER BY PaymentDate DESC");
                while (rs.next()) {
                    int so = rs.getInt("SaleOrderID");
                    boolean soNull = rs.wasNull();
                    int po = rs.getInt("PONumber");
                    boolean poNull = rs.wasNull();
                    int payId = rs.getInt("PaymentID");
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(payId),
                            soNull ? "-" : String.valueOf(so),
                            poNull ? "-" : String.valueOf(po),
                            rs.getString("PaymentDate"), String.format("%.2f", rs.getDouble("Amount")),
                            rs.getString("PaymentMethod"), rs.getString("Direction")));
                    String payLabel = "Payment #" + payId + " - " + String.format("%.2f", rs.getDouble("Amount")) + " NIS - " + rs.getString("PaymentDate");
                    payPicker.getItems().add(payLabel);
                    payIdMap.put(payLabel, payId);
                }
                conn.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        refresh.run();
        table.setOnMouseClicked(e -> {
            ObservableList<String> row = table.getSelectionModel().getSelectedItem();
            if (row != null) {
                String payLabel = "Payment #" + row.get(0) + " - " + row.get(4) + " NIS - " + row.get(3);
                payPicker.setValue(payLabel);
            }
        });
        btnAdd.setOnAction(e -> {
            if (datePicker.getValue() == null) { err("Select a payment date."); return; }
            try {
                Integer soId = soBox.getValue() == null ? null : soMap.get(soBox.getValue());
                Integer poNum = poBox.getValue() == null ? null : poMap.get(poBox.getValue());
                boolean ok = PaymentDAO.addPayment(new Payment(soId, poNum, datePicker.getValue().toString(), Double.parseDouble(amtField.getText().trim()), methodField.getText().trim(), dirField.getText().trim()));
                if (ok) {
                    ok("Payment added!");
                    soBox.setValue(null);
                    poBox.setValue(null);
                    datePicker.setValue(LocalDate.now());
                    amtField.clear();
                    methodField.clear();
                    dirField.clear();
                    refresh.run();
                }
                else {
                    err("Failed. Check enum values.");
                }
            }
            catch (Exception ex) {
                err("Invalid values.");
            }
        });
        btnDel.setOnAction(e -> {
            String sel = payPicker.getValue();
            if (sel == null) { err("Select a payment to delete."); return; }
            if (!confirmDelete("payment")) {
                return;
            }
            try {
                boolean ok = PaymentDAO.deletePayment(payIdMap.get(sel));
                if (ok) {
                    ok("Payment deleted!");
                    payPicker.setValue(null);
                    refresh.run();
                }
                else {
                    err("Not found.");
                }
            }
            catch (Exception ex) {
                err("Invalid ID.");}
        });
        btnRef.setOnAction(e -> {
            payPicker.setValue(null);
            soBox.setValue(null);
            poBox.setValue(null);
            datePicker.setValue(LocalDate.now());
            amtField.clear();
            methodField.clear();
            dirField.clear();
            refresh.run();
        });
        return pane;
    }

    private VBox buildInventoryTab() {
        VBox pane = pane();
        sectionTitle(pane, "Inventory Transactions");
        Label hint = new Label("Receipt adds to stock | Dispatch subtracts from stock | Adjustment corrects a count");
        hint.setStyle("-fx-text-fill:#64748B; -fx-font-size:12px; -fx-font-style:italic;");
        pane.getChildren().add(hint);

        // Batch dropdown
        ComboBox<String> batchBox = new ComboBox<>();
        batchBox.setPromptText("Select batch...");
        batchBox.setStyle("-fx-font-size:13px;"); batchBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> batchMap = new java.util.LinkedHashMap<>();

        // Employee dropdown
        ComboBox<String> empBox = new ComboBox<>();
        empBox.setPromptText("Select employee...");
        empBox.setStyle("-fx-font-size:13px;"); empBox.setMaxWidth(Double.MAX_VALUE);
        java.util.Map<String, Integer> empMap = new java.util.LinkedHashMap<>();

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Receipt", "Dispatch", "Adjustment");
        typeBox.setPromptText("Transaction Type");
        typeBox.setStyle("-fx-font-size:13px;");
        typeBox.setMaxWidth(Double.MAX_VALUE);
        TextField qtyField = field("Quantity");
        TextField refField = field("Reference ID (PO or SO number)");

        Label batchLabel = new Label("Batch:"); batchLabel.setStyle("-fx-font-weight:bold;");
        Label empLabel   = new Label("Employee:"); empLabel.setStyle("-fx-font-weight:bold;");

        HBox row1 = new HBox(10, new VBox(4, batchLabel, batchBox), new VBox(4, empLabel, empBox), typeBox);
        HBox row2 = new HBox(10, qtyField, refField);
        HBox.setHgrow(row1.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(row1.getChildren().get(1), Priority.ALWAYS);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        HBox.setHgrow(qtyField, Priority.ALWAYS);
        HBox.setHgrow(refField, Priority.ALWAYS);
        pane.getChildren().addAll(row1, row2);
        Button btnAdd = btn("Record Transaction", GREEN);
        Button btnRef = btn("Refresh", DARK);
        pane.getChildren().add(new HBox(10, btnAdd, btnRef));
        TableView<ObservableList<String>> table = table();
        addCols(table, new String[]{"ID", "Batch", "Product", "Employee", "Type", "Qty", "Date", "Ref"}, new int[]{55, 60, 150, 120, 90, 60, 130, 60});
        pane.getChildren().add(table);
        Runnable refresh = () -> {
            table.getItems().clear();
            batchBox.getItems().clear(); batchMap.clear();
            empBox.getItems().clear(); empMap.clear();
            try {
                Connection conn = DBConnection.connect();
                ResultSet rsBatch = conn.createStatement().executeQuery(
                        "SELECT b.BatchID, p.ProductName, b.QtyInStock FROM Batch b JOIN Product p ON b.ProductID=p.ProductID ORDER BY p.ProductName");
                while (rsBatch.next()) {
                    String label = "Batch #" + rsBatch.getInt("BatchID") + " - " + rsBatch.getString("ProductName") + " (qty=" + rsBatch.getInt("QtyInStock") + ")";
                    batchMap.put(label, rsBatch.getInt("BatchID")); batchBox.getItems().add(label);
                }
                ResultSet rsEmp = conn.createStatement().executeQuery(
                        "SELECT EmployeeID, CONCAT(FirstName,' ',LastName,' (',Role,')') AS Name FROM Employee ORDER BY FirstName");
                while (rsEmp.next()) { empMap.put(rsEmp.getString("Name"), rsEmp.getInt("EmployeeID")); empBox.getItems().add(rsEmp.getString("Name")); }
                ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT it.TransactionID, it.BatchID, p.ProductName, CONCAT(e.FirstName,' ',e.LastName) AS Emp, it.TxnType, it.Quantity, it.TxnDate, it.ReferenceID " +
                                "FROM InventoryTransaction it JOIN Batch b ON it.BatchID=b.BatchID " +
                                "JOIN Product p ON b.ProductID=p.ProductID JOIN Employee e ON it.EmployeeID=e.EmployeeID ORDER BY it.TxnDate DESC");
                while (rs.next()) {
                    int ref = rs.getInt("ReferenceID");
                    boolean refNull = rs.wasNull();
                    table.getItems().add(FXCollections.observableArrayList(
                            String.valueOf(rs.getInt("TransactionID")), String.valueOf(rs.getInt("BatchID")),
                            rs.getString("ProductName"), rs.getString("Emp"), rs.getString("TxnType"),
                            String.valueOf(rs.getInt("Quantity")), rs.getString("TxnDate"),
                            refNull ? "-" : String.valueOf(ref)));
                }
                conn.close();
            }
            catch (Exception e) {
                e.printStackTrace();}
        };
        refresh.run();
        btnAdd.setOnAction(e -> {
            try {
                if (batchBox.getValue() == null) { err("Select a batch."); return; }
                if (empBox.getValue() == null) { err("Select an employee."); return; }
                if (typeBox.getValue() == null) {
                    err("Select transaction type.");
                    return;
                }
                boolean ok = InventoryTransactionDAO.addTransaction(new InventoryTransaction(
                        batchMap.get(batchBox.getValue()), empMap.get(empBox.getValue()),
                        typeBox.getValue(), Integer.parseInt(qtyField.getText().trim()),
                        refField.getText().trim().isEmpty() ? 0 : Integer.parseInt(refField.getText().trim())));
                if (ok) {
                    ok("Transaction recorded and stock updated!");
                    batchBox.setValue(null);
                    empBox.setValue(null);
                    typeBox.setValue(null);
                    qtyField.clear();
                    refField.clear();
                    refresh.run();
                }
                else {
                    err("Failed. Check batch and quantity.");
                }
            }
            catch (Exception ex) {
                err("Invalid values.");
            }
        });
        btnRef.setOnAction(e -> refresh.run());
        return pane;
    }

    private Tab tab(String title, VBox content) {
        Tab t = new Tab(title);
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:" + GRAY + ";");
        t.setContent(sp);
        return t;
    }

    private VBox pane() {
        VBox p = new VBox(14);
        p.setPadding(new Insets(20));
        p.setStyle("-fx-background-color:" + GRAY + ";");
        return p;
    }

    private void sectionTitle(VBox pane, String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lbl.setTextFill(Color.web(DARK));
        lbl.setStyle("-fx-padding:0 0 8 0; -fx-border-color:" + MID + "; -fx-border-width:0 0 2 0;");
        pane.getChildren().add(lbl);
    }

    private Label sub(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        l.setTextFill(Color.web(MID));
        return l;
    }

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius:6;" +
                " -fx-border-radius:6; -fx-border-color:#CBD5E1; -fx-padding:8 12;" +
                " -fx-font-size:13px; -fx-background-color:white;");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private Button btn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white;" +
                " -fx-background-radius:6; -fx-padding:8 18; -fx-cursor:hand;" +
                " -fx-font-weight:bold; -fx-font-size:13px;");
        return b;
    }

    private HBox row(javafx.scene.Node... nodes) {
        HBox h = new HBox(10, nodes);
        for (javafx.scene.Node n : nodes) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }
        return h;
    }

    private TableView<ObservableList<String>> table() {
        TableView<ObservableList<String>> t = new TableView<>();
        t.setPrefHeight(260);
        t.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No data."));
        return t;
    }

    private void addCols(TableView<ObservableList<String>> table, String[] cols, int[] widths) {
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<ObservableList<String>, String> c = new TableColumn<>(cols[i]);
            c.setPrefWidth(widths[i]);
            c.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(idx)));
            table.getColumns().add(c);
        }
    }


    private VBox kpi(String label, String value, String color) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(12));
        card.setPrefWidth(140);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-border-color:#E2E8F0; -fx-border-radius:10;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size:11px; -fx-text-fill:#64748B;");
        Label val = new Label(value);
        val.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        val.setTextFill(Color.web(color));
        val.setWrapText(true);
        card.getChildren().addAll(lbl, val);
        return card;
    }

    private boolean confirmDelete(String item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete " + item + "?");
        alert.setContentText("This cannot be undone.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void ok(String msg) {
        statusBar.setText("  " + msg);
        statusBar.setStyle("-fx-background-color:#F0FDF4; -fx-text-fill:#16A34A; -fx-border-color:#BBF7D0; -fx-border-width:1 0 0 0; -fx-padding:8 16;");
    }

    private void err(String msg) {
        statusBar.setText("  " + msg);
        statusBar.setStyle("-fx-background-color:#FEF2F2; -fx-text-fill:#DC2626; -fx-border-color:#FECACA; -fx-border-width:1 0 0 0; -fx-padding:8 16;");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private int getLastId(String query) {
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (rs.next()) {
                int id = rs.getInt(1);
                conn.close();
                return id;
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private VBox buildChartsTab() {
        VBox pane = pane();
        sectionTitle(pane, "Charts & Statistics");

        Label rev = new Label("Monthly Revenue (NIS) - Incoming Payments");
        rev.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        rev.setTextFill(Color.web(DARK));
        pane.getChildren().add(rev);

        Canvas revCanvas = new Canvas(900, 220);
        GraphicsContext revGc = revCanvas.getGraphicsContext2D();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT DATE_FORMAT(PaymentDate,'%Y-%m') AS Month, " +
                            "SUM(Amount) AS Total FROM Payment " +
                            "WHERE Direction='Incoming' " +
                            "GROUP BY Month ORDER BY Month LIMIT 9");
            java.util.List<String> months = new java.util.ArrayList<>();
            java.util.List<Double> vals   = new java.util.ArrayList<>();
            while (rs.next()) {
                months.add(rs.getString("Month"));
                vals.add(rs.getDouble("Total"));
            }
            conn.close();
            drawBarChart(revGc, months, vals, 900, 220, "#2E6DA4", "NIS");
        } catch (Exception e) { e.printStackTrace(); }
        pane.getChildren().add(revCanvas);
        pane.getChildren().add(new Separator());

        HBox catRow = new HBox(40);
        catRow.setAlignment(Pos.TOP_LEFT);

        VBox catBox = new VBox(8);
        Label catTitle = new Label("Sales by Category (units sold)");
        catTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        catTitle.setTextFill(Color.web(DARK));
        catBox.getChildren().add(catTitle);

        Canvas catCanvas = new Canvas(440, 260);
        GraphicsContext catGc = catCanvas.getGraphicsContext2D();
        java.util.List<String> catNames = new java.util.ArrayList<>();
        java.util.List<Double> catVals  = new java.util.ArrayList<>();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT c.Name, COALESCE(SUM(soi.QtyOrdered),0) AS Total " +
                            "FROM Category c " +
                            "LEFT JOIN Product p ON p.CategoryID=c.CategoryID " +
                            "LEFT JOIN Batch b ON b.ProductID=p.ProductID " +
                            "LEFT JOIN SaleOrderItem soi ON soi.BatchID=b.BatchID " +
                            "GROUP BY c.CategoryID ORDER BY Total DESC");
            while (rs.next()) {
                catNames.add(rs.getString("Name"));
                catVals.add(rs.getDouble("Total"));
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        drawPieChart(catGc, catNames, catVals, 440, 260);
        catBox.getChildren().add(catCanvas);
        catRow.getChildren().add(catBox);

        VBox whBox = new VBox(8);
        Label whTitle = new Label("Stock Levels per Warehouse (units)");
        whTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        whTitle.setTextFill(Color.web(DARK));
        whBox.getChildren().add(whTitle);

        Canvas whCanvas = new Canvas(380, 260);
        GraphicsContext whGc = whCanvas.getGraphicsContext2D();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT w.WarehouseName, COALESCE(SUM(b.QtyInStock),0) AS Total " +
                            "FROM Warehouse w LEFT JOIN Batch b ON b.WarehouseID=w.WarehouseID " +
                            "GROUP BY w.WarehouseID ORDER BY Total DESC");
            java.util.List<String> wNames = new java.util.ArrayList<>();
            java.util.List<Double> wVals  = new java.util.ArrayList<>();
            while (rs.next()) {
                wNames.add(rs.getString("WarehouseName"));
                wVals.add(rs.getDouble("Total"));
            }
            conn.close();
            drawBarChart(whGc, wNames, wVals, 380, 260, "#16A34A", "units");
        } catch (Exception e) { e.printStackTrace(); }
        whBox.getChildren().add(whCanvas);
        catRow.getChildren().add(whBox);
        pane.getChildren().add(catRow);
        pane.getChildren().add(new Separator());

        Label topCliTitle = new Label("Top 5 Clients by Total Orders Value (NIS)");
        topCliTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        topCliTitle.setTextFill(Color.web(DARK));
        pane.getChildren().add(topCliTitle);

        Canvas topCliCanvas = new Canvas(900, 200);
        GraphicsContext topCliGc = topCliCanvas.getGraphicsContext2D();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT cl.ClientName, COALESCE(SUM(so.TotalAmount),0) AS Total " +
                            "FROM Client cl LEFT JOIN SaleOrder so ON so.ClientID=cl.ClientID " +
                            "GROUP BY cl.ClientID ORDER BY Total DESC LIMIT 5");
            java.util.List<String> cNames = new java.util.ArrayList<>();
            java.util.List<Double> cVals  = new java.util.ArrayList<>();
            while (rs.next()) {
                cNames.add(rs.getString("ClientName"));
                cVals.add(rs.getDouble("Total"));
            }
            conn.close();
            drawBarChart(topCliGc, cNames, cVals, 900, 200, "#D97706", "NIS");
        } catch (Exception e) { e.printStackTrace(); }
        pane.getChildren().add(topCliCanvas);
        pane.getChildren().add(new Separator());

        Label topProdTitle = new Label("Top 5 Best-Selling Products (units sold)");
        topProdTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        topProdTitle.setTextFill(Color.web(DARK));
        pane.getChildren().add(topProdTitle);

        Canvas topProdCanvas = new Canvas(900, 200);
        GraphicsContext topProdGc = topProdCanvas.getGraphicsContext2D();
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT p.ProductName, COALESCE(SUM(soi.QtyOrdered),0) AS Total " +
                            "FROM Product p " +
                            "LEFT JOIN Batch b ON b.ProductID=p.ProductID " +
                            "LEFT JOIN SaleOrderItem soi ON soi.BatchID=b.BatchID " +
                            "GROUP BY p.ProductID ORDER BY Total DESC LIMIT 5");
            java.util.List<String> pNames = new java.util.ArrayList<>();
            java.util.List<Double> pVals  = new java.util.ArrayList<>();
            while (rs.next()) {
                pNames.add(rs.getString("ProductName"));
                pVals.add(rs.getDouble("Total"));
            }
            conn.close();
            drawBarChart(topProdGc, pNames, pVals, 900, 200, "#DC2626", "units");
        } catch (Exception e) { e.printStackTrace(); }
        pane.getChildren().add(topProdCanvas);

        return pane;
    }

    private void drawBarChart(GraphicsContext gc, java.util.List<String> labels,
                              java.util.List<Double> values, double W, double H,
                              String hexColor, String unit) {
        double padL = 60, padB = 50, padT = 20, padR = 20;
        double chartW = W - padL - padR;
        double chartH = H - padT - padB;

        gc.setFill(Color.web("#F8FAFC"));
        gc.fillRect(0, 0, W, H);

        if (values.isEmpty()) {
            gc.setFill(Color.web("#94A3B8"));
            gc.fillText("No data", W / 2 - 20, H / 2);
            return;
        }

        double maxVal = values.stream().mapToDouble(d -> d).max().orElse(1);
        if (maxVal == 0) maxVal = 1;

        int n = labels.size();
        double barW = (chartW / n) * 0.6;
        double gap  = (chartW / n) * 0.4;

        gc.setStroke(Color.web("#E2E8F0"));
        gc.setFill(Color.web("#64748B"));
        gc.setFont(Font.font("Arial", 10));
        gc.setLineWidth(1);
        int gridLines = 4;
        for (int i = 0; i <= gridLines; i++) {
            double y = padT + chartH - (chartH * i / gridLines);
            gc.strokeLine(padL, y, padL + chartW, y);
            double val = maxVal * i / gridLines;
            gc.fillText(String.format("%.0f", val), 2, y + 4);
        }

        gc.setFill(Color.web(hexColor));
        for (int i = 0; i < n; i++) {
            double x   = padL + i * (chartW / n) + gap / 2;
            double barH = (values.get(i) / maxVal) * chartH;
            double y    = padT + chartH - barH;
            gc.fillRoundRect(x, y, barW, barH, 4, 4);

            gc.setFill(Color.web("#1E293B"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            String valStr = values.get(i) > 999
                    ? String.format("%.0fk", values.get(i) / 1000)
                    : String.format("%.0f", values.get(i));
            gc.fillText(valStr, x + barW / 2 - 8, y - 4);

            gc.setFill(Color.web("#475569"));
            gc.setFont(Font.font("Arial", 10));
            String lbl = labels.get(i);
            if (lbl.length() > 12) lbl = lbl.substring(0, 11) + "…";
            gc.fillText(lbl, x, padT + chartH + 14);

            gc.setFill(Color.web(hexColor));
        }

        gc.setStroke(Color.web("#CBD5E1"));
        gc.setLineWidth(1.5);
        gc.strokeLine(padL, padT, padL, padT + chartH);
        gc.strokeLine(padL, padT + chartH, padL + chartW, padT + chartH);
    }

    private void drawPieChart(GraphicsContext gc, java.util.List<String> labels,
                              java.util.List<Double> values, double W, double H) {
        gc.setFill(Color.web("#F8FAFC"));
        gc.fillRect(0, 0, W, H);

        if (values.isEmpty()) {
            gc.setFill(Color.web("#94A3B8"));
            gc.fillText("No data", W / 2 - 20, H / 2);
            return;
        }

        double total = values.stream().mapToDouble(d -> d).sum();
        if (total == 0) total = 1;

        String[] palette = {
                "#2E6DA4","#16A34A","#DC2626","#D97706","#7C3AED",
                "#0891B2","#BE185D","#65A30D","#EA580C","#6366F1"
        };

        double cx = 130, cy = H / 2, r = 110;
        double startAngle = 0;

        for (int i = 0; i < values.size(); i++) {
            double sweep = (values.get(i) / total) * 360.0;
            gc.setFill(Color.web(palette[i % palette.length]));
            gc.fillArc(cx - r, cy - r, r * 2, r * 2, startAngle, sweep,
                    javafx.scene.shape.ArcType.ROUND);
            startAngle += sweep;
        }

        gc.setFill(Color.web("#F8FAFC"));
        gc.fillOval(cx - 55, cy - 55, 110, 110);

        gc.setFont(Font.font("Arial", 11));
        double legendX = cx + r + 20;
        double legendY = 24;
        for (int i = 0; i < labels.size(); i++) {
            gc.setFill(Color.web(palette[i % palette.length]));
            gc.fillRoundRect(legendX, legendY + i * 22, 12, 12, 3, 3);
            gc.setFill(Color.web("#1E293B"));
            String lbl = labels.get(i);
            if (lbl.length() > 14) lbl = lbl.substring(0, 13) + "…";
            double pct = total > 0 ? (values.get(i) / total * 100) : 0;
            gc.fillText(lbl + " (" + String.format("%.0f", pct) + "%)",
                    legendX + 18, legendY + i * 22 + 11);
        }
    }
}