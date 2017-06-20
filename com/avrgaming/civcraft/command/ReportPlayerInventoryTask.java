/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import java.util.Queue;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.command.CommandSender;
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
/*    */ public class ReportPlayerInventoryTask
/*    */   implements Runnable
/*    */ {
/*    */   Queue<OfflinePlayer> offplayers;
/*    */   CommandSender sender;
/*    */   
/*    */   public ReportPlayerInventoryTask(CommandSender sender, Queue<OfflinePlayer> offplayers)
/*    */   {
/* 35 */     this.sender = sender;
/* 36 */     this.offplayers = offplayers;
/*    */   }
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
/*    */   public void run()
/*    */   {
/* 56 */     CivMessage.sendError(this.sender, "Deprecated do not use anymore.. or fix it..");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\ReportPlayerInventoryTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */