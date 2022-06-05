package me.santipingui58.game.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;

public class ChatCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("chat")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
            Player p = (Player) sender;
				 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				if (sp.getChat()) {
					sp.setChat(false);
					p.sendMessage("§cChat is now disabled! You will only see messages related to Splindux and from Staff members");
				} else {
					sp.setChat(true);	
					p.sendMessage("§aChat is now enabled!");
				}
				 
			}
		
}
			return false;
			
		}
}