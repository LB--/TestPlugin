package com.lb_stuff.testplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;

public class TestPluginPlugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);

		new Colorizer().infect();
		getLogger().info(""+ChatColor.AQUA+"test logger");
		System.out.println(""+ChatColor.AQUA+"test println");
	}
	@Override
	public void onDisable()
	{
	}

//	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		final Player p = e.getPlayer();
		getServer().getScheduler().runTaskLater(this, new Runnable(){@Override public void run()
		{
			EntityDamageEvent event = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.DROWNING, p.getHealth());
			getServer().getPluginManager().callEvent(event);
			p.setLastDamageCause(event);
			p.setHealth(0.0);
		}}, 20*6);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e)
	{
		getLogger().info("######## \""+e.getDeathMessage()+"\"");
	}
}
