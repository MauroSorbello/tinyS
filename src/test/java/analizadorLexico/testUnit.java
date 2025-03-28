package analizadorLexico;


import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;


public class testUnit {

    @Nested
    class TestUnit {
        Escaner escaner = new Escaner();

        /*
        @BeforeAll
        static void beforeAll() {
            System.out.println("Ejecución de pruebas iniciada");
        }

        // Configuración inicial (si es necesario)
        @BeforeEach
        void setUp() {
            // Inicialización de objetos o dependencias necesarias antes de cada prueba.
        }

         */

        @Test
        void testCasoExitoso() throws IOException {
            String source = "if while en la proxima s /n ale hola hola€";
            escaner.setSource(source);

            List<Token> tokens = escaner.scanTokens();

            for (Token i : tokens)
            {
                System.out.println(i.toString());
            }


        }
    /*
        @Test
        void testCasoFallido() {
            // Ejemplo de una prueba que podría fallar
        }

     */
    }


}
