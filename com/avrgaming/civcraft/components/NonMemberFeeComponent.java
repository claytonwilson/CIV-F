/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ public class NonMemberFeeComponent
/*    */   extends Component
/*    */ {
/*    */   private Buildable buildable;
/* 31 */   private double feeRate = 0.05D;
/*    */   
/*    */   public NonMemberFeeComponent(Buildable buildable) {
/* 34 */     this.buildable = buildable;
/*    */   }
/*    */   
/*    */   private String getKey()
/*    */   {
/* 39 */     return this.buildable.getDisplayName() + ":" + this.buildable.getId() + ":" + "fee";
/*    */   }
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 44 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getKey());
/*    */     
/* 46 */     if (entries.size() == 0) {
/* 47 */       this.buildable.sessionAdd(getKey(), this.feeRate);
/* 48 */       return;
/*    */     }
/*    */     
/* 51 */     this.feeRate = Double.valueOf(((SessionEntry)entries.get(0)).value).doubleValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onSave()
/*    */   {
/* 58 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getKey());
/*    */     
/* 60 */     if (entries.size() == 0) {
/* 61 */       this.buildable.sessionAdd(getKey(), this.feeRate);
/* 62 */       return;
/*    */     }
/* 64 */     CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, getKey(), this.feeRate);
/*    */   }
/*    */   
/*    */   public double getFeeRate()
/*    */   {
/* 69 */     return this.feeRate;
/*    */   }
/*    */   
/*    */   public void setFeeRate(double feeRate)
/*    */   {
/* 74 */     this.feeRate = feeRate;
/* 75 */     onSave();
/*    */   }
/*    */   
/*    */   public Buildable getBuildable()
/*    */   {
/* 80 */     return this.buildable;
/*    */   }
/*    */   
/*    */   public String getFeeString() {
/* 84 */     DecimalFormat df = new DecimalFormat();
/* 85 */     return df.format(getFeeRate() * 100.0D) + "%";
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\NonMemberFeeComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */