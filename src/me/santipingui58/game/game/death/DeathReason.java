package me.santipingui58.game.game.death;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.Main;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.ffa.FFA;

public enum DeathReason {
THEMSELF,SPLEEFED,SNOWBALLED, PK_FAILED, PLAYING_FAILED;
	
	
	public String getDeathMessage(UUID kr, UUID kd,Arena arena) {
		FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
		String left = Main.ffa2v2 ? ffa.getTeamsAlive().size() +" §bcouples" : ffa.getPlayers().size() + " §bplayers";
		
		OfflinePlayer killer = Bukkit.getOfflinePlayer(kr);
		OfflinePlayer killed = Bukkit.getOfflinePlayer(kd);
		if (this.equals(THEMSELF)) {
			return "§6"+ killed.getName() + " §bspleefed themself! §a" + left+ " left!";
		} else if (this.equals(SPLEEFED)) {
			return "§6"+ killed.getName() + "§b was spleefed by §6"+ killer.getName() + "§b! §a" +  left+ " left!";
		} else if (this.equals(SNOWBALLED)) {
			String snowballed = arena.getSpleefType().equals(SpleefType.SPLEEF) ? "snowballed" : "splegged";
			return "§6"+ killed.getName() + "§b was "+snowballed+" by §6"+ killer.getName() + "§b! §a" + left+ " left!";
		} else if (this.equals(PK_FAILED)) {
			return "§6"+ killed.getName() + " §bfailed! §a" +  left+ "left!";
		} else if (this.equals(PLAYING_FAILED)) {
			return "§6"+ killed.getName() + " §bhas fell while playing against §6"+ killer.getName() + "§b! §a"+ left+ " left!";
		}	
	
		return null;
} 
}
