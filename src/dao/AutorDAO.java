package dao;

import dto.AutorDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public static boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Autor WHERE Email = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM Autor WHERE Nombre_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public static void create(AutorDTO nuevo) {
        String sql = "INSERT INTO Autor (Email, Nombre_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevo.getEmail());
            ps.setString(2, nuevo.getNombre_Autor());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] createBatch(List<AutorDTO> autores) {
        String sql = "INSERT INTO Autor (Nombre_Autor, Email) VALUES (?, ?)";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Iniciar transacción

            for (AutorDTO autor : autores) {
                ps.setString(1, autor.getNombre_Autor());
                ps.setString(2, autor.getEmail());
                ps.addBatch();
            }

            int[] resultado = ps.executeBatch();
            con.commit();
            return resultado;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new int[0];
    }

    public static AutorDTO buscarPorId(int id) {
        String sql = "SELECT * FROM Autor WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AutorDTO autor = new AutorDTO();
                    autor.setID_Autor(rs.getInt("ID_Autor"));
                    autor.setEmail(rs.getString("Email"));
                    autor.setNombre_Autor(rs.getString("Nombre_Autor"));
                    return autor;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<AutorDTO> listarTodos() {
        List<AutorDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Autor";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AutorDTO autor = new AutorDTO();
                autor.setID_Autor(rs.getInt("ID_Autor"));
                autor.setEmail(rs.getString("Email"));
                autor.setNombre_Autor(rs.getString("Nombre_Autor"));
                lista.add(autor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean actualizar(AutorDTO autor) {
        String sql = "UPDATE Autor SET Email = ?, Nombre_Autor = ? WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getEmail());
            ps.setString(2, autor.getNombre_Autor());
            ps.setInt(3, autor.getID_Autor());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean eliminar(int id) {
        //Al eliminar un autor, eliminar también sus asociaciones en la tabla Libro_Autor
        String sqlLibroAutor = "DELETE FROM Libro_Autor WHERE ID_Autor = ?";
        String sqlAutor = "DELETE FROM Autor WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement psLibroAutor = con.prepareStatement(sqlLibroAutor); PreparedStatement psAutor = con.prepareStatement(sqlAutor)) {

            con.setAutoCommit(false);

            psLibroAutor.setInt(1, id);
            psLibroAutor.executeUpdate();

            psAutor.setInt(1, id);
            int filasAfectadas = psAutor.executeUpdate();

            con.commit();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void buscarAutoresPorPalabraClave(String palabraClave) {
        String sql = "SELECT * FROM Autor WHERE Nombre_Autor LIKE ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + palabraClave + "%");
            ResultSet rs = ps.executeQuery();

            System.out.println("Autores encontrados:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ID_Autor")
                        + ", Nombre: " + rs.getString("Nombre_Autor"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> librosPorAutor(int idAutor) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT L.Titulo FROM Libro L "
                + "JOIN Libro_Autor LA ON L.ID_Libro = LA.ID_Libro "
                + "WHERE LA.ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAutor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("Titulo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
