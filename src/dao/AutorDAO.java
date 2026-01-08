package dao;

import dto.AutorDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public static boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Autor WHERE Email = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Autor WHERE Nombre_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public void insertar(AutorDTO autor) throws SQLException {
        String sql = "INSERT INTO Autor (Email, Nombre_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getEmail());
            ps.setString(2, autor.getNombre_Autor());
            ps.executeUpdate();
        }
    }

    public void insertarBatch(List<AutorDTO> autores) throws SQLException {
        String sql = "INSERT INTO Autor (Email, Nombre_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);
            for (AutorDTO a : autores) {
                ps.setString(1, a.getEmail());
                ps.setString(2, a.getNombre_Autor());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
        }
    }

    public AutorDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Autor WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new AutorDTO(
                        rs.getInt("ID_Autor"),
                        rs.getString("Email"),
                        rs.getString("Nombre_Autor")
                );
            }
        }
        return null;
    }

    public List<AutorDTO> listarTodos() throws SQLException {
        List<AutorDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Autor";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new AutorDTO(
                        rs.getInt("ID_Autor"),
                        rs.getString("Email"),
                        rs.getString("Nombre_Autor")
                ));
            }
        }
        return lista;
    }

    public void actualizar(AutorDTO autor) throws SQLException {
        String sql = "UPDATE Autor SET Email = ?, Nombre_Autor = ? WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getEmail());
            ps.setString(2, autor.getNombre_Autor());
            ps.setInt(3, autor.getID_Autor());
            ps.executeUpdate();
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Autor WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<String> librosPorAutor(int idAutor) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT l.Titulo FROM Libro l JOIN Libro_Autor la ON l.ID_Libro = la.ID_Libro WHERE la.ID_Autor = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString(1));
        }
        return lista;
    }
}
