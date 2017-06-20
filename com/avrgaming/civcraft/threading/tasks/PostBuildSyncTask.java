/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivData;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.structure.ArrowTower;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.CannonTower;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.material.MaterialData;
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
/*     */ public class PostBuildSyncTask
/*     */   implements Runnable
/*     */ {
/*     */   Template tpl;
/*     */   Buildable buildable;
/*     */   
/*     */   public PostBuildSyncTask(Template tpl, Buildable buildable)
/*     */   {
/*  52 */     this.tpl = tpl;
/*  53 */     this.buildable = buildable;
/*     */   }
/*     */   
/*     */   public static void start(Template tpl, Buildable buildable) {
/*  57 */     for (BlockCoord relativeCoord : tpl.doorRelativeLocations) {
/*  58 */       SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
/*  59 */       BlockCoord absCoord = new BlockCoord(buildable.getCorner().getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
/*     */       
/*  61 */       Block block = absCoord.getBlock();
/*  62 */       if (ItemManager.getId(block) != sb.getType()) {
/*  63 */         if (buildable.getCiv().isAdminCiv()) {
/*  64 */           ItemManager.setTypeIdAndData(block, 0, 0, false);
/*     */         } else {
/*  66 */           ItemManager.setTypeIdAndData(block, sb.getType(), (byte)sb.getData(), false);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     label1128:
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */     for (BlockCoord relativeCoord : tpl.commandBlockRelativeLocations) {
/*  88 */       SimpleBlock sb = tpl.blocks[relativeCoord.getX()][relativeCoord.getY()][relativeCoord.getZ()];
/*     */       
/*     */ 
/*  91 */       BlockCoord absCoord = new BlockCoord(buildable.getCorner().getBlock().getRelative(relativeCoord.getX(), relativeCoord.getY(), relativeCoord.getZ()));
/*     */       
/*     */       String str1;
/*  94 */       switch ((str1 = sb.command).hashCode()) {case -1129879890:  if (str1.equals("/techbar")) {} break; case -925067079:  if (str1.equals("/tradeoutpost")) break; break; case -666478481:  if (str1.equals("/techdata")) {} break; case -666180784:  if (str1.equals("/techname")) {} break; case 46935660:  if (str1.equals("/sign")) {} break; case 1259250254:  if (str1.equals("/control")) {} break; case 1405094425:  if (str1.equals("/respawn")) {} break; case 1440197682:  if (str1.equals("/chest")) {} break; case 1514006251:  if (str1.equals("/itemframe")) {} break; case 1559623024:  if (str1.equals("/towerfire")) {} break; case 2123618884:  TownHall townhall; if (!str1.equals("/revive"))
/*     */         {
/*     */           break label1128;
/*  97 */           if ((buildable instanceof TradeOutpost)) {
/*  98 */             TradeOutpost outpost = (TradeOutpost)buildable;
/*  99 */             outpost.setTradeOutpostTower(absCoord);
/*     */             try {
/* 101 */               outpost.build_trade_outpost_tower();
/*     */             } catch (CivException e) {
/* 103 */               e.printStackTrace();
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */             if ((buildable instanceof TownHall)) {
/* 111 */               TownHall townhall = (TownHall)buildable;
/*     */               
/* 113 */               int index = Integer.valueOf((String)sb.keyvalues.get("id")).intValue();
/* 114 */               townhall.addTechBarBlock(absCoord, index);
/*     */               
/*     */ 
/*     */               break label1128;
/*     */               
/* 119 */               if ((buildable instanceof TownHall)) {
/* 120 */                 TownHall townhall = (TownHall)buildable;
/*     */                 
/* 122 */                 townhall.setTechnameSign(absCoord);
/* 123 */                 townhall.setTechnameSignData((byte)sb.getData());
/*     */                 
/*     */ 
/*     */                 break label1128;
/*     */                 
/* 128 */                 if ((buildable instanceof TownHall)) {
/* 129 */                   TownHall townhall = (TownHall)buildable;
/*     */                   
/* 131 */                   townhall.setTechdataSign(absCoord);
/* 132 */                   townhall.setTechdataSignData((byte)sb.getData());
/*     */                   
/*     */ 
/*     */                   break label1128;
/*     */                   
/* 137 */                   String strvalue = (String)sb.keyvalues.get("id");
/* 138 */                   if (strvalue != null) {
/* 139 */                     int index = Integer.valueOf(strvalue).intValue();
/*     */                     
/* 141 */                     if ((buildable instanceof TownHall)) {
/* 142 */                       townhall = (TownHall)buildable;
/* 143 */                       townhall.createGoodieItemFrame(absCoord, index, sb.getData());
/* 144 */                       townhall.addStructureBlock(absCoord, false);
/*     */                       
/*     */ 
/*     */                       break label1128;
/*     */                       
/* 149 */                       if ((buildable instanceof TownHall)) {
/* 150 */                         TownHall townhall = (TownHall)buildable;
/*     */                         
/* 152 */                         townhall.setRespawnPoint(absCoord);
/*     */                       }
/*     */                     }
/*     */                   }
/* 156 */                 } } } } } else if ((buildable instanceof TownHall)) {
/* 157 */           TownHall townhall = (TownHall)buildable;
/*     */           
/* 159 */           townhall.setRevivePoint(absCoord);
/*     */           
/*     */           break label1128;
/*     */           
/* 163 */           if ((buildable instanceof TownHall)) {
/* 164 */             TownHall townhall = (TownHall)buildable;
/* 165 */             townhall.createControlPoint(absCoord);
/*     */             
/*     */             break label1128;
/*     */             
/* 169 */             if ((buildable instanceof ArrowTower)) {
/* 170 */               ArrowTower arrowtower = (ArrowTower)buildable;
/* 171 */               arrowtower.setTurretLocation(absCoord);
/*     */             }
/* 173 */             if ((buildable instanceof CannonTower)) {
/* 174 */               CannonTower cannontower = (CannonTower)buildable;
/* 175 */               cannontower.setTurretLocation(absCoord);
/*     */               
/*     */ 
/*     */               break label1128;
/*     */               
/* 180 */               StructureSign structSign = CivGlobal.getStructureSign(absCoord);
/* 181 */               if (structSign == null) {
/* 182 */                 structSign = new StructureSign(absCoord, buildable);
/*     */               }
/* 184 */               structSign.setText(sb.message);
/* 185 */               Block block = absCoord.getBlock();
/* 186 */               ItemManager.setTypeId(block, sb.getType());
/* 187 */               ItemManager.setData(block, sb.getData());
/*     */               
/* 189 */               structSign.setDirection(ItemManager.getData(block.getState()));
/* 190 */               townhall = sb.keyvalues.keySet().iterator(); if (townhall.hasNext()) { String key = (String)townhall.next();
/* 191 */                 structSign.setType(key);
/* 192 */                 structSign.setAction((String)sb.keyvalues.get(key));
/*     */               }
/*     */               
/*     */ 
/* 196 */               structSign.setOwner(buildable);
/* 197 */               buildable.addStructureSign(structSign);
/* 198 */               CivGlobal.addStructureSign(structSign);
/*     */               
/* 200 */               structSign.save();
/*     */               
/*     */               break label1128;
/* 203 */               StructureChest structChest = CivGlobal.getStructureChest(absCoord);
/* 204 */               if (structChest == null) {
/* 205 */                 structChest = new StructureChest(absCoord, buildable);
/*     */               }
/* 207 */               structChest.setChestId(Integer.valueOf((String)sb.keyvalues.get("id")).intValue());
/* 208 */               buildable.addStructureChest(structChest);
/* 209 */               CivGlobal.addStructureChest(structChest);
/*     */               
/*     */ 
/* 212 */               Block block = absCoord.getBlock();
/* 213 */               if (ItemManager.getId(block) != 54) {
/* 214 */                 byte chestData = CivData.convertSignDataToChestData((byte)sb.getData());
/* 215 */                 ItemManager.setTypeId(block, 54);
/* 216 */                 ItemManager.setData(block, chestData, true);
/*     */                 
/* 218 */                 Chest chest = (Chest)block.getState();
/* 219 */                 MaterialData data = chest.getData();
/* 220 */                 ItemManager.setData(data, chestData);
/* 221 */                 chest.setData(data);
/* 222 */                 chest.update();
/*     */               }
/*     */               
/* 225 */               structChest.save();
/*     */             }
/*     */           } }
/*     */         break; }
/* 229 */       buildable.onPostBuild(absCoord, sb);
/*     */     }
/*     */     
/* 232 */     if ((buildable instanceof TownHall)) {
/* 233 */       UpdateTechBar techbartask = new UpdateTechBar(buildable.getCiv());
/* 234 */       techbartask.run();
/*     */     }
/*     */     
/*     */ 
/* 238 */     buildable.updateSignText();
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/* 244 */     start(this.tpl, this.buildable);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PostBuildSyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */