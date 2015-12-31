package br.com.fatec;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Tiro {

    private int x, y;
    private boolean estaAtivo;
    private int iw, ih;
    private Image icon;
    private String usuario;
    private Shooter player1;
    private Shooter player2 = null;
    public void setarUsuario(){
    	this.usuario = player1.getUsuario();
    }
    public String retornarUsuario(){
    	return this.usuario;
    }
    public Tiro(Dimension a, Shooter at, boolean superTiro) {
    	player1 = at;
        if (Game.TEMA == 1) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("tiro2.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("tiro3.png")).getImage();
            }
        } else if (Game.TEMA == 2) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("tiro4.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("tiro5.png")).getImage();
            }
        } else if (Game.TEMA == 9) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("torta.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("torta2.png")).getImage();
            }
        }
        iw = icon.getWidth(null);
        ih = icon.getHeight(null);
        setarUsuario();
        x = at.getX();
        y = at.getY() - at.getHeight() / 20;
        estaAtivo = true;
    }
    public Tiro(Dimension a, Shooter at, boolean superTiro, Shooter at2) {
    	player1 = at;
    	player2 = at2;
        if (Game.TEMA == 1) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("tiro2.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("tiro3.png")).getImage();
            }
        } else if (Game.TEMA == 2) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("tiro4.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("tiro5.png")).getImage();
            }
        } else if (Game.TEMA == 9) {
            if (!superTiro) {
                icon = new ImageIcon(getClass().getResource("torta.png")).getImage();
            } else {
                icon = new ImageIcon(getClass().getResource("torta2.png")).getImage();
            }
        }
        iw = icon.getWidth(null);
        ih = icon.getHeight(null);

        x = at.getX();
        y = (at.getY() - at.getHeight()) / 6;
        estaAtivo = true;
    }

    public void move() {
        if (!estaAtivo) {
            return;
        }
       
        	y = y + 3;
    
        if (y <= 0 || y > Game.altura) {
            estaAtivo = false;
        }
    }
    public void moveCon() {
        if (!estaAtivo) {
            return;
        }
        
        	y = y - 3;
       
        if (y <= 0 || y > Game.altura) {
            estaAtivo = false;
        }
    }
    public void draw(Graphics g) {
        if (estaAtivo) {
            g.drawImage(icon, x - iw / 2, y - ih / 2, null);
        }
    }

    public boolean estaAtivo() {
        return estaAtivo;
    }
    public void setEstaAtivo(boolean a){
    	this.estaAtivo = a;
    }

    public boolean acertouEm(Invader i, boolean superTiro) {
        int ox = i.getX();
        int oy = i.getY();
        int limite;
        if (!superTiro) {
            limite = 15;
        } else {
            limite = 100;
        }
        if (Math.sqrt((x - ox) * (x - ox) + (y - oy) * (y - oy)) < limite) {
            estaAtivo = false;
            return true;
        } else {
            return false;
        }
    }
    public boolean acertouEmPlayer(Shooter i, boolean superTiro) {
        int ox = i.getX();
        int oy = i.getY();
        int limite;
        if (!superTiro) {
            limite = 15;
        } else {
            limite = 100;
        }
        if (Math.sqrt((x - ox) * (x - ox) + (y - oy) * (y - oy)) < limite) {
            //estaAtivo = false;
            return true;
        } else {
            return false;
        }
    }
}
