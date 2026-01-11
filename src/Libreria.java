import java.util.InputMismatchException;
import java.util.Scanner;
import service.AutorService;
import service.EditorialService;
import service.LibroService;

public class Libreria {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        int opcion = 0;

        while(opcion != 4){
            mostrarMenuPrincipal();
            try {
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        EditorialService.menu();
                        break;
                    case 2:
                        LibroService.menu();
                        break;
                    case 3:
                        AutorService.menu();
                        break;
                    case 4:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige una opción del 1 al 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduce un número entre el 1 y el 4.");
                sc.nextLine();
            }
        }
        sc.close();
        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\nMENÚ PRINCIPAL");
            System.out.println("1. Menú de editoriales.");
            System.out.println("2. Menú de libros.");
            System.out.println("3. Menú de autores.");
            System.out.println("4. Salir del programa.");
            System.out.print("Elige una opción entre 1 y  4: ");
    }
}
