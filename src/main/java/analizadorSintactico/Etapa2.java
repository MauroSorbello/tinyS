package analizadorSintactico;

import ErrorManage.ErrorTiny;
import analizadorLexico.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Etapa2 {
    static Parser parser = new Parser();
    static Escaner escaner = new Escaner();
    static LectorCF lector = new LectorCF();

    public static void main(String[] args) throws IOException, ErrorTiny {
        String source;
        escaner.setEscaner(lector);
        lector.lectorArchivo("/home/maurosorbello/Documentos/Compiladores/src/test/resources/sintaxisTest/personaBasic.s");
        source = lector.rechargeBuffer();
        escaner.setBuffer(source);
        parser.setEscaner(escaner);
        Token firstToken = escaner.nextToken();
        parser.setCurrentToken(firstToken);
        System.out.println(parser.s());


    }
}
