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
/*    */ public class Monument
/*    */   extends Structure
/*    */ {
/*    */   protected Monument(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 33 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public Monument(ResultSet rs) throws SQLException, CivException {
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
/* 47 */     return "building";
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Monument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */