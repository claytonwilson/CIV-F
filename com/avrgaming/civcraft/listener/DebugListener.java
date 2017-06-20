package com.avrgaming.civcraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DebugListener
  implements Listener
{
  @EventHandler(priority=EventPriority.MONITOR)
  public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {}
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {}
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {}
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onEntityDamageEvent(EntityDamageEvent event) {}
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onEntityInteractEvent(EntityInteractEvent event) {}
}


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\DebugListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */