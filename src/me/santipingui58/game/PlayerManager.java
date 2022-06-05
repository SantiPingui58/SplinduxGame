package me.santipingui58.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager {
	private static PlayerManager manager;	
	
 public static PlayerManager getManager() {
        if (manager == null)
        	manager = new PlayerManager();
        return manager;
    }
 
 private Set<GamePlayer> players = new HashSet<GamePlayer>();
 private HashMap<UUID,GamePlayer> playersCache = new HashMap<UUID,GamePlayer>();
 
 public Set<GamePlayer> getPlayers() {
	 return this.players;
 }
 
	public void sendSyncMessage(Collection<UUID> s, String string) {
		new BukkitRunnable() {
			public void run() {
		for (UUID ss  : s) {
			if (Bukkit.getOfflinePlayer(ss).isOnline()) Bukkit.getPlayer(ss).sendMessage(string);
			}
		}
		}.runTask(Main.get());
		
	}

	public GamePlayer getPlayer(UUID p) {
		if (this.playersCache.containsKey(p)) {
			return this.playersCache.get(p);
		} else {
			for (GamePlayer gp : this.players) {
				if (gp.getUUID().compareTo(p)==0) {
					this.playersCache.put(p, gp);
					return gp;
				}
			}
			GamePlayer gp = new GamePlayer(p);
			this.playersCache.put(p, gp);
			this.players.add(gp);
			return gp;
		}
	}

	public GamePlayer getPlayer(Player p) {
		return getPlayer(p.getUniqueId());
	}

	public HashMap<UUID, GamePlayer> getPlayersCache() {
		return this.playersCache;
	}

}
