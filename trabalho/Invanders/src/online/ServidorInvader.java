package online;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import br.com.fatec.Game;
import br.com.fatec.InvadersPanel;
import br.com.fatec.Packet;
import br.com.fatec.Packet.PacketTypes;
import br.com.fatec.Packet00Login;
import br.com.fatec.Packet01Desconectar;
import br.com.fatec.Packet02Move;
import br.com.fatec.Packet03Atirar;
import br.com.fatec.ShooterMP;
import br.com.fatec.Tiro;
public class ServidorInvader extends Thread{
private DatagramSocket socket;
private InvadersPanel invadersPanel;

private List<Tiro> tirosNaTela = new ArrayList<Tiro>();
private List<ShooterMP> usuariosConectados = Collections.synchronizedList(new ArrayList<ShooterMP>());
public ServidorInvader(InvadersPanel i) throws SocketException{
	this.invadersPanel = i;
	//this.game = game;
	this.socket = new DatagramSocket(1331);
}
public void run(){
	while(true){
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data,data.length);
		try {
			socket.receive(packet);
			this.parsePacket(packet.getData(),packet.getAddress(), packet.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
private void parsePacket(byte[] data, InetAddress endereco, int porta){
	String mensagem = new String(data).trim();
	PacketTypes type = Packet.lookupPacket(mensagem.substring(0, 2));
	Packet packet = null;
	switch(type){
	default:
	case INVALIDO:
	break;
	case LOGIN:
		packet = new Packet00Login(data);
		System.out.println("[" + endereco.getHostAddress() + ":" + porta + "] " + ((Packet00Login) packet).getUsuario() + " acabou de se conectar...");
		ShooterMP shooter= new ShooterMP(invadersPanel.getPreferredSize(),((Packet00Login)packet).getUsuario(), endereco, porta);
		;
		this.addConnection(shooter, ((Packet00Login)packet));
		break;
	case MOVER:
		packet = new Packet02Move(data);
		this.handleMove(((Packet02Move)packet));
		break;
	case ATIRAR:
		packet = new Packet03Atirar(data);
		this.handleTiro((Packet03Atirar)packet);
		break;
	case DESCONECTAR:
		packet = new Packet01Desconectar(data);
		this.removerConexao((Packet01Desconectar)packet);
		break;
	}
}
public void addTiroTela(Tiro tiro, Packet03Atirar packet){
	this.tirosNaTela.add(tiro);
	packet.writeData(this);
}
public void enviarData(byte[] data, InetAddress enderecoIp, int porta){
	DatagramPacket packet = new DatagramPacket(data, data.length, enderecoIp, porta);
	try{
		this.socket.send(packet);
	}
	catch(IOException e){
		e.printStackTrace();
	}

}
public void enviarDataParaTodosOsClientes(byte[] data){
for(ShooterMP t: usuariosConectados){
	enviarData(data,t.enderecoIp, t.porta);
} //enviar todas as modificacoes para todos os clientes
}
public ShooterMP getShooterMP(String usuario){
	for(ShooterMP shooter : this.usuariosConectados){
		if(shooter.getUsuario().equals(usuario)){
			return shooter;
		}
	}
	return null;
}
public int getShooterMPIndex(String usuario){
	int index = 0;
	for(ShooterMP shooter: this.usuariosConectados){
		if(shooter.getUsuario().equals(usuario)){
			break;
		}
		index++;
	}
	return index;
}
public void addConnection(ShooterMP shooter, Packet00Login packet){
	boolean jaConectou = false;
	for(ShooterMP s: this.usuariosConectados){
		if(shooter.getUsuario().equalsIgnoreCase(s.getUsuario())){
			if(s.enderecoIp == null){
				s.enderecoIp = shooter.enderecoIp;
			}
			if(s.porta == -1){
				s.porta = shooter.porta;
			}
			jaConectou = true;
		}
		else{
			enviarData(packet.getData(), s.enderecoIp, s.porta);
			Packet00Login packetNew = new Packet00Login(s.getUsuario(),s.getX(),s.getX());			
			enviarData(packetNew.getData(), shooter.enderecoIp, shooter.porta);
			// envia data do servidor para o cliente
		}
	}
	if(!jaConectou){
		this.usuariosConectados.add(shooter);
		
	}
}
private void handleTiro(Packet03Atirar packet){
	if(getShooterMP(packet.getUsuario())!= null){
		int index = getShooterMPIndex(packet.getUsuario());
		ShooterMP shooter = this.usuariosConectados.get(index);
		if(shooter.isEstaAtivo()){
		Tiro tiro = new Tiro(null, shooter, false);
		this.addTiroTela(tiro, packet);
		}
	}
}
public void removerConexao(Packet01Desconectar packet){
	this.usuariosConectados.remove(getShooterMPIndex(packet.getUsuario()));
	packet.writeData(this);
}
private void handleMove(Packet02Move packet){
	if(getShooterMP(packet.getUsuario()) != null){
		int index = getShooterMPIndex(packet.getUsuario());
		ShooterMP shooter = this.usuariosConectados.get(index);
		String usuarioShooter = shooter.getUsuario();
		for (ShooterMP shooter2 : this.usuariosConectados) {
			shooter.setX(packet.getX());
			if (shooter2.getUsuario().equals(usuarioShooter)) {
				shooter.setY(packet.getY());
			} else {
				shooter.setY(invadersPanel.getHeight() - packet.getY());
			}

		}
		shooter.setUsuario(packet.getUsuario());
		//shooter.setX(packet.getX());
		//shooter.setY(packet.getY());
		packet.writeData(this);
	}
}
}
