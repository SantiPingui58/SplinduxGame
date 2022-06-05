package me.santipingui58.game.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class ResetCommand implements CommandExecutor {

	
	private Set<UUID> delay = new HashSet<UUID>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("reset")){
			Player p = (Player) sender;
			GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL) && arena.getSpleefType().equals(SpleefType.SPLEEF) && !arena.isAtMiniSpleef()) {
					if (!arena.getDeadPlayers1().contains(sp.getUUID()) && !arena.getDeadPlayers2().contains(sp.getUUID())) {
						if (arena.getState().equals(GameState.PAUSE)) {
							sp.sendMessage("§cYou cannot execute this command while the game is paused.");
							return false;
						}
						
						if (delay.contains(sp.getUUID())) {
							sp.sendMessage("§cPlease wait 3 seconds before execute this command.");
							return false;
						} 
						
						delay.add(sp.getUUID());
						new BukkitRunnable() {
							public void run() {
								delay.remove(sp.getUUID());
							}
						}.runTaskLaterAsynchronously(Main.get(), 60L);
						
						
					if (!arena.getResetRequest().contains(sp.getUUID())) {
						arena.getResetRequest().add(sp.getUUID());
						
						
					if (arena.getResetRequest().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
						GameManager.getManager().resetArenaWithCommand(arena);
					} else {
						sendRequest(arena,sp);
						if (arena.getDuelPlayers1().contains(sp.getUUID())) {
							boolean allteam = true;
							for (UUID team : arena.getDuelPlayers1()) {
								if (arena.getDeadPlayers1().contains(team)) continue;
								if (!arena.getResetRequest().contains(team)) {
								allteam = false;
										break;	
								}
							}
							if (allteam) {
								
								for (UUID players : arena.getViewers()) {
									if (arena.getDuelPlayers1().size()!=1) {
									Bukkit.getPlayer(players).sendMessage("§aAll §9blue team §ahas accepted the reset request at §b" + Utils.getUtils().time(arena.getTotalTime()));
								}else {
									Bukkit.getPlayer(players).sendMessage("§6"+ sp.getName() + " §ahas accepted the reset request at §b" + Utils.getUtils().time(arena.getTotalTime()));
								}
								}
							}
							
						} else if (arena.getDuelPlayers2().contains(sp.getUUID())) {
							boolean allteam = true;
							for (UUID team : arena.getDuelPlayers2()) {
								if (arena.getDeadPlayers2().contains(team)) continue;
								if (!arena.getResetRequest().contains(team)) {
								allteam = false;
										break;	
								}
							}
							if (allteam) {			
								for (UUID players : arena.getViewers()) {
									if (arena.getDuelPlayers2().size()!=1) {
										Bukkit.getPlayer(players).sendMessage("§aAll §cred team §ahas accepted the reset request at §b" + Utils.getUtils().time(arena.getTotalTime()));
								}else {
									Bukkit.getPlayer(players).sendMessage("§6"+ sp.getName() + " §ahas accepted the reset request at §b" + Utils.getUtils().time(arena.getTotalTime()));
								}
								}
							}
							
						}
						
					}
					
					
				} else {					
					arena.getResetRequest().remove(sp.getUUID());
					Set<UUID> list = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
					
					for (UUID players : arena.getViewers()) {
						if (list.isEmpty()) {
							Bukkit.getPlayer(players).sendMessage("§b"+sp.getName()+" §chas cancelled their reset request.");
						} else {
							Bukkit.getPlayer(players).sendMessage("§b"+sp.getName()+" §chas cancelled their reset request §7(Left to accept: " + 
						Utils.getUtils().getPlayerNamesFromList(list) + ")");
									
						}
					
					}
				}
				} else {
					p.sendMessage("§cOnly alive players can request a /reset.");	
				}
				} else {
					p.sendMessage("§cYou can not execute this command here.");		
				}
			} else {
				p.sendMessage("§cYou need to be in a duel game to execute this command.");	
			}
		}
		}
		return false;
	}

	
	
	private void sendRequest(Arena arena,GamePlayer sp) {	
		
	Set<UUID> list = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().resetArenaWithCommand(arena);
		} else {
		
		for (UUID players : arena.getViewers()) {			
			if (!players.equals(sp.getUUID())) {
				Bukkit.getPlayer(players).sendMessage("§b"+sp.getName() + "§6 has requested a reset of the field. To accept the request do /reset §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getResetRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to reset the arena!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reset"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aReset arena").create()));
				Bukkit.getPlayer(players).spigot().sendMessage(message);
				}
				
			} else {
				Bukkit.getPlayer(players).sendMessage("§6You sent a reset request to your opponent.");
			}
		}
		
		}
	}
	
}
