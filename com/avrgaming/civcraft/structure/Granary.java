/*    */ package com.avrgaming.civcraft.structure;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class Granary
/*    */   extends Structure
/*    */ {
/*    */   protected Granary(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 33 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public Granary(ResultSet rs) throws SQLException, CivException {
/* 37 */     super(rs);
/*    */   }
/*    */   
/*    */   public String getDynmapDescription()
/*    */   {
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public String getMarkerIconName()
/*    */   {
/* 47 */     return "chest";
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Granary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */