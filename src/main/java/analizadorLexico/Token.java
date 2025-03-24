package analizadorLexico;

public class Token {
    final TokenType type;
    final String lexema;
    final Object literal;
    final int line;
    final int column;

    public Token(TokenType type, String lexema, Object literal, Object line, Object column) {
        this.type = type;
        this.lexema = lexema;
        this.literal = literal;
        this.column = (int) column;
        this.line = (int) line;
    }

    public String toString() {
        return type + " " + lexema + " " + literal + " " + line + " " + column;
    }
}
