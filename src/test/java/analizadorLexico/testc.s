class Fibonacci{
 pubIntsuma;
 pubInti,j;
 }
 implFibonacci{
 fnIntsucesion_fib(Intn){
 i=0;j=0;suma=0;
 while(i<=n){
 if(i==0){
 (imprimo_numero(i));
 (imprimo_sucesion(suma));}
 else
 if(i==1){
 (imprimo_numero(i));
 suma=suma+i;
 (imprimo_sucesion(suma));}
 else{
 (imprimo_numero(i));
 suma=suma+j;
 j=suma;
 (imprimo_sucesion(suma));
 }
 (++i);
 }
 retsuma;
 }
 .(){
 i=0; // inicializoi
 j=0; // inicializoj
 suma=0; // inicializosuma
 }
 fnimprimo_numero(Intnum){
 (IO.out_str("f_"));
 (IO.out_int(num));
 (IO.out_str("="));
 }
 fnimprimo_sucesion(Ints){
 // Elvalores:
 (IO.out_int(s));
 (IO.out_str("\n"));
 }
 }
 start{
 Fibonaccifib;
 Intn;
 fib=newFibonacci();
 n=IO.in_int();
 (IO.out_int(fib.sucesion_fib(n)));
 }