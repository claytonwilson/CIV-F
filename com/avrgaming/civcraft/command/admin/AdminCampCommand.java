/*    */ package com.avrgaming.civcraft.command.admin;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.Camp;
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import java.io.IOException;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
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
/*    */ public class AdminCampCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 36 */     this.command = "/ad camp";
/* 37 */     this.displayName = "Admin camp";
/*    */     
/* 39 */     this.commands.put("destroy", "[name] - destroyes this camp.");
/* 40 */     this.commands.put("setraidtime", "[name] - d:M:y:H:m sets the raid time.");
/* 41 */     this.commands.put("rebuild", "rebuilds this camp template");
/*    */   }
/*    */   
/*    */   public void rebuild_cmd() throws CivException {
/* 45 */     Camp camp = getNamedCamp(1);
/*    */     try
/*    */     {
/* 48 */       camp.repairFromTemplate();
/*    */     }
/*    */     catch (IOException localIOException) {}catch (CivException e) {
/* 51 */       e.printStackTrace();
/*    */     }
/* 53 */     camp.reprocessCommandSigns();
/* 54 */     CivMessage.send(this.sender, "Repaired.");
/*    */   }
/*    */   
/*    */   public void setraidtime_cmd() throws CivException {
/* 58 */     Resident resident = getNamedResident(1);
/*    */     
/* 60 */     if (!resident.hasCamp()) {
/* 61 */       throw new CivException("This resident does not have a camp.");
/*    */     }
/*    */     
/* 64 */     if (this.args.length < 3) {
/* 65 */       throw new CivException("Enter a camp owner and date like DAY:MONTH:YEAR:HOUR:MIN");
/*    */     }
/*    */     
/* 68 */     Camp camp = resident.getCamp();
/*    */     
/* 70 */     String dateStr = this.args[2];
/* 71 */     SimpleDateFormat parser = new SimpleDateFormat("d:M:y:H:m");
/*    */     
/*    */     try
/*    */     {
/* 75 */       Date next = parser.parse(dateStr);
/* 76 */       camp.setNextRaidDate(next);
/* 77 */       CivMessage.sendSuccess(this.sender, "Set raid date.");
/*    */     } catch (ParseException e) {
/* 79 */       throw new CivException("Couldnt parse " + this.args[2] + " into a date, use format: DAY:MONTH:YEAR:HOUR:MIN");
/*    */     }
/*    */     Date next;
/*    */   }
/*    */   
/*    */   public void destroy_cmd() throws CivException {
/* 85 */     Camp camp = getNamedCamp(1);
/* 86 */     camp.destroy();
/* 87 */     CivMessage.sendSuccess(this.sender, "Camp destroyed.");
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 92 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 97 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminCampCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */