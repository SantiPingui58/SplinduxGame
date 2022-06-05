package me.santipingui58.game.task;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class ArenaStartingCountdownTask {
 
	
	private Arena arena;
	private int time;
	private int task;
	private List<ArmorStand> armorstands;
	public ArenaStartingCountdownTask(Arena arena,List<ArmorStand> armorstands) {
		this.arena = arena;
		this.time = 3;
		this.armorstands = armorstands;
		arena.setState(GameState.STARTING);
		task();

	}
	
	private void task() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			public void run() {
				if (time==3) {
					if (arena.getDecayTask()!=null) arena.getDecayTask().orderLocations();
				}
				
				
				String title = "";
				float d = 0.85F;
				switch(time) {
				case 3: title = ChatColor.AQUA+"READY!"; break;
				case 2: title = ChatColor.AQUA+"SET!"; break;
				case 1: title = ChatColor.AQUA+"GO!!"; d=0.95F;break;
				}	
				
				for (UUID sp : arena.getViewers()) {
					Player p = Bukkit.getPlayer(sp);
					p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, d);
					p.sendMessage(title);
	    			Utils.getUtils().sendTitles(sp,"", title, 5, 20, 5);
				}

		    	time = time-1;
		    	if (time<=0) {	    		
		    		arena.setState(GameState.GAME);
		    		PlayerManager pp = PlayerManager.getManager();
		    		for (UUID sp : arena.getPlayers()) {
		    			if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
		    				pp.getPlayer(sp).giveShovel();		 
		    				Player p = Bukkit.getPlayer(sp);
		    			p.setAllowFlight(false);
		    			p.setFlying(false);
		    			}
		    		}
		    		
		    		if (armorstands!=null) {
		    			for (ArmorStand stand : armorstands) {
		    				stand.remove();
		    			}
		    		}
		    		
		    		Bukkit.getScheduler().cancelTask(task);
		    	}
		    
		    }
		    }, 2L, arena.getArenaStartingCountdownDelay());
	}
}
