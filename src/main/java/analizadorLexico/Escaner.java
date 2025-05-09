package analizadorLexico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static analizadorLexico.TokenType.*;

public class Escaner {
    private String source;
    private final List<Token> tokens = new ArrayList<>();
    ErrorLex error = new ErrorLex();
    private LectorCF lectorCF;

    private int start = 0;
    private int current = 0;
    private int line = 0;
    private int column = 0;

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
        keywords.put("€", END);
    }

    Escaner(String source) {
        this.source = source;
    }

    public void setEscaner(LectorCF lectorCF) {
        this.lectorCF = lectorCF;
    }

    Escaner(String source, LectorCF lectorCF) {
        this.source = source;
        this.lectorCF = lectorCF;
    }

    Escaner() {
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() throws IOException {
        while (!isAtEnd()) {

            //Empezamos un nuevo lexema
            start = current;
            nextToken();
        }

        tokens.add(new Token(END, "", column, line));
        return tokens;

    }

    private boolean isAtEnd() throws IOException {
        //return current >= source.length();
        if (current == 2048) {
            source = lectorCF.rechargeBuffer();
        }
        return source.charAt(current + 1) == '€';
    }

    public void nextToken() throws IOException {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                return;
            case ')':
                addToken(RIGHT_PAREN);
                return;
            case ',':
                addToken(COMMA);
                return;
            case '.':
                addToken(DOT);
                return;
            case '*':
                addToken(STAR);
                return;
            case ';':
                addToken(SEMICOLON);
                return;
            case '{':
                addToken(LEFT_BRACE);
                return;
            case '}':
                addToken(RIGHT_BRACE);
                return;
            case '[':
                addToken(LEFT_BRACKET);
                return;
            case ']':
                addToken(RIGHT_BRACKET);
                return;
            case '+':
                addToken(nextMatch('+') ? PLUS_PLUS : PLUS);
                return;
            case '-':
                addToken(nextMatch('-') ? MINUS_MINUS : MINUS);
                return;
            case '!':
                addToken(nextMatch('=') ? NOT_EQUAL : NOT);
                return;
            case '=':
                addToken(nextMatch('=') ? EQUAL_EQUAL : EQUAL);
                return;
            case '>':
                addToken(nextMatch('=') ? GREATER_EQUAL : GREATER);
                return;
            case '<':
                addToken(nextMatch('=') ? LESS_EQUAL : LESS);
                return;
            case '/':
                //Puede ser un comentario:
                if (nextMatch('/')) {
                    //Pasamos de largo el comentario
                    while (look() != '\n' && !isAtEnd()) advance();
                    column=0;
                    line = line + 1;
                } else {
                    addToken(SLASH);
                }
                return;
            //literales cadenas
            case '"':
                string();
                return;

            case ' ':
                return;
            case '\t':
                return;
            case '\r':
                return;
            case '\n':
                column=0;
                line++;
                return;
            //case '\v': No lo toma
            //
        }
        if (isDigit(c)) {
            number();
        } else {
            if (isAlpha(c)) {
                identifier(c);
            } else {
                ErrorLex.errorDec(line, column, "CARACTER INVALIDO", String.valueOf(c));
            }

        }

    }

    //Avanza al proximo caracter
    private char advance() throws IOException {
        if (isAtEnd()) return '€';
        if (column >= source.length()) {
            source=lectorCF.rechargeBuffer();
            current = 0;
        }
        column++;
        return source.charAt(current++);

    }

    //miramos el siguiente sin consumirlo
    private char look() throws IOException {
        if (isAtEnd()) return '€';
        return source.charAt(current);
    }

    //agrega un token sin literal
    //private void addToken(TokenType type){
    //addToken(type);
    //}

    //agrega un token
    private void addToken(TokenType type){
        String text = source.substring(start,current);
        tokens.add(new Token(type,text,column,line));
    }

    private boolean nextMatch(char expected) throws IOException {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        column++;
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
    private void string() throws IOException {
        while (look() != '"' && !isAtEnd()) {
            if (look() == '\n') {
                line++;
                column = 0;
            }
            advance();
        }
        if (isAtEnd()) {
            ErrorLex.errorDec(line, column, "STRING SIN TERMINAR", source.substring(start + 1, current - 1));
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING);
    }

    private  boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private void number() throws IOException {
        boolean isDouble = false;
        while (isDigit(look())) advance();

        //Buscar la parte decimal

        if(look() == '.'){
            if (isDigit(lookNext())) {
                advance();
                isDouble = true;
            }else {
                ErrorLex.errorDec(line, column, "DOUBLE INVALID", source.substring(start + 1, current - 1));
            }
        }
        if(isAlpha(look())) {
            ErrorLex.errorDec(line, column, "CARACTER INVALIDO", source.substring(start + 1, current - 1));
        }

        while (isDigit(look())) advance();
        if (isDouble){
            addToken(DOUBLE);
        }
        else{
            addToken(INTEGER);
        }

    }

    private char lookNext() throws IOException {
        if(isAtEnd()) return '€';
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

    private void identifier(char c) throws IOException {
        boolean identificadorTipo = isAlphaCapital(c);

        while (isAlphaNumeric(look())){
            advance();
        }
        String text = source.substring(start,current);
        if (keywords.containsKey(text)){
            addToken(keywords.get(text));
        }else{
            if (identificadorTipo){
                addToken(CLASS);
            }else{
                addToken(OBJETS);
            }
        }
    }



}
