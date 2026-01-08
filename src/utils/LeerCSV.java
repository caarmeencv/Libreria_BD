package utils;

import dto.AutorDTO;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;


public class LeerCSV {

    private static final String CSV_PATH = "src/autores.csv";

    public static List<AutorDTO> loadContactosFromCsv() {
        List<AutorDTO> autores = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] row;


            while ((row = reader.readNext()) != null) {
                String nombre = row[0];
                String email = row[1];

                AutorDTO autor = new AutorDTO();
                autor.setNombre_Autor(nombre);
                autor.setEmail(email);

                autores.add(autor);
            }

        } catch (Exception e) {
            System.out.println("Error leyendo CSV: " + e.getMessage());
        }

        return autores;
    }
}
