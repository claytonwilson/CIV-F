/*     */ package com.avrgaming.civcraft.permission;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
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
/*     */ public class PermissionNode
/*     */ {
/*     */   private String type;
/*     */   private boolean permitOwner;
/*     */   private boolean permitGroup;
/*     */   private boolean permitOthers;
/*     */   
/*     */   public PermissionNode(String type)
/*     */   {
/*  50 */     setType(type);
/*  51 */     this.permitOwner = true;
/*  52 */     this.permitGroup = true;
/*  53 */     this.permitOthers = false;
/*     */   }
/*     */   
/*     */   public String getSaveString() {
/*  57 */     return this.type + ":" + this.permitOwner + ":" + this.permitGroup + ":" + this.permitOthers;
/*     */   }
/*     */   
/*     */   public void loadFromString(String src) throws CivException {
/*  61 */     String[] split = src.split(":");
/*  62 */     setType(split[0]);
/*     */     
/*  64 */     this.permitOwner = Boolean.valueOf(split[1]).booleanValue();
/*  65 */     this.permitGroup = Boolean.valueOf(split[2]).booleanValue();
/*  66 */     this.permitOthers = Boolean.valueOf(split[3]).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isPermitOwner()
/*     */   {
/*  71 */     return this.permitOwner;
/*     */   }
/*     */   
/*     */   public void setPermitOwner(boolean permitOwner) {
/*  75 */     this.permitOwner = permitOwner;
/*     */   }
/*     */   
/*     */   public boolean isPermitGroup() {
/*  79 */     return this.permitGroup;
/*     */   }
/*     */   
/*     */   public void setPermitGroup(boolean permitGroup) {
/*  83 */     this.permitGroup = permitGroup;
/*     */   }
/*     */   
/*     */   public boolean isPermitOthers() {
/*  87 */     return this.permitOthers;
/*     */   }
/*     */   
/*     */   public void setPermitOthers(boolean permitOthers) {
/*  91 */     this.permitOthers = permitOthers;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  95 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  99 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getString() {
/* 103 */     String ret = "";
/* 104 */     if (isPermitOwner()) {
/* 105 */       ret = ret + "Owner: yes ";
/*     */     } else {
/* 107 */       ret = ret + "Owner: no ";
/*     */     }
/* 109 */     if (isPermitGroup()) {
/* 110 */       ret = ret + "Group: yes ";
/*     */     } else {
/* 112 */       ret = ret + "Group: no ";
/*     */     }
/* 114 */     if (isPermitOthers()) {
/* 115 */       ret = ret + "Others: yes ";
/*     */     } else {
/* 117 */       ret = ret + "Others: no ";
/*     */     }
/* 119 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\permission\PermissionNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */