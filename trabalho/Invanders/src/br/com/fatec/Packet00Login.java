package br.com.fatec;



import online.ClienteInvader;
import online.ServidorInvader;

public class Packet00Login extends Packet{
	public Packet00Login(byte[] data){
		super(00);
		String[] dataArray = readData(data).split(",");
		this.usuario = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
	}
	public Packet00Login(String usuario, int x, int y){
		super(00);
		this.usuario = usuario;
		this.x = x;
		this.y = y;
		
	} //enviando dados para o servidor
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private String usuario;
	private int x;
	private int y;
	

	@Override
	public void writeData(ServidorInvader servidor) {
		// TODO Auto-generated method stub
		servidor.enviarDataParaTodosOsClientes(getData());
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return ("00"+ this.usuario + "," + getX() + "," + getY()).getBytes();
	}
	@Override
	public void writeData(ClienteInvader cliente) {
		// TODO Auto-generated method stub
		cliente.enviarData(getData());
	}

}
