package me.santipingui58.game.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.DataManager;
import me.santipingui58.data.SplinduxDataAPI;
import me.santipingui58.data.integration.IntegrationBukkitType;
import me.santipingui58.data.integration.IntegrationBungeeType;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.data.spleef.GameType;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.death.BreakReason;
import me.santipingui58.game.game.death.BrokenBlock;
import me.santipingui58.game.game.death.DeathReason;
import me.santipingui58.game.game.ffa.FFA;
import me.santipingui58.game.game.ranked.RankedTeam;
import me.santipingui58.game.game.request.ArenaRequest;
import me.santipingui58.game.level.LevelManager;
import me.santipingui58.game.utils.ActionBarAPI;
import me.santipingui58.game.utils.ItemBuilder;
import me.santipingui58.game.utils.Utils;


public class GameManager {

	private static GameManager manager;	
	
 public static GameManager getManager() {
        if (manager == null)
        	manager = new GameManager();
        return manager;
    }
 
 
 private Set<Arena> arenas = new HashSet<Arena>();
	
 public Set<Arena> getArenas() {
	 return this.arenas;
 }
 

 DataPlayer dp = DataPlayer.getPlayer();
 
 
 public void loadArenas() { 
 	new BukkitRunnable() {
 		public void run() {
 	    	int arenasint = 0;
 	if (Main.arenas.getConfig().contains("arenas")) {
 	Set<String> arenas = Main.arenas.getConfig().getConfigurationSection("arenas").getKeys(false);
 		for (String b : arenas) {		
 			try {
 			
     			Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena1"), false);
     			Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena2"), false);
     			Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".lobby"), false);	
 				String stype = Main.arenas.getConfig().getString("arenas."+b+".spleeftype");
 				String gtype = Main.arenas.getConfig().getString("arenas."+b+".gametype");
 				stype = stype.toUpperCase();	
 				gtype = gtype.toUpperCase();	
 				SpleefType spleeftype = SpleefType.valueOf(stype);
 				GameType gametype = GameType.valueOf(gtype);
 				int min = Main.arenas.getConfig().getInt("arenas."+b+".min_size");
					int max = Main.arenas.getConfig().getInt("arenas."+b+".max_size");
					if (gametype.equals(GameType.DUEL)) {
 					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
         			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
         			
         			String it = null;
         			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
      				it = it.toUpperCase();
      				Material item = Material.valueOf(it);
         			loadArena(b,null,spawn1,spawn2,lobby,arena1,arena2,spleeftype,gametype,item,min,max,null,null,0);
         			
 				}
				arenasint++;
			
 			} catch (Exception e) {
 				Main.get().getLogger().info(b);
 				e.printStackTrace();
 			}
 		}
 		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
 		
 		/*
 		GameManager.getManager().loadFFAArenas();
 		
 		loadFFAArenaNoRotate(SpleefType.SPLEEF,0,null);
 		
 		loadFFAArenasRotate(SpleefType.SPLEGG,-1);
 		loadFFAArenasRotate(SpleefType.TNTRUN,-1);
 		*/
 	}
 	}
 	}.runTaskAsynchronously(Main.get());

 }
 
 
 public Arena loadArena(String name, Location mainspawn,Location spawn1,Location spawn2,Location lobby,Location arena1,Location arena2,
		 SpleefType spleeftype,GameType gametype,Material item,int min,int max,Location flag1, Location flag2,int radious) {  
	 Arena a = null;
	 if (gametype.equals(GameType.DUEL)) {
		 a = new Arena(name,spawn1,spawn2,arena1,arena2,lobby,spleeftype,gametype,item,min,max);
	 } 
	 
    this.arenas.add(a);     
    DataManager.getManager().addToSet("loaded-arenas", name+"."+Main.server);
    DataManager.getManager().set("arena."+name+"."+Main.server+".ingame", "false");
    DataManager.getManager().set("arena."+name+"."+Main.server+".min", String.valueOf(min));
    DataManager.getManager().set("arena."+name+"."+Main.server+".max", String.valueOf(max));
    DataManager.getManager().set("arena."+name+"."+Main.server+".type", spleeftype.toString());
    return a;
}


public FFA getFFAArenaByArena(Arena arena) {
	// TODO Auto-generated method stub
	return null;
}


public static ItemStack[] gameitems;
public static ItemStack[] spectateitems;

public ItemStack[] gameitems() {
	if (gameitems==null) {
	ItemStack iron_shovel = new ItemStack(Material.IRON_SPADE);
	ItemMeta ironMeta = iron_shovel.getItemMeta();
	ironMeta.setUnbreakable(true);
	ironMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
	ironMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	iron_shovel.setItemMeta(ironMeta);
	
	ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SPADE);
	ItemMeta diamondMeta = diamond_shovel.getItemMeta();
	diamondMeta.setUnbreakable(true);
	diamondMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
	diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	diamond_shovel.setItemMeta(diamondMeta);
	
	ItemStack golden_shovel = new ItemStack(Material.GOLD_SPADE);
	ItemMeta goldenMeta = diamond_shovel.getItemMeta();
	goldenMeta.setUnbreakable(true);
	goldenMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
	goldenMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	golden_shovel.setItemMeta(goldenMeta);
	
	ItemStack x2snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(2).build();
	ItemStack x4snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(4).build();
	ItemStack x6snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(6).build();
	ItemStack x8snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(8).build();
	ItemStack x10snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(10).build();
	
	ItemStack redflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
	LeatherArmorMeta redmeta =(LeatherArmorMeta) redflag.getItemMeta();
	redmeta.setColor(Color.RED);
	redflag.setItemMeta(redmeta);
	ItemStack blueflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
	LeatherArmorMeta bluemeta =(LeatherArmorMeta) blueflag.getItemMeta();
	bluemeta.setColor(Color.BLUE);
	blueflag.setItemMeta(bluemeta);
	
	ItemStack pause = new ItemBuilder(Material.REDSTONE).setTitle("§c§lPause").build();
	ItemStack resume = new ItemBuilder(Material.GLOWSTONE_DUST).setTitle("§a§lResume").build();
	ItemStack[] items = {iron_shovel, diamond_shovel, x2snowball,x4snowball,x6snowball,x8snowball,x10snowball,blueflag,redflag,golden_shovel,pause,resume};
	gameitems = items;
	}

	return gameitems;
}

 
public ItemStack[] spectateitems() {
	if (spectateitems==null) {
		ItemStack showhide = new ItemBuilder(Material.WATCH).setTitle("§eShow/Hide Spectators").build();
		ItemStack[] items = {showhide};
		spectateitems = items;
	}
	
	return spectateitems;
}





public int calcualteELO(List<UUID> winner, List<UUID> loser, int difpuntos,SpleefType spleefType) {
	int p1 = 0;
	int p2 = 0;
	DataPlayer dp = DataPlayer.getPlayer();
	for (UUID sp : winner) p1 = p1 + dp.getELO(sp,spleefType);
	for (UUID sp : loser) p2 = p2 + dp.getELO(sp,spleefType);

	int a = winner.size();
	int b = winner.size();
	if (a==0) a = 1;
	if (b==0) b=1;
	
	p1 = p1/a;
	p2 = p2/b;
	
	int newELO = elo(p1,p2);	
	return newELO;
}

private int elo(int elo1, int elo2) {
	int k = 48;
	  double p1 = ((double) elo1/ (double) 400); 
	  double p1_ = Math.pow(10, p1); 

	  double p2 = ((double)elo2/(double)400); 
	  double p2_ = Math.pow(10, p2);

	  double d = p1_ + p2_;

	  double ex1 = (double)p1_/(double)d;   
	  double a1 = ((double)k*(1-(double)ex1));
	  return (int) a1;
}


public Arena getArenaByName(String ar) {
	for (Arena arena : this.arenas) {
		if (arena.getName().equalsIgnoreCase(ar)) {
			return arena;
		}
	}
	return null;
}


public Arena duelGame(Set<UUID> players,String ar,SpleefType type,int teamSize,boolean cantie,int time) {
	Arena arena = GameManager.getManager().getArenaByName(ar);
	if (arena!=null) {

		arena.getQueue().addAll(players);
		arena.setTeamSize(teamSize);
		arena.ranked(true);			
		Set<UUID> t1 = new HashSet<UUID>();
		Set<UUID> t2 = new HashSet<UUID>();
		
		int i = 1;
		for (UUID sp : arena.getQueue()) {
			DataManager.getManager().addToSet("ingame-players", sp.toString());
			GamePlayer gp = PlayerManager.getManager().getPlayer(sp);
			gp.setArena(arena);
			gp.setInGame(true);

			if (i<=teamSize) {
				t1.add(sp);
			} else {
				t2.add(sp);
			}
			i++;
		}
		for (UUID u : t1) DataManager.getManager().set(u.toString()+".spawn."+Main.server, Utils.getUtils().setLoc(arena.getSpawn1(),true));
		for (UUID u : t2) DataManager.getManager().set(u.toString()+".spawn."+Main.server, Utils.getUtils().setLoc(arena.getSpawn2(),true));
		
		RankedTeam team1 = new RankedTeam(t1);
		RankedTeam team2 = new RankedTeam(t2);
		
		arena.setRankedTeam1(team1);
		arena.setRankedTeam2(team2);
		
		arena.setTimedMaxTime(time);
		arena.setTie(cantie);
		FAWESplinduxAPI.getAPI().placeBlocks(arena.getArena1(), arena.getArena2(), Material.SNOW_BLOCK);
		new BukkitRunnable() {
			 public void run() {
					arena.startGameDuel(false);	
			 }
		 }.runTaskLater(Main.get(), 10L);
		
	} else {
		new BukkitRunnable() {
			public void run() {
				
				String msg = "";
		for (UUID sp_2 : players) {
			Bukkit.getPlayer(sp_2).sendMessage("§cCouldn't find a map to play! Duel cancelled.");
			msg = msg + sp_2.toString() + ",";
		}
			
			SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.SEND_TO_LOBBY,msg);
		}
		}.runTaskLater(Main.get(),3L);
		
		return null;
	}
	
	/*
	new BukkitRunnable() {
		public void run() {
	
	for (SpleefPlayer sp : arena.getPlayers()) {
	if (sp.getPlayer().isOp()) {
		TextComponent message = new TextComponent("§2Would you like to record this game? [CLICK HERE]");
		message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/hover record"));
		message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Record this game §a").create()));
			sp.getPlayer().spigot().sendMessage(message);
	}
	}
		}
		
	}.runTask(Main.get());
	
	arena.doRecordingRequest();
	new BukkitRunnable() {
		public void run() {
			arena.cancelRecordingRequest();
		}
	}.runTaskLater(Main.get(),20L*10);
	*/
	
	return arena;
}





public void endGameDuel(Arena arena,String w,GameEndReason reason) {	
	//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("EndGameDuel in primary thread");
	arena.setTie(false);
	arena.setTeamSize(0);
	arena.setShrinkAmount(0);
	arena.setCrumbleAmount(0);
	arena.resetMiniSpleefRound();
	arena.resetMiniSpleefTimer();
	arena.setDecayRound(0);
	arena.reset(false, true);
	arena.resetTotalTime();
	arena.setState(GameState.FINISHING);
	arena.getResetRequest().clear();
	arena.setShrinkedDuelArena1(arena.getArena1());
	arena.setShrinkedDuelArena2(arena.getArena2());
	arena.setShrinkedDuelSpawn1(arena.getSpawn1());
	arena.setShrinkedDuelSpawn2(arena.getSpawn2());	
	arena.setDuelArena1(arena.getArena1());
	arena.setDuelArena2(arena.getArena2());
	arena.setDuelSpawn1(arena.getSpawn1());
	arena.setDuelSpawn2(arena.getSpawn2());		
	arena.getDeadPlayers1().clear();
	arena.getDeadPlayers2().clear();		
	arena.getDuelTempDisconnectedPlayers1().clear();
	arena.getDuelTempDisconnectedPlayers2().clear();
	arena.resetPlayTo();
	arena.getPointsHistory().clear();
	arena.clean(arena.getExtremeArena1(), arena.getExtremeArena2());
	arena.setExtremeArena1(arena.getArena1());
	arena.setExtremeArena2(arena.getArena2());
	List<UUID> players = new ArrayList<UUID>();
	players.addAll(arena.getDuelPlayers1());
	players.addAll(arena.getDuelPlayers2());
	
	for (UUID sp : arena.getViewers()) {
		ActionBarAPI.sendActionBar(Bukkit.getPlayer(sp), "");
	}
			
	players.forEach((pp) -> {
		Player p = Bukkit.getPlayer(pp);
		p.setWalkSpeed(0.2F);		
	}); 		
	 

			
			
	List<UUID> spectators = new ArrayList<UUID>();
	spectators.addAll(arena.getSpectators());				
	spectators.forEach((p)-> SpectateManager.getManager().leaveSpectate(Bukkit.getPlayer(p), false));
	arena.getSpectators().clear();
	arena.resetTimer();
				
	List<UUID> winners = new ArrayList<UUID>();
	List<UUID> losers = new ArrayList<UUID>();
	int elo = 0;
	
	if (reason.equals(GameEndReason.WINNER)) {
	if (w.equalsIgnoreCase("Team1")) {
		winners.addAll(arena.getDuelPlayers1());
		elo = arena.getEloWinner1();
		losers.addAll(arena.getDuelPlayers2());
	} else {
		winners.addAll(arena.getDuelPlayers2());
		losers.addAll(arena.getDuelPlayers1());
		elo = arena.getEloWinner2();
		
	}


	
	
	}
	
	
	
	if (reason.equals(GameEndReason.LOG_OFF)) {
		if (w.equalsIgnoreCase("Team1")) {
			winners.addAll(arena.getDuelPlayers1());	
			elo = arena.getEloWinner1();
		} else {
			winners.addAll(arena.getDuelPlayers2());
			elo = arena.getEloWinner2();
		}
		
		losers.add(arena.getDisconnectedPlayer());
		
		}
		
	

	
	if (arena.isRanked()) {
		if (!reason.equals(GameEndReason.ENDGAME)) {
			try {
		elo = GameManager.getManager().calcualteELO(winners, losers, arena.getTeamSize(),arena.getSpleefType());
		if (arena.getRankedTeam1().getPlayers().contains(winners.get(0))) {
		arena.getRankedTeam1().newELO(elo,arena.getSpleefType());
		arena.getRankedTeam2().newELO(-elo,arena.getSpleefType());
		} else {
			arena.getRankedTeam1().newELO(-elo, arena.getSpleefType());
			arena.getRankedTeam2().newELO(elo,arena.getSpleefType());
		}
			} catch(Exception ex) {}
	}
	}
	
	if (reason.equals(GameEndReason.WINNER)) {	
		for (UUID loser : losers) LevelManager.getManager().addLevel(loser, 1);
		for (UUID winner : winners) { 
			LevelManager.getManager().addLevel(winner, 3);
			PlayerManager.getManager().getPlayer(winner).addCoins(20, true,false);
			//SWSManager.getManager().addPoints(winner, 20+(arena.getTeamSize()*4), true,true);
		}
	
		if (arena.getDuelPlayers1().size()==1 && arena.getDuelPlayers2().size()==1) for (UUID winner : winners) 
			dp.setDuelWins(winner, arena.getSpleefType(), dp.getDuelWins(winner, arena.getSpleefType())+1);
	
	
	String p1 = arena.getTeamName(1);
	String p2 = arena.getTeamName(2);
			if (winners.equals(arena.getDuelPlayers1())) {					
					endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(arena.getSpleefType()),arena.getRankedTeam2().getELO(arena.getSpleefType()),false, arena);										
		} else {					
				endGameMessage(p2,p1,arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(arena.getSpleefType()),arena.getRankedTeam1().getELO(arena.getSpleefType()),false,arena);
		}
		
	
	} else if (reason.equals(GameEndReason.LOG_OFF)) {
		
				String p1 = arena.getTeamName(1);
				String p2 = arena.getTeamName(2);						
				if (w.equalsIgnoreCase("Team1")) {								
					endGameMessage(p1,Bukkit.getOfflinePlayer(arena.getDisconnectedPlayer()).getName(),arena.getPoints1(),arena.getPoints2(),true,elo,arena.getRankedTeam1().getELO(arena.getSpleefType()),arena.getRankedTeam2().getELO(arena.getSpleefType()),false,arena);										
			} else {					
					endGameMessage(p2,Bukkit.getOfflinePlayer(arena.getDisconnectedPlayer()).getName(),arena.getPoints2(),arena.getPoints1(),true,elo,arena.getRankedTeam2().getELO(arena.getSpleefType()),arena.getRankedTeam1().getELO(arena.getSpleefType()),false,arena);
			}
			
	
	} else if (reason.equals(GameEndReason.ENDGAME)) {
		

	} else if (reason.equals(GameEndReason.TIE)) {
		String p1 = arena.getTeamName(1);
		String p2 = arena.getTeamName(2);	
		endGameMessage(p1,p2,arena.getPoints1(),arena.getPoints2(),false,0,0,0,true,arena);
	}	
	
	Set<UUID> toKick = new HashSet<UUID>();
	toKick.addAll(arena.getViewers());
	
	
	new BukkitRunnable() {
		String sendToLobby  =  "";
		public void run() {
	for (UUID u : toKick) {
		if (Bukkit.getOfflinePlayer(u).isOnline()) Bukkit.getPlayer(u).kickPlayer("End game");
		GamePlayer gp = PlayerManager.getManager().getPlayer(u);
		gp.setArena(null);
		gp.setInGame(false);
		gp.setSpectating(false);
		sendToLobby =  sendToLobby.equalsIgnoreCase("") ? u.toString() : sendToLobby +","+u.toString();
	}
		}
	}.runTaskLater(Main.get(), 5L);
	
	//SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.SEND_TO_LOBBY, sendToLobby);
	
	arena.getDuelPlayers1().clear();
	arena.getDuelPlayers2().clear();
	arena.setDisconnectedPlayer(null);
	arena.setState(GameState.LOBBY);			
	
	
	arena.resetRecord();
	arena.setPoints1(0);
	arena.setPoints2(0);
	arena.setRankedTeam1(null);
	arena.setRankedTeam2(null);
	
}


public void endGameMessage(String winner, String loser, int winner_points, int loser_points, boolean ranked,int elodif, int elo1, int elo2, boolean tie,Arena arena) {
	 String prefix = getGamePrefix(arena);
	 String string_elo1 = "§7(§e"+elo1+ "§7)";
	 String string_elo2 = "§7(§e"+elo2+ "§7)";
	 String string_pos_elodif = "§a(+"+elodif+"§a)";
	 String string_neg_elodif = "§c(-"+elodif+"§c)";
	 String what_happened = "§b won against ";
	 
	 
	 	if (tie) what_happened= "§b tied against ";
		if (!ranked) {prefix = getGamePrefix(arena); string_elo1 = ""; string_elo2 = ""; string_pos_elodif = "";string_neg_elodif = "";}
		
			String msg = prefix +"§b" +winner +string_elo1 + string_pos_elodif+ what_happened
					+ loser + string_elo2 + string_neg_elodif+ " §7(§e§l" + winner_points + "§7-§e§l" + loser_points+"§7)";		
			
			new BukkitRunnable() {
				public void run() {
					SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.BROADCAST, msg);
					SplinduxDataAPI.getAPI().createIntegrationBukkit("lobby2",IntegrationBukkitType.DISCORD_BROADCAST, msg);
				}
			}.runTaskLaterAsynchronously(Main.get(),20L);
			
			
}

public String getGamePrefix(Arena arena) {
	
	String preprefix = null;
	switch(arena.getSpleefType()) {
	default:break;
	case SPLEEF: preprefix = "Spleef"; break;
	case TNTRUN: preprefix = "TntRun"; break;
	case SPLEGG: preprefix = "Splegg"; break;
	}
	
	
		if (arena.getGameType().equals(GameType.DUEL)) {
			return "§2["+ preprefix +" Duels] ";
		} else {
			return "§2["+ preprefix +" FFA] ";
		}
}




public void playToWithCommand(Arena g,int crumble) {
	g.setPlayTo(crumble);
	g.setPlayToRequest(null);
	
	for (UUID p2 : g.getViewers()) {
			Bukkit.getPlayer(p2).sendMessage("§6The arena playto has been set to: §6" + crumble);
	}
}

public void crumbleWithCommand(Arena g,int crumble) {
	g.crumbleArena(crumble);
	g.setCrumbleRequest(null);	
	
	new BukkitRunnable() {
		public void run() {
	for (UUID p2 : g.getViewers()) {
			Bukkit.getPlayer(p2).sendMessage("§6The arena has been crumbled with a percentage: §a" + crumble);
	}			
	}
}.runTask(Main.get());
}


public void pauseWithCommand(Arena arena) {
	arena.pause();
	
}

public void redoWithCommand(Arena arena) {
	arena.redo();
	
}

public void unpauseWithCommand(Arena arena) {
	arena.unpause();
	
}



public void resetArenaWithCommand(Arena arena) {
	new BukkitRunnable() {
		public void run() {
		if (!arena.getState().equals(GameState.GAME)) return;
	
	new BukkitRunnable() {
		public void run() {
	for (UUID players : arena.getViewers()) {
		if (Bukkit.getOfflinePlayer(players).isOnline()) {
		Bukkit.getPlayer(players).sendMessage("§6The arena has been reset.");						
}
	}
	}
	}.runTask(Main.get());
	
	arena.resetArena();
	arena.setState(GameState.STARTING);		
	
		}
}.runTaskAsynchronously(Main.get());
}


public LinkedHashMap<DeathReason,UUID> getDeathReason(UUID sp) {
	LinkedHashMap<DeathReason,UUID> hashmap = new LinkedHashMap <DeathReason,UUID>();
	Arena arena = PlayerManager.getManager().getPlayer(sp).getArena();
		BrokenBlock broken = getNearestBlockIn(sp,arena);
		if (broken!=null) {
				if (broken.getReason().equals(BreakReason.SHOVEL)) {
			hashmap.put(DeathReason.SPLEEFED, broken.getPlayer());
			return hashmap;
			} else {
				hashmap.put(DeathReason.SNOWBALLED, broken.getPlayer());
				return hashmap;
			}
			
		} else {	
			hashmap.put(DeathReason.THEMSELF, null);
		}

	return hashmap;
}




 public BrokenBlock getNearestBlockIn(UUID sp,Arena arena) {
	 BrokenBlock block = null;
	 FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
	List<BrokenBlock> list= new ArrayList<BrokenBlock>();
	list.addAll(ffa.getBrokenBlocks());
	for (BrokenBlock broken : list) {
		 if (broken==null) continue;
		 if (broken.getPlayer().equals(sp)) continue;
		 if (!broken.isAlive()) continue;
		 
		 if (!Bukkit.getOfflinePlayer(broken.getPlayer()).isOnline()) continue;
		 
		 if (!Bukkit.getPlayer(broken.getPlayer()).getWorld().getName().equalsIgnoreCase(Bukkit.getPlayer(sp).getWorld().getName())) continue;
		 
			 Location deathPlayerLocation = Bukkit.getPlayer(sp).getLocation();
			 Location killerPlayerLocation =  Bukkit.getPlayer(broken.getPlayer()).getLocation();
			 Location blockLocation = broken.getLocation();
			 
				 double playerReach = 0;
				 if (broken.getReason().equals(BreakReason.SNOWBALL)) {
					 playerReach = 2000;
				 } else {
				 switch (arena.getSpleefType()) {
				case BOWSPLEEF:
					break;
				case POTSPLEEF:
					break;
				case SPLEEF:
					playerReach = 15; break;
				case SPLEGG:
					playerReach = 50; break;
				case TNTRUN:
					playerReach = 8;break;
				default:
					break;
				 } 
				 }
				 
				 if  (blockLocation.distanceSquared(killerPlayerLocation)<=playerReach*playerReach) {
					 if (block==null) {
						 block = broken;
					 } else {
						 if (blockLocation.distanceSquared(deathPlayerLocation) < block.getLocation().distanceSquared(deathPlayerLocation)) {
							 block = broken;
						 }
					 }							 
				 }	 
			 
	 }

	 return block;
 }

	public void fell(Player p,LinkedHashMap <DeathReason,UUID> r,Arena arena) {	
		//if (Bukkit.isPrimaryThread()) Bukkit.getLogger().info("Fell in primary thread");
		if (!arena.getState().equals(GameState.GAME)) return;			

		if (arena.getGameType().equals(GameType.FFA)) {
			/*FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
			removeBrokenBlocksAtDead(sp,ffa);
			sp.setScoreboard(ScoreboardType.FFAGAME_LOBBY);
			ffa.getPlayers().remove(sp);
			if (ffa.isInEvent()) {		
				if (Main.ffa2v2) ffa.getTeamByPlayer(sp.getUUID()).killPlayer(sp.getUUID());
				for (SpleefPlayer players : ffa.getPlayers()) {
					if (Main.ffa2v2) {
						ffa.getEvent().addPoint2v2(players.getUUID(), 1,false,false);
						} else {
							ffa.getEvent().addPoint(players.getUUID(), 1,false);
						}
				}
				
				}
			SpectateManager.getManager().spectateSpleef(sp, arena);
			
			DeathReason reason = (new ArrayList<DeathReason>(r.keySet())).get(0);	
			SpleefPlayer killer = (new ArrayList<SpleefPlayer>(r.values())).get(0);	
			
			 if (killer!=null) {
			    	killer.getPlayer().playSound(killer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.75F);					 
			    	if (!killer.equals(sp)) {
			    		killer.addFFAKill(arena.getSpleefType());
			    		if (ffa.isInEvent() && !arena.getSpleefType().equals(SpleefType.TNTRUN)) {
			    			if (Main.ffa2v2) {
			    				if (!ffa.getTeamByPlayer(killer.getUUID()).getUUID().equals(ffa.getTeamByPlayer(sp.getUUID()).getUUID())) {
			    				ffa.getEvent().addPoint2v2(killer.getUUID(), 5,false,true);
			    				}
			    			} else {
			    				ffa.getEvent().addPoint(killer.getUUID(), 5,false);
			    			}
			    			
			    			try {
			    				FFAEvent event = ffa.getEvent();
			    				UUID lastWinner = event.getLastWinner();
			    				if (Main.ffa2v2) {
			    					FFATeam team = ffa.getTeam(sp.getUUID());
			    					if (team.getPlayer1().equals(lastWinner) || team.getPlayer2().equals(lastWinner))
			    						event.addPoint2v2(killer.getUUID(), 12, false,true);
			    				} else {
			    			if (lastWinner.equals(sp.getOfflinePlayer().getUniqueId())) event.addPoint(killer.getUUID(), 12, false);
			    				}
			    			} catch(Exception ex) {}
			    		}
			    	}
			    }
			 if (Main.ffa2v2) {
				 FFATeam team = ffa.getTeamByPlayer(sp.getUUID());
				 team.killPlayer(sp.getUUID());
			 }
			 	
			 boolean condition = Main.ffa2v2 ? ffa.getTeamsAlive().size()<=1 : ffa.getPlayers().size()<=1;
			 if (condition) {		 
					ffa.endGame(GameEndReason.WINNER);
				}
			 
				  Player p = sp.getPlayer();
	p.playSound(arena.getLobby(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.9F);
		sp.giveQueueItems(false,arena.getSpleefType().equals(SpleefType.SPLEEF),true);
		if (sp.getHelmet()!=null) {
			p.getInventory().setHelmet(sp.getHelmet().getItem(sp, true));
			}
		if (arena.getPlayers().size()>0)
		for (SpleefPlayer players : arena.getViewers()) {
			players.getPlayer().sendMessage(getGamePrefix(arena)+reason.getDeathMessage(killer, sp, arena));
		}
*/
		} else if (arena.getGameType().equals(GameType.DUEL)) {
			
			boolean point = true;
			boolean reset = false;				
		
			arena.getResetRequest().remove(p.getUniqueId());
			
			Set<UUID> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
		if (resetRequest.size()<=1) {
				if (arena.getDuelPlayers1().size()<1 && arena.getDuelPlayers2().size()<1) {
					point = true;
				} else {									
					if (arena.getTeamSize()>1) SpectateManager.getManager().spectateSpleef(p, arena);		
					reset = true;
		
					GameManager.getManager().resetArenaWithCommand(arena);
					new BukkitRunnable() {
						public void run() {
					for (UUID players : arena.getViewers()) { 
						Bukkit.getPlayer(players).sendMessage("§6"+ p.getName()+ "§b fell!");							
						}	
					}
				}.runTask(Main.get());
				
				
			}
			}
			
			

			
			if (arena.getCrumbleRequest()!=null) {
				ArenaRequest request = arena.getCrumbleRequest();
				if (request.getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
					GameManager.getManager().crumbleWithCommand(arena, request.getAmount());
				} 
			}
						
		
				UUID sp = p.getUniqueId();
			if (arena.getDuelPlayers1().contains(p.getUniqueId()) && !arena.getDeadPlayers1().contains(sp)) {
				arena.getDeadPlayers1().add(sp);
				if (arena.getDeadPlayers1().size()<arena.getDuelPlayers1().size()) {
				point =false;
				}
			} else if (arena.getDuelPlayers2().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
				arena.getDeadPlayers2().add(sp);
				if (arena.getDeadPlayers2().size()<arena.getDuelPlayers2().size()) {
					point = false;
				}
			} 
			if (arena.getState().equals(GameState.GAME)) {
				
	
			if (point) {
						arena.point(sp);				 
			} else if (!reset){			
				

				for (UUID players : arena.getViewers()) { 
					Bukkit.getPlayer(players).sendMessage("§6"+ p.getName()+ "§b fell!");							
					}				
				SpectateManager.getManager().spectateSpleef(p, arena);
						
			} else {
				arena.reset(false, true);
			}
			}
		
		}
	}

	public Set<UUID> leftPlayersToSomething(Set<UUID> something,Arena arena,boolean canBeDead) {
		Set<UUID> list = new HashSet<UUID>();
		for (UUID s : arena.getPlayers()) {
			if (!canBeDead) {
			if (arena.getDeadPlayers1().contains(s) || arena.getDeadPlayers2().contains(s)) {
			} else {
			if (!something.contains(s)) {
				list.add(s);
			}
			}
			} else {
				if (!something.contains(s)) {
					list.add(s);
				}
			}
		}
		return list;
	}


	public FFA getFFAArena(SpleefType spleefType) {
		// TODO Auto-generated method stub
		return null;
	}








 	
}
