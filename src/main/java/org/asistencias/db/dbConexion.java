package org.asistencias.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConexion {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:asistencia_db");
    }
}
