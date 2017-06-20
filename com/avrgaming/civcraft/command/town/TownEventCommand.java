/*    */ package com.avrgaming.civcraft.command.town;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.randomevents.ConfigRandomEvent;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class TownEventCommand extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 16 */     this.command = "/town event";
/* 17 */     this.displayName = "Town Event";
/*    */     
/* 19 */     this.commands.put("show", "Shows current town event.");
/* 20 */     this.commands.put("activate", "Activates current event.");
/*    */   }
/*    */   
/*    */   public void activate_cmd() throws CivException {
/* 24 */     Town town = getSelectedTown();
/* 25 */     RandomEvent event = town.getActiveEvent();
/*    */     
/* 27 */     if (event == null) {
/* 28 */       CivMessage.sendError(this.sender, "No current event.");
/*    */     } else {
/* 30 */       event.activate();
/* 31 */       CivMessage.sendSuccess(this.sender, "Event activated!");
/*    */     }
/*    */   }
/*    */   
/*    */   public void show_cmd() throws CivException {
/* 36 */     Town town = getSelectedTown();
/* 37 */     RandomEvent event = town.getActiveEvent();
/*    */     
/* 39 */     if (event == null) {
/* 40 */       CivMessage.sendError(this.sender, "No current event.");
/*    */     } else {
/* 42 */       SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/*    */       
/* 44 */       CivMessage.sendHeading(this.sender, "Current Event: " + event.configRandomEvent.name);
/* 45 */       CivMessage.send(this.sender, "§2Started On: §a" + sdf.format(event.getStartDate()));
/* 46 */       CivMessage.send(this.sender, "§2End Date:§a" + sdf.format(event.getEndDate()));
/* 47 */       if (event.isActive()) {
/* 48 */         CivMessage.send(this.sender, "§7Event has been activated.");
/*    */       } else {
/* 50 */         CivMessage.send(this.sender, "§eEvent has not been activated. Use '/town event activate' to activate the event.");
/*    */       }
/* 52 */       CivMessage.send(this.sender, "§2-- Messages From Event ---");
/* 53 */       CivMessage.send(this.sender, "§7" + event.getMessages());
/*    */     }
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 59 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 64 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownEventCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */