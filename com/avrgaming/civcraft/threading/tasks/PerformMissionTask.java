/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigMission;
/*    */ import com.avrgaming.civcraft.items.units.MissionBook;
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
/*    */ public class PerformMissionTask
/*    */   implements Runnable
/*    */ {
/*    */   ConfigMission mission;
/*    */   String playerName;
/*    */   
/*    */   public PerformMissionTask(ConfigMission mission, String playerName)
/*    */   {
/* 30 */     this.mission = mission;
/* 31 */     this.playerName = playerName;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 37 */     MissionBook.performMission(this.mission, this.playerName);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PerformMissionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */