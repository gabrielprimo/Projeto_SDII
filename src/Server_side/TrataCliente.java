/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva
*/
package Server_side;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrataCliente implements Runnable	{
    private InputStream	cliente;
    private Servidor servidor;
    private Timer timer;
    private int[] status = new int[3];
    private int n = 1;
    private int e = 2;
    private int _n;
    private int _e;
    private int index;
    public int d;
    public RSA rsa;
    public boolean logado = false; 
    
    public TrataCliente(InputStream cliente, Servidor servidor, int[] status, int i, RSA rsa, int d, int _n, int _e) {
        this.cliente = cliente;
        this.servidor =	servidor;
        this.status = status;      
        this.index = i;
        this.rsa = rsa;
        this.d = d;
        this._n = _n;
        this._e = _e;
        
    }
    public void	run() {
        Scanner	s = new	Scanner(this.cliente);
        String auxx;
        ArrayList<Integer> lista;
        String orig;
        char tipo;
        while(s.hasNextLine())	{
            String request = s.nextLine();
            tipo =  request.charAt(0);
            if(tipo != '['){
                
                switch (tipo) {
                    case 'N':
                        this.n = Integer.parseInt(request.substring(1));
                        System.out.println("Cliente passou chava publica N: " + this.n);
                        servidor.ch_pub_n.add(this.n);
                        servidor.responde_cliente('N', this.index, this.n, this.e);                 
                        break;

                    case 'E':
                        this.e = Integer.parseInt(request.substring(1));
                        System.out.println("Cliente passou chava publica E: " + this.e);
                        servidor.ch_pub_e.add(this.e);
                        servidor.responde_cliente('E', this.index, this.n, this.e);                 
                        break;
                    default:
                        System.out.println("ERRO_1");
                        break;    
                }
            }else{
               
                System.out.println("request cifrada : " + request);
                lista = Decup.decupar(request);
                System.out.println("N: " + _n + " D: " + d);
                orig = rsa.descifra(lista, _n, d);
                System.out.println("request decifrada: " + orig);
                tipo = orig.charAt(0);
                
                switch(tipo){
                    case 'L':
                        System.out.println("Tentou login : " + orig);
                        
                        if(!valida(orig)){
                            
                            System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
                            System.out.println("Falhou login : " + orig);
                            System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
                            try {
                                this.finalize();
                            } catch (Throwable ex) {
                                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            System.out.println("conseguiu login : " + orig);
                            System.out.println("=======================================\n");
                            servidor.us_logados.add(index, true); 
                            servidor.responde_cliente('L', index, this.n, this.e);
                        }
                        break;

                    case 'U':
                        String nrecurso = orig.substring(1);
                        System.out.println("Solicitando uso do Recurso : " + (nrecurso + 1));
                        int i = Integer.parseInt(nrecurso);
                        this.timer = new Timer(status, this.servidor, i);
                        status[i] = 1;
                        servidor.broadcast("A");
                        new Thread(timer).start();
                        break;

                    default:
                        System.out.println("ERRO _2");
                        break;
                }
            }
        }
    s.close();
    }
    public boolean valida(String orig){
        Integer hash = hash(orig);
        return servidor.usuarios.contains(hash); 
    }
    
    public Integer hash(String orig){
        Integer h = 0;
        int i = 0; 
        int tam = orig.length();
        while(i < tam){
            h = h + (int)orig.charAt(i);
            ++i;
        }
        System.out.println("Hash::" + h);
        return h; 
    }
}
