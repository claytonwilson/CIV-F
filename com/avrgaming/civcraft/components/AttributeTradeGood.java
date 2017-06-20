/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*    */ import com.avrgaming.civcraft.items.BonusGoodie;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class AttributeTradeGood extends AttributeBase
/*    */ {
/* 11 */   HashSet<String> goods = new HashSet();
/*    */   double value;
/*    */   String attribute;
/*    */   
/*    */   public void createComponent(Buildable buildable, boolean async)
/*    */   {
/* 17 */     super.createComponent(buildable, async);
/*    */     
/* 19 */     String[] good_ids = getString("goods").split(",");
/* 20 */     String[] arrayOfString1; int j = (arrayOfString1 = good_ids).length; for (int i = 0; i < j; i++) { String id = arrayOfString1[i];
/* 21 */       this.goods.add(id.toLowerCase().trim());
/*    */     }
/*    */     
/* 24 */     this.attribute = getString("attribute");
/* 25 */     this.value = getDouble("value");
/*    */   }
/*    */   
/*    */ 
/*    */   public double getGenerated()
/*    */   {
/* 31 */     if (!getBuildable().isActive()) {
/* 32 */       return 0.0D;
/*    */     }
/*    */     
/* 35 */     Town town = getBuildable().getTown();
/* 36 */     double generated = 0.0D;
/*    */     
/* 38 */     for (BonusGoodie goodie : town.getBonusGoodies()) {
/* 39 */       if (this.goods.contains(goodie.getConfigTradeGood().id)) {
/* 40 */         generated += this.value;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 45 */     return generated;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeTradeGood.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */