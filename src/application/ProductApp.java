package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductApp extends Application {

    @Override
    public void start(Stage stage) {

        // ================= OUTPUT =================
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        // ================= CATEGORY SECTION =================
        TextField categoryIdField = new TextField();
        categoryIdField.setPromptText("Category ID (For Update/Delete Only)");

        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Category Name");

        TextField categoryDescField = new TextField();
        categoryDescField.setPromptText("Category Description");

        Button addCategoryButton = new Button("Add Category");
        Button updateCategoryButton = new Button("Update Category");
        Button deleteCategoryButton = new Button("Delete Category");
        Button showCategoriesButton = new Button("Show Categories");

        HBox categoryButtons = new HBox(8, addCategoryButton, updateCategoryButton,
                deleteCategoryButton, showCategoriesButton);

        // ================= PRODUCT SECTION =================
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID (For Update/Delete Only)");

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Product Description");

        TextField priceField = new TextField();
        priceField.setPromptText("Unit Price");

        TextField reorderField = new TextField();
        reorderField.setPromptText("Reorder Level");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category ID");

        Button addProductButton = new Button("Add Product");
        Button updateProductButton = new Button("Update Product");
        Button deleteProductButton = new Button("Delete Product");
        Button showProductsButton = new Button("Show Products");

        HBox productButtons = new HBox(8, addProductButton, updateProductButton,
                deleteProductButton, showProductsButton);

        // ================= WAREHOUSE SECTION =================
        TextField warehouseIdField = new TextField();
        warehouseIdField.setPromptText("Warehouse ID (For Update/Delete Only)");

        TextField warehouseNameField = new TextField();
        warehouseNameField.setPromptText("Warehouse Name");

        TextField warehouseAddressField = new TextField();
        warehouseAddressField.setPromptText("Warehouse Address");

        TextField warehouseCityField = new TextField();
        warehouseCityField.setPromptText("Warehouse City");

        TextField warehousePhoneField = new TextField();
        warehousePhoneField.setPromptText("Warehouse Phone");

        TextField warehouseCapacityField = new TextField();
        warehouseCapacityField.setPromptText("Capacity");

        Button addWarehouseButton = new Button("Add Warehouse");
        Button updateWarehouseButton = new Button("Update Warehouse");
        Button deleteWarehouseButton = new Button("Delete Warehouse");
        Button showWarehousesButton = new Button("Show Warehouses");

        HBox warehouseButtons = new HBox(8, addWarehouseButton, updateWarehouseButton,
                deleteWarehouseButton, showWarehousesButton);

        // ================= BATCH SECTION =================
        TextField batchIdField = new TextField();
        batchIdField.setPromptText("Batch ID (For Update/Delete Only)");

        TextField batchProductIdField = new TextField();
        batchProductIdField.setPromptText("Product ID");

        TextField batchWarehouseIdField = new TextField();
        batchWarehouseIdField.setPromptText("Warehouse ID");

        TextField batchNumberField = new TextField();
        batchNumberField.setPromptText("Batch Number");

        TextField batchQuantityField = new TextField();
        batchQuantityField.setPromptText("Quantity in Stock");

        TextField batchExpiryField = new TextField();
        batchExpiryField.setPromptText("Expiry Date yyyy-mm-dd");

        TextField batchLocationField = new TextField();
        batchLocationField.setPromptText("Storage Location");

        Button addBatchButton = new Button("Add Batch");
        Button updateBatchButton = new Button("Update Batch");
        Button deleteBatchButton = new Button("Delete Batch");
        Button showBatchesButton = new Button("Show Batches");



        HBox batchButtons = new HBox(8, addBatchButton, updateBatchButton,
                deleteBatchButton, showBatchesButton);

        // ================= Supplier SECTION =================

        TextField supplierIdField = new TextField();
        supplierIdField.setPromptText("Supplier ID (For Update/Delete Only)");

        TextField supplierNameField = new TextField();
        supplierNameField.setPromptText("Supplier Name");

        TextField supplierContactField = new TextField();
        supplierContactField.setPromptText("Contact Person");

        TextField supplierPhoneField = new TextField();
        supplierPhoneField.setPromptText("Phone");

        TextField supplierEmailField = new TextField();
        supplierEmailField.setPromptText("Email");

        TextField supplierCityField = new TextField();
        supplierCityField.setPromptText("City");

        Button addSupplierButton = new Button("Add Supplier");
        Button updateSupplierButton = new Button("Update Supplier");
        Button deleteSupplierButton = new Button("Delete Supplier");
        Button showSuppliersButton = new Button("Show Suppliers");



        HBox supplierButtons = new HBox(8, addSupplierButton, updateSupplierButton,
                deleteSupplierButton, showSuppliersButton);

        // ================= CLIENT SECTION =================
        TextField clientIdField = new TextField();
        clientIdField.setPromptText("Client ID (For Update/Delete Only)");

        TextField clientNameField = new TextField();
        clientNameField.setPromptText("Client Name");

        TextField clientTypeField = new TextField();
        clientTypeField.setPromptText("Client Type: Pharmacy or Clinic");

        TextField clientPhoneField = new TextField();
        clientPhoneField.setPromptText("Phone");

        TextField clientCityField = new TextField();
        clientCityField.setPromptText("City");

        TextField clientCreditField = new TextField();
        clientCreditField.setPromptText("Credit Limit");

        Button addClientButton = new Button("Add Client");
        Button updateClientButton = new Button("Update Client");
        Button deleteClientButton = new Button("Delete Client");
        Button showClientsButton = new Button("Show Clients");

        HBox clientButtons = new HBox(8, addClientButton, updateClientButton,
                deleteClientButton, showClientsButton);

        // ================= EMPLOYEE SECTION =================
        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Employee ID (For Update/Delete Only)");

        TextField employeeFirstNameField = new TextField();
        employeeFirstNameField.setPromptText("First Name");

        TextField employeeLastNameField = new TextField();
        employeeLastNameField.setPromptText("Last Name");

        TextField employeeRoleField = new TextField();
        employeeRoleField.setPromptText("Role");

        TextField employeeHireDateField = new TextField();
        employeeHireDateField.setPromptText("Hire Date yyyy-mm-dd");

        TextField employeePhoneField = new TextField();
        employeePhoneField.setPromptText("Phone");

        TextField employeeSalaryField = new TextField();
        employeeSalaryField.setPromptText("Salary");

        Button addEmployeeButton = new Button("Add Employee");
        Button updateEmployeeButton = new Button("Update Employee");
        Button deleteEmployeeButton = new Button("Delete Employee");
        Button showEmployeesButton = new Button("Show Employees");

        HBox employeeButtons = new HBox(8, addEmployeeButton, updateEmployeeButton,
                deleteEmployeeButton, showEmployeesButton);

        // ================= SUPPLIER PRODUCT SECTION =================

        TextField spSupplierIdField = new TextField();
        spSupplierIdField.setPromptText("Supplier ID");

        TextField spProductIdField = new TextField();
        spProductIdField.setPromptText("Product ID");

        TextField spUnitCostField = new TextField();
        spUnitCostField.setPromptText("Unit Cost");

        Button addSupplierProductButton = new Button("Link Supplier To Product");
        Button deleteSupplierProductButton = new Button("Remove Link");
        Button showSupplierProductsButton = new Button("Show Links");
        Button updateSupplierProductButton = new Button("Update Unit Cost");

        HBox supplierProductButtons = new HBox(
                8,
                addSupplierProductButton,
                updateSupplierProductButton,
                deleteSupplierProductButton,
                showSupplierProductsButton
        );
        // ================= PURCHASE ORDER SECTION =================

        TextField poSupplierIdField = new TextField();
        poSupplierIdField.setPromptText("Supplier ID");

        TextField poEmployeeIdField = new TextField();
        poEmployeeIdField.setPromptText("Employee ID");

        TextField poOrderDateField = new TextField();
        poOrderDateField.setPromptText("Order Date yyyy-mm-dd");

        TextField poDeliveryDateField = new TextField();
        poDeliveryDateField.setPromptText("Expected Delivery Date yyyy-mm-dd");

        TextField poStatusField = new TextField();
        poStatusField.setPromptText("Pending / Delivered / Cancelled");

        TextField poTotalAmountField = new TextField();
        poTotalAmountField.setPromptText("Total Amount");

        Button addPOButton = new Button("Create Purchase Order");
        Button showPOButton = new Button("Show Purchase Orders");

        HBox poButtons = new HBox(
                8,
                addPOButton,
                showPOButton
        );
        // ================= PURCHASE ORDER ITEM SECTION =================

        TextField poiPONumberField = new TextField();
        poiPONumberField.setPromptText("PO Number");

        TextField poiProductIdField = new TextField();
        poiProductIdField.setPromptText("Product ID");

        TextField poiQtyField = new TextField();
        poiQtyField.setPromptText("Quantity Ordered");

        TextField poiUnitCostField = new TextField();
        poiUnitCostField.setPromptText("Unit Cost");

        TextField poiReceivedField = new TextField();
        poiReceivedField.setPromptText("Quantity Received");

        Button addPOItemButton = new Button("Add Product To PO");
        Button showPOItemsButton = new Button("Show PO Items");

        HBox poItemButtons = new HBox(
                8,
                addPOItemButton,
                showPOItemsButton
        );

        // ================= SALE ORDER SECTION =================
        TextField soClientIdField = new TextField();
        soClientIdField.setPromptText("Client ID");

        TextField soEmployeeIdField = new TextField();
        soEmployeeIdField.setPromptText("Employee ID");

        TextField soOrderDateField = new TextField();
        soOrderDateField.setPromptText("Order Date yyyy-mm-dd");

        TextField soDeliveryDateField = new TextField();
        soDeliveryDateField.setPromptText("Delivery Date yyyy-mm-dd");

        TextField soStatusField = new TextField();
        soStatusField.setPromptText("Pending / Delivered / Cancelled");

        TextField soTotalAmountField = new TextField();
        soTotalAmountField.setPromptText("Total Amount");

        TextField soPaymentStatusField = new TextField();
        soPaymentStatusField.setPromptText("Paid / Unpaid / Partial");

        Button addSOButton = new Button("Create Sale Order");
        Button showSOButton = new Button("Show Sale Orders");

        HBox soButtons = new HBox(8, addSOButton, showSOButton);

        // ================= SALE ORDER ITEM SECTION =================
        TextField soiSaleOrderIdField = new TextField();
        soiSaleOrderIdField.setPromptText("Sale Order ID");

        TextField soiBatchIdField = new TextField();
        soiBatchIdField.setPromptText("Batch ID");

        TextField soiQtyField = new TextField();
        soiQtyField.setPromptText("Quantity Ordered");

        TextField soiUnitPriceField = new TextField();
        soiUnitPriceField.setPromptText("Unit Price");

        TextField soiDiscountField = new TextField();
        soiDiscountField.setPromptText("Discount");

        Button addSOItemButton = new Button("Add Item To Sale Order");
        Button showSOItemsButton = new Button("Show Sale Order Items");

        HBox soItemButtons = new HBox(8, addSOItemButton, showSOItemsButton);

        // ================= PAYMENT SECTION =================
        TextField paymentIdField = new TextField();
        paymentIdField.setPromptText("Payment ID (For Delete Only)");

        TextField paymentSaleOrderIdField = new TextField();
        paymentSaleOrderIdField.setPromptText("Sale Order ID (leave empty for outgoing payment)");

        TextField paymentPONumberField = new TextField();
        paymentPONumberField.setPromptText("PO Number (leave empty for incoming payment)");

        TextField paymentDateField = new TextField();
        paymentDateField.setPromptText("Payment Date yyyy-mm-dd");

        TextField paymentAmountField = new TextField();
        paymentAmountField.setPromptText("Amount");

        TextField paymentMethodField = new TextField();
        paymentMethodField.setPromptText("Cash / BankTransfer / Cheque");

        TextField paymentDirectionField = new TextField();
        paymentDirectionField.setPromptText("Incoming / Outgoing");

        Button addPaymentButton = new Button("Add Payment");
        Button deletePaymentButton = new Button("Delete Payment");
        Button showPaymentsButton = new Button("Show Payments");

        HBox paymentButtons = new HBox(8, addPaymentButton, deletePaymentButton, showPaymentsButton);


        // ================= Inventory Transaction SECTION =================

        TextField txnBatchIdField = new TextField();
        txnBatchIdField.setPromptText("Batch ID");

        TextField txnEmployeeIdField = new TextField();
        txnEmployeeIdField.setPromptText("Employee ID");

        TextField txnTypeField = new TextField();
        txnTypeField.setPromptText("Receipt / Dispatch / Adjustment");

        TextField txnQuantityField = new TextField();
        txnQuantityField.setPromptText("Quantity");

        TextField txnReferenceIdField = new TextField();
        txnReferenceIdField.setPromptText("Reference ID");

        Button addTxnButton = new Button("Add Inventory Transaction");
        Button showTxnButton = new Button("Show Inventory Transactions");

        HBox txnButtons = new HBox(8, addTxnButton, showTxnButton);

        // ================= REPORTS SECTION =================
        Button basicStatsButton = new Button("Basic Statistics");
        Button lowStockButton = new Button("Low Stock Products");
        Button expiringButton = new Button("Expiring Batches");
        Button stockValueButton = new Button("Stock Value by Category");
        Button salesSummaryButton = new Button("Sales Summary");
        Button paymentsSummaryButton = new Button("Payments Summary");
        Button warehouseCapacityButton = new Button("Warehouse Capacity");

        HBox reportButtons1 = new HBox(8, basicStatsButton, lowStockButton, expiringButton);
        HBox reportButtons2 = new HBox(8, stockValueButton, salesSummaryButton, paymentsSummaryButton, warehouseCapacityButton);

        // ================= CATEGORY ACTIONS =================
        addCategoryButton.setOnAction(e -> {
            String name = categoryNameField.getText().trim();
            String desc = categoryDescField.getText().trim();

            if (name.isEmpty() || desc.isEmpty()) {
                showErrorAlert("Validation Error", "All category fields must be filled.");
                return;
            }

            CategoryDAO.addCategory(new Category(name, desc));
            showInfoAlert("Success", "Category added successfully.");

            categoryIdField.clear();
            categoryNameField.clear();
            categoryDescField.clear();
        });

        updateCategoryButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(categoryIdField.getText().trim());

                Category existing = CategoryDAO.getCategoryById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Category ID does not exist.");
                    return;
                }

                String name = categoryNameField.getText().trim().isEmpty()
                        ? existing.getName()
                        : categoryNameField.getText().trim();

                String desc = categoryDescField.getText().trim().isEmpty()
                        ? existing.getDescription()
                        : categoryDescField.getText().trim();

                CategoryDAO.updateCategory(new Category(id, name, desc));

                showInfoAlert("Success", "Category updated successfully.");
                categoryIdField.clear();
                categoryNameField.clear();
                categoryDescField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Category ID.");
            }
        });

        deleteCategoryButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(categoryIdField.getText().trim());
                boolean success = CategoryDAO.deleteCategory(id);

                if (success) {
                    showInfoAlert("Success", "Category deleted successfully.");
                    categoryIdField.clear();
                    categoryNameField.clear();
                    categoryDescField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Category not found.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Category ID.");
            }
        });

        showCategoriesButton.setOnAction(e -> {
            outputArea.clear();
            for (Category c : CategoryDAO.getAllCategories()) {
                outputArea.appendText(c.getId() + " | " + c.getName() + " | " +
                        c.getDescription() + "\n");
            }
        });

        // ================= PRODUCT ACTIONS =================
        addProductButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String desc = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int reorder = Integer.parseInt(reorderField.getText().trim());
                int categoryId = Integer.parseInt(categoryField.getText().trim());

                if (name.isEmpty() || desc.isEmpty() || price < 0 || reorder < 0 || categoryId <= 0) {
                    showErrorAlert("Validation Error", "Please enter valid product data.");
                    return;
                }

                Product p = new Product(name, desc, price, reorder, categoryId);
                boolean success = ProductDAO.addProduct(p);

                if (success) {

                    showInfoAlert("Success", "Product added successfully.");

                    productIdField.clear();
                    nameField.clear();
                    descriptionField.clear();
                    priceField.clear();
                    reorderField.clear();
                    categoryField.clear();

                } else {
                    showErrorAlert("Database Error", "Product was not added. Check Category ID.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid product values.");
            }
        });

        updateProductButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(productIdField.getText().trim());

                Product existing = ProductDAO.getProductById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Product ID does not exist.");
                    return;
                }

                String name = nameField.getText().trim().isEmpty()
                        ? existing.getName()
                        : nameField.getText().trim();

                String desc = descriptionField.getText().trim().isEmpty()
                        ? existing.getDescription()
                        : descriptionField.getText().trim();

                double price = priceField.getText().trim().isEmpty()
                        ? existing.getPrice()
                        : Double.parseDouble(priceField.getText().trim());

                int reorder = reorderField.getText().trim().isEmpty()
                        ? existing.getReorderLevel()
                        : Integer.parseInt(reorderField.getText().trim());

                int categoryId = categoryField.getText().trim().isEmpty()
                        ? existing.getCategoryId()
                        : Integer.parseInt(categoryField.getText().trim());

                ProductDAO.updateProduct(new Product(id, name, desc, price, reorder, categoryId));
                showInfoAlert("Success", "Product updated successfully.");
                productIdField.clear();
                nameField.clear();
                descriptionField.clear();
                priceField.clear();
                reorderField.clear();
                categoryIdField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid product values.");
            }
        });

        deleteProductButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(productIdField.getText().trim());
                boolean success = ProductDAO.deleteProduct(id);

                if (success) {
                    showInfoAlert("Success", "Product deleted successfully.");
                    productIdField.clear();
                    nameField.clear();
                    descriptionField.clear();
                    priceField.clear();
                    reorderField.clear();
                    categoryField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Product not found.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Product ID.");
            }
        });

        showProductsButton.setOnAction(e -> {
            outputArea.clear();
            for (Product p : ProductDAO.getAllProducts()) {
                outputArea.appendText(
                        p.getId() + " | " +
                                p.getName() + " | " +
                                p.getDescription() + " | " +
                                p.getPrice() + " | Reorder: " +
                                p.getReorderLevel() + " | Category ID: " +
                                p.getCategoryId() + "\n"
                );
            }
        });

        // ================= WAREHOUSE ACTIONS =================
        addWarehouseButton.setOnAction(e -> {
            String name = warehouseNameField.getText().trim();
            String address = warehouseAddressField.getText().trim();
            String city = warehouseCityField.getText().trim();
            String phone = warehousePhoneField.getText().trim();
            int capacity = Integer.parseInt(warehouseCapacityField.getText().trim());

            if (name.isEmpty() || address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
                showErrorAlert("Validation Error", "All warehouse fields must be filled.");
                return;
            }

            boolean success = WarehouseDAO.addWarehouse(new Warehouse(name, address, city, phone, capacity));

            if (success) {
                showInfoAlert("Success", "Warehouse added successfully.");

                warehouseIdField.clear();
                warehouseNameField.clear();
                warehouseAddressField.clear();
                warehouseCityField.clear();
                warehousePhoneField.clear();
                warehouseCapacityField.clear();

            } else {
                showErrorAlert("Database Error", "Warehouse was not added.");
            }
        });

        updateWarehouseButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(warehouseIdField.getText().trim());

                Warehouse existing = WarehouseDAO.getWarehouseById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Warehouse ID does not exist.");
                    return;
                }

                String name = warehouseNameField.getText().trim().isEmpty()
                        ? existing.getName()
                        : warehouseNameField.getText().trim();

                String address = warehouseAddressField.getText().trim().isEmpty()
                        ? existing.getAddress()
                        : warehouseAddressField.getText().trim();

                String city = warehouseCityField.getText().trim().isEmpty()
                        ? existing.getCity()
                        : warehouseCityField.getText().trim();

                String phone = warehousePhoneField.getText().trim().isEmpty()
                        ? existing.getPhone()
                        : warehousePhoneField.getText().trim();
                int capacity = Integer.parseInt(warehouseCapacityField.getText().trim()) ;


                WarehouseDAO.updateWarehouse(new Warehouse(id, name, address, city, phone, capacity));
                showInfoAlert("Success", "Warehouse updated successfully.");
                warehouseIdField.clear();
                warehouseNameField.clear();
                warehouseAddressField.clear();
                warehouseCityField.clear();
                warehousePhoneField.clear();
                warehouseCapacityField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Warehouse ID.");
            }
        });

        deleteWarehouseButton.setOnAction(e -> {
            int id = Integer.parseInt(warehouseIdField.getText());

            boolean success = WarehouseDAO.deleteWarehouse(id);

            if(success) {

                warehouseIdField.clear();
                warehouseNameField.clear();
                warehouseAddressField.clear();
                warehouseCityField.clear();
                warehousePhoneField.clear();
                warehouseCapacityField.clear();


                outputArea.setText("Warehouse deleted successfully!");
            }
        });

        showWarehousesButton.setOnAction(e -> {
            outputArea.clear();
            for (Warehouse w : WarehouseDAO.getAllWarehouses()) {
                outputArea.appendText(
                        w.getId() + " | " +
                                w.getName() + " | " +
                                w.getAddress() + " | " +
                                w.getCity() + " | " +
                                w.getPhone() + "\n"+
                                w.getCapacity() + "\n"
                );
            }
        });

        // ================= BATCH ACTIONS =================
        addBatchButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(batchProductIdField.getText().trim());
                int warehouseId = Integer.parseInt(batchWarehouseIdField.getText().trim());
                String batchNumber = batchNumberField.getText().trim();
                int quantity = Integer.parseInt(batchQuantityField.getText().trim());
                String expiry = batchExpiryField.getText().trim();
                String location = batchLocationField.getText().trim();

                if (expiry.isEmpty() || location.isEmpty()
                        || productId <= 0 || warehouseId <= 0 || quantity < 0) {
                    showErrorAlert("Validation Error", "Please enter valid batch data.");
                    return;
                }
                boolean enoughCapacity = WarehouseDAO.hasEnoughCapacity(warehouseId, quantity);

                if (!ProductDAO.productExists(productId)) {
                    showErrorAlert("Invalid Product ID", "The entered Product ID does not exist.");
                    return;
                }

                if (!WarehouseDAO.warehouseExists(warehouseId)) {
                    showErrorAlert("Invalid Warehouse ID", "The entered Warehouse ID does not exist.");
                    return;
                }

                if (!WarehouseDAO.hasEnoughCapacity(warehouseId, quantity)) {
                    showErrorAlert("Capacity Error", "Cannot add this batch. Warehouse capacity would be exceeded.");
                    return;
                }
                if (!location.matches("[A-Za-z]-\\d+")) {
                    showErrorAlert("Invalid Location",
                            "Location must be like A-1, a-1, B-3, etc.");
                    return;
                }



                Batch b = new Batch(productId, warehouseId, quantity, expiry, location);
                boolean success = BatchDAO.addBatch(b);

                if (success) {
                    showInfoAlert("Success", "Batch added successfully.");

                    batchIdField.clear();
                    batchProductIdField.clear();
                    batchWarehouseIdField.clear();
                    batchNumberField.clear();
                    batchQuantityField.clear();
                    batchExpiryField.clear();
                    batchLocationField.clear();
                } else {
                    showErrorAlert("Database Error", "Batch was not added. Check entered data.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid batch values.");
            }
        });

        updateBatchButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(batchIdField.getText().trim());

                Batch existing = BatchDAO.getBatchById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Batch ID does not exist.");
                    return;
                }

                int productId = batchProductIdField.getText().trim().isEmpty()
                        ? existing.getProductId()
                        : Integer.parseInt(batchProductIdField.getText().trim());

                int warehouseId = batchWarehouseIdField.getText().trim().isEmpty()
                        ? existing.getWarehouseId()
                        : Integer.parseInt(batchWarehouseIdField.getText().trim());

                int quantity = batchQuantityField.getText().trim().isEmpty()
                        ? existing.getQuantity()
                        : Integer.parseInt(batchQuantityField.getText().trim());

                String expiry = batchExpiryField.getText().trim().isEmpty()
                        ? existing.getExpiryDate()
                        : batchExpiryField.getText().trim();

                String location = batchLocationField.getText().trim().isEmpty()
                        ? existing.getStorageLocation()
                        : batchLocationField.getText().trim();

                BatchDAO.updateBatch(new Batch(id, productId, warehouseId, quantity, expiry, location));
                showInfoAlert("Success", "Batch updated successfully.");
                batchIdField.clear();
                batchProductIdField.clear();
                batchWarehouseIdField.clear();
                batchQuantityField.clear();
                batchExpiryField.clear();
                batchLocationField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid batch values.");
            }
        });

        deleteBatchButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(batchIdField.getText().trim());
                boolean success = BatchDAO.deleteBatch(id);

                if (success) {
                    showInfoAlert("Success", "Batch deleted successfully.");
                    batchIdField.clear();
                    batchProductIdField.clear();
                    batchWarehouseIdField.clear();
                    batchQuantityField.clear();
                    batchExpiryField.clear();
                    batchLocationField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Batch not found.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Batch ID.");
            }
        });

        showBatchesButton.setOnAction(e -> {
            outputArea.clear();
            for (Batch b : BatchDAO.getAllBatches()) {
                outputArea.appendText(
                        b.getId() + " | Product ID: " +
                                b.getProductId() + " | Warehouse ID: " +
                                b.getWarehouseId() + " | Batch: " +
                                b.getQuantity() + " | Expiry: " +
                                b.getExpiryDate() + " | Location: " +
                                b.getStorageLocation() + "\n"
                );
            }
        });

        // ================= Supplier ACTIONS =================

        addSupplierButton.setOnAction(e -> {
            String name = supplierNameField.getText().trim();
            String contact = supplierContactField.getText().trim();
            String phone = supplierPhoneField.getText().trim();
            String email = supplierEmailField.getText().trim();
            String city = supplierCityField.getText().trim();

            if (name.isEmpty() || contact.isEmpty() || phone.isEmpty() || email.isEmpty() || city.isEmpty()) {
                showErrorAlert("Validation Error", "All supplier fields must be filled.");
                return;
            }

            boolean success = SupplierDAO.addSupplier(new Supplier(name, contact, phone, email, city));

            if (success) {
                showInfoAlert("Success", "Supplier added successfully.");

                supplierIdField.clear();
                supplierNameField.clear();
                supplierContactField.clear();
                supplierPhoneField.clear();
                supplierEmailField.clear();
                supplierCityField.clear();
            } else {
                showErrorAlert("Database Error", "Supplier was not added.");
            }
        });

        showSuppliersButton.setOnAction(e -> {
            outputArea.clear();

            for (Supplier s : SupplierDAO.getAllSuppliers()) {
                outputArea.appendText(
                        s.getId() + " | " +
                                s.getName() + " | " +
                                s.getContactPerson() + " | " +
                                s.getPhone() + " | " +
                                s.getEmail() + " | " +
                                s.getCity() + "\n"
                );
            }
        });
        updateSupplierButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(supplierIdField.getText().trim());

                Supplier existing = SupplierDAO.getSupplierById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Supplier ID does not exist.");
                    return;
                }

                String name = supplierNameField.getText().trim().isEmpty() ? existing.getName() : supplierNameField.getText().trim();
                String contact = supplierContactField.getText().trim().isEmpty() ? existing.getContactPerson() : supplierContactField.getText().trim();
                String phone = supplierPhoneField.getText().trim().isEmpty() ? existing.getPhone() : supplierPhoneField.getText().trim();
                String email = supplierEmailField.getText().trim().isEmpty() ? existing.getEmail() : supplierEmailField.getText().trim();
                String city = supplierCityField.getText().trim().isEmpty() ? existing.getCity() : supplierCityField.getText().trim();

                SupplierDAO.updateSupplier(new Supplier(id, name, contact, phone, email, city));
                showInfoAlert("Success", "Supplier updated successfully.");
                supplierIdField.clear();
                supplierNameField.clear();
                supplierContactField.clear();
                supplierPhoneField.clear();
                supplierEmailField.clear();
                supplierCityField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Supplier ID.");
            }
        });

        deleteSupplierButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(supplierIdField.getText().trim());

                boolean success = SupplierDAO.deleteSupplier(id);

                if (success) {
                    showInfoAlert("Success", "Supplier deleted successfully.");
                    supplierIdField.clear();
                    supplierNameField.clear();
                    supplierContactField.clear();
                    supplierPhoneField.clear();
                    supplierEmailField.clear();
                    supplierCityField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Supplier not found.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Supplier ID.");
            }
        });

        // ================= CLIENT ACTIONS =================
        addClientButton.setOnAction(e -> {
            try {
                String name = clientNameField.getText().trim();
                String type = clientTypeField.getText().trim();
                String phone = clientPhoneField.getText().trim();
                String city = clientCityField.getText().trim();
                double credit = Double.parseDouble(clientCreditField.getText().trim());

                if (name.isEmpty() || type.isEmpty() || phone.isEmpty() || city.isEmpty() || credit < 0) {
                    showErrorAlert("Validation Error", "Please enter valid client data.");
                    return;
                }

                boolean success = ClientDAO.addClient(new Client(name, type, phone, city, credit));

                if (success) {
                    showInfoAlert("Success", "Client added successfully.");

                    clientIdField.clear();
                    clientNameField.clear();
                    clientTypeField.clear();
                    clientPhoneField.clear();
                    clientCityField.clear();
                    clientCreditField.clear();
                } else {
                    showErrorAlert("Database Error", "Client was not added. Type must be Pharmacy or Clinic.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid client values.");
            }
        });

        showClientsButton.setOnAction(e -> {
            outputArea.clear();

            for (Client c : ClientDAO.getAllClients()) {
                outputArea.appendText(
                        c.getId() + " | " +
                                c.getName() + " | " +
                                c.getType() + " | " +
                                c.getPhone() + " | " +
                                c.getCity() + " | Credit Limit: " +
                                c.getCreditLimit() + "\n"
                );
            }
        });

        updateClientButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(clientIdField.getText().trim());

                Client existing = ClientDAO.getClientById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Client ID does not exist.");
                    return;
                }

                String name = clientNameField.getText().trim().isEmpty() ? existing.getName() : clientNameField.getText().trim();
                String type = clientTypeField.getText().trim().isEmpty() ? existing.getType() : clientTypeField.getText().trim();
                String phone = clientPhoneField.getText().trim().isEmpty() ? existing.getPhone() : clientPhoneField.getText().trim();
                String city = clientCityField.getText().trim().isEmpty() ? existing.getCity() : clientCityField.getText().trim();
                double credit = clientCreditField.getText().trim().isEmpty() ? existing.getCreditLimit() : Double.parseDouble(clientCreditField.getText().trim());

                ClientDAO.updateClient(new Client(id, name, type, phone, city, credit));
                showInfoAlert("Success", "Client updated successfully.");
                clientIdField.clear();
                clientNameField.clear();
                clientTypeField.clear();
                clientPhoneField.clear();
                clientCityField.clear();
                clientCreditField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid client values.");
            }
        });

        deleteClientButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(clientIdField.getText().trim());

                boolean success = ClientDAO.deleteClient(id);

                if (success) {
                    showInfoAlert("Success", "Client deleted successfully.");
                    clientIdField.clear();
                    clientNameField.clear();
                    clientTypeField.clear();
                    clientPhoneField.clear();
                    clientCityField.clear();
                    clientCreditField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Client not found or linked to sale orders.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Client ID.");
            }
        });
        // ================= EMPLOYEE ACTIONS =================
        addEmployeeButton.setOnAction(e -> {
            try {
                String firstName = employeeFirstNameField.getText().trim();
                String lastName = employeeLastNameField.getText().trim();
                String role = employeeRoleField.getText().trim();
                String hireDate = employeeHireDateField.getText().trim();
                String phone = employeePhoneField.getText().trim();
                double salary = Double.parseDouble(employeeSalaryField.getText().trim());

                if (firstName.isEmpty() || lastName.isEmpty() || role.isEmpty()
                        || hireDate.isEmpty() || phone.isEmpty() || salary < 0) {
                    showErrorAlert("Validation Error", "Please enter valid employee data.");
                    return;
                }

                boolean success = EmployeeDAO.addEmployee(
                        new Employee(firstName, lastName, role, hireDate, phone, salary)
                );

                if (success) {
                    showInfoAlert("Success", "Employee added successfully.");

                    employeeIdField.clear();
                    employeeFirstNameField.clear();
                    employeeLastNameField.clear();
                    employeeRoleField.clear();
                    employeeHireDateField.clear();
                    employeePhoneField.clear();
                    employeeSalaryField.clear();
                } else {
                    showErrorAlert("Database Error", "Employee was not added.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid employee values.");
            }
        });

        showEmployeesButton.setOnAction(e -> {
            outputArea.clear();

            for (Employee emp : EmployeeDAO.getAllEmployees()) {
                outputArea.appendText(
                        emp.getId() + " | " +
                                emp.getFirstName() + " " +
                                emp.getLastName() + " | " +
                                emp.getRole() + " | " +
                                emp.getHireDate() + " | " +
                                emp.getPhone() + " | Salary: " +
                                emp.getSalary() + "\n"
                );
            }
        });

        updateEmployeeButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(employeeIdField.getText().trim());

                Employee existing = EmployeeDAO.getEmployeeById(id);
                if (existing == null) {
                    showErrorAlert("Database Error", "Employee ID does not exist.");
                    return;
                }

                String firstName = employeeFirstNameField.getText().trim().isEmpty()
                        ? existing.getFirstName()
                        : employeeFirstNameField.getText().trim();

                String lastName = employeeLastNameField.getText().trim().isEmpty()
                        ? existing.getLastName()
                        : employeeLastNameField.getText().trim();

                String role = employeeRoleField.getText().trim().isEmpty()
                        ? existing.getRole()
                        : employeeRoleField.getText().trim();

                String hireDate = employeeHireDateField.getText().trim().isEmpty()
                        ? existing.getHireDate()
                        : employeeHireDateField.getText().trim();

                String phone = employeePhoneField.getText().trim().isEmpty()
                        ? existing.getPhone()
                        : employeePhoneField.getText().trim();

                double salary = employeeSalaryField.getText().trim().isEmpty()
                        ? existing.getSalary()
                        : Double.parseDouble(employeeSalaryField.getText().trim());

                EmployeeDAO.updateEmployee(
                        new Employee(id, firstName, lastName, role, hireDate, phone, salary)
                );

                showInfoAlert("Success", "Employee updated successfully.");

                employeeIdField.clear();
                employeeFirstNameField.clear();
                employeeLastNameField.clear();
                employeeRoleField.clear();
                employeeHireDateField.clear();
                employeePhoneField.clear();
                employeeSalaryField.clear();

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid employee values.");
            }
        });

        deleteEmployeeButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(employeeIdField.getText().trim());

                boolean success = EmployeeDAO.deleteEmployee(id);

                if (success) {
                    showInfoAlert("Success", "Employee deleted successfully.");
                    employeeIdField.clear();
                    employeeIdField.clear();
                    employeeFirstNameField.clear();
                    employeeLastNameField.clear();
                    employeeRoleField.clear();
                    employeeHireDateField.clear();
                    employeePhoneField.clear();
                    employeeSalaryField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Employee not found or linked to orders/transactions.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Employee ID.");
            }
        });

        // ================= SupplierProduct ACTIONS =================
        addSupplierProductButton.setOnAction(e -> {

            try {

                int supplierId =
                        Integer.parseInt(spSupplierIdField.getText().trim());

                int productId =
                        Integer.parseInt(spProductIdField.getText().trim());

                double unitCost =
                        Double.parseDouble(spUnitCostField.getText().trim());

                boolean success =
                        SupplierProductDAO.addSupplierProduct(
                                new SupplierProduct(
                                        supplierId,
                                        productId,
                                        unitCost
                                )
                        );

                if(success){

                    showInfoAlert(
                            "Success",
                            "Supplier linked to Product successfully."
                    );

                    spSupplierIdField.clear();
                    spProductIdField.clear();
                    spUnitCostField.clear();

                }else{

                    showErrorAlert(
                            "Database Error",
                            "Could not create link."
                    );
                }

            } catch(Exception ex){

                showErrorAlert(
                        "Input Error",
                        "Please enter valid values."
                );
            }
        });
        showSupplierProductsButton.setOnAction(e -> {

            outputArea.clear();

            for(SupplierProduct sp :
                    SupplierProductDAO.getAllSupplierProducts()){

                outputArea.appendText(
                        "Supplier ID: "
                                + sp.getSupplierId()
                                + " | Product ID: "
                                + sp.getProductId()
                                + " | Unit Cost: "
                                + sp.getUnitCost()
                                + "\n"
                );
            }
        });

        updateSupplierProductButton.setOnAction(e -> {
            try {
                int supplierId = Integer.parseInt(spSupplierIdField.getText().trim());
                int productId = Integer.parseInt(spProductIdField.getText().trim());
                double unitCost = Double.parseDouble(spUnitCostField.getText().trim());

                boolean success = SupplierProductDAO.updateSupplierProduct(
                        new SupplierProduct(supplierId, productId, unitCost)
                );

                if (success) {
                    showInfoAlert("Success", "Unit cost updated successfully.");

                    spSupplierIdField.clear();
                    spProductIdField.clear();
                    spUnitCostField.clear();
                } else {
                    showErrorAlert("Update Failed", "Supplier-product link does not exist.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid supplier ID, product ID, and unit cost.");
            }
        });

        deleteSupplierProductButton.setOnAction(e -> {

            try {

                int supplierId =
                        Integer.parseInt(spSupplierIdField.getText().trim());

                int productId =
                        Integer.parseInt(spProductIdField.getText().trim());

                boolean success =
                        SupplierProductDAO.deleteSupplierProduct(
                                supplierId,
                                productId
                        );

                if(success){

                    showInfoAlert(
                            "Success",
                            "Link deleted successfully."
                    );

                    spSupplierIdField.clear();
                    spProductIdField.clear();
                    spUnitCostField.clear();

                }else{

                    showErrorAlert(
                            "Delete Failed",
                            "Link not found."
                    );
                }

            } catch(Exception ex){

                showErrorAlert(
                        "Input Error",
                        "Please enter valid IDs."
                );
            }
        });

        // ================= PurchaseOrder ACTIONS =================
        addPOButton.setOnAction(e -> {

            try {

                PurchaseOrder po = new PurchaseOrder(
                        Integer.parseInt(poSupplierIdField.getText().trim()),
                        Integer.parseInt(poEmployeeIdField.getText().trim()),
                        poOrderDateField.getText().trim(),
                        poDeliveryDateField.getText().trim(),
                        poStatusField.getText().trim(),
                        Double.parseDouble(poTotalAmountField.getText().trim())
                );

                boolean success =
                        PurchaseOrderDAO.addPurchaseOrder(po);

                if(success){

                    showInfoAlert(
                            "Success",
                            "Purchase Order created."
                    );

                    poSupplierIdField.clear();
                    poEmployeeIdField.clear();
                    poOrderDateField.clear();
                    poDeliveryDateField.clear();
                    poStatusField.clear();
                    poTotalAmountField.clear();

                }else{

                    showErrorAlert(
                            "Database Error",
                            "Could not create Purchase Order."
                    );
                }

            } catch(Exception ex){

                showErrorAlert(
                        "Input Error",
                        "Please enter valid values."
                );
            }
        });

        showPOButton.setOnAction(e -> {

            outputArea.clear();

            for(PurchaseOrder po :
                    PurchaseOrderDAO.getAllPurchaseOrders()){

                outputArea.appendText(
                        "PO#: "
                                + po.getPoNumber()
                                + " | Supplier: "
                                + po.getSupplierId()
                                + " | Employee: "
                                + po.getEmployeeId()
                                + " | Status: "
                                + po.getStatus()
                                + " | Total: "
                                + po.getTotalAmount()
                                + "\n"
                );
            }
        });

        // ================= PurchaseOrder Item ACTIONS =================

        addPOItemButton.setOnAction(e -> {

            try {

                PurchaseOrderItem item =
                        new PurchaseOrderItem(
                                Integer.parseInt(
                                        poiPONumberField.getText().trim()
                                ),
                                Integer.parseInt(
                                        poiProductIdField.getText().trim()
                                ),
                                Integer.parseInt(
                                        poiQtyField.getText().trim()
                                ),
                                Double.parseDouble(
                                        poiUnitCostField.getText().trim()
                                ),
                                Integer.parseInt(
                                        poiReceivedField.getText().trim()
                                )
                        );

                boolean success =
                        PurchaseOrderDAO.addPurchaseOrderItem(item);

                if(success){

                    showInfoAlert(
                            "Success",
                            "Item added to Purchase Order."
                    );

                    poiPONumberField.clear();
                    poiProductIdField.clear();
                    poiQtyField.clear();
                    poiUnitCostField.clear();
                    poiReceivedField.clear();

                }else{

                    showErrorAlert(
                            "Database Error",
                            "Could not add item."
                    );
                }

            } catch(Exception ex){

                showErrorAlert(
                        "Input Error",
                        "Please enter valid values."
                );
            }
        });

        showPOItemsButton.setOnAction(e -> {

            outputArea.clear();

            for(PurchaseOrderItem item :
                    PurchaseOrderDAO.getAllPurchaseOrderItems()){

                outputArea.appendText(
                        "PO#: "
                                + item.getPoNumber()
                                + " | Product: "
                                + item.getProductId()
                                + " | Qty: "
                                + item.getQtyOrdered()
                                + " | Cost: "
                                + item.getUnitCost()
                                + "\n"
                );
            }
        });

        // ================= SALE ORDER ACTIONS =================
        addSOButton.setOnAction(e -> {
            try {
                SaleOrder so = new SaleOrder(
                        Integer.parseInt(soClientIdField.getText().trim()),
                        Integer.parseInt(soEmployeeIdField.getText().trim()),
                        soOrderDateField.getText().trim(),
                        soDeliveryDateField.getText().trim(),
                        soStatusField.getText().trim(),
                        Double.parseDouble(soTotalAmountField.getText().trim()),
                        soPaymentStatusField.getText().trim()
                );

                boolean success = SaleOrderDAO.addSaleOrder(so);

                if (success) {
                    showInfoAlert("Success", "Sale Order created.");

                    soClientIdField.clear();
                    soEmployeeIdField.clear();
                    soOrderDateField.clear();
                    soDeliveryDateField.clear();
                    soStatusField.clear();
                    soTotalAmountField.clear();
                    soPaymentStatusField.clear();
                } else {
                    showErrorAlert("Database Error", "Could not create Sale Order.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid sale order values.");
            }
        });

        showSOButton.setOnAction(e -> {
            outputArea.clear();

            for (SaleOrder so : SaleOrderDAO.getAllSaleOrders()) {
                outputArea.appendText(
                        "SO#: " + so.getSaleOrderId()
                                + " | Client: " + so.getClientId()
                                + " | Employee: " + so.getEmployeeId()
                                + " | Status: " + so.getStatus()
                                + " | Payment: " + so.getPaymentStatus()
                                + " | Total: " + so.getTotalAmount()
                                + "\n"
                );
            }
        });

        // ================= SALE ORDER ITEM ACTIONS =================
        addSOItemButton.setOnAction(e -> {
            try {
                SaleOrderItem item = new SaleOrderItem(
                        Integer.parseInt(soiSaleOrderIdField.getText().trim()),
                        Integer.parseInt(soiBatchIdField.getText().trim()),
                        Integer.parseInt(soiQtyField.getText().trim()),
                        Double.parseDouble(soiUnitPriceField.getText().trim()),
                        Double.parseDouble(soiDiscountField.getText().trim())
                );

                boolean success = SaleOrderDAO.addSaleOrderItem(item);

                if (success) {
                    showInfoAlert("Success", "Item added to Sale Order.");

                    soiSaleOrderIdField.clear();
                    soiBatchIdField.clear();
                    soiQtyField.clear();
                    soiUnitPriceField.clear();
                    soiDiscountField.clear();
                } else {
                    showErrorAlert("Database Error", "Could not add sale order item.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid sale order item values.");
            }
        });

        showSOItemsButton.setOnAction(e -> {
            outputArea.clear();

            for (SaleOrderItem item : SaleOrderDAO.getAllSaleOrderItems()) {
                outputArea.appendText(
                        "SO#: " + item.getSaleOrderId()
                                + " | Batch: " + item.getBatchId()
                                + " | Qty: " + item.getQtyOrdered()
                                + " | Unit Price: " + item.getUnitPrice()
                                + " | Discount: " + item.getDiscount()
                                + "\n"
                );
            }
        });

        // ================= PAYMENT ACTIONS =================
        addPaymentButton.setOnAction(e -> {
            try {
                Integer saleOrderId = paymentSaleOrderIdField.getText().trim().isEmpty()
                        ? null
                        : Integer.parseInt(paymentSaleOrderIdField.getText().trim());

                Integer poNumber = paymentPONumberField.getText().trim().isEmpty()
                        ? null
                        : Integer.parseInt(paymentPONumberField.getText().trim());

                String date = paymentDateField.getText().trim();
                double amount = Double.parseDouble(paymentAmountField.getText().trim());
                String method = paymentMethodField.getText().trim();
                String direction = paymentDirectionField.getText().trim();

                if (date.isEmpty() || method.isEmpty() || direction.isEmpty() || amount <= 0) {
                    showErrorAlert("Validation Error", "Please enter valid payment data.");
                    return;
                }

                boolean success = PaymentDAO.addPayment(
                        new Payment(saleOrderId, poNumber, date, amount, method, direction)
                );

                if (success) {
                    showInfoAlert("Success", "Payment added successfully.");

                    paymentIdField.clear();
                    paymentSaleOrderIdField.clear();
                    paymentPONumberField.clear();
                    paymentDateField.clear();
                    paymentAmountField.clear();
                    paymentMethodField.clear();
                    paymentDirectionField.clear();
                } else {
                    showErrorAlert("Database Error", "Payment was not added. Check IDs and enum values.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid payment values.");
            }
        });

        showPaymentsButton.setOnAction(e -> {
            outputArea.clear();

            for (Payment p : PaymentDAO.getAllPayments()) {
                outputArea.appendText(
                        "Payment ID: " + p.getPaymentId()
                                + " | SO: " + p.getSaleOrderId()
                                + " | PO: " + p.getPoNumber()
                                + " | Date: " + p.getPaymentDate()
                                + " | Amount: " + p.getAmount()
                                + " | Method: " + p.getPaymentMethod()
                                + " | Direction: " + p.getDirection()
                                + "\n"
                );
            }
        });

        deletePaymentButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(paymentIdField.getText().trim());

                boolean success = PaymentDAO.deletePayment(id);

                if (success) {
                    showInfoAlert("Success", "Payment deleted successfully.");
                    paymentIdField.clear();
                } else {
                    showErrorAlert("Delete Failed", "Payment not found.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter a valid Payment ID.");
            }
        });

        // ================= Inventory Transaction Actions =================

        addTxnButton.setOnAction(e -> {
            try {
                InventoryTransaction t = new InventoryTransaction(
                        Integer.parseInt(txnBatchIdField.getText().trim()),
                        Integer.parseInt(txnEmployeeIdField.getText().trim()),
                        txnTypeField.getText().trim(),
                        Integer.parseInt(txnQuantityField.getText().trim()),
                        Integer.parseInt(txnReferenceIdField.getText().trim())
                );

                boolean success = InventoryTransactionDAO.addTransaction(t);

                if (success) {
                    showInfoAlert("Success", "Inventory transaction added and stock updated.");

                    txnBatchIdField.clear();
                    txnEmployeeIdField.clear();
                    txnTypeField.clear();
                    txnQuantityField.clear();
                    txnReferenceIdField.clear();
                } else {
                    showErrorAlert("Transaction Failed",
                            "Transaction was not added. Check Batch ID, Employee ID, type, quantity, and available stock.");
                }

            } catch (Exception ex) {
                showErrorAlert("Input Error", "Please enter valid transaction data.");
            }
        });

        showTxnButton.setOnAction(e -> {
            outputArea.clear();

            for (InventoryTransaction t : InventoryTransactionDAO.getAllTransactions()) {
                outputArea.appendText(
                        "Txn ID: " + t.getTransactionId()
                                + " | Batch: " + t.getBatchId()
                                + " | Employee: " + t.getEmployeeId()
                                + " | Type: " + t.getTxnType()
                                + " | Qty: " + t.getQuantity()
                                + " | Date: " + t.getTxnDate()
                                + " | Ref: " + t.getReferenceId()
                                + "\n"
                );
            }
        });


        // ================= REPORT ACTIONS =================
        basicStatsButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getBasicStatistics());
        });

        lowStockButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getLowStockProducts());
        });

        expiringButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getExpiringBatches());
        });

        stockValueButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getStockValueByCategory());
        });

        salesSummaryButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getSalesSummary());
        });

        paymentsSummaryButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getPaymentsSummary());
        });
        warehouseCapacityButton.setOnAction(e -> {
            outputArea.clear();
            outputArea.setText(ReportDAO.getWarehouseCapacityReport());
        });

        // ================= LAYOUT =================
        VBox root = new VBox(8);
        root.setPadding(new Insets(15));

        root.getChildren().addAll(
                new Label("Category Section"),
                categoryIdField,
                categoryNameField,
                categoryDescField,
                categoryButtons,

                new Separator(),

                new Label("Product Section"),
                productIdField,
                nameField,
                descriptionField,
                priceField,
                reorderField,
                categoryField,
                productButtons,

                new Separator(),

                new Label("Warehouse Section"),
                warehouseIdField,
                warehouseNameField,
                warehouseAddressField,
                warehouseCityField,
                warehousePhoneField,
                warehouseCapacityField,
                warehouseButtons,

                new Separator(),

                new Label("Batch Section"),
                batchIdField,
                batchProductIdField,
                batchWarehouseIdField,
                batchQuantityField,
                batchExpiryField,
                batchLocationField,
                batchButtons,

                new Separator(),

                new Label("Supplier Section"),
                supplierIdField,
                supplierNameField,
                supplierContactField,
                supplierPhoneField,
                supplierEmailField,
                supplierCityField,
                supplierButtons,

                new Separator(),

                new Label("Client Section"),
                clientIdField,
                clientNameField,
                clientTypeField,
                clientPhoneField,
                clientCityField,
                clientCreditField,
                clientButtons,

                new Separator(),

                new Label("Employee Section"),
                employeeIdField,
                employeeFirstNameField,
                employeeLastNameField,
                employeeRoleField,
                employeeHireDateField,
                employeePhoneField,
                employeeSalaryField,
                employeeButtons,

                new Separator(),

                new Label("Supplier Product Section"),

                spSupplierIdField,
                spProductIdField,
                spUnitCostField,

                supplierProductButtons,

                new Separator(),

                new Label("Purchase Order Section"),

                poSupplierIdField,
                poEmployeeIdField,
                poOrderDateField,
                poDeliveryDateField,
                poStatusField,
                poTotalAmountField,

                poButtons,

                new Separator(),

                new Label("Purchase Order Item Section"),

                poiPONumberField,
                poiProductIdField,
                poiQtyField,
                poiUnitCostField,
                poiReceivedField,

                poItemButtons,

                new Separator(),

                new Label("Sale Order Section"),
                soClientIdField,
                soEmployeeIdField,
                soOrderDateField,
                soDeliveryDateField,
                soStatusField,
                soTotalAmountField,
                soPaymentStatusField,
                soButtons,

                new Separator(),

                new Label("Sale Order Item Section"),
                soiSaleOrderIdField,
                soiBatchIdField,
                soiQtyField,
                soiUnitPriceField,
                soiDiscountField,
                soItemButtons,

                new Separator(),

                new Label("Payment Section"),
                paymentIdField,
                paymentSaleOrderIdField,
                paymentPONumberField,
                paymentDateField,
                paymentAmountField,
                paymentMethodField,
                paymentDirectionField,
                paymentButtons,

                new Separator(),

                new Label("Inventory Transaction Section"),
                txnBatchIdField,
                txnEmployeeIdField,
                txnTypeField,
                txnQuantityField,
                txnReferenceIdField,
                txnButtons,

                new Separator(),

                new Label("Reports and Statistics Section"),
                reportButtons1,
                reportButtons2,

                outputArea
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 850, 850);
        stage.setTitle("Health Plus Warehouse Management Module");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}