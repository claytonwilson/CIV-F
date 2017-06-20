/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ 
/*     */ 
/*     */ public class ConfigArena
/*     */ {
/*     */   public String id;
/*     */   public String name;
/*     */   public String world_source;
/*     */   public int control_block_hp;
/*     */   public LinkedList<ConfigArenaTeam> teams;
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigArena> config_arenas)
/*     */   {
/*  21 */     config_arenas.clear();
/*     */     
/*  23 */     List<Map<?, ?>> cottage_list = cfg.getMapList("arenas");
/*     */     
/*  25 */     for (Map<?, ?> cl : cottage_list)
/*     */     {
/*  27 */       List<Map<?, ?>> teams_map_list = (List)cl.get("teams");
/*     */       
/*  29 */       ConfigArena arena = new ConfigArena();
/*  30 */       arena.id = ((String)cl.get("id"));
/*  31 */       arena.name = ((String)cl.get("name"));
/*  32 */       arena.world_source = ((String)cl.get("world_folder"));
/*  33 */       arena.control_block_hp = ((Integer)cl.get("control_block_hp")).intValue();
/*     */       
/*  35 */       if (teams_map_list != null) {
/*  36 */         arena.teams = new LinkedList();
/*  37 */         for (Map<?, ?> tm : teams_map_list) {
/*  38 */           ConfigArenaTeam team = new ConfigArenaTeam();
/*     */           
/*  40 */           team.number = ((Integer)tm.get("number"));
/*  41 */           team.name = ((String)tm.get("name"));
/*     */           
/*     */ 
/*  44 */           team.controlPoints = new LinkedList();
/*  45 */           List<?> someList = (List)tm.get("controlblocks");
/*  46 */           for (Object obj : someList) {
/*  47 */             String[] coords = ((String)obj).split(",");
/*  48 */             BlockCoord bcoord = new BlockCoord(arena.world_source, 
/*  49 */               Integer.valueOf(coords[0]).intValue(), 
/*  50 */               Integer.valueOf(coords[1]).intValue(), 
/*  51 */               Integer.valueOf(coords[2]).intValue());
/*     */             
/*     */ 
/*  54 */             team.controlPoints.add(bcoord);
/*     */           }
/*     */           
/*     */ 
/*  58 */           team.revivePoints = new LinkedList();
/*  59 */           someList = (List)tm.get("revivepoints");
/*  60 */           for (Object obj : someList) {
/*  61 */             String[] coords = ((String)obj).split(",");
/*  62 */             BlockCoord bcoord = new BlockCoord(arena.world_source, 
/*  63 */               Integer.valueOf(coords[0]).intValue(), 
/*  64 */               Integer.valueOf(coords[1]).intValue(), 
/*  65 */               Integer.valueOf(coords[2]).intValue());
/*     */             
/*     */ 
/*  68 */             team.revivePoints.add(bcoord);
/*     */           }
/*     */           
/*     */ 
/*  72 */           team.respawnPoints = new LinkedList();
/*  73 */           someList = (List)tm.get("respawnpoints");
/*  74 */           for (Object obj : someList) {
/*  75 */             String[] coords = ((String)obj).split(",");
/*  76 */             BlockCoord bcoord = new BlockCoord(arena.world_source, 
/*  77 */               Integer.valueOf(coords[0]).intValue(), 
/*  78 */               Integer.valueOf(coords[1]).intValue(), 
/*  79 */               Integer.valueOf(coords[2]).intValue());
/*     */             
/*     */ 
/*  82 */             team.respawnPoints.add(bcoord);
/*     */           }
/*     */           
/*     */ 
/*  86 */           team.chests = new LinkedList();
/*  87 */           someList = (List)tm.get("chests");
/*  88 */           for (Object obj : someList) {
/*  89 */             String[] coords = ((String)obj).split(",");
/*  90 */             BlockCoord bcoord = new BlockCoord(arena.world_source, 
/*  91 */               Integer.valueOf(coords[0]).intValue(), 
/*  92 */               Integer.valueOf(coords[1]).intValue(), 
/*  93 */               Integer.valueOf(coords[2]).intValue());
/*     */             
/*     */ 
/*  96 */             team.chests.add(bcoord);
/*     */           }
/*     */           
/*  99 */           String respawnSignStr = (String)tm.get("respawnsign");
/* 100 */           String[] respawnSplit = respawnSignStr.split(",");
/* 101 */           team.respawnSign = new BlockCoord(arena.world_source, 
/* 102 */             Integer.valueOf(respawnSplit[0]).intValue(), 
/* 103 */             Integer.valueOf(respawnSplit[1]).intValue(), 
/* 104 */             Integer.valueOf(respawnSplit[2]).intValue());
/*     */           
/* 106 */           arena.teams.add(team);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 112 */       config_arenas.put(arena.id, arena);
/*     */     }
/*     */     
/* 115 */     CivLog.info("Loaded " + config_arenas.size() + " arenas.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigArena.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */