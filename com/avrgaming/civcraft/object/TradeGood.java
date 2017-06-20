/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class TradeGood
/*     */   extends SQLObject
/*     */ {
/*     */   private ConfigTradeGood info;
/*     */   private Town town;
/*     */   private Civilization civ;
/*     */   private BlockCoord coord;
/*     */   private BlockCoord bonusLocation;
/*     */   private Structure struct;
/*     */   public static final String TABLE_NAME = "TRADE_GOODS";
/*     */   
/*     */   public TradeGood(ConfigTradeGood good, BlockCoord coord)
/*     */   {
/*  48 */     this.info = good;
/*  49 */     this.coord = coord;
/*     */     try {
/*  51 */       setName(good.id);
/*     */     } catch (InvalidNameException e) {
/*  53 */       e.printStackTrace();
/*     */     }
/*     */     
/*  56 */     this.town = null;
/*  57 */     this.civ = null;
/*     */   }
/*     */   
/*     */   public TradeGood(ResultSet rs) throws SQLException, InvalidNameException {
/*  61 */     load(rs);
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/*  66 */     if (!SQL.hasTable("TRADE_GOODS")) {
/*  67 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "TRADE_GOODS" + " (" + 
/*  68 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  69 */         "`name` VARCHAR(64) NOT NULL," + 
/*  70 */         "`town_id` int(11)," + 
/*  71 */         "`structure_id` int(11), " + 
/*  72 */         "`coord` mediumtext DEFAULT NULL," + 
/*  73 */         "`bonusLocation` mediumtext DEFAULT NULL," + 
/*     */         
/*  75 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  77 */       SQL.makeTable(table_create);
/*  78 */       CivLog.info("Created TRADE_GOODS table");
/*     */     } else {
/*  80 */       CivLog.info("TRADE_GOODS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException
/*     */   {
/*  87 */     setId(rs.getInt("id"));
/*  88 */     setName(rs.getString("name"));
/*  89 */     setInfo((ConfigTradeGood)CivSettings.goods.get(getName()));
/*  90 */     setTown(CivGlobal.getTownFromId(rs.getInt("town_id")));
/*  91 */     String bonusLocation = rs.getString("bonusLocation");
/*  92 */     if (bonusLocation != null) {
/*  93 */       this.bonusLocation = new BlockCoord(bonusLocation);
/*     */     } else {
/*  95 */       this.bonusLocation = null;
/*     */     }
/*     */     
/*  98 */     this.coord = new BlockCoord(rs.getString("coord"));
/*  99 */     addProtectedBlocks(this.coord);
/*     */     
/* 101 */     setStruct(CivGlobal.getStructureById(rs.getInt("structure_id")));
/*     */     
/* 103 */     if ((getStruct() != null) && 
/* 104 */       ((this.struct instanceof TradeOutpost))) {
/* 105 */       TradeOutpost outpost = (TradeOutpost)this.struct;
/* 106 */       outpost.setGood(this);
/*     */     }
/*     */     
/*     */ 
/* 110 */     if (getTown() != null) {
/* 111 */       this.civ = getTown().getCiv();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addProtectedBlocks(BlockCoord coord2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save()
/*     */   {
/* 129 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 134 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 136 */     hashmap.put("name", getName());
/* 137 */     if (getTown() != null) {
/* 138 */       hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/*     */     } else {
/* 140 */       hashmap.put("town_id", null);
/*     */     }
/* 142 */     if (this.bonusLocation != null) {
/* 143 */       hashmap.put("bonusLocation", this.bonusLocation.toString());
/*     */     } else {
/* 145 */       hashmap.put("bonusLocation", null);
/*     */     }
/* 147 */     hashmap.put("coord", this.coord.toString());
/* 148 */     if (getStruct() == null) {
/* 149 */       hashmap.put("structure_id", null);
/*     */     } else {
/* 151 */       hashmap.put("structure_id", Integer.valueOf(getStruct().getId()));
/*     */     }
/*     */     
/*     */ 
/* 155 */     SQL.updateNamedObject(this, hashmap, "TRADE_GOODS");
/*     */   }
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */   public Town getTown()
/*     */   {
/* 164 */     return this.town;
/*     */   }
/*     */   
/*     */   public void setTown(Town town)
/*     */   {
/* 169 */     this.town = town;
/*     */   }
/*     */   
/*     */   public Civilization getCiv()
/*     */   {
/* 174 */     return this.civ;
/*     */   }
/*     */   
/*     */   public void setCiv(Civilization civ)
/*     */   {
/* 179 */     this.civ = civ;
/*     */   }
/*     */   
/*     */   public ConfigTradeGood getInfo()
/*     */   {
/* 184 */     return this.info;
/*     */   }
/*     */   
/*     */   public void setInfo(ConfigTradeGood info)
/*     */   {
/* 189 */     this.info = info;
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord()
/*     */   {
/* 194 */     return this.coord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 199 */   public void setCoord(BlockCoord coord) { this.coord = coord; }
/*     */   
/*     */   public static double getBaseValue(TradeGood good) {
/* 202 */     ConfigTradeGood configTradeGood = good.getInfo();
/* 203 */     double value = configTradeGood.value;
/* 204 */     return value;
/*     */   }
/*     */   
/*     */   public static int getTradeGoodCount(BonusGoodie goodie, Town town) {
/* 208 */     int amount = 0;
/*     */     
/* 210 */     for (BonusGoodie g : town.getBonusGoodies()) {
/* 211 */       if (goodie.getDisplayName().equals(g.getDisplayName())) {
/* 212 */         amount++;
/*     */       }
/*     */     }
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
/* 227 */     return amount;
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
/*     */ 
/*     */ 
/*     */   private static double getTradeGoodIncomeBonus(TradeGood good, Town town)
/*     */   {
/* 245 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public static double getTradeGoodValue(BonusGoodie goodie, Town town)
/*     */   {
/* 250 */     TradeGood good = goodie.getOutpost().getGood();
/* 251 */     double value = getBaseValue(good);
/*     */     try
/*     */     {
/* 254 */       goodMax = CivSettings.getInteger(CivSettings.goodsConfig, "trade_good_multiplier_max").intValue();
/*     */     } catch (InvalidConfiguration e) { int goodMax;
/* 256 */       e.printStackTrace();
/* 257 */       return 0.0D; }
/*     */     int goodMax;
/* 259 */     int effectiveCount = getTradeGoodCount(goodie, town);
/* 260 */     effectiveCount--;
/*     */     
/* 262 */     if (effectiveCount > goodMax) {
/* 263 */       effectiveCount = goodMax;
/*     */     }
/*     */     
/* 266 */     double rate = 1.0D + 0.5D * effectiveCount;
/*     */     
/*     */ 
/* 269 */     rate += getTradeGoodIncomeBonus(good, town);
/*     */     
/* 271 */     value *= rate;
/* 272 */     return value;
/*     */   }
/*     */   
/*     */   public static double getTownBaseGoodPaymentViaGoodie(Town town)
/*     */   {
/* 277 */     double total_payment = 0.0D;
/*     */     
/* 279 */     for (BonusGoodie goodie : town.getBonusGoodies()) {
/* 280 */       TradeOutpost outpost = goodie.getOutpost();
/* 281 */       if (outpost != null)
/*     */       {
/*     */ 
/*     */ 
/* 285 */         CultureChunk cc = CivGlobal.getCultureChunk(outpost.getCorner().getLocation());
/* 286 */         if (cc != null)
/*     */         {
/*     */ 
/*     */ 
/* 290 */           if (outpost.isActive())
/*     */           {
/*     */ 
/*     */ 
/* 294 */             double payment = getTradeGoodValue(goodie, town);
/* 295 */             total_payment += payment;
/*     */           } }
/*     */       } }
/* 298 */     return total_payment;
/*     */   }
/*     */   
/*     */   public static double getTownTradePayment(Town town) {
/* 302 */     double total_payment = getTownBaseGoodPaymentViaGoodie(town);
/* 303 */     total_payment *= town.getTradeRate();
/*     */     
/*     */ 
/*     */ 
/* 307 */     return total_payment;
/*     */   }
/*     */   
/*     */   public Structure getStruct()
/*     */   {
/* 312 */     return this.struct;
/*     */   }
/*     */   
/*     */   public void setStruct(Structure struct)
/*     */   {
/* 317 */     this.struct = struct;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\TradeGood.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */