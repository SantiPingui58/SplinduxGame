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


public class ForceScoreCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcescore")){
				final Player p = (Player) sender;
				 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				if (args.length==2 && p.hasPermission("splindux.staff")) {
					
					if (sp.isInGame()|| sp.isSpectating()) {
						Arena arena = sp.getArena();
						if (arena.getGameType().equals(GameType.DUEL)) {
							
							int crumble = 0;

							try {
								crumble = Integer.parseInt(args[1]);
								if (crumble>=0 && crumble<=arena.getPlayTo()) {
									
									for (UUID spo : arena.getPlayers()) {
										if (Bukkit.getOfflinePlayer(spo).getName().equalsIgnoreCase(args[0])) {
											
											if (arena.getDuelPlayers1().contains(spo)) {
											arena.setPoints1(crumble);
											
											} else if (arena.getDuelPlayers2().contains(spo)) {
												arena.setPoints2(crumble);
											}
											for (UUID viewers : arena.getViewers()) Bukkit.getPlayer(viewers).sendMessage("§cA Staff has changed the score of the game.");
											break;	
										}
									}			
								} else {
										p.sendMessage("§cThe score must be a number between 0 and " + arena.getPlayTo() + ".");
										return false;
									}
								
							} catch (Exception e) {
								p.sendMessage("§a"+ args[1]+ " §cisnt a valid number.");
								return false;
							}

						} else { 
							p.sendMessage("§cYou can only execute this command in Duels");	
						}
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				}  else {
					p.sendMessage("/forcescore <player> <score>");
				}

	
	  
	
}
		return false;
	}
	
	

}