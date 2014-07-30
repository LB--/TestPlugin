package com.lb_stuff.testplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockBreakEvent;

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

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onXp(BlockExpEvent e)
	{
		if(e instanceof BlockBreakEvent)
		{
			return;
		}
		e.setExpToDrop(e.getExpToDrop()*10);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e)
	{
		e.setExpToDrop(e.getExpToDrop()*10);
	}
}
