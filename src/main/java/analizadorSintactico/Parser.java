package analizadorSintactico;

import ErrorManage.ErrorTiny;
import analizadorLexico.ErrorLex;
import ErrorManage.ErrorTiny;
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

    public void setEscaner(Escaner escaner){
        this.escaner = escaner;
    }

    public void setCurrentToken (Token currentToken) {
        this.currentToken = currentToken;
    }

    public void macheo(TokenType tokenType) throws IOException, ErrorTiny {
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
    private void peekToken() throws IOException, ErrorTiny {

        if (!hasLookahead) {
            lookaheadToken = escaner.nextToken();
            hasLookahead = true;
        }
    }

    public boolean s() throws ErrorTiny, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL){
            program();
            macheo(EOF);
            return true;
        }
        throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba class/impl, se encontró " + currentToken.getLexema());
    }

    private void program() throws ErrorTiny, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL || type == START){
            lista_definiciones();
            start();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba class/impl/start, se encontró " + currentToken.getLexema());        }
    }
    private void start() throws ErrorTiny, IOException {
        if (currentToken.getType() == START){
            macheo(START);
            bloque_metodo();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba start, se encontró " + currentToken.getLexema());        }
    }


    private void lista_definiciones() throws ErrorTiny, IOException {
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
                        throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba start/class/impl, se encontró " + currentToken.getLexema());                    }
            }
        }

    }

    private void class_lista_recursivo() throws IOException, ErrorTiny {
        if (currentToken.getType() == CLASS){
            clas();
            lista_factorizacion();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba class, se encontró " + currentToken.getLexema());        }
    }

    private void impl_lista_recursivo() throws IOException, ErrorTiny {
        if(currentToken.getType()==IMPL){
            impl();
            lista_factorizacion();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba impl, se encontró " + currentToken.getLexema());
        }
    }


    private void lista_factorizacion() throws IOException, ErrorTiny {
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
                    throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba class/impl/start, se encontró " + currentToken.getLexema());                }
            }
        }
    }

    private void clas() throws IOException, ErrorTiny {
        if(currentToken.getType()==CLASS){
            macheo(CLASS);
            macheo(IDCLASS);
            clas_factorizado();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba class, se encontró " + currentToken.getLexema());        }
    }

    private void clas_factorizado() throws IOException, ErrorTiny {
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
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba {/:, se encontró " + currentToken.getLexema());            }
        }
    }

    private void atributo_class_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();

        if(type == IDCLASS || type == PUB || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            atributo();
            atributo_class_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/pub/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());            }
        }
    }

    private void impl() throws IOException, ErrorTiny {
        if(currentToken.getType() == IMPL){
            macheo(IMPL);
            macheo(IDCLASS);
            macheo(LEFT_BRACE);
            miembro();
            miembro_impl_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba impl, se encontró " + currentToken.getLexema());        }
    }

    private void miembro_impl_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == FN || type == ST || type == DOT){
            miembro();
            miembro_impl_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba fn/st/./}, se encontró " + currentToken.getLexema());            }
        }
    }
    private void herencia() throws IOException, ErrorTiny {
        if(currentToken.getType() == DOBLE_DOT){
            macheo(DOBLE_DOT);
            tipo();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba :, se encontró " + currentToken.getLexema());        }
    }

    private void miembro() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == FN|| type == ST){
            metodo();
        }else{
            if(type == DOT){
                constructor();
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba fn/st/., se encontró " + currentToken.getLexema());            }
        }
    }

    private void constructor() throws IOException, ErrorTiny {
        if (currentToken.getType()==DOT){
            macheo(DOT);
            argumentos_formales();
            bloque_metodo();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba ., se encontró " + currentToken.getLexema());        }
    }

    private void atributo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo();
            lista_declaraciones_variables();
            macheo(SEMICOLON);
        }else{
            if(type == PUB){
                visibilidad();
                tipo();
                lista_declaraciones_variables();
                macheo(SEMICOLON);
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array/pub, se encontró " + currentToken.getLexema());            }
        }
    }

    private void metodo() throws IOException, ErrorTiny {
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
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba fn/st, se encontró " + currentToken.getLexema());            }
        }
    }

    private void tipo_metodo_factorizacion() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == VOID || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo_metodo();
        }else{
            if(type == IDOBJETS){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array/void/idMetodo/idAtributo, se encontró " + currentToken.getLexema());              }
        }
    }

    private void visibilidad() throws IOException, ErrorTiny {
        if (currentToken.getType() == PUB){
            macheo(PUB);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba pub, se encontró " + currentToken.getLexema());          }
    }

    private void forma_metodo() throws IOException, ErrorTiny {
        if (currentToken.getType() == ST){
            macheo(ST);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba st, se encontró " + currentToken.getLexema());          }
    }

    private void bloque_metodo() throws IOException, ErrorTiny {
        if(currentToken.getType()==LEFT_BRACE){
            macheo(LEFT_BRACE);
            decl_var_loc_bloque_recursivo();
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba {, se encontró " + currentToken.getLexema());          }
    }

    private void decl_var_loc_bloque_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();

        if(type == IDCLASS  || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY ){
            decl_var_locales();
            decl_var_loc_bloque_recursivo();
        }else{
            if(type == LEFT_BRACE || type == RIGHT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type == IF || type == WHILE || type == RET || type == IDOBJETS || type == PUB || type == NEW || type == FN || type == ST || type == DOT || type == SELF){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array/{/}/;/(/if/while/ret/idObject/pub/new/fn/st/./self, se encontró " + currentToken.getLexema());              }
        }
    }
// Corroborar id

    private void sentencia_bloque_recursivo() throws IOException, ErrorTiny {
      
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type== IF || type == WHILE || type == RET || type == IDOBJETS || type == SELF){
            sentencia();
            sentencia_bloque_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba {/;/(/if/while/ret/idObjects/self, se encontró " + currentToken.getLexema());              }
        }
    }

    private void decl_var_locales() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            tipo();
            lista_declaraciones_variables();
            macheo(SEMICOLON);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());          }
    }

    private void lista_declaraciones_variables() throws IOException, ErrorTiny {
        if(currentToken.getType()==IDOBJETS){
            macheo(IDOBJETS);
            lista_declaraciones_variables_Pr();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idObjects, se encontró " + currentToken.getLexema());          }
    }

    private void lista_declaraciones_variables_Pr() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == COMMA){
            macheo(COMMA);
            lista_declaraciones_variables();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba ,/;, se encontró " + currentToken.getLexema());              }
        }
    }

    private void argumentos_formales() throws IOException, ErrorTiny {
        if(currentToken.getType()==LEFT_PAREN){
            macheo(LEFT_PAREN);
            lista_argumentos_formales_factorizado();
            macheo(RIGHT_PAREN);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba (, se encontró " + currentToken.getLexema());          }
    }

    private void lista_argumentos_formales_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            lista_argumentos_formales();
        }else{
            if(type == RIGHT_PAREN){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());              }
        }
    }

    private void lista_argumentos_formales() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            argumento_formal();
            lista_argumentos_formales_pr();
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());          }
    }

    private void lista_argumentos_formales_pr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==COMMA){
            macheo(COMMA);
            lista_argumentos_formales();
        }else{
            if(type==RIGHT_PAREN){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba ,/), se encontró " + currentToken.getLexema());              }
        }
    }
    private void argumento_formal() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            tipo();
            macheo(IDOBJETS);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());
        }
    }

    private void tipo_metodo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            tipo();
        }else{
            if (type == VOID){
                macheo(VOID);
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array/void, se encontró " + currentToken.getLexema());
            }
        }
    }

    private void tipo() throws IOException, ErrorTiny{
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
                    throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/Str/Bool/Int/Double/Array, se encontró " + currentToken.getLexema());                  }
            }
        }
    }

    private void tipo_primitivo() throws IOException, ErrorTiny{
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
                        throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba Str/Bool/Int/Double, se encontró " + currentToken.getLexema());                      }
                }
            }
        }
    }

    private void tipo_referencia() throws IOException, ErrorTiny{
        if(currentToken.getType()==IDCLASS){
            macheo(IDCLASS);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass, se encontró " + currentToken.getLexema());          }
    }

    private void tipo_arreglo() throws IOException, ErrorTiny{
        if(currentToken.getType() == ARRAY){
            macheo(ARRAY);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba Array, se encontró " + currentToken.getLexema());          }
    }
// corroborar asignacion ID

    private void sentencia() throws IOException, ErrorTiny{

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
                                    throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba if/while/ret/idObjects/self/(/{/;, se encontró " + currentToken.getLexema());                                  }
                            }
                        }
                    }
                }
            }
        }
    }

    private void ExpOr_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type==IDCLASS || type==IDOBJETS || type==PLUS || type==MINUS || type==NOT || type==PLUS_PLUS || type==MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == STRING_LITERAL || type==SELF || type == NEW || type == LEFT_PAREN){
            expOr();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba idClass/idObjects/+/-/!/++/--/nil/true/false/int_literal/double_literal/str_literal/new/(, se encontró " + currentToken.getLexema());              }
        }
    }

    private void sentencia_else()throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == ELSE){
            macheo(ELSE);
            sentencia();
        }else{
            if( type == RIGHT_BRACE){
                return;
            }else{
                throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba else/}, se encontró " + currentToken.getLexema());              }
        }
    }

    private void bloque() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new ErrorSintactico(currentToken.getLine(), currentToken.getColumn(),"Se esperaba {, se encontró " + currentToken.getLexema());
        }
    }

    private void asignacion() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS ){
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


    private void accesoVar_simple() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type == IDOBJETS){
            macheo(IDOBJETS);
            accesoVar_simple_pr();

        }else{

            throw new IOException("Se espera un identificador en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());



        }
    }

    private void accesoVar_simple_pr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == EQUAL){
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

    private void encadenado_simple_recursivo() throws IOException, ErrorTiny{
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

    private void accesoSelf_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF){
            macheo(SELF);
            encadenado_simple_recursivo();
        }else{
            throw new IOException("Se espera un acceso self en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void encadeado_simple() throws IOException, ErrorTiny{
        if(currentToken.getType() == DOT){
            macheo(DOT);
            macheo(IDOBJETS);
        }else{
            throw new IOException("Se espera un punto en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void sentencia_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
        }else{
            throw new IOException("Se espera un parentesis en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expOr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAnd();
            expOrPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expOrPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == OR) {
            expAnd();
            expOrPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expAnd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expIgual();
            expAndPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expAndPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == AND) {
            expIgual();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expIgual() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expCompuesta();
            expIgualPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expIgualPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == EQUAL_EQUAL || type == NOT_EQUAL) {
            opIgual();
            expCompuesta();
            expIgualPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expCompuesta() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAd();
            expCompuestaPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expCompuestaPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
            opCompuesta();
            expAd();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expAd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expMul();
            expAdPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expAdPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == PLUS || type == MINUS) {
            opAd();
            expMul();
            expAdPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expMul() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expUn();
            expMulPr();
        }else{
            throw new IOException("Se espera una expresion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void expMulPr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == MULT || type == SLASH || type == DIV || type == PERCENTAGE) {
            opMul();
            expUn();
            expMulPr();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void expUn() throws IOException, ErrorTiny{
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

    private void opIgual() throws IOException, ErrorTiny{
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

    private void opAd() throws IOException, ErrorTiny{
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

    private void opCompuesta() throws IOException, ErrorTiny{
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

    private void opUnario() throws IOException, ErrorTiny{
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

    private void opMul() throws IOException, ErrorTiny{
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

    private void operando() throws IOException, ErrorTiny{
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

    private void encadenado_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            encadenado();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == LEFT_BRACKET || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV) {
                return;
            } else {
                throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
            }
        }
    }

    private void literal() throws IOException, ErrorTiny{
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

    private void primario() throws IOException, ErrorTiny{
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

    private void expresionParentizada() throws IOException, ErrorTiny{
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

    private void accesoSelf() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF ) {
            macheo(SELF);
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void accesoVar_pr() throws IOException, ErrorTiny{
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

    private void llamada_metodo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            argumentos_actuales();
            encadenado_factorizado();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void llamada_metodo_estatico() throws IOException, ErrorTiny{
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

    private void llamada_conclasor() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == NEW ) {
            macheo(NEW);
            llamada_conclasor_pr();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void llamada_conclasor_pr() throws IOException, ErrorTiny{
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

    private void argumentos_actuales() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN) {
            macheo(LEFT_PAREN);
            lista_expresiones_factorizado();
            macheo(RIGHT_PAREN);
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_expresiones_factorizado() throws IOException, ErrorTiny{
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

    private void lista_expresiones() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            expOr();
            lista_expresiones_pr();
        }else {
            throw new IOException("Se espera una experesion o la finalización de la sentencia en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void lista_expresiones_pr() throws IOException, ErrorTiny{
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

    private void encadenado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            macheo(DOT);
            encadenado_pr();
        }else {
            throw new IOException("Se espera una experesion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void encadenado_pr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS) {
            macheo(IDOBJETS);
            id_factor();
        }else {
            throw new IOException("Se espera una experesion en línea " + currentToken.getLine() + ", columna " + currentToken.getColumn());
        }
    }

    private void id_factor() throws IOException, ErrorTiny{
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























