package dao;

import dto.LibroDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public static boolean existeISBN(String isbn) {
        String sql = "SELECT COUNT(*) FROM Libro WHERE ISBN = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public static void create(LibroDTO libro) {
        String sql = "INSERT INTO Libro (ISBN, Titulo, ID_Editorial) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getISBN());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getID_Editorial());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LibroDTO buscarPorId(int id) {
        String sql = "SELECT * FROM Libro WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LibroDTO libro = new LibroDTO();
                libro.setID_Libro(rs.getInt("ID_Libro"));
                libro.setISBN(rs.getString("ISBN"));
                libro.setTitulo(rs.getString("Titulo"));
                libro.setID_Editorial(rs.getInt("ID_Editorial"));
                return libro;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<LibroDTO> listarTodos() {
        List<LibroDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Libro";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LibroDTO libro = new LibroDTO();
                libro.setID_Libro(rs.getInt("ID_Libro"));
                libro.setISBN(rs.getString("ISBN"));
                libro.setTitulo(rs.getString("Titulo"));
                libro.setID_Editorial(rs.getInt("ID_Editorial"));
                lista.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void actualizar(LibroDTO libro) {
        String sql = "UPDATE Libro SET ISBN = ?, Titulo = ?, ID_Editorial = ? WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getISBN());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getID_Editorial());
            ps.setInt(4, libro.getID_Libro());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Libro WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void relacionarAutor(int idLibro, int idAutor) throws SQLException {
        String sql = "INSERT INTO Libro_Autor (ID_Libro, ID_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();
        }
    }

    public void eliminarRelacion(int idLibro, int idAutor) throws SQLException {
        String sql = "DELETE FROM Libro_Autor WHERE ID_Libro = ? AND ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();
        }
    }

    public List<String> autoresPorLibro(int idLibro) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT a.Nombre_Autor FROM Autor a JOIN Libro_Autor la ON a.ID_Autor = la.ID_AutorWHERE la.ID_Libro = ?";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString(1));
            }
        }
        return lista;
    }
}
