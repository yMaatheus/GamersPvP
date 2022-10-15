package dev.gamerspvp.fullpvp.sword;

import dev.gamerspvp.fullpvp.Main;

public class SwordManager {
	
	//private Main instance;
	
	/*
	 * comando de givar espada
	 * sistema de bloquear utiliza§§o da espada
	 * bloco de notas de nomes bloqueados
	 */
	
	public SwordManager(Main instance) {
		//this.instance = instance;
		new SwordListener(instance);
	}
}