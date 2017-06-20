/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class GivePlatinum extends com.avrgaming.civcraft.randomevents.RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 12 */     for (com.avrgaming.civcraft.object.Resident resident : getParentTown().getResidents()) {
/* 13 */       com.avrgaming.global.perks.PlatinumManager.givePlatinumDaily(resident, 
/* 14 */         ((ConfigPlatinumReward)CivSettings.platinumRewards.get("randomEventSuccess")).name, 
/* 15 */         Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("randomEventSuccess")).amount), 
/* 16 */         getString("message"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\GivePlatinum.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */