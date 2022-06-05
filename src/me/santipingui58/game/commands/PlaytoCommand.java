package me.santipingui58.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.request.ArenaRequest;
import me.santipingui58.game.game.request.RequestType;

public class PlaytoCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("playto")){
			Player p = (Player) sender;
			GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			
			if (sp.isInGame()) {
				Arena arena = sp.getArena();
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (arena.getPlayToRequest()==null) {
					if (args.length==0) {
						p.sendMessage("§aUse of command: /playto <number>");
						p.sendMessage("§7(The percentage must be between 1 and 99");
					} else {
						int crumble = 0;
						try {
							crumble = Integer.parseInt(args[0]);
							if (crumble>=1 && crumble<=99) {
								
							} else {
								p.sendMessage("§cThe number must between 1 and 99.");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
						
						ArenaRequest request = new ArenaRequest(arena,sp.getUUID(), crumble,RequestType.PLAYTO);
						arena.setPlayToRequest(request);
						request.sendMessage();
						}
					} else {
						p.sendMessage("§cThere is a playto request at the moment, cancel it or deny it to make a new one.");	
					}
					
			}	else {
				p.sendMessage("§cYou need to be in a duel game to execute this command.");	
				}
			
							
			} else {
				p.sendMessage("§cYou can not execute this command here.");		
			}
		} 
		
}
		return false;
		
	}



}
