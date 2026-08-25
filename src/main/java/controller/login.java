package controller;

import db.dbConexion;
import model.user;

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
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new user(rs.getInt("id"), rs.getString("nombre"), rs.getString("rol"));
                }
                return null;
            }
        }
    }
}
