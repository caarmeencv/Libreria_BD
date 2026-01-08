import java.util.Scanner;
import service.AutorService;
import service.EditorialService;
import service.LibroService;

public class Libreria {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AutorService autorService = new AutorService();
        LibroService libroService = new LibroService();
        EditorialService editorialService = new EditorialService();

        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("        GESTIÓN LIBRERÍA      ");
            System.out.println("==============================");
            System.out.println("1. Editoriales");
            System.out.println("2. Libros");
            System.out.println("3. Autores");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    editorialService.menu();
                    break;
                case 2:
                    libroService.menu();
                    break;
                case 3:
                    autorService.menu();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 0);

        sc.close();
        System.out.println("Programa finalizado.");
    }
}
