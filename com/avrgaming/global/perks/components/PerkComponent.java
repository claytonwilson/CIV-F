/*    */ package com.avrgaming.global.perks.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.perks.NotVerifiedException;
/*    */ import com.avrgaming.global.perks.Perk;
/*    */ import com.avrgaming.global.perks.PerkManager;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PerkComponent
/*    */ {
/* 16 */   private HashMap<String, String> attributes = new HashMap();
/*    */   private String name;
/*    */   private Perk parent;
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/* 24 */   public void setName(String name) { this.name = name; }
/*    */   
/*    */   public String getString(String key)
/*    */   {
/* 28 */     return (String)this.attributes.get(key);
/*    */   }
/*    */   
/*    */   public double getDouble(String key) {
/* 32 */     return Double.valueOf((String)this.attributes.get(key)).doubleValue();
/*    */   }
/*    */   
/*    */   public void setAttribute(String key, String value) {
/* 36 */     this.attributes.put(key, value);
/*    */   }
/*    */   
/*    */   public Perk getParent() {
/* 40 */     return this.parent;
/*    */   }
/*    */   
/* 43 */   public void setParent(Perk parent) { this.parent = parent; }
/*    */   
/*    */   public void markAsUsed(Resident resident)
/*    */   {
/* 47 */     getParent().count -= 1;
/* 48 */     if (getParent().count <= 0) {
/* 49 */       resident.perks.remove(getParent().getIdent());
/*    */     }
/*    */     try
/*    */     {
/* 53 */       PerkManager.markAsUsed(resident, getParent());
/*    */     } catch (SQLException e) {
/* 55 */       e.printStackTrace();
/*    */     } catch (NotVerifiedException e) {
/* 57 */       CivMessage.send(resident, "§cYou're not verified!? Please contact an admin.");
/* 58 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onActivate(Resident resident) {}
/*    */   
/*    */   public void createComponent() {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\components\PerkComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */