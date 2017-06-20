/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class DiplomacyManager
/*     */ {
/*     */   private Civilization ourCiv;
/*  47 */   private HashMap<Integer, Relation> relations = new HashMap();
/*     */   
/*     */ 
/*  50 */   private int warCount = 0;
/*     */   
/*     */   public DiplomacyManager(Civilization civ) {
/*  53 */     this.ourCiv = civ;
/*     */   }
/*     */   
/*     */   public boolean atWarWith(Civilization other)
/*     */   {
/*  58 */     if (this.ourCiv.getId() == other.getId()) {
/*  59 */       return false;
/*     */     }
/*     */     
/*  62 */     Relation relation = (Relation)this.relations.get(Integer.valueOf(other.getId()));
/*  63 */     if ((relation != null) && (relation.getStatus() == Relation.Status.WAR)) {
/*  64 */       return true;
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAtWar() {
/*  70 */     return this.warCount != 0;
/*     */   }
/*     */   
/*     */   public void deleteRelation(Relation relation)
/*     */   {
/*  75 */     if ((relation.getStatus() == Relation.Status.WAR) && 
/*  76 */       (this.relations.containsKey(Integer.valueOf(relation.getOtherCiv().getId())))) {
/*  77 */       this.warCount -= 1;
/*  78 */       if (this.warCount < 0) {
/*  79 */         this.warCount = 0;
/*     */       }
/*     */     }
/*  82 */     this.relations.remove(Integer.valueOf(relation.getOtherCiv().getId()));
/*     */     
/*  84 */     Relation theirRelation = relation.getOtherCiv().getDiplomacyManager().getRelation(this.ourCiv);
/*  85 */     if (theirRelation != null) {
/*     */       try {
/*  87 */         relation.getOtherCiv().getDiplomacyManager().relations.remove(Integer.valueOf(theirRelation.getOtherCiv().getId()));
/*  88 */         theirRelation.delete();
/*     */       } catch (SQLException e) {
/*  90 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*  95 */       relation.delete();
/*     */     } catch (SQLException e) {
/*  97 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteAllRelations() {
/* 102 */     LinkedList<Relation> removeUs = new LinkedList();
/*     */     
/* 104 */     for (Relation relation : this.relations.values()) {
/* 105 */       removeUs.add(relation);
/*     */     }
/*     */     
/* 108 */     for (Relation relation : removeUs) {
/* 109 */       deleteRelation(relation);
/*     */     }
/*     */     
/* 112 */     this.relations.clear();
/*     */   }
/*     */   
/*     */   public void setAggressor(Civilization aggressor, Civilization otherCiv) {
/* 116 */     Relation relation = (Relation)this.relations.get(Integer.valueOf(otherCiv.getId()));
/* 117 */     if (relation != null) {
/* 118 */       relation.setAggressor(aggressor);
/* 119 */       relation.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setRelation(Civilization otherCiv, Relation.Status status, Date expires) {
/* 124 */     Relation relation = (Relation)this.relations.get(Integer.valueOf(otherCiv.getId()));
/*     */     
/* 126 */     if (relation == null) {
/* 127 */       this.relations.put(Integer.valueOf(otherCiv.getId()), new Relation(this.ourCiv, otherCiv, status, expires));
/*     */     } else {
/* 129 */       if (relation.getStatus() == status) {
/* 130 */         return;
/*     */       }
/*     */       
/* 133 */       if (relation.getStatus() == Relation.Status.WAR)
/*     */       {
/* 135 */         this.warCount -= 1;
/*     */       }
/*     */       
/* 138 */       if (expires != null) {
/* 139 */         relation.setExpires(expires);
/*     */       }
/* 141 */       relation.setStatus(status);
/*     */     }
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
/* 158 */     if (status == Relation.Status.WAR) {
/* 159 */       this.warCount += 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public Relation.Status getRelationStatus(Civilization otherCiv) {
/* 164 */     if (otherCiv.getId() == this.ourCiv.getId()) {
/* 165 */       return Relation.Status.ALLY;
/*     */     }
/*     */     
/* 168 */     Relation relation = (Relation)this.relations.get(Integer.valueOf(otherCiv.getId()));
/* 169 */     if (relation == null) {
/* 170 */       return Relation.Status.NEUTRAL;
/*     */     }
/* 172 */     return relation.getStatus();
/*     */   }
/*     */   
/*     */   public Relation getRelation(Civilization otherCiv) {
/* 176 */     return (Relation)this.relations.get(Integer.valueOf(otherCiv.getId()));
/*     */   }
/*     */   
/*     */   public void addRelation(Relation relation) {
/* 180 */     Relation currentRelation = (Relation)this.relations.get(Integer.valueOf(relation.getOtherCiv().getId()));
/*     */     
/* 182 */     if ((relation.getStatus() == Relation.Status.WAR) && (
/* 183 */       (currentRelation == null) || (currentRelation.getStatus() != Relation.Status.WAR))) {
/* 184 */       this.warCount += 1;
/*     */     }
/*     */     
/* 187 */     this.relations.put(Integer.valueOf(relation.getOtherCiv().getId()), relation);
/*     */   }
/*     */   
/*     */   public Collection<Relation> getRelations() {
/* 191 */     return this.relations.values();
/*     */   }
/*     */   
/*     */   public int getWarCount() {
/* 195 */     return this.warCount;
/*     */   }
/*     */   
/*     */   public boolean atWarWith(Player attacker) {
/* 199 */     Resident resident = CivGlobal.getResident(attacker);
/* 200 */     if (resident == null) {
/* 201 */       return false;
/*     */     }
/* 203 */     if (!resident.hasTown()) {
/* 204 */       return false;
/*     */     }
/*     */     
/* 207 */     return atWarWith(resident.getTown().getCiv());
/*     */   }
/*     */   
/*     */   public Relation.Status getRelationStatus(Player player) {
/* 211 */     Resident resident = CivGlobal.getResident(player);
/* 212 */     if (resident == null) {
/* 213 */       return Relation.Status.NEUTRAL;
/*     */     }
/* 215 */     if (!resident.hasTown()) {
/* 216 */       return Relation.Status.NEUTRAL;
/*     */     }
/*     */     
/* 219 */     return getRelationStatus(resident.getTown().getCiv());
/*     */   }
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
/*     */   public boolean isHostileWith(Resident resident)
/*     */   {
/* 233 */     return isHostileWith(resident.getCiv());
/*     */   }
/*     */   
/*     */   public boolean isHostileWith(Civilization civ) {
/* 237 */     Relation relation = (Relation)this.relations.get(Integer.valueOf(civ.getId()));
/* 238 */     if (relation == null) {
/* 239 */       return false;
/*     */     }
/* 241 */     switch (relation.getStatus()) {
/*     */     case HOSTILE: 
/*     */     case NEUTRAL: 
/* 244 */       return true;
/*     */     }
/* 246 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\DiplomacyManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */