/*
Carlos Silva
Rafael Alan
Jose Henrique
Gabriel Primo
Pedro Pataro
Wanderley Silva
*/
package Server_side;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer implements Runnable {

    private int[] status = new int[3]; 
    private Servidor servidor;
    private int i = 0;
    public Timer(int[] status, Servidor servidor, int i){
        this.status = status; 
        this.servidor = servidor;
        this.i = i;
       }
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
        }
        status[i] = 0;
        servidor.broadcast("A");
    }
}
