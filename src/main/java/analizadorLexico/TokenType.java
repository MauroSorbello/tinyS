package analizadorLexico;

public enum TokenType {
    //Identificadores
    //IDENTIFIER, no estamos seguros
    IDCLASS, IDOBJETS,

    //Clases predefinidas
    ARRAY, STR, INT, DOUBLE, BOOL, START,


    //Literales
    INTEGER_LITERAL, DOUBLE_LITERAL, STRING_LITERAL, NIL,

    //Palabras clave
    IMPL, ELSE, FALSE, IF, RET, WHILE, TRUE, NEW, FN, ST, PUB, SELF, DIV,CLASS,VOID,

    //Tokens simbolos
    // (            )          ,     .    +      /     -      {              }            ;           [             ]           *       :
    LEFT_PAREN, RIGHT_PAREN, COMMA, DOT, PLUS, SLASH, MINUS, LEFT_BRACE, RIGHT_BRACE, SEMICOLON, LEFT_BRACKET, RIGHT_BRACKET, MULT, DOBLE_DOT,

    //Tokens operaciones
    // !   !=         =        ==         >           >=       <       <=        ++          --
    NOT, NOT_EQUAL, EQUAL, EQUAL_EQUAL,GREATER,GREATER_EQUAL,LESS,LESS_EQUAL, PLUS_PLUS, MINUS_MINUS,

    //Finalizar
    END, EOF

}
