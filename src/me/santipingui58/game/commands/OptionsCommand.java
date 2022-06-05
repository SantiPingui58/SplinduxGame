package me.santipingui58.game.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class OptionsCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("options")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
            @SuppressWarnings("unused")
			Player p = (Player) sender;
				// SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			//	 new OptionsMenu(sp).o(p);
				 
			}
		
}
			return false;
			
		}
}