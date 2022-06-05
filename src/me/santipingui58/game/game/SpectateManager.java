package me.santipingui58.game.game;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.DataManager;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.utils.ActionBarAPI;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;

public class SpectateManager {

	private static SpectateManager manager;	
	 public static SpectateManager getManager() {
	        if (manager == null)
	        	manager = new SpectateManager();
	        return manager;
	    }
	 
	 
	 public void preSpectate(Player p) {
		 GamePlayer gp_spectating = PlayerManager.getManager().getPlayer(UUID.fromString(DataManager.getManager().get(p.getUniqueId().toString()+"-spectating")));
			spectateSpleef(p, gp_spectating.getArena());
			GamePlayer gp = PlayerManager.getManager().getPlayer(p);
			gp.setSpectating(true);
			gp.setArena(gp_spectating.getArena());
	 }
	 
	 PlayerManager pp = PlayerManager.getManager();
	 private void spectate(Player p,Arena arena,Set<UUID> playing, Set<UUID> spectators,boolean giveSpectate) {
		 if (spectators==null) return;
			new BukkitRunnable() {
				public void run() {
		if (giveSpectate) pp.getPlayer(p).giveSpectateItems();
		
			for (UUID play : playing) {
				Bukkit.getPlayer(play).hidePlayer(Main.get(), p);		
			sendKeepInTABPacket(Bukkit.getPlayer(play),p);
				}
			
			Set<UUID> newSpect = new HashSet<UUID>();
			newSpect.addAll(spectators);
			for (UUID spect : newSpect) {
				
				if (pp.getPlayer(p).isHidingSpectators()) {
					p.hidePlayer(Main.get(),Bukkit.getPlayer(spect));
				} else {
					p.showPlayer(Main.get(),Bukkit.getPlayer(spect));
				}
			

				if (pp.getPlayer(spect).isHidingSpectators()) {
					Bukkit.getPlayer(spect).hidePlayer(Main.get(), p);	
				} else {
					Bukkit.getPlayer(spect).showPlayer(Main.get(), p);	
				}
				
				sendKeepInTABPacket(Bukkit.getPlayer(spect),p);
			}
			
			p.teleport(arena.getLobby());	
				}
			}.runTask(Main.get());
			
	 }
	 
	public void spectateSpleef(Player p,Arena arena) {	
		Main.get().getLogger().info("spectateSpleef " + p.getName());
		arena.getSpectators().add(p.getUniqueId());
		Set<UUID> list = new HashSet<UUID>();
		for (UUID playing : arena.getPlayers()) {
			if (!arena.getSpectators().contains(playing)) 	list.add(playing);	
		}
		
		if (arena.isGuildGame()) {
			if (arena.getSpectators().size()+1>arena.getMaxSpectators()) arena.addMaxSpectators();
		}
		
		
		spectate(p,arena,list,arena.getSpectators(),arena.getGameType().equals(GameType.DUEL));
		
		new BukkitRunnable() {
			public void run() {
					if (DataPlayer.getPlayer().hasNightVision(p.getUniqueId())) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
						}
					p.setAllowFlight(true);
		p.setFlying(true);	
				
		}
		}.runTaskLater(Main.get(), 5L);
	}
	
	
	
	public void doEverything(Arena arena) {
		new BukkitRunnable() {
			public void run() {
		for (UUID sp : arena.getPlayers()) {
			for (UUID spect : arena.getSpectators()) {
				if (sp!=null && Bukkit.getOfflinePlayer(sp).isOnline() && spect!=null && Bukkit.getOfflinePlayer(spect).isOnline()) 
				Bukkit.getPlayer(sp).hidePlayer(Main.get(),Bukkit.getPlayer(spect));
				
				for (UUID otherPlayers : arena.getPlayers()) {
					if (!otherPlayers.equals(sp))Bukkit.getPlayer(sp).showPlayer(Main.get(),Bukkit.getPlayer(otherPlayers));
				}
				sendKeepInTABPacket(Bukkit.getPlayer(sp),Bukkit.getPlayer(spect));
			}
		}
			}
	}.runTask(Main.get());
	}
	
	
	public void showOrHideSpectators(Player p,Arena arena,boolean show) {
		new BukkitRunnable() {
			public void run() {
		pp.getPlayer(p).setHidingSpectators(!show);
		p.sendMessage("§aHiding spectators: §b" + String.valueOf(!show).toUpperCase());
		List<UUID> spectators = new ArrayList<UUID>();
			spectators.addAll(arena.getSpectators());
			for (UUID spect : spectators) {
				if (show) {
				 p.showPlayer(Main.get(), Bukkit.getPlayer(spect));	
				} else {
					 p.hidePlayer(Main.get(), Bukkit.getPlayer(spect));	
					
				}
				sendKeepInTABPacket(p,Bukkit.getPlayer(spect));
			}	
		}
		}.runTask(Main.get());
	}
	
	public void leaveSpectate(Player p, boolean allowFly) {
		DataManager.getManager().removeToSet("spectator-players", p.getUniqueId().toString());
		GamePlayer gp = PlayerManager.getManager().getPlayer(p);
		if (gp.getArena()!=null) gp.getArena().getSpectators().remove(p.getUniqueId());
		gp.setSpectating(false);
		//SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.SEND_TO_LOBBY, p.getUniqueId().toString());
		p.kickPlayer("End game");

		new BukkitRunnable() {
			public void run() {
				if (Bukkit.getOfflinePlayer(p.getUniqueId()).isOnline()) {
		ActionBarAPI.sendActionBar(p, "");
		if (allowFly) {
		p.setAllowFlight(false);
		p.setFlying(false);
		}
		for (Player p : Bukkit.getOnlinePlayers()) p.getPlayer().showPlayer(Main.get(),p);
		}
			}
		}.runTask(Main.get());
	}
	
	

	
	
	public void sendKeepInTABPacket(Player player, Player toShow) {
		EntityPlayer[] entity = { (EntityPlayer) ((CraftPlayer) toShow).getHandle()};
 		((CraftPlayer) player).getHandle().playerConnection.sendPacket( new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entity));
	}
	
}
