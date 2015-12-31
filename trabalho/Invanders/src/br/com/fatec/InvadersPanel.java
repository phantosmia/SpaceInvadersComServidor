package br.com.fatec;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import online.ClienteInvader;
import online.ServidorInvader;
@SuppressWarnings("serial")
public class InvadersPanel extends JComponent implements Runnable, KeyListener, MouseListener {
	public static boolean runningServer = false;
	public ClienteInvader socketClient;
	public ServidorInvader socketServer;
	public static InvadersPanel invadersPanel;
	private volatile boolean execute;

	public synchronized ArrayList<ShooterMP> getShooters() {
		return this.shooters;
	}

	private ArrayList<ShooterMP> shooters = new ArrayList<ShooterMP>();

	public void adicionaShooter(ShooterMP shooterMP) {
		String usuarioShooter = shooterMP.getUsuario();
		for (ShooterMP shooter2 : this.getShooters()) {
			if (!shooter2.getUsuario().equals(usuarioShooter))
				shooterMP.setY(invadersPanel.getHeight() - shooterMP.getY());
		}
		this.getShooters().add(shooterMP);

	}

	public void removerShooterMP(String usuario) {
		int index = 0;
		for (ShooterMP s : getShooters()) {
			if (s instanceof ShooterMP && ((ShooterMP) s).getUsuario().equals(usuario)) {
				break;
			}
			index++;
		}
		this.getShooters().remove(index);
	}

	public int getShooterMPIndex(String usuario) {
		int index = 0;
		for (ShooterMP s : getShooters()) {
			if (s instanceof ShooterMP && ((ShooterMP) s).getUsuario().equals(usuario)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void moverShooter(String usuario, int x, int y) {
		int index = getShooterMPIndex(usuario);
		ShooterMP shooter = (ShooterMP) this.getShooters().get(index);
		String usuarioShooter = shooter.getUsuario();
		for (ShooterMP shooter2 : this.getShooters()) {
			shooter.setX(x);
			if (shooter2.getUsuario().equals(usuarioShooter)) {
				shooter.setY(y);
			} else {
				shooter.setY(invadersPanel.getHeight() - y);
			}
			break;

		}
	}

	public void criarTiroShooter(String usuario) {
		int index = getShooterMPIndex(usuario);
		ShooterMP shooter = (ShooterMP) this.getShooters().get(index);

		Tiro tiro = new Tiro(null, shooter, false);
		/*
		 * for(Tiro t: tiros){ for(ShooterMP shooter2:this.getShooters()){
		 * if(shooter2 != null) t = new Tiro(null, shooter, false,shooter2);
		 * else break;
		 * 
		 * } tiro = t; }
		 */

		tiros.add(tiro);
		// .adicionarBalas(t);
	}

	// Dimensões da área do jogo
	private static int larguraDefinidaPeloUser = Game.largura;
	private static int alturaDefinidaPeloUser = Game.altura;
	private static final int largura = larguraDefinidaPeloUser;
	private static final int altura = alturaDefinidaPeloUser;
	// Uma thread para controlar a animação.
	private Thread animator;
	private boolean isPaused = false;
	// Teremos alguns UFOs passeando na tela.
	private ArrayList<Invader> invasores;
	private int MAX_INVASORES = Game.MAX_INVASORES;
	// O shooter e sua direção de movimento.
	private Shooter shooter;
	private ShooterMP shooter2;
	private Direcao dir;
	private ArrayList<Tiro> tiros;
	private ArrayList<TiroInvader> tirosInvader;
	private static final int MAX_TIROS = 20;
	@SuppressWarnings("unused")
	private static final int MAX_SUPER_TIROS = 1;
	// Construtor, inicializa estruturas, registra interfaces, etc.
	private int count;
	private boolean superTiro = false;
	private BufferedImage image;
	private boolean habilitarSTiro = false;
	public InvadersPanel() throws IOException {
		setBackground(Color.WHITE);
		if (Game.TEMA == 1) {
			URL resource = getClass().getResource("background1.jpg");
			image = ImageIO.read(resource);
		} else if (Game.TEMA == 2) {
			URL resource = getClass().getResource("background2.jpg");
			image = ImageIO.read(resource);
		} else if (Game.TEMA == 9) {
			URL resource = getClass().getResource("background5.jpg");
			image = ImageIO.read(resource);
		}
		setPreferredSize(new Dimension(largura, altura));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
		if (Game.runningOnline == false) {
			shooter = new Shooter(this.getPreferredSize());
		} else {
			inicio();
			String usuario;
			usuario = JOptionPane.showInputDialog(this, "Por favor, digite um nome de usuario");
			shooter2 = new ShooterMP(getPreferredSize(), usuario, null, -1);

		}
		invasores = new ArrayList<>(MAX_INVASORES);
		tiros = new ArrayList<>(MAX_TIROS);
		tirosInvader = new ArrayList<>(MAX_TIROS);
		for (int i = 0; i < MAX_INVASORES; i++) {
			invasores.add(new Invader(this.getPreferredSize()));
		}
		this.execute = true;
	}

	// Avisa que agora temos a interface em um container parente.
	public void inicio() {
		invadersPanel = this;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		try {
			startGame();
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Iniciamos o jogo (e a thread de controle)
	public synchronized void start() throws UnknownHostException, SocketException {

	}

	private void startGame() throws SocketException, UnknownHostException, InterruptedException {

		if (animator == null) {
			animator = new Thread(this);
			animator.start();

		}
		if (Game.runningOnline == true) {
			if (runningServer == true) {
				socketServer = new ServidorInvader(this);
				socketServer.start();
			}
			runningServer = false;
			socketClient = new ClienteInvader(this, "localhost");
			socketClient.start();
			if (Game.runningOnline == true) {
				Packet00Login loginPacket = new Packet00Login(shooter2.getUsuario(), shooter2.getX(), shooter2.getY());
				if (socketServer != null) {
					socketServer.addConnection((ShooterMP) shooter2, loginPacket);
				}
				loginPacket.writeData(socketClient);
				invadersPanel.adicionaShooter(shooter2);
			}
		}
	}
	// Laço principal do jogo.

	@Override
	public void run() {
		while (this.execute) {
			try {
				inicio();
				gameUpdate();
				shooters.size();
			} catch (IOException ex) {
				Logger.getLogger(InvadersPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void stopExecuting() throws IOException {
		this.execute = false;

		int escolha = JOptionPane.showConfirmDialog(null, "Deseja jogar novamente?", "Aviso!",
				JOptionPane.YES_NO_OPTION);
		if (escolha == JOptionPane.NO_OPTION) {
			System.exit(0);
		} else {
			Runtime.getRuntime().exec("java -jar Invaders.jar");
			System.exit(0);
		}
	}

	// Atualizamos os elementos do jogo.
	private void gameUpdate() throws IOException {
		// System.out.println(shooters.size());
		if (!isPaused) {
			for (Invader i : invasores) {
				i.move();
			}
			if (Game.runningOnline == false)
				shooter.move(dir);
			else
				shooter2.move(dir);
			for (Tiro b : tiros) {
				if (Game.runningOnline) {
					if (b.retornarUsuario().equals(shooter2.getUsuario()))
						b.moveCon();
					else
						b.move();
				} else
					b.moveCon();
			}
			for (TiroInvader c : tirosInvader) {
				c.move();
			}
			Iterator<Tiro> it = tiros.iterator();
			while (it.hasNext()) {
				if (!(it.next()).estaAtivo()) {
					it.remove();

				}
			}
			Iterator<TiroInvader> iy = tirosInvader.iterator();
			while (iy.hasNext()) {
				if (!(iy.next()).estaAtivo()) {
					iy.remove();
				}
			}
			Iterator<Invader> ii = invasores.iterator();
			while (ii.hasNext()) {
				if (!(ii.next()).estaAtivo()) {
					ii.remove();
				}
			}
			for (Tiro b : tiros) {
				for (Invader i : invasores) {
					if (b.acertouEm(i, superTiro)) {
						i.desativa();
					}
				}
			}
			if (Game.runningOnline) {
				for (Tiro b : tiros) {
					for (Shooter i : shooters) {
						if (b.acertouEmPlayer(i, superTiro) && (!b.retornarUsuario().equals(i.getUsuario()))) {
							i.desativa();
							b.setEstaAtivo(false);
							for(Shooter e: shooters){
								if(b.retornarUsuario().equals(e.getUsuario())){
									e.setPontos(100-count);
									System.out.println("Pontos de " +e.getUsuario()+" sao "+e.getPontos());
									System.out.println("eh null?"+shooter2.getUsuario());
								}
							}
						}
					}
				}
			}
			if (invasores.isEmpty() && Game.runningOnline == false) {
				stopExecuting();
			}
		}
		repaint();
	}
	// Desenhamos o componente (e seus elementos)
public void gravarArquivo(){
	
}
public void lerArquivo(){
	
}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 3, 4, this);
		g.setColor(Color.BLACK);
		if (Game.runningOnline == false) {
			g.drawRect(0, 0, getWidth(), getHeight() - 100);
		} else {
			g.drawRect(0, 0, getWidth(), getHeight() / 2);
		}
		for (Invader i : invasores) {
			i.draw(g);
		}
		if (Game.runningOnline == false)
			shooter.draw(g);
		else {
			for (ShooterMP s : shooters) {
				s.draw(g);
				repaint();
			}
		}
		for (Tiro b : tiros) {
			b.draw(g);
		}
		String i = "Invasores: " + invasores.size();
		String t = "Tiros: " + count;
		String s = "";
		if (invasores.size() <= 5) {
			s = "SUPER TIRO HABILITADO";
			habilitarSTiro = true;
		}
		g.setFont(new Font("Serif", Font.PLAIN, 18));
		g.setColor(Color.RED);
		g.drawString(i, 5, getHeight() - 10);
		g.drawString(t, 730, getHeight() - 10);
		g.drawString(s, 300, getHeight() - 10);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_P) {
			isPaused = !isPaused;
		}
		if (isPaused) {
			return;
		}
		if (keyCode == KeyEvent.VK_LEFT) {
			dir = Direcao.LEFT;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			dir = Direcao.RIGHT;
		} else if (keyCode == KeyEvent.VK_UP) {
			dir = Direcao.UP;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			dir = Direcao.DOWN;
		} else if (keyCode == KeyEvent.VK_SPACE) {
			if (tiros.size() < MAX_TIROS) {
				if (Game.runningOnline == false) {
					superTiro = false;
					tiros.add(new Tiro(null, shooter, superTiro));
					count = count + 1;
				} else {
					if (shooter2.isEstaAtivo()) {
						superTiro = false;
						Tiro t = new Tiro(null, shooter2, superTiro);
						//tiros.add(t);
						count = count + 1;
						System.out.println(shooter2.getUsuario());
						Packet03Atirar packet = new Packet03Atirar(shooter2.getUsuario(), null, false);
						InvadersPanel.invadersPanel.socketClient.addTiroTela(t, packet);
						packet.writeData(InvadersPanel.invadersPanel.socketClient);
					}
				}
			}
		} else if (keyCode == KeyEvent.VK_CONTROL) {
			if (habilitarSTiro) {
				superTiro = true;
				tiros.add(new Tiro(null, shooter, superTiro));
				count = count + 1;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
