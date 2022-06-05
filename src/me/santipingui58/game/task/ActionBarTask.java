package me.santipingui58.game.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.Main;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.utils.ActionBarAPI;


public class ActionBarTask {
	

	public ActionBarTask() {
		new BukkitRunnable() {
			public void run() {
				for (Arena arena : GameManager.getManager().getArenas()) {
					if (!arena.getGameType().equals(GameType.DUEL)) continue;
					if (arena.getPlayTo()==7) continue;
					if (arena.getViewers()==null) continue;
					for (UUID sp : arena.getViewers()) {
						if (Bukkit.getOfflinePlayer(sp).isOnline()) {
							new BukkitRunnable() {
								public void run() {
						ActionBarAPI.sendActionBar(Bukkit.getOfflinePlayer(sp), "§6§lPlaying to: §a§l"+arena.getPlayTo());
							}
							}.runTask(Main.get());
						}
					}
				}
				
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 40L);
	}
}
