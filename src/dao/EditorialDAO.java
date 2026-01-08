package dao;

import dto.EditorialDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {

    public static boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Editorial WHERE Nombre_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public void insertar(EditorialDTO editorial) throws SQLException {
        String sql = "INSERT INTO Editorial (Nombre_Editorial) VALUES (?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombre_Editorial());
            ps.executeUpdate();
        }
    }

    public EditorialDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Editorial WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new EditorialDTO(
                        rs.getInt("ID_Editorial"),
                        rs.getString("Nombre_Editorial")
                );
            }
        }
        return null;
    }

    public List<EditorialDTO> listarTodos() throws SQLException {
        List<EditorialDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Editorial";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EditorialDTO(
                        rs.getInt("ID_Editorial"),
                        rs.getString("Nombre_Editorial")
                ));
            }
        }
        return lista;
    }

    public void actualizar(EditorialDTO editorial) throws SQLException {
        String sql = "UPDATE Editorial SET Nombre_Editorial = ? WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombre_Editorial());
            ps.setInt(2, editorial.getID_Editorial());
            ps.executeUpdate();
        }
    }

    public int contarLibros(int idEditorial) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Libro WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Editorial WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
