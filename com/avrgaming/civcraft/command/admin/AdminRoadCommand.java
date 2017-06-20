/*    */ package com.avrgaming.civcraft.command.admin;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.road.Road;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class AdminRoadCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 20 */     this.command = "/ad road";
/* 21 */     this.displayName = "Admin Road";
/*    */     
/*    */ 
/* 24 */     this.commands.put("setraidtime", "d:M:y:H:m sets the raid time on the nearest road");
/*    */   }
/*    */   
/*    */   public void setraidtime_cmd() throws CivException {
/* 28 */     Town town = getNamedTown(1);
/* 29 */     Player player = getPlayer();
/*    */     
/* 31 */     if (this.args.length < 3) {
/* 32 */       throw new CivException("Enter a date like DAY:MONTH:YEAR:HOUR:MIN");
/*    */     }
/*    */     
/* 35 */     Buildable buildable = town.getNearestBuildable(player.getLocation());
/*    */     
/* 37 */     if (!(buildable instanceof Road)) {
/* 38 */       throw new CivException("Nearest structure is not a road, it's a " + buildable.getDisplayName());
/*    */     }
/*    */     
/* 41 */     Road road = (Road)buildable;
/*    */     
/* 43 */     String dateStr = this.args[2];
/* 44 */     SimpleDateFormat parser = new SimpleDateFormat("d:M:y:H:m");
/*    */     
/*    */     try
/*    */     {
/* 48 */       Date next = parser.parse(dateStr);
/* 49 */       road.setNextRaidDate(next);
/* 50 */       CivMessage.sendSuccess(this.sender, "Set raid date.");
/*    */     } catch (ParseException e) {
/* 52 */       throw new CivException("Couldnt parse " + this.args[2] + " into a date, use format: DAY:MONTH:YEAR:HOUR:MIN");
/*    */     }
/*    */     Date next;
/*    */   }
/*    */   
/*    */   public void doDefaultAction()
/*    */     throws CivException
/*    */   {
/* 60 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 65 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminRoadCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */