package me.santipingui58.game.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.data.Language;
import me.santipingui58.data.player.DataPlayer;



public class TranslateCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("translate")){
			final Player p = (Player) sender;
			DataPlayer dp = DataPlayer.getPlayer();
			if (args.length==0) {
				help(p);
			} else if (args[0].equalsIgnoreCase("setlang") && args.length==2) {
				if (args[1].equalsIgnoreCase("SPANISH") || args[1].equalsIgnoreCase("ENGLISH") || args[1].equalsIgnoreCase("RUSSIAN")) {
					String la = args[1].toUpperCase();
					Language l = Language.valueOf(la);
					DataPlayer.getPlayer().setLanguage(p.getUniqueId(),l);
					p.sendMessage("§aLanguage set to: §b" + l.toString()+"§a!");
				} else {
					help(p);
				}
			} else if (args[0].equalsIgnoreCase("translate") && args.length==1) {
				boolean b = !dp.hasTranslate(p.getUniqueId());
				dp.translate(p.getUniqueId(), b);
				p.sendMessage("§aAutomatic translate set to: §b" +b+"§a!");
			} else {
				help(p);
			}
			}
			

}
		
		
		return false;
	}
	
	

	private void help(Player p) {
		p.sendMessage("§aUse of command: /translate setlang <SPANISH/ENGLISH/RUSSIAN>");
		p.sendMessage("§aUse of command: /translate translate");
	}
	
}