/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*     */ import com.avrgaming.civcraft.components.PlayerProximityComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Relation;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import org.bukkit.Location;
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
/*     */ public class ScoutTower
/*     */   extends Structure
/*     */ {
/*     */   double range;
/*     */   private PlayerProximityComponent proximityComponent;
/*  46 */   private int reportSeconds = 60;
/*  47 */   private int count = 0;
/*     */   
/*     */   public ScoutTower(ResultSet rs) throws SQLException, CivException {
/*  50 */     super(rs);
/*     */   }
/*     */   
/*     */   protected ScoutTower(Location center, String id, Town town) throws CivException
/*     */   {
/*  55 */     super(center, id, town);
/*  56 */     this.hitpoints = getMaxHitPoints();
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/*  61 */     super.loadSettings();
/*     */     try
/*     */     {
/*  64 */       this.range = CivSettings.getDouble(CivSettings.warConfig, "scout_tower.range");
/*  65 */       this.proximityComponent = new PlayerProximityComponent();
/*  66 */       this.proximityComponent.createComponent(this);
/*     */       
/*  68 */       this.proximityComponent.setBuildable(this);
/*  69 */       this.proximityComponent.setCenter(getCenterLocation());
/*  70 */       this.proximityComponent.setRadius(this.range);
/*     */       
/*  72 */       this.reportSeconds = ((int)CivSettings.getDouble(CivSettings.warConfig, "scout_tower.update"));
/*     */     }
/*     */     catch (InvalidConfiguration e)
/*     */     {
/*  76 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void scoutDebug(String str) {
/*  81 */     if ((getCiv().scoutDebug) && (getCiv().scoutDebugPlayer != null))
/*     */     {
/*     */       try {
/*  84 */         player = CivGlobal.getPlayer(getCiv().scoutDebugPlayer);
/*     */       } catch (CivException e) { Player player;
/*     */         return; }
/*     */       Player player;
/*  88 */       CivMessage.send(player, "§e[ScoutDebug] " + str);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMaxHitPoints()
/*     */   {
/*  94 */     double rate = 1.0D;
/*  95 */     if (getTown().getBuffManager().hasBuff("buff_chichen_itza_tower_hp")) {
/*  96 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_chichen_itza_tower_hp");
/*  97 */       rate += getTown().getBuffManager().getEffectiveDouble("buff_barricade");
/*     */     }
/*  99 */     return (int)(this.info.max_hitpoints * rate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void process(HashSet<String> alreadyAnnounced)
/*     */   {
/* 108 */     this.count += 1;
/* 109 */     if (this.count < this.reportSeconds) {
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     this.count = 0;
/* 114 */     boolean empty = true;
/*     */     
/* 116 */     for (PlayerLocationCache pc : this.proximityComponent.tryGetNearbyPlayers(true)) {
/* 117 */       empty = false;
/* 118 */       scoutDebug("Inspecting player:" + pc.getName());
/*     */       try
/*     */       {
/* 121 */         player = CivGlobal.getPlayer(pc.getName());
/*     */       } catch (CivException e) { Player player;
/* 123 */         scoutDebug("not online?"); return;
/*     */       }
/*     */       
/*     */       Player player;
/* 127 */       if (player.isOp()) {
/* 128 */         scoutDebug("player is op");
/*     */       }
/*     */       else
/*     */       {
/* 132 */         Location center = getCenterLocation().getLocation();
/*     */         
/*     */ 
/* 135 */         if (alreadyAnnounced.contains(getCiv().getName() + ":" + player.getName())) {
/* 136 */           scoutDebug("already announced:" + pc.getName());
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 141 */           String relationName = "";
/* 142 */           String relationColor = "";
/* 143 */           if (!getTown().isOutlaw(player.getName()))
/*     */           {
/* 145 */             Resident resident = CivGlobal.getResident(player);
/* 146 */             if ((resident != null) && (resident.hasTown()) && (resident.getCiv() == getCiv())) {
/* 147 */               scoutDebug("same civ");
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 152 */               Relation.Status relation = getCiv().getDiplomacyManager().getRelationStatus(player);
/* 153 */               switch (relation)
/*     */               {
/*     */ 
/*     */               case PEACE: 
/*     */               case WAR: 
/* 158 */                 scoutDebug("ally or peace");
/* 159 */                 break;
/*     */               
/*     */ 
/*     */ 
/*     */               default: 
/* 164 */                 relationName = relation.name();
/* 165 */                 relationColor = Relation.getRelationColor(relation);
/* 166 */                 break; }
/* 167 */                } } else { relationName = "OUTLAW";
/* 168 */             relationColor = "§e";
/*     */             
/*     */ 
/*     */ 
/* 172 */             if (center.getWorld() != getCorner().getLocation().getWorld()) {
/* 173 */               scoutDebug("wrong world");
/*     */ 
/*     */ 
/*     */             }
/* 177 */             else if (center.distance(player.getLocation()) < this.range)
/*     */             {
/* 179 */               CivMessage.sendScout(getCiv(), "Scout tower detected " + relationColor + 
/* 180 */                 player.getName() + "(" + relationName + ")" + "§f" + 
/* 181 */                 " at (" + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ() + ") in " + 
/* 182 */                 getTown().getName());
/* 183 */               alreadyAnnounced.add(getCiv().getName() + ":" + player.getName());
/*     */             }
/*     */           }
/*     */         }
/*     */       } }
/* 188 */     if (empty) {
/* 189 */       scoutDebug("Proximity cache was empty");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 195 */     return "tower";
/*     */   }
/*     */   
/*     */   public int getReportSeconds() {
/* 199 */     return this.reportSeconds;
/*     */   }
/*     */   
/*     */   public void setReportSeconds(int reportSeconds) {
/* 203 */     this.reportSeconds = reportSeconds;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\ScoutTower.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */