package analizadorSintactico;

public class ErrorSintactico extends RuntimeException {
  ErrorSintactico(int line, int column, String descripcion){
    super ("| LINEA " + line + "( COLUMNA: " + column + ") | " + descripcion  );
  }
}
