package me.santipingui58.game.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;



public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("spawn")){
			Player p = (Player) sender;
			GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			sp.leave();
		}
		}
		return false;
	}

}
