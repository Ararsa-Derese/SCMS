package com.SCMS.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    public final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/SCMS";
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Database() {

    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (user == null || password == null) {
                connection = DriverManager.getConnection(CONNECTION_STRING, "root", "");
            } else {
                connection = DriverManager.getConnection(url, user, password);
            }
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement =  connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public Object[][] getInventory() {
        Object[][] inventory = null;
        try {
            connect();
            ResultSet resultSet = executeQuery("select * from inventory i join products p on i.product_id = p.product_id join suppliers s on p.supplier_id = s.supplier_id;");
            int rowCount = getRowCount(resultSet);
            int columnCount = 12;
            inventory = new Object[rowCount][columnCount];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {

                inventory[i][0] = resultSet.getString("code");
                inventory[i][1] = resultSet.getString("name");
                inventory[i][2] = resultSet.getInt("quantity");
                inventory[i][3] = resultSet.getDouble("price");
                inventory[i][4] = resultSet.getString("unit");
                inventory[i][5] = resultSet.getString("category");
                inventory[i][6] = resultSet.getString("supplier_name");
                inventory[i][7] = resultSet.getString("date_added");
                inventory[i][8] = resultSet.getString("expiry_date");
                inventory[i][9] = resultSet.getString("description");
                inventory[i][10] = resultSet.getString("product_id");
                inventory[i][11] = resultSet.getString("inventory_id");
                i++;
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    private int getRowCount(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.last();
                return resultSet.getRow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addInventory(String product_id, int quantity, String location, String expiry_date) {
        String query = "insert into inventory(product_id, quantity, location, expiry_date) values(" + product_id + ", " + quantity + ", '" + location + "', '" + expiry_date + "');";
        try{
            connect();
            executeUpdate(query);
            disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeInventory(String id) {
        System.out.println("id: "+ id);
        String query = "DELETE FROM inventory WHERE inventory_id='" + id + "';";
        try {
            connect();
            System.out.println(executeUpdate(query));
            disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object[][] getProducts() {
        Object[][] products = null;
        try {
            connect();
            ResultSet resultSet = executeQuery(
                    "select * from products p join suppliers s on p.supplier_id = s.supplier_id;");
            int rowCount = getRowCount(resultSet);
            int columnCount = 9;
            products = new Object[rowCount][columnCount];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {

                products[i][0] = resultSet.getString("code");
                products[i][1] = resultSet.getString("name");
                products[i][2] = resultSet.getDouble("price");
                products[i][3] = resultSet.getString("unit");
                products[i][4] = resultSet.getString("category");
                products[i][5] = resultSet.getString("supplier_name");
                products[i][6] = resultSet.getString("description");
                products[i][7] = resultSet.getString("product_id");
                products[i][8] = resultSet.getString("image");
                i++;
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public boolean addProduct(String name, String code, double price, String unit, String category, String description, String image, String supplier_id) {
        String query = "INSERT INTO products (name, code, price, unit, category, description, image, supplier_id) VALUES('" + name + "', '"
                + code + "', '" + price + "', '" + unit + "', '" + category + "', '" + description + "', '" + image + "', '"
                + supplier_id + "');";
        try {
            connect();
            executeUpdate(query);
            disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeProduct(String id) {
        String query = "DELETE FROM products WHERE product_id='" + id + "';";
        try {
            connect();
            System.out.println(executeUpdate(query));
            disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object[][] getSuppliers() {
        Object[][] suppliers = null;
        try {
            connect();
            ResultSet resultSet = executeQuery(
                    "select * from suppliers;");
            int rowCount = getRowCount(resultSet);
            int columnCount = 8;
            suppliers = new Object[rowCount][columnCount];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {
                suppliers[i][0] = resultSet.getString("supplier_name");
                suppliers[i][1] = resultSet.getString("supplier_address");
                suppliers[i][2] = resultSet.getString("supplier_phone");
                suppliers[i][3] = resultSet.getString("supplier_name");
                suppliers[i][4] = resultSet.getString("supplier_email");
                suppliers[i][5] = resultSet.getString("supplier_id");
                i++;
            }
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}
