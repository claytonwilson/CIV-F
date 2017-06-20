/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import org.bukkit.entity.Player;
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
/*    */ 
/*    */ public class PlayerLocationCacheUpdate
/*    */   implements Runnable
/*    */ {
/* 32 */   public static int UPDATE_LIMIT = 20;
/* 33 */   public static Queue<String> playerQueue = new LinkedList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/* 40 */     for (int i = 0; i < UPDATE_LIMIT; i++) {
/* 41 */       String playerName = (String)playerQueue.poll();
/* 42 */       if (playerName == null) {
/* 43 */         return;
/*    */       }
/*    */       try
/*    */       {
/* 47 */         Player player = CivGlobal.getPlayer(playerName);
/* 48 */         PlayerLocationCache.updateLocation(player);
/* 49 */         playerQueue.add(playerName);
/*    */       }
/*    */       catch (CivException e)
/*    */       {
/* 53 */         PlayerLocationCache.remove(playerName);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\PlayerLocationCacheUpdate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */