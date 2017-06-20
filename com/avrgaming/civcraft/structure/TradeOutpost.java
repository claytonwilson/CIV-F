/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class TradeOutpost
/*     */   extends Structure
/*     */ {
/*     */   protected BlockCoord tradeGoodCoord;
/*  52 */   protected BlockCoord tradeOutpostTower = null;
/*  53 */   protected ItemFrameStorage frameStore = null;
/*  54 */   protected TradeGood good = null;
/*  55 */   protected BonusGoodie goodie = null;
/*     */   
/*     */   protected TradeOutpost(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  60 */     super(center, id, town);
/*  61 */     loadSettings();
/*     */   }
/*     */   
/*     */   public TradeOutpost(ResultSet rs) throws SQLException, CivException {
/*  65 */     super(rs);
/*  66 */     loadSettings();
/*     */   }
/*     */   
/*     */ 
/*     */   public void loadSettings() {}
/*     */   
/*     */ 
/*     */   public void checkForTradeGood(BlockCoord coord) {}
/*     */   
/*     */   public BlockCoord getTradeGoodCoord()
/*     */   {
/*  77 */     return this.tradeGoodCoord;
/*     */   }
/*     */   
/*     */   public void setTradeGoodCoord(BlockCoord tradeGoodCoord) {
/*  81 */     this.tradeGoodCoord = tradeGoodCoord;
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  91 */     return "scales";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDemolish()
/*     */     throws CivException
/*     */   {
/* 101 */     if (this.frameStore == null) {
/* 102 */       return;
/*     */     }
/*     */     
/* 105 */     ItemStack frameItem = this.frameStore.getItem();
/* 106 */     if (frameItem != null) {
/* 107 */       BonusGoodie goodie = CivGlobal.getBonusGoodie(frameItem);
/* 108 */       if ((goodie != null) && 
/* 109 */         (goodie.getOutpost() == this)) {
/* 110 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 115 */     throw new CivException("Cannot demolish when bonus goodie is not in item frame.");
/*     */   }
/*     */   
/*     */   public void build_trade_outpost(Location centerLoc)
/*     */     throws CivException
/*     */   {
/* 121 */     TradeGood good = CivGlobal.getTradeGood(this.tradeGoodCoord);
/* 122 */     if (good == null) {
/* 123 */       throw new CivException("Couldn't find trade good at location:" + good);
/*     */     }
/*     */     
/* 126 */     if (good.getInfo().water) {
/* 127 */       throw new CivException("Trade Outposts cannot be built on water goods.");
/*     */     }
/*     */     
/* 130 */     if (good.getTown() != null) {
/* 131 */       throw new CivException("Good is already claimed.");
/*     */     }
/*     */     
/* 134 */     good.setStruct(this);
/* 135 */     good.setTown(getTown());
/* 136 */     good.setCiv(getTown().getCiv());
/*     */     
/*     */ 
/* 139 */     setGood(good);
/*     */   }
/*     */   
/*     */ 
/*     */   public void build_trade_outpost_tower()
/*     */     throws CivException
/*     */   {
/* 146 */     TradeGood good = this.good;
/* 147 */     if (good == null) {
/* 148 */       throw new CivException("Couldn't find trade good at location:" + good);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 153 */     if (this.tradeOutpostTower == null) {
/* 154 */       throw new CivException("Couldn't find trade outpost tower.");
/*     */     }
/*     */     
/* 157 */     Location centerLoc = this.tradeOutpostTower.getLocation();
/*     */     
/*     */ 
/* 160 */     for (int i = 0; i < 3; i++) {
/* 161 */       Block b = centerLoc.getBlock().getRelative(0, i, 0);
/* 162 */       ItemManager.setTypeId(b, 7);ItemManager.setData(b, 0);
/*     */       
/* 164 */       StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 165 */       addStructureBlock(sb.getCoord(), false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 170 */     Block b = centerLoc.getBlock().getRelative(1, 2, 0);
/* 171 */     ItemManager.setTypeId(b, 68);
/* 172 */     ItemManager.setData(b, 5);
/* 173 */     Sign s = (Sign)b.getState();
/* 174 */     s.setLine(0, good.getInfo().name);
/* 175 */     s.update();
/* 176 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/*     */     
/* 178 */     addStructureBlock(sb.getCoord(), false);
/*     */     
/*     */ 
/* 181 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 182 */     addStructureBlock(new BlockCoord(b), false);
/* 183 */     Block b2 = b.getRelative(1, 0, 0);
/* 184 */     Entity entity = CivGlobal.getEntityAtLocation(b2.getLocation());
/* 185 */     addStructureBlock(new BlockCoord(b2), false);
/*     */     
/* 187 */     if ((entity == null) || (!(entity instanceof ItemFrame))) {
/* 188 */       this.frameStore = new ItemFrameStorage(b.getLocation(), BlockFace.EAST);
/*     */     } else {
/* 190 */       this.frameStore = new ItemFrameStorage((ItemFrame)entity, b.getLocation());
/*     */     }
/*     */     
/* 193 */     this.frameStore.setBuildable(this);
/*     */   }
/*     */   
/*     */   public ItemFrameStorage getItemFrameStore() {
/* 197 */     return this.frameStore;
/*     */   }
/*     */   
/*     */   public BlockCoord getTradeOutpostTower() {
/* 201 */     return this.tradeOutpostTower;
/*     */   }
/*     */   
/*     */   public void setTradeOutpostTower(BlockCoord tradeOutpostTower) {
/* 205 */     this.tradeOutpostTower = tradeOutpostTower;
/*     */   }
/*     */   
/*     */   public TradeGood getGood() {
/* 209 */     return this.good;
/*     */   }
/*     */   
/*     */   public void setGood(TradeGood good) {
/* 213 */     this.good = good;
/*     */   }
/*     */   
/*     */   public BonusGoodie getGoodie() {
/* 217 */     return this.goodie;
/*     */   }
/*     */   
/*     */   public void setGoodie(BonusGoodie goodie) {
/* 221 */     this.goodie = goodie;
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 226 */     if (this.goodie != null) {
/* 227 */       this.goodie.delete();
/*     */     }
/*     */     
/* 230 */     super.delete();
/*     */   }
/*     */   
/*     */   public void onDestroy()
/*     */   {
/* 235 */     super.onDestroy();
/*     */     
/* 237 */     if (this.goodie != null) {
/*     */       try {
/* 239 */         this.goodie.delete();
/*     */       } catch (SQLException e) {
/* 241 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public void onComplete()
/*     */   {
/* 259 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 253 */           TradeOutpost.this.createTradeGood();
/*     */         } catch (CivException e) {
/* 255 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/* 259 */     }, 20L);
/*     */   }
/*     */   
/*     */   public void createTradeGood() throws CivException
/*     */   {
/* 264 */     if (!isActive()) {
/* 265 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 274 */       this.goodie = new BonusGoodie(this);
/*     */       
/* 276 */       if (this.goodie.getFrame() == null)
/*     */       {
/* 278 */         return;
/*     */       }
/*     */       
/* 281 */       TownHall townhall = this.goodie.getFrame().getTown().getTownHall();
/* 282 */       if (townhall != null) {
/* 283 */         for (ItemFrameStorage ifs : townhall.getGoodieFrames()) {
/* 284 */           if (ifs.getFrameID() == this.goodie.getFrame().getFrameID()) {
/* 285 */             townhall.getTown().loadGoodiePlaceIntoFrame(townhall, this.goodie);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (SQLException e) {
/* 290 */       e.printStackTrace();
/* 291 */       throw new CivException("Internal database error.");
/*     */     } catch (InvalidNameException e) {
/* 293 */       e.printStackTrace();
/* 294 */       throw new CivException("Invalid name exception.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onLoad() throws CivException
/*     */   {
/* 300 */     createTradeGood();
/*     */   }
/*     */   
/*     */   public void fancyDestroyStructureBlocks() {
/* 304 */     for (BlockCoord coord : this.structureBlocks.keySet())
/*     */     {
/* 306 */       if (CivGlobal.getStructureChest(coord) == null)
/*     */       {
/*     */ 
/*     */ 
/* 310 */         if (CivGlobal.getStructureSign(coord) == null)
/*     */         {
/*     */ 
/*     */ 
/* 314 */           if ((ItemManager.getId(coord.getBlock()) != 7) && (ItemManager.getId(coord.getBlock()) != 0))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 319 */             Random rand = new Random();
/*     */             
/*     */ 
/* 322 */             if (rand.nextInt(100) <= 10) {
/* 323 */               ItemManager.setTypeId(coord.getBlock(), 13);
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/* 328 */             else if (rand.nextInt(100) <= 50) {
/* 329 */               ItemManager.setTypeId(coord.getBlock(), 51);
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/* 334 */             else if (rand.nextInt(100) <= 1) {
/* 335 */               FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withColor(Color.RED).withTrail().withFlicker().build();
/* 336 */               FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 337 */               for (int i = 0; i < 3; i++) {
/*     */                 try {
/* 339 */                   fePlayer.playFirework(coord.getBlock().getWorld(), coord.getLocation(), effect);
/*     */                 } catch (Exception e) {
/* 341 */                   e.printStackTrace();
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\TradeOutpost.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */