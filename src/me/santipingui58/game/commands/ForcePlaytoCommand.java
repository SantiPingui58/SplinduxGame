package me.santipingui58.game.commands;

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



public class ForcePlaytoCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forceplayto")){
				final Player p = (Player) sender;
				 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				if (args.length==1 && p.hasPermission("splindux.staff")) {
					
					if (sp.isInGame()|| sp.isSpectating()) {
						Arena arena = sp.getArena();

						if (arena.getGameType().equals(GameType.DUEL)) {
							
							int crumble = 0;

							try {
								crumble = Integer.parseInt(args[0]);
								if (crumble>=1 && crumble<=99) {
									arena.setPlayTo(crumble);
									for (UUID viewers : arena.getViewers()) Bukkit.getPlayer(viewers).sendMessage("§cA Staff has changed the play to of the game.");
								} else {
										p.sendMessage("§cThe score must be a number between 1 and 99.");
										return false;
									}
								
							} catch (Exception e) {
								p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
								return false;
							}

						} else { 
							p.sendMessage("§cYou can only execute this command in Duels");	
						}
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				}  else {
					p.sendMessage("/forceplayto <playto>");
				}

	
	  
	
}
		return false;
	}
	
	

}