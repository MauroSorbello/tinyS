package analizadorLexico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class LectorCF {

    private int current=0;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java LectorCF <archivo>");
            System.exit(64);
        }else{
            if(args.length == 1){
                runFile(args[0]);
            }else{
                runPrompt();
            }
        }
    }

    private static void runFile(String file){
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void runPrompt(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print("> ");
            String line = reader.readLine();
            if(line == null) break;
            run(line);
        }
    }

    private static void run(String source) {
        Escaner escaner = new Escaner(source);
        List<Token> tokens = escaner.scanTokens();

        // Fase inicial, solo imprimir los tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
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
