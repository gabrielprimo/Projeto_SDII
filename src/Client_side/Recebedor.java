/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva
*/
package Client_side;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Recebedor implements Runnable{
    private InputStream	servidor;
    NewJFrame j;
    public int _n;
    public int _e;
    private int d;
    private int n;
    RSA rsa;
    
    public Recebedor(InputStream servidor, RSA rsa, int d, int n, PrintStream saida) {
	this.servidor =	servidor;
        this.j = j;
        this.d = d;
        this.rsa = rsa;
        this.n = n;
        
        //subindo a interface
        this.j = new NewJFrame(saida, this.rsa, this);
        j.setVisible(true);
    }
    public void	run() {
        
        Scanner s = new Scanner(this.servidor);
        String auxx;
        ArrayList<Integer> lista;
        String orig;
        char tipo;
        while(s.hasNextLine()){
            String response = s.nextLine();
            System.out.println("Recebendo msg cifrada: " + response);
            lista = Decup.decupar(response);
            orig = rsa.descifra(lista, n, d);
            System.out.println("mensagem decifrada: " + orig);
            tipo = orig.charAt(0);
            switch(tipo){
                
                case 'L':
                    auxx = orig.substring(1);
                    System.out.println("atualizando a interface com os status das impressoras");
                    j.setCor(auxx);
                    j.enab();
                    break;
                
                case 'A':
                    System.out.println("atualizando a interface com os status das impressoras");
                    auxx = orig.substring(1);
                    j.setCor(auxx);
                    break;
                    
                case 'N':
                    auxx = orig.substring(1);
                    this._n = Integer.parseInt(auxx);
                    System.out.println("Gravando a chave publlica N: " + _n + " do Servidor");
                    break;
                    
                case 'E':
                    auxx = orig.substring(1);
                    this._e = Integer.parseInt(auxx);
                    System.out.println("Gravando a chave publlica E: " + _e + " do Servidor");
                    break;
                    
                default:
                        System.out.println("ERRO_1");
                        break;
            }
        }
    }
}
