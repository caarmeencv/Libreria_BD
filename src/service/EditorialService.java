package service;

import dao.EditorialDAO;
import dto.EditorialDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import utils.Validation;

public class EditorialService {

    private static final EditorialDAO dao = new EditorialDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void menu() {
        int opcion;
        do {
            System.out.println("\nMENÚ DE EDITORIALES");
            System.out.println("1. Crear editorial nueva.");
            System.out.println("2. Consultar una editorial por ID.");
            System.out.println("3. Listar editoriales.");
            System.out.println("4. Modificar una editorial.");
            System.out.println("5. Eliminar una editorial.");
            System.out.println("6. Volver al menú principal.");
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
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void crear() throws SQLException {
        System.out.print("Escribe un nombre para la nueva editorial: ");
        String nombre = sc.nextLine();

        if (!Validation.validarNombreEditorial(nombre)) return;

        dao.insertar(new EditorialDTO(0, nombre));
        System.out.println("Editorial creada correctamente.");
    }

    private static void consultar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        EditorialDTO e = dao.buscarPorId(id);
        if (e == null) {
            System.out.println("No existe esa editorial.");
            return;
        }

        System.out.println(e);
        System.out.println("Número de libros: " + dao.contarLibros(id));
    }

    private static void listar() throws SQLException {
        List<EditorialDTO> lista = dao.listarTodos();
        lista.forEach(e -> System.out.println(e.getID_Editorial() + " - " + e.getNombre_Editorial()));
    }

    private static void modificar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt(); sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        if (!Validation.validarNombreEditorial(nombre)) return;

        dao.actualizar(new EditorialDTO(id, nombre));
        System.out.println("Editorial modificada.");
    }

    private static void eliminar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        int libros = dao.contarLibros(id);
        if (libros > 0) {
            System.out.println("No se puede borrar. Tiene " + libros + " libros asociados.");
            return;
        }

        if (dao.eliminar(id)) System.out.println("Editorial eliminada.");
        else System.out.println("No existe.");
    }
}
