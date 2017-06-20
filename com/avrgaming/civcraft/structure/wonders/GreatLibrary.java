/*     */ package com.avrgaming.civcraft.structure.wonders;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigEnchant;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
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
/*     */ public class GreatLibrary
/*     */   extends Wonder
/*     */ {
/*     */   public GreatLibrary(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  48 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public GreatLibrary(ResultSet rs) throws SQLException, CivException {
/*  52 */     super(rs);
/*     */   }
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  57 */     if (isActive()) {
/*  58 */       addBuffs();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onComplete()
/*     */   {
/*  64 */     addBuffs();
/*     */   }
/*     */   
/*     */   public void onDestroy()
/*     */   {
/*  69 */     super.onDestroy();
/*  70 */     removeBuffs();
/*     */   }
/*     */   
/*     */   protected void removeBuffs()
/*     */   {
/*  75 */     removeBuffFromCiv(getCiv(), "buff_greatlibrary_extra_beakers");
/*  76 */     removeBuffFromTown(getTown(), "buff_greatlibrary_double_tax_beakers");
/*     */   }
/*     */   
/*     */   protected void addBuffs()
/*     */   {
/*  81 */     addBuffToCiv(getCiv(), "buff_greatlibrary_extra_beakers");
/*  82 */     addBuffToTown(getTown(), "buff_greatlibrary_double_tax_beakers");
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateSignText()
/*     */   {
/*     */     label369:
/*  89 */     for (StructureSign sign : getSigns()) {
/*     */       String str;
/*  91 */       switch ((str = sign.getAction().toLowerCase()).hashCode()) {case 48:  if (str.equals("0")) break; break; case 49:  if (str.equals("1")) {} break; case 50:  if (str.equals("2")) {} break; case 51:  if (!str.equals("3")) {
/*     */           break label369;
/*  93 */           ConfigEnchant enchant = (ConfigEnchant)CivSettings.enchants.get("ench_fire_aspect");
/*  94 */           sign.setText(enchant.name + "\n\n" + "§a" + enchant.cost + " Coins.");
/*     */           
/*     */           break label369;
/*  97 */           ConfigEnchant enchant = (ConfigEnchant)CivSettings.enchants.get("ench_fire_protection");
/*  98 */           sign.setText(enchant.name + "\n\n" + "§a" + enchant.cost + " Coins.");
/*     */           
/*     */           break label369;
/* 101 */           ConfigEnchant enchant = (ConfigEnchant)CivSettings.enchants.get("ench_flame");
/* 102 */           sign.setText(enchant.name + "\n\n" + "§a" + enchant.cost + " Coins.");
/*     */         }
/*     */         else {
/* 105 */           ConfigEnchant enchant = (ConfigEnchant)CivSettings.enchants.get("ench_punchout");
/* 106 */           sign.setText(enchant.name + "\n\n" + "§a" + enchant.cost + " Coins.");
/*     */         }
/*     */         break;
/*     */       }
/* 110 */       sign.update();
/* 111 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 118 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 120 */     if (resident == null) {
/* 121 */       return;
/*     */     }
/*     */     
/* 124 */     if ((!resident.hasTown()) || (resident.getCiv() != getCiv())) {
/* 125 */       CivMessage.sendError(player, "Only members of " + getCiv().getName() + " may use The Great Library.");
/* 126 */       return;
/*     */     }
/*     */     
/* 129 */     ItemStack hand = player.getItemInHand();
/*     */     
/*     */     String str;
/* 132 */     switch ((str = sign.getAction()).hashCode()) {case 48:  if (str.equals("0")) break; break; case 49:  if (str.equals("1")) {} break; case 50:  if (str.equals("2")) {} break; case 51:  if (!str.equals("3")) {
/*     */         break label695;
/* 134 */         if (!Enchantment.FIRE_ASPECT.canEnchantItem(hand)) {
/* 135 */           CivMessage.sendError(player, "You cannot put this enchantment on this item.");
/* 136 */           return;
/*     */         }
/*     */         
/* 139 */         ConfigEnchant configEnchant = (ConfigEnchant)CivSettings.enchants.get("ench_fire_aspect");
/* 140 */         if (!resident.getTreasury().hasEnough(configEnchant.cost)) {
/* 141 */           CivMessage.send(player, "§cYou do not have enough money, you need " + configEnchant.cost + " coins.");
/* 142 */           return;
/*     */         }
/*     */         
/* 145 */         resident.getTreasury().withdraw(configEnchant.cost);
/* 146 */         hand.addEnchantment(Enchantment.FIRE_ASPECT, 2);
/*     */         
/*     */         break label696;
/* 149 */         if (!Enchantment.PROTECTION_FIRE.canEnchantItem(hand)) {
/* 150 */           CivMessage.sendError(player, "You cannot put this enchantment on this item.");
/* 151 */           return;
/*     */         }
/*     */         
/* 154 */         ConfigEnchant configEnchant = (ConfigEnchant)CivSettings.enchants.get("ench_fire_protection");
/* 155 */         if (!resident.getTreasury().hasEnough(configEnchant.cost)) {
/* 156 */           CivMessage.send(player, "§cYou do not have enough money, you need " + configEnchant.cost + " coins.");
/* 157 */           return;
/*     */         }
/*     */         
/* 160 */         resident.getTreasury().withdraw(configEnchant.cost);
/* 161 */         hand.addEnchantment(Enchantment.PROTECTION_FIRE, 3);
/*     */         
/*     */         break label696;
/* 164 */         if (!Enchantment.ARROW_FIRE.canEnchantItem(hand)) {
/* 165 */           CivMessage.sendError(player, "You cannot put this enchantment on this item.");
/* 166 */           return;
/*     */         }
/*     */         
/* 169 */         ConfigEnchant configEnchant = (ConfigEnchant)CivSettings.enchants.get("ench_flame");
/* 170 */         if (!resident.getTreasury().hasEnough(configEnchant.cost)) {
/* 171 */           CivMessage.send(player, "§cYou do not have enough money, you need " + configEnchant.cost + " coins.");
/* 172 */           return;
/*     */         }
/*     */         
/* 175 */         resident.getTreasury().withdraw(configEnchant.cost);
/* 176 */         hand.addEnchantment(Enchantment.ARROW_FIRE, 1);
/*     */         break label696;
/*     */       } else {
/* 179 */         switch (ItemManager.getId(hand)) {
/*     */         case 257: 
/*     */         case 270: 
/*     */         case 274: 
/*     */         case 278: 
/*     */         case 285: 
/* 185 */           ConfigEnchant configEnchant = (ConfigEnchant)CivSettings.enchants.get("ench_punchout");
/*     */           
/* 187 */           if (!LoreMaterial.isCustom(hand)) {
/* 188 */             CivMessage.sendError(player, "This item is not a custom civcraft item and cannot recieve this enhancement.");
/* 189 */             return;
/*     */           }
/*     */           
/* 192 */           if (LoreMaterial.hasEnhancement(hand, configEnchant.enchant_id)) {
/* 193 */             CivMessage.sendError(player, "You already have this enhancement on this item.");
/* 194 */             return;
/*     */           }
/*     */           
/* 197 */           if (!resident.getTreasury().hasEnough(configEnchant.cost)) {
/* 198 */             CivMessage.send(player, "§cYou do not have enough money, you need " + configEnchant.cost + " coins.");
/* 199 */             return;
/*     */           }
/*     */           
/* 202 */           resident.getTreasury().withdraw(configEnchant.cost);
/* 203 */           ItemStack newItem = LoreMaterial.addEnhancement(hand, (LoreEnhancement)LoreEnhancement.enhancements.get(configEnchant.enchant_id));
/* 204 */           player.setItemInHand(newItem);
/* 205 */           break;
/*     */         default: 
/* 207 */           CivMessage.sendError(player, "You can only add this enchantment to pickaxes."); return;
/*     */         }
/*     */       }
/*     */       break; }
/*     */     label695:
/* 212 */     return;
/*     */     label696:
/*     */     ConfigEnchant configEnchant;
/* 215 */     CivMessage.sendSuccess(player, "Enchant Success!");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\GreatLibrary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */