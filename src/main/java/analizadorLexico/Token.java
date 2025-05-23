package analizadorLexico;

public class Token {
    final TokenType type;
    final String lexema;
    final int line;
    final int column;

    public Token(TokenType type, String lexema, Object column, Object line) {
        this.type = type;
        this.lexema = lexema;

        this.column = (int) column;
        this.line = (int) line;
    }

    public TokenType getType() {
        return type;
    }
    public String getLexema() {
        return lexema;
    }
    public int getLine() {
        return line;
    }
    public int getColumn() {
        return column;
    }

    public String toString() {
        return "| " +type + " | " + lexema  + " | LINEA " + line + " (COLUMNA" + column + ") |";
    }
}
