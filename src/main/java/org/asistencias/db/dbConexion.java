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
        String url = "jdbc:mysql://" + env("DB_HOST", "localhost") + ":" + env("DB_PORT", "3306")
                + "/" + env("DB_NAME", "asistencia_db");
        return DriverManager.getConnection(url, env("DB_USER", "root"), env("DB_PASS", ""));
    }
}
