package org.asistencias.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class dbConexion {

    private static final Map<String, String> ENV = cargarEnv();

    private static Map<String, String> cargarEnv() {
        Map<String, String> mapa = new HashMap<>();
        Path archivo = Paths.get(".env");
        if (!Files.exists(archivo)) return mapa;
        try {
            for (String linea : Files.readAllLines(archivo)) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                int i = linea.indexOf('=');
                if (i > 0) mapa.put(linea.substring(0, i).trim(), linea.substring(i + 1).trim());
            }
        } catch (Exception ignorado) {
        }
        return mapa;
    }

    private static String env(String clave, String porDefecto) {
        String valor = System.getenv(clave);
        if (valor == null) valor = ENV.get(clave);
        return valor != null ? valor : porDefecto;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + env("DB_HOST", "localhost") + ":" + env("DB_PORT", "3306")
                   + "/" + env("DB_NAME", "asistencia_db");
        return DriverManager.getConnection(url, env("DB_USER", "root"), env("DB_PASS", ""));
    }
}
