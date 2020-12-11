/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
*/
package Client_side;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente{
    public static void main(String[]args)
	throws UnknownHostException,IOException{
	//dispara cliente
	new Cliente().executa();
	}
    private String host;
    private int	porta;
    private int id;
    private RSA rsa = new RSA();
    private int p;
    private int q;
    private int n;
    private int y;
    private int x;
    private int phi_de_N;
    private int e;
    private int pub_key[] = new int[2];
    
    public Cliente() {
	//this.host = host;
	//this.porta      = porta;
        this.p          = rsa.generate_prime();
        this.q          = rsa.generate_prime();
        this.n          = p*q;
        this.y          = rsa.totient(p);
        this.x          = rsa.totient(q);
        this.phi_de_N   = x * y;
        this.e          = rsa.generate_e(phi_de_N);
        this.pub_key[0] = n;
        this.pub_key[1] = e;
    }
    public void	executa()throws	UnknownHostException, IOException{
        BufferedReader br = new BufferedReader(new FileReader("c:/config_host.txt"));
        String host = "";
        while(br.ready()){
            host = br.readLine();
            System.out.println(host);
        }
        br.close();
        //System.out.println(">>>" + host);
        BufferedReader br_ = new BufferedReader(new FileReader("c:/config_porta.txt"));
        String porta = "";
        while(br_.ready()){
            porta = br_.readLine();
            System.out.println(porta);
        }
        br.close();
        //System.out.println(">>>" + porta);
        
	Socket	cliente	= new Socket(host, Integer.parseInt(porta));
	
	PrintStream saida = new	PrintStream(cliente.getOutputStream());
        
        saida.println("N" + this.n);
        
        saida.println("E" + this.e);
        
        int d = rsa.calculate_private_key(phi_de_N, e);
        
        System.out.println("CHAVES PUBLICAS N: " + n + " E: " + e);
        
        System.out.println("CHAVE PRIVADA: " + d);
        
        //classe que irá tratar as respostas
        Recebedor r = new Recebedor(cliente.getInputStream(), rsa, d, this.n, saida);
	new Thread(r).start();
        								
    }
}
