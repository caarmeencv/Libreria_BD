package service;

import dao.EditorialDAO;
import dao.LibroDAO;
import dto.EditorialDTO;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import utils.Validation;

public class EditorialService {

    // Scanner estático para toda la clase
    private static final Scanner sc = new Scanner(System.in);

    // Método principal para mostrar el menú y manejar las opciones
    public static void dameOpcion() {
        int opcion = 0;
        while (opcion != 7) {
            mostrarMenuEditorial();

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
                        mostrarLibrosDeEditorial();
                        break;
                    case 7:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige una opción del 1 al 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduce un número entre 1 y 7.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Método para mostrar el menú de editoriales
    public static void mostrarMenuEditorial() {
        System.out.println("\nMENÚ DE EDITORIALES");
        System.out.println("1. Crear editorial.");
        System.out.println("2. Consultar una editorial por ID.");
        System.out.println("3. Listar todas las editoriales.");
        System.out.println("4. Modificar una editorial.");
        System.out.println("5. Eliminar una editorial.");
        System.out.println("6. Mostrar libros de una editorial.");
        System.out.println("7. Volver al menú principal.");
    }

    // Método para crear una nueva editorial
    public static void crear() {
        String nombre;

        //Comprobar validez del nombre con Validation.validarNombreEditorial
        while (true) {
            System.out.print("Introduce nombre de la editorial: ");
            nombre = sc.nextLine();
            if (Validation.validarNombreEditorial(nombre)) {
                break;
            }
        }

        EditorialDTO nuevo = new EditorialDTO();
        nuevo.setNombre_Editorial(nombre);
        EditorialDAO.create(nuevo);
        System.out.println("Editorial creada con ID: " + nuevo.getID_Editorial());
    }

    // Método para consultar una editorial por su ID
    public static void consultar() {
        //Si no hay editoriales, no dejar continuar
        if (EditorialDAO.listarTodos().isEmpty()) {
            System.out.println("No hay editoriales para consultar.");
            return;
        }
        System.out.print("Introduce la ID de la Editorial a consultar: ");
        int id = sc.nextInt();

        try {
            EditorialDTO editorial = EditorialDAO.buscarPorId(id);

            if (editorial == null) {
                System.out.println("No existe esa editorial.");
                return;
            }

            System.out.println(editorial);
            System.out.println("Número de libros: " + EditorialDAO.contarLibros(id));

        } catch (Exception e) {
            System.out.println("Error al consultar la editorial.");
            e.printStackTrace();
        }
    }

    // Método para listar todas las editoriales
    public static void listar() {
        List<EditorialDTO> lista = EditorialDAO.listarTodos();

        // Si la lista está vacía, informar al usuario
        if (lista.isEmpty()) {
            System.out.println("No hay editoriales.");
            return;
        }

        System.out.println("Lista de editoriales:");
        for (EditorialDTO e : lista) {
            System.out.println(e);
        }
    }

    // Método para modificar una editorial existente
    public static void modificar() {
        try {
            // Que muestre todas las editoriales antes de pedir la ID
            listar();
            //si no hay editoriales, no dejar continuar
            if (EditorialDAO.listarTodos().isEmpty()) {
                return;
            }
            System.out.print("Introduce la ID de la Editorial a modificar: ");
            int id = sc.nextInt();
            sc.nextLine();

            EditorialDTO editorial = EditorialDAO.buscarPorId(id);

            if (editorial != null) {
                String nombre;
                while (true) {
                    System.out.println("Nombre actual: " + editorial.getNombre_Editorial());
                    System.out.println("Introduce el nuevo nombre de la editorial (Para mantener nombre actual, no introducir nada): ");
                    nombre = sc.nextLine();

                    // Si el usuario no introduce nada, mantener el nombre actual
                    if (nombre.isEmpty()) {
                        nombre = editorial.getNombre_Editorial();
                        break;
                    }

                    // Comprobar validez del nombre con Validation.validarNombreEditorial
                    if (Validation.validarNombreEditorial(nombre)) {
                        break;
                    }

                    System.out.println("Nombre no válido. Inténtalo de nuevo.");
                }

                editorial.setNombre_Editorial(nombre);
                EditorialDAO.actualizar(editorial);
                System.out.println("Editorial modificada.");
            } else {
                System.out.println("No existe ninguna editorial con esa ID.");
            }

        } catch (Exception e) {
            System.out.println("Error al modificar la editorial.");
            e.printStackTrace();
        }

    }

    // Método para eliminar una editorial
    public static void eliminar() {
        try {
            // En este método solo se deben eliminar editoriales que no tengan libros asociados
            listar();
            //si no hay editoriales, no dejar continuar
            if (EditorialDAO.listarTodos().isEmpty()) {
                return;
            }
            System.out.print("Introduce la ID de la Editorial a eliminar: ");
            int id = sc.nextInt();

            int numLibros = EditorialDAO.contarLibros(id);
            if (numLibros > 0) {
                System.out.println("No se puede eliminar la editorial porque tiene libros asociados.");
                System.out.println("Los libros que se deben eliminar son los siguientes: ");
                List<String> libros = EditorialDAO.librosPorEditorial(id);
                for (String libro : libros) {
                    System.out.println(libro);
                }
                return;
            }

            boolean eliminado = EditorialDAO.eliminar(id);
            if (eliminado) {
                System.out.println("Editorial eliminada.");
            } else {
                System.out.println("No existe esa editorial.");
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar la editorial.");
            e.printStackTrace();
        }
    }

    // Método para mostrar los libros de una editorial específica
    public static void mostrarLibrosDeEditorial() {
        try {
            //si no hay libros, no dejar continuar
            if (LibroDAO.listarTodos().isEmpty()) {
                System.out.println("No hay libros en la base de datos.");
                return;
            }
            listar();
            //si no hay editoriales, no dejar continuar
            if (EditorialDAO.listarTodos().isEmpty()) {
                return;
            }
            System.out.print("Introduce la ID de la Editorial para mostrar sus libros: ");
            int id = sc.nextInt();

            List<String> libros = EditorialDAO.librosPorEditorial(id);

            // Si no hay libros asociados a la editorial, informar al usuario
            if (libros.isEmpty()) {
                System.out.println("No hay libros asociados a esta editorial.");
                return;
            }

            System.out.println("Libros asociados a la editorial:");
            for (String libro : libros) {
                System.out.println(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al mostrar los libros de la editorial.");
            e.printStackTrace();
        }
    }
}
