package service;

import dao.EditorialDAO;
import dao.LibroDAO;
import dto.EditorialDTO;
import dto.LibroDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import utils.Validation;

public class LibroService {

    private static final LibroDAO dao = new LibroDAO();
    private static final EditorialDAO editorialDAO = new EditorialDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void dameOpcion() {
        int opcion = 0;
        while (opcion != 10) {
            mostrarMenuLibro();
            opcion = sc.nextInt();
            sc.nextLine();

            try {
                switch (opcion) {
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
        }
    }

    public static void mostrarMenuLibro() {
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
    }

    public static void crear() {
        String isbn, titulo;

        //Comprobar la validez del ISBN con Validation.validarISBN
        while (true) {
            System.out.print("Introduce ISBN del nuevo libro: ");
            isbn = sc.nextLine();
            if (Validation.validarISBN(isbn)) {
                break;
            }
        }

        //Comprobar la validez del título con Validation.validarTituloLibro
        while (true) {
            System.out.print("Introduce título del nuevo libro: ");
            titulo = sc.nextLine();
            if (Validation.validarTituloLibro(titulo)) {
                break;
            }
        }

        //Pedir un id de una editorial existente
        int idEditorial;
        while (true) {
            System.out.print("Introduce ID de la editorial del nuevo libro: ");
            idEditorial = sc.nextInt();
            if (editorialDAO.buscarPorId(idEditorial) != null) {
                break;
            } else {
                System.out.println("No existe una editorial con ese ID. Inténtalo de nuevo.");
            }
        }

        LibroDTO nuevo = new LibroDTO();
        nuevo.setISBN(isbn);
        nuevo.setTitulo(titulo);
        nuevo.setID_Editorial(idEditorial);
        LibroDAO.create(nuevo);
        System.out.println("Libro creado con ID: " + nuevo.getID_Libro());
    }

    public static void consultar() {
        System.out.print("Introduce la ID del libro a consultar: ");
        int id = sc.nextInt();

        try {
            LibroDTO libro = LibroDAO.buscarPorId(id);

            if (libro == null) {
                System.out.println("No existe ese libro.");
                return;
            }

            System.out.println(libro);

        } catch (Exception e) {
            System.out.println("Error al consultar el libro.");
            e.printStackTrace();
        }
    }

    public static void listar() {
        try {
            List<LibroDTO> lista = LibroDAO.listarTodos();

            if (lista.isEmpty()) {
                System.out.println("No hay libros registrados.");
                return;
            }

            for (LibroDTO libro : lista) {
                System.out.println(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al listar los libros.");
            e.printStackTrace();
        }  
    }

    public static void modificar() {
        try {
            listar();
            System.out.print("Introduce la ID del libro a modificar: ");
            int id = sc.nextInt();
            sc.nextLine();

            LibroDTO libro = LibroDAO.buscarPorId(id);

            if (libro != null){
                String titulo;
                while (true) {
                    System.out.println("Título actual: " + libro.getTitulo());
                    System.out.print("Introduce el nuevo título del libro (Para mantener nombre actual, no introducir nada): ");
                    titulo = sc.nextLine();
                    //Si el usuario no introduce nada, mantener el título actual
                    if (titulo.isEmpty()) {
                        titulo = libro.getTitulo();
                        break;
                    }

                    if (Validation.validarTituloLibro(titulo)) {
                        break;
                    }

                    System.out.println("Título no válido. Inténtalo de nuevo.");
                }

                String isbn;
                while (true) { 
                    System.out.println("ISBN actual: " + libro.getISBN());
                    System.out.print("Introduce el nuevo ISBN del libro (Para mantener ISBN actual, no introducir nada): ");
                    isbn = sc.nextLine();
                    //Si el usuario no introduce nada, mantener el ISBN actual
                    if (isbn.isEmpty()) {
                        isbn = libro.getISBN();
                        break;
                    }

                    if (Validation.validarISBN(isbn)) {
                        break;
                    }

                    System.out.println("ISBN no válido. Inténtalo de nuevo.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al modificar el libro.");
            e.printStackTrace();
        }
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
