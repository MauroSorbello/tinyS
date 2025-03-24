package analizadorLexico;

import java.util.ArrayList;
import java.util.List;

public class Escaner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 0;

    Escaner(String source) {
        this.source = source;
    }

    List<Token> scanTokens(){
        while (!isAtEnd()){

            //Empezamos un nuevo lexema
            start = current;
            scanTokens();
        }

        tokens.add(new Token(END, "", null, line));
        return tokens;

    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private void scanToken(){
        char c = advance();
        switch (c){

        }
    }

    private char advance(){
        return source.charAt(current++);
    }




}
