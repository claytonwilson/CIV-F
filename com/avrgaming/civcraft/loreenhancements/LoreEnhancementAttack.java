/*     */ package com.avrgaming.civcraft.loreenhancements;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class LoreEnhancementAttack
/*     */   extends LoreEnhancement
/*     */ {
/*     */   public LoreEnhancementAttack()
/*     */   {
/*  16 */     this.variables.put("amount", "1.0");
/*     */   }
/*     */   
/*     */   public String getLoreString(double baseLevel) {
/*     */     double m;
/*     */     try {
/*  22 */       m = CivSettings.getDouble(CivSettings.civConfig, "global.attack_catalyst_multiplier");
/*     */     } catch (InvalidConfiguration e) { double m;
/*  24 */       e.printStackTrace();
/*  25 */       m = 1.0D;
/*     */     }
/*  27 */     return "§3+" + baseLevel * m + " Attack";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeUtil add(AttributeUtil attrs)
/*     */   {
/*  38 */     double amount = Double.valueOf((String)this.variables.get("amount")).doubleValue();
/*  39 */     double baseLevel = amount;
/*  40 */     if (attrs.hasEnhancement("LoreEnhancementAttack"))
/*     */     {
/*     */ 
/*  43 */       baseLevel = Double.valueOf(attrs.getEnhancementData("LoreEnhancementAttack", "level")).doubleValue();
/*     */       
/*     */ 
/*  46 */       String[] lore = attrs.getLore();
/*  47 */       for (int i = 0; i < lore.length; i++) {
/*  48 */         if (lore[i].equals(getLoreString(baseLevel))) {
/*  49 */           lore[i] = getLoreString(baseLevel + amount);
/*     */         }
/*     */       }
/*  52 */       attrs.setLore(lore);
/*     */       
/*     */ 
/*  55 */       String[] split = attrs.getName().split("\\(");
/*  56 */       attrs.setName(split[0] + "(+" + (baseLevel + amount) + ")");
/*     */       
/*     */ 
/*  59 */       attrs.setEnhancementData("LoreEnhancementAttack", "level", baseLevel + amount);
/*     */     } else {
/*  61 */       attrs.addEnhancement("LoreEnhancementAttack", "level", baseLevel);
/*  62 */       attrs.addLore(getLoreString(baseLevel));
/*  63 */       attrs.setName(attrs.getName() + "§b" + "(+" + amount + ")");
/*     */     }
/*     */     
/*  66 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(attrs.getCivCraftProperty("mid"));
/*  67 */     if (craftMat == null) {
/*  68 */       CivLog.warning("Couldn't find craft mat with MID of:" + attrs.getCivCraftProperty("mid"));
/*  69 */       return attrs;
/*     */     }
/*     */     
/*  72 */     return attrs;
/*     */   }
/*     */   
/*     */   public double getLevel(AttributeUtil attrs)
/*     */   {
/*  77 */     if (attrs.hasEnhancement("LoreEnhancementAttack"))
/*     */     {
/*  79 */       Double baseLevel = Double.valueOf(attrs.getEnhancementData("LoreEnhancementAttack", "level"));
/*  80 */       return baseLevel.doubleValue();
/*     */     }
/*  82 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canEnchantItem(ItemStack item)
/*     */   {
/*  88 */     return isWeapon(item);
/*     */   }
/*     */   
/*     */   public double getExtraAttack(AttributeUtil attrs)
/*     */   {
/*     */     try {
/*  94 */       double m = CivSettings.getDouble(CivSettings.civConfig, "global.attack_catalyst_multiplier");
/*  95 */       return getLevel(attrs) * m;
/*     */     } catch (InvalidConfiguration e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*     */     
/* 100 */     return getLevel(attrs);
/*     */   }
/*     */   
/*     */   public String serialize(ItemStack stack)
/*     */   {
/* 105 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 106 */     return attrs.getEnhancementData("LoreEnhancementAttack", "level");
/*     */   }
/*     */   
/*     */   public ItemStack deserialize(ItemStack stack, String data)
/*     */   {
/* 111 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 112 */     attrs.setEnhancementData("LoreEnhancementAttack", "level", data);
/* 113 */     attrs.setName(attrs.getName() + "§b" + "(+" + Double.valueOf(data) + ")");
/* 114 */     return attrs.getStack();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancementAttack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */