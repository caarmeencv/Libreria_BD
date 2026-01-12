package dao;

import dto.AutorDTO;
import factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    // Verifica si un email ya existe en la base de datos, para evitar duplicados
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

    // Verifica si un nombre de autor ya existe en la base de datos, para evitar duplicados
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

    // Crea un nuevo autor y asigna el ID generado al DTO
    public static void create(AutorDTO nuevo) {
        String sql = "INSERT INTO Autor (Email, Nombre_Autor) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nuevo.getEmail());
            ps.setString(2, nuevo.getNombre_Autor());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    nuevo.setID_Autor(idGenerado);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Crea múltiples autores en batch, retornando un array con los resultados de cada inserción, donde cada elemento indica el número de filas afectadas por esa inserción
    // Si una inserción falla, el valor correspondiente en el array será Statement.EXECUTE_FAILED
    public static int[] createBatch(List<AutorDTO> autores) {
        String sql = "INSERT INTO Autor (Nombre_Autor, Email) VALUES (?, ?)";

        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

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

    // Busca un autor por su ID y retorna un AutorDTO con los datos encontrados, o null si no se encuentra
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

    // Lista todos los autores en la base de datos y retorna una lista de AutorDTO
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

    // Actualiza los datos de un autor existente, retornando true si la actualización fue exitosa
    public static boolean actualizar(AutorDTO autor) {
        String sql = "UPDATE Autor SET Email = ?, Nombre_Autor = ? WHERE ID_Autor = ?";
        try (Connection con = ConnectionFactory.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getEmail());
            ps.setString(2, autor.getNombre_Autor());
            ps.setInt(3, autor.getID_Autor());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina un autor por su ID, retornando true si la eliminación fue exitosa
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

    // Busca autores cuyo nombre contenga una palabra clave específica e imprime los resultados
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

    // Retorna una lista de títulos de libros escritos por un autor específico, dado su ID
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
