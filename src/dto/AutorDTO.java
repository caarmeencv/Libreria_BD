package dto;

public class AutorDTO {
    private int ID_Autor;
    private String Email;
    private String Nombre_Autor;

    public AutorDTO() {}
    
    public AutorDTO(int ID_Autor, String Email, String Nombre_Autor) {
        this.ID_Autor = ID_Autor;
        this.Email = Email;
        this.Nombre_Autor = Nombre_Autor;
    }

    public int getID_Autor() {
        return ID_Autor;
    }

    public void setID_Autor(int ID_Autor) {
        this.ID_Autor = ID_Autor;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getNombre_Autor() {
        return Nombre_Autor;
    }

    public void setNombre_Autor(String Nombre_Autor) {
        this.Nombre_Autor = Nombre_Autor;
    }

    @Override
    public String toString() {
        return "AutorDTO [ID_Autor=" + ID_Autor + ", Nombre_Autor=" + Nombre_Autor + ", Email=" + Email + "]";
    }
    
}
