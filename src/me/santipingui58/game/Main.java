package me.santipingui58.game;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.santipingui58.data.spleef.SpleefType;
import me.santipingui58.game.commands.AdminCommand;
import me.santipingui58.game.commands.ChatCommand;
import me.santipingui58.game.commands.CrumbleCommand;
import me.santipingui58.game.commands.ForceDecayCommand;
import me.santipingui58.game.commands.ForceFinishCommand;
import me.santipingui58.game.commands.ForceGameTimeCommand;
import me.santipingui58.game.commands.ForcePauseCommand;
import me.santipingui58.game.commands.ForcePlaytoCommand;
import me.santipingui58.game.commands.ForceRedoCommand;
import me.santipingui58.game.commands.ForceResetCommand;
import me.santipingui58.game.commands.ForceResumeCommand;
import me.santipingui58.game.commands.ForceScoreCommand;
import me.santipingui58.game.commands.HoverCommand;
import me.santipingui58.game.commands.MatchesCommand;
import me.santipingui58.game.commands.NightVisionCommand;
import me.santipingui58.game.commands.OptionsCommand;
import me.santipingui58.game.commands.PauseCommand;
import me.santipingui58.game.commands.PingCommand;
import me.santipingui58.game.commands.PlaytoCommand;
import me.santipingui58.game.commands.RedoCommand;
import me.santipingui58.game.commands.ResetCommand;
import me.santipingui58.game.commands.ResumeCommand;
import me.santipingui58.game.commands.SpawnCommand;
import me.santipingui58.game.commands.TranslateCommand;
import me.santipingui58.game.game.GameManager;
import me.santipingui58.game.helmet.HelmetManager;
import me.santipingui58.game.listener.ChatListener;
import me.santipingui58.game.listener.PlayerConnectionListener;
import me.santipingui58.game.listener.PlayerListener;
import me.santipingui58.game.listener.ServerListener;
import me.santipingui58.game.task.ActionBarTask;
import me.santipingui58.game.task.ArenaTask;
import me.santipingui58.game.task.HighMoveTask;
import me.santipingui58.game.task.IntegrationTask;
import me.santipingui58.game.task.LowMoveTask;
import me.santipingui58.game.task.MinuteTask;
import me.santipingui58.game.task.ScoreboardTask;
import me.santipingui58.game.task.StressingTask;
import me.santipingui58.game.task.TNTRunTask;
import me.santipingui58.game.utils.Configuration;

public class Main extends JavaPlugin {

	private static Plugin pl;
	
	public static String server;
	
	public static Configuration config,arenas,helmets;
	
	public static boolean isOpen;
	
	public static boolean ffa2v2;
	
	public static boolean stressing = false;
	
	public static Plugin get() {
	    return pl;
	  }	
	
	
	public static SpleefType getServerType() {
		return SpleefType.valueOf(server.replaceAll("\\d","").toUpperCase());
	}
	
	
	@Override
	public void onEnable() {
		isOpen = false;
		pl = this;
		configs();		
		server = config.getConfig().getString("server");	
		listeners();
		commands();
		tasks();
		loads();
	}
	
	private void tasks() {
		//new FindGamesTask();
		new MinuteTask();	
		new IntegrationTask();
		new ArenaTask();
		new LowMoveTask();
		new HighMoveTask();
		new ScoreboardTask();
		new TNTRunTask();
		new ActionBarTask();
		if (stressing) new StressingTask();
		
	}
	
	private void configs() {
		config = new Configuration("config.yml",this);
		arenas = new Configuration("arenas.yml",this);	
		helmets = new Configuration("helmets.yml",this);	
	}

	private void loads() {
		GameManager.getManager().loadArenas();
		HelmetManager.getManager().loadHelmets();
	}
	
	private void listeners() {
		getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);	
		getServer().getPluginManager().registerEvents(new ServerListener(), this);	
		getServer().getPluginManager().registerEvents(new ChatListener(), this);	
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);	
	}
	
	
	private void commands() {
		getCommand("admin").setExecutor(new AdminCommand());
		getCommand("chat").setExecutor(new ChatCommand());
		getCommand("crumble").setExecutor(new CrumbleCommand());
		getCommand("forcedecay").setExecutor(new ForceDecayCommand());
		getCommand("forcefinish").setExecutor(new ForceFinishCommand());
		getCommand("forcegametime").setExecutor(new ForceGameTimeCommand());
		getCommand("forcepause").setExecutor(new ForcePauseCommand());
		getCommand("forceplayto").setExecutor(new ForcePlaytoCommand());
		getCommand("forceredo").setExecutor(new ForceRedoCommand());
		getCommand("forcereset").setExecutor(new ForceResetCommand());
		getCommand("forceresume").setExecutor(new ForceResumeCommand());
		getCommand("forcescore").setExecutor(new ForceScoreCommand());
		getCommand("matches").setExecutor(new MatchesCommand());
		getCommand("nightvision").setExecutor(new NightVisionCommand());
		getCommand("options").setExecutor(new OptionsCommand());
		getCommand("pause").setExecutor(new PauseCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("playto").setExecutor(new PlaytoCommand());
		getCommand("redo").setExecutor(new RedoCommand());
		getCommand("reset").setExecutor(new ResetCommand());
		getCommand("resume").setExecutor(new ResumeCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("translate").setExecutor(new TranslateCommand());	
		getCommand("hover").setExecutor(new HoverCommand());
	}
	
}
