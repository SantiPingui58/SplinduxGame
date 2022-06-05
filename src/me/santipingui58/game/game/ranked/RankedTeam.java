package me.santipingui58.game.game.ranked;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.data.SplinduxDataAPI;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.Main;


public class RankedTeam {

	
	private Set<UUID> players = new HashSet<UUID>();
	
	public RankedTeam(Set<UUID> players) {
		this.players = players;
	}
	
	public Set<UUID> getPlayers() {
		return this.players;
	}
	
	public List<UUID> getOnlinePlayers() {
		List<UUID> list = new ArrayList<UUID>();
		for (UUID u : this.players) {
			Player p = Bukkit.getPlayer(u);
			if (Bukkit.getOnlinePlayers().contains(p)) list.add(p.getUniqueId());
		}
		return list;
	}
	
	
	public int getELO(SpleefType type) {
		int elo = 0;
		for (UUID sp : getOnlinePlayers()) elo = elo + DataPlayer.getPlayer().getELO(sp, type);
		elo = this.players.size() >0 ? elo/this.players.size() : elo;
		return elo;
	}

	public void newELO(int elo,SpleefType type) {
		for (UUID uuid : this.players) {
			
			if (!SplinduxDataAPI.getAPI().isLoaded(uuid)) {
				Main.get().getLogger().info("NOT LOADED");
				SplinduxDataAPI.getAPI().loadData(uuid);
			} 
				DataPlayer.getPlayer().setELO(uuid, type, DataPlayer.getPlayer().getELO(uuid, type)+elo);
				Main.get().getLogger().info("newELO:" + DataPlayer.getPlayer().getELO(uuid, type));
			}
	}
}
