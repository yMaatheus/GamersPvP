package dev.gamerspvp.gladiador.eventochat;

public class EventoChat {
	
	private int resultado;
	private boolean acontecendo;
	
	public EventoChat() {
		this.resultado = 0;
		this.acontecendo = false;
	}
	
	public void reset() {
		this.resultado = 0;
		this.acontecendo = false;
	}
	
	public int getResultado() {
		return resultado;
	}
	
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
	
	public boolean isAcontecendo() {
		return acontecendo;
	}
	
	public void setAcontecendo(boolean acontecendo) {
		this.acontecendo = acontecendo;
	}
}