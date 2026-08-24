package org.asistencias.controller;

import org.asistencias.db.dbConexion;
import org.asistencias.model.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login {

    public static user autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT id, nombre, rol FROM usuarios WHERE correo = ? AND contrasena = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, sha256(contrasena));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new user(rs.getInt("id"), rs.getString("nombre"), rs.getString("rol"));
                }
                return null;
            }
        }
    }

    static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder(64);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
