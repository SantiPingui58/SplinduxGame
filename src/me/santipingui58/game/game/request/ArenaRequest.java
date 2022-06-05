package me.santipingui58.game.game.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ArenaRequest {
	private UUID uuid;
	private Arena arena;
	private UUID challenger;
	private List<UUID> acceptedPlayers;
	private int amount;
	private RequestType type;
	public ArenaRequest(Arena arena,UUID challenger,int amount,RequestType type) {
		this.arena = arena;
		uuid = UUID.randomUUID();
		this.type = type;
		this.challenger = challenger;
		this.acceptedPlayers = new ArrayList<UUID>();
		this.amount = amount;
	}
	
	
	public RequestType getType() {
		return this.type;
	} 
	
	public int getAmount() {
		return this.amount;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	public UUID getChallenger() {
		return this.challenger;
	}
	
	public List<UUID> getAcceptedPlayers() {
		return this.acceptedPlayers;
	}
	
	public void sendMessage() {
		List<UUID> list = new ArrayList<UUID>();		
		
		for (UUID splayer : arena.getPlayers()) {
			if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())) {
				if (this.type.equals(RequestType.CRUMBLE) && !arena.getDeadPlayers1().contains(splayer)&& !arena.getDeadPlayers2().contains(splayer)) { 
				list.add(splayer);
			} else  {
				list.add(splayer);
			}
				
				
			}
		}
		
		Player cha = Bukkit.getPlayer(challenger);
		
		for (UUID players : arena.getViewers()) {
			Player pl = Bukkit.getPlayer(players);
			if (this.type.equals(RequestType.CRUMBLE)) {
				if (players!=this.challenger) {
					pl.sendMessage("§b"+cha.getName() + "§6 has requested to crumble the field with " + this.amount+"%. §7(Left to accept: " 
			+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
				} else {
					Bukkit.getPlayer(players).sendMessage("§6You sent a crumble request to your opponent.");
				}
			} else {
				pl.sendMessage("§b"+cha.getName() + "§6 has requested to play to " + this.amount+". §7(Left to accept: " 
						+ Utils.getUtils().getPlayerNamesFromList(list) + ")");
			}
			if (arena.getPlayers().contains(players)) {
			if (players!=this.challenger) {
				if (this.type.equals(RequestType.PLAYTO)) {
					pl.spigot().sendMessage(getInvitation(this.challenger));
				} else {
					if (!arena.getDeadPlayers1().contains(players) && !arena.getDeadPlayers2().contains(players))
						pl.spigot().sendMessage(getInvitation(this.challenger));
				}
				
			} else {
				
				TextComponent msg1 = null;
				if (this.type.equals(RequestType.CRUMBLE)) {
					msg1=new TextComponent("[CANCEL CRUMBLE]");
				} else {
					msg1=new TextComponent("[CANCEL PLAYTO]");
				}
				msg1.setColor(net.md_5.bungee.api.ChatColor.RED );
				msg1.setBold( true );
				if (this.type.equals(RequestType.CRUMBLE)) {
				msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumblecancel"));	
				msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cCancel crumble request").create()));
				} else {
					msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtocancel"));	
					msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cCancel play to request").create()));
				}
				ComponentBuilder cb = new ComponentBuilder(msg1);
				cha.spigot().sendMessage(cb.create());
			}
			}
		}
	}
	
	private BaseComponent[] getInvitation(UUID dueler) {
		TextComponent msg1 = null;
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg1	= new TextComponent("[CRUMBLE]");
		} else  {
			msg1	= new TextComponent("[PLAYTO]");
		}
		msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN);
		msg1.setBold( true );
		
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbleaccept"));	
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept crumble request").create()));
		} else {
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtoaccept"));	
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept play to request").create()));
		}
		TextComponent msg2 = null;
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg2	= new TextComponent("[DENY CRUMBLE]");
		} else  {
			msg2	= new TextComponent("[DENY PLAYTO]");
		}
		msg2.setColor(net.md_5.bungee.api.ChatColor.RED);
		msg2.setBold( true );
		if (this.type.equals(RequestType.CRUMBLE)) {
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny crumble request").create()));
		} else {
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny play to request").create()));
		}
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		return cb.create();
	}

	public void crumbleAccept(UUID sp) {
		Player p = Bukkit.getPlayer(sp);
		getAcceptedPlayers().add(sp);
		if (getAcceptedPlayers().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
			GameManager.getManager().crumbleWithCommand(arena,getAmount());
		} else {
			List<UUID> leftToAccept = new ArrayList<UUID>();		
			
			for (UUID splayer : arena.getPlayers()) {
				if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())
						&& !arena.getDeadPlayers1().contains(splayer)&& !arena.getDeadPlayers2().contains(splayer)) {
					leftToAccept.add(splayer);
				}
			}
			TextComponent msg1 = new TextComponent("[CRUMBLE]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbleaccept "));		
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept crumble request").create()));
			TextComponent msg2 = new TextComponent("[DENY CRUMBLE]");
			msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
			msg2.setBold( true );
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbledeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny crumble request").create()));
			
			ComponentBuilder cb = new ComponentBuilder(msg1);
			cb.append(" ");
			cb.append(msg2);
			BaseComponent[] bc =  cb.create();			
			if (leftToAccept.isEmpty()) {
				GameManager.getManager().crumbleWithCommand(arena,getAmount());
				return;
			} 
			for (UUID dueled : arena.getPlayers()) {			
				Player du = Bukkit.getPlayer(dueled);			
				du.sendMessage("§6"+p.getName() + " §ahas accepted the request! §7(Left to accept: " + 
				Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
				if (dueled!=getChallenger()) {
					if (!getAcceptedPlayers().contains(dueled) && !arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp))
				du.spigot().sendMessage(bc);
			}
			}
		}
		
		
	}

	public void playtoAccept(UUID sp) {
		getAcceptedPlayers().add(sp);
		if (getAcceptedPlayers().size()>=arena.getPlayers().size()) {
			GameManager.getManager().playToWithCommand(arena, getAmount());
		} else {
			List<UUID> leftToAccept = new ArrayList<UUID>();		
			
			for (UUID splayer : arena.getPlayers()) {
				if (!getAcceptedPlayers().contains(splayer) && !splayer.equals(getChallenger())) {
					leftToAccept.add(splayer);
				}
			}
			TextComponent msg1 = new TextComponent("[PLAYTO]");
			msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
			msg1.setBold( true );
			msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtoaccept "));	
			msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept play to request").create()));
			
			TextComponent msg2 = new TextComponent("[DENY PLAYTO]");
			
			msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
			msg2.setBold( true );
			msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
			msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny play to request").create()));
			ComponentBuilder cb = new ComponentBuilder(msg1);
			cb.append(" ");
			cb.append(msg2);
			BaseComponent[] bc =  cb.create();			
			if (leftToAccept.isEmpty()) {
				GameManager.getManager().playToWithCommand(arena, getAmount());
				return;
			} 
			
			for (UUID dueled : arena.getPlayers()) {	
				Player du = Bukkit.getPlayer(dueled);
				du.sendMessage("§6"+Bukkit.getPlayer(sp) + " §ahas accepted the request! §7(Left to accept: " + 
				Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
				if (dueled!=getChallenger()) {
					if (!getAcceptedPlayers().contains(dueled))
				du.spigot().sendMessage(bc);
			}
			}
		}
		
		
	}
	
}
