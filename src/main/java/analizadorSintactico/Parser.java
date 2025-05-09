package analizadorSintactico;

import analizadorLexico.ErrorLex;
import analizadorLexico.Escaner;
import analizadorLexico.Token;
import analizadorLexico.TokenType;

import static analizadorLexico.TokenType.CLASS;
import static analizadorLexico.TokenType.IMPL;

public class Parser {

    private Token tokenActual;
    private Escaner escaner;

    //public void macheo(TokenType tokenType){
        //if (tokenActual.getType() == tokenType){
        //    tokenActual = escaner.
        //}
    //}

    private void s() throws ErrorLex {
        if (tokenActual.getType() == CLASS || tokenActual.getType()== IMPL){
            //program();
            //macheo(END);
        }
    }







}
