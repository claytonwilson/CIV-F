/*    */ package com.avrgaming.civcraft.structure.wonders;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.bukkit.Location;
/*    */ 
/*    */ public class CouncilOfEight
/*    */   extends Wonder
/*    */ {
/*    */   public CouncilOfEight(ResultSet rs) throws SQLException, CivException
/*    */   {
/* 14 */     super(rs);
/*    */   }
/*    */   
/*    */   public CouncilOfEight(Location center, String id, Town town) throws CivException {
/* 18 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   protected void removeBuffs() {}
/*    */   
/*    */   protected void addBuffs() {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\CouncilOfEight.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */