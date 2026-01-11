package service;

import dao.AutorDAO;
import dto.AutorDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import utils.LeerCSV;
import utils.Validation;

public class AutorService {

    private static final AutorDAO dao = new AutorDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void menu() {
        int op;
        do {
            System.out.println("\n--- AUTORES ---");
            System.out.println("1. Crear");
            System.out.println("2. Consultar por ID");
            System.out.println("3. Listar");
            System.out.println("4. Modificar");
            System.out.println("5. Eliminar");
            System.out.println("6. Cargar desde CSV");
            System.out.println("0. Volver");
            op = sc.nextInt(); sc.nextLine();

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
                        cargarCSV();
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
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        if (!Validation.validarNombreAutor(nombre) ||
            !Validation.validarEmailAutor(email)) return;

        dao.insertar(new AutorDTO(0, email, nombre));
        System.out.println("Autor creado correctamente.");
    }

    private static void consultar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        AutorDTO a = dao.buscarPorId(id);
        if (a == null) {
            System.out.println("No existe ese autor.");
            return;
        }

        System.out.println(a);
        dao.librosPorAutor(id).forEach(l -> System.out.println("Libro: " + l));
    }

    private static void listar() throws SQLException {
        List<AutorDTO> lista = dao.listarTodos();
        lista.forEach(a -> System.out.println(a.getID_Autor() + " - " + a.getNombre_Autor()));
    }

    private static void modificar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt(); sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo email: ");
        String email = sc.nextLine();

        if (!Validation.validarNombreAutor(nombre) ||
            !Validation.validarEmailAutor(email)) return;

        dao.actualizar(new AutorDTO(id, email, nombre));
        System.out.println("Autor modificado.");
    }

    private static void eliminar() throws SQLException {
        System.out.print("ID: ");
        int id = sc.nextInt();

        if (dao.eliminar(id)) System.out.println("Autor eliminado.");
        else System.out.println("No existe.");
    }

    private static void cargarCSV() throws SQLException {
        List<AutorDTO> lista = LeerCSV.loadContactosFromCsv();

        lista.removeIf(a ->
                !Validation.validarNombreAutor(a.getNombre_Autor()) ||
                !Validation.validarEmailAutor(a.getEmail())
        );

        dao.insertarBatch(lista);
        System.out.println("Autores cargados: " + lista.size());
    }
}
