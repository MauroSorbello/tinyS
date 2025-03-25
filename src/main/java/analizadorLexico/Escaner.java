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

        tokens.add(new Token(END, "", null, current, line));
        return tokens;

    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN);break;
            case ',': addToken(COMMA);break;
            case '.': addToken(DOT);break;
            case '*': addToken(STAR);break;
            case ';': addToken(SEMICOLON);break;
            case '{': addToken(LEFT_BRACE);break;
            case '}': addToken(RIGHT_BRACE);break;
            case '[': addToken(LEFT_BRACKET);break;
            case ']': addToken(RIGHT_BRACKET);break;
            case '+':
                addToken(nextMatch('+') ? PLUS_PLUS : PLUS);
                break;
            case '-':
                addToken(nextMatch('-') ? MINUS_MINUS : MINUS);
                break;
            case '!':
                addToken(nextMatch('=') ? NOT_EQUAL : NOT);
                break;
            case '=':
                addToken(nextMatch('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '>':
                addToken(nextMatch('=') ? GREATER_EQUAL : GREATER);
                break;
            case '<':
                addToken(nextMatch('=') ? LESS_EQUAL : LESS);
                break;
            case '/':
                //Puede ser un comentario:
                if (nextMatch('/')){
                    //Pasamos de largo el comentario
                    while (look() != '\n' && !isAtEnd()) advance();
                    line = line + 1;
                } else {
                    addToken(SLASH);
                }

                break;
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            //case '\v': No lo toma
                break;
        }
    }

    //Avanza al proximo caracter
    private char advance(){
        return source.charAt(current++);

    }

    //miramos el siguiente sin consumirlo
    private char look() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
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

    private boolean nextMatch(char expected){
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }


}
