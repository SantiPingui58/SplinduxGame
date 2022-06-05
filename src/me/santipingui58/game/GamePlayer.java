package me.santipingui58.game;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.data.SplinduxDataAPI;
import me.santipingui58.data.integration.IntegrationBungeeType;
import me.santipingui58.data.player.DataPlayer;
import me.santipingui58.data.spleef.GameType;
import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameEndReason;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.game.ffa.FFA;
import me.santipingui58.game.game.ffa.FFATeam;
import me.santipingui58.game.game.ffa.GameMutation;
import me.santipingui58.game.utils.ActionBarAPI;
import me.santipingui58.game.utils.ItemBuilder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class GamePlayer {

	
	private UUID uuid;
	private boolean isInGame;
	private boolean isSpectating;
	private Location location;
	private boolean isHidingSpectators;
	private Arena arena;
	private boolean chat;
	private String prefix;
	public GamePlayer(UUID uuid) {
		this.uuid = uuid;
		this.chat = true;
	}

	

	public boolean getChat() {
		return this.chat;
	}
	
	public void setChat(boolean b) {
		this.chat  =b;
	}
	
	
	public Arena getArena() {
		return this.arena;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	
	public void setLocation(Location l) {
		this.location = l;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public boolean isInGame() {
		return this.isInGame;
	}
	
	public void setInGame(boolean b) {
		this.isInGame = b;
	}
	
	public boolean isSpectating() {
		return this.isSpectating;
	}
	
	public void setSpectating(boolean b) {
		this.isSpectating = b;
	}
	
	public boolean isHidingSpectators() {
		return this.isHidingSpectators;
	}
	
	public void setHidingSpectators(boolean b) {
	this.isHidingSpectators = b;
	}

	 


		public void giveGameItems() {
			UUID u = this.uuid;
			new BukkitRunnable() {
				public void run() {
			if (!Bukkit.getOfflinePlayer(u).isOnline()) return;
			Player p = Bukkit.getPlayer(u);
			for(PotionEffect effect : p.getActivePotionEffects())	{
			   p.removePotionEffect(effect.getType());
			}
			if (DataPlayer.getPlayer().hasNightVision(u)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1));
			}
			
			clearGameInventory();
			if (arena==null) return;
			if (DataPlayer.getPlayer().getHelmet(u)!=null) {
			//p.getInventory().setHelmet(HelmetManager.getManager().getHelmetByName(DataPlayer.getPlayer().getHelmet(u)).getItem(u, true));
			}
			
				if (Main.ffa2v2 && arena.getGameType().equals(GameType.FFA)) {
					FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
					FFATeam team = ffa.getTeamByPlayer(u);	
					ItemStack blueflag = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
					LeatherArmorMeta bluemeta =(LeatherArmorMeta) blueflag.getItemMeta();
					bluemeta.setColor(Color.fromRGB(team.getRGB()[0], team.getRGB()[1], team.getRGB()[2]));
					blueflag.setItemMeta(bluemeta);
					p.getInventory().setChestplate(blueflag);
				}
			
			
			 if (arena.getGameType().equals(GameType.DUEL)) {

					p.getInventory().setItem(7, GameManager.getManager().gameitems()[10]);
					if (arena.getDuelPlayers1().size()>=2 && arena.getDuelPlayers2().size()>=2) {
				if (arena.getDuelPlayers1().contains(u)) {
					p.getInventory().setChestplate(GameManager.getManager().gameitems()[7]);
					p.getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 4));
				} else if (arena.getDuelPlayers2().contains(u)) {
					p.getInventory().setChestplate(GameManager.getManager().gameitems()[8]);
					p.getInventory().setItem(8, new ItemStack(Material.INK_SACK,1,(byte) 1));
				
				}
				}
			 }
				}

				
			 }.runTask(Main.get());
		}
	 
		
		

		public void clearGameInventory() {
			if (!Bukkit.getOfflinePlayer(uuid).isOnline()) return;
			Player player = Bukkit.getPlayer(uuid);
			ItemStack[] armorContents = player.getInventory().getArmorContents().clone(); //Clone instance of ItemStack[]
			player.getInventory().clear(); //Clear inventory
			player.getInventory().setArmorContents(armorContents); //Set armor using the clone instance.
			player.updateInventory(); //Update... but its not necesary all time.
					
		}

		public void giveShovel() {
			new BukkitRunnable() {
				public void run() {			
			if (!Bukkit.getOfflinePlayer(uuid).isOnline()) return;
			Player p = Bukkit.getPlayer(uuid);
			ItemStack[] gameitems = GameManager.getManager().gameitems();
			if (arena.getSpleefType().equals(SpleefType.SPLEEF)) {
			if (p.hasPermission("splindux.diamondshovel")) {
				p.getInventory().setItem(0, gameitems[1]);
			} else {
				p.getInventory().setItem(0, gameitems[0]);
			}
			} else if (arena.getSpleefType().equals(SpleefType.SPLEGG)) {
				p.getInventory().setItem(0, gameitems[9]);
			} else if (arena.getSpleefType().equals(SpleefType.TNTRUN)) return;
			
			if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
				FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
				if (!ffa.isInEvent()) {
			if (p.hasPermission("splindux.x10snowballs")) {
				p.getInventory().setItem(1, gameitems[6]);
			} else if (p.hasPermission("splindux.x8snowballs")) {
			p.getInventory().setItem(1,gameitems[5]);
			}else if (p.hasPermission("splindux.x6snowballs")) {
				p.getInventory().setItem(1, gameitems[4]);
			}else if (p.hasPermission("splindux.x4snowballs")) {
				p.getInventory().setItem(1, gameitems[3]);
			} else {
				p.getInventory().setItem(1,gameitems[2]);
			}
				} else {
					p.getInventory().setItem(1,gameitems[2]);
				}
				
				for (GameMutation mutation : ffa.getInGameMutations()) mutation.giveMutationItems(uuid);
			}
			
			if (arena.getGameType().equals(GameType.DUEL)) {
				p.getInventory().setItem(7, gameitems[10]);
			}
				}
			}.runTask(Main.get());
		}
		
		
		
		public Player getPlayer() {
			return Bukkit.getPlayer(uuid);
		}

		public OfflinePlayer getOfflinePlayer() {
			return Bukkit.getOfflinePlayer(uuid);
		}
		
		public void stopFly() {
			Player p = getPlayer();
			new BukkitRunnable() {
				public void run() {
					p.setAllowFlight(false);
			p.setFlying(false);
			}
			}.runTask(Main.get());
			
		}

	 
		
		  public void addCoins(int i,boolean multiplier,boolean found) {
		  Player p = getPlayer();
				if (multiplier) {
				if (p.hasPermission("splindux.extreme")) {
					i = (int) ((i*1.5));
				} else if (p.hasPermission("splindux.epic")) {
					i = (int) ((i*1.25));
				} else if (p.hasPermission("splindux.vip")) {
					i = (int) ((i*1.1));
				} 
				}
				
				DataPlayer.getPlayer().setCoins(uuid, DataPlayer.getPlayer().getCoins(uuid)+i);
				
				if (getOfflinePlayer().isOnline()) {
					if (found) {
						p.sendMessage("§eYou have found §6§l" + i + "&ecoins!");				
				} else {
					p.sendMessage("§aYou have won §6"+i+" coins");
				}
				}
		  }

			public void giveSpectateItems() {
				Player p = getPlayer();
				p.getInventory().clear();
				p.getInventory().setItem(8, GameManager.getManager().spectateitems()[0]);
			}



			public void sendMessage(String string) {
				if (getOfflinePlayer().isOnline())getPlayer().sendMessage(string);
				
			}



			public String getName() {
				return getOfflinePlayer().getName();
			}



			public void leave() {
			Player p = getPlayer();
			UUID player = p.getUniqueId();
			p.setWalkSpeed(0.2F);
			p.setGameMode(GameMode.ADVENTURE);
			p.eject();
		 
			ActionBarAPI.sendActionBar(getPlayer(), "");
			if (getArena()!=null) {
				Arena arena = getArena();
				FFA ffa = null;
				if (arena.getGameType().equals(GameType.FFA)) {
					ffa = GameManager.getManager().getFFAArena(arena.getSpleefType());
				}
			if (arena.getPlayers().contains(player) || (ffa!=null && ffa.getQueue().contains(player))) {
				if (!arena.getResetRequest().isEmpty()) {
				Set<UUID> resetRequest = GameManager.getManager().leftPlayersToSomething(arena.getResetRequest(), arena,false);
				if (resetRequest.contains(player) && arena.getPlayers().size()>=1) {
					if (arena.getResetRequest().size()<1) {
						GameManager.getManager().resetArenaWithCommand(arena);
					} else {
						arena.getResetRequest().remove(player);
					}
				}
				}
				if (!arena.getEndGameRequest().isEmpty()) {
				Set<UUID> endGameRequest = GameManager.getManager().leftPlayersToSomething(arena.getEndGameRequest(), arena,true);
				if (endGameRequest.contains(player)) {
					if (arena.getEndGameRequest().size()<=1) {
						GameManager.getManager().endGameDuel(arena, null,GameEndReason.ENDGAME);
					} else {
						arena.getEndGameRequest().remove(player);
					}
				}
				}
				
				
				
				if (arena.getGameType().equals(GameType.DUEL)) {
					
					if (arena.getDuelPlayers1().contains(player)) {
						if (arena.getDuelPlayers1().size()<=1) {
							arena.getDeadPlayers1().remove(player);
							arena.getDeadPlayers2().remove(player);
							arena.getDuelPlayers1().remove(player);
							arena.getDuelPlayers2().remove(player);
							arena.setDisconnectedPlayer(player);
							GameManager.getManager().endGameDuel(arena,"Team2",GameEndReason.LOG_OFF);
						}
						
						Set<UUID> alive = new HashSet<UUID>();
						for (UUID s : arena.getDuelPlayers1()) {
							if (!arena.getDeadPlayers1().contains(s)) {
								if (Bukkit.getOfflinePlayer(s).isOnline()) alive.add(s);
							}
						}
						
						if (alive.contains(player)) {
							if (alive.size()<=1)  {
								arena.point(player);
							}
						}
						
						
					} else if (arena.getDuelPlayers2().contains(player)) {
						if (arena.getDuelPlayers2().size()<=1) {
							arena.getDeadPlayers1().remove(player);
							arena.getDeadPlayers2().remove(player);
							arena.getDuelPlayers1().remove(player);
							arena.getDuelPlayers2().remove(player);
							arena.setDisconnectedPlayer(player);
							GameManager.getManager().endGameDuel(arena,"Team1",GameEndReason.LOG_OFF);
						}	
							Set<UUID> alive = new HashSet<UUID>();
							for (UUID s : arena.getDuelPlayers2()) {
								if (!arena.getDeadPlayers2().contains(s)) {
									alive.add(s);
								}
							}					
							if (alive.contains(player)) {
							if (alive.size()<=1)  {
								arena.point(player);
							
							}
							} 
					
					}
					arena.getDeadPlayers1().remove(player);
					arena.getDeadPlayers2().remove(player);
					arena.getDuelPlayers1().remove(player);
					arena.getDuelPlayers2().remove(player);
					
					if (arena.getPlayToRequest()!=null) {
						
						if (arena.getPlayToRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1) {
							GameManager.getManager().playToWithCommand(arena, arena.getPlayToRequest().getAmount());
						}		
				}
					
				if (arena.getCrumbleRequest()!=null) {
					if (arena.getCrumbleRequest().getAcceptedPlayers().size()+1>=arena.getPlayers().size()-1-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
						GameManager.getManager().crumbleWithCommand(arena, arena.getCrumbleRequest().getAmount());
					}		
			}
				
				} else if (arena.getGameType().equals(GameType.FFA)) {
					if (Main.ffa2v2) {
						FFATeam team = ffa.getTeamByPlayer(getUUID());
						team.killPlayer(getUUID());
					}

					Set<UUID> players = new HashSet<UUID>();
					ffa.getPlayers().remove(player);		
					ffa.getQueue().remove(player);
					players.addAll(ffa.getPlayers());
					
					
					boolean condition = Main.ffa2v2 ? ffa.getTeamsAlive().size()<=1 : players.size()<=1;
					
					if (condition) {
						if (arena.getGameType().equals(GameType.FFA) && (arena.getState().equals(GameState.STARTING)|| arena.getState().equals(GameState.GAME)) ) {
							ffa.endGame(GameEndReason.WINNER);
						}					
						
					}
				}
			}
			}
			if (arena!=null) arena.getSpectators().remove(p.getUniqueId());
				setSpectating(false);
				setInGame(false);
				SplinduxDataAPI.getAPI().createIntegrationBungee(IntegrationBungeeType.SEND_TO_LOBBY, uuid.toString());
			
			}



			public String getPrefix() {
				if (this.prefix==null) {
					RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
					if (provider != null) {
					    LuckPerms api = provider.getProvider();
						 User user = api.getUserManager().getUser(getUUID());
						 CachedMetaData metaData = user.getCachedData().getMetaData();
						 String prefix = metaData.getPrefix();
						 
						 if (prefix!=null) {
							 return ChatColor.translateAlternateColorCodes('&', prefix);
						 }
						 return "";
					}
				}
				
				return this.prefix;
			}

}
