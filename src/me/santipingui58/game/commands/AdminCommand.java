package me.santipingui58.game.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;

public class AdminCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	
		
		if(cmd.getName().equalsIgnoreCase("admin")){
			final CommandSender p = sender;
			if (p.isOp()) {
				 if (args[0].equalsIgnoreCase("loadedarenas")) {
					 p.sendMessage("Arenas:" + GameManager.getManager().getArenas().size());
			for (Arena arena : GameManager.getManager().getArenas()) {
				p.sendMessage(arena.getName() + " " +arena.getGameType().toString() + " " + arena.getSpleefType().toString());
			}
			}
		}
			
}
		
		
		return false;
	}
	

		 
	
}