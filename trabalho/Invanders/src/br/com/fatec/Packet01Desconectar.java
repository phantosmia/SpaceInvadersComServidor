package br.com.fatec;

import online.ClienteInvader;
import online.ServidorInvader;

public class Packet01Desconectar extends Packet{
	private String usuario;
    public Packet01Desconectar(byte[] data){
    	super(01);
    	this.usuario = readData(data);
    }
    public Packet01Desconectar(String usuario){
    	super(01);
    	this.usuario = usuario;
    }
	

	@Override
	public void writeData(ServidorInvader servidor) {
		// TODO Auto-generated method stub
		servidor.enviarDataParaTodosOsClientes(getData());
	}
	

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return ("01"+ this.usuario).getBytes();
	}
	public String getUsuario(){
		return usuario;
	}
	@Override
	public void writeData(ClienteInvader cliente) {
		// TODO Auto-generated method stub
		cliente.enviarData(getData());
	}
}
