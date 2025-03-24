package analizadorLexico;
import java.util.ArrayList;
import java.util.List;

import static analizadorLexico.TokenType.*;

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

    private char advance(){
        return source.charAt(current++);
    }

    private void scanToken() {
        char c = advance();
        switch (c){
            case '(': addToken(LEFT_PAREN);
            case ')': addToken(RIGHT_PAREN);
            case ',': addToken(COMMA);
            case '.': addToken(DOT);
            case '*': addToken(PLUS);
            case ';': addToken(SEMICOLON);
            case '{': addToken(LEFT_BRACE);
            case '}': addToken(RIGHT_BRACE);
            case '[': addToken(LEFT_BRACKET);
            case ']': addToken(RIGHT_BRACKET);
        }
    }

}
