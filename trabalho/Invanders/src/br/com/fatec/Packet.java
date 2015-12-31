package br.com.fatec;
import online.ClienteInvader;
import online.ServidorInvader;
public abstract class Packet {
	public static enum PacketTypes{
		INVALIDO(-1),
		LOGIN(00),
		DESCONECTAR(01),
		MOVER(02),
		ATIRAR(03);
		private int packetId;
		private PacketTypes(int packetId){
			this.packetId = packetId;
		}
		public int getId(){
			return packetId;
		}
	}
	public byte packetId;
	public Packet(int packetId){
		this.packetId = (byte) packetId;
	}
	public abstract void writeData(ClienteInvader cliente);
	public abstract void writeData(ServidorInvader servidor);
	public String readData(byte[] data){
		String mensagem = new String(data).trim();
		return mensagem.substring(2);
	}
	public abstract byte[] getData();
	public static PacketTypes lookupPacket(String packetId){
		try{
			return lookupPacket(Integer.parseInt(packetId));
		}
		catch(NumberFormatException e){
			return PacketTypes.INVALIDO;
		}
	}
	public static PacketTypes lookupPacket(int id){
		for(PacketTypes p : PacketTypes.values()){
			if(p.getId() == id){
				return p;
			}
		}
		return PacketTypes.INVALIDO;
	}
}
