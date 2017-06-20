/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ public class FoundCivSync
/*    */   implements Runnable
/*    */ {
/*    */   Resident resident;
/*    */   
/*    */   public FoundCivSync(Resident resident)
/*    */   {
/* 35 */     this.resident = resident;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 43 */       player = CivGlobal.getPlayer(this.resident);
/*    */     } catch (CivException e1) { Player player;
/* 45 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 49 */       Civilization.newCiv(this.resident.desiredCivName, this.resident.desiredCapitolName, this.resident, player, this.resident.desiredTownLocation);
/*    */     } catch (CivException e) { Player player;
/* 51 */       CivMessage.send(player, "§c" + e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\FoundCivSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */