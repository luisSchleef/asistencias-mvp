package db;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class dbConexion {

    private static final String PROPIEDAD_RUTA = "asistencias.db";
    private static final String ENV_RUTA = "ASISTENCIAS_DB";
    private static final String RECURSO_INIT = "/db/init.sql";

    public static String ruta() {
        String porPropiedad = System.getProperty(PROPIEDAD_RUTA);
        if (porPropiedad != null && !porPropiedad.isBlank()) return porPropiedad;
        String porEnv = System.getenv(ENV_RUTA);
        if (porEnv != null && !porEnv.isBlank()) return porEnv;
        return "asistencia_db";
    }

    public static Connection getConnection() throws SQLException {
        String ruta = ruta();
        boolean iniciar = esArchivo(ruta) && !Files.exists(Path.of(ruta));
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
        // PRAGMA es por conexión: activar FKs (y CASCADE) en cada una
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        if (iniciar) ejecutarInit(conn);
        return conn;
    }

    private static boolean esArchivo(String ruta) {
        return ruta != null && !ruta.contains(":memory:");
    }

    private static void ejecutarInit(Connection conn) throws SQLException {
        String sql = leerInit();
        try (Statement st = conn.createStatement()) {
            for (String sentencia : sql.split(";")) {
                String s = sentencia.strip();
                if (!s.isEmpty()) st.execute(s);
            }
        }
    }

    private static String leerInit() throws SQLException {
        try (InputStream in = dbConexion.class.getResourceAsStream(RECURSO_INIT)) {
            if (in == null) {
                throw new SQLException("No se encontró el script de inicialización " + RECURSO_INIT
                        + " en el classpath");
            }
            return new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            throw new SQLException("No se pudo leer " + RECURSO_INIT, e);
        }
    }
}
