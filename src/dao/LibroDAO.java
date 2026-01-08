package dao;

import dto.LibroDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public static boolean existeISBN(String isbn) {
        String sql = "SELECT COUNT(*) FROM Libro WHERE ISBN = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public void insertar(LibroDTO libro, int idEditorial) throws SQLException {
        String sql = "INSERT INTO Libro (ID_Editorial, ISBN, Titulo) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            ps.setString(2, libro.getISBN());
            ps.setString(3, libro.getTitulo());
            ps.executeUpdate();
        }
    }

    public LibroDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Libro WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LibroDTO(
                        rs.getInt("ID_Libro"),
                        rs.getString("ISBN"),
                        rs.getString("Titulo")
                );
            }
        }
        return null;
    }

    public List<LibroDTO> listarTodos() throws SQLException {
        List<LibroDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libro";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new LibroDTO(
                        rs.getInt("ID_Libro"),
                        rs.getString("ISBN"),
                        rs.getString("Titulo")
                ));
            }
        }
        return lista;
    }

    public void actualizar(LibroDTO libro) throws SQLException {
        String sql = "UPDATE Libro SET ISBN = ?, Titulo = ? WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getISBN());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getID_Libro());
            ps.executeUpdate();
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Libro WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ===== RELACIONES =====

    public void relacionarAutor(int idLibro, int idAutor) throws SQLException {
        String sql = "INSERT INTO Libro_Autor (ID_Libro, ID_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();
        }
    }

    public void eliminarRelacion(int idLibro, int idAutor) throws SQLException {
        String sql = "DELETE FROM Libro_Autor WHERE ID_Libro = ? AND ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();
        }
    }

    public List<String> autoresPorLibro(int idLibro) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT a.Nombre_Autor FROM Autor a JOIN Libro_Autor la ON a.ID_Autor = la.ID_AutorWHERE la.ID_Libro = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString(1));
        }
        return lista;
    }
}
