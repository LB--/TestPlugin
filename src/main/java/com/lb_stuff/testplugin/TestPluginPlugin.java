package com.lb_stuff.testplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.Material;
import org.bukkit.projectiles.ProjectileSource;

public class TestPluginPlugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		ItemStack is = new ItemStack(Material.POTION);
		new Potion(PotionType.SLOWNESS).splash().apply(is);
		PotionMeta m = (PotionMeta)is.getItemMeta();
		m.clearCustomEffects();
		m.addCustomEffect(PotionEffectType.SPEED.createEffect(20*10, 1), true);
		is.setItemMeta(m);
		Player p = e.getPlayer();
		p.getWorld().dropItem(p.getEyeLocation(), is).setPickupDelay(0);
	}
	@EventHandler
	public void onSplash(PotionSplashEvent e)
	{
		ThrownPotion tp = e.getPotion();
		ProjectileSource ps = tp.getShooter();
		if(ps instanceof Player)
		{
			Player p = (Player)ps;
			p.sendMessage("e.getPotion().getEffects():");
			for(PotionEffect pe : e.getPotion().getEffects())
			{
				p.sendMessage("- "+pe);
			}
			PotionMeta m = (PotionMeta)e.getPotion().getItem().getItemMeta();
			p.sendMessage("m.getCustomEffects():");
			for(PotionEffect pe : m.getCustomEffects())
			{
				p.sendMessage("- "+pe);
			}
		}
	}
}
