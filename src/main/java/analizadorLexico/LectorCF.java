package analizadorLexico;


import java.io.IOException;
import java.io.RandomAccessFile;

public class LectorCF {
    private RandomAccessFile raf;  // Atributo de la clase
    private int current=0;
    private StringBuilder source= new StringBuilder();

    public void lectorArchivo(String ruta) throws ErrorLex {
        try {
            this.raf = new RandomAccessFile(ruta, "r"); // Abrimos el archivo una sola vez
        }catch (IOException e){
            ErrorLex.errorDec(0, 0, "Error al abrir el archivo", "lectorArchivo()");
            //throw new IOException(e.getMessage());
        }
    }

    public String rechargeBuffer() throws ErrorLex, IOException {
        int caracter;
        try {
            this.raf.seek(current);
        }catch (IOException e){
            ErrorLex.errorDec(current, 0, "Error al rellenar el buffer", "rechargeBuffer()");
            //throw new IOException(e.getMessage());
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


}
