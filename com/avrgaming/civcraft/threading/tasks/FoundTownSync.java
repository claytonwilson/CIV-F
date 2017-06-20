/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class FoundTownSync
/*    */   implements Runnable
/*    */ {
/*    */   Resident resident;
/*    */   
/*    */   public FoundTownSync(Resident resident)
/*    */   {
/* 35 */     this.resident = resident;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 42 */       player = CivGlobal.getPlayer(this.resident);
/*    */     } catch (CivException e1) { Player player;
/* 44 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 48 */       Town.newTown(this.resident, this.resident.desiredTownName, this.resident.getCiv(), false, false, this.resident.desiredTownLocation);
/*    */     } catch (CivException e) { Player player;
/* 50 */       CivMessage.send(player, "§c" + e.getMessage());
/* 51 */       return;
/*    */     }
/*    */     
/*    */ 
/* 55 */     CivMessage.global(this.resident.desiredTownName + " has been founded by " + this.resident.getCiv().getName() + "!");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\FoundTownSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */