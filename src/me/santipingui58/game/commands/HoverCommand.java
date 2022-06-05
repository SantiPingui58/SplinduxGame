package me.santipingui58.game.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.request.ArenaRequest;



public class HoverCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
			
		if(cmd.getName().equalsIgnoreCase("hover")){
			Player p = (Player) sender;
			if (args.length==0) return false;
			 GamePlayer sp = PlayerManager.getManager().getPlayer(p);		 
			 switch(args[0]) {
			 case "crumblecancel": crumbleCancel(sp,args);
			 return true;
			 case "crumbledeny": crumbleDeny(sp,args);
			 return true;
			 case "crumbleaccept": crumbleAccept(sp,args);
			 return true;
			 case "playtocancel": playtoCancel(sp,args);
			 return true;
			 case "playtodeny": playtoDeny(sp,args);
			 return true;
			 case "playtoaccept": playtoAccept(sp,args);
			 }		 
			} 			
}	
		return false;
	}


	private void playtoAccept(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getPlayToRequest()!=null) {
				ArenaRequest request = arena.getPlayToRequest();
				if (!request.getAcceptedPlayers().contains(sp.getUUID())) {
					request.playtoAccept(sp.getUUID());
				} else {
					p.sendMessage("§cYou already accepted this request.");
				}
			}else {
				p.sendMessage("§cThis crumble request has expired.");
			}
	
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void playtoDeny(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getPlayToRequest()!=null) {
			for (UUID dueled : arena.getPlayers()) {
				Bukkit.getPlayer(dueled).sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Playto cancelled.");
			}
			
			arena.setPlayToRequest(null);
			
			} else {
				p.sendMessage("§cThis crumble request has expired.");
			}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void playtoCancel(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			ArenaRequest request = arena.getPlayToRequest();		
		if (request!=null) {
			if (request.getChallenger().equals(sp.getUUID())) {
		for (UUID dueled : arena.getViewers()) {
			Bukkit.getPlayer(dueled).sendMessage("§b" + sp.getName() + "§c has cancelled the  playto request.");
		}
		arena.setPlayToRequest(null);
			} else {
				p.sendMessage("§cThis duel request has expired.");
			}
		} else {
			p.sendMessage("§cThis duel request has expired.");
		}
	} else {
		p.sendMessage("§cThis crumble request has expired.");
	}
		
	}



	private void crumbleAccept(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (!arena.getDeadPlayers1().contains(sp.getUUID()) && !arena.getDeadPlayers2().contains(sp.getUUID())) {
			if (arena.getCrumbleRequest()!=null) {
				ArenaRequest request = arena.getCrumbleRequest();
				if (!request.getAcceptedPlayers().contains(sp.getUUID())) {
					request.crumbleAccept(sp.getUUID());
				} else {
					p.sendMessage("§cYou already accepted this request.");
				}
			}else {
				p.sendMessage("§cThis crumble request has expired.");
			}
	} else {
		p.sendMessage("§cOnly alive players can execute this command.");
	}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}
		
	}



	private void crumbleDeny(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			if (arena.getCrumbleRequest()!=null) {
			for (UUID dueled : arena.getPlayers()) {
				Bukkit.getPlayer(dueled).getPlayer().sendMessage("§cThe player §b" + sp.getName() + "§c has denied the request! Crumble cancelled.");
			}
			
			arena.setCrumbleRequest(null);
			
			} else {
				p.sendMessage("§cThis crumble request has expired.");
			}
		} else {
			p.sendMessage("§cThis crumble request has expired.");
		}		
	}



	private void crumbleCancel(GamePlayer sp, String[] args) {
		Player p = sp.getPlayer();
		if (sp.isInGame()) {
			Arena arena = sp.getArena();
			ArenaRequest request = arena.getCrumbleRequest();		
		if (request!=null) {
			if (request.getChallenger().equals(sp.getUUID())) {
		for (UUID dueled : arena.getViewers()) {
			Bukkit.getPlayer(dueled).getPlayer().sendMessage("§b" + sp.getName() + "§c has cancelled the  crumble request.");
		}
		
			arena.setCrumbleRequest(null);
			} else {
				p.sendMessage("§cThis duel request has expired.");
			}
		} else {
			p.sendMessage("§cThis duel request has expired.");
		}
	} else {
		p.sendMessage("§cThis crumble request has expired.");
	}	
	}



	
}