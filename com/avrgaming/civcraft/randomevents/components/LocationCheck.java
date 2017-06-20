/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEventComponent;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ 
/*    */ public class LocationCheck
/*    */   extends RandomEventComponent
/*    */ {
/*    */   public void process() {}
/*    */   
/*    */   public boolean onCheck()
/*    */   {
/* 20 */     String varname = getString("varname");
/* 21 */     String locString = (String)getParent().componentVars.get(varname);
/*    */     
/* 23 */     if (locString == null) {
/* 24 */       CivLog.warning("Couldn't get var name:" + varname + " for location check component.");
/* 25 */       return false;
/*    */     }
/*    */     
/* 28 */     BlockCoord bcoord = new BlockCoord(locString);
/* 29 */     double radiusSquared = 2500.0D;
/* 30 */     List<PlayerLocationCache> cache = PlayerLocationCache.getNearbyPlayers(bcoord, radiusSquared);
/*    */     
/* 32 */     for (PlayerLocationCache pc : cache) {
/* 33 */       Resident resident = CivGlobal.getResident(pc.getName());
/* 34 */       if (resident.getTown() == getParentTown()) {
/* 35 */         return true;
/*    */       }
/*    */     }
/*    */     
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\LocationCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */