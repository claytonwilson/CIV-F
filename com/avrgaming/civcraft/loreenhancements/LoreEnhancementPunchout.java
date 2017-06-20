/*    */ package com.avrgaming.civcraft.loreenhancements;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*    */ import gpl.AttributeUtil;
/*    */ import java.util.Random;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoreEnhancementPunchout
/*    */   extends LoreEnhancement
/*    */ {
/*    */   public String getDisplayName()
/*    */   {
/* 15 */     return "Punchout";
/*    */   }
/*    */   
/*    */   public AttributeUtil add(AttributeUtil attrs) {
/* 19 */     attrs.addEnhancement("LoreEnhancementPunchout", null, null);
/* 20 */     attrs.addLore("§6" + getDisplayName());
/* 21 */     return attrs;
/*    */   }
/*    */   
/*    */   public int onStructureBlockBreak(BuildableDamageBlock sb, int damage)
/*    */   {
/* 26 */     Random rand = new Random();
/*    */     
/* 28 */     if ((damage <= 1) && 
/* 29 */       (rand.nextInt(100) <= 50)) {
/* 30 */       damage += rand.nextInt(5) + 1;
/*    */     }
/*    */     
/*    */ 
/* 34 */     return damage;
/*    */   }
/*    */   
/*    */   public String serialize(ItemStack stack)
/*    */   {
/* 39 */     return "";
/*    */   }
/*    */   
/*    */   public ItemStack deserialize(ItemStack stack, String data)
/*    */   {
/* 44 */     return stack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\loreenhancements\LoreEnhancementPunchout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */