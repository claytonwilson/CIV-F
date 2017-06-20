/*    */ package com.avrgaming.civcraft.loreenhancements;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import gpl.AttributeUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LoreEnhancement
/*    */ {
/*    */   public AttributeUtil add(AttributeUtil attrs)
/*    */   {
/* 16 */     return attrs;
/*    */   }
/*    */   
/* 19 */   public static HashMap<String, LoreEnhancement> enhancements = new HashMap();
/* 20 */   public HashMap<String, String> variables = new HashMap();
/*    */   
/*    */   public static void init() {
/* 23 */     enhancements.put("LoreEnhancementSoulBound", new LoreEnhancementSoulBound());
/* 24 */     enhancements.put("LoreEnhancementAttack", new LoreEnhancementAttack());
/* 25 */     enhancements.put("LoreEnhancementDefense", new LoreEnhancementDefense());
/* 26 */     enhancements.put("LoreEnhancementPunchout", new LoreEnhancementPunchout());
/* 27 */     enhancements.put("LoreEnhancementArenaItem", new LoreEnhancementArenaItem());
/*    */   }
/*    */   
/* 30 */   public boolean onDeath(PlayerDeathEvent event, ItemStack stack) { return false; }
/*    */   
/*    */   public boolean canEnchantItem(ItemStack item) {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   public static boolean isWeapon(ItemStack item) {
/* 37 */     switch (ItemManager.getId(item)) {
/*    */     case 261: 
/*    */     case 267: 
/*    */     case 268: 
/*    */     case 272: 
/*    */     case 276: 
/*    */     case 283: 
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isArmor(ItemStack item)
/*    */   {
/* 51 */     switch (ItemManager.getId(item)) {
/*    */     case 298: 
/*    */     case 299: 
/*    */     case 300: 
/*    */     case 301: 
/*    */     case 302: 
/*    */     case 303: 
/*    */     case 304: 
/*    */     case 305: 
/*    */     case 306: 
/*    */     case 307: 
/*    */     case 308: 
/*    */     case 309: 
/*    */     case 310: 
/*    */     case 311: 
/*    */     case 312: 
/*    */     case 313: 
/*    */     case 314: 
/*    */     case 315: 
/*    */     case 316: 
/*    */     case 317: 
/* 72 */       return true;
/*    */     }
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isWeaponOrArmor(ItemStack item)
/*    */   {
/* 79 */     return (isWeapon(item)) || (isArmor(item));
/*    */   }
/*    */   
/*    */   public boolean hasEnchantment(ItemStack item) {
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   public String getDisplayName() {
/* 87 */     return "LoreEnchant";
/*    */   }
/*    */   
/*    */   public int onStructureBlockBreak(BuildableDamageBlock dmgBlock, int damage) {
/* 91 */     return damage;
/*    */   }
/*    */   
/* 94 */   public double getLevel(AttributeUtil attrs) { return 0.0D; }
/*    */   
/*    */   public abstract String serialize(ItemStack paramItemStack);
/*    */   
/*    */   public abstract ItemStack deserialize(ItemStack paramItemStack, String paramString);
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */