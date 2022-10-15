package net.gamers.center.backup;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import net.gamers.center.Main;
import net.gamers.center.database.Redis;
import net.gamers.center.games.status.GamesStatusManager;

public class BackupManager {
	
	private TimeZone timeZone;
	private int day, hour, minute;
	
	public BackupManager() throws Exception {
		this.timeZone = TimeZone.getTimeZone("America/Sao_Paulo");
		String horario = Main.getInstance().getConfig().getString("backup.horario");
		this.day = Integer.parseInt(horario.split(":")[0]);
		this.hour = Integer.parseInt(horario.split(":")[1]);
		this.minute = Integer.parseInt(horario.split(":")[2]);
		Main.getInstance().getTaskUpdater().add(() -> checkBackup());
	}
	
	public void checkBackup() {
		Calendar calendar = GregorianCalendar.getInstance(timeZone);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (this.day == day && this.hour == hour && this.minute == minute) {
			
			/*
			 * Start restart all
			 * 
			 * Definir o modo manuten��o para todos os servidores
			 * Enviar mensagem informando aos servidores para reiniciar(Servidor enviar� todos os jogadores ao lobby e depois reiniciar�)
			 * Ap�s reiniciar os servidores enviar�o mensagem informando para executar o backup (Ap�s terminar 1 backup de servidor iniciar o reinicio dos proxy's e logo apos reiniciarem iniciar o backup)
			 * O backup ser� feito e logo ap�s ser� retirado o servidor da manuten��o.
			 */
			
			GamesStatusManager.executeMaintenanceAll();
			Redis redis = Main.getInstance().getDataCenter().getRedis();
			redis.publish("backup_restartbukkit;restart");
			
		}
	}
}