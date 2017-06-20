/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
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
/*    */ public class NotificationTask
/*    */   implements Runnable
/*    */ {
/*    */   String message;
/*    */   String playerName;
/*    */   
/*    */   public NotificationTask(String playerName, String msg)
/*    */   {
/* 33 */     this.message = msg;
/* 34 */     this.playerName = playerName;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 40 */       Player player = CivGlobal.getPlayer(this.playerName);
/* 41 */       CivMessage.send(player, this.message);
/*    */     }
/*    */     catch (CivException localCivException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\NotificationTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */