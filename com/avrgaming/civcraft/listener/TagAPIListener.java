/*    */ package com.avrgaming.civcraft.listener;
/*    */ 
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
/*    */ 
/*    */ public class TagAPIListener implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onNameTag(AsyncPlayerReceiveNameTagEvent event)
/*    */   {
/* 13 */     event.setTag(com.avrgaming.civcraft.main.CivGlobal.updateTag(event.getNamedPlayer(), event.getPlayer()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\TagAPIListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */