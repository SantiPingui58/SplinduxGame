package me.santipingui58.game.task;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.scoreboard.PinguiScoreboard;


public class ScoreboardTask {
	
	
	PlayerManager pm = PlayerManager.getManager();
	
	public ScoreboardTask() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {					
					if (pm.getPlayer(p).isInGame() || pm.getPlayer(p).isSpectating()) {
						PinguiScoreboard.getScoreboard().scoreboard(pm.getPlayer(p));
					}
				} 
			}
		}.runTaskTimer(Main.get(), 0L, 10L);
	}
}
