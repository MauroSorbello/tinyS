package analizadorSintactico;

import analizadorLexico.ErrorLex;
import analizadorLexico.Escaner;
import analizadorLexico.Token;
import analizadorLexico.TokenType;

import java.io.IOException;

import static analizadorLexico.TokenType.*;

public class Parser {

    private Token currentToken;
    private Escaner escaner;
    private Token lookaheadToken;
    private boolean hasLookahead = false;

    public void macheo(TokenType tokenType) throws IOException {
        if (currentToken.getType() == tokenType){
            if (hasLookahead) {
                currentToken = lookaheadToken;
                hasLookahead = false;
            } else {
                currentToken = escaner.nextToken();
            }
        }
    }

    //Leer token sin consumirlo
    private Token peekToken() throws IOException {
        if (!hasLookahead) {
            lookaheadToken = escaner.nextToken();
            hasLookahead = true;
        }
        return lookaheadToken;
    }

    private void s() throws ErrorLex, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL){
            program();
            macheo(EOF);
        }
        throw new IOException("TOKEN INVALIDO en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
    }

    private void program() throws ErrorLex, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL || type == START){
            lista_definiciones();
            start();
        }else{
            throw new IOException("TOKEN INVALIDO en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }
    private void start() throws ErrorLex, IOException {
        if (currentToken.getType() == START){
            macheo(START);
            bloque_metodo();
        }else{
            throw new IOException("Se espera un metodo start en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }


    private void lista_definiciones() throws ErrorLex, IOException {
        TokenType type = currentToken.getType();
        if (type == START){
            return;
        }else{
            if (type == CLASS) {
                class_lista_recursivo();
            }else{
                    if (type == IMPL){
                        impl_lista_recursivo();
                    }else{
                        throw new IOException("Se espera un metodo start, la definición de una clase o una implementación en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                    }
            }
        }

    }

    private void class_lista_recursivo() throws IOException {
        if (currentToken.getType() == CLASS){
            clas();
            lista_factorizacion();
        }else{
            throw new IOException("Se espera la palabra clave class en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void impl_lista_recursivo() throws IOException {
        if(currentToken.getType()==IMPL){
            impl();
            lista_factorizacion();
        }else{
            throw new IOException("Se espera una implementación en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());

        }
    }


    private void lista_factorizacion() throws IOException {
        TokenType type = currentToken.getType();
        if(type == CLASS ){
            class_lista_recursivo();
        }else{
            if(type == IMPL){
                impl_lista_recursivo();
            }else{
                if(type == START){
                    return;
                }else{
                    throw new IOException("Se espera un metodo start en línea, la definición de una clase o una implementación en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                }
            }
        }
    }

    private void clas() throws IOException {
        if(currentToken.getType()==CLASS){
            macheo(CLASS);
            macheo(IDCLASS);
            clas_factorizado();
        }else{
            throw new IOException("Se espera la definición de una clase en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void clas_factorizado() throws IOException {
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            atributo_class_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            if(type == DOBLE_DOT ){
                herencia();
                macheo(LEFT_BRACE);
                atributo_class_recursivo();
                macheo(RIGHT_BRACE);
            }else{
                throw new IOException("Se espera la definición de una clase o una herencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void atributo_class_recursivo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == PUB || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            atributo();
            atributo_class_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new IOException("Se espera una definicion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void impl() throws IOException {
        if(currentToken.getType() == IMPL){
            macheo(IMPL);
            macheo(IDCLASS);
            macheo(LEFT_BRACE);
            miembro();
            miembro_impl_recursivo();
        }else{
            throw new IOException("Se espera un impl en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void miembro_impl_recursivo() throws IOException {
        TokenType type = currentToken.getType();
        if(type == FN || type == ST || type == DOT){
            miembro();
            miembro_impl_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new IOException("La implementación esta incompleta en línea" + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }
    private void herencia() throws IOException {
        if(currentToken.getType() == DOBLE_DOT){
            macheo(DOBLE_DOT);
            tipo();
        }else{
            throw new IOException("Se esperan dos puntos en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void miembro() throws IOException {
        TokenType type = currentToken.getType();
        if (type == FN|| type == ST){
            metodo();
        }else{
            if(type == DOT){
                constructor();
            }else{
                throw new IOException("Se espera finalizar el miembro en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void constructor() throws IOException {
        if (currentToken.getType()==DOT){
            macheo(DOT);
            argumentos_formales();
            bloque_metodo();
        }else{
            throw new IOException("Se espera un punto en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void atributo() throws IOException {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo();
            lista_declaraciones_variables();
        }else{
            if(type == PUB){
                visibilidad();
                tipo();
                lista_declaraciones_variables();
            }else{
                throw new IOException("Se espera un atributo en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void metodo() throws IOException {
        TokenType type = currentToken.getType();
        if(type == FN){
            macheo(FN);
            tipo_metodo_factorizacion();
            macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos
            argumentos_formales();
            bloque_metodo();
        }else{
            if(type == ST ){
                forma_metodo();
                macheo(FN);
                tipo_metodo_factorizacion();
                macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos
                argumentos_formales();
                bloque_metodo();
            }else{
                throw new IOException("Se espera un punto en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void tipo_metodo_factorizacion() throws IOException {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == VOID || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo_metodo();
        }else{
            if(type == IDOBJETS){
                return;
            }else{
                throw new IOException("Se espera un tipo para el método en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void visibilidad() throws IOException{
        if (currentToken.getType() == PUB){
            macheo(PUB);
        }else{
            throw new IOException("Se espera una asignacion de visibilidad en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void forma_metodo() throws IOException{
        if (currentToken.getType() == ST){
            macheo(ST);
        }else{
            throw new IOException("Se espera la palabra st en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void bloque_metodo() throws IOException{
        if(currentToken.getType()==LEFT_BRACE){
            macheo(LEFT_BRACE);
            decl_var_loc_bloque_recursivo();
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new IOException("Se espera un corchete que abre en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void decl_var_loc_bloque_recursivo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS  || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY ){
            decl_var_locales();
            decl_var_loc_bloque_recursivo();
        }else{
            if(type == LEFT_BRACE || type == RIGHT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type == IF || type == WHILE || type == RET || type == IDOBJETS || type == PUB || type == NEW || type == FN || type == ST || type == DOT || type == SELF){
                return;
            }else{
                throw new IOException("Se espera una declaracion de variables en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void sentencia_bloque_recursivo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type== IF || type == WHILE || type == RET || type == IDOBJETS || type == SELF){
            sentencia();
            sentencia_bloque_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new IOException("Se espera una sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void decl_var_locales() throws IOException{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            tipo();
            lista_declaraciones_variables();
        }else{
            throw new IOException("Se espera una declaracion de variables locales en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_declaraciones_variables() throws IOException{
        if(currentToken.getType()==IDOBJETS){
            macheo(IDOBJETS);
            lista_declaraciones_variables_Pr();
        }else{
            throw new IOException("Se espera una lista de declaraciones de variables en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_declaraciones_variables_Pr() throws IOException{
        TokenType type = currentToken.getType();
        if (type == COMMA){
            macheo(COMMA);
            lista_declaraciones_variables();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new IOException("Se espera una nueva declaración o que se finalize la lista en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void argumentos_formales() throws IOException{
        if(currentToken.getType()==LEFT_PAREN){
            macheo(LEFT_PAREN);
            lista_argumentos_formales_factorizado();
            macheo(RIGHT_PAREN);
        }else{
            throw new IOException("Se espera un parentesis en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_argumentos_formales_factorizado() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            lista_argumentos_formales();
        }else{
            if(type == RIGHT_PAREN){
                return;
            }else{
                throw new IOException("Se espera la lista de argumentos en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void lista_argumentos_formales() throws IOException{

        if(currentToken.getType() == LEFT_PAREN){
            argumento_formal();
            lista_argumentos_formales_pr();
        }else{
            throw new IOException("Se espera una lista de argumentos formales en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_argumentos_formales_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type==COMMA){
            macheo(COMMA);
            lista_argumentos_formales();
        }else{
            if(type==RIGHT_PAREN){
                return;
            }else{
                throw new IOException("Se espera un nuevo argumento o la finalizacion de la lista en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }
    private void argumento_formal() throws IOException{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            tipo();
            macheo(IDOBJETS);
        }else{
            throw new IOException("Se espera un argumento formal en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());

        }
    }

    private void tipo_metodo() throws IOException{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            tipo();
        }else{
            if (type == VOID){
                macheo(VOID);
            }else{
                throw new IOException("Se espera un tipo en la declaracion del metodo, en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());

            }
        }
    }

    private void tipo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == STR || type == BOOL || type == INT || type == DOUBLE){
            tipo_primitivo();
        }else{
            if(type == IDCLASS){
                tipo_referencia();
            }else{
                if(type == ARRAY){
                    tipo_arreglo();
                }else{
                    throw new IOException("Se espera un tipo valido en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                }
            }
        }
    }

    private void tipo_primitivo() throws IOException{
        TokenType type = currentToken.getType();
        if(type==STR){
            macheo(STR);
        }else{
            if(type==BOOL){
                macheo(BOOL);
            }else{
                if(type==INT){
                    macheo(INT);
                }else{
                    if(type==DOUBLE){
                        macheo(DOUBLE);
                    }else{
                        throw new IOException("Se espera un tipo primitivo en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                    }
                }
            }
        }
    }

    private void tipo_referencia() throws IOException{
        if(currentToken.getType()==IDCLASS){
            macheo(IDCLASS);
        }else{
            throw new IOException("Se espera un tipo de referecia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void tipo_arreglo() throws IOException{
        if(currentToken.getType() == ARRAY){
            macheo(ARRAY);
        }else{
            throw new IOException("Se espera un tipo arreglo en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void sentencia() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IF){
            macheo(IF);
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
            sentencia();
            sentencia_else();
        }else{
            if(type==WHILE){
                macheo(WHILE);
                macheo(LEFT_PAREN);
                expOr();
                macheo(RIGHT_PAREN);
                sentencia();
            }else{
                if(type==RET){
                    macheo(RET);
                    ExpOr_factorizado();
                    macheo(SEMICOLON);
                }else{
                    if(type==IDOBJETS || type==SELF){
                        asignacion();
                        macheo(SEMICOLON);
                    }else{
                        if(type==LEFT_PAREN){
                           sentencia_simple();
                           macheo(SEMICOLON);
                        }else{
                            if(type==LEFT_BRACE){
                                bloque();
                            }else{
                                if(type==SEMICOLON){
                                    macheo(SEMICOLON);
                                }else{
                                    throw new IOException("Se espera una sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void ExpOr_factorizado() throws IOException{
        TokenType type = currentToken.getType();
        if (type==IDCLASS || type==IDOBJETS || type==PLUS || type==MINUS || type==NOT || type==PLUS_PLUS || type==MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == STRING_LITERAL || type==SELF || type == NEW || type == LEFT_PAREN){
            expOr();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }
//////////////////////////////ARREGLAR///////////////////////////////////////////////////////////////////////
    private void sentencia_else()throws IOException{
        TokenType type = currentToken.getType();
        if(type == ELSE){
            macheo(ELSE);
            sentencia();
        }else{
            if( type == RIGHT_BRACE){
                return;
            }else{
                throw new IOException("Se espera la finalizacion de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void bloque() throws IOException{
        TokenType type = currentToken.getType();
        if (type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new IOException("Se espera un bloque en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void asignacion() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS){
            accesoVar_simple();
            macheo(EQUAL);
            expOr();
        }else{
            if (type == SELF){
                accesoSelf_simple();
                macheo(EQUAL);
                expOr();
            }else{
                throw new IOException("Se espera un acceso a variable o self en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void accesoVar_simple() throws IOException{
        if(currentToken.getType() == IDOBJETS){
            macheo(IDOBJETS);
            accesoVar_simple_pr();
        }else{
            throw new IOException("Se espera un identificador en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void accesoVar_simple_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT){
            encadenado_simple_recursivo();
        }else{
            if(type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
            }else{
                throw new IOException("Se espera una expresion o más identificadores en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void encadenado_simple_recursivo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT){
            accesoVar_simple();
            encadenado_simple_recursivo();
        }else{
            if(type == EQUAL){
                return;
            }else{
                throw new IOException("Se espera una acceso a variable en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void accesoSelf_simple() throws IOException{
        TokenType type = currentToken.getType();
        if(type == SELF){
            macheo(SELF);
            encadenado_simple_recursivo();
        }else{
            throw new IOException("Se espera un acceso self en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void encadeado_simple() throws IOException{
        if(currentToken.getType() == DOT){
            macheo(DOT);
            macheo(IDOBJETS);
        }else{
            throw new IOException("Se espera un punto en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void sentencia_simple() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
        }else{
            throw new IOException("Se espera un parentesis en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expOr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAnd();
            expOrPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expOrPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == OR) {
            expAnd();
            expOrPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expAnd() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expIgual();
            expAndPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expAndPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == AND) {
            expIgual();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET || type == OR) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expIgual() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expCompuesta();
            expIgualPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expIgualPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == EQUAL_EQUAL || type == NOT_EQUAL) {
            opIgual();
            expCompuesta();
            expIgualPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET || type == OR || type == AND) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expCompuesta() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAd();
            expCompuestaPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expCompuestaPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
            opCompuesta();
            expAd();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expAd() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expMul();
            expAdPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expAdPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == PLUS || type == MINUS) {
            opAd();
            expMul();
            expAdPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expMul() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expUn();
            expMulPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expMulPr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == MULT || type == SLASH || type == DIV || type == PERCENTAGE) {
            opMul();
            expUn();
            expMulPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == LEFT_PAREN || type == LEFT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expUn() throws IOException{
        TokenType type = currentToken.getType();
        if (type == LEFT_PAREN) {
            peekToken();
            if (lookaheadToken.getType() == INT){
                opUnario();
                expUn();
            }else {
                operando();
            }
        } else {
            if (type == IDCLASS || type == IDOBJETS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW) {
                operando();
            } else {
                if (type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS) {
                    opUnario();
                    expUn();
                } else {
                    throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                }
            }
        }
    }

    private void opIgual() throws IOException{
        if (currentToken.getType() == EQUAL_EQUAL){
            macheo(EQUAL_EQUAL);
        }else {
            if (currentToken.getType() == NOT_EQUAL) {
                macheo(NOT_EQUAL);
            }else{
                throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void opAd() throws IOException{
        if (currentToken.getType() == PLUS){
            macheo(PLUS);
        }else {
            if (currentToken.getType() == MINUS) {
                macheo(MINUS);
            } else {
                throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void opCompuesta() throws IOException{
        if (currentToken.getType() == GREATER){
            macheo(GREATER);
        }else {
            if (currentToken.getType() == GREATER_EQUAL) {
                macheo(GREATER_EQUAL);
            }else{
                if (currentToken.getType() == LESS) {
                    macheo(LESS);
                }else {
                    if (currentToken.getType() == LESS_EQUAL) {
                        macheo(LESS_EQUAL);
                    } else {
                        throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                    }
                }
            }
        }
    }

    private void opUnario() throws IOException{
        if (currentToken.getType() == PLUS){
            macheo(PLUS);
        }else {
            if (currentToken.getType() == MINUS) {
                macheo(MINUS);
            }else{
                if (currentToken.getType() == NOT) {
                    macheo(NOT);
                }else {
                    if (currentToken.getType() == PLUS_PLUS) {
                        macheo(PLUS_PLUS);
                    } else {
                        if (currentToken.getType() == MINUS_MINUS) {
                            macheo(MINUS_MINUS);
                        } else {
                            if (currentToken.getType() == LEFT_BRACE) {
                                macheo(LEFT_BRACE);
                                macheo(INT);
                                macheo(RIGHT_PAREN);
                            } else {
                                throw new IOException("Se espera la una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                            }
                        }
                    }
                }
            }
        }
    }

    private void opMul() throws IOException{
        if (currentToken.getType() == MULT){
            macheo(MULT);
        }else {
            if (currentToken.getType() == SLASH) {
                macheo(SLASH);
            }else{
                if (currentToken.getType() == PERCENTAGE) {
                    macheo(PERCENTAGE);
                }else {
                    if (currentToken.getType() == DIV) {
                        macheo(DIV);
                    } else {
                        throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                    }
                }
            }
        }
    }

    private void operando() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == LEFT_PAREN  || type == SELF || type == NEW){
            primario();
            encadenado_factorizado();
        }else{
            if (type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL){
                literal();
            }else {
                throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void encadenado_factorizado() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            encadenado();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == LEFT_BRACKET || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void literal() throws IOException{
        if (currentToken.getType() == NIL){
            macheo(NIL);
        }else {
            if (currentToken.getType() == TRUE) {
                macheo(TRUE);
            }else{
                if (currentToken.getType() == FALSE) {
                    macheo(FALSE);
                }else {
                    if (currentToken.getType() == INTEGER_LITERAL) {
                        macheo(INTEGER_LITERAL);
                    } else {
                        if (currentToken.getType() == STRING_LITERAL) {
                            macheo(STRING_LITERAL);
                        } else {
                            if (currentToken.getType() == DOUBLE_LITERAL) {
                                macheo(DOUBLE_LITERAL);
                            } else {
                                throw new IOException("Se espera la una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                            }
                        }
                    }
                }
            }
        }
    }

    private void primario() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            expresionParentizada();
        }else{
            if(type==SELF){
                accesoSelf();
            }else{
                if(type==IDOBJETS){
                    macheo(IDOBJETS);
                    id_factor();
                }else{
                    if(type==IDCLASS){
                        llamada_metodo_estatico();
                    }else{
                        if(type==NEW){
                            llamada_conclasor();
                        }else{
                            throw new IOException("Se espera una sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
                        }
                    }
                }
            }
        }
    }

    private void expresionParentizada() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void accesoSelf() throws IOException{
        TokenType type = currentToken.getType();
        if(type == SELF ) {
            macheo(SELF);
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void accesoVar_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA  || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == DIV || type == PERCENTAGE){
            encadenado_factorizado();
        }else{
            if (type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
                encadenado_factorizado();
            }else {
                throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void llamada_metodo() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            argumentos_actuales();
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void llamada_metodo_estatico() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS ) {
            macheo(IDCLASS);
            macheo(DOT);
            macheo(IDOBJETS);
            llamada_metodo();
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void llamada_conclasor() throws IOException{
        TokenType type = currentToken.getType();
        if(type == NEW ) {
            macheo(NEW);
            llamada_conclasor_pr();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void llamada_conclasor_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS){
            macheo(IDCLASS);
            argumentos_actuales();
            encadenado_factorizado();
        }else{
            if (type == STR || type == DOUBLE || type == INT || type == BOOL){
                tipo_primitivo();
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
            }else {
                throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void argumentos_actuales() throws IOException{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN) {
            macheo(LEFT_PAREN);
            lista_expresiones_factorizado();
            macheo(RIGHT_PAREN);
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_expresiones_factorizado() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            lista_expresiones();
        }else {
            if (type == RIGHT_PAREN) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void lista_expresiones() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            expOr();
            lista_expresiones_pr();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_expresiones_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == COMMA) {
            macheo(COMMA);
            lista_expresiones();
        }else {
            if (type == RIGHT_PAREN) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void encadenado() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            macheo(DOT);
            encadenado_pr();
        }else {
            throw new IOException("Se espera una experesion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void encadenado_pr() throws IOException{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS) {
            macheo(IDOBJETS);
            id_factor();
        }else {
            throw new IOException("Se espera una experesion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void id_factor() throws IOException{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == LEFT_BRACKET || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == LESS || type == GREATER || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV ) {
            accesoVar_pr();
        }else {
            if (type == LEFT_PAREN) {
                llamada_metodo();
            } else {
                throw new IOException("Se espera una experesion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

 




}























