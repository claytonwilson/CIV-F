/*    */ package com.avrgaming.civcraft.loreenhancements;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import gpl.AttributeUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class LoreEnhancementDefense
/*    */   extends LoreEnhancement
/*    */ {
/*    */   public LoreEnhancementDefense()
/*    */   {
/* 14 */     this.variables.put("amount", "1.0");
/*    */   }
/*    */   
/*    */   public String getLoreString(double baseLevel) {
/* 18 */     return "§3+" + baseLevel + " Defense";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AttributeUtil add(AttributeUtil attrs)
/*    */   {
/* 29 */     double amount = Double.valueOf((String)this.variables.get("amount")).doubleValue();
/* 30 */     double baseLevel = amount;
/*    */     
/* 32 */     if (attrs.hasEnhancement("LoreEnhancementDefense"))
/*    */     {
/*    */ 
/* 35 */       baseLevel = Double.valueOf(attrs.getEnhancementData("LoreEnhancementDefense", "level")).doubleValue();
/*    */       
/*    */ 
/* 38 */       String[] lore = attrs.getLore();
/* 39 */       for (int i = 0; i < lore.length; i++) {
/* 40 */         if (lore[i].equals(getLoreString(baseLevel))) {
/* 41 */           lore[i] = getLoreString(baseLevel + amount);
/*    */         }
/*    */       }
/* 44 */       attrs.setLore(lore);
/*    */       
/*    */ 
/* 47 */       String[] split = attrs.getName().split("\\(");
/* 48 */       attrs.setName(split[0] + "(+" + (baseLevel + amount) + ")");
/*    */       
/*    */ 
/* 51 */       attrs.setEnhancementData("LoreEnhancementDefense", "level", baseLevel + amount);
/*    */     } else {
/* 53 */       attrs.addEnhancement("LoreEnhancementDefense", "level", baseLevel);
/* 54 */       attrs.addLore(getLoreString(baseLevel));
/* 55 */       attrs.setName(attrs.getName() + "§b" + "(+" + amount + ")");
/*    */     }
/*    */     
/* 58 */     return attrs;
/*    */   }
/*    */   
/*    */   public boolean canEnchantItem(ItemStack item)
/*    */   {
/* 63 */     return isArmor(item);
/*    */   }
/*    */   
/*    */   public double getLevel(AttributeUtil attrs)
/*    */   {
/* 68 */     if (attrs.hasEnhancement("LoreEnhancementDefense"))
/*    */     {
/* 70 */       Double baseLevel = Double.valueOf(attrs.getEnhancementData("LoreEnhancementDefense", "level"));
/* 71 */       return baseLevel.doubleValue();
/*    */     }
/* 73 */     return 1.0D;
/*    */   }
/*    */   
/*    */   public double getExtraDefense(AttributeUtil attrs)
/*    */   {
/*    */     try {
/* 79 */       double m = CivSettings.getDouble(CivSettings.civConfig, "global.defense_catalyst_multiplier");
/* 80 */       return getLevel(attrs) * m;
/*    */     } catch (InvalidConfiguration e) {
/* 82 */       e.printStackTrace();
/*    */     }
/*    */     
/* 85 */     return getLevel(attrs);
/*    */   }
/*    */   
/*    */   public String serialize(ItemStack stack)
/*    */   {
/* 90 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 91 */     return attrs.getEnhancementData("LoreEnhancementDefense", "level");
/*    */   }
/*    */   
/*    */   public ItemStack deserialize(ItemStack stack, String data)
/*    */   {
/* 96 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 97 */     attrs.setEnhancementData("LoreEnhancementDefense", "level", data);
/* 98 */     attrs.setName(attrs.getName() + "§b" + "(+" + Double.valueOf(data) + ")");
/* 99 */     return attrs.getStack();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancementDefense.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */