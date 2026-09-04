package controller;

import db.dbConexion;
import model.user;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class crud {

    // Valida en la capa de datos, no solo en la UI
    private static void requerirAdmin(user actor) {
        if (actor == null || !"ADMIN".equals(actor.rol())) {
            throw new SecurityException("Se requiere rol ADMIN para esta operación");
        }
    }

    // Registrar Entrada y salida
    public void registrar(int usuarioId, String tipo) throws SQLException {
        String sql = "INSERT INTO asistencias (usuario_id, tipo_id, fecha, hora) VALUES (?, (SELECT id FROM tipos_asistencia WHERE nombre = ?), DATE('now','localtime'), TIME('now','localtime'))";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, tipo);
            ps.executeUpdate();
        }
    }

    // Registra la hora cuando se registra
    private List<String[]> registroPorHora(String tipo, boolean despuesDe, String hora) throws SQLException {
        String operador = despuesDe ? ">" : "<";
        String sql = "SELECT u.id, u.nombre, a.fecha, a.hora "
                   + "FROM asistencias a JOIN usuarios u ON u.id = a.usuario_id "
                   + "JOIN tipos_asistencia t ON t.id = a.tipo_id "
                   + "WHERE t.nombre = ? AND a.hora " + operador + " ? "
                   + "ORDER BY a.fecha DESC, a.hora DESC";
        List<String[]> filas = new ArrayList<>();
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setString(2, hora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new String[]{
                        String.valueOf(rs.getInt(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                    });
                }
            }
        }
        return filas;
    }

    public List<String[]> reporteAtrasos(user actor) throws SQLException {
        requerirAdmin(actor);
        return registroPorHora("ENTRADA", true, "09:30:00");
    }

    public List<String[]> reporteSalidasAnticipadas(user actor) throws SQLException {
        requerirAdmin(actor);
        return registroPorHora("SALIDA", false, "17:30:00");
    }


    public List<String[]> reporteInasistencias(user actor) throws SQLException {
        requerirAdmin(actor);
        Map<Integer, String> usuarios = new LinkedHashMap<>(); // id -> nombre
        Map<Integer, Set<LocalDate>> diasPorUsuario = new HashMap<>();
        Map<Integer, LocalDate> primerDia = new HashMap<>();
        LocalDate inicioGlobal = null;
        LocalDate ayer = LocalDate.now().minusDays(1);

        try (Connection conn = dbConexion.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nombre FROM usuarios ORDER BY id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) usuarios.put(rs.getInt(1), rs.getString(2));
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT DISTINCT usuario_id, fecha FROM asistencias");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int uid = rs.getInt(1);
                    LocalDate dia;
                    try {
                        dia = LocalDate.parse(rs.getString(2));
                    } catch (DateTimeParseException e) {
                        continue; // fecha inválida o corrupta: se ignora en el reporte
                    }
                    diasPorUsuario.computeIfAbsent(uid, k -> new HashSet<>()).add(dia);
                    primerDia.merge(uid, dia, (a, b) -> a.isBefore(b) ? a : b);
                    if (inicioGlobal == null || dia.isBefore(inicioGlobal)) inicioGlobal = dia;
                }
            }
        }

        List<String[]> filas = new ArrayList<>();
        if (inicioGlobal == null) return filas;
        for (Map.Entry<Integer, String> u : usuarios.entrySet()) {
            int uid = u.getKey();
            LocalDate desde = primerDia.getOrDefault(uid, inicioGlobal);
            Set<LocalDate> dias = diasPorUsuario.getOrDefault(uid, Set.of());
            for (LocalDate d = desde; !d.isAfter(ayer); d = d.plusDays(1)) {
                DayOfWeek dw = d.getDayOfWeek();
                if (dw == DayOfWeek.SATURDAY || dw == DayOfWeek.SUNDAY) continue;
                if (!dias.contains(d)) {
                    filas.add(new String[]{String.valueOf(uid), u.getValue(), d.toString()});
                }
            }
        }
        return filas;
    }

    public List<String[]> listarAsistencias(user actor) throws SQLException {
        requerirAdmin(actor);
        String sql = "SELECT a.id, u.nombre, t.nombre, a.fecha, a.hora "
                   + "FROM asistencias a JOIN usuarios u ON u.id = a.usuario_id "
                   + "JOIN tipos_asistencia t ON t.id = a.tipo_id "
                   + "ORDER BY a.fecha DESC, a.hora DESC";
        List<String[]> filas = new ArrayList<>();
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                });
            }
        }
        return filas;
    }

    public List<String[]> listarUsuarios(user actor) throws SQLException {
        requerirAdmin(actor);
        List<String[]> filas = new ArrayList<>();
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT u.id, u.nombre, u.correo, r.nombre FROM usuarios u JOIN roles r ON r.id = u.rol_id ORDER BY u.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                });
            }
        }
        return filas;
    }

    public List<String[]> listarRoles(user actor) throws SQLException {
        requerirAdmin(actor);
        List<String[]> filas = new ArrayList<>();
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM roles ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) filas.add(new String[]{rs.getString(1)});
        }
        return filas;
    }

    public void crearUsuario(user actor, String nombre, String correo, String contrasena, String rol) throws SQLException {
        requerirAdmin(actor);
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol_id) VALUES (?, ?, ?, (SELECT id FROM roles WHERE nombre = ?))";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, BCrypt.hashpw(contrasena, BCrypt.gensalt()));
            ps.setString(4, rol);
            ps.executeUpdate();
        }
    }

    public void actualizarUsuario(user actor, int id, String nombre, String correo, String contrasena, String rol) throws SQLException {
        requerirAdmin(actor);
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol_id = (SELECT id FROM roles WHERE nombre = ?)"
                   + (contrasena == null || contrasena.isEmpty() ? "" : ", contrasena = ?")
                   + " WHERE id = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, rol);
            int i = 4;
            if (contrasena != null && !contrasena.isEmpty()) ps.setString(i++, BCrypt.hashpw(contrasena, BCrypt.gensalt()));
            ps.setInt(i, id);
            ps.executeUpdate();
        }
    }

    public void eliminarUsuario(user actor, int id) throws SQLException {
        requerirAdmin(actor);
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
