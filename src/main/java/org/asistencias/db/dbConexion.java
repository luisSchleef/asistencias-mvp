package org.asistencias.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConexion {

    private static String env(String clave, String porDefecto) {
        String valor = System.getenv(clave);
        return valor != null ? valor : porDefecto;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + env("DB_NAME", "asistencia_db");

        return DriverManager.getConnection(url);
    }
}
