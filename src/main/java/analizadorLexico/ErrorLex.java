package analizadorLexico;

public class ErrorLex extends Exception{
    public ErrorLex(){}

    public ErrorLex(String msg){
        super(msg);
    }

    public static void errorDec(int line, int column, String descripcion, String lexema){
        report(line, column, descripcion, lexema);
    }

    private static void report(int line, int column, String descripcion, String lexema){
        System.err.println("| LINEA " + line + "( COLUMNA: " + column + ") | " + descripcion + " " + lexema );
    }
}
