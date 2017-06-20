/*    */ package com.avrgaming.civcraft.structure.wonders;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.BuffManager;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Collection;
/*    */ import org.bukkit.Location;
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
/*    */ public class NotreDame
/*    */   extends Wonder
/*    */ {
/*    */   public NotreDame(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 35 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public NotreDame(ResultSet rs) throws SQLException, CivException {
/* 39 */     super(rs);
/*    */   }
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 44 */     if (isActive()) {
/* 45 */       addBuffs();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onComplete()
/*    */   {
/* 51 */     addBuffs();
/*    */   }
/*    */   
/*    */   public void onDestroy()
/*    */   {
/* 56 */     super.onDestroy();
/* 57 */     removeBuffs();
/*    */   }
/*    */   
/*    */   protected void removeBuffs()
/*    */   {
/* 62 */     removeBuffFromTown(getTown(), "buff_notre_dame_no_anarchy");
/* 63 */     removeBuffFromTown(getTown(), "buff_notre_dame_coins_from_peace");
/* 64 */     removeBuffFromTown(getTown(), "buff_notre_dame_extra_war_penalty");
/*    */   }
/*    */   
/*    */   protected void addBuffs()
/*    */   {
/* 69 */     addBuffToTown(getTown(), "buff_notre_dame_no_anarchy");
/* 70 */     addBuffToTown(getTown(), "buff_notre_dame_coins_from_peace");
/* 71 */     addBuffToTown(getTown(), "buff_notre_dame_extra_war_penalty");
/*    */   }
/*    */   
/*    */   public void processPeaceTownCoins()
/*    */   {
/* 76 */     double totalCoins = 0.0D;
/*    */     
/* 78 */     double coinsPerTown = getTown().getBuffManager().getEffectiveInt("buff_notre_dame_coins_from_peace");
/*    */     
/* 80 */     for (Civilization civ : CivGlobal.getCivs()) {
/* 81 */       if (!civ.isAdminCiv())
/*    */       {
/*    */ 
/*    */ 
/* 85 */         if (!civ.getDiplomacyManager().isAtWar())
/*    */         {
/*    */ 
/*    */ 
/* 89 */           totalCoins += coinsPerTown * civ.getTowns().size(); }
/*    */       }
/*    */     }
/* 92 */     getTown().depositTaxed(totalCoins);
/* 93 */     CivMessage.sendTown(getTown(), "Generated " + totalCoins + " from the peaceful towns of the world!");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\NotreDame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */