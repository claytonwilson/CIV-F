/*    */ package com.avrgaming.civcraft.command.debug;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.Camp;
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
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
/*    */ public class DebugCampCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 33 */     this.command = "/dbg test ";
/* 34 */     this.displayName = "Test Commands";
/*    */     
/* 36 */     this.commands.put("growth", "[name] - Shows a list of this player's camp growth spots.");
/*    */   }
/*    */   
/*    */   public void growth_cmd() throws CivException
/*    */   {
/* 41 */     Resident resident = getNamedResident(1);
/*    */     
/* 43 */     if (!resident.hasCamp()) {
/* 44 */       throw new CivException("This guy doesnt have a camp.");
/*    */     }
/*    */     
/* 47 */     Camp camp = resident.getCamp();
/*    */     
/* 49 */     CivMessage.sendHeading(this.sender, "Growth locations");
/*    */     
/* 51 */     String out = "";
/* 52 */     for (BlockCoord coord : camp.growthLocations) {
/* 53 */       boolean inGlobal = CivGlobal.vanillaGrowthLocations.contains(coord);
/* 54 */       out = out + coord.toString() + " in global:" + inGlobal;
/*    */     }
/*    */     
/* 57 */     CivMessage.send(this.sender, out);
/*    */   }
/*    */   
/*    */   public void doDefaultAction()
/*    */     throws CivException
/*    */   {
/* 63 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 68 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\debug\DebugCampCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */