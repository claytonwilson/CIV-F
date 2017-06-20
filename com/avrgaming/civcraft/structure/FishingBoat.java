/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FishingBoat
/*     */   extends TradeOutpost
/*     */ {
/*  49 */   public static int WATER_LEVEL = 62;
/*  50 */   public static int TOLERANCE = 20;
/*     */   
/*     */   protected FishingBoat(Location center, String id, Town town) throws CivException
/*     */   {
/*  54 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   protected FishingBoat(ResultSet rs) throws SQLException, CivException {
/*  58 */     super(rs);
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  63 */     return "anchor";
/*     */   }
/*     */   
/*     */ 
/*     */   public void build_trade_outpost(Location centerLoc)
/*     */     throws CivException
/*     */   {
/*  70 */     TradeGood good = CivGlobal.getTradeGood(this.tradeGoodCoord);
/*  71 */     if (good == null) {
/*  72 */       throw new CivException("Couldn't find trade good at location:" + good);
/*     */     }
/*     */     
/*  75 */     if (!good.getInfo().water) {
/*  76 */       throw new CivException("Fishing boats can only be built on water goods.");
/*     */     }
/*     */     
/*  79 */     if (good.getTown() != null) {
/*  80 */       throw new CivException("Good is already claimed.");
/*     */     }
/*     */     
/*  83 */     good.setStruct(this);
/*  84 */     good.setTown(getTown());
/*  85 */     good.setCiv(getTown().getCiv());
/*     */     
/*  87 */     setGood(good);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void build_trade_outpost_tower()
/*     */     throws CivException
/*     */   {
/*  95 */     TradeGood good = this.good;
/*  96 */     if (good == null) {
/*  97 */       throw new CivException("Couldn't find trade good at location:" + good);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 102 */     if (this.tradeOutpostTower == null) {
/* 103 */       throw new CivException("Couldn't find trade outpost tower.");
/*     */     }
/*     */     
/* 106 */     Location centerLoc = this.tradeOutpostTower.getLocation();
/*     */     
/*     */ 
/* 109 */     for (int i = 0; i < 3; i++) {
/* 110 */       Block b = centerLoc.getBlock().getRelative(0, i, 0);
/* 111 */       ItemManager.setTypeId(b, 7);ItemManager.setData(b, 0);
/*     */       
/* 113 */       StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 114 */       addStructureBlock(sb.getCoord(), false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 119 */     Block b = centerLoc.getBlock().getRelative(1, 2, 0);
/* 120 */     ItemManager.setTypeId(b, 68);
/* 121 */     ItemManager.setData(b, 5);
/* 122 */     Sign s = (Sign)b.getState();
/* 123 */     s.setLine(0, good.getInfo().name);
/* 124 */     s.update();
/* 125 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/*     */     
/* 127 */     addStructureBlock(sb.getCoord(), false);
/*     */     
/*     */ 
/* 130 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 131 */     addStructureBlock(new BlockCoord(b), false);
/* 132 */     Block b2 = b.getRelative(1, 0, 0);
/* 133 */     Entity entity = CivGlobal.getEntityAtLocation(b2.getLocation());
/*     */     
/* 135 */     if ((entity == null) || (!(entity instanceof ItemFrame))) {
/* 136 */       this.frameStore = new ItemFrameStorage(b.getLocation(), BlockFace.EAST);
/*     */     } else {
/* 138 */       this.frameStore = new ItemFrameStorage((ItemFrame)entity, b.getLocation());
/*     */     }
/*     */     
/* 141 */     this.frameStore.setBuildable(this);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Location repositionCenter(Location center, String dir, double x_size, double z_size)
/*     */   {
/* 147 */     Location loc = new Location(center.getWorld(), 
/* 148 */       center.getX(), center.getY(), center.getZ(), 
/* 149 */       center.getYaw(), center.getPitch());
/*     */     
/*     */ 
/* 152 */     if (isTileImprovement())
/*     */     {
/* 154 */       loc = center.getChunk().getBlock(0, center.getBlockY(), 0).getLocation();
/*     */ 
/*     */     }
/* 157 */     else if (dir.equalsIgnoreCase("east")) {
/* 158 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 159 */       loc.setX(loc.getX() + 0.0D);
/*     */     }
/* 161 */     else if (dir.equalsIgnoreCase("west")) {
/* 162 */       loc.setZ(loc.getZ() - z_size / 2.0D);
/* 163 */       loc.setX(loc.getX() - (0.0D + x_size));
/*     */ 
/*     */     }
/* 166 */     else if (dir.equalsIgnoreCase("north")) {
/* 167 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 168 */       loc.setZ(loc.getZ() - (0.0D + z_size));
/*     */     }
/* 170 */     else if (dir.equalsIgnoreCase("south")) {
/* 171 */       loc.setX(loc.getX() - x_size / 2.0D);
/* 172 */       loc.setZ(loc.getZ() + 0.0D);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 177 */     if (getTemplateYShift() != 0)
/*     */     {
/* 179 */       loc.setY(WATER_LEVEL + getTemplateYShift());
/*     */     }
/*     */     
/* 182 */     return loc;
/*     */   }
/*     */   
/*     */   public void onLoad() throws CivException
/*     */   {
/* 187 */     super.createTradeGood();
/*     */   }
/*     */   
/*     */   protected void checkBlockPermissionsAndRestrictions(Player player, Block centerBlock, int regionX, int regionY, int regionZ, Location savedLocation) throws CivException
/*     */   {
/* 192 */     super.checkBlockPermissionsAndRestrictions(player, centerBlock, regionX, regionY, regionZ, savedLocation);
/*     */     
/* 194 */     if (player.getLocation().getBlockY() - WATER_LEVEL > TOLERANCE) {
/* 195 */       throw new CivException("You must be close to the water's surface to build this structure.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\FishingBoat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */