/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuffManager
/*     */ {
/*  37 */   private HashMap<String, Buff> buffs = new HashMap();
/*     */   
/*     */ 
/*  40 */   private HashMap<String, Buff> effectiveBuffs = new HashMap();
/*     */   
/*     */   public void clearBuffs()
/*     */   {
/*  44 */     synchronized (this) {
/*  45 */       this.buffs.clear();
/*  46 */       this.effectiveBuffs.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasBuffId(String id, HashMap<String, Buff> map)
/*     */   {
/*  52 */     for (Buff buff : map.values()) {
/*  53 */       if (buff.getId().equals(id)) {
/*  54 */         return true;
/*     */       }
/*     */     }
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   public void addBuff(String buffkey, String buff_id, String source) throws CivException
/*     */   {
/*  62 */     synchronized (this) {
/*  63 */       if (this.buffs.containsKey(buffkey)) {
/*  64 */         throw new CivException("Already contains buff key:" + buffkey);
/*     */       }
/*     */       
/*  67 */       Buff buff = new Buff(buffkey, buff_id, source);
/*  68 */       this.buffs.put(buff.getKey(), buff);
/*  69 */       if ((buff.isStackable()) || (!hasBuffId(buff_id, this.effectiveBuffs))) {
/*  70 */         this.effectiveBuffs.put(buff.getKey(), buff);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeBuff(String buff_key)
/*     */   {
/*  77 */     this.buffs.remove(buff_key);
/*  78 */     this.effectiveBuffs.remove(buff_key);
/*     */   }
/*     */   
/*     */   public Collection<Buff> getEffectiveBuffs() {
/*  82 */     return this.effectiveBuffs.values();
/*     */   }
/*     */   
/*     */   public Collection<Buff> getEffectiveBuffs(String buff_id) {
/*  86 */     ArrayList<Buff> returnList = new ArrayList();
/*  87 */     synchronized (this) {
/*  88 */       for (Buff buff : this.effectiveBuffs.values()) {
/*  89 */         if (buff.getParent().equals(buff_id)) {
/*  90 */           returnList.add(buff);
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getEffectiveDouble(String buff_id)
/*     */   {
/* 105 */     double ret = 0.0D;
/*     */     
/* 107 */     synchronized (this) {
/* 108 */       for (Buff buff : this.effectiveBuffs.values()) {
/* 109 */         if (buff.getParent().equals(buff_id)) {
/* 110 */           ret += Double.valueOf(buff.getValue()).doubleValue();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 115 */     return ret;
/*     */   }
/*     */   
/*     */   public int getEffectiveInt(String buff_id) {
/* 119 */     int ret = 0;
/*     */     
/* 121 */     synchronized (this) {
/* 122 */       for (Buff buff : this.effectiveBuffs.values()) {
/* 123 */         if (buff.getParent().equals(buff_id)) {
/* 124 */           ret += Integer.valueOf(buff.getValue()).intValue();
/*     */         }
/*     */       }
/*     */     }
/* 128 */     return ret;
/*     */   }
/*     */   
/*     */   public String getValue(String buff_id)
/*     */   {
/* 133 */     synchronized (this) {
/* 134 */       for (Buff buff : this.effectiveBuffs.values()) {
/* 135 */         if (buff.getParent().equals(buff_id)) {
/* 136 */           return buff.getValue();
/*     */         }
/*     */       }
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public void debugPrint() {
/* 144 */     String out = "";
/* 145 */     for (Buff buff : this.buffs.values()) {
/* 146 */       out = out + "key:" + buff.getKey() + " id:" + buff.getId() + " source:" + buff.getSource() + ",";
/*     */     }
/*     */     
/* 149 */     out = "";
/* 150 */     for (Buff buff : this.effectiveBuffs.values()) {
/* 151 */       out = out + "key:" + buff.getKey() + " id:" + buff.getId() + " source:" + buff.getSource() + ",";
/*     */     }
/* 153 */     CivLog.info(out);
/*     */   }
/*     */   
/*     */   public boolean hasBuff(String id) {
/* 157 */     for (Buff buff : this.buffs.values()) {
/* 158 */       if ((buff.getId().equals(id)) || (buff.getParent().equals(id))) {
/* 159 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   public Collection<Buff> getAllBuffs() {
/* 167 */     return this.buffs.values();
/*     */   }
/*     */   
/*     */   public boolean hasBuffKey(String key)
/*     */   {
/* 172 */     return this.buffs.containsKey(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\BuffManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */