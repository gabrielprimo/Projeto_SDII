/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva
*/
package Server_side;

import Client_side.*;
import static java.lang.Math.pow;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.IntToDoubleFunction;

public class RSA {
    int p, q, n, toti_n, e, d;
    
    public int get_private_key(){
        return this.d;
    }
    
    public ArrayList<Integer> get_public_keys(){
        ArrayList<Integer> vec = new ArrayList();
        vec.add(this.n);
        vec.add(this.e);
        return vec;
    }
    
    public ArrayList<Integer> chy(String string, int e, int n){
        
        int tam = string.length();
        int i = 0;
        int k, aux;
        char letter;
        ArrayList<Integer> lista = new ArrayList();
        while(i < tam){
            letter = string.charAt(i);
            k = ord(letter);
            BigInteger big = new BigInteger(Integer.toString(k));
            BigInteger big_e = new BigInteger(Integer.toString(e));
            BigInteger big_n = new BigInteger(Integer.toString(n));
            lista.add(big.modPow(big_e, big_n).intValue());
            i++;
        }
        return lista;
    }
    
    public String descifra(ArrayList<Integer> lista, int n, int d){
        String word = "";
        int i = 0;
        int tamanho = lista.size();
        int texto;
        char letra;
        BigInteger big_n = new BigInteger(Integer.toString(n));
        BigInteger big_d = new BigInteger(Integer.toString(d));
        while (i < tamanho){
            BigInteger result = new BigInteger(Integer.toString(lista.get(i)));
            texto = result.modPow(big_d, big_n).intValue();
            letra = chr(texto);
            word = word + letra;
            i++;
        }
        return word;
    }
    
    public int totient(int n){
        if(isprime(n)){
            return n - 1;
        }
        return -1;
    }
    
    public boolean isprime(int n){
        
        if (n <= 1)  return false;
        if (n <= 3)  return true;
        if (n%2 == 0 || n%3 == 0) return false;

        int i = 5;
        while(i * i <= n){
            if (n%i == 0 || n%(i+2) == 0){
            return false;
            }
            i = i + 6;
        }
    return true;
    }
    
    public int generate_e(int num){
        Random gerador = new Random();
        int generated;
        while(true){
            generated = gerador.nextInt(num);            
            if(this.mdc(num, generated) == 1 && generated > 1 && generated < num){
                return generated;
            }
        }
    }
    
    public int mdc(int x, int y){
        int rest = 1;
        while(y != 0){
            rest = x%y;
            x = y;
            y = rest;
        }
        return x;
    }
    
    public int ord(char c){
        return (int) c;
    }
    
    public char chr(int i){
        return (char) i;
    }
    
    public int generate_prime(){
        int e;
            do{
                e = generate_random(1, 60);
            }while(!isprime(e));
        return e;
    }
    
    public int generate_random(int from, int to){
        Random gerador = new Random();
	return from + (gerador.nextInt()%to);
    }
    
    public long mod(BigInteger x, int y){
        BigInteger aux = new BigInteger(Integer.toString(y));
        if(x.compareTo(aux)==-1){
            return x.longValue();
        }
        return x.longValue()%y;
    }
    
    public int calculate_private_key(int phi_de_n, int e){
        int d = 1;
       
        while((d*e) % (phi_de_n) != 1){
            
            d += 1;
        }
        return d;
    }
    
}
