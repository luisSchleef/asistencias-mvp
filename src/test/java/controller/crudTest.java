package controller;

import db.dbConexion;
import model.user;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Usa una BD temporal por método vía la propiedad asistencias.db de dbConexion
class crudTest {

    @TempDir
    static Path tempDir;

    static Path bd;

    @BeforeAll
    static void apuntarBdATemporal() {
        bd = tempDir.resolve("asistencia_test_db");
        System.setProperty("asistencias.db", bd.toString());
    }

    @AfterAll
    static void limpiarPropiedad() {
        System.clearProperty("asistencias.db");
    }

    @BeforeEach
    void bdLimpia() throws Exception {
        Files.deleteIfExists(bd);
    }

    private user admin() {
        return new user(1, "admin", "ADMIN");
    }

    private user empleado() {
        return new user(2, "pepe", "EMPLEADO");
    }

    @Test
    void iniciarBdDesdeCero() throws Exception {
        try (var conn = dbConexion.getConnection()) {
            var rs = conn.createStatement().executeQuery(
                    "SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name IN ('usuarios','roles','tipos_asistencia','asistencias')");
            rs.next();
            assertEquals(4, rs.getInt(1));
        }
    }

    @Test
    void crearYAutenticarUsuario() throws Exception {
        crud c = new crud();
        c.crearUsuario(admin(), "Ana", "ana@empresa.cl", "secreto", "EMPLEADO");
        assertNotNull(login.autenticar("ana@empresa.cl", "secreto"));
        assertEquals(null, login.autenticar("ana@empresa.cl", "otra"));
    }

    @Test
    void contrasenaSeGuardaHasheada() throws Exception {
        crud c = new crud();
        c.crearUsuario(admin(), "Ana", "ana@empresa.cl", "secreto", "EMPLEADO");
        try (var conn = dbConexion.getConnection()) {
            var rs = conn.createStatement().executeQuery(
                    "SELECT contrasena FROM usuarios WHERE correo = 'ana@empresa.cl'");
            rs.next();
            String hash = rs.getString(1);
            assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
            assertFalse(hash.contains("secreto"));
        }
    }

    @Test
    void correoDuplicadoLanzaError() throws Exception {
        crud c = new crud();
        c.crearUsuario(admin(), "Ana", "ana@empresa.cl", "secreto", "EMPLEADO");
        assertThrows(SQLException.class,
                () -> c.crearUsuario(admin(), "Ana 2", "ana@empresa.cl", "otra", "EMPLEADO"));
    }

    @Test
    void rolInsuficienteNoPermiteAdministrar() throws Exception {
        crud c = new crud();
        assertThrows(SecurityException.class, () -> c.listarUsuarios(empleado()));
        assertThrows(SecurityException.class, () -> c.reporteAtrasos(empleado()));
        assertThrows(SecurityException.class, () -> c.reporteInasistencias(empleado()));
        assertThrows(SecurityException.class,
                () -> c.crearUsuario(empleado(), "X", "x@empresa.cl", "x", "EMPLEADO"));
        assertThrows(SecurityException.class, () -> c.eliminarUsuario(empleado(), 99));
    }

    @Test
    void eliminarUsuarioEliminaSusAsistencias() throws Exception {
        crud c = new crud();
        c.crearUsuario(admin(), "Ana", "ana@empresa.cl", "secreto", "EMPLEADO");
        List<String[]> usuarios = c.listarUsuarios(admin());
        int idAna = Integer.parseInt(usuarios.get(usuarios.size() - 1)[0]);
        c.registrar(idAna, "ENTRADA");
        assertFalse(c.listarAsistencias(admin()).isEmpty());
        c.eliminarUsuario(admin(), idAna);
        assertTrue(c.listarAsistencias(admin()).isEmpty());
    }

    @Test
    void inasistenciasSinDatosVacio() throws Exception {
        crud c = new crud();
        assertTrue(c.reporteInasistencias(admin()).isEmpty());
    }
}
