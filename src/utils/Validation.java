package utils;

import dao.AutorDAO;
import dao.LibroDAO;
import dao.EditorialDAO;

public class Validation {

    // =======================
    // VALIDAR EMAIL DE AUTOR
    // =======================
    public static boolean validarEmailAutor(String email) {

        // Comprobar que el email no existe ya en otro autor
        if (AutorDAO.existeEmail(email)) {
            System.out.println("Ya existe un autor con ese email.");
            return false;
        }

        // Formato básico de email: algo@algo.algo
        if (!email.matches(".+@.+\\..+")) {
            System.out.println("El correo debe tener el siguiente formato: usuario@dominio.extension.");
            return false;
        }

        return true;
    }

    // =========================
    // VALIDAR NOMBRE DE AUTOR
    // =========================
    public static boolean validarNombreAutor(String nombre) {

        // Comprobar que el nombre no existe ya en otro autor
        if (AutorDAO.existeNombre(nombre)) {
            System.out.println("Ya existe un autor con ese nombre.");
            return false;
        }

        // Comprobar que no es nulo ni vacío
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }

        // Máximo 50 caracteres
        if (nombre.length() > 50) {
            System.out.println("El nombre no puede tener más de 50 caracteres.");
            return false;
        }

        // Solo letras y espacios, primera letra mayúscula
        if (!nombre.matches("^[A-Z][a-zA-Z áéíóúÁÉÍÓÚñÑ]*$")) {
            System.out.println("El nombre solo puede contener letras y espacios, y debe empezar por mayúscula.");
            return false;
        }

        return true;
    }

    // =======================
    // VALIDAR ISBN DE LIBRO
    // =======================
    public static boolean validarISBN(String isbn) {

        // Comprobar que el ISBN no existe ya en otro libro
        if (LibroDAO.existeISBN(isbn)) {
            System.out.println("Ya existe un libro con ese ISBN.");
            return false;
        }

        // Comprobar que no es nulo ni vacío
        if (isbn == null || isbn.isEmpty()) {
            System.out.println("El ISBN no puede estar vacío.");
            return false;
        }

        // Solo números y longitud 10 o 13
        if (!isbn.matches("\\d{10}|\\d{13}")) {
            System.out.println("El ISBN debe tener 10 o 13 dígitos numéricos.");
            return false;
        }

        return true;
    }

    // =========================
    // VALIDAR TÍTULO DE LIBRO
    // =========================
    public static boolean validarTituloLibro(String titulo) {

        // Comprobar que no es nulo ni vacío
        if (titulo == null || titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return false;
        }

        // Máximo 100 caracteres
        if (titulo.length() > 100) {
            System.out.println("El título no puede tener más de 100 caracteres.");
            return false;
        }

        return true;
    }

    // =============================
    // VALIDAR NOMBRE DE EDITORIAL
    // =============================
    public static boolean validarNombreEditorial(String nombre) {

        // Comprobar que no es nulo ni vacío
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }

        // Comprobar que no existe ya otra editorial con ese nombre
        if (EditorialDAO.existeNombre(nombre)) {
            System.out.println("Ya existe una editorial con ese nombre.");
            return false;
        }

        // Máximo 50 caracteres
        if (nombre.length() > 50) {
            System.out.println("El nombre no puede tener más de 50 caracteres.");
            return false;
        }

        // Letras, números y espacios
        if (!nombre.matches("[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]+")) {
            System.out.println("El nombre de la editorial solo puede contener letras, números y espacios.");
            return false;
        }

        return true;
    }
}
