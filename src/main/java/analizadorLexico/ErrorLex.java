package analizadorLexico;

public class ErrorLex extends Exception{
    public ErrorLex(){}

    public ErrorLex(String msg){
        super(msg);
    }
}
