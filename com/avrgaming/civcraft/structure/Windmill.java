/*    */ package com.avrgaming.civcraft.structure;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.WindmillStartSyncTask;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Windmill
/*    */   extends Structure
/*    */ {
/*    */   public static enum CropType
/*    */   {
/* 34 */     WHEAT, 
/* 35 */     CARROTS, 
/* 36 */     POTATOES;
/*    */   }
/*    */   
/*    */   public Windmill(ResultSet rs) throws SQLException, CivException {
/* 40 */     super(rs);
/*    */   }
/*    */   
/*    */   public Windmill(Location center, String id, Town town) throws CivException
/*    */   {
/* 45 */     super(center, id, town);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onEffectEvent() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void processWindmill()
/*    */   {
/* 56 */     TaskMaster.syncTask(new WindmillStartSyncTask(this), 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Windmill.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */