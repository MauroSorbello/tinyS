package analizadorLexico;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEtapa1 {

    @BeforeAll
    static void setup() throws IOException {
        // Código que se ejecuta una sola vez antes de todos los tests
        System.out.println("Configuración inicial para todos los tests");

        // Aquí puedes inicializar recursos compartidos, como el escaner y el lector

        // Inicializar el lector con el archivo
    }


    private List<Token> scan(String source) throws IOException, ErrorLex {
        LectorCF lector = new LectorCF();
        Escaner escaner = new Escaner();
        escaner.setEscaner(lector);
        lector.lectorArchivo(source); /// Usar un metodo para establecer la fuente directamente
        String buffer = lector.rechargeBuffer();
        escaner.setBuffer(buffer);

        List<Token> tokens = new ArrayList<>();
        Token tokenActual;
        do {
            tokenActual = escaner.nextToken();
            if (tokenActual != null) {
                tokens.add(tokenActual);
                if (tokenActual.getType() == TokenType.EOF) {
                    break;
                }
            } else {
                // Manejar el caso en que tokenActual es nulo
                //System.err.println("Error: tokenActual es nulo");
                break; // Salir del bucle si no se puede obtener un token válido
            }
        } while (true);
        return tokens;
    }





    @Test
    public void testCaracterInvalido() {
        String path = "/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/identificadoresErroneos.s";

        ErrorLex exception = assertThrows(ErrorLex.class, () -> {
            scan(path); // Esta llamada debe lanzar la excepción
        });

        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("CARACTER INVALIDO"),
                "Se esperaba un mensaje que contenga 'CARACTER INVALIDO' pero fue: " + mensaje);
    }
    @Test
    public void testComentarios() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDCLASS,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.STRING_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDCLASS,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.TRUE,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.PLUS,
                TokenType.INTEGER_LITERAL,
                TokenType.MULT,
                TokenType.INTEGER_LITERAL,
                TokenType.MINUS,
                TokenType.LEFT_PAREN,
                TokenType.INTEGER_LITERAL,
                TokenType.SLASH,
                TokenType.INTEGER_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/comentarios.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de buffer "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testBuffer() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.IDOBJETS,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testBuffer.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de buffer "+
                    "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }
    @Test
    public void testSecuenciasComplejas() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
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
                TokenType.INTEGER_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/secuenciasCompletasIO.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de secuencias completas "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testNumerosPares() throws IOException, ErrorLex {

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
                TokenType.IDOBJETS,
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
                TokenType.PERCENTAGE,
                TokenType.INTEGER_LITERAL,
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
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.PLUS_PLUS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.RET,
                TokenType.INTEGER_LITERAL,
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
                TokenType.IDOBJETS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/numerosPares.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de numeros pares "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testContador() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.CLASS,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.PUB,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.IMPL,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.FN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.WHILE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.GREATER_EQUAL,
                TokenType.INTEGER_LITERAL,
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
                TokenType.LEFT_PAREN,
                TokenType.MINUS_MINUS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.RET,
                TokenType.INTEGER_LITERAL,
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
                TokenType.IDOBJETS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/contador.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test contador "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testFactorial() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.CLASS,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.PUB,
                TokenType.INT,
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
                TokenType.INT,
                TokenType.IDOBJETS,
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
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.MULT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.PLUS_PLUS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
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
                TokenType.IDOBJETS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/factorial.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test factorial "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testPrimo() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.CLASS,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.PUB,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.PUB,
                TokenType.IDCLASS,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.IMPL,
                TokenType.IDCLASS,
                TokenType.LEFT_BRACE,
                TokenType.FN,
                TokenType.IDCLASS,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.INT,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.TRUE,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.IDOBJETS,
                TokenType.SEMICOLON,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.WHILE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LESS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IF,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.PERCENTAGE,
                TokenType.IDOBJETS,
                TokenType.EQUAL_EQUAL,
                TokenType.INTEGER_LITERAL,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.IDOBJETS,
                TokenType.EQUAL,
                TokenType.FALSE,
                TokenType.SEMICOLON,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RET,
                TokenType.FALSE,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.PLUS_PLUS,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RET,
                TokenType.TRUE,
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
                TokenType.TRUE,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.FN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
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
                TokenType.RIGHT_BRACE,
                TokenType.FN,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
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
                TokenType.IDOBJETS,
                TokenType.DOT,
                TokenType.IDOBJETS,
                TokenType.LEFT_PAREN,
                TokenType.IDOBJETS,
                TokenType.RIGHT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.SEMICOLON,
                TokenType.RIGHT_BRACE,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/primo.s");
//        for (int i = 0; i < tokens.size(); i++) {
//            System.out.println(tokens.get(i).getType());
//        }

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de codigo primos "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }

    @Test
    public void testPalabrasClavesYSignos() throws IOException, ErrorLex {

        List<TokenType> tiposEsperados = Arrays.asList(
                TokenType.CLASS,
                TokenType.IMPL,
                TokenType.ELSE,
                TokenType.FALSE,
                TokenType.IF,
                TokenType.RET,
                TokenType.WHILE,
                TokenType.TRUE,
                TokenType.NEW,
                TokenType.FN,
                TokenType.ST,
                TokenType.PUB,
                TokenType.SELF,
                TokenType.SEMICOLON,
                TokenType.DOT,
                TokenType.COMMA,
                TokenType.LEFT_PAREN,
                TokenType.RIGHT_PAREN,
                TokenType.LEFT_BRACE,
                TokenType.RIGHT_BRACE,
                TokenType.LEFT_BRACKET,
                TokenType.RIGHT_BRACKET,
                TokenType.EOF
        );


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testPalabrasClaves.s");
//        for (int i = 0; i < tokens.size(); i++) {
//            System.out.println(tokens.get(i).getType());
//        }

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de palabras claves "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }



    @Test
    public void testFibonacci() throws IOException, ErrorLex {

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


        List<Token> tokens = scan("/home/nacho/IdeaProjects/tinyS/src/test/resources/lexicalTest/testFibonacci.s");

        for (int i = 0; i < tokens.size(); i++) {
            assertEquals(tiposEsperados.get(i), tokens.get(i).getType(),
                    "Error en el test de Fibonacci "+
                            "Error en el token " + (i + 1) + ": Se esperaba " + tiposEsperados.get(i) +
                            " pero se obtuvo " + tokens.get(i).getType() + " con " + tokens.get(i).toString());
        }
    }
}