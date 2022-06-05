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
import me.santipingui58.game.game.GameEndReason;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.GameState;



public class ForceResetCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
			} else if	(cmd.getName().equalsIgnoreCase("forcereset")){
				final Player p = (Player) sender;
				 GamePlayer sp = PlayerManager.getManager().getPlayer(p);
				if (args.length==0 && p.hasPermission("splindux.staff")) {
					
					if (sp.isInGame()|| sp.isSpectating()) {
						Arena arena = sp.getArena();
						
						if (arena.getGameType().equals(GameType.DUEL)) {
							if (arena.getState().equals(GameState.PAUSE)) {
								sp.sendMessage("§cYou cannot execute this command while the game is paused.");
								return false;
							}
							GameManager.getManager().resetArenaWithCommand(arena);
							for (UUID viewers : arena.getViewers()) Bukkit.getPlayer(viewers).sendMessage("§cA Staff has reset the arena.");
						} else if (arena.getGameType().equals(GameType.FFA)){
							GameManager.getManager().getFFAArenaByArena(arena).endGame(GameEndReason.TIME_OUT);
							for (UUID viewers : arena.getViewers()) Bukkit.getPlayer(viewers).sendMessage("§cA Staff has finished the round.");

						}
						
					} else {
						p.sendMessage("§cYou need to be in a game to execute this command.");	
					}
				} 

	
	  
	
}
		return false;
	}
	
	

}