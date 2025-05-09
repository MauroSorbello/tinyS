package analizadorLexico;

import java.io.IOException;
import java.util.List;

public class Etapa1 {
    static Escaner escaner = new Escaner();
    static LectorCF lector = new LectorCF();

    public static void main(String[] args) throws IOException {
        String source;
        escaner.setEscaner(lector);
        lector.lectorArchivo("C:/Users/Mauro Sorbello/Documents/FACULTAD/4 AÑO/Compiladores/tiny/tinyS/src/test/java/analizadorLexico/testb.s");
        source = lector.rechargeBuffer();
        escaner.setSource(source);

        List<Token> tokens = escaner.scanTokens();

        for (Token i : tokens)
        {
            System.out.println(i.toString());
        }


    }
}
