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



public class ForceGameTimeCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcegametime")){
				final Player p = (Player) sender;
				 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				if (p.hasPermission("splindux.admin")) {
					if (sp.isInGame()|| sp.isSpectating()) {
						Arena arena = sp.getArena();
						int crumble = 0;

						try {
							crumble = Integer.parseInt(args[0]);
								
								arena.setTotalTime(crumble);
								
								if (arena.getTotalTime()>=420) {
									if (arena.getTotalTime()>=1070) {
										arena.setDecayRound(4);
									} else if (arena.getTotalTime()>=900) {
										arena.setDecayRound(3);
									}  else if (arena.getTotalTime()>=750) {
										arena.setDecayRound(2);
									}  else if (arena.getTotalTime()>=600) {
										arena.setDecayRound(1);
									} 
									
									arena.decay(true);
								}
								
								for (UUID viewers : arena.getViewers()) Bukkit.getPlayer(viewers).sendMessage("§cA Staff has changed the Arena Total Time.");
							
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
						
						
						
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				} 

	
	  
	
}
		return false;
	}
	
	

}