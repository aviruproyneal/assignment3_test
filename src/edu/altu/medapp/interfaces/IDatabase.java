package edu.altu.medapp.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {
    Connection getConnection() throws SQLException;
}