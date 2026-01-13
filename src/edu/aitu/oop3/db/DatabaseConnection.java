package edu.altu.oop3.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL =
            "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.slgskrauppciihcotnds";
    private static final String PASSWORD = loadPassword(); // ← Changed!

    private static String loadPassword() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            String password = props.getProperty("DB_PASSWORD");
            if (password == null || password.isBlank()) {
                throw new RuntimeException("DB_PASSWORD is not set in config.properties");
            }
            return password;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load DB_PASSWORD from config.properties", e);
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}