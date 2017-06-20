/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import java.io.IOException;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveBuildableRefresh implements InteractiveResponse
/*    */ {
/*    */   String playerName;
/*    */   Buildable buildable;
/*    */   
/*    */   public InteractiveBuildableRefresh(Buildable buildable, String playerName)
/*    */   {
/* 21 */     this.playerName = playerName;
/* 22 */     this.buildable = buildable;
/* 23 */     displayMessage();
/*    */   }
/*    */   
/*    */   public void displayMessage()
/*    */   {
/*    */     try {
/* 29 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 34 */     CivMessage.sendHeading(player, "Building Refresh");
/* 35 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Are you sure you want to refresh the blocks for your " + this.buildable.getDisplayName() + "?");
/* 36 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Any blocks inside the structure (or where the structure ought to be) will be replaced with whats inside the template.");
/* 37 */     CivMessage.send(player, "§a" + CivColor.BOLD + "You may lose some blocks. If that's ok, please type 'yes'. Type anything else to cancel.");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void respond(String message, Resident resident)
/*    */   {
/* 44 */     resident.clearInteractiveMode();
/*    */     
/* 46 */     if (!message.equalsIgnoreCase("yes")) {
/* 47 */       CivMessage.send(resident, "§7Refresh cancelled.");
/* 48 */       return;
/*    */     }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 77 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       Buildable buildable;
/*    */       Resident resident;
/*    */       
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/*    */           try
/*    */           {
/* 64 */             this.buildable.repairFromTemplate();
/* 65 */             this.buildable.getTown().markLastBuildableRefeshAsNow();
/* 66 */             CivMessage.sendSuccess(this.resident, this.buildable.getDisplayName() + " refreshed.");
/*    */           } catch (IOException e) {
/* 68 */             e.printStackTrace();
/* 69 */             throw new CivException("IO error. Couldn't find template file:" + this.buildable.getSavedTemplatePath() + " ?");
/*    */           }
/*    */         } catch (CivException e) {
/* 72 */           CivMessage.sendError(this.resident, e.getMessage());
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveBuildableRefresh.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */