package edu.altu.medapp.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));

            this.url = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
            this.user = "postgres.slgskrauppciihcotnds";
            this.password = props.getProperty("DB_PASSWORD");

            if (password == null || password.isBlank()) {
                throw new RuntimeException("DB_PASSWORD not set");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}