package analizadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static analizadorLexico.TokenType.*;

public class Escaner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 0;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("fn", FN);
        keywords.put("class", CLASS);
        keywords.put("objets", OBJETS);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("while", WHILE);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("new", NEW);
        keywords.put("return", RET);
        keywords.put("self", SELF);
        keywords.put("pub", PUB);
        keywords.put("nil", NIL);
        keywords.put("impl", IMPL);
        keywords.put("st", ST);
        keywords.put("div", DIV);
    }

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
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '*':
                addToken(STAR);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case '[':
                addToken(LEFT_BRACKET);
                break;
            case ']':
                addToken(RIGHT_BRACKET);
                break;
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
                if (nextMatch('/')) {
                    //Pasamos de largo el comentario
                    while (look() != '\n' && !isAtEnd()) advance();
                    line = line + 1;
                } else {
                    addToken(SLASH);
                }
                break;
            //literales cadenas
            case '"':
                string();
                break;

            case ' ':
                break;
            case '\t':
                break;
            case '\r':
                break;
            case '\n':
                line++;
                break;
            //case '\v': No lo toma
            //
        }
        if (isDigit(c)){
            number();
        } else{
            if (isAlpha(c)){
                identifier(c);
            } /*else{
                Lox.error(line, "Caracter inesperado");
             }*/

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
    /**
     * Processes a string literal enclosed in double quotes from the source code.
     * The method identifies the start and end of a string literal and handles
     * potential errors related to invalid or unclosed string definitions.
     * <p>
     * Behavior:
     * - Iterates through characters within a string literal, ensuring it ends
     *   with a closing double-quote.
     * - Tracks line numbers and increments if a newline character is encountered
     *   within the string literal.
     * - If an unexpected end of the source code is reached before closing the
     *   string, an error is reported.
     * - On proper closure, extracts the string value, removes surrounding quotes,
     *   and creates a corresponding string token.
     * <p>
     * Preconditions:
     * - Called when encountering a '"' character from the source code during
     *   scanning.
     * <p>
     * Postconditions:
     * - If successfully parsed, adds a `STRING` token with its extracted value
     *   to the token collection.
     * - If parsing fails (e.g., due to an unclosed string), logs an error and
     *   does not generate a token.
     */
    //
    private void string() {
        while (look() != '"' && !isAtEnd()) {
            if (look() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            //Lox.error(line, "string sin terminar");
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private  boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private void number() {
        boolean isDouble = false;
        while (isDigit(look())) advance();

        //Buscar la parte decimal
        if(look() == '.' && isDigit(lookNext())){
            advance();
            isDouble = true;
        }
        while (isDigit(look())) advance();
        if (isDouble){
            addToken(DOUBLE, Double.parseDouble(source.substring(start,current)));
        }
        else{
            addToken(INTEGER, Integer.parseInt(source.substring(start,current)));
        }

    }

    private char lookNext(){
        if(isAtEnd()) return '\0';
        return source.charAt(current+1);
    }

    private boolean isAlphaCapital(char c){
        return (c >= 'A' && c <= 'Z');
    }

    private boolean isAlphaLower(char c){
        return (c >= 'a' && c <= 'z');
    }

    private boolean isAlpha(char c){
        return isAlphaCapital(c) ||
                isAlphaLower(c);
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) ||
                isDigit(c) ||
                c == '_';
    }

    private void identifier(char c) {
        boolean identificadorTipo = isAlphaCapital(c);

        while (isAlphaNumeric(look())){
            advance();
        }
        String text = source.substring(start,current);
        if (keywords.containsKey(text)){
            addToken(keywords.get(text));
        }else{
            if (identificadorTipo){
                addToken(CLASS,text);
            }else{
                addToken(OBJETS,text);
            }
        }
    }



}
