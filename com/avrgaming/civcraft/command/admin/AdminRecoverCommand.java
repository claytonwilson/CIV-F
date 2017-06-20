/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.recover.RecoverStructuresAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.sql.SQLException;
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
/*     */ public class AdminRecoverCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  37 */     this.command = "/ad recover";
/*  38 */     this.displayName = "Admin recover";
/*     */     
/*  40 */     this.commands.put("structures", "Finds and recovers all of the 'broken' structures.");
/*  41 */     this.commands.put("listbroken", "Lists all broken structures and their locations.");
/*     */     
/*  43 */     this.commands.put("listorphantowns", "Lists all of the currently orphaned towns.");
/*  44 */     this.commands.put("listorphancivs", "Lists all of the currently orphaned civs.");
/*     */     
/*  46 */     this.commands.put("listorphanleaders", "Lists all orphaned leaders.");
/*  47 */     this.commands.put("fixleaders", "Looks up leaders of civilizations and sets them back in town.");
/*     */     
/*  49 */     this.commands.put("listorphanmayors", "List all leaders who are not mayors of the capitol.");
/*  50 */     this.commands.put("fixmayors", "Makes all leaders of civs mayors in the capitol town.");
/*     */     
/*  52 */     this.commands.put("forcesaveresidents", "force saves all residents");
/*  53 */     this.commands.put("forcesavetowns", "force saves all towns");
/*  54 */     this.commands.put("forcesavecivs", "force saves all civs");
/*     */     
/*  56 */     this.commands.put("listdefunctcivs", "list all towns with no leader group.");
/*  57 */     this.commands.put("killdefunctcivs", "attempts to delete defunct civs.");
/*     */     
/*  59 */     this.commands.put("listdefuncttowns", "list all towns with no mayors group");
/*  60 */     this.commands.put("killdefuncttowns", "attempts to delete defunct towns.");
/*     */     
/*  62 */     this.commands.put("listnocaptials", "list all civs with no capitols");
/*  63 */     this.commands.put("cleannocapitols", "clean out all civs with no capitols.");
/*     */   }
/*     */   
/*     */   public void listnocapitols_cmd()
/*     */   {
/*  68 */     CivMessage.sendHeading(this.sender, "Defunct Civs");
/*  69 */     for (Civilization civ : CivGlobal.getCivs())
/*     */     {
/*  71 */       Town town = CivGlobal.getTown(civ.getCapitolName());
/*  72 */       if (town == null) {
/*  73 */         CivMessage.send(this.sender, civ.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleannocapitols_cmd() {
/*  79 */     for (Civilization civ : )
/*     */     {
/*  81 */       Town town = CivGlobal.getTown(civ.getCapitolName());
/*  82 */       if (town == null) {
/*  83 */         CivMessage.send(this.sender, "Deleting " + civ.getName());
/*     */         try {
/*  85 */           civ.delete();
/*     */         } catch (SQLException e) {
/*  87 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void listdefunctcivs_cmd() {
/*  94 */     CivMessage.sendHeading(this.sender, "Defunct Civs");
/*  95 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  96 */       if (civ.getLeaderGroup() == null) {
/*  97 */         CivMessage.send(this.sender, civ.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void killdefunctcivs_cmd() {
/* 103 */     for (Civilization civ : ) {
/* 104 */       if (civ.getLeaderGroup() == null) {
/* 105 */         CivMessage.send(this.sender, "Deleteing " + civ.getName());
/*     */         try {
/* 107 */           civ.delete();
/*     */         } catch (SQLException e) {
/* 109 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void listdefuncttowns_cmd() {
/* 116 */     CivMessage.sendHeading(this.sender, "Defunct Towns");
/* 117 */     for (Town town : CivGlobal.getTowns()) {
/* 118 */       if (town.getMayorGroup() == null) {
/* 119 */         CivMessage.send(this.sender, town.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void killdefuncttowns_cmd() {
/* 125 */     for (Town town : ) {
/* 126 */       if (town.getMayorGroup() == null) {
/* 127 */         CivMessage.send(this.sender, "Deleting " + town.getName());
/*     */         try {
/* 129 */           town.delete();
/*     */         } catch (SQLException e) {
/* 131 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void forcesaveresidents_cmd() throws SQLException
/*     */   {
/* 139 */     for (Resident resident : ) {
/* 140 */       resident.saveNow();
/*     */     }
/* 142 */     CivMessage.sendSuccess(this.sender, "Saved " + CivGlobal.getResidents().size() + " residents");
/*     */   }
/*     */   
/*     */   public void forcesavetowns_cmd() throws SQLException {
/* 146 */     for (Town town : ) {
/* 147 */       town.saveNow();
/*     */     }
/* 149 */     CivMessage.sendSuccess(this.sender, "Saved " + CivGlobal.getTowns().size() + " towns");
/*     */   }
/*     */   
/*     */   public void forcesavecivs_cmd() throws SQLException {
/* 153 */     for (Civilization civ : ) {
/* 154 */       civ.saveNow();
/*     */     }
/* 156 */     CivMessage.sendSuccess(this.sender, "Saved " + CivGlobal.getCivs().size() + " civs");
/*     */   }
/*     */   
/*     */   public void listorphanmayors_cmd() {
/* 160 */     for (Civilization civ : ) {
/* 161 */       Town capitol = civ.getTown(civ.getCapitolName());
/* 162 */       if (capitol != null)
/*     */       {
/*     */ 
/*     */ 
/* 166 */         Resident leader = civ.getLeader();
/* 167 */         if (leader != null)
/*     */         {
/*     */ 
/*     */ 
/* 171 */           CivMessage.send(this.sender, "Broken: " + leader.getName() + " in civ: " + civ.getName() + " in capitol:" + capitol.getName());
/*     */         }
/*     */       }
/*     */     }
/* 175 */     CivMessage.sendSuccess(this.sender, "Finished");
/*     */   }
/*     */   
/*     */   public void fixmayors_cmd()
/*     */   {
/* 180 */     for (Civilization civ : ) {
/* 181 */       Town capitol = civ.getTown(civ.getCapitolName());
/* 182 */       if (capitol != null)
/*     */       {
/*     */ 
/*     */ 
/* 186 */         Resident leader = civ.getLeader();
/* 187 */         if (leader != null)
/*     */         {
/*     */ 
/*     */ 
/* 191 */           if (capitol.getMayorGroup() == null) {
/* 192 */             CivMessage.send(this.sender, "Town:" + capitol.getName() + " doesnt have a mayors group??");
/*     */           }
/*     */           else
/*     */           {
/* 196 */             capitol.getMayorGroup().addMember(leader);
/*     */             try {
/* 198 */               capitol.getMayorGroup().saveNow();
/*     */             } catch (SQLException e) {
/* 200 */               e.printStackTrace();
/*     */             }
/* 202 */             CivMessage.send(this.sender, "Fixed " + leader.getName() + " in civ: " + civ.getName() + " in capitol:" + capitol.getName());
/*     */           } }
/*     */       }
/*     */     }
/* 206 */     CivMessage.sendSuccess(this.sender, "Finished");
/*     */   }
/*     */   
/*     */ 
/*     */   public void fixleaders_cmd()
/*     */   {
/* 212 */     for (Civilization civ : ) {
/* 213 */       Resident res = civ.getLeader();
/* 214 */       if (res != null)
/*     */       {
/*     */ 
/*     */ 
/* 218 */         if (!res.hasTown()) {
/* 219 */           Town capitol = civ.getTown(civ.getCapitolName());
/* 220 */           if (capitol == null) {
/* 221 */             CivMessage.send(this.sender, "-- no capitol for civ " + civ.getName());
/*     */           }
/*     */           else {
/* 224 */             res.setTown(capitol);
/*     */             try {
/* 226 */               res.saveNow();
/*     */             } catch (SQLException e) {
/* 228 */               e.printStackTrace();
/*     */             }
/* 230 */             CivMessage.send(this.sender, "Fixed Civ:" + civ.getName() + " leader:" + res.getName());
/*     */           }
/*     */         }
/* 233 */         else if (!civ.getLeaderGroup().hasMember(res)) {
/* 234 */           civ.getLeaderGroup().addMember(res);
/*     */           try {
/* 236 */             civ.getLeaderGroup().saveNow();
/*     */           } catch (SQLException e) {
/* 238 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void listorphanleaders_cmd() {
/* 246 */     CivMessage.sendHeading(this.sender, "Orphan Leaders");
/*     */     
/* 248 */     for (Civilization civ : CivGlobal.getCivs()) {
/* 249 */       Resident res = civ.getLeader();
/* 250 */       if (res != null)
/*     */       {
/*     */ 
/*     */ 
/* 254 */         if (!res.hasTown()) {
/* 255 */           Town capitol = civ.getTown(civ.getCapitolName());
/* 256 */           if (capitol == null) {
/* 257 */             CivMessage.send(this.sender, "-- no capitol for civ " + civ.getName());
/*     */           }
/*     */           else
/*     */           {
/* 261 */             CivMessage.send(this.sender, "Broken Civ:" + civ.getName() + " Leader:" + res.getName());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void listorphantowns_cmd() {
/* 269 */     CivMessage.sendHeading(this.sender, "Orphan Towns");
/*     */     
/* 271 */     for (Town town : CivGlobal.orphanTowns) {
/* 272 */       CivMessage.send(this.sender, town.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void listorphancivs_cmd() {
/* 277 */     CivMessage.sendHeading(this.sender, "Orphan Civs");
/*     */     
/* 279 */     for (Civilization civ : CivGlobal.orphanCivs) {
/* 280 */       CivMessage.send(this.sender, civ.getName() + " capitol:" + civ.getCapitolName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void listbroken_cmd()
/*     */   {
/* 286 */     CivMessage.send(this.sender, "Starting List Broken Task");
/* 287 */     TaskMaster.syncTask(new RecoverStructuresAsyncTask(this.sender, true), 0L);
/*     */   }
/*     */   
/*     */   public void structures_cmd() {
/* 291 */     CivMessage.send(this.sender, "Starting Recover Task");
/* 292 */     TaskMaster.syncTask(new RecoverStructuresAsyncTask(this.sender, false), 0L);
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 298 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 303 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminRecoverCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */