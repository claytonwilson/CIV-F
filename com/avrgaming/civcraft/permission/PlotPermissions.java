/*     */ package com.avrgaming.civcraft.permission;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.util.ArrayList;
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
/*     */ public class PlotPermissions
/*     */ {
/*     */   public static enum Type
/*     */   {
/*  31 */     BUILD, 
/*  32 */     DESTROY, 
/*  33 */     INTERACT, 
/*  34 */     ITEMUSE;
/*     */   }
/*     */   
/*  37 */   public PermissionNode build = new PermissionNode("build");
/*  38 */   public PermissionNode destroy = new PermissionNode("destroy");
/*  39 */   public PermissionNode interact = new PermissionNode("interact");
/*  40 */   public PermissionNode itemUse = new PermissionNode("itemUse");
/*     */   
/*     */ 
/*     */   private boolean fire;
/*     */   
/*     */ 
/*     */   private boolean mobs;
/*     */   
/*     */ 
/*     */   private Resident owner;
/*     */   
/*     */ 
/*  52 */   private ArrayList<PermissionGroup> groups = new ArrayList();
/*     */   
/*     */   public String getSaveString()
/*     */   {
/*  56 */     String ownerString = "";
/*  57 */     if (this.owner != null) {
/*  58 */       ownerString = this.owner.getName();
/*     */     }
/*     */     
/*  61 */     String groupString = "0";
/*  62 */     if (this.groups.size() != 0) {
/*  63 */       for (PermissionGroup grp : this.groups) {
/*  64 */         groupString = groupString + grp.getId() + ":";
/*     */       }
/*     */     }
/*     */     
/*  68 */     return this.build.getSaveString() + "," + this.destroy.getSaveString() + "," + this.interact.getSaveString() + "," + this.itemUse.getSaveString() + "," + ownerString + "," + groupString + "," + this.fire + "," + this.mobs;
/*     */   }
/*     */   
/*     */   public void loadFromSaveString(Town town, String src) throws CivException {
/*  72 */     String[] split = src.split(",");
/*     */     
/*  74 */     this.build.loadFromString(split[0]);
/*  75 */     this.destroy.loadFromString(split[1]);
/*  76 */     this.interact.loadFromString(split[2]);
/*  77 */     this.itemUse.loadFromString(split[3]);
/*     */     
/*  79 */     setOwner(CivGlobal.getResident(split[4]));
/*  80 */     String[] grpString = split[5].split(":");
/*     */     String[] arrayOfString1;
/*  82 */     int j = (arrayOfString1 = grpString).length; for (int i = 0; i < j; i++) { String gstr = arrayOfString1[i];
/*  83 */       gstr = gstr.trim();
/*  84 */       if ((!gstr.equals("0")) && (!gstr.equals("")))
/*     */       {
/*     */ 
/*  87 */         PermissionGroup group = CivGlobal.getPermissionGroup(town, Integer.valueOf(gstr));
/*  88 */         addGroup(group);
/*     */       }
/*     */     }
/*  91 */     if (split.length > 7) {
/*  92 */       this.fire = Boolean.valueOf(split[6]).booleanValue();
/*  93 */       this.mobs = Boolean.valueOf(split[7]).booleanValue();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isFire()
/*     */   {
/* 101 */     return this.fire;
/*     */   }
/*     */   
/*     */   public void setFire(boolean fire) {
/* 105 */     this.fire = fire;
/*     */   }
/*     */   
/*     */   public boolean isMobs() {
/* 109 */     return this.mobs;
/*     */   }
/*     */   
/*     */   public void setMobs(boolean mobs) {
/* 113 */     this.mobs = mobs;
/*     */   }
/*     */   
/*     */   public String getBuildString() {
/* 117 */     return this.build.getString();
/*     */   }
/*     */   
/*     */   public String getDestroyString() {
/* 121 */     return this.destroy.getString();
/*     */   }
/*     */   
/*     */   public String getInteractString() {
/* 125 */     return this.interact.getString();
/*     */   }
/*     */   
/*     */   public String getItemUseString() {
/* 129 */     return this.itemUse.getString();
/*     */   }
/*     */   
/*     */   public Resident getOwner() {
/* 133 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(Resident owner) {
/* 137 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   private boolean checkPermissionNode(PermissionNode node, Resident resident) {
/* 141 */     if (node != null) {
/* 142 */       if ((this.owner == resident) && (node.isPermitOwner())) {
/* 143 */         return true;
/*     */       }
/* 145 */       if ((this.owner != null) && (this.owner.isFriend(resident)) && (node.isPermitOwner())) {
/* 146 */         return true;
/*     */       }
/* 148 */       if ((this.groups.size() != 0) && (node.isPermitGroup())) {
/* 149 */         for (PermissionGroup group : this.groups) {
/* 150 */           if (group.hasMember(resident)) {
/* 151 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 156 */       if (node.isPermitOthers()) {
/* 157 */         return true;
/*     */       }
/*     */     }
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasPermission(Type type, Resident resident) {
/* 164 */     if (resident.isPermOverride()) {
/* 165 */       return true;
/*     */     }
/*     */     
/* 168 */     switch (type) {
/*     */     case BUILD: 
/* 170 */       return checkPermissionNode(this.build, resident);
/*     */     case DESTROY: 
/* 172 */       return checkPermissionNode(this.destroy, resident);
/*     */     case INTERACT: 
/* 174 */       return checkPermissionNode(this.interact, resident);
/*     */     case ITEMUSE: 
/* 176 */       return checkPermissionNode(this.itemUse, resident);
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   public void addGroup(PermissionGroup grp) {
/* 185 */     if (grp == null) {
/* 186 */       return;
/*     */     }
/*     */     
/* 189 */     if (!this.groups.contains(grp)) {
/* 190 */       this.groups.add(grp);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeGroup(PermissionGroup grp) {
/* 195 */     this.groups.remove(grp);
/*     */   }
/*     */   
/*     */   public ArrayList<PermissionGroup> getGroups() {
/* 199 */     return this.groups;
/*     */   }
/*     */   
/*     */   public void resetPerms() {
/* 203 */     this.build.setPermitOwner(true);
/* 204 */     this.build.setPermitGroup(true);
/* 205 */     this.build.setPermitOthers(false);
/*     */     
/* 207 */     this.destroy.setPermitOwner(true);
/* 208 */     this.destroy.setPermitGroup(true);
/* 209 */     this.destroy.setPermitOthers(false);
/*     */     
/* 211 */     this.interact.setPermitOwner(true);
/* 212 */     this.interact.setPermitGroup(true);
/* 213 */     this.interact.setPermitOthers(false);
/*     */     
/* 215 */     this.itemUse.setPermitOwner(true);
/* 216 */     this.itemUse.setPermitGroup(true);
/* 217 */     this.itemUse.setPermitOthers(false);
/*     */   }
/*     */   
/*     */   public String getGroupString() {
/* 221 */     String out = "";
/*     */     
/* 223 */     for (PermissionGroup grp : this.groups) {
/* 224 */       out = out + grp.getName() + ", ";
/*     */     }
/* 226 */     return out;
/*     */   }
/*     */   
/*     */   public void clearGroups() {
/* 230 */     this.groups.clear();
/*     */   }
/*     */   
/*     */   public void replaceGroups(PermissionGroup defaultGroup) {
/* 234 */     this.groups.clear();
/* 235 */     addGroup(defaultGroup);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\permission\PlotPermissions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */