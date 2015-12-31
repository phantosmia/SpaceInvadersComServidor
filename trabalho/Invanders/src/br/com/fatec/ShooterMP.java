package br.com.fatec;
import java.awt.Dimension;
import java.net.InetAddress;
public class ShooterMP extends Shooter {
	public InetAddress enderecoIp;
	public int porta;
	public ShooterMP(Dimension a, String usuario, InetAddress enderecoIP, int porta){
		super(a,usuario);
		this.porta = porta;
		this.enderecoIp = enderecoIP;
	}
	
}
