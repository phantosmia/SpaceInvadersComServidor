package br.com.fatec;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class TiroInvader {

    private int x, y;
    private boolean estaAtivo;
    private int iw, ih;
    private Image icon;
    @SuppressWarnings("unused")
	private Dimension area;

    public TiroInvader(Dimension a, Invader at) {
        area = a;
        icon = new ImageIcon(getClass().getResource("tiro2.png")).getImage();
        iw = icon.getWidth(null);
        ih = icon.getHeight(null);

        x = at.getX();
        y = at.getY() - at.getHeight() / 6;
        estaAtivo = true;
    }

    public void move() {
        if (!estaAtivo) {
            return;
        }
        y = y + 3;
        if (y <= 0) {
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

    public boolean acertouEm(Shooter i) {
        int ox = i.getX();
        int oy = i.getY();

        if (Math.sqrt((x - ox) * (x - ox) + (y - oy) * (y - oy)) < 20) {
            estaAtivo = false;
            return true;
        } else {
            return false;
        }
    }
}
