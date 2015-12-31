package br.com.fatec;


import java.awt.Dimension;


import online.ClienteInvader;

import online.ServidorInvader;


public class Packet03Atirar extends Packet{
	private Dimension dimension;
	private String usuario;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	private Shooter shooter;
	private int velocidade;
	public int getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}
	public void setShooter(Shooter shooter) {
		this.shooter = shooter;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void setArena(Game game) {
		this.game = game;
	}

	private int x, y;
	private boolean superTiro;
	private Game game = new Game();
	public Packet03Atirar(byte[]data){
		super(03);
		
		String[] dataArray = readData(data).split(",");
		
		System.out.println(dataArray[0]);
		//this.shooter(dataArray[0]);
		//this.shooter = gson.fromJson(dataArray[0], Shooter.class);
		this.usuario = dataArray[0];
		this.dimension = null;
		superTiro = false;
		//this.y = Integer.parseInt(dataArray[2]);
	
	}
	public Packet03Atirar(String usuario, Dimension a, boolean superTiro) {
		super(03);
		
		this.usuario = usuario;
		//this.shooter = gson.fromJson(string,Shooter.class);
		this.dimension = null;
		this.superTiro = superTiro;
		
	}

	public Shooter getShooter() {
		return shooter;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void writeData(ClienteInvader cliente) {
		// TODO Auto-generated method stub
		
			cliente.enviarData(getData());
		
		
	}

	@Override
	public void writeData(ServidorInvader servidor) {
		// TODO Auto-generated method stub
		servidor.enviarDataParaTodosOsClientes(getData());
	}

	@Override
	public byte[] getData() {
	
	//	System.out.println(gson.toJson(this.shooter));
		System.out.println("problem?");
		return ("03"  + this.usuario).getBytes();
	}

}
