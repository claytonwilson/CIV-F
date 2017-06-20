/*    */ package com.avrgaming.civcraft.object;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import java.text.DecimalFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class AttrSource
/*    */ {
/*    */   public HashMap<String, Double> sources;
/*    */   public double total;
/*    */   AttrSource rate;
/*    */   
/*    */   public AttrSource(HashMap<String, Double> sources, double total, AttrSource rate)
/*    */   {
/* 16 */     this.sources = sources;
/* 17 */     this.total = total;
/* 18 */     this.rate = rate;
/*    */   }
/*    */   
/*    */   public ArrayList<String> getSourceDisplayString(String sourceColor, String valueColor) {
/* 22 */     ArrayList<String> out = new ArrayList();
/* 23 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 25 */     out.add(CivMessage.buildSmallTitle("Sources"));
/*    */     
/* 27 */     for (String source : this.sources.keySet()) {
/* 28 */       out.add(sourceColor + source + ": " + valueColor + df.format(this.sources.get(source)));
/*    */     }
/*    */     
/* 31 */     return out;
/*    */   }
/*    */   
/*    */   public ArrayList<String> getRateDisplayString(String sourceColor, String valueColor) {
/* 35 */     ArrayList<String> out = new ArrayList();
/* 36 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 38 */     if (this.rate != null) {
/* 39 */       out.add(CivMessage.buildSmallTitle("Rates"));
/*    */       
/* 41 */       for (String source : this.rate.sources.keySet()) {
/* 42 */         out.add(sourceColor + source + ": " + valueColor + df.format(((Double)this.rate.sources.get(source)).doubleValue() * 100.0D) + "%");
/*    */       }
/*    */     }
/* 45 */     return out;
/*    */   }
/*    */   
/*    */   public ArrayList<String> getTotalDisplayString(String sourceColor, String valueColor) {
/* 49 */     ArrayList<String> out = new ArrayList();
/* 50 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 52 */     out.add(CivMessage.buildSmallTitle("Totals"));
/* 53 */     out.add(sourceColor + "Total: " + valueColor + df.format(this.total) + sourceColor);
/* 54 */     return out;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\AttrSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */