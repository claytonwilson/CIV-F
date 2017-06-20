/*    */ package com.avrgaming.civcraft.structure;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.config.ConfigTempleSacrifice;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.EntityType;
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
/*    */ public class Temple
/*    */   extends Structure
/*    */ {
/*    */   protected Temple(Location center, String id, Town town)
/*    */     throws CivException
/*    */   {
/* 37 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   public Temple(ResultSet rs) throws SQLException, CivException {
/* 41 */     super(rs);
/*    */   }
/*    */   
/*    */   public String getDynmapDescription()
/*    */   {
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public String getMarkerIconName()
/*    */   {
/* 51 */     return "church";
/*    */   }
/*    */   
/*    */   public void onEntitySacrifice(EntityType entityType)
/*    */   {
/* 56 */     ConfigTempleSacrifice sac = null;
/* 57 */     for (ConfigTempleSacrifice s : CivSettings.templeSacrifices) {
/* 58 */       for (String str : s.entites) {
/* 59 */         if (str.equalsIgnoreCase(entityType.toString())) {
/* 60 */           sac = s;
/* 61 */           break;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 66 */     if (sac == null) {
/* 67 */       return;
/*    */     }
/*    */     
/* 70 */     getTown().addAccumulatedCulture(sac.reward);
/* 71 */     CivMessage.sendTown(getTown(), "Our Sacrifice has awarded our town §d" + sac.reward + "§f" + " culture.");
/* 72 */     getTown().save();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Temple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */