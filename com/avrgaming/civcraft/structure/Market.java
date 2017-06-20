/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMarketItem;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
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
/*     */ public class Market
/*     */   extends Structure
/*     */ {
/*  50 */   public HashMap<Integer, LinkedList<StructureSign>> signIndex = new HashMap();
/*     */   
/*  52 */   public static int BULK_AMOUNT = 64;
/*     */   
/*     */   protected Market(Location center, String id, Town town) throws CivException {
/*  55 */     super(center, id, town);
/*  56 */     CivGlobal.addMarket(this);
/*     */   }
/*     */   
/*     */   public Market(ResultSet rs) throws SQLException, CivException {
/*  60 */     super(rs);
/*  61 */     CivGlobal.addMarket(this);
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/*  66 */     super.delete();
/*  67 */     CivGlobal.removeMarket(this);
/*     */   }
/*     */   
/*     */   public static void globalSignUpdate(int id) {
/*  71 */     for (Market market : )
/*     */     {
/*  73 */       LinkedList<StructureSign> signs = (LinkedList)market.signIndex.get(Integer.valueOf(id));
/*  74 */       if (signs != null)
/*     */       {
/*     */ 
/*     */ 
/*  78 */         for (StructureSign sign : signs) {
/*  79 */           ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(Integer.valueOf(id));
/*  80 */           if (item != null) {
/*     */             try {
/*  82 */               market.setSignText(sign, item);
/*     */             } catch (ClassCastException e) {
/*  84 */               CivLog.error("Can't cast structure sign to sign for market update.");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void processBuy(Player player, Resident resident, int bulkCount, ConfigMarketItem item) throws CivException {
/*  93 */     item.buy(resident, player, bulkCount);
/*     */   }
/*     */   
/*     */   public void processSell(Player player, Resident resident, int bulkCount, ConfigMarketItem item) throws CivException {
/*  97 */     item.sell(resident, player, bulkCount);
/*     */   }
/*     */   
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */     throws CivException
/*     */   {
/* 104 */     Integer id = Integer.valueOf(sign.getType());
/* 105 */     ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(id);
/* 106 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 108 */     if (resident == null) {
/* 109 */       CivMessage.sendError(player, "You're not registerd?? what??");
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     if (item == null) {
/* 114 */       CivMessage.sendError(player, "ERROR: Unknown item. Market ID:" + id); return;
/*     */     }
/*     */     
/*     */     String str;
/* 118 */     switch ((str = sign.getAction().toLowerCase()).hashCode()) {case -1377556294:  if (str.equals("buybig")) {} break; case 97926:  if (str.equals("buy")) {} break; case 3526482:  if (str.equals("sell")) break;  case 1978307694:  if ((goto 229) && (str.equals("sellbig")))
/*     */       {
/* 120 */         processSell(player, resident, BULK_AMOUNT, item);
/*     */         
/*     */         break label229;
/* 123 */         processSell(player, resident, 1, item);
/*     */         
/*     */         break label229;
/* 126 */         processBuy(player, resident, 1, item);
/*     */         
/*     */         break label229;
/* 129 */         processBuy(player, resident, BULK_AMOUNT, item);
/*     */       }
/*     */       break; }
/*     */     label229:
/* 133 */     player.updateInventory();
/* 134 */     globalSignUpdate(id.intValue());
/*     */   }
/*     */   
/*     */   public void setSignText(StructureSign sign, ConfigMarketItem item) { String itemColor;
/*     */     String itemColor;
/*     */     String itemColor;
/* 140 */     switch (item.lastaction) {
/*     */     case NEUTRAL: 
/* 142 */       itemColor = "§a";
/* 143 */       break;
/*     */     case SELL: 
/* 145 */       itemColor = "§c";
/* 146 */       break;
/*     */     default: 
/* 148 */       itemColor = "§0";
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*     */       String str1;
/* 154 */       switch ((str1 = sign.getAction().toLowerCase()).hashCode()) {case -1377556294:  if (str1.equals("buybig")) {} break; case 97926:  if (str1.equals("buy")) {} break; case 3526482:  if (str1.equals("sell")) break;  case 1978307694:  if ((goto 667) && (str1.equals("sellbig")))
/*     */         {
/* 156 */           Sign s = (Sign)sign.getCoord().getBlock().getState();
/* 157 */           s.setLine(0, ChatColor.BOLD + "Sell Bulk");
/* 158 */           s.setLine(1, item.name);
/* 159 */           s.setLine(2, itemColor + item.getSellCostForAmount(BULK_AMOUNT) + " Coins");
/* 160 */           s.setLine(3, "Amount " + BULK_AMOUNT);
/* 161 */           s.update();
/* 162 */           return;
/*     */           
/* 164 */           Sign s = (Sign)sign.getCoord().getBlock().getState();
/* 165 */           s.setLine(0, ChatColor.BOLD + "Sell");
/* 166 */           s.setLine(1, item.name);
/* 167 */           s.setLine(2, itemColor + item.getSellCostForAmount(1) + " Coins");
/* 168 */           s.setLine(3, "Amount 1");
/* 169 */           s.update();
/* 170 */           return;
/*     */           
/* 172 */           Sign s = (Sign)sign.getCoord().getBlock().getState();
/* 173 */           s.setLine(0, ChatColor.BOLD + "Buy");
/* 174 */           s.setLine(1, item.name);
/* 175 */           s.setLine(2, itemColor + item.getBuyCostForAmount(1) + " Coins");
/* 176 */           s.setLine(3, "Amount 1");
/* 177 */           s.update();
/* 178 */           return;
/*     */           
/* 180 */           Sign s = (Sign)sign.getCoord().getBlock().getState();
/* 181 */           s.setLine(0, ChatColor.BOLD + "Buy Bulk");
/* 182 */           s.setLine(1, item.name);
/* 183 */           s.setLine(2, itemColor + item.getBuyCostForAmount(BULK_AMOUNT) + " Coins");
/* 184 */           s.setLine(3, "Amount " + BULK_AMOUNT);
/* 185 */           s.update();
/*     */         }
/*     */         break; }
/*     */     } catch (Exception e) {
/* 189 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildSign(String action, Integer id, BlockCoord absCoord, ConfigMarketItem item, SimpleBlock commandBlock)
/*     */   {
/* 195 */     Block b = absCoord.getBlock();
/*     */     
/* 197 */     ItemManager.setTypeIdAndData(b, ItemManager.getId(Material.WALL_SIGN), (byte)commandBlock.getData(), false);
/*     */     
/* 199 */     StructureSign structSign = CivGlobal.getStructureSign(absCoord);
/* 200 */     if (structSign == null) {
/* 201 */       structSign = new StructureSign(absCoord, this);
/*     */     }
/*     */     
/* 204 */     structSign.setDirection(ItemManager.getData(b.getState()));
/* 205 */     structSign.setType(id);
/* 206 */     structSign.setAction(action);
/*     */     
/* 208 */     structSign.setOwner(this);
/* 209 */     addStructureSign(structSign);
/* 210 */     CivGlobal.addStructureSign(structSign);
/*     */     
/* 212 */     LinkedList<StructureSign> signs = (LinkedList)this.signIndex.get(id);
/* 213 */     if (signs == null) {
/* 214 */       signs = new LinkedList();
/*     */     }
/*     */     
/* 217 */     signs.add(structSign);
/* 218 */     this.signIndex.put(id, signs);
/* 219 */     setSignText(structSign, item);
/* 220 */     structSign.save();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock)
/*     */   {
/*     */     String str;
/* 227 */     switch ((str = commandBlock.command.toLowerCase().trim()).hashCode()) {case -2008952481:  if (str.equals("/sellbig")) break; break; case 1498103:  if (str.equals("/buy")) {} break; case 46931969:  if (str.equals("/sell")) {} break; case 1680411049:  if (!str.equals("/buybig"))
/*     */       {
/* 229 */         return;Integer id = Integer.valueOf((String)commandBlock.keyvalues.get("id"));
/* 230 */         ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(id);
/* 231 */         if ((item != null) && 
/* 232 */           (item.isStackable())) {
/* 233 */           buildSign("sellbig", id, absCoord, item, commandBlock);
/*     */           
/*     */ 
/* 236 */           return;
/*     */           
/* 238 */           Integer id = Integer.valueOf((String)commandBlock.keyvalues.get("id"));
/*     */           
/* 240 */           ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(id);
/* 241 */           if (item != null) {
/* 242 */             buildSign("sell", id, absCoord, item, commandBlock);
/*     */             
/* 244 */             return;
/*     */             
/* 246 */             Integer id = Integer.valueOf((String)commandBlock.keyvalues.get("id"));
/* 247 */             ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(id);
/* 248 */             if (item != null)
/* 249 */               buildSign("buy", id, absCoord, item, commandBlock);
/*     */           }
/*     */         }
/*     */       } else {
/* 253 */         Integer id = Integer.valueOf((String)commandBlock.keyvalues.get("id"));
/* 254 */         ConfigMarketItem item = (ConfigMarketItem)CivSettings.marketItems.get(id);
/* 255 */         if ((item != null) && 
/* 256 */           (item.isStackable())) {
/* 257 */           buildSign("buybig", id, absCoord, item, commandBlock);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Market.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */