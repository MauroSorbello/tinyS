package analizadorLexico;

public class ErrorLex extends Exception{
    public ErrorLex(){}

    public ErrorLex(int line, int column, String descripcion, String lexema) throws ErrorLex{
        //report(line, column, descripcion, lexema);
        super("| LINEA " + line + " ( COLUMNA: " + column + ") | " + descripcion + " " + lexema );
    }
}