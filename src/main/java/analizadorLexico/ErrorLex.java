package analizadorLexico;

public class ErrorLex extends Exception{

    ErrorLex(int line, int column, String descripcion, String lexema){
        super ("| LINEA " + line + "( COLUMNA: " + column + ") | " + descripcion + " " + lexema );
    }
}
