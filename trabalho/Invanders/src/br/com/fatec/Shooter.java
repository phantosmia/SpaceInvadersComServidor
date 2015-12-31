package br.com.fatec;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Shooter implements Comparable<Shooter>,Serializable{
	// Posição do defensor em pixels.
	
	private  String ranking;
	private double pontos;
	public double getPontos() {
		return pontos;
	}

	public void setPontos(double pontos) {
		this.pontos = pontos;
	}

	private int x, y;
	private boolean estaAtivo;
public boolean isEstaAtivo(){
	return estaAtivo;
}
	public int getIw() {
		return iw;
	}

	public void setIw(int iw) {
		this.iw = iw;
	}

	public int getIh() {
		return ih;
	}

	public void setIh(int ih) {
		this.ih = ih;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public static Dimension getArea() {
		return area;
	}

	public static void setArea(Dimension area) {
		Shooter.area = area;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String usuario;
	// Tamanho do defensor em pixels.
	private int iw, ih;
	// Imagem do defensor.
	private Image icon;
	// área do painel do jogo (para controlar movimento).
	protected static Dimension area;
	// Construtor, inicializa atributos e posiciona o shooter.

	public Shooter(Dimension a) {
		area = a;
		if (Game.TEMA == 1) {
			icon = new ImageIcon(getClass().getResource("nave6.png")).getImage();
		} else if (Game.TEMA == 2) {
			icon = new ImageIcon(getClass().getResource("nave8.png")).getImage();
		} else if (Game.TEMA == 9) {
			icon = new ImageIcon(getClass().getResource("face.png")).getImage();
		} else {
			icon = new ImageIcon(getClass().getResource("nave6.png")).getImage();
		}
		estaAtivo = true;
		iw = icon.getWidth(null);
		ih = icon.getHeight(null);
		// x e y iniciais centrados na área de movimentação.
		x = (int) (iw / 2 + (a.width - iw) / 2);
		if (Game.runningOnline == false)
			y = (int) (a.height - 100 + ih / 2);
		else
			y = (int) ((a.height / 2) + ih / 2);
	}

	public void desativa() {
		estaAtivo = false;
	}

	// Método que movimenta o shooter, verificando se está na área válida.
	public Shooter(Dimension a, String usuario) {
		area = a;
		estaAtivo = true;
		this.usuario = usuario;
		if (Game.TEMA == 1) {
			icon = new ImageIcon(getClass().getResource("nave6.png")).getImage();
		} else if (Game.TEMA == 2) {
			icon = new ImageIcon(getClass().getResource("nave8.png")).getImage();
		} else if (Game.TEMA == 9) {
			icon = new ImageIcon(getClass().getResource("face.png")).getImage();
		} else {
			icon = new ImageIcon(getClass().getResource("nave6.png")).getImage();
		}
		iw = icon.getWidth(null);
		ih = icon.getHeight(null);
		// x e y iniciais centrados na área de movimentação.
		x = (int) (iw / 2 + (a.width - iw) / 2);
		if (Game.runningOnline == false)
			y = (int) (a.height - 100 + ih / 2);
		else
			y = (int) ((a.height / 2) + ih / 2);
	}

	public void move(Direcao dir) {
		if (dir == null) {
			return;
		}
		switch (dir) {
		case LEFT: {
			x--;
			if (x < iw / 2) {
				x = iw / 2;
			}
			break;
		}
		case RIGHT: {
			x++;
			if (x > area.width - iw / 2) {
				x = area.width - iw / 2;
			}
			break;
		}
		case UP: {
			y--;
			if (Game.runningOnline == false) {
				if (y < area.height - 100 + ih / 2) {
					y = area.height - 100 + ih / 2;
				}
			} else {
				if (y < (area.height / 2) + ih / 2) {
					y = (area.height / 2) + ih / 2;
				}
			}
			break;
		}
		case DOWN: {
			y++;
			if (y > area.height - ih / 2) {
				y = area.height - ih / 2;
			}
			break;
		}
		}
		if (Game.runningOnline == true) {
			Packet02Move packet = new Packet02Move(this.getUsuario(), this.getX(), this.getY());
			packet.writeData(InvadersPanel.invadersPanel.socketClient);
		}
	}

	public void moveOnline(Direcao dir, String user) {
		if (dir == null) {
			return;
		}
		switch (dir) {
		case LEFT: {
			x--;
			if (x < iw / 2) {
				x = iw / 2;
			}
			break;
		}
		case RIGHT: {
			x++;
			if (x > area.width - iw / 2) {
				x = area.width - iw / 2;
			}
			break;
		}
		case UP: {
			if (user == getUsuario()) {
				y--;
				if (Game.runningOnline == false) {
					if (y < area.height - 100 + ih / 2) {
						y = area.height - 100 + ih / 2;
					}
				} else {
					if (y < (area.height / 2) + ih / 2) {
						y = (area.height / 2) + ih / 2;
					}
				}
				break;
			}
			else {
				y--;
				if (Game.runningOnline == false) {
					if (y < area.height - 100 + ih / 2) {
						y = area.height - 100 + ih / 2;
					}
				} else {
					if (y < (area.height / 2) + ih / 2) {
						y = (area.height / 2) + ih / 2;
					}
				}
				break;
			}
		}
		case DOWN: {
			y++;
			if (y > area.height - ih / 2) {
				y = area.height - ih / 2;
			}
			break;
		}
		}
		if (Game.runningOnline == true) {
			Packet02Move packet = new Packet02Move(this.getUsuario(), this.getX(), this.getY());
			packet.writeData(InvadersPanel.invadersPanel.socketClient);
		}
	}
	// Método que desenha o shooter em um contexto gráfico.

	public void draw(Graphics g) {
		if (estaAtivo) {
			g.drawImage(icon, x - iw / 2, y - ih / 2, null);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return y;
	}

	public boolean equals(Object o) {
        if (!(o instanceof Shooter))
            return false;
        Shooter person = (Shooter) o;
        return person.ranking.equals(ranking) && person.pontos == pontos;
    }

    // public int hashCode() {
    //    return your hash;
    // }

    public String toString(){
        return "Rank: " + ranking +", points = " + pontos ;
    }

    public int compareTo(Shooter person) {
        return pontos > person.pontos ? 1 : pontos < person.pontos ? -1 : 0;
    }
}