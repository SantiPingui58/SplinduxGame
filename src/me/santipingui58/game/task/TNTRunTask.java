package me.santipingui58.game.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.GamePlayer;
import me.santipingui58.game.Main;
import me.santipingui58.game.PlayerManager;
import me.santipingui58.game.game.Arena;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.game.GameState;
import me.santipingui58.game.game.ffa.FFA;

public class TNTRunTask {

	private List<Block> waiting = new ArrayList<>();
	private List<Location> dLocs = new ArrayList<>();
	
	
	
	public TNTRunTask() {
		task();
	}
	
	private void task() {
		new BukkitRunnable() {
			public void run() {
				for (Arena arena : GameManager.getManager().getArenas()) {
					if (!arena.getState().equals(GameState.GAME)) continue;
					FFA ffa = GameManager.getManager().getFFAArenaByArena(arena);
					if (arena.getSpleefType().equals(SpleefType.TNTRUN)  || (ffa!=null && ffa.hasSnowRun())) {
					
						Set<UUID> set = new HashSet<UUID>();
						set.addAll(arena.getPlayers());
					for (UUID sp : set) {
						Player p = Bukkit.getPlayer(sp);
						GamePlayer gp = PlayerManager.getManager().getPlayer(p);
						if (p==null) continue;
				 if ((p.getGameMode().equals(GameMode.SPECTATOR)) || (p.getGameMode().equals(GameMode.CREATIVE))) {
					 continue;
				      }
					if (gp.isSpectating()) continue;
					if (gp.isInGame()) {
						boolean condition = (gp.getArena().getSpleefType().equals(SpleefType.TNTRUN) && gp.getArena().getState().equals(GameState.GAME)) || ffa.hasSnowRun();
						if (!condition) continue;
					
				 int y = p.getLocation().getBlockY() + 1;
		         Block block = null;
		         for (int i = 0; i <= 2; i++) {
		             block = getBlockUnderPlayer(y, p.getLocation());
		             y--;
		             if (block != null) {
		                 break;
		             }
		         }
		         if (block != null) {
		             final Block b = block;
		            if (!waiting.contains(b)) {
		                 waiting.add(b);
		                 new BukkitRunnable() {
		                     public void run() {
		                         waiting.remove(b);
		                         remove(b);
		                     }
		                 }.runTaskLater(Main.get(), 6L);
				 
		             }
		         }
					}
					}
				}
		}
			}
		}.runTaskTimerAsynchronously(Main.get(), 20L, 1L);
		
	}
	
	
	public void remove(Block b) {

        if (b.getType() != Material.AIR) {
            dLocs.add(b.getLocation());
        }
        changeBlock(b, Material.AIR);
        Block down = b.getRelative(BlockFace.DOWN);
        if (down.getType() != Material.AIR) {
            dLocs.add(down.getLocation());
        }
        changeBlock(down, Material.AIR);
    }
	
	public void changeBlock(Block block, Material material) {
        CraftBlock block1 = ((CraftBlock)block);
        //Material[] allowedMaterials = {Material.TNT,Material.GRAVEL,Material.SAND,Material.AIR};
      //  List<Material> list = Arrays.asList(allowedMaterials);
       // if (list.contains(block.getType())) {
        block1.setType(material); 
      //  }
        
    }

 private double ADD = 0.3;
private Block getBlockUnderPlayer(int y, Location location) {
    Pos loc = new Pos(location.getX(), y, location.getZ());
    Block b11 = loc.getBlock(location.getWorld(), +ADD, -ADD);
    if (b11.getType() != Material.AIR) {
        return b11;
    }
    Block b12 = loc.getBlock(location.getWorld(), -ADD, +ADD);
    if (b12.getType() != Material.AIR) {
        return b12;
    }
    Block b21 = loc.getBlock(location.getWorld(), +ADD, +ADD);
    if (b21.getType() != Material.AIR) {
        return b21;
    }
    Block b22 = loc.getBlock(location.getWorld(), -ADD, -ADD);
    if (b22.getType() != Material.AIR) {
        return b22;
    }
    return null;
}



}

class Pos {

private double x;
private int y;
private double z;

public Pos(double x, int y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
}

public Block getBlock(World world, double ax, double az) {
    return world.getBlockAt(NumberConversions.floor(x + ax), y, NumberConversions.floor(z + az));
}
}

