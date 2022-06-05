package me.santipingui58.game.commands;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class PauseCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("pause")){
			Player p = (Player) sender;
			GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			if (sp.isInGame()) {
				Arena arena = sp.getArena(); 
				if (arena.getGameType().equals(GameType.DUEL) && !arena.isAtMiniSpleef()) {
					
					if (!arena.getDeadPlayers1().contains(sp.getUUID()) && !arena.getDeadPlayers2().contains(sp.getUUID())) {
					if (!arena.getPauseRequest().contains(sp.getUUID())) {
						arena.getPauseRequest().add(sp.getUUID());
					if (arena.getPauseRequest().size()  >=  arena.getPlayers().size()-arena.getDeadPlayers1().size()+arena.getDeadPlayers2().size()) {
						GameManager.getManager().pauseWithCommand(arena);
					} else {
						sendRequest(arena,sp);
					}
				} else {
					arena.getPauseRequest().remove(sp.getUUID());
					Set<UUID> list = GameManager.getManager().leftPlayersToSomething(arena.getPauseRequest(), arena,false);
					
					for (UUID players : arena.getViewers()) {
						if (list.isEmpty()) {
							Bukkit.getPlayer(players).sendMessage("§b"+sp.getName()+" §chas cancelled their pause request.");
						} else {
							Bukkit.getPlayer(players).sendMessage("§b"+sp.getName()+" §chas cancelled their pause request §7(Left to accept: " + 
						Utils.getUtils().getPlayerNamesFromList(list) + ")");				
						}				
					}
				}
				} else {
					p.sendMessage("§cOnly alive players can request a /pause.");	
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
		
	Set<UUID> list = GameManager.getManager().leftPlayersToSomething(arena.getPauseRequest(), arena,false);
		
		if (list.isEmpty()) {
			GameManager.getManager().pauseWithCommand(arena);
		} else {
		
		for (UUID players : arena.getViewers()) {			
			if (!players.equals(sp)) {
				Bukkit.getPlayer(players).sendMessage("§b"+sp.getName() + "§9 has requested to pause the match. To accept the request do /pause §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				
				
				if (arena.getPlayers().contains(players) && !arena.getPauseRequest().contains(players)) {
					if (arena.getDeadPlayers1().contains(players) || arena.getDeadPlayers2().contains(players)) continue;
				TextComponent message = new TextComponent("§bClick here to pause the arena!");
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/pause"));
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aPause arena").create()));
				Bukkit.getPlayer(players).spigot().sendMessage(message);
				}
				
			} else {
				Bukkit.getPlayer(players).sendMessage("§9You sent a pause request to your opponent.");
			}
		}
		
		}
	}
	
}
