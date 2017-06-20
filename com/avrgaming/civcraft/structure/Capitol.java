/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.ProjectileArrowComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.ControlPoint;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureBlock;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
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
/*     */ public class Capitol
/*     */   extends TownHall
/*     */ {
/*  55 */   private HashMap<Integer, ProjectileArrowComponent> arrowTowers = new HashMap();
/*     */   private StructureSign respawnSign;
/*  57 */   private int index = 0;
/*     */   
/*     */   public Capitol(ResultSet rs) throws SQLException, CivException {
/*  60 */     super(rs);
/*     */   }
/*     */   
/*     */   protected Capitol(Location center, String id, Town town)
/*     */     throws CivException
/*     */   {
/*  66 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   private RespawnLocationHolder getSelectedHolder() {
/*  70 */     ArrayList<RespawnLocationHolder> respawnables = getTown().getCiv().getAvailableRespawnables();
/*  71 */     return (RespawnLocationHolder)respawnables.get(this.index);
/*     */   }
/*     */   
/*     */   private void changeIndex(int newIndex) {
/*  75 */     ArrayList<RespawnLocationHolder> respawnables = getTown().getCiv().getAvailableRespawnables();
/*     */     
/*  77 */     if (this.respawnSign != null) {
/*     */       try {
/*  79 */         this.respawnSign.setText("Respawn At\n§2" + CivColor.BOLD + ((RespawnLocationHolder)respawnables.get(newIndex)).getRespawnName());
/*  80 */         this.index = newIndex;
/*     */       } catch (IndexOutOfBoundsException e) {
/*  82 */         if (respawnables.size() > 0) {
/*  83 */           this.respawnSign.setText("Respawn At\n§2" + CivColor.BOLD + ((RespawnLocationHolder)respawnables.get(0)).getRespawnName());
/*  84 */           this.index = 0;
/*     */         }
/*     */       }
/*     */       
/*  88 */       this.respawnSign.update();
/*     */     } else {
/*  90 */       CivLog.warning("Could not find civ spawn sign:" + getId() + " at " + getCorner());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/*  97 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  99 */     if (resident == null) {
/* 100 */       return;
/*     */     }
/*     */     
/* 103 */     if (!War.isWarTime()) {
/*     */       return;
/*     */     }
/*     */     String str;
/* 107 */     switch ((str = sign.getAction()).hashCode()) {case 3377907:  if (str.equals("next")) {} break; case 3449395:  if (str.equals("prev")) break; break; case 1097387304:  if (!str.equals("respawn"))
/*     */       {
/* 109 */         return;changeIndex(this.index - 1);
/* 110 */         return;
/*     */         
/* 112 */         changeIndex(this.index + 1);
/*     */       }
/*     */       else {
/* 115 */         ArrayList<RespawnLocationHolder> respawnables = getTown().getCiv().getAvailableRespawnables();
/* 116 */         if (this.index >= respawnables.size()) {
/* 117 */           this.index = 0;
/* 118 */           changeIndex(this.index);
/* 119 */           CivMessage.sendError(resident, "Unable to find selected respawn point. We've reset the sign, please select respawn point again.");
/* 120 */           return;
/*     */         }
/*     */         
/* 123 */         RespawnLocationHolder holder = getSelectedHolder();
/* 124 */         int respawnTimeSeconds = getRespawnTime();
/* 125 */         Date now = new Date();
/*     */         
/* 127 */         if (resident.getLastKilledTime() != null) {
/* 128 */           long secondsLeft = resident.getLastKilledTime().getTime() + respawnTimeSeconds * 1000 - now.getTime();
/* 129 */           if (secondsLeft > 0L) {
/* 130 */             secondsLeft /= 1000L;
/* 131 */             CivMessage.sendError(resident, "§cCannot respawn yet. You have " + secondsLeft + " seconds left.");
/* 132 */             return;
/*     */           }
/*     */         }
/*     */         
/* 136 */         BlockCoord revive = holder.getRandomRevivePoint();
/*     */         Location loc;
/* 138 */         Location loc; if (revive == null) {
/* 139 */           loc = player.getBedSpawnLocation();
/*     */         } else {
/* 141 */           loc = revive.getLocation();
/*     */         }
/*     */         
/* 144 */         CivMessage.send(player, "§aRespawning...");
/* 145 */         player.teleport(loc);
/*     */       }
/*     */       
/*     */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock)
/*     */   {
/* 155 */     if (commandBlock.command.equals("/towerfire")) {
/* 156 */       String id = (String)commandBlock.keyvalues.get("id");
/* 157 */       Integer towerID = Integer.valueOf(id);
/*     */       
/* 159 */       if (!this.arrowTowers.containsKey(towerID))
/*     */       {
/* 161 */         ProjectileArrowComponent arrowTower = new ProjectileArrowComponent(this, absCoord.getLocation());
/* 162 */         arrowTower.createComponent(this);
/* 163 */         arrowTower.setTurretLocation(absCoord);
/*     */         
/* 165 */         this.arrowTowers.put(towerID, arrowTower);
/*     */       }
/* 167 */     } else if (commandBlock.command.equals("/next")) {
/* 168 */       ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
/* 169 */       ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
/*     */       
/* 171 */       StructureSign structSign = new StructureSign(absCoord, this);
/* 172 */       structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Next\nLocation");
/* 173 */       structSign.setDirection(commandBlock.getData());
/* 174 */       structSign.setAction("next");
/* 175 */       structSign.update();
/* 176 */       addStructureSign(structSign);
/* 177 */       CivGlobal.addStructureSign(structSign);
/*     */       
/* 179 */       structSign.save();
/* 180 */     } else if (commandBlock.command.equals("/prev")) {
/* 181 */       ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
/* 182 */       ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
/* 183 */       StructureSign structSign = new StructureSign(absCoord, this);
/* 184 */       structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Prevous\nLocation");
/* 185 */       structSign.setDirection(commandBlock.getData());
/* 186 */       structSign.setAction("prev");
/* 187 */       structSign.update();
/* 188 */       addStructureSign(structSign);
/* 189 */       CivGlobal.addStructureSign(structSign);
/*     */       
/* 191 */       structSign.save();
/* 192 */     } else if (commandBlock.command.equals("/respawndata")) {
/* 193 */       ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
/* 194 */       ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
/* 195 */       StructureSign structSign = new StructureSign(absCoord, this);
/* 196 */       structSign.setText("Capitol");
/* 197 */       structSign.setDirection(commandBlock.getData());
/* 198 */       structSign.setAction("respawn");
/* 199 */       structSign.update();
/* 200 */       addStructureSign(structSign);
/* 201 */       CivGlobal.addStructureSign(structSign);
/*     */       
/* 203 */       this.respawnSign = structSign;
/* 204 */       changeIndex(this.index);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createControlPoint(BlockCoord absCoord)
/*     */   {
/* 212 */     Location centerLoc = absCoord.getLocation();
/*     */     
/*     */ 
/*     */ 
/* 216 */     Block b = centerLoc.getBlock();
/* 217 */     ItemManager.setTypeId(b, ItemManager.getId(Material.SANDSTONE));ItemManager.setData(b, 0);
/*     */     
/* 219 */     StructureBlock sb = new StructureBlock(new BlockCoord(b), this);
/* 220 */     addStructureBlock(sb.getCoord(), true);
/*     */     
/*     */ 
/*     */ 
/* 224 */     b = centerLoc.getBlock().getRelative(0, 1, 0);
/* 225 */     ItemManager.setTypeId(b, 49);
/* 226 */     sb = new StructureBlock(new BlockCoord(b), this);
/* 227 */     addStructureBlock(sb.getCoord(), true);
/*     */     int capitolControlHitpoints;
/*     */     try
/*     */     {
/* 231 */       capitolControlHitpoints = CivSettings.getInteger(CivSettings.warConfig, "war.control_block_hitpoints_capitol").intValue();
/*     */     } catch (InvalidConfiguration e) { int capitolControlHitpoints;
/* 233 */       e.printStackTrace();
/* 234 */       capitolControlHitpoints = 100;
/*     */     }
/*     */     
/* 237 */     BlockCoord coord = new BlockCoord(b);
/* 238 */     this.controlPoints.put(coord, new ControlPoint(coord, this, capitolControlHitpoints));
/*     */   }
/*     */   
/*     */   public void onInvalidPunish()
/*     */   {
/*     */     try
/*     */     {
/* 245 */       invalid_respawn_penalty = CivSettings.getInteger(CivSettings.warConfig, "war.invalid_respawn_penalty").intValue();
/*     */     } catch (InvalidConfiguration e) { int invalid_respawn_penalty;
/* 247 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     int invalid_respawn_penalty;
/* 251 */     CivMessage.sendTown(getTown(), "§c" + CivColor.BOLD + "Our civ's capitol cannot be supported by the blocks underneath!" + 
/* 252 */       " It will take us an extra " + invalid_respawn_penalty + " mins to respawn during war if its not fixed in time!");
/*     */   }
/*     */   
/*     */   public boolean isValid()
/*     */   {
/* 257 */     if (getCiv().isAdminCiv()) {
/* 258 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */     for (Town town : getCiv().getTowns()) {
/* 266 */       TownHall townhall = town.getTownHall();
/* 267 */       if (townhall == null) {
/* 268 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 272 */     return super.isValid();
/*     */   }
/*     */   
/*     */   public String getRespawnName()
/*     */   {
/* 277 */     return "Capitol\n" + getTown().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Capitol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */