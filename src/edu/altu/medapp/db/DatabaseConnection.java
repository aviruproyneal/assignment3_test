package edu.altu.medapp.db;

import edu.altu.medapp.interfaces.IDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection implements IDatabase {
    private static final String URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.slgskrauppciihcotnds";
    private static final String PASSWORD = loadPassword();

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

    private static class Holder {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}