/*    */ package com.avrgaming.civcraft.object;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.structure.Library;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.enchantments.Enchantment;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LibraryEnchantment
/*    */ {
/*    */   public Enchantment enchant;
/*    */   public LoreEnhancement enhancement;
/*    */   public int level;
/*    */   public double price;
/*    */   public String name;
/*    */   public String displayName;
/*    */   
/*    */   public LibraryEnchantment(String name, int lvl, double p)
/*    */     throws CivException
/*    */   {
/* 36 */     this.enchant = Library.getEnchantFromString(name);
/* 37 */     if (this.enchant == null) {
/* 38 */       this.enhancement = ((LoreEnhancement)LoreEnhancement.enhancements.get(name));
/* 39 */       if (this.enhancement == null) {
/* 40 */         throw new CivException("Could not create CivEnchantment:" + name + ". Couldn't find enchantment or enhancement");
/*    */       }
/*    */     }
/* 43 */     this.level = lvl;
/* 44 */     this.price = p;
/*    */     
/* 46 */     this.name = name;
/* 47 */     if (this.enchant != null) {
/* 48 */       this.displayName = name.replace("_", " ");
/*    */     } else {
/* 50 */       this.displayName = this.enhancement.getDisplayName();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\LibraryEnchantment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */