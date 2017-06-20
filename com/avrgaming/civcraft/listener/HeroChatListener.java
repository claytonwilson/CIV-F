/*    */ package com.avrgaming.civcraft.listener;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.dthielke.herochat.Channel;
/*    */ import com.dthielke.herochat.ChannelChatEvent;
/*    */ import com.dthielke.herochat.Chatter;
/*    */ import com.dthielke.herochat.Chatter.Result;
/*    */ import com.dthielke.herochat.ChatterManager;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ 
/*    */ public class HeroChatListener implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.MONITOR)
/*    */   public void onChannelChatEvent(ChannelChatEvent event)
/*    */   {
/* 21 */     Resident resident = CivGlobal.getResident(event.getSender().getName());
/* 22 */     if (resident == null) {
/* 23 */       event.setResult(Chatter.Result.FAIL);
/* 24 */       return;
/*    */     }
/*    */     
/* 27 */     if ((!resident.isInteractiveMode()) && 
/* 28 */       (resident.isMuted())) {
/* 29 */       event.setResult(Chatter.Result.MUTED);
/* 30 */       return;
/*    */     }
/*    */     
/*    */ 
/* 34 */     if (event.getChannel().getDistance() > 0) {
/* 35 */       for (String name : Resident.allchatters)
/*    */       {
/*    */         try {
/* 38 */           player = CivGlobal.getPlayer(name);
/*    */         } catch (CivException e) { Player player;
/* 40 */           continue;
/*    */         }
/*    */         Player player;
/* 43 */         Chatter you = com.dthielke.herochat.Herochat.getChatterManager().getChatter(player);
/* 44 */         if (!event.getSender().isInRange(you, event.getChannel().getDistance())) {
/* 45 */           player.sendMessage("§f" + event.getSender().getName() + "[Far]: " + event.getMessage());
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\listener\HeroChatListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */