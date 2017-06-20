/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.DecimalFormat;
/*     */ import org.bukkit.Location;
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
/*     */ 
/*     */ public class Bank
/*     */   extends Structure
/*     */ {
/*  46 */   private int level = 1;
/*  47 */   private double interestRate = 0.0D;
/*     */   
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   private static final int IRON_SIGN = 0;
/*     */   private static final int GOLD_SIGN = 1;
/*     */   private static final int DIAMOND_SIGN = 2;
/*     */   private static final int EMERALD_SIGN = 3;
/*     */   
/*     */   protected Bank(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  58 */     super(center, id, town);
/*  59 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  60 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   public Bank(ResultSet rs) throws SQLException, CivException {
/*  64 */     super(rs);
/*  65 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  66 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   public double getBankExchangeRate() {
/*  70 */     double exchange_rate = 0.0D;
/*  71 */     switch (this.level) {
/*     */     case 1: 
/*  73 */       exchange_rate = 0.4D;
/*  74 */       break;
/*     */     case 2: 
/*  76 */       exchange_rate = 0.5D;
/*  77 */       break;
/*     */     case 3: 
/*  79 */       exchange_rate = 0.6D;
/*  80 */       break;
/*     */     case 4: 
/*  82 */       exchange_rate = 0.7D;
/*  83 */       break;
/*     */     case 5: 
/*  85 */       exchange_rate = 0.8D;
/*  86 */       break;
/*     */     case 6: 
/*  88 */       exchange_rate = 0.9D;
/*  89 */       break;
/*     */     case 7: 
/*  91 */       exchange_rate = 1.0D;
/*  92 */       break;
/*     */     case 8: 
/*  94 */       exchange_rate = 1.2D;
/*  95 */       break;
/*     */     case 9: 
/*  97 */       exchange_rate = 1.5D;
/*  98 */       break;
/*     */     case 10: 
/* 100 */       exchange_rate = 2.0D;
/*     */     }
/*     */     
/*     */     
/* 104 */     double rate = 1.0D;
/* 105 */     double addtional = rate * getTown().getBuffManager().getEffectiveDouble("buff_barter");
/* 106 */     rate += addtional;
/* 107 */     if (rate > 1.0D) {
/* 108 */       exchange_rate *= rate;
/*     */     }
/* 110 */     return exchange_rate;
/*     */   }
/*     */   
/*     */   public void onBonusGoodieUpdate()
/*     */   {
/* 115 */     updateSignText();
/*     */   }
/*     */   
/*     */   private String getExchangeRateString() {
/* 119 */     return ((int)(getBankExchangeRate() * 100.0D) + "%").toString();
/*     */   }
/*     */   
/*     */ 
/* 123 */   private String getNonResidentFeeString() { return "Fee: " + new StringBuilder(String.valueOf((int)(this.nonMemberFeeComponent.getFeeRate() * 100.0D))).append("%").toString().toString(); }
/*     */   
/*     */   private String getSignItemPrice(int signId) {
/*     */     double itemPrice;
/*     */     double itemPrice;
/* 128 */     if (signId == 0) {
/* 129 */       itemPrice = CivSettings.iron_rate;
/*     */     } else { double itemPrice;
/* 131 */       if (signId == 1) {
/* 132 */         itemPrice = CivSettings.gold_rate;
/*     */       } else { double itemPrice;
/* 134 */         if (signId == 2) {
/* 135 */           itemPrice = CivSettings.diamond_rate;
/*     */         } else {
/* 137 */           itemPrice = CivSettings.emerald_rate;
/*     */         }
/*     */       }
/*     */     }
/* 141 */     String out = "1 = ";
/* 142 */     out = out + (int)(itemPrice * getBankExchangeRate());
/* 143 */     out = out + " Coins";
/* 144 */     return out;
/*     */   }
/*     */   
/*     */   public void exchange_for_coins(Resident resident, int itemId, double coins) throws CivException {
/* 148 */     double exchange_rate = 0.0D;
/*     */     
/* 150 */     Player player = CivGlobal.getPlayer(resident);
/*     */     String itemName;
/* 152 */     String itemName; if (itemId == 265) {
/* 153 */       itemName = "Iron"; } else { String itemName;
/* 154 */       if (itemId == 266) {
/* 155 */         itemName = "Gold"; } else { String itemName;
/* 156 */         if (itemId == 264) {
/* 157 */           itemName = "Diamond";
/*     */         } else
/* 159 */           itemName = "Emerald";
/*     */       } }
/* 161 */     exchange_rate = getBankExchangeRate();
/*     */     
/* 163 */     if (!resident.takeItemInHand(itemId, 0, 1)) {
/* 164 */       throw new CivException("You do not have enough " + itemName + " in your hand.");
/*     */     }
/*     */     
/* 167 */     Town usersTown = resident.getTown();
/*     */     
/*     */ 
/* 170 */     if (usersTown == getTown()) {
/* 171 */       DecimalFormat df = new DecimalFormat();
/* 172 */       resident.getTreasury().deposit((int)(coins * exchange_rate));
/* 173 */       CivMessage.send(player, 
/* 174 */         "§aExchanged 1 " + itemName + " for " + df.format(coins * exchange_rate) + " coins.");
/* 175 */       return;
/*     */     }
/*     */     
/*     */ 
/* 179 */     double giveToPlayer = (int)(coins * exchange_rate);
/* 180 */     double giveToTown = (int)giveToPlayer * getNonResidentFee();
/* 181 */     giveToPlayer -= giveToTown;
/*     */     
/* 183 */     giveToTown = Math.round(giveToTown);
/* 184 */     giveToPlayer = Math.round(giveToPlayer);
/*     */     
/* 186 */     getTown().depositDirect(giveToTown);
/* 187 */     resident.getTreasury().deposit(giveToPlayer);
/*     */     
/* 189 */     CivMessage.send(player, "§aExchanged 1 " + itemName + " for " + giveToPlayer + " coins.");
/* 190 */     CivMessage.send(player, "§e Paid " + giveToTown + " coins in non-resident taxes.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 198 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 200 */     if (resident == null) {
/* 201 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 206 */       if (LoreMaterial.isCustom(player.getItemInHand())) {
/* 207 */         throw new CivException("You cannot exchange this item at the bank.");
/*     */       }
/*     */       String str;
/* 210 */       switch ((str = sign.getAction()).hashCode()) {case -1634062812:  if (str.equals("emerald")) {} break; case 3178592:  if (str.equals("gold")) {} break; case 3241160:  if (str.equals("iron")) break; break; case 1655054676:  if (!str.equals("diamond"))
/*     */         {
/* 212 */           return;exchange_for_coins(resident, 265, CivSettings.iron_rate);
/* 213 */           return;
/*     */           
/* 215 */           exchange_for_coins(resident, 266, CivSettings.gold_rate);
/*     */         }
/*     */         else {
/* 218 */           exchange_for_coins(resident, 264, CivSettings.diamond_rate);
/* 219 */           return;
/*     */           
/* 221 */           exchange_for_coins(resident, 388, CivSettings.emerald_rate);
/*     */         }
/*     */         break; }
/*     */     } catch (CivException e) {
/* 225 */       CivMessage.send(player, "§c" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateSignText() {
/*     */     label353:
/* 231 */     for (StructureSign sign : getSigns()) {
/*     */       String str;
/* 233 */       switch ((str = sign.getAction().toLowerCase()).hashCode()) {case -1634062812:  if (str.equals("emerald")) {} break; case 3178592:  if (str.equals("gold")) {} break; case 3241160:  if (str.equals("iron")) break; break; case 1655054676:  if (!str.equals("diamond")) {
/*     */           break label353;
/* 235 */           sign.setText("Iron\nAt " + 
/* 236 */             getExchangeRateString() + "\n" + 
/* 237 */             getSignItemPrice(0) + "\n" + 
/* 238 */             getNonResidentFeeString());
/*     */           
/*     */           break label353;
/* 241 */           sign.setText("Gold\nAt " + 
/* 242 */             getExchangeRateString() + "\n" + 
/* 243 */             getSignItemPrice(1) + "\n" + 
/* 244 */             getNonResidentFeeString());
/*     */         }
/*     */         else {
/* 247 */           sign.setText("Diamond\n" + 
/* 248 */             getExchangeRateString() + "\n" + 
/* 249 */             "At " + getSignItemPrice(2) + "\n" + 
/* 250 */             getNonResidentFeeString());
/*     */           
/*     */           break label353;
/* 253 */           sign.setText("Emerald\n" + 
/* 254 */             getExchangeRateString() + "\n" + 
/* 255 */             "At " + getSignItemPrice(3) + "\n" + 
/* 256 */             getNonResidentFeeString());
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/* 261 */       sign.update();
/* 262 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 268 */     String out = "<u><b>Bank</u></b><br/>";
/* 269 */     out = out + "Level: " + this.level;
/* 270 */     return out;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 275 */     return "bank";
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 279 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(int level) {
/* 283 */     this.level = level;
/*     */   }
/*     */   
/*     */   public double getNonResidentFee() {
/* 287 */     return this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double nonResidentFee) {
/* 291 */     this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
/*     */   }
/*     */   
/*     */   public double getInterestRate() {
/* 295 */     return this.interestRate;
/*     */   }
/*     */   
/*     */   public void setInterestRate(double interestRate) {
/* 299 */     this.interestRate = interestRate;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLoad()
/*     */   {
/* 305 */     if (this.interestRate == 0.0D) {
/* 306 */       getTown().getTreasury().setPrincipalAmount(0.0D);
/* 307 */       return;
/*     */     }
/*     */     
/*     */ 
/* 311 */     getTown().getTreasury().setPrincipalAmount(getTown().getTreasury().getBalance());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onDailyEvent()
/*     */   {
/* 318 */     double effectiveInterestRate = this.interestRate;
/* 319 */     if (effectiveInterestRate == 0.0D) {
/* 320 */       getTown().getTreasury().setPrincipalAmount(0.0D);
/* 321 */       return;
/*     */     }
/*     */     
/* 324 */     double principal = getTown().getTreasury().getPrincipalAmount();
/*     */     
/* 326 */     if (getTown().getBuffManager().hasBuff("buff_greed")) {
/* 327 */       double increase = getTown().getBuffManager().getEffectiveDouble("buff_greed");
/* 328 */       effectiveInterestRate += increase;
/* 329 */       CivMessage.sendTown(getTown(), "§7Your goodie buff 'Greed' has increased the interest our town generated.");
/*     */     }
/*     */     
/* 332 */     double newCoins = principal * effectiveInterestRate;
/*     */     
/*     */ 
/* 335 */     newCoins = Math.floor(newCoins);
/*     */     
/* 337 */     if (newCoins != 0.0D) {
/* 338 */       CivMessage.sendTown(getTown(), "§aOur town earned " + newCoins + " coins from interest on a principal of " + principal + " coins.");
/* 339 */       getTown().getTreasury().deposit(newCoins);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 344 */     getTown().getTreasury().setPrincipalAmount(getTown().getTreasury().getBalance());
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock)
/*     */   {
/* 350 */     this.level = getTown().saved_bank_level;
/* 351 */     this.interestRate = getTown().saved_bank_interest_amount;
/*     */   }
/*     */   
/*     */   public NonMemberFeeComponent getNonMemberFeeComponent() {
/* 355 */     return this.nonMemberFeeComponent;
/*     */   }
/*     */   
/*     */   public void setNonMemberFeeComponent(NonMemberFeeComponent nonMemberFeeComponent) {
/* 359 */     this.nonMemberFeeComponent = nonMemberFeeComponent;
/*     */   }
/*     */   
/*     */   public void onGoodieFromFrame() {
/* 363 */     updateSignText();
/*     */   }
/*     */   
/*     */   public void onGoodieToFrame() {
/* 367 */     updateSignText();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Bank.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */