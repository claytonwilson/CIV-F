/*    */ package com.avrgaming.civcraft.siege;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.block.BlockBreakEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ public class CannonListener implements org.bukkit.event.Listener
/*    */ {
/* 15 */   BlockCoord bcoord = new BlockCoord();
/*    */   
/*    */   @EventHandler(priority=EventPriority.NORMAL)
/*    */   public void onBlockBreak(BlockBreakEvent event) {
/* 19 */     this.bcoord.setFromLocation(event.getBlock().getLocation());
/* 20 */     Cannon cannon = (Cannon)Cannon.cannonBlocks.get(this.bcoord);
/* 21 */     if (cannon != null) {
/* 22 */       cannon.onHit(event);
/* 23 */       event.setCancelled(true);
/* 24 */       return;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   @EventHandler(priority=EventPriority.NORMAL)
/*    */   public void onPlayerInteract(PlayerInteractEvent event)
/*    */   {
/* 32 */     if (!event.hasBlock()) {
/* 33 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 37 */       this.bcoord.setFromLocation(event.getClickedBlock().getLocation());
/* 38 */       Cannon cannon = (Cannon)Cannon.fireSignLocations.get(this.bcoord);
/* 39 */       if (cannon != null) {
/* 40 */         cannon.processFire(event);
/* 41 */         event.setCancelled(true);
/* 42 */         return;
/*    */       }
/*    */       
/* 45 */       cannon = (Cannon)Cannon.angleSignLocations.get(this.bcoord);
/* 46 */       if (cannon != null) {
/* 47 */         cannon.processAngle(event);
/* 48 */         event.setCancelled(true);
/* 49 */         return;
/*    */       }
/*    */       
/* 52 */       cannon = (Cannon)Cannon.powerSignLocations.get(this.bcoord);
/* 53 */       if (cannon != null) {
/* 54 */         cannon.processPower(event);
/* 55 */         event.setCancelled(true);
/* 56 */         return;
/*    */       }
/*    */     } catch (CivException e) {
/* 59 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
/* 60 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\siege\CannonListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */