package service;

import dao.AutorDAO;
import dao.EditorialDAO;
import dao.LibroDAO;
import dto.EditorialDTO;
import dto.LibroDTO;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import utils.Validation;

public class LibroService {

    //Scanner estático para toda la clase
    private static final Scanner sc = new Scanner(System.in);

    //  Menú de libros
    public static void dameOpcion() {
        int opcion = 0;
        while (opcion != 10) {
            mostrarMenuLibro();

            try {
                opcion = sc.nextInt();
                sc.nextLine();
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
                        buscarPorPalabraClave();
                        break;
                    case 7:
                        relacionar();
                        break;
                    case 8:
                        mostrarAutores();
                        break;
                    case 9:
                        eliminarRelacion();
                        break;
                    case 10:
                        System.out.println("Volviendo al menú principal...");
                    default:
                        System.out.println("Opción no válida. Por favor, elige una opción del 1 al 10.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduce un número entre 1 y 10.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Mostrar el menú de libros
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

    // Crear un nuevo libro
    public static void crear() {
        String isbn, titulo;

        //si no hay editoriales, no dejar continuar
        if (EditorialDAO.listarTodos().isEmpty()) {
            System.out.println("No hay editoriales para asignar al libro. Crea una editorial primero.");
            return;
        }

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

        //mostrar las editoriales disponibles
        System.out.println("Editoriales disponibles:");
        List<EditorialDTO> editoriales = EditorialDAO.listarTodos();
        for (EditorialDTO editorial : editoriales) {
            System.out.println(editorial);
        }
        while (true) {
            System.out.print("Introduce ID de la editorial del nuevo libro: ");
            idEditorial = sc.nextInt();
            if (EditorialDAO.buscarPorId(idEditorial) != null) {
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

    // Consultar un libro por su ID
    public static void consultar() {
        //Si no hay libros, no dejar continuar
        if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros para consultar.");
            return;
        }
        System.out.print("Introduce la ID del libro a consultar: ");
        int id = sc.nextInt();

        try {
            LibroDTO libro = LibroDAO.buscarPorId(id);

            if (libro == null) {
                System.out.println("No existe un libro con ID " + id);
                return;
            }

            System.out.println(libro);

        } catch (Exception e) {
            System.out.println("Error al consultar el libro con ID " + id + ".");
            e.printStackTrace();
        }
    }

    // Listar todos los libros
    public static void listar() {
        //si no hay libros, no dejar continuar
        if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros para listar.");
            return;
        }
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

    // Modificar un libro por su ID
    public static void modificar() {
        try {
            System.out.println("Lista de libros:");
            // Mostrar todos los libros
            listar();
            //si no hay libros, no dejar continuar
            if (LibroDAO.listarTodos().isEmpty()) {
                return;
            }

            int idLibro;
            LibroDTO libro;

            // Pedir un ID válido
            while (true) {
                System.out.print("Introduce el ID del libro a modificar: ");
                idLibro = sc.nextInt();
                sc.nextLine();

                libro = LibroDAO.buscarPorId(idLibro);
                if (libro != null) {
                    break;
                } else {
                    System.out.println("No existe ningún libro con ese ID. Intenta de nuevo.");
                }
            }

            String nuevoISBN;

            //Comprobar la validez del ISBN con Validation.validarISBN
            while (true) {
                System.out.println("ISBN actual: " + libro.getISBN());
                System.out.print("Introduce nuevo ISBN (Enter para mantener actual): ");
                nuevoISBN = sc.nextLine();

                if (nuevoISBN.isEmpty()) {
                    nuevoISBN = libro.getISBN();
                    break;
                }

                if (Validation.validarISBN(nuevoISBN)) {
                    break;
                } else {
                    System.out.println("ISBN no válido. Intenta de nuevo.");
                }
            }

            String nuevoTitulo;

            //Comprobar la validez del título con Validation.validarTituloLibro
            while (true) {
                System.out.println("Título actual: " + libro.getTitulo());
                System.out.print("Introduce nuevo título (Enter para mantener actual): ");
                nuevoTitulo = sc.nextLine();

                if (nuevoTitulo.isEmpty()) {
                    nuevoTitulo = libro.getTitulo();
                    break;
                }

                if (Validation.validarTituloLibro(nuevoTitulo)) {
                    break;
                } else {
                    System.out.println("Título no válido. Intenta de nuevo.");
                }
            }

            int nuevaEditorialId;
            List<EditorialDTO> editoriales = EditorialDAO.listarTodos();

            //si no hay editoriales, no dejar continuar
            if (editoriales.isEmpty()) {
                System.out.println("No hay editoriales disponibles. Debes crear una editorial primero.");
                return;
            }

            System.out.println("Editoriales disponibles:");
            for (EditorialDTO e : editoriales) {
                System.out.println("ID: " + e.getID_Editorial() + " | Nombre: " + e.getNombre_Editorial());
            }

            //Pedir un id de una editorial existente
            while (true) {
                System.out.print("Introduce ID de la nueva editorial (Enter para mantener actual): ");
                String input = sc.nextLine();

                if (input.isEmpty()) {
                    nuevaEditorialId = libro.getID_Editorial();
                    break;
                }

                try {
                    nuevaEditorialId = Integer.parseInt(input);
                    if (EditorialDAO.buscarPorId(nuevaEditorialId) != null) {
                        break;
                    } else {
                        System.out.println("No existe una editorial con ID " + nuevaEditorialId + ". Intenta de nuevo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Introduce un número válido.");
                }
            }

            libro.setISBN(nuevoISBN);
            libro.setTitulo(nuevoTitulo);
            libro.setID_Editorial(nuevaEditorialId);

            LibroDAO.actualizar(libro);
            System.out.println("Libro modificado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al modificar el libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Eliminar un libro por su ID
    public static void eliminar() {
        System.out.println("Lista de libros:");
        listar();
        //si no hay libros, no dejar continuar
        if (LibroDAO.listarTodos().isEmpty()) {
            return;
        }
        System.out.print("Introduce la ID del libro a eliminar: ");
        int id = sc.nextInt();

        try {
            boolean eliminado = LibroDAO.eliminar(id);
            if (eliminado) {
                System.out.println("Libro eliminado.");
            } else {
                System.out.println("No existe ningún libro con ID " + id);
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el libro.");
            e.printStackTrace();
        }
    }

    // Buscar libros y autores por palabra clave
    public static void buscarPorPalabraClave() {

        if (LibroDAO.listarTodos().isEmpty() && AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros ni autores para buscar.");
            return;
        }
        System.out.print("Introduce la palabra clave para buscar en libros y autores: ");
        String palabraClave = sc.nextLine();

        try {

            System.out.println("Resultados en libros:");
            LibroDAO.buscarLibrosPorPalabraClave(palabraClave);
            System.out.println("Resultados en autores:");
            AutorDAO.buscarAutoresPorPalabraClave(palabraClave);

        } catch (Exception e) {
            System.out.println("Error al buscar por palabra clave.");
            e.printStackTrace();
        }

    }

    // Mostrar autores de un libro específico
    public static void mostrarAutores() {
        //si no hay autores ni libros, no dejar continuar

        if (LibroDAO.listarTodos().isEmpty() && AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros ni autores para mostrar.");
            return;
        } else if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros para mostrar sus autores.");
            return;
        } else if (AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay autores para mostrar.");
            return;
        }

        //Mostrar libros disponibles
        System.out.println("Libros disponibles:");
        listar();
        System.out.print("Introduce la ID del libro para mostrar sus autores: ");
        int idLibro = sc.nextInt();

        try {
            List<String> autores = LibroDAO.autoresPorLibro(idLibro);
            if (autores.isEmpty()) {
                System.out.println("El libro con ID " + idLibro + " no tiene autores asociados.");
                return;
            }

            System.out.println("Autores del libro con ID " + idLibro + ":");
            for (String autor : autores) {
                System.out.println(autor);
            }

        } catch (Exception e) {
            System.out.println("Error al mostrar los autores del libro con ID " + idLibro + ".");
            e.printStackTrace();
        }
    }

    // Relacionar un autor con un libro
    public static void relacionar() {
        //dice que no hay libros o autores para relacionar
        if (LibroDAO.listarTodos().isEmpty() && AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros ni autores para relacionar.");
            return;
        } else if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros para relacionar.");
            return;
        } else if (AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay autores para relacionar.");
            return;
        }
        try {
            //mostrar libros y autores disponibles
            System.out.println("Libros disponibles:");
            List<LibroDTO> libros = LibroDAO.listarTodos();
            for (LibroDTO libro : libros) {
                System.out.println(libro);
            }
            System.out.print("ID Libro: ");
            int idLibro = sc.nextInt();
            System.out.println("Autores disponibles:");
            List<dto.AutorDTO> autores = AutorDAO.listarTodos();
            for (dto.AutorDTO autor : autores) {
                System.out.println(autor);
            }
            System.out.print("ID Autor: ");
            int idAutor = sc.nextInt();

            //si ya existe la relación, no dejar continuar
            if (LibroDAO.existeRelacion(idLibro, idAutor)) {
                System.out.println("Ya existe una relación entre el libro con ID " + idLibro + " y el autor con ID " + idAutor + ".");
                return;
            }

            LibroDAO.relacionarAutor(idLibro, idAutor);
            System.out.println("Relación entre el autor con ID " + idAutor + " y el libro con ID " + idLibro + " creada.");

        } catch (Exception e) {
            System.out.println("Error al relacionar autor y libro.");
            e.printStackTrace();
        }
    }

    // Eliminar la relación entre un autor y un libro
    public static void eliminarRelacion() {
        //dice que no hay libros o autores para eliminar la relación
        if (LibroDAO.listarTodos().isEmpty() && AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros ni autores para eliminar la relación.");
            return;
        } else if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros para eliminar la relación.");
            return;
        } else if (AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay autores para eliminar la relación.");
            return;
        }
        try {
            //mostrar libros y autores disponibles
            System.out.println("Libros disponibles:");
            List<LibroDTO> libros = LibroDAO.listarTodos();
            for (LibroDTO libro : libros) {
                System.out.println(libro);
            }

            System.out.print("ID Libro: ");
            int idLibro = sc.nextInt();

            System.out.println("Autores disponibles:");
            List<dto.AutorDTO> autores = AutorDAO.listarTodos();
            for (dto.AutorDTO autor : autores) {
                System.out.println(autor);
            }
            System.out.print("ID Autor: ");
            int idAutor = sc.nextInt();

            //si no existe la relación, no dejar continuar
            if (!LibroDAO.existeRelacion(idLibro, idAutor)) {
                System.out.println("No existe una relación entre el libro con ID " + idLibro + " y el autor con ID " + idAutor + ".");
                return;
            }

            LibroDAO.eliminarRelacion(idLibro, idAutor);
            System.out.println("Relación entre el autor con ID " + idAutor + " y el libro con ID " + idLibro + " eliminada.");

        } catch (Exception e) {
            System.out.println("Error al eliminar la relación entre autor y libro.");
            e.printStackTrace();
        }
    }
}
