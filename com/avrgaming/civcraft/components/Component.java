/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class Component
/*     */ {
/*  31 */   public static ConcurrentHashMap<String, ArrayList<Component>> componentsByType = new ConcurrentHashMap();
/*     */   
/*  33 */   public static ReentrantLock componentsLock = new ReentrantLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Buildable buildable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private HashMap<String, String> attributes = new HashMap();
/*  52 */   protected String typeName = null;
/*     */   
/*     */   public void createComponent(Buildable buildable) {
/*  55 */     createComponent(buildable, false);
/*     */   }
/*     */   
/*     */   public void createComponent(Buildable buildable, boolean async) {
/*  59 */     if (this.typeName == null) {
/*  60 */       if (async) {
/*  61 */         TaskMaster.asyncTask(new RegisterComponentAsync(buildable, this, getClass().getName(), true), 0L);
/*     */       } else {
/*  63 */         new RegisterComponentAsync(buildable, this, getClass().getName(), true).run();
/*     */       }
/*     */     }
/*  66 */     else if (async) {
/*  67 */       TaskMaster.asyncTask(new RegisterComponentAsync(buildable, this, this.typeName, true), 0L);
/*     */     } else {
/*  69 */       new RegisterComponentAsync(buildable, this, this.typeName, true).run();
/*     */     }
/*     */     
/*  72 */     this.buildable = buildable;
/*     */   }
/*     */   
/*     */   public void destroyComponent() {
/*  76 */     TaskMaster.asyncTask(new RegisterComponentAsync(null, this, getClass().getName(), false), 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */   public void onSave() {}
/*     */   
/*     */   public String getName()
/*     */   {
/*  86 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  90 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getString(String key) {
/*  94 */     return (String)this.attributes.get(key);
/*     */   }
/*     */   
/*     */   public double getDouble(String key) {
/*  98 */     return Double.valueOf((String)this.attributes.get(key)).doubleValue();
/*     */   }
/*     */   
/*     */   public void setAttribute(String key, String value) {
/* 102 */     this.attributes.put(key, value);
/*     */   }
/*     */   
/*     */   public Buildable getBuildable() {
/* 106 */     return this.buildable;
/*     */   }
/*     */   
/*     */   public void setBuildable(Buildable buildable) {
/* 110 */     this.buildable = buildable;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 114 */     if (this.buildable != null) {
/* 115 */       return this.buildable.isActive();
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\Component.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */