/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva 
*/
package Server_side;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author gabri
 */
public class Servidor{
    public static void main(String[] args) throws IOException{
        //inicia o servidor
        new Servidor(12345).executa();
    }
    private int	porta;
    private List<PrintStream> clientes;
    public List<Integer> ch_pub_n = new ArrayList();
    public List<Integer> ch_pub_e = new ArrayList();
    public List<Integer> usuarios = new ArrayList();
    public List<Boolean> us_logados = new ArrayList();
    private int status[] = new int[3];
    private RSA rsa = new RSA();
    private int _p;
    private int _q;
    private int _n;
    private int _y;
    private int _x;
    private int _phi_de_N;
    private int _e;
    private int _pub_key[] = new int[2];
    public int d;
    
    public Servidor (int porta) throws FileNotFoundException, IOException {
        this.porta = porta;
        this.clientes =	new ArrayList<PrintStream>();
        this.status[0] = 0;
        this.status[1] = 0;
        this.status[2] = 0;
        this._p          = rsa.generate_prime();
        this._q          = rsa.generate_prime();
        this._n          = _p * _q;
        this._y          = rsa.totient(_p);
        this._x          = rsa.totient(_q);
        this._phi_de_N   = _x * _y;
        this._e          = rsa.generate_e(_phi_de_N);
        this._pub_key[0] = _n;
        this._pub_key[1] = _e;
        System.out.println("CHAVES N: " + _n + " E: " + _e);
        /*usuarios.add(440);
        usuarios.add(428);
        usuarios.add(452);
        usuarios.add(480);*/
        BufferedReader br = new BufferedReader(new FileReader("c:/config_hash.txt"));
        String host = "";
        while(br.ready()){
            host = br.readLine();
            usuarios.add(Integer.parseInt(host));
            System.out.println(host);
        }
        br.close();
    }
    public void	executa	() throws IOException{
        this.d = rsa.calculate_private_key(_phi_de_N, _e);
	ServerSocket servidor = new ServerSocket(this.porta);
	System.out.println("Porta 12345 aberta!");
        int i = 0;
	while(true){
            
	    //aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente "+					
            cliente.getInetAddress().getHostAddress()
            );
            
	//adiciona saida do cliente à lista
	PrintStream ps	= new PrintStream(cliente.getOutputStream());
	this.clientes.add(ps);
        
        //adiciona o atual status do cliente
        this.us_logados.add(false);
        
        //cria	tratador de cliente numa nova thread
	TrataCliente tc	=	
            new	TrataCliente(cliente.getInputStream(),this, status, i, rsa, d, _n, _e);
            new	Thread(tc).start();
            ++i;
	}
    }
    
    //Faz a transmissão das mensagens a todos os clients logados
    public void	broadcast(String msg) {
        System.out.println("=======================================\n");
        System.out.println("iniciando transmissão:");
        String vet = msg + status[0] + "" + status[1] + "" + status[2];
        int index = 0;
        ArrayList<Integer> lista;
        
        for(PrintStream	cliente:this.clientes)	
        {
            if(this.us_logados.get(index))
            {
                lista = rsa.chy(vet, this.ch_pub_e.get(index), this.ch_pub_n.get(index));
                cliente.println(lista.toString());
                System.out.println("Client: " + index + " resposta enviada");
                lista.clear();
                
            }
                ++index;
        }
       
    }
    public void responde_cliente(char cr, int i, int n, int e){
        String vet = "";
        ArrayList<Integer> lista;
        
        if(cr == 'E')
        {
            //enviando chave n            
            vet = "N" + _n;
            System.out.println("utilizando as chave publicas N: " + n + "e E: " + e + " do cliente para cifrar a msg:");
            System.out.println("msg original: " + vet);
            lista = rsa.chy(vet, e, n);
            System.out.println("msg cifrada: " + lista.toString());
            clientes.get(i).println(lista.toString());
            lista.clear();
            vet = "";
            
            //enviando a chave e            
            vet = "E" + _e;
            System.out.println("msg original: " + vet);
            lista = rsa.chy(vet, e, n);
            System.out.println("msg cifrada: " + lista.toString());
            clientes.get(i).println(lista.toString());
            vet = "";
        }
        if(cr == 'L'){
            vet = "L" + status[0] + "" + status[1] + "" + status[2];
            System.out.println("msg original: " + vet);
            lista = rsa.chy(vet, e, n);
            System.out.println("msg cifrada: " + lista.toString());
            System.out.println("=======================================\n");
            clientes.get(i).println(lista.toString());
            vet = "";
        }
    }
}

        
