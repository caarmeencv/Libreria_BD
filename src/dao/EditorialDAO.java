package dao;

import dto.EditorialDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAO {

    // Método para verificar si existe una editorial por nombre, para evitar duplicados
    public static boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Editorial WHERE Nombre_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    // Método para crear una nueva editorial, devolviendo el ID autoincremental generado
    public static void create(EditorialDTO nuevo) {
        String sql = "INSERT INTO Editorial(Nombre_Editorial) VALUES (?)";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nuevo.getNombre_Editorial());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    nuevo.setID_Editorial(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar una editorial por su ID
    public static EditorialDTO buscarPorId(int id) {
        String sql = "SELECT * FROM Editorial WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EditorialDTO editorial = new EditorialDTO();
                    editorial.setID_Editorial(rs.getInt("ID_Editorial"));
                    editorial.setNombre_Editorial(rs.getString("Nombre_Editorial"));
                    return editorial;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para listar todas las editoriales
    public static List<EditorialDTO> listarTodos() {
        List<EditorialDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Editorial";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EditorialDTO editorial = new EditorialDTO();
                editorial.setID_Editorial(rs.getInt("ID_Editorial"));
                editorial.setNombre_Editorial(rs.getString("Nombre_Editorial"));
                lista.add(editorial);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método para actualizar una editorial existente
    public static void actualizar(EditorialDTO editorial) {
        String sql = "UPDATE Editorial SET Nombre_Editorial = ? WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombre_Editorial());
            ps.setInt(2, editorial.getID_Editorial());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para contar el número de libros asociados a una editorial
    public static int contarLibros(int idEditorial) {
        String sql = "SELECT COUNT(*) FROM Libro WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idEditorial;
    }

    // Método para eliminar una editorial por su ID
    public static boolean eliminar(int id) {
        String sql = "DELETE FROM Editorial WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para mostrar libros asociados a una editorial, por su ID
    public static List<String> librosPorEditorial(int idEditorial) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT Titulo FROM Libro WHERE ID_Editorial = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEditorial);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString("Titulo"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
