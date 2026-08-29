package controller;

import db.dbConexion;
import model.user;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login {

    public static user autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT u.id, u.nombre, r.nombre AS rol, u.contrasena "
                   + "FROM usuarios u JOIN roles r ON r.id = u.rol_id WHERE u.correo = ?";
        try (Connection conn = dbConexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String almacenada = rs.getString("contrasena");
                if (almacenada == null) return null;
                try {
                    if (BCrypt.checkpw(contrasena, almacenada)) {
                        return new user(rs.getInt("id"), rs.getString("nombre"), rs.getString("rol"));
                    }
                } catch (IllegalArgumentException e) {
                }
                return null;
            }
        }
    }
}
