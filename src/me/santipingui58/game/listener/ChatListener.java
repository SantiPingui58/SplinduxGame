package me.santipingui58.game.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.Language;
import me.santipingui58.data.SplinduxDataAPI;
import me.santipingui58.data.integration.IntegrationBukkitType;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.level.LevelManager;
import me.santipingui58.translate.TranslateAPI;




public class ChatListener implements Listener {
	
	 DataPlayer dp = DataPlayer.getPlayer();
	
	private List<Player> cooldown = new ArrayList<Player>();
	@EventHandler (priority= EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		e.getRecipients().clear();
		Player p = e.getPlayer();
		GamePlayer sp = PlayerManager.getManager().getPlayer(p);
		  
		if (!p.hasPermission("splindux.chatcooldown")) {
			if (cooldown.contains(p)) {
					p.sendMessage("§cWait 2 seconds between messages.");
					e.setCancelled(true);
					return;
				}
				
			}
		cooldown.add(p);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldown.remove(p);
			}
		}.runTaskLaterAsynchronously(Main.get(), 40L);
			

		String prefix = ChatColor.translateAlternateColorCodes('&', sp.getPrefix());
		String level = LevelManager.getManager().getRank(p.getUniqueId()).getRankName();
		ChatColor c = ChatColor.WHITE;
		
		if (p.hasPermission("splindux.vip")) {
			c = ChatColor.DARK_AQUA;
		}
	
		
		//int pos = sp.getRankingPosition();
		String position = "";
		//if (pos!=-1) position = "§7("+pos+"º) ";
		prefix = position + prefix + "§7["+ level+"§7] "+c;
		String msg = e.getMessage();

		 msg = e.getMessage().replaceAll("%", "%%");
		 
		 List<GamePlayer> withTranslateESP = new ArrayList<GamePlayer>();
		 List<GamePlayer> withTranslateENG = new ArrayList<GamePlayer>();
		 List<GamePlayer> withTranslateRUS = new ArrayList<GamePlayer>();
		 
		 List<GamePlayer> withoutTranslate = new ArrayList<GamePlayer>();
		 withoutTranslate.add(sp);
		 
		 for (Player online : Bukkit.getOnlinePlayers()) {
			
			 if (online.equals(p)) continue;		
			 GamePlayer sonline = PlayerManager.getManager().getPlayer(online);

			 boolean condition = sonline.getChat() || p.hasPermission("splindux.staff");
			 if (condition) {
			 Language receptor = dp.getLanguage(online.getUniqueId());
			 Language emisor = dp.getLanguage(sp.getUUID());
			 if (dp.hasTranslate(online.getUniqueId()) && receptor!=emisor) {
				 if (receptor.equals(Language.SPANISH)) {
					 withTranslateESP.add(sonline);
				 } else if (receptor.equals(Language.ENGLISH)) {
					 withTranslateENG.add(sonline);
				 } else {
					 withTranslateRUS.add(sonline);
				 }
			 } else {
				 withoutTranslate.add(sonline);
			 }
		 }
		 }
		 
		 String message = "";
		 
		 ChatColor color = ChatColor.valueOf(dp.getDefaultColorChat(sp.getUUID()));
		 if (p.hasPermission("splindux.staff")) {
			 message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: " + color+msg);
		} else if (p.hasPermission("splindux.donatorchat")) {
			message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: "+color+msg);
		} else {
			message = prefix +" "+sp.getName() +"§8: §7"+ msg;
		}
	 e.setFormat(message);
		 for (GamePlayer player : withoutTranslate) {
			 player.sendMessage(message);
		 }
		 

	
				if (p.hasPermission("splindux.staff")) {
					 message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: " + color);
				} else if (p.hasPermission("splindux.donatorchat")) {
					message = ChatColor.translateAlternateColorCodes('&', prefix +sp.getName() +"§8: "+color);
				} else {
					message = prefix +" "+sp.getName() +"§8: §7";
				}
				
				final String m = message;
				try {
					TranslateAPI.getAPI().translate(dp.getLanguage(sp.getUUID()), Language.SPANISH, msg).thenAccept(text -> {
						
						for (GamePlayer player : withTranslateESP) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					TranslateAPI.getAPI().translate(dp.getLanguage(sp.getUUID()), Language.ENGLISH, msg).thenAccept(text -> {
						for (GamePlayer player : withTranslateENG) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					TranslateAPI.getAPI().translate(dp.getLanguage(sp.getUUID()), Language.RUSSIAN, msg).thenAccept(text -> {
						for (GamePlayer player : withTranslateRUS) player.sendMessage(m+text);
					});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				SplinduxDataAPI.getAPI().createIntegrationBukkit("lobby2",IntegrationBukkitType.DISCORD_MESSAGE,"[Spleef]" + m+message);
		
	}
	
	

}
