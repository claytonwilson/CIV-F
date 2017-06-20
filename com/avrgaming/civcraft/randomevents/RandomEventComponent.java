/*    */ package com.avrgaming.civcraft.randomevents;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public abstract class RandomEventComponent
/*    */ {
/*    */   private String name;
/* 10 */   private HashMap<String, String> attributes = new HashMap();
/*    */   private RandomEvent parent;
/*    */   
/*    */   public String getName() {
/* 14 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 18 */     this.name = name;
/*    */   }
/*    */   
/* 21 */   public String getString(String key) { return (String)this.attributes.get(key); }
/*    */   
/*    */   public double getDouble(String key)
/*    */   {
/* 25 */     return Double.valueOf((String)this.attributes.get(key)).doubleValue();
/*    */   }
/*    */   
/*    */   public void setAttribute(String key, String value) {
/* 29 */     this.attributes.put(key, value);
/*    */   }
/*    */   
/*    */   protected Town getParentTown() {
/* 33 */     return this.parent.getTown();
/*    */   }
/*    */   
/*    */   protected RandomEvent getParent() {
/* 37 */     return this.parent;
/*    */   }
/*    */   
/*    */   protected void sendMessage(String message) {
/* 41 */     com.avrgaming.civcraft.main.CivMessage.sendTown(this.parent.getTown(), message);
/* 42 */     this.parent.savedMessages.add(message);
/*    */   }
/*    */   
/*    */ 
/* 46 */   public void createComponent(RandomEvent parent) { this.parent = parent; }
/*    */   public abstract void process();
/* 48 */   public boolean onCheck() { return false; }
/*    */   public void onStart() {}
/*    */   public void onCleanup() {}
/* 51 */   public boolean requiresActivation() { return false; }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\RandomEventComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */