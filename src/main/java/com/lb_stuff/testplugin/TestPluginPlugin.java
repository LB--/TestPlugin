package com.lb_stuff.testplugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestPluginPlugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable()
	{
	}

	private File getPlayerConfig(UUID uuid)
	{
		File f = new File(getDataFolder(), "players/");
		f.mkdirs();
		f = new File(f, ""+uuid);
		try
		{
			f.createNewFile();
		}
		catch(IOException ex)
		{
			getLogger().log(Level.SEVERE, null, ex);
		}
		return f;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		File f = getPlayerConfig(e.getPlayer().getUniqueId());
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
		conf.set("last-username", e.getPlayer().getName());
		try
		{
			conf.save(f);
		}
		catch(IOException ex)
		{
			getLogger().log(Level.SEVERE, null, ex);
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLEave(PlayerQuitEvent e)
	{
		File f = getPlayerConfig(e.getPlayer().getUniqueId());
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
		conf.set("last-display-name", e.getPlayer().getDisplayName());
		try
		{
			conf.save(f);
		}
		catch(IOException ex)
		{
			getLogger().log(Level.SEVERE, null, ex);
		}
	}
}
