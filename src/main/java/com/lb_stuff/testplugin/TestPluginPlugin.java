package com.lb_stuff.testplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;

public class TestPluginPlugin extends JavaPlugin implements Listener
{
	private static Class<?> findClass(String name)
	{
		try
		{
			return Class.forName(name);
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static Class<?> NMS_DamageSource = null;
	private static Class<?> NMS_Explosion = null;
	private static Class<?> NMS_Entity = null;
	private static Class<?> NMS_EntityLiving = null;
	private static Class<?> NMS_EntityHuman = null;
	private static Class<?> NMS_EntityArrow = null;
	private static Class<?> NMS_EntityFireball = null;
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);

		NMS_DamageSource   = findClass("net.minecraft.server.v1_7_R4.DamageSource");
		NMS_Explosion      = findClass("net.minecraft.server.v1_7_R4.Explosion");
		NMS_Entity         = findClass("net.minecraft.server.v1_7_R4.Entity");
		NMS_EntityLiving   = findClass("net.minecraft.server.v1_7_R4.EntityLiving");
		NMS_EntityHuman    = findClass("net.minecraft.server.v1_7_R4.EntityHuman");
		NMS_EntityArrow    = findClass("net.minecraft.server.v1_7_R4.EntityArrow");
		NMS_EntityFireball = findClass("net.minecraft.server.v1_7_R4.EntityFireball");
	}
	@Override
	public void onDisable()
	{
	}

	private static Method findMethod(Class<?> start, String name, Class<?>... argtypes)
	{
		Method m = null;
		while(start.getSuperclass() != null)
		{
			try
			{
				m = start.getDeclaredMethod(name, argtypes);
			}
			catch(NoSuchMethodException e)
			{
				start = start.getSuperclass();
			}
		}
		return m;
	}
	private static Object invokeMethod(Method m, Object inst, Object... args)
	{
		try
		{
			m.setAccessible(true);
			return m.invoke(inst, args);
		}
		catch(IllegalAccessException|InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
	private static Field findField(Class<?> start, String name)
	{
		Field f = null;
		while(start.getSuperclass() != null)
		{
			try
			{
				f = start.getDeclaredField(name);
			}
			catch(NoSuchFieldException e)
			{
				start = start.getSuperclass();
			}
		}
		return f;
	}
	private static Object getField(Field f, Object inst)
	{
		try
		{
			f.setAccessible(true);
			return f.get(inst);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static Object getHandle(Entity e)
	{
		return invokeMethod(findMethod(e.getClass(), "getHandle"), e);
	}

	private static boolean damageEntity(Entity e, Object damagesource, float f)
	{
		Object handle = getHandle(e);
		Method m = findMethod(handle.getClass(), "damageEntity", NMS_DamageSource, Float.TYPE);
		return (Boolean)invokeMethod(m, handle, damagesource, f);
	}
	private static Object damageSource(String name, Class<?>[] clazzes, Object... args)
	{
		return invokeMethod(findMethod(NMS_DamageSource, name, clazzes), null, args);
	}
	private static Object damageSource(String name)
	{
		return getField(findField(NMS_DamageSource, name), null);
	}
	private static Object damageSourceFromCause(EntityDamageEvent e)
	{
		DamageCause d = e.getCause();
		EntityDamageByEntityEvent ee = null;
		if(e instanceof EntityDamageByEntityEvent)
		{
			ee = (EntityDamageByEntityEvent)e;
		}
		switch(d)
		{
			case BLOCK_EXPLOSION: return damageSource("explosion", new Class<?>[]{NMS_Explosion}, (Object)null);
			case CONTACT: return damageSource("CACTUS");
			case CUSTOM: return damageSource("GENERIC");
			case DROWNING: return damageSource("DROWN");
			case ENTITY_ATTACK:
			{
				if(ee.getDamager() instanceof Player)
				{
					return damageSource("playerAttack", new Class<?>[]{NMS_EntityHuman}, getHandle(ee.getDamager()));
				}
				return damageSource("mobAttack", new Class<?>[]{NMS_EntityLiving}, getHandle(ee.getDamager()));
			}
			case ENTITY_EXPLOSION: return damageSource("explosion", new Class<?>[]{NMS_Explosion}, (Object)null);
			case FALL: return damageSource("FALL");
			case FALLING_BLOCK: return damageSource("ANVIL");
			case FIRE: return damageSource("FIRE");
			case FIRE_TICK: return damageSource("BURN");
			case LAVA: return damageSource("LAVA");
			case LIGHTNING: return damageSource("mobAttack", new Class<?>[]{NMS_EntityLiving}, getHandle(ee.getDamager()));
			case MAGIC: return damageSource("MAGIC");
			case MELTING: return damageSource("BURN");
			case POISON: return damageSource("MAGIC");
			case PROJECTILE:
			{
				Projectile proj = (Projectile)ee.getDamager();
				Object shooter = null;
				if(proj.getShooter() instanceof Entity)
				{
					shooter = getHandle((Entity)proj.getShooter());
				}
				if(ee.getDamager() instanceof Arrow)
				{
					return damageSource("arrow", new Class<?>[]{NMS_EntityArrow, NMS_Entity}, getHandle(ee.getDamager()), shooter);
				}
				else if(ee.getDamager() instanceof Fireball)
				{
					return damageSource("fireball", new Class<?>[]{NMS_EntityFireball, NMS_Entity}, getHandle(ee.getDamager()), shooter);
				}
				return damageSource("projectile", new Class<?>[]{NMS_Entity, NMS_Entity}, getHandle(ee.getDamager()), shooter);
			}
			case STARVATION: return damageSource("STARVE");
			case SUFFOCATION: return damageSource("STUCK");
			case SUICIDE: return damageSource("GENERIC");
			case THORNS: return damageSource("a", new Class<?>[]{NMS_Entity}, getHandle(ee.getDamager()));
			case VOID: return damageSource("OUT_OF_WORLD");
			case WITHER: return damageSource("MAGIC");
		}
		return damageSource("GENERIC");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent e)
	{
		if(e instanceof Player)
		{
			return;
		}
		for(Player p : Bukkit.getServer().getOnlinePlayers())
		{
			if(!p.getUniqueId().equals(e.getEntity().getUniqueId()))
			{
				damageEntity(p, damageSourceFromCause(e), (float)e.getDamage());
			}
		}
	}
}
