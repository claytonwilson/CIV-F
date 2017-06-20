/*    */ package com.avrgaming.civcraft.listener;
/*    */ 
/*    */ import com.avrgaming.civcraft.interactive.InteractiveResponse;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChatListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   void OnPlayerAsyncChatEvent(AsyncPlayerChatEvent event)
/*    */   {
/* 36 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 37 */     if (resident == null)
/*    */     {
/* 39 */       return;
/*    */     }
/*    */     
/* 42 */     if (resident.isTownChat()) {
/* 43 */       event.setCancelled(true);
/* 44 */       if (resident.getTownChatOverride() == null) {
/* 45 */         CivMessage.sendTownChat(resident.getTown(), resident, event.getFormat(), event.getMessage());
/*    */       } else {
/* 47 */         CivMessage.sendTownChat(resident.getTownChatOverride(), resident, event.getFormat(), event.getMessage());
/*    */       }
/*    */     }
/*    */     
/* 51 */     if (resident.isCivChat())
/*    */     {
/* 53 */       event.setCancelled(true);
/* 54 */       Civilization civ; Civilization civ; if (resident.getTown() == null) {
/* 55 */         civ = null;
/*    */       } else {
/* 57 */         civ = resident.getTown().getCiv();
/*    */       }
/*    */       
/* 60 */       if (resident.getCivChatOverride() == null) {
/* 61 */         CivMessage.sendCivChat(civ, resident, event.getFormat(), event.getMessage());
/*    */       } else {
/* 63 */         CivMessage.sendCivChat(resident.getCivChatOverride(), resident, event.getFormat(), event.getMessage());
/*    */       }
/*    */     }
/*    */     
/* 67 */     if (resident.isInteractiveMode()) {
/* 68 */       resident.getInteractiveResponse().respond(event.getMessage(), resident);
/* 69 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\ChatListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */