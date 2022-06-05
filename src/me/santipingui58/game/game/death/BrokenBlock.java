package me.santipingui58.game.game.death;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.game.Main;


public class BrokenBlock {


	private UUID sp;
	private Location location;
	private BreakReason reason;
	private boolean alive;
	public BrokenBlock(UUID sp,Location l,BreakReason r) {
		this.location = l;
		this.sp = sp;
		this.reason = r;
		this.alive = true;
		new BukkitRunnable() {

			@Override
			public void run() {
				alive = false;
			}
			
		}.runTaskLater(Main.get(), 20L*10);
	}
	
	
	public boolean isAlive() {
		return this.alive;
	}
	public BreakReason getReason() {
		return this.reason;
	}
	
	public UUID getPlayer() {
		return this.sp;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	
	
}
