/*    */ package pvptimer;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.util.DateUtil;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PvPTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 18 */     for (Resident resident : ) {
/* 19 */       if (resident.isProtected())
/*    */       {
/*    */ 
/*    */         try
/*    */         {
/*    */ 
/* 25 */           int mins = CivSettings.getInteger(CivSettings.civConfig, "global.pvp_timer").intValue();
/* 26 */           if (DateUtil.isAfterMins(new Date(resident.getRegistered()), mins))
/*    */           {
/* 28 */             resident.setisProtected(false);
/* 29 */             CivMessage.send(resident, "§7Your PvP protection has expired.");
/*    */           }
/*    */         } catch (InvalidConfiguration e) {
/* 32 */           e.printStackTrace();
/* 33 */           return;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\pvptimer\PvPTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */