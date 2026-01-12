package dao;

import dto.LibroDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // Verifica si un libro con el mismo ISBN ya existe en la base de datos, para evitar duplicados
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

    // Crea un nuevo libro en la base de datos y asigna el ID generado al objeto LibroDTO
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

    // Busca un libro por su ID y devuelve un objeto LibroDTO con los datos encontrados
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

    // Lista todos los libros en la base de datos y devuelve una lista de objetos LibroDTO
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

    //  Actualiza los datos de un libro existente en la base de datos
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

    // Elimina un libro de la base de datos, incluyendo sus relaciones con autores
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

    // Busca libros cuyo título contenga una palabra clave y los imprime en consola
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

    // Métodos para gestionar la relación entre libros y autores
    // Crea una relación entre un libro y un autor en la tabla Libro_Autor
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

    // Elimina la relación entre un libro y un autor en la tabla Libro_Autor
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

    // Devuelve una lista de nombres de autores asociados a un libro específico
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

    // Verifica si existe una relación entre un libro y un autor en la tabla Libro_Autor, devuelve true si existe, false en caso contrario
    // Esto es útil para evitar duplicados al crear relaciones
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
