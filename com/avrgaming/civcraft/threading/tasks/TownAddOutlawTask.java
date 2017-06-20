/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import org.bukkit.ChatColor;
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
/*    */ public class TownAddOutlawTask
/*    */   implements Runnable
/*    */ {
/*    */   String name;
/*    */   Town town;
/*    */   
/*    */   public TownAddOutlawTask(String name, Town town)
/*    */   {
/* 37 */     this.name = name;
/* 38 */     this.town = town;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 45 */       Player player = CivGlobal.getPlayer(this.name);
/* 46 */       CivMessage.send(player, "§e" + ChatColor.BOLD + "You are now an outlaw to " + this.town.getName() + " towers will fire upon you if you visit them!");
/*    */     }
/*    */     catch (CivException localCivException) {}
/*    */     
/* 50 */     this.town.addOutlaw(this.name);
/* 51 */     this.town.save();
/* 52 */     CivMessage.sendTown(this.town, "§e" + this.name + " is now an outlaw in this town!");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\TownAddOutlawTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */