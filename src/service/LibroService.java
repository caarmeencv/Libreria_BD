package service;

import dao.EditorialDAO;
import dao.LibroDAO;
import dto.LibroDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import utils.Validation;

public class LibroService {

    private static final LibroDAO dao = new LibroDAO();
    private static final EditorialDAO editorialDAO = new EditorialDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void menu() {
        int op;
        do {
            System.out.println("\nMENÚ DE LIBROS");
            System.out.println("1. Crear libro.");
            System.out.println("2. Consultar libro por ID.");
            System.out.println("3. Listar todos los libros.");
            System.out.println("4. Modificar un libro por ID.");
            System.out.println("5. Eliminar un libro por ID.");
            System.out.println("6. Buscar autor o libro por palabra clave.");
            System.out.println("7. Relacionar autor con libro.");
            System.out.println("8. Mostrar autores de un libro.");
            System.out.println("9. Eliminar la relación entre un autor y un libro.");
            System.out.println("10. Volver al menú principal.");
            op = sc.nextInt();
            sc.nextLine();

            try {
                switch (op) {
                    case 1:
                        crear();
                        break;
                    case 2:
                        consultar();
                        break;
                    case 3:
                        listar();
                        break;
                    case 4:
                        modificar();
                        break;
                    case 5:
                        eliminar();
                        break;
                    case 6:
                        relacionar();
                        break;
                    case 7:
                        eliminarRelacion();
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void crear() throws SQLException {
        if (editorialDAO.listarTodos().isEmpty()) {
            System.out.println("No existen editoriales. Cree una primero.");
            return;
        }

        System.out.print("ISBN: ");
        String isbn = sc.nextLine();
        if (!Validation.validarISBN(isbn)) {
            return;
        }

        System.out.print("Título: ");
        String titulo = sc.nextLine();
        if (!Validation.validarTituloLibro(titulo)) {
            return;
        }

        System.out.print("ID Editorial: ");
        int idEditorial = sc.nextInt();

        dao.insertar(new LibroDTO(0, isbn, titulo), idEditorial);
        System.out.println("Libro creado correctamente.");
    }

    private static void consultar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        LibroDTO l = dao.buscarPorId(id);
        if (l == null) {
            System.out.println("No existe ese libro.");
            return;
        }

        System.out.println(l);
        dao.autoresPorLibro(id).forEach(a -> System.out.println("Autor: " + a));
    }

    private static void listar() throws SQLException {
        List<LibroDTO> lista = dao.listarTodos();
        lista.forEach(l -> System.out.println(l.getID_Libro() + " - " + l.getTitulo()));
    }

    private static void modificar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nuevo ISBN: ");
        String isbn = sc.nextLine();
        if (!Validation.validarISBN(isbn)) {
            return;
        }

        System.out.print("Nuevo título: ");
        String titulo = sc.nextLine();
        if (!Validation.validarTituloLibro(titulo)) {
            return;
        }

        dao.actualizar(new LibroDTO(id, isbn, titulo));
        System.out.println("Libro modificado.");
    }

    private static void eliminar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        if (dao.eliminar(id)) {
            System.out.println("Libro eliminado.");
        } else {
            System.out.println("No existe.");
        }
    }

    private static void relacionar() throws SQLException {
        System.out.print("ID Libro: ");
        int idLibro = sc.nextInt();
        System.out.print("ID Autor: ");
        int idAutor = sc.nextInt();

        dao.relacionarAutor(idLibro, idAutor);
        System.out.println("Relación creada.");
    }

    private static void eliminarRelacion() throws SQLException {
        System.out.print("ID Libro: ");
        int idLibro = sc.nextInt();
        System.out.print("ID Autor: ");
        int idAutor = sc.nextInt();

        dao.eliminarRelacion(idLibro, idAutor);
        System.out.println("Relación eliminada.");
    }
}
