package service;

import dao.AutorDAO;
import dao.LibroDAO;
import dto.AutorDTO;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import utils.LeerCSV;
import utils.Validation;

public class AutorService {

    // Scanner estático para toda la clase
    private static final Scanner sc = new Scanner(System.in);

    // Método para mostrar el menú y manejar las opciones
    public static void dameOpcion() {
        int opcion = 0;
        while (opcion != 11) {
            mostrarMenuAutor();

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
                        LibroService.buscarPorPalabraClave();
                        break;
                    case 7:
                        LibroService.relacionar();
                        break;
                    case 8:
                        librosDeAutor();
                        break;
                    case 9:
                        LibroService.eliminarRelacion();
                        break;
                    case 10:
                        cargarDesdeCSV();
                        break;
                    case 11:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige un número del 1 al 11.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduce un número entre 1 y 11.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para mostrar el menú de autores
    public static void mostrarMenuAutor() {
        System.out.println("\nMENÚ DE AUTORES");
        System.out.println("1. Crear autor.");
        System.out.println("2. Consultar un autor por ID.");
        System.out.println("3. Listar todos los autores.");
        System.out.println("4. Modificar un autor por ID.");
        System.out.println("5. Eliminar un autor por ID.");
        System.out.println("6. Buscar libros y autores por una palabra clave.");
        System.out.println("7. Relacionar un autor con un libro.");
        System.out.println("8. Mostrar libros de un autor.");
        System.out.println("9. Eliminar la relación entre un autor y un libro.");
        System.out.println("10. Cargar autores desde CSV.");
        System.out.println("11. Volver al menú principal.");
    }

    // Método para crear un nuevo autor
    public static void crear() {
        String nombre, email;

        //Comprobar validez del nombre con Validation.validarNombreAutor
        while (true) {
            System.out.print("Nombre del autor: ");
            nombre = sc.nextLine();
            if (Validation.validarNombreAutor(nombre)) {
                break;
            }
        }

        //Comprobar validez del email con Validation.validarEmailAutor
        while (true) {
            System.out.print("Email del autor: ");
            email = sc.nextLine();
            if (Validation.validarEmailAutor(email)) {
                break;
            }
        }

        AutorDTO nuevo = new AutorDTO();
        nuevo.setNombre_Autor(nombre);
        nuevo.setEmail(email);
        AutorDAO.create(nuevo);
        System.out.println("Autor creado con ID: " + nuevo.getID_Autor());

    }

    // Método para consultar un autor por ID
    public static void consultar() {
        System.out.print("Introduce la ID del Autor a consultar: ");
        int id = sc.nextInt();

        try {
            AutorDTO autor = AutorDAO.buscarPorId(id);

            if (autor == null) {
                System.out.println("No existe un autor con ID " + id);
                return;
            }

            System.out.println(autor);

        } catch (Exception e) {
            System.out.println("Error al consultar el autor.");
            e.printStackTrace();
        }
    }

    // Método para listar todos los autores
    public static void listar() {
        List<AutorDTO> lista = AutorDAO.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay autores.");
            return;
        }

        System.out.println("Lista de autores:");
        for (AutorDTO a : lista) {
            System.out.println(a);
        }
    }

    // Método para modificar un autor por ID
    public static void modificar() {
        try {
            // Que muestre todos los autores antes de pedir la ID
            listar();
            //si no hay autores, no deja continuar
            if (AutorDAO.listarTodos().isEmpty()) {
                return;
            }
            System.out.print("Introduce la ID del Autor a modificar: ");
            int id = sc.nextInt();
            sc.nextLine();

            AutorDTO autor = AutorDAO.buscarPorId(id);

            if (autor == null) {
                System.out.println("No existe un autor con ID " + id);
                return;
            }

            String nombre, email;

            //Comprobar validez del nombre con Validation.validarNombreAutor
            while (true) {
                System.out.println("Nombre actual del autor: " + autor.getNombre_Autor());
                System.out.println("Introduce el nuevo nombre del autor (dejar en blanco para mantener el nombre actual): ");
                nombre = sc.nextLine();
                if (nombre.isEmpty()) {
                    nombre = autor.getNombre_Autor();
                    break;
                }
                if (Validation.validarNombreAutor(nombre)) {
                    break;
                }
            }

            //Comprobar validez del email con Validation.validarEmailAutor
            while (true) {
                System.out.println("Email actual del autor: " + autor.getEmail());
                System.out.println("Introduce el nuevo email del autor (dejar en blanco para mantener el email actual): ");
                email = sc.nextLine();
                if (email.isEmpty()) {
                    email = autor.getEmail();
                    break;
                }
                if (Validation.validarEmailAutor(email)) {
                    break;
                }
            }

            autor.setNombre_Autor(nombre);
            autor.setEmail(email);

            if (AutorDAO.actualizar(autor)) {
                System.out.println("Autor modificado.");
            } else {
                System.out.println("No se pudo modificar el autor.");
            }

        } catch (Exception e) {
            System.out.println("Error al modificar el autor.");
            e.printStackTrace();
        }

    }

    // Método para eliminar un autor por ID
    public static void eliminar() {
        try {
            // Que muestre todos los autores antes de pedir la ID
            listar();
            //si no hay autores, no deja continuar
            if (AutorDAO.listarTodos().isEmpty()) {
                return;
            }
            System.out.print("Introduce la ID del Autor a eliminar: ");
            int id = sc.nextInt();

            if (AutorDAO.eliminar(id)) {
                System.out.println("Autor eliminado.");
            } else {
                System.out.println("No existe un autor con ID " + id);
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar el autor.");
            e.printStackTrace();
        }
    }

    // Método para mostrar los libros de un autor, dado su ID
    public static void librosDeAutor() {
        //si no hay autores y libros, no deja continuar
        if (AutorDAO.listarTodos().isEmpty() && LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay autores ni libros en la base de datos.");
            return;
        } else if (AutorDAO.listarTodos().isEmpty()) {
            System.out.println("No hay autores en la base de datos.");
            return;
        } else if (LibroDAO.listarTodos().isEmpty()) {
            System.out.println("No hay libros en la base de datos.");
            return;
        }

        //Que muestre todos los autores antes de pedir la ID
        listar();

        try {
            System.out.print("Introduce la ID del Autor para mostrar sus libros: ");
            int id = sc.nextInt();
            List<String> libros = AutorDAO.librosPorAutor(id);

            //si la lista está vacía, el autor no tiene libros o no existe
            if (libros.isEmpty()) {
                System.out.println("El autor no tiene libros asociados o no existe.");
                return;
            }

            // Mostrar los títulos de los libros del autor
            System.out.println("Libros del autor con ID " + id + ":");
            for (String titulo : libros) {
                System.out.println(titulo);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener los libros del autor.");
            e.printStackTrace();
        }
    }

    // Método para cargar autores desde un archivo CSV
    public static void cargarDesdeCSV() {

        List<AutorDTO> autoresCSV = LeerCSV.loadContactosFromCsv();
        List<AutorDTO> autoresValidos = new ArrayList<>();

        for (AutorDTO autor : autoresCSV) {

            String nombre = autor.getNombre_Autor();
            String email = autor.getEmail();

            // Validar datos, si no son válidos, mostrar mensaje y saltar fila
            if (!Validation.validarNombreAutor(nombre) || !Validation.validarEmailAutor(email)) {
                System.out.println("Fila inválida -> Nombre: " + nombre + ", Email: " + email);
                continue; // saltar esta fila y seguir con las demás
            }

            autoresValidos.add(autor);
        }

        // Si no hay autores válidos, mostrar mensaje y salir
        if (autoresValidos.isEmpty()) {
            System.out.println("No hay autores válidos para insertar.");
            return;
        }

        int[] resultado = AutorDAO.createBatch(autoresValidos);

        System.out.println("Autores insertados correctamente: " + resultado.length);
    }
}
