package analizadorLexico;


import java.io.FileReader;
import java.io.IOException;

public class LectorCF {
    public static void main(String[] args) {
        String rutaArchivo = "archivo.s"; // Ruta del archivo .s

        try (FileReader fr = new FileReader(rutaArchivo)) {
            int caracter;
            while ((caracter = fr.read()) != -1) { // Lee un carácter a la vez
                System.out.print((char) caracter); // Convierte el código ASCII a carácter
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
