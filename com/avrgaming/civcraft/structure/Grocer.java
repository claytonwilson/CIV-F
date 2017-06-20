/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigGrocerLevel;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public class Grocer
/*     */   extends Structure
/*     */ {
/*  41 */   private int level = 1;
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   
/*     */   protected Grocer(Location center, String id, Town town) throws CivException
/*     */   {
/*  46 */     super(center, id, town);
/*  47 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  48 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   public Grocer(ResultSet rs) throws SQLException, CivException {
/*  52 */     super(rs);
/*  53 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  54 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  59 */     String out = "<u><b>Grocer</u></b><br/>";
/*     */     
/*  61 */     for (int i = 0; i < this.level; i++) {
/*  62 */       ConfigGrocerLevel grocerlevel = (ConfigGrocerLevel)CivSettings.grocerLevels.get(Integer.valueOf(i + 1));
/*  63 */       out = out + "<b>" + grocerlevel.itemName + "</b> Amount: " + grocerlevel.amount + " Price: " + grocerlevel.price + " coins.<br/>";
/*     */     }
/*     */     
/*  66 */     return out;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  71 */     return "cutlery";
/*     */   }
/*     */   
/*     */   public int getLevel() {
/*  75 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(int level) {
/*  79 */     this.level = level;
/*     */   }
/*     */   
/*     */   public double getNonResidentFee() {
/*  83 */     return this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double nonResidentFee) {
/*  87 */     this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
/*     */   }
/*     */   
/*     */   private String getNonResidentFeeString() {
/*  91 */     return "Fee: " + new StringBuilder(String.valueOf((int)(getNonResidentFee() * 100.0D))).append("%").toString().toString();
/*     */   }
/*     */   
/*     */   private StructureSign getSignFromSpecialId(int special_id) {
/*  95 */     for (StructureSign sign : getSigns()) {
/*  96 */       int id = Integer.valueOf(sign.getAction()).intValue();
/*  97 */       if (id == special_id) {
/*  98 */         return sign;
/*     */       }
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public void sign_buy_material(Player player, String itemName, int id, byte data, int amount, double price)
/*     */   {
/* 106 */     int payToTown = (int)Math.round(price * getNonResidentFee());
/*     */     try
/*     */     {
/* 109 */       Resident resident = CivGlobal.getResident(player.getName());
/* 110 */       Town t = resident.getTown();
/*     */       
/* 112 */       if (t == getTown())
/*     */       {
/* 114 */         resident.buyItem(itemName, id, data, price, amount);
/* 115 */         CivMessage.send(player, "§aBought " + amount + " " + itemName + " for " + price + " coins.");
/* 116 */         return;
/*     */       }
/*     */       
/* 119 */       resident.buyItem(itemName, id, data, price + payToTown, amount);
/* 120 */       getTown().depositDirect(payToTown);
/* 121 */       CivMessage.send(player, "§aBought " + amount + " " + itemName + " for " + price + " coins.");
/* 122 */       CivMessage.send(player, "§ePaid " + payToTown + " coins in non-resident taxes.");
/*     */ 
/*     */     }
/*     */     catch (CivException e)
/*     */     {
/* 127 */       CivMessage.send(player, "§c" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateSignText()
/*     */   {
/* 135 */     int count = 0;
/*     */     
/* 137 */     for (count = 0; count < this.level; count++) {
/* 138 */       StructureSign sign = getSignFromSpecialId(count);
/* 139 */       ConfigGrocerLevel grocerlevel = (ConfigGrocerLevel)CivSettings.grocerLevels.get(Integer.valueOf(count + 1));
/*     */       
/* 141 */       sign.setText("Buy\n" + grocerlevel.itemName + "\n" + 
/* 142 */         "For " + grocerlevel.price + " Coins\n" + 
/* 143 */         getNonResidentFeeString());
/*     */       
/* 145 */       sign.update();
/* 146 */       sign.save();
/*     */     }
/* 149 */     for (; 
/* 149 */         count < getSigns().size(); count++) {
/* 150 */       StructureSign sign = getSignFromSpecialId(count);
/* 151 */       sign.setText("Grocer Shelf\nEmpty");
/* 152 */       sign.update();
/* 153 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 160 */     int special_id = Integer.valueOf(sign.getAction()).intValue();
/* 161 */     if (special_id < this.level) {
/* 162 */       ConfigGrocerLevel grocerlevel = (ConfigGrocerLevel)CivSettings.grocerLevels.get(Integer.valueOf(special_id + 1));
/* 163 */       sign_buy_material(player, grocerlevel.itemName, grocerlevel.itemId, 
/* 164 */         (byte)grocerlevel.itemData, grocerlevel.amount, grocerlevel.price);
/*     */     } else {
/* 166 */       CivMessage.send(player, "§cGrocer shelf empty, stock it using /town upgrade.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Grocer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */