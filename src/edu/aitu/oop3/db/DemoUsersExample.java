package edu.altu.oop3.db;

import java.sql.*;

public class DemoUsersExample {
    public static void main(String[] args) {
        System.out.println("Demo: create table, insert, select");
        try (Connection connection = DatabaseConnection.getConnection()) {
            createTableIfNeeded(connection);
            insertUser(connection, "Alice", "alice@example.com");
            insertUser(connection, "Bob", "bob@example.com");
            printAllUsers(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNeeded(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS demo_users (
                id serial primary key,
                name varchar(100) not null,
                email varchar(100) unique not null
            )
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table demo_users is ready.");
        }
    }

    private static void insertUser(Connection connection, String name, String email) throws SQLException {
        String sql = "INSERT INTO demo_users (name, email) VALUES (?, ?) ON CONFLICT (email) DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            System.out.println("Inserted rows: " + rows);
        }
    }

    private static void printAllUsers(Connection connection) throws SQLException {
        String sql = "SELECT id, name, email FROM demo_users ORDER BY id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Current users:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.printf(" %d | %s | %s%n", id, name, email);
            }
        }
    }
}