package me.santipingui58.game.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.santipingui58.data.DataManager;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.SpectateManager;
import me.santipingui58.game.utils.Utils;

public class PlayerConnectionListener implements Listener {

	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);
		//for (UUID u : t2) DataManager.getManager().set(u.toString()+".spawn."+Main.server, Utils.getUtils().setLoc(arena.getSpawn2(),true));
		
		
		
		if (DataManager.getManager().getSet("spectator-players").contains(p.getUniqueId().toString())) {
			SpectateManager.getManager().preSpectate(p);
		} else {
		if (DataManager.getManager().get(p.getUniqueId().toString()+".spawn."+Main.server)!=null) {
			p.teleport(Utils.getUtils().getLoc(DataManager.getManager().get(p.getUniqueId().toString()+".spawn."+Main.server), true));
		} else {
			e.getPlayer().teleport(new Location(Bukkit.getWorld("world"),0,10,0));	
		}
		}
		Set<Player> hidedPlayers = new HashSet<Player>();
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,2,2));
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (p.canSee(pl)) {
				p.hidePlayer(Main.get(), pl);
				hidedPlayers.add(pl);
			}
		}
		new BukkitRunnable() {
			public void run() {
				for (Player pl : hidedPlayers) p.showPlayer(Main.get(),pl);
			}
		}.runTaskLater(Main.get(), 10L);
		
	}
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		GamePlayer gp = PlayerManager.getManager().getPlayer(e.getPlayer());
		gp.leave();
		SpectateManager.getManager().leaveSpectate(e.getPlayer(), false);
		gp.setArena(null);
		DataManager.getManager().removeToSet("ingame-players", gp.getUUID().toString());
		DataManager.getManager().removeToSet("spectator-players", gp.getUUID().toString());
		PlayerManager.getManager().getPlayers().remove(gp);
		PlayerManager.getManager().getPlayersCache().remove(e.getPlayer().getUniqueId());
	}
}
