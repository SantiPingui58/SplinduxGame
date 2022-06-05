package me.santipingui58.game.task;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.DataManager;
import me.santipingui58.data.SplinduxDataAPI;
import me.santipingui58.data.integration.IntegrationBukkitType;
import me.santipingui58.data.integration.IntegrationBungeeType;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.Main;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.SpectateManager;
import me.santipingui58.game.utils.Utils;




public class IntegrationTask {
	
	//SERVER;Type;Message
	public IntegrationTask() {
		 new BukkitRunnable() {
			 @Override
			public void run() {
				Set<String> list = new HashSet<String>();
				list.addAll(DataManager.getManager().getSet("bukkit-integration"));
				for (String s : list) {	
					String[] mgs = s.split(";");			
					if (mgs[0].equalsIgnoreCase(Main.server)) {	
						IntegrationBukkitType type = IntegrationBukkitType.valueOf(mgs[1]);
						String message = mgs[2];
						String[] data = message.split(",");
						try {
						switch(type) {
						 //SantiPingui58-hikarilof-slenderbrine100-Triffuny,arena,spleef,4,canTie,maxTime,exactArena
						case START_GAME:
							String[] players = data[0].split("_");
							 Set<UUID> uuids = new HashSet<UUID>();
							for (String ss : players) uuids.add(UUID.fromString(ss));
							SpleefType spleefType = SpleefType.valueOf(data[2]);
							int teamSize = Integer.parseInt(data[3]);
							boolean cantie = Boolean.parseBoolean(data[4]);
							int time = Integer.parseInt(data[5]);
							 String ar= data[7];
							 new BukkitRunnable() {
								 public void run() {
										GameManager.getManager().duelGame(uuids, ar, spleefType, teamSize, cantie, time);
								 }
							 }.runTask(Main.get());
						
							SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.MOVE_PLAYER,Main.server+","+data[0]);
						
							break;		
							
							//player
						case SPECTATE:
							if (Bukkit.getOfflinePlayer(UUID.fromString(data[0])).isOnline())
								SpectateManager.getManager().preSpectate(Bukkit.getPlayer(UUID.fromString(data[0])));
							break;
						case TITLE:		
							String title = data[0];
							String subtitle = data[1];
							Utils.getUtils().sendTitles(Bukkit.getOnlinePlayers(), title, subtitle, 5, 100, 5);
							break;
						default:
							break;
						} 
						}  catch(Exception ex) {
							Bukkit.getLogger().info("<INTEGRATION ERROR>");
							Bukkit.getLogger().info("Server: " + mgs[0]);
							Bukkit.getLogger().info("Type: " + type.toString());
							Bukkit.getLogger().info("Message: " + message);
							ex.printStackTrace();
						}
						DataManager.getManager().removeToSet("bukkit-integration", s);
					}
				}
				
			}
		 }.runTaskTimerAsynchronously(Main.get(), 0L, 2L);
	}
}
