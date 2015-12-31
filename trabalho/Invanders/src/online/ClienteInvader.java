package online;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import br.com.fatec.Tiro;
import br.com.fatec.InvadersPanel;
import br.com.fatec.Packet;
import br.com.fatec.Packet.PacketTypes;
import br.com.fatec.Packet00Login;
import br.com.fatec.Packet01Desconectar;
import br.com.fatec.Packet02Move;
import br.com.fatec.Packet03Atirar;
import br.com.fatec.ShooterMP;
public class ClienteInvader extends Thread {
	private InetAddress enderecoIP;
	private DatagramSocket socket;
	private InvadersPanel invadersPanel;
	private List<Tiro> tirosNaTela = new ArrayList<Tiro>();
	public ClienteInvader(InvadersPanel invadersPanel, String enderecoIP) throws SocketException, UnknownHostException {
		this.invadersPanel = invadersPanel;
		this.socket = new DatagramSocket();
		this.enderecoIP = InetAddress.getByName(enderecoIP);
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length); 
			try {
				socket.receive(packet); // recebendo os dados
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void parsePacket(byte[] data, InetAddress endereco, int porta) throws LineUnavailableException,  IOException{
		String mensagem = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(mensagem.substring(0, 2));
		Packet packet = null;
		switch(type){
		default:
		case INVALIDO:
			break;
		case ATIRAR:
			packet = new Packet03Atirar(data);
			handleTiro((Packet03Atirar)packet);
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login) packet, endereco, porta);
			break;
		case DESCONECTAR: 
			packet = new Packet01Desconectar(data);
			System.out.println("[" + endereco.getHostAddress() + ":" + porta + "]" + ((Packet01Desconectar)packet).getUsuario()+ " saiu do jogo.");
			invadersPanel.removerShooterMP(((Packet01Desconectar)packet).getUsuario());
			break;
		case MOVER:
			packet = new Packet02Move(data);
			handleMove(((Packet02Move) packet));
			break;
		} 
	}
	public void enviarData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data,data.length, enderecoIP, 1331);
		try{
			socket.send(packet);
		}
		catch(IOException e){
			e.printStackTrace();
		}
}
	public void addTiroTela(Tiro tiro, Packet03Atirar packet){
		this.tirosNaTela.add(tiro);
	}
	private void handleTiro(Packet03Atirar packet){
		//Tiro tiro = new Tiro(null,packet.getShooter(),false);
		invadersPanel.criarTiroShooter(packet.getUsuario());
		//arena.criarTiroTanque(packet.getUsuario(), packet.getX(), packet.getY(), packet.getAngulo(), tiro);
	}
	private void handleLogin(Packet00Login packet, InetAddress endereco, int porta){
		System.out.println("[" + endereco.getHostAddress() + ":" + porta + "] " + packet.getUsuario() + " acabou de logar...");
		ShooterMP shooter = new ShooterMP(invadersPanel.getPreferredSize(),packet.getUsuario(),endereco,porta);
		invadersPanel.adicionaShooter(shooter);
	
	}
	private void handleMove(Packet02Move packet){
		
		this.invadersPanel.moverShooter(packet.getUsuario(), packet.getX(), packet.getY());
		
	}
}
