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
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getISBN());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getID_Editorial());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    libro.setID_Libro(idGenerado);
                }
            }

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

    public static boolean eliminar(int id) {
        //Para eliminar un libro tenemos que eliminar también la relacion entre el libro y el autor en la tabla Libro_Autor(ID_Libro, ID_Autor)
        String sql = "DELETE FROM Libro WHERE ID_Libro = ?";
        String sqlRelaciones = "DELETE FROM Libro_Autor WHERE ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement psRelaciones = con.prepareStatement(sqlRelaciones); PreparedStatement ps = con.prepareStatement(sql)) {

            psRelaciones.setInt(1, id);
            psRelaciones.executeUpdate();

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<LibroDTO> buscarLibrosPorPalabraClave(String palabraClave) {
        String sql = "SELECT * FROM Libro WHERE Titulo LIKE ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + palabraClave + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("ID_Libro: " + rs.getInt("ID_Libro") + ", Titulo: " + rs.getString("Titulo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void relacionarAutor(int idLibro, int idAutor) {
        String sql = "INSERT INTO Libro_Autor (ID_Libro, ID_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void eliminarRelacion(int idLibro, int idAutor) {
        String sql = "DELETE FROM Libro_Autor WHERE ID_Libro = ? AND ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> autoresPorLibro(int idLibro) {
        String sql = "SELECT a.Nombre_Autor FROM Autor a JOIN Libro_Autor la ON a.ID_Autor = la.ID_Autor WHERE la.ID_Libro = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            List<String> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(rs.getString("Nombre_Autor"));
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean existeRelacion(int idLibro, int idAutor) {
        String sql = "SELECT COUNT(*) FROM Libro_Autor WHERE ID_Libro = ? AND ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
