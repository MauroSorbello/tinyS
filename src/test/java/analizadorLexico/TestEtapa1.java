package analizadorLexico;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEtapa1 {
    static Escaner escaner = new Escaner();
    static LectorCF lector = new LectorCF();

    @BeforeAll
    static void setup() throws IOException {
        // Código que se ejecuta una sola vez antes de todos los tests
        System.out.println("Configuración inicial para todos los tests");

        // Aquí puedes inicializar recursos compartidos, como el escaner y el lector
        escaner.setEscaner(lector);
        // Inicializar el lector con el archivo
    }


    private List<Token> scan(String source) throws IOException {
        lector.lectorArchivo(source); /// Usar un mtodo para establecer la fuente directamente
        String buffer = lector.rechargeBuffer();
        escaner.setBuffer(buffer);


        List<Token> tokens = new ArrayList<>();
        Token tokenActual;
        do {
            tokenActual = escaner.nextToken();
            tokens.add(tokenActual);
        } while (tokenActual.getType() != TokenType.EOF);
        return tokens;
    }





//    @Test
//    public void testBuffer() throws IOException {
//
//        List<TokenType> tiposEsperados = Arrays.asList(
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.IDOBJETS,
//                TokenType.EOF
//        );
//
//
//        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testBuffer.s");
//
//        for (int i = 0; i < tokens.size(); i++) {
//            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
//                    "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
//                            " pero se obtuvo " + tokens.get(i).getType() + " con lexema: " + tokens.get(i).getLexema());
//        }
//    }

    @Test
    public void testPalabrasClavesYSignos() throws IOException {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.IF,
                TokenType.WHILE,
                TokenType.ELSE,
                TokenType.CLASS,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testPalabrasClaves.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con lexema: " + tokens.get(i).getLexema());
        }
    }



    @Test
    public void testEscanerDesdeArchivo() throws IOException {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.CLASS,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.PUB,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.PUB,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.COMMA,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.IMPL,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.FN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.WHILE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LESS_EQUAL,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IF,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.EQUAL_EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.ELSE,
                TokenType.IF,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.EQUAL_EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.PLUS,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.ELSE,
                TokenType.LEFT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.PLUS,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.PLUS_PLUS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.RET,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.DOT,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.FN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.STRING_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.STRING_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.FN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.STRING_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.RIGHT_BRACE,
                TokenType.START,
                TokenType.LEFT_BRACE,
                TokenType.IDCLASS,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.NEW,
                TokenType.IDCLASS,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDCLASS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testc.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con lexema: " + tokens.get(i).getLexema());
        }
    }
}