/*    */ package com.avrgaming.civcraft.threading.sync;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class SyncUpdateTagsBetweenCivs
/*    */   implements Runnable
/*    */ {
/* 29 */   Set<Player> civList = new HashSet();
/* 30 */   Set<Player> otherCivList = new HashSet();
/*    */   
/*    */   public SyncUpdateTagsBetweenCivs(Set<Player> civList, Set<Player> otherCivList) {
/* 33 */     this.civList = civList;
/* 34 */     this.otherCivList = otherCivList;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 40 */     for (Player player : this.civList) {
/* 41 */       if (!this.otherCivList.isEmpty()) {
/* 42 */         TagAPI.refreshPlayer(player, this.otherCivList);
/*    */       }
/*    */     }
/*    */     
/* 46 */     for (Player player : this.otherCivList) {
/* 47 */       if (!this.civList.isEmpty()) {
/* 48 */         TagAPI.refreshPlayer(player, this.civList);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\SyncUpdateTagsBetweenCivs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */