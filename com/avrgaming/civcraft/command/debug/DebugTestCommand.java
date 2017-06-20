/*    */ package com.avrgaming.civcraft.command.debug;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.test.TestGetChestThread;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.timers.LagSimulationTimer;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ public class DebugTestCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 38 */     this.command = "/dbg test ";
/* 39 */     this.displayName = "Test Commands";
/*    */     
/* 41 */     this.commands.put("getsyncchesttest", "Does a performance test by getting chests. NEVER RUN THIS ON PRODUCTION.");
/* 42 */     this.commands.put("setlag", "[tps] - tries to set the tps to this amount to simulate lag.");
/*    */   }
/*    */   
/*    */   public void setlag_cmd() throws CivException {
/* 46 */     Integer tps = getNamedInteger(1);
/* 47 */     TaskMaster.syncTimer("lagtimer", new LagSimulationTimer(tps.intValue()), 0L);
/* 48 */     CivMessage.sendSuccess(this.sender, "Let the lagging begin.");
/*    */   }
/*    */   
/*    */   public void getsyncchesttest_cmd() throws CivException {
/* 52 */     Integer count = getNamedInteger(1);
/*    */     
/* 54 */     for (int i = 0; i < count.intValue(); i++) {
/* 55 */       TaskMaster.asyncTask(new TestGetChestThread(), 0L);
/*    */     }
/*    */     
/* 58 */     CivMessage.sendSuccess(this.sender, "Started " + count + " threads, watch logs.");
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 63 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 68 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   private void isNetizen() throws CivException {
/* 72 */     if (!getPlayer().getName().equalsIgnoreCase("netizen539")) {
/* 73 */       throw new CivException("You must be netizen to run these commands.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {
/* 80 */     isNetizen();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\debug\DebugTestCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */