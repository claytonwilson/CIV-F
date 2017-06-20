/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.components.SignSelectionActionInterface;
/*     */ import com.avrgaming.civcraft.components.SignSelectionComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigStableHorse;
/*     */ import com.avrgaming.civcraft.config.ConfigStableItem;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.items.BonusGoodie;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import gpl.HorseModifier;
/*     */ import gpl.HorseModifier.HorseType;
/*     */ import gpl.HorseModifier.HorseVariant;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Horse;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public class Stable
/*     */   extends Structure
/*     */ {
/*  57 */   public static Integer FEE_MIN = Integer.valueOf(5);
/*  58 */   public static Integer FEE_MAX = Integer.valueOf(100);
/*  59 */   private HashMap<Integer, SignSelectionComponent> signSelectors = new HashMap();
/*     */   private BlockCoord horseSpawnCoord;
/*     */   private BlockCoord muleSpawnCoord;
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   
/*     */   public Stable(ResultSet rs) throws SQLException, CivException {
/*  65 */     super(rs);
/*  66 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  67 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   protected Stable(Location center, String id, Town town) throws CivException {
/*  71 */     super(center, id, town);
/*  72 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  73 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   public void loadSettings() {
/*  77 */     super.loadSettings();
/*     */     
/*  79 */     SignSelectionComponent horseVender = new SignSelectionComponent();
/*  80 */     SignSelectionComponent muleVender = new SignSelectionComponent();
/*  81 */     SignSelectionComponent itemVender = new SignSelectionComponent();
/*     */     
/*  83 */     this.signSelectors.put(Integer.valueOf(0), horseVender);
/*  84 */     this.signSelectors.put(Integer.valueOf(1), muleVender);
/*  85 */     this.signSelectors.put(Integer.valueOf(2), itemVender);
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
/*     */ 
/*     */ 
/*     */ 
/* 209 */     for (ConfigStableItem item : CivSettings.stableItems) {
/* 210 */       SignSelectionComponent comp = (SignSelectionComponent)this.signSelectors.get(Integer.valueOf(item.store_id));
/* 211 */       if (comp != null)
/*     */       {
/*     */ 
/* 214 */         if (item.item_id == 0) {
/* 215 */           comp.addItem(new String[] { "§a" + item.name, "Buy For", item.cost, "Fee:" + this.nonMemberFeeComponent.getFeeString() }, new SignSelectionActionInterface()
/*     */           {
/*     */             int horse_id;
/*     */             double cost;
/*     */             
/*     */             public void process(Player player)
/*     */             {
/*  98 */               ConfigStableHorse horse = (ConfigStableHorse)CivSettings.horses.get(Integer.valueOf(this.horse_id));
/*  99 */               if (horse == null) {
/* 100 */                 CivMessage.sendError(player, "Unknown horse ID.");
/* 101 */                 return;
/*     */               }
/*     */               
/* 104 */               Resident resident = CivGlobal.getResident(player);
/* 105 */               if (!horse.mule) {
/* 106 */                 boolean allow = false;
/* 107 */                 for (BonusGoodie goodie : Stable.this.getTown().getBonusGoodies()) {
/* 108 */                   if (goodie.getConfigTradeGood().id.equals("good_horses")) {
/* 109 */                     allow = true;
/* 110 */                     break;
/*     */                   }
/*     */                 }
/*     */                 
/* 114 */                 if (!allow) {
/* 115 */                   CivMessage.sendError(player, "Town does not have any horses. Socket a horse trade good in the town hall."); return;
/*     */                 }
/*     */               }
/*     */               
/*     */               double paid;
/*     */               double paid;
/* 121 */               if (resident.getTown() != Stable.this.getTown()) {
/* 122 */                 if (!resident.getTreasury().hasEnough(Stable.this.getItemCost(this.cost))) {
/* 123 */                   CivMessage.sendError(player, "You do not have the required " + Stable.this.getItemCost(this.cost) + " coins.");
/* 124 */                   return;
/*     */                 }
/*     */                 
/*     */ 
/* 128 */                 resident.getTreasury().withdraw(Stable.this.getItemCost(this.cost));
/* 129 */                 Stable.this.getTown().depositTaxed(Stable.this.getFeeToTown(this.cost));
/* 130 */                 CivMessage.send(player, "§ePaid " + Stable.this.getFeeToTown(this.cost) + " in non-resident fees.");
/* 131 */                 paid = Stable.this.getItemCost(this.cost);
/*     */               } else {
/* 133 */                 if (!resident.getTreasury().hasEnough(this.cost)) {
/* 134 */                   CivMessage.sendError(player, "You do not have the required " + this.cost + " coins.");
/* 135 */                   return;
/*     */                 }
/*     */                 
/* 138 */                 resident.getTreasury().withdraw(this.cost);
/* 139 */                 paid = this.cost;
/*     */               }
/*     */               
/*     */               HorseModifier mod;
/* 143 */               if (!horse.mule) {
/* 144 */                 HorseModifier mod = HorseModifier.spawn(Stable.this.horseSpawnCoord.getLocation());
/* 145 */                 mod.setType(HorseModifier.HorseType.NORMAL);
/*     */               } else {
/* 147 */                 mod = HorseModifier.spawn(Stable.this.muleSpawnCoord.getLocation());
/* 148 */                 mod.setType(HorseModifier.HorseType.MULE);
/*     */               }
/*     */               
/* 151 */               mod.setVariant(HorseModifier.HorseVariant.valueOf(horse.variant));
/* 152 */               HorseModifier.setHorseSpeed(mod.getHorse(), horse.speed);
/* 153 */               ((Horse)mod.getHorse()).setJumpStrength(horse.jump);
/* 154 */               ((Horse)mod.getHorse()).setHealth(horse.health);
/* 155 */               ((Horse)mod.getHorse()).setOwner(player);
/* 156 */               ((Horse)mod.getHorse()).setBaby();
/*     */               
/* 158 */               CivMessage.send(player, "§aPaid " + paid + " coins.");
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
/*     */             }
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
/*     */           });
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
/*     */         }
/*     */         else
/*     */         {
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
/* 217 */           comp.addItem(new String[] { "§a" + item.name, "Buy For", item.cost, "Fee: " + this.nonMemberFeeComponent.getFeeString() }, new SignSelectionActionInterface()
/*     */           {
/*     */             int item_id;
/*     */             double cost;
/*     */             
/*     */             public void process(Player player)
/*     */             {
/* 175 */               Resident resident = CivGlobal.getResident(player);
/*     */               double paid;
/*     */               double paid;
/* 178 */               if (resident.getTown() != Stable.this.getTown()) {
/* 179 */                 if (!resident.getTreasury().hasEnough(Stable.this.getItemCost(this.cost))) {
/* 180 */                   CivMessage.sendError(player, "You do not have the required " + Stable.this.getItemCost(this.cost) + " coins.");
/* 181 */                   return;
/*     */                 }
/*     */                 
/* 184 */                 resident.getTreasury().withdraw(Stable.this.getItemCost(this.cost));
/* 185 */                 CivMessage.send(player, "§ePaid " + Stable.this.getFeeToTown(this.cost) + " in non-resident fees.");
/* 186 */                 paid = Stable.this.getItemCost(this.cost);
/*     */               } else {
/* 188 */                 if (!resident.getTreasury().hasEnough(this.cost)) {
/* 189 */                   CivMessage.sendError(player, "You do not have the required " + this.cost + " coins.");
/* 190 */                   return;
/*     */                 }
/*     */                 
/* 193 */                 resident.getTreasury().withdraw(this.cost);
/* 194 */                 paid = this.cost;
/*     */               }
/*     */               
/* 197 */               HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { ItemManager.createItemStack(this.item_id, 1) });
/* 198 */               if (leftovers.size() > 0) {
/* 199 */                 for (ItemStack stack : leftovers.values()) {
/* 200 */                   player.getWorld().dropItem(player.getLocation(), stack);
/*     */                 }
/*     */               }
/*     */               
/* 204 */               CivMessage.send(player, "§aPaid " + paid + " coins.");
/*     */             }
/*     */           });
/*     */         }
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
/*     */   private double getItemCost(double cost)
/*     */   {
/* 223 */     return cost + getFeeToTown(cost);
/*     */   }
/*     */   
/*     */   private double getFeeToTown(double cost) {
/* 227 */     return cost * this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 232 */     SignSelectionComponent signSelection = (SignSelectionComponent)this.signSelectors.get(Integer.valueOf(sign.getAction()));
/* 233 */     if (signSelection == null) {
/* 234 */       CivLog.warning("No sign seletor component for with id:" + sign.getAction()); return;
/*     */     }
/*     */     
/*     */     String str;
/* 238 */     switch ((str = sign.getType()).hashCode()) {case 3242771:  if (str.equals("item")) {} break; case 3377907:  if (str.equals("next")) break;  case 3449395:  if ((goto 153) && (str.equals("prev")))
/*     */       {
/* 240 */         signSelection.processPrev();
/* 241 */         return;
/*     */         
/* 243 */         signSelection.processNext();
/* 244 */         return;
/*     */         
/* 246 */         signSelection.processAction(player);
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateSignText() {
/* 253 */     for (SignSelectionComponent comp : this.signSelectors.values()) {
/* 254 */       comp.setMessageAllItems(3, "Fee: " + this.nonMemberFeeComponent.getFeeString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock sb)
/*     */   {
/*     */     String str;
/*     */     
/* 264 */     switch ((str = sb.command).hashCode()) {case -1777079507:  if (str.equals("/horsespawn")) {} break; case 46648258:  if (str.equals("/item")) {} break; case 46783394:  if (str.equals("/next")) {} break; case 46854882:  if (str.equals("/prev")) break; break; case 2022437803:  if (!str.equals("/mulespawn"))
/*     */       {
/* 266 */         return;ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 267 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/* 268 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 269 */         structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Prev");
/* 270 */         structSign.setDirection(sb.getData());
/* 271 */         structSign.setAction((String)sb.keyvalues.get("id"));
/* 272 */         structSign.setType("prev");
/* 273 */         structSign.update();
/* 274 */         addStructureSign(structSign);
/* 275 */         CivGlobal.addStructureSign(structSign);
/* 276 */         structSign.save();
/* 277 */         return;
/*     */         
/* 279 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 280 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 282 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 283 */         structSign.setText("");
/* 284 */         structSign.setDirection(sb.getData());
/* 285 */         structSign.setAction((String)sb.keyvalues.get("id"));
/* 286 */         structSign.setType("item");
/* 287 */         structSign.update();
/*     */         
/* 289 */         addStructureSign(structSign);
/* 290 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 292 */         structSign.save();
/*     */         
/* 294 */         int selectorIndex = Integer.valueOf((String)sb.keyvalues.get("id")).intValue();
/* 295 */         SignSelectionComponent signComp = (SignSelectionComponent)this.signSelectors.get(Integer.valueOf(selectorIndex));
/* 296 */         if (signComp != null) {
/* 297 */           signComp.setActionSignCoord(absCoord);
/* 298 */           signComp.updateActionSign();
/*     */         } else {
/* 300 */           CivLog.warning("No sign selector found for id:" + selectorIndex);
/*     */           
/*     */ 
/* 303 */           return;
/*     */           
/* 305 */           ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 306 */           ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */           
/* 308 */           StructureSign structSign = new StructureSign(absCoord, this);
/* 309 */           structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Next");
/* 310 */           structSign.setDirection(sb.getData());
/* 311 */           structSign.setType("next");
/* 312 */           structSign.setAction((String)sb.keyvalues.get("id"));
/* 313 */           structSign.update();
/* 314 */           addStructureSign(structSign);
/* 315 */           CivGlobal.addStructureSign(structSign);
/*     */           
/* 317 */           structSign.save();
/* 318 */           return;
/*     */           
/* 320 */           this.horseSpawnCoord = absCoord;
/*     */         }
/*     */       } else {
/* 323 */         this.muleSpawnCoord = absCoord;
/*     */       }
/*     */       break; }
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double d) {
/* 329 */     this.nonMemberFeeComponent.setFeeRate(d);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Stable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */