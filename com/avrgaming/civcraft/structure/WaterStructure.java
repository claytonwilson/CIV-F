/*    */ package com.avrgaming.civcraft.structure;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.bukkit.Chunk;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.Player;
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
/*    */ public class WaterStructure
/*    */   extends Structure
/*    */ {
/* 33 */   public static int WATER_LEVEL = 62;
/* 34 */   public static int TOLERANCE = 20;
/*    */   
/*    */   public WaterStructure(ResultSet rs) throws SQLException, CivException {
/* 37 */     super(rs);
/*    */   }
/*    */   
/*    */   protected WaterStructure(Location center, String id, Town town) throws CivException
/*    */   {
/* 42 */     super(center, id, town);
/*    */   }
/*    */   
/*    */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size)
/*    */   {
/* 47 */     Location loc = new Location(center.getWorld(), 
/* 48 */       center.getX(), center.getY(), center.getZ(), 
/* 49 */       center.getYaw(), center.getPitch());
/*    */     
/*    */ 
/* 52 */     if (isTileImprovement())
/*    */     {
/* 54 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*    */ 
/*    */     }
/* 57 */     else if (dir.equalsIgnoreCase("east")) {
/* 58 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 59 */       loc.setX(loc.getX() + 0.0D);
/*    */     }
/* 61 */     else if (dir.equalsIgnoreCase("west")) {
/* 62 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 63 */       loc.setX(loc.getX() - (0.0D + x_size));
/*    */ 
/*    */     }
/* 66 */     else if (dir.equalsIgnoreCase("north")) {
/* 67 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 68 */       loc.setZ(loc.getZ() - (0.0D + z_size));
/*    */     }
/* 70 */     else if (dir.equalsIgnoreCase("south")) {
/* 71 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 72 */       loc.setZ(loc.getZ() + 0.0D);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 77 */     if (getTemplateYShift() != 0)
/*    */     {
/* 79 */       loc.setY(WATER_LEVEL + getTemplateYShift());
/*    */     }
/*    */     
/* 82 */     return loc;
/*    */   }
/*    */   
/*    */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ, Location savedLocation) throws CivException
/*    */   {
/* 87 */     super.checkBlockPermissionsAndRestrictions(player, centerBlock, regionX, regionY, regionZ, savedLocation);
/*    */     
/* 89 */     if (player.getLocation().getBlockY() - WATER_LEVEL > TOLERANCE) {
/* 90 */       throw new CivException("You must be close to the water's surface to build this structure.");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String getMarkerIconName()
/*    */   {
/* 97 */     return "anchor";
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\WaterStructure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */