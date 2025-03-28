package analizadorLexico;


import java.io.IOException;
import java.io.RandomAccessFile;

public class LectorCF {
    private RandomAccessFile raf;  // Atributo de la clase

    public void lectorArchivo(String ruta) throws IOException {
        try {
            this.raf = new RandomAccessFile(ruta, "r"); // Abrimos el archivo una sola vez
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }
    private static int current=0;
    private static StringBuilder source= new StringBuilder();

    public void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Error: Se requiere el archivo fuente como parámetro.");
            System.exit(1);  // Termina el programa con un código de error
        }

        // Obtener el archivo fuente desde los argumentos
        String archivoFuente = args[0];
        this.lectorArchivo(archivoFuente);
    }

    public String rechargeBuffer() throws IOException {
        int caracter;
        try {
            this.raf.seek(current);
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
        int i;
        for (i = current; i <= current + 2048; i++) {
            if ((caracter = raf.read()) != -1) {
                source.append((char) caracter);
            } else {
                source.append("€");
                return source.toString();
            }
        }
        current = i;
        return source.toString();
    }

    private static void setPath(String path){
        LectorCF.path = path;
    }
    private static String getPath(){
        return LectorCF.path;
    }

    public String rechargeBuffer(){
        return "";
    }
}
