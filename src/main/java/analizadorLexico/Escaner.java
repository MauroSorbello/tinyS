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
        keywords.put("Array", ARRAY);
        keywords.put("Int", INT);
        keywords.put("Double", DOUBLE);
        keywords.put("Str", STR);
        keywords.put("start", START);
        keywords.put("void", VOID);
    }

    Escaner(String source) {
        this.source = source;
    }


    Escaner(String source, LectorCF lectorCF) {
        this.source = source;
        this.lectorCF = lectorCF;
    }

    Escaner(LectorCF lectorCF) {
        this.lectorCF = lectorCF;
    }

    Escaner() {}

    public void setEscaner(LectorCF lectorCF) {
        this.lectorCF = lectorCF;
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

        tokens.add(new Token(EOF, "", column, line));
        return tokens;

    }

    private boolean isAtEnd() throws IOException {
        //return current >= source.length();
        if (current == 2048) {
            source = lectorCF.rechargeBuffer();
        }
        return source.charAt(current + 1) == '€';
    }

    public Token nextToken() throws IOException {
        char c = advance();
        switch (c) {
            case '(':
                start=current-1;
                return addToken(LEFT_PAREN);

            case ')':
                start=current-1;
                return addToken(RIGHT_PAREN);

            case ',':
                start=current-1;
                return addToken(COMMA);

            case '.':
                start=current-1;
                return addToken(DOT);

            case ':':
                start=current-1;
                return addToken(DOBLE_DOT);

            case '*':
                start=current-1;
                return addToken(MULT);

            case ';':
                start=current-1;
                return addToken(SEMICOLON);

            case '{':
                start=current-1;
                return addToken(LEFT_BRACE);

            case '}':
                start=current-1;
                return addToken(RIGHT_BRACE);

            case '[':
                start=current-1;
                return addToken(LEFT_BRACKET);

            case ']':
                start=current-1;
                return addToken(RIGHT_BRACKET);
            case '€':
                return addToken(END);

            case '+':
                start=current-1;
                return addToken(nextMatch('+') ? PLUS_PLUS : PLUS);

            case '-':
                start=current-1;
                return addToken(nextMatch('-') ? MINUS_MINUS : MINUS);

            case '!':
                start=current-1;
                return addToken(nextMatch('=') ? NOT_EQUAL : NOT);

            case '=':
                start=current-1;
                return addToken(nextMatch('=') ? EQUAL_EQUAL : EQUAL);

            case '>':
                start=current-1;
                return addToken(nextMatch('=') ? GREATER_EQUAL : GREATER);

            case '<':
                start=current-1;
                return addToken(nextMatch('=') ? LESS_EQUAL : LESS);

            case '/':
                //Puede ser un comentario:
                if (nextMatch('/')) {
                    //Pasamos de largo el comentario
                    while (look() != '\n' && !isAtEnd()) advance();
                    column=0;
                    line = line + 1;
                    return nextToken();
                } else {
                    start=current-1;
                    return addToken(SLASH);
                }

            //literales cadenas
            case '"':
                return string();


            case ' ':
                return nextToken();
            case '\t':

                return nextToken();
            case '\r':

                return nextToken();
            case '\n':
                column=0;
                line++;
                return nextToken();
            //case '\v': No lo toma
            //
        }
        if (isDigit(c)) {
            return number();
        } else {
            if (isAlpha(c)) {
                return identifier(c);
            } else {
                ErrorLex.errorDec(line, column, "CARACTER INVALIDO", String.valueOf(c));
                throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);
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
    private Token addToken(TokenType type){
        String text = source.substring(start,current);
        Token token = new Token(type,text,column,line);
        tokens.add(token);
        return token;
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
    private Token string() throws IOException {
        while (look() != '"' && !isAtEnd()) {
            if (look() == '\n') {
                line++;
                column = 0;
            }
            advance();
        }
        if (isAtEnd()) {
            ErrorLex.errorDec(line, column, "STRING SIN TERMINAR", source.substring(start + 1, current - 1));
            throw new IOException("Cadena sin terminar en línea " + line + ", columna " + column);
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        return addToken(STRING_LITERAL);
    }

    private  boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    private Token number() throws IOException {
        boolean isDouble = false;
        while (isDigit(look())) advance();

        //Buscar la parte decimal

        if(look() == '.'){
            if (isDigit(lookNext())) {
                advance();
                isDouble = true;
            }else {

                ErrorLex.errorDec(line, column, "DOUBLE INVALID", source.substring(start + 1, current - 1));
                throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);
            }
        }
        if(isAlpha(look())) {

            ErrorLex.errorDec(line, column, "CARACTER INVALIDO", source.substring(start + 1, current - 1));
            throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);

        }

        while (isDigit(look())) advance();
        if (isDouble){
            return addToken(DOUBLE_LITERAL);
        }
        else{
            return addToken(INTEGER_LITERAL);
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

    private Token identifier(char c) throws IOException {
        start=current-1;
        boolean identificadorTipo = isAlphaCapital(c);

        while (isAlphaNumeric(look())){
            advance();
        }
        String text = source.substring(start,current);
        if (keywords.containsKey(text)){
            return addToken(keywords.get(text));
        }else{
            if (identificadorTipo){
                return addToken(IDCLASS);
            }else{
                return addToken(IDOBJETS);
            }
        }
    }



}
