/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import java.util.Collection;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.kitteh.tag.TagAPI;
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
/*    */ 
/*    */ 
/*    */ public class SyncUpdateTags
/*    */   implements Runnable
/*    */ {
/*    */   Collection<Resident> residentsToSendUpdate;
/*    */   String playerToUpdate;
/*    */   
/*    */   public SyncUpdateTags(String playerToUpdate, Collection<Resident> residentsToSendUpdate)
/*    */   {
/* 36 */     this.residentsToSendUpdate = residentsToSendUpdate;
/* 37 */     this.playerToUpdate = playerToUpdate;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 43 */       Player player = CivGlobal.getPlayer(this.playerToUpdate);
/* 44 */       for (Resident resident : this.residentsToSendUpdate) {
/*    */         try {
/* 46 */           Player resPlayer = CivGlobal.getPlayer(resident);
/* 47 */           if (player != resPlayer)
/*    */           {
/*    */ 
/*    */ 
/* 51 */             TagAPI.refreshPlayer(player, resPlayer);
/* 52 */             TagAPI.refreshPlayer(resPlayer, player);
/*    */           }
/*    */         }
/*    */         catch (CivException localCivException1) {}
/*    */       }
/*    */     }
/*    */     catch (CivException e1) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncUpdateTags.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */