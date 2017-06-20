/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.sync.SyncUpdateTagsBetweenCivs;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.bukkit.Bukkit;
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
/*    */ public class UpdateTagBetweenCivsTask
/*    */   implements Runnable
/*    */ {
/*    */   Civilization civ;
/*    */   Civilization otherCiv;
/*    */   
/*    */   public UpdateTagBetweenCivsTask(Civilization civ, Civilization otherCiv)
/*    */   {
/* 39 */     this.civ = civ;
/* 40 */     this.otherCiv = otherCiv;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 45 */     Set<Player> civList = new HashSet();
/* 46 */     Set<Player> otherCivList = new HashSet();
/*    */     Player[] arrayOfPlayer;
/* 48 */     int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 49 */       Resident resident = CivGlobal.getResident(player);
/* 50 */       if ((resident != null) && (resident.hasTown()))
/*    */       {
/*    */ 
/*    */ 
/* 54 */         if (resident.getTown().getCiv() == this.civ) {
/* 55 */           civList.add(player);
/* 56 */         } else if (resident.getTown().getCiv() == this.otherCiv) {
/* 57 */           otherCivList.add(player);
/*    */         }
/*    */       }
/*    */     }
/* 61 */     TaskMaster.syncTask(new SyncUpdateTagsBetweenCivs(civList, otherCivList));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\UpdateTagBetweenCivsTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */