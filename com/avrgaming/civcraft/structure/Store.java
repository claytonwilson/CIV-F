/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StoreMaterial;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class Store
/*     */   extends Structure
/*     */ {
/*  42 */   private int level = 1;
/*     */   
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   
/*  46 */   ArrayList<StoreMaterial> materials = new ArrayList();
/*     */   
/*     */   protected Store(Location center, String id, Town town) throws CivException {
/*  49 */     super(center, id, town);
/*  50 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  51 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   protected Store(ResultSet rs) throws SQLException, CivException {
/*  55 */     super(rs);
/*  56 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  57 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   public int getLevel() {
/*  61 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(int level) {
/*  65 */     this.level = level;
/*     */   }
/*     */   
/*     */   public double getNonResidentFee() {
/*  69 */     return this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double nonResidentFee) {
/*  73 */     this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
/*     */   }
/*     */   
/*     */   private String getNonResidentFeeString() {
/*  77 */     return "Fee: " + new StringBuilder(String.valueOf((int)(this.nonMemberFeeComponent.getFeeRate() * 100.0D))).append("%").toString().toString();
/*     */   }
/*     */   
/*     */   public void addStoreMaterial(StoreMaterial mat) throws CivException {
/*  81 */     if (this.materials.size() >= 4) {
/*  82 */       throw new CivException("Store is full.");
/*     */     }
/*  84 */     this.materials.add(mat);
/*     */   }
/*     */   
/*     */   private StructureSign getSignFromSpecialId(int special_id) {
/*  88 */     for (StructureSign sign : getSigns()) {
/*  89 */       int id = Integer.valueOf(sign.getAction()).intValue();
/*  90 */       if (id == special_id) {
/*  91 */         return sign;
/*     */       }
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public void updateSignText()
/*     */   {
/*  99 */     int count = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 104 */     for (StoreMaterial mat : this.materials) {
/* 105 */       StructureSign sign = getSignFromSpecialId(count);
/* 106 */       if (sign == null) {
/* 107 */         CivLog.error("sign from special id was null, id:" + count);
/* 108 */         return;
/*     */       }
/*     */       
/* 111 */       sign.setText("Buy 64\n" + 
/* 112 */         mat.name + "\n" + 
/* 113 */         "For " + (int)mat.price + " Coins\n" + 
/* 114 */         getNonResidentFeeString());
/* 115 */       sign.update();
/* 116 */       sign.save();
/* 117 */       count++;
/*     */     }
/* 121 */     for (; 
/*     */         
/* 121 */         count < getSigns().size(); count++) {
/* 122 */       StructureSign sign = getSignFromSpecialId(count);
/* 123 */       sign.setText("Store Self\nEmpty");
/* 124 */       sign.update();
/* 125 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 131 */     int special_id = Integer.valueOf(sign.getAction()).intValue();
/* 132 */     if (special_id < this.materials.size()) {
/* 133 */       StoreMaterial mat = (StoreMaterial)this.materials.get(special_id);
/* 134 */       sign_buy_material(player, mat.name, mat.type, mat.data, 64, mat.price);
/*     */     } else {
/* 136 */       CivMessage.send(player, "§cStore shelf empty, stock it using /town upgrade.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sign_buy_material(Player player, String itemName, int id, byte data, int amount, double price)
/*     */   {
/* 144 */     int payToTown = (int)Math.round(price * getNonResidentFee());
/*     */     try
/*     */     {
/* 147 */       Resident resident = CivGlobal.getResident(player.getName());
/* 148 */       Town t = resident.getTown();
/*     */       
/* 150 */       if (t == getTown())
/*     */       {
/* 152 */         resident.buyItem(itemName, id, data, price, amount);
/* 153 */         CivMessage.send(player, "§aBought " + amount + " " + itemName + " for " + price + " coins.");
/* 154 */         return;
/*     */       }
/*     */       
/* 157 */       resident.buyItem(itemName, id, data, price + payToTown, amount);
/* 158 */       getTown().depositDirect(payToTown);
/* 159 */       CivMessage.send(player, "§ePaid " + payToTown + " coins in non-resident taxes.");
/*     */ 
/*     */     }
/*     */     catch (CivException e)
/*     */     {
/* 164 */       CivMessage.send(player, "§c" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDynmapDescription()
/*     */   {
/* 171 */     String out = "<u><b>Store</u></b><br/>";
/* 172 */     if (this.materials.size() == 0) {
/* 173 */       out = out + "Nothing stocked.";
/*     */     }
/*     */     else {
/* 176 */       for (StoreMaterial mat : this.materials) {
/* 177 */         out = out + mat.name + " for " + mat.price + "<br/>";
/*     */       }
/*     */     }
/* 180 */     return out;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 185 */     return "bricks";
/*     */   }
/*     */   
/*     */   public void reset() {
/* 189 */     this.materials.clear();
/* 190 */     updateSignText();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Store.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */