package br.com.fatec;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Invader {
    // Posição e “velocidade�? do UFO em pixels.

    private int x, y;
    private int dx, dy;
    // Tamanho do UFO em pixels.
    private int iw, ih;
    // Imagem do UFO.
    private Image icon;
    // área do painel do jogo (para controlar movimento).
    private Dimension area;
    private boolean estaAtivo;
// Construtor, inicializa atributos e posiciona o UFO.

    public Invader(Dimension a) {
        area = a;
        if (Game.TEMA == 1) {
            icon = new ImageIcon(getClass().getResource("nave5.png")).getImage();
        } else if (Game.TEMA == 2){
            icon = new ImageIcon(getClass().getResource("nave7.png")).getImage();
        } else if (Game.TEMA == 9){
            icon = new ImageIcon(getClass().getResource("dilma2.png")).getImage();
        } else {
            icon = new ImageIcon(getClass().getResource("nave5.png")).getImage();            
        }
        iw = icon.getWidth(null);
        ih = icon.getHeight(null);
        // x e y calculados usando a área do jogo.
        x = (int) (iw / 2 + Math.random() * (a.width - iw));
        y = (int) (ih / 2 + Math.random() * (a.height - 100 - ih));
        // dx e dy aleatórios.
        while (dx == 0 || dy == 0) {
            dx = 3 - (int) (Math.random() * 6);
            dy = 2 - (int) (Math.random() * 4);
        }
        estaAtivo = true;
    }
    // Método que movimenta o UFO, verificando se está na área válida.

    public void move() {
        x += dx;
        y += dy;
        if (x < iw / 2) {
            dx = -dx;
            x += dx;
        }
        if (y < ih / 2) {
            dy = -dy;
            y += dy;
        }
        if (x > area.width - iw / 2) {
            dx = -dx;
            x += dx;
        }
        if (y > area.height - 100 - ih / 2) {
            dy = -dy;
            y += dy;
        }
    }
    // Método que desenha o UFO em um contexto gráfico.

    public void draw(Graphics g) {
        if (estaAtivo) {
            g.drawImage(icon, x - iw / 2, y - ih / 2, null);
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void desativa() {
        estaAtivo = false;
    }

    public boolean estaAtivo() {
        return estaAtivo;
    }

    int getHeight() {
        return y;
    }
}