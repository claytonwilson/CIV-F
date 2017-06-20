/*    */ package com.avrgaming.civcraft.nocheat;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import fr.neatmonster.nocheatplus.checks.CheckType;
/*    */ import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
/*    */ import fr.neatmonster.nocheatplus.hooks.AbstractNCPHook;
/*    */ import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class NoCheatPlusSurvialFlyHandler
/*    */   extends AbstractNCPHook
/*    */ {
/*    */   public static void init()
/*    */   {
/* 16 */     NCPHookManager.addHook(CheckType.MOVING_SURVIVALFLY, new NoCheatPlusSurvialFlyHandler());
/*    */   }
/*    */   
/*    */   public String getHookName()
/*    */   {
/* 21 */     return "CivCraft:" + getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   public String getHookVersion()
/*    */   {
/* 26 */     return "1.0";
/*    */   }
/*    */   
/*    */   public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo info)
/*    */   {
/*    */     try
/*    */     {
/* 33 */       violationGrace = CivSettings.getDouble(CivSettings.nocheatConfig, "nocheatplus.survivalfly.violation_grace");
/*    */     } catch (InvalidConfiguration e) { double violationGrace;
/* 35 */       e.printStackTrace();
/* 36 */       return false;
/*    */     }
/*    */     double violationGrace;
/* 39 */     if (info.getAddedVl() < violationGrace) {
/* 40 */       return true;
/*    */     }
/*    */     
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\nocheat\NoCheatPlusSurvialFlyHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */