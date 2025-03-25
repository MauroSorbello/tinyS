package analizadorLexico;

public enum TokenType {
    //Identificadores
    IDENTIFIER, CLASS, OBJETS,

    //Literales
    INTEGER, DOUBLE, STRING, NIL,

    //Palabras clave
    IMPL, ELSE, FALSE, IF, RET, WHILE, TRUE, NEW, FN, ST, PUB, SELF, DIV,

    //Tokens simbolos
    LEFT_PAREN, RIGHT_PAREN, COMMA, DOT, PLUS, SLASH, MINUS, LEFT_BRACE, RIGHT_BRACE, SEMICOLON, LEFT_BRACKET, RIGHT_BRACKET, STAR,

    //Tokens operaciones
    NOT, NOT_EQUAL, EQUAL, EQUAL_EQUAL,GREATER,GREATER_EQUAL,LESS,LESS_EQUAL, PLUS_PLUS, MINUS_MINUS,

    //Finalizar
    END

}
