package me.santipingui58.game.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.game.PlayerManager;

public class NightVisionCommand implements CommandExecutor {
	
	

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			
			if(cmd.getName().equalsIgnoreCase("nightvision")) {
			if(!(sender instanceof Player)) {
				
				return true;
				
			} else {
            Player p = (Player) sender;
				DataPlayer dp = DataPlayer.getPlayer();
				if (dp.hasNightVision(p.getUniqueId())) {
					dp.nightVision(p.getUniqueId(),false);
					p.sendMessage("§cNight vision is now disabled!");
					p.removePotionEffect(PotionEffectType.NIGHT_VISION);
				} else {
					dp.nightVision(p.getUniqueId(),true);	
					p.sendMessage("§aNight vision is now enabled!");
					if (PlayerManager.getManager().getPlayer(p).isInGame()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1));
					}
				}
				 
			}
		
}
			return false;
			
		}
}