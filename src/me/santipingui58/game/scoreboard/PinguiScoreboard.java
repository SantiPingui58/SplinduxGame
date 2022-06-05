package me.santipingui58.game.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.utils.Utils;



public class PinguiScoreboard {
	
	private static PinguiScoreboard scoreboard;	
	
	 public static PinguiScoreboard getScoreboard() {
	        if (scoreboard == null)
	        	scoreboard = new PinguiScoreboard();
	        return scoreboard;
	    }
	
	public void scoreboard(GamePlayer p) {
				String[] data = null;
				List<String> cache = new ArrayList<String>();
				String displayname = "§e§lSplin§b§lDux";
					Arena arena = p.getArena();
					cache.add(displayname);
					cache.add("§f");
					
					//if (arena.getState().equals(GameState.GAME) || arena.getState().equals(GameState.STARTING)) {
						
						if (arena.getPoints1()>=arena.getPoints2()) {
							if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) {
						cache.add("§2"+ Bukkit.getPlayer(arena.getDuelPlayers1().get(0)).getName() + ": §e" + arena.getPoints1());
						cache.add("§2"+Bukkit.getPlayer(arena.getDuelPlayers2().get(0)).getName()+ ": §e" + arena.getPoints2());
							} else {
								cache.add("§9Blue Team: §e" + arena.getPoints1());
								cache.add("§cRed Team: §e" + arena.getPoints2());
							}
						} else {				
							if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) {
								cache.add("§2"+ Bukkit.getPlayer(arena.getDuelPlayers2().get(0)).getName() + ": §e" + arena.getPoints2());
								cache.add("§2"+ Bukkit.getPlayer(arena.getDuelPlayers1().get(0)).getName() + ": §e" + arena.getPoints1());
									} else {
										cache.add("§cRed Team: §e" + arena.getPoints2());
										cache.add("§9Blue Team: §e" + arena.getPoints1());
									}
						}
						cache.add("§f§f");
						cache.add("§2Time: §e" + Utils.getUtils().time(arena.getTotalTime()));		
					cache.add("§f§f§f§f§f");
					if (arena.getState().equals(GameState.PAUSE)) {
					cache.add("§c§lGAME PAUSED");
					cache.add("§f§f§f§f§f§f");
					}
				/*} else {
					cache.add("§2ELO: §e" +sp.getPlayerStats().getELO(arena.getSpleefType()));
					cache.add("§21vs1 Wins: §e" +sp.getPlayerStats().getDuelWins(arena.getSpleefType()));
					cache.add("§21vs1 Games: §e" +sp.getPlayerStats().getDuelGames(arena.getSpleefType()));
					cache.add("§f§f");
					cache.add("§cGame not started");
					cache.add("§f§f§f");
					cache.add("   §7mc.splindux.com");
				}
				*/
				
				
				for(int i = 0; i < cache.size(); i++) {
					data = cache.toArray(new String[i]);
				}
				
				final String[] data2 = data;
				new BukkitRunnable() {
					public void run() {
						BoardAPI.ScoreboardUtil.unrankedSidebarDisplay(p.getPlayer(), data2);	
				}
				}.runTask(Main.get());
				
 					
	}
	
	
	


	
	
}
	

