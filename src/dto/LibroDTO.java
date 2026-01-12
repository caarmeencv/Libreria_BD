package dto;

public class LibroDTO {

    private int ID_Libro;
    private String ISBN;
    private String Titulo;
    private int ID_Editorial;

    public LibroDTO() {
    }

    public LibroDTO(int ID_Libro, String ISBN, String Titulo, int ID_Editorial) {
        this.ID_Libro = ID_Libro;
        this.ISBN = ISBN;
        this.Titulo = Titulo;
        this.ID_Editorial = ID_Editorial;
    }

    public int getID_Libro() {
        return ID_Libro;
    }

    public void setID_Libro(int ID_Libro) {
        this.ID_Libro = ID_Libro;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public int getID_Editorial() {
        return ID_Editorial;
    }

    public void setID_Editorial(int ID_Editorial) {
        this.ID_Editorial = ID_Editorial;
    }

    @Override
    public String toString() {
        return "LibroDTO [ID_Libro=" + ID_Libro + ", Titulo=" + Titulo + ", ISBN=" + ISBN + ", ID_Editorial=" + ID_Editorial + "]";
    }

}
