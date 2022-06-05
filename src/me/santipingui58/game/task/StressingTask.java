package me.santipingui58.game.task;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.santipingui58.game.Main;


public class StressingTask {
	

	public StressingTask() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers())  {
					int random = ThreadLocalRandom.current().nextInt(1, 100 + 1);
					if (random<=25) {
						new BukkitRunnable() {
							public void run() {
						p.teleport(new Location(p.getWorld(),p.getLocation().getX(), p.getLocation().getY()-2,p.getLocation().getZ()));
							}
						}.runTask(Main.get());
					}
				}
				
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L*5);
	}
}
