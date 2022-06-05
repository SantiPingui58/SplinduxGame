package me.santipingui58.game.level;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.data.spleef.SpleefRank;
import me.santipingui58.game.Main;

public class LevelManager {

	
	private static LevelManager manager;	
	 public static LevelManager getManager() {
	        if (manager == null)
	        	manager = new LevelManager();
	        return manager;
	    }
	
	 DataPlayer dp = DataPlayer.getPlayer();
	 //Returns the rank of a Player
	 public SpleefRank getRank(UUID sp) {
		 SpleefRank rank = null;
		 for (SpleefRank sr : SpleefRank.values()) {
			 if (rank==null) {
				 rank = sr;
			 } else {
				 if (sr.getRequiredLevel()>rank.getRequiredLevel() && dp.getLevel(sp)>=sr.getRequiredLevel()) {
					 rank = sr;
				 }
			 }
		 }	 
		 return rank;	 
	 }
	 
	 
	 public SpleefRank getRank(int level) {
		 SpleefRank rank = null;
		 for (SpleefRank sr : SpleefRank.values()) {
			 if (rank==null) {
				 rank = sr;
			 } else {
				 if (sr.getRequiredLevel()>rank.getRequiredLevel() && level>=sr.getRequiredLevel()) {
					 rank = sr;
				 }
			 }
		 }	 
		 return rank;	 
	 }
	 
	 //Check if the player has more EXP than requiered to level up
	 public void checkLevel(UUID sp, SpleefRank rank ) {
		 if (rank.getNextRank()!=null) {
		 if (dp.getLevel(sp)>=rank.getNextRank().getRequiredLevel()) {
			 levelUp(sp);
		 }
		 }
	 }
	 
	 
	 public void addLevel(UUID sp,int level) {
		 int oldlevel = dp.getLevel(sp);	 
		 SpleefRank oldrank = getRank(sp);
		dp.setLevel(sp,oldlevel+level);		
		 checkLevel(sp,oldrank);
		 if (Bukkit.getOfflinePlayer(sp).isOnline()) {
		 setExp(sp);
		 }
		 
	 }
	 
	 
	 public void setLevel(UUID sp,int level) {
		 dp.setLevel(sp, level);
		 if (Bukkit.getOfflinePlayer(sp).isOnline()) {
		 setExp(sp);
		 }
		 
	 }
	 
	 public void levelUp(UUID sp) {
		 OfflinePlayer p = Bukkit.getOfflinePlayer(sp);
		 if (getRank(sp).getMainRank().equals(getRank(sp))) {
			 for (Player pa : Bukkit.getOnlinePlayers()) {
						 pa.sendMessage("§e§lSplin§b§ldux  §aCongratulations to §b"+ p.getName() +"§a for level up to " +getRank(sp).getRankName()+ "§a!");
					 
		 } 
		 }
			 
			 new BukkitRunnable() {
					@Override
					public void run() {
						if (p.isOnline()) {
			 	p.getPlayer().sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			    p.getPlayer().sendMessage("");
			    p.getPlayer().sendMessage("    §aCongratulations! You have leved up to " + getRank(sp).getRankName() +"§a!");
			    p.getPlayer().sendMessage("");
				p.getPlayer().sendMessage("§d§m-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					}
					}
				}.runTaskLater(Main.get(), 5L);	
	 
	 }
	 
	 
	 public void setExp(UUID sp) {
		 Player p = Bukkit.getPlayer(sp);
		 SpleefRank srank = getRank(sp);
		  p.setExp(0);
		  p.setLevel(0);
		  int current_level = DataPlayer.getPlayer().getLevel(sp);
		  
		  
		  int prev_level = 0; 

		  if (srank.getNextRank()==null) {
			  p.setLevel(50);
			  return;
		  }
		  if (srank.getPrevRank()==null) {
			  prev_level = 0; 
		  } else {
			  prev_level = srank.getRequiredLevel(); 
		  }
		  
		  
		  int next_level = srank.getNextRank().getRequiredLevel(); 
	
		  int piso = current_level - prev_level;
		  int max = next_level - prev_level;
		  
		  
		  double resultado = (double) piso/ (double) max;
		  
		  if (resultado >= 1 || resultado <= 0) {
			  resultado = 0;
		  }
		  
		  p.setExp((float) resultado);
		  p.setLevel(srank.getInt());
		  
		  
	  }
	 
	 //Returns a percentage of progress to the next level.
	 public String getPercentage(UUID sp) {
		SpleefRank rank = LevelManager.getManager().getRank(sp);
		 int current = DataPlayer.getPlayer().getLevel(sp)-rank.getRequiredLevel();
		 int max = rank.getNextRank().getRequiredLevel() - rank.getRequiredLevel();		
		 double p = (double)current/ (double) max;
		 int p2 = (int) (p*100);
		 return String.valueOf(p2)+"%";
		 
	 }
	 
	 
	 
}
