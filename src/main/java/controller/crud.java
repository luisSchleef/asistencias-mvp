package org.asistencias.controller;

import org.asistencias.db.dbConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class crud {

    // Registrar Entrada y salida
    public void registrar(int usuarioId, String tipo) throws SQLException {
        String sql = "INSERT INTO asistencias (usuario_id, tipo) VALUES (?, ?)";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, tipo);
            ps.executeUpdate();
        }
    }

    // Registra la hora cuando se registra
    private List<String[]> registroPorHora(String tipo, String operador, String hora) throws SQLException {
        String sql = "SELECT u.id, u.nombre, DATE(a.fecha_hora), TIME(a.fecha_hora) "
                   + "FROM asistencias a JOIN usuarios u ON u.id = a.usuario_id "
                   + "WHERE a.tipo = ? AND TIME(a.fecha_hora) " + operador + " ? "
                   + "ORDER BY a.fecha_hora DESC";
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

    public List<String[]> reporteAtrasos() throws SQLException {
        return registroPorHora("ENTRADA", ">", "09:30:00");
    }

    public List<String[]> reporteSalidasAnticipadas() throws SQLException {
        return registroPorHora("SALIDA", "<", "17:30:00");
    }


    public List<String[]> reporteInasistencias() throws SQLException {
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
                    "SELECT DISTINCT usuario_id, DATE(fecha_hora) FROM asistencias");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int uid = rs.getInt(1);
                    LocalDate dia = LocalDate.parse(rs.getString(2));
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

    public List<String[]> listarUsuarios() throws SQLException {
        List<String[]> filas = new ArrayList<>();
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, nombre, correo, rol FROM usuarios ORDER BY id");
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

    public void crearUsuario(String nombre, String correo, String contrasena, String rol) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, contrasena);
            ps.setString(4, rol);
            ps.executeUpdate();
        }
    }

    public void actualizarUsuario(int id, String nombre, String correo, String contrasena, String rol) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol = ?"
                   + (contrasena == null || contrasena.isEmpty() ? "" : ", contrasena = ?")
                   + " WHERE id = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, rol);
            int i = 4;
            if (contrasena != null && !contrasena.isEmpty()) ps.setString(i++, contrasena);
            ps.setInt(i, id);
            ps.executeUpdate();
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
