package br.com.fatec;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import online.ClienteInvader;

import online.ServidorInvader;




public class Game   {
	private Thread thread;
	private boolean running;
    static int MAX_INVASORES;
    static int TEMA;
    static int altura = 600;
    static int largura = 800;
    static boolean runningOnline = false;
    public ClienteInvader socketClient;
	public ServidorInvader socketServer;
    public static void main(String args[]) throws IOException {
    	
        int tema = Integer.parseInt(JOptionPane.showInputDialog("Tema?\n1 - Super Mario\n2 - Star Wars", "1"));
       
        int nivel = Integer.parseInt(JOptionPane.showInputDialog("Nivel?\n1 - Facil\n2 - Intermediario\n3 - Dificil\n4 - Multijogador", "1"));
        if (nivel == 1) {
            MAX_INVASORES = 10;
        } else if (nivel == 2) {
            MAX_INVASORES = 20;
        } else if (nivel == 4) {
        	MAX_INVASORES = 0;
        	runningOnline = true;
        	   int servidor = Integer.parseInt(JOptionPane.showInputDialog("1- iniciar servidor \n 2- Iniciar Cliente"));
               if(servidor == 1)
                	InvadersPanel.runningServer = true;
    	} else {
            MAX_INVASORES = 30;
        
            
        }
        
        if (tema == 1) {
            TEMA = 1;
        } else if (tema == 2) {
            TEMA = 2;
        } else if (tema == 9) {
            TEMA = 9;
        } else {
            TEMA = 1;
        }
        criarTela();
    }

    public static void criarTela() throws IOException {
        JFrame frame = new JFrame("Space Invaders - Raquel e Alec");
        InvadersPanel sip = new InvadersPanel();
      
        frame.getContentPane().add(sip);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        sip.start();
    }



}