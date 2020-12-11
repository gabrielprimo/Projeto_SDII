/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva
*/
package Client_side;

import java.util.ArrayList;

public class Decup {
    
    static ArrayList<Integer> decupar(String str){
        ArrayList<Integer> lista = new ArrayList();
        String aux = "";
        int tam = str.length();
        int i= 0;
        char letra;
        while(i < tam){
            letra = str.charAt(i);
            if(letra != '[' &&  letra != ',' && letra != ']' && letra != ' '){
                aux = aux + letra;
            }else if(letra != '[' && letra != ' '){
                lista.add(Integer.parseInt(aux));
                aux = "";
            }
            ++i;
        }
        
        return lista;
    }
}
