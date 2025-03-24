package analizadorLexico;

import java.util.ArrayList;
import java.util.List;

import static analizadorLexico.TokenType.END;

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

        tokens.add(new Token(END, "", null, current, line));
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

    //Avanza al proximo caracter
    private char advance(){
        return source.charAt(current++);
    }

    //agrega un token sin literal
    private void addToken(TokenType type){
        addToken(type,null);
    }
    //agrega un token
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start,current);
        tokens.add(new Token(type,text,literal,line,start));
    }




}
