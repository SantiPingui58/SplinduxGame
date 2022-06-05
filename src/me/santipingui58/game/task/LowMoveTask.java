package me.santipingui58.game.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.utils.Utils;

public class LowMoveTask {
	
	
	public LowMoveTask() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {	
					GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				spectatorDistance(sp);	
				setLocation(sp);
					boolean f = false;
					if (sp.isInGame() && f) {
						deleteFloorTimer(sp);	
				}
				
				
				
				//afkCheck(sp);
				
			
		
				sp.setLocation(sp.getPlayer().getLocation());
				potions(sp);
			}
			
				//Remove fire at any moment
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					if (p.getFireTicks()>1) {
						new BukkitRunnable() {
							public void run() {
						p.setFireTicks(0);
					}
						}.runTask(Main.get());
					}
				}		
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 20L);
		
		
	}
	
	

	public void deleteFloorTimer(GamePlayer sp) {
		if (!sp.getPlayer().isOnGround()) return;
		if (sp.isInGame() && sp.getArena().getState().equals(GameState.GAME) && sp.getArena().getSpleefType().equals(SpleefType.TNTRUN)) {
			if (sp.isSpectating()) return;
		            Player player = sp.getPlayer();
		            if ((!player.getGameMode().equals(GameMode.SPECTATOR)) && (!player.getGameMode().equals(GameMode.CREATIVE))) {	 
		            	List<Block> list = Utils.getUtils().getBlockBelow(player);
		           	
		            	if (list.size()<=0) return;
		            	 Block standingBlock = list.get(0);
		            	 if (standingBlock==null) return;
					      final Block blockBelow_1 = standingBlock.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
					      final Block blockBelow_2 = standingBlock.getLocation().subtract(0.0D, 2.0D, 0.0D).getBlock();					      
					      Material m1 = blockBelow_1.getType();
					      Material m2 = blockBelow_2.getType();
					      Material m3 = standingBlock.getType();
					      List<Material> allowedMaterials = new ArrayList<Material>();
					      allowedMaterials.add(Material.AIR);
					      allowedMaterials.add(Material.SAND);
					      allowedMaterials.add(Material.GRAVEL);
					      allowedMaterials.add(Material.TNT);
					
					      if (!allowedMaterials.contains(m1))  return;
					      if (!allowedMaterials.contains(m2))  return;
					      if (!allowedMaterials.contains(m3))  return;
					      
					      final Block block = standingBlock;
					      
					      double x = sp.getPlayer().getLocation().getBlockX();
					      double y = sp.getPlayer().getLocation().getBlockY();
					      double z = sp.getPlayer().getLocation().getBlockZ();
		              new BukkitRunnable() {
		            	 
		            	  public void run() {
		            		
		            		 if (block!=null) {
		            				if (sp.isInGame() && sp.getArena().getState().equals(GameState.GAME)) {
		            					 double xx = sp.getPlayer().getLocation().getBlockX();
		       					      double yy = sp.getPlayer().getLocation().getBlockY();
		       					      double zz = sp.getPlayer().getLocation().getBlockZ();
		       					      if (x==xx && y==yy && z==zz) {
		            					
		            					 if (!allowedMaterials.contains(m1))  return;
		       					      if (!allowedMaterials.contains(m2))  return;
		       					   if (!allowedMaterials.contains(m3))  return;
		       					block.setType(Material.AIR);
		              blockBelow_1.setType(Material.AIR);
		              blockBelow_2.setType(Material.AIR);
		       					      }
		              
		            		 }
		            		 }
		            }
		              }.runTaskLater(Main.get(),7L);
		            }
		        }
	}
	
	
	
	
	private void spectatorDistance(GamePlayer sp) {
		//To prevent spectators to get away from players on the game, if they are 40 blocks away from the player they are spectating, they get teleported to them.
		if (sp==null) return;
				if (sp.isSpectating()) {    	
					Arena arena = sp.getArena();
					if (arena.getLobby().getWorld().getName().equalsIgnoreCase(sp.getPlayer().getLocation().getWorld().getName())) {
						int distance = arena.getGameType().equals(GameType.FFA) ? 100 : 40;

						
					if (arena.getLobby().distanceSquared(sp.getPlayer().getLocation()) > distance*distance) {
						new BukkitRunnable() {
							public void run() {
						sp.getPlayer().teleport(arena.getLobby());
						}
						}.runTask(Main.get());
					}
					} else {
					}
				}
	}
	
	private void setLocation(GamePlayer sp) {
		//Set the old player location
					if (sp.getPlayer().isOnline()) {
					sp.setLocation(sp.getPlayer().getLocation());	
				}
	}
	
	

	


	private void potions(GamePlayer p) {
		if (p.getPlayer().getActivePotionEffects().size()>0) {
			for (PotionEffect effect : p.getPlayer().getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
					//PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(sp.getPlayer(),true);
					new BukkitRunnable() {	    			
				            @Override
				            public void run() {
				            	boolean b = true;
				            	if (!p.getOfflinePlayer().isOnline()) cancel();
				            	if (p.getPlayer().getActivePotionEffects().size()>0) {
					    			for (PotionEffect effect : p.getPlayer().getActivePotionEffects()) {
					    				if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
					    					b = false;
					    				}
					    			}
					    				}
				            	
				            	if (b) {
				            		//PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(sp.getPlayer(),false);
				            		cancel();
				            	}
				            }
				        }.runTaskTimer(Main.get(), 0L, 10);
				}
			
			}
		}
		
	}

}
