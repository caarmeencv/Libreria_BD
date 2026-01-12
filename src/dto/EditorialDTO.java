package dto;

public class EditorialDTO {

    private int ID_Editorial;
    private String Nombre_Editorial;

    public EditorialDTO() {
    }

    public EditorialDTO(int ID_Editorial, String Nombre_Editorial) {
        this.ID_Editorial = ID_Editorial;
        this.Nombre_Editorial = Nombre_Editorial;
    }

    public int getID_Editorial() {
        return ID_Editorial;
    }

    public void setID_Editorial(int ID_Editorial) {
        this.ID_Editorial = ID_Editorial;
    }

    public String getNombre_Editorial() {
        return Nombre_Editorial;
    }

    public void setNombre_Editorial(String Nombre_Editorial) {
        this.Nombre_Editorial = Nombre_Editorial;
    }

    @Override
    public String toString() {
        return "EditorialDTO [ID_Editorial=" + ID_Editorial + ", Nombre_Editorial=" + Nombre_Editorial + "]";
    }

}
