package me.santipingui58.game.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.game.death.BreakReason;
import me.santipingui58.game.game.death.BrokenBlock;
import me.santipingui58.game.game.ffa.FFA;
import me.santipingui58.game.game.ffa.GameMutation;
import me.santipingui58.game.game.ffa.MutationType;

public class PlayerListener implements Listener {

	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();  

		if (!p.getGameMode().equals(GameMode.CREATIVE)) {
			
			 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			 if (sp.isSpectating()) e.setCancelled(true);
			if (sp.isInGame()) {
				if (sp.getArena().getState().equals(GameState.GAME)) {
				if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT) && !e.getBlock().getType().equals(Material.CONCRETE_POWDER)) {
					e.setCancelled(true);
				} else {
					if (sp.getArena().getGameType().equals(GameType.FFA)) {
						FFA ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
						for (GameMutation mutation : ffa.getInGameMutations()) {
							if (mutation.getType().equals(MutationType.KOHI_SPLEEF)) {
								sp.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
								break;
							}
						}
						BrokenBlock kill = new BrokenBlock(sp.getUUID(),e.getBlock().getLocation(),BreakReason.SHOVEL);
						ffa.getBrokenBlocks().add(kill);
					}
					
				
					
				}
				} else if (sp.getArena().getState().equals(GameState.LOBBY) && sp.getArena().getGameType().equals(GameType.FFA)){
					if (!e.getBlock().getType().equals(Material.SNOW_BLOCK) && !e.getBlock().getType().equals(Material.TNT)) {
						e.setCancelled(true);
					} else {
					new BukkitRunnable() {
						public void run() {
							e.getBlock().setType(Material.SNOW_BLOCK);
						}
					}.runTaskLater(Main.get(), 20L*7);
					}
				} else {
					e.setCancelled(true);
				}
			} else {
				e.setCancelled(true);
			}
		} 
	}
	
	@EventHandler
	public void onHand(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		GamePlayer sp = PlayerManager.getManager().getPlayer(p);
		
		  if (e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("arenas") && (e.getBlock().getType().equals(Material.TNT))) {
			 try {
				 if (sp.getArena().getGameType().equals(GameType.FFA)) {
				 FFA ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
				for (GameMutation mutation : ffa.getInGameMutations()) {
					if (mutation.getType().equals(MutationType.TNT_SPLEEF)) {
						mutation.getTNT().add(e.getBlock().getLocation());
					}
				}
				 }
				
			 } catch (Exception ex) {}
			return;  
		  }


		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		} 
	}
	
}

		
	