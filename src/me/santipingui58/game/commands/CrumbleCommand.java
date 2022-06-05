package me.santipingui58.game.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.data.spleef.GameType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.game.request.ArenaRequest;
import me.santipingui58.game.game.request.RequestType;




public class CrumbleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
		if(cmd.getName().equalsIgnoreCase("crumble")){
			Player p = (Player) sender;
			GamePlayer sp = PlayerManager.getManager().getPlayer(p);
			
			if (sp.isInGame()) {
				Arena arena = sp.getArena();
				if (arena.getGameType().equals(GameType.DUEL)) {
					if (arena.getCrumbleRequest()==null) {
					if (!arena.getDeadPlayers1().contains(sp.getUUID()) && !arena.getDeadPlayers2().contains(sp.getUUID())) {
					if (args.length==0) {
						p.sendMessage("§aUse of command: /crumble <percentage>");
						p.sendMessage("§7(The percentage must be between 10 and 90, the higher will be the less snow in the arena)");
					} else {
						int crumble = 0;

						try {
							crumble = Integer.parseInt(args[0]);
							if (crumble>=10 && crumble<=90) {
								if (arena.getState().equals(GameState.PAUSE)) {
									sp.sendMessage("§cYou cannot execute this command while the game is paused.");
									return false;
								}
								ArenaRequest request = new ArenaRequest(arena,sp.getUUID(), crumble,RequestType.CRUMBLE);
								arena.setCrumbleRequest(request);
								request.sendMessage();
							} else {
								p.sendMessage("§cThe percentage must be a number between 10 and 90.");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
							return false;
						}
							
						}
						
				} else {
					p.sendMessage("§cOnly alive players can request a /crumble.");	
				}
				} else {
					p.sendMessage("§cThere is a crumble request at the moment, cancel it or deny it to make a new one.");	
				}
				
				}else {
					p.sendMessage("§cYou need to be in a 1v1 game to execute this command.");	
				} 
			
							
			} else {
				p.sendMessage("§cYou can not execute this command here.");		
			}
		} 
		
}
		return false;
		
	}


	
}
