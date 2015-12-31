package br.com.fatec;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import online.ClienteInvader;

public class Packet02Move extends Packet {
	private String usuario;
	private int x, y;
	private Game game = new Game();
	public Packet02Move(byte[]data){
		super(02);
		String[] dataArray = readData(data).split(",");
		this.usuario = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
	}
	public Packet02Move(String usuario2, int x2, int y2) {
		super(02);
		this.usuario = usuario2;
		this.x = x2;
		this.y = y2;
	}

	public String getUsuario() {
		return usuario;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	@Override
	public void writeData(ClienteInvader cliente) {
		// TODO Auto-generated method stub
	
			cliente.enviarData(getData());
		
		
	}
	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return ("02" + this.usuario + "," + this.x +"," + this.y).getBytes();
	}
	@Override
	public void writeData(online.ServidorInvader servidor) {
		// TODO Auto-generated method stub
		servidor.enviarDataParaTodosOsClientes(getData());
	}

}
