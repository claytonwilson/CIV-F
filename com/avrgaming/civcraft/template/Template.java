/*     */ package com.avrgaming.civcraft.template;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.PlayerBlockChangeUtil;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Template
/*     */ {
/*     */   public SimpleBlock[][][] blocks;
/*     */   public int size_x;
/*     */   public int size_y;
/*     */   public int size_z;
/*     */   private String dir;
/*     */   private String filepath;
/*     */   public static void init()
/*     */     throws IOException, CivException
/*     */   {}
/*     */   
/*     */   public static enum TemplateType
/*     */   {
/*  59 */     STRUCTURE, 
/*  60 */     WONDER;
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
/*  71 */   public ArrayList<BlockCoord> commandBlockRelativeLocations = new ArrayList();
/*  72 */   public LinkedList<BlockCoord> doorRelativeLocations = new LinkedList();
/*  73 */   public LinkedList<BlockCoord> attachableLocations = new LinkedList();
/*  74 */   public static HashSet<Integer> attachableTypes = new HashSet();
/*     */   
/*     */ 
/*  77 */   public static HashMap<String, Template> templateCache = new HashMap();
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
/*     */   public static int initStaticTemplatesDirection(String dir)
/*     */     throws IOException, CivException
/*     */   {
/*  96 */     int count = 0;
/*  97 */     for (ConfigBuildableInfo info : CivSettings.structures.values())
/*  98 */       if (info.has_template)
/*     */       {
/*     */ 
/*     */ 
/* 102 */         Template tpl = new Template();
/* 103 */         tpl.dir = dir;
/* 104 */         tpl.load_template("templates/themes/default/structures/" + info.template_base_name + "/" + info.template_base_name + "_" + tpl.dir + ".def");
/* 105 */         count++;
/*     */       }
/* 107 */     return count;
/*     */   }
/*     */   
/*     */   public static void initAttachableTypes() {
/* 111 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.SAPLING)));
/* 112 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.BED)));
/* 113 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.POWERED_RAIL)));
/* 114 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.DETECTOR_RAIL)));
/* 115 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.GRASS)));
/* 116 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.DEAD_BUSH)));
/* 117 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.YELLOW_FLOWER)));
/* 118 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.RED_ROSE)));
/* 119 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.BROWN_MUSHROOM)));
/* 120 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.RED_MUSHROOM)));
/* 121 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.TORCH)));
/* 122 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_WIRE)));
/* 123 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.WHEAT)));
/* 124 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.SIGN_POST)));
/* 125 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.WALL_SIGN)));
/* 126 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.LADDER)));
/* 127 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.RAILS)));
/* 128 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.LEVER)));
/* 129 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.STONE_PLATE)));
/* 130 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.WOOD_PLATE)));
/* 131 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_TORCH_ON)));
/* 132 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_TORCH_OFF)));
/* 133 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.STONE_BUTTON)));
/* 134 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.CACTUS)));
/* 135 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.SUGAR_CANE)));
/* 136 */     attachableTypes.add(Integer.valueOf(93));
/* 137 */     attachableTypes.add(Integer.valueOf(94));
/* 138 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.TRAP_DOOR)));
/* 139 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.PUMPKIN_STEM)));
/* 140 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.MELON_STEM)));
/* 141 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.VINE)));
/* 142 */     attachableTypes.add(Integer.valueOf(111));
/* 143 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.BREWING_STAND)));
/* 144 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.COCOA)));
/* 145 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.TRIPWIRE)));
/* 146 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.TRIPWIRE_HOOK)));
/* 147 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.FLOWER_POT)));
/* 148 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.CARROT)));
/* 149 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.POTATO)));
/* 150 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.WOOD_BUTTON)));
/* 151 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.ANVIL)));
/* 152 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.GOLD_PLATE)));
/* 153 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.IRON_PLATE)));
/* 154 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_COMPARATOR_ON)));
/* 155 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.REDSTONE_COMPARATOR_OFF)));
/* 156 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.DAYLIGHT_DETECTOR)));
/* 157 */     attachableTypes.add(Integer.valueOf(ItemManager.getId(Material.ACTIVATOR_RAIL)));
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
/*     */   public static String getTemplateFilePath(Location playerLocationForDirection, Buildable buildable, String theme)
/*     */   {
/* 171 */     TemplateType type = TemplateType.STRUCTURE;
/*     */     
/* 173 */     if ((buildable instanceof Structure)) {
/* 174 */       type = TemplateType.STRUCTURE;
/* 175 */     } else if ((buildable instanceof Wonder)) {
/* 176 */       type = TemplateType.WONDER;
/*     */     }
/*     */     
/* 179 */     String dir = parseDirection(playerLocationForDirection);
/* 180 */     dir = invertDirection(dir);
/*     */     
/* 182 */     return getTemplateFilePath(buildable.getTemplateBaseName(), dir, type, theme);
/*     */   }
/*     */   
/*     */   public static String getTemplateFilePath(String template_file, String direction, TemplateType type, String theme) {
/* 186 */     String typeStr = "";
/* 187 */     if (type == TemplateType.STRUCTURE) {
/* 188 */       typeStr = "structures";
/*     */     }
/* 190 */     if (type == TemplateType.WONDER) {
/* 191 */       typeStr = "wonders";
/*     */     }
/*     */     
/* 194 */     template_file = template_file.replaceAll(" ", "_");
/*     */     
/* 196 */     if (direction.equals("")) {
/* 197 */       return ("templates/themes/" + theme + "/" + typeStr + "/" + template_file + "/" + template_file + ".def").toLowerCase();
/*     */     }
/*     */     
/* 200 */     return ("templates/themes/" + theme + "/" + typeStr + "/" + template_file + "/" + template_file + "_" + direction + ".def").toLowerCase();
/*     */   }
/*     */   
/*     */   public void buildPreviewScaffolding(Location center, Player player)
/*     */   {
/* 205 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 207 */     resident.undoPreview();
/*     */     
/* 209 */     for (int y = 0; y < this.size_y; y++) {
/* 210 */       Block b = center.getBlock().getRelative(0, y, 0);
/* 211 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 212 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       
/* 214 */       b = center.getBlock().getRelative(this.size_x - 1, y, this.size_z - 1);
/* 215 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 216 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       
/* 218 */       b = center.getBlock().getRelative(this.size_x - 1, y, 0);
/* 219 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 220 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       
/* 222 */       b = center.getBlock().getRelative(0, y, this.size_z - 1);
/* 223 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 224 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */     }
/*     */     
/*     */ 
/* 228 */     for (int x = 0; x < this.size_x; x++) {
/* 229 */       Block b = center.getBlock().getRelative(x, this.size_y - 1, 0);
/* 230 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 231 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       
/* 233 */       b = center.getBlock().getRelative(x, this.size_y - 1, this.size_z - 1);
/* 234 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 235 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */     }
/*     */     
/*     */ 
/* 239 */     for (int z = 0; z < this.size_z; z++) {
/* 240 */       Block b = center.getBlock().getRelative(0, this.size_y - 1, z);
/* 241 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 242 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       
/* 244 */       b = center.getBlock().getRelative(this.size_x - 1, this.size_y - 1, z);
/* 245 */       ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 246 */       resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */     }
/*     */     
/* 249 */     for (int z = 0; z < this.size_z; z++) {
/* 250 */       for (int x = 0; x < this.size_x; x++) {
/* 251 */         Block b = center.getBlock().getRelative(x, 0, z);
/* 252 */         ItemManager.sendBlockChange(player, b.getLocation(), 7, 0);
/* 253 */         resident.previewUndo.put(new BlockCoord(b.getLocation()), new SimpleBlock(ItemManager.getId(b), ItemManager.getData(b)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void buildScaffolding(Location center)
/*     */   {
/* 261 */     for (int y = 0; y < this.size_y; y++) {
/* 262 */       Block b = center.getBlock().getRelative(0, y, 0);
/* 263 */       ItemManager.setTypeId(b, 7);
/*     */       
/* 265 */       b = center.getBlock().getRelative(this.size_x - 1, y, this.size_z - 1);
/* 266 */       ItemManager.setTypeId(b, 7);
/*     */       
/* 268 */       b = center.getBlock().getRelative(this.size_x - 1, y, 0);
/* 269 */       ItemManager.setTypeId(b, 7);
/*     */       
/* 271 */       b = center.getBlock().getRelative(0, y, this.size_z - 1);
/* 272 */       ItemManager.setTypeId(b, 7);
/*     */     }
/*     */     
/* 275 */     for (int x = 0; x < this.size_x; x++) {
/* 276 */       Block b = center.getBlock().getRelative(x, this.size_y - 1, 0);
/* 277 */       ItemManager.setTypeId(b, 7);
/*     */       
/* 279 */       b = center.getBlock().getRelative(x, this.size_y - 1, this.size_z - 1);
/* 280 */       ItemManager.setTypeId(b, 7);
/*     */     }
/*     */     
/* 283 */     for (int z = 0; z < this.size_z; z++) {
/* 284 */       Block b = center.getBlock().getRelative(0, this.size_y - 1, z);
/* 285 */       ItemManager.setTypeId(b, 7);
/*     */       
/* 287 */       b = center.getBlock().getRelative(this.size_x - 1, this.size_y - 1, z);
/* 288 */       ItemManager.setTypeId(b, 7);
/*     */     }
/*     */     
/* 291 */     for (int z = 0; z < this.size_z; z++) {
/* 292 */       for (int x = 0; x < this.size_x; x++) {
/* 293 */         Block b = center.getBlock().getRelative(x, 0, z);
/* 294 */         ItemManager.setTypeId(b, 7);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeScaffolding(Location center)
/*     */   {
/* 301 */     for (int y = 0; y < this.size_y; y++) {
/* 302 */       Block b = center.getBlock().getRelative(0, y, 0);
/* 303 */       if (ItemManager.getId(b) == 7) {
/* 304 */         ItemManager.setTypeId(b, 0);
/* 305 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */       
/* 308 */       b = center.getBlock().getRelative(this.size_x - 1, y, this.size_z - 1);
/* 309 */       if (ItemManager.getId(b) == 7) {
/* 310 */         ItemManager.setTypeId(b, 0);
/* 311 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */       
/* 314 */       b = center.getBlock().getRelative(this.size_x - 1, y, 0);
/* 315 */       if (ItemManager.getId(b) == 7) {
/* 316 */         ItemManager.setTypeId(b, 0);
/* 317 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */       
/*     */ 
/* 321 */       b = center.getBlock().getRelative(0, y, this.size_z - 1);
/* 322 */       if (ItemManager.getId(b) == 7) {
/* 323 */         ItemManager.setTypeId(b, 0);
/* 324 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 329 */     for (int x = 0; x < this.size_x; x++) {
/* 330 */       Block b = center.getBlock().getRelative(x, this.size_y - 1, 0);
/* 331 */       if (ItemManager.getId(b) == 7) {
/* 332 */         ItemManager.setTypeId(b, 0);
/* 333 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */       
/* 336 */       b = center.getBlock().getRelative(x, this.size_y - 1, this.size_z - 1);
/* 337 */       if (ItemManager.getId(b) == 7) {
/* 338 */         ItemManager.setTypeId(b, 0);
/* 339 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 344 */     for (int z = 0; z < this.size_z; z++) {
/* 345 */       Block b = center.getBlock().getRelative(0, this.size_y - 1, z);
/* 346 */       if (ItemManager.getId(b) == 7) {
/* 347 */         ItemManager.setTypeId(b, 0);
/* 348 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */       
/* 351 */       b = center.getBlock().getRelative(this.size_x - 1, this.size_y - 1, z);
/* 352 */       if (ItemManager.getId(b) == 7) {
/* 353 */         ItemManager.setTypeId(b, 0);
/* 354 */         ItemManager.setData(b, 0, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void saveUndoTemplate(String string, String subdir, Location center)
/*     */     throws CivException, IOException
/*     */   {
/* 363 */     String filepath = "templates/undo/" + subdir;
/* 364 */     File undo_tpl_file = new File(filepath);
/* 365 */     undo_tpl_file.mkdirs();
/*     */     
/* 367 */     FileWriter writer = new FileWriter(undo_tpl_file.getAbsolutePath() + "/" + string);
/*     */     
/*     */ 
/* 370 */     writer.write(this.size_x + ";" + this.size_y + ";" + this.size_z + "\n");
/* 371 */     for (int x = 0; x < this.size_x; x++) {
/* 372 */       for (int y = 0; y < this.size_y; y++) {
/* 373 */         for (int z = 0; z < this.size_z; z++) {
/* 374 */           Block b = center.getBlock().getRelative(x, y, z);
/*     */           
/* 376 */           if ((ItemManager.getId(b) == 68) || (ItemManager.getId(b) == 63)) {
/* 377 */             if ((b.getState() instanceof Sign)) {
/* 378 */               Sign sign = (Sign)b.getState();
/*     */               
/* 380 */               String signText = "";
/* 381 */               String[] arrayOfString; int j = (arrayOfString = sign.getLines()).length; for (int i = 0; i < j; i++) { String line = arrayOfString[i];
/* 382 */                 signText = signText + line + ",";
/*     */               }
/* 384 */               writer.write(x + ":" + y + ":" + z + "," + ItemManager.getId(b) + ":" + ItemManager.getData(b) + "," + signText + "\n");
/*     */             }
/*     */           } else {
/* 387 */             writer.write(x + ":" + y + ":" + z + "," + ItemManager.getId(b) + ":" + ItemManager.getData(b) + "\n");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 394 */     writer.close();
/*     */     
/* 396 */     for (int x = 0; x < this.size_x; x++) {
/* 397 */       for (int y = 0; y < this.size_y; y++) {
/* 398 */         for (int z = 0; z < this.size_z; z++)
/*     */         {
/* 400 */           Block b = center.getBlock().getRelative(x, y, z);
/*     */           
/* 402 */           ItemManager.setTypeId(b, 0);
/* 403 */           ItemManager.setData(b, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void initUndoTemplate(String structureHash, String subdir) throws IOException, CivException {
/* 410 */     String filepath = "templates/undo/" + subdir + "/" + structureHash;
/*     */     
/* 412 */     File templateFile = new File(filepath);
/* 413 */     BufferedReader reader = new BufferedReader(new FileReader(templateFile));
/*     */     
/*     */ 
/* 416 */     String line = null;
/* 417 */     line = reader.readLine();
/* 418 */     if (line == null) {
/* 419 */       reader.close();
/* 420 */       throw new CivException("Invalid template file:" + filepath);
/*     */     }
/*     */     
/* 423 */     String[] split = line.split(";");
/* 424 */     this.size_x = Integer.valueOf(split[0]).intValue();
/* 425 */     this.size_y = Integer.valueOf(split[1]).intValue();
/* 426 */     this.size_z = Integer.valueOf(split[2]).intValue();
/* 427 */     getTemplateBlocks(reader, this.size_x, this.size_y, this.size_z);
/* 428 */     reader.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTemplateCopy(String masterTemplatePath, String string, Town town)
/*     */   {
/* 439 */     String copyTemplatePath = "templates/inprogress/" + town.getName();
/* 440 */     File inprogress_tpl_file = new File(copyTemplatePath);
/* 441 */     inprogress_tpl_file.mkdirs();
/*     */     
/*     */ 
/* 444 */     File master_tpl_file = new File(masterTemplatePath);
/* 445 */     inprogress_tpl_file = new File(copyTemplatePath + "/" + string);
/*     */     try
/*     */     {
/* 448 */       Files.copy(master_tpl_file.toPath(), inprogress_tpl_file.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */     } catch (IOException e) {
/* 450 */       System.out.println("Failure to copy file!");
/* 451 */       e.printStackTrace();
/* 452 */       return null;
/*     */     }
/*     */     
/* 455 */     return copyTemplatePath + "/" + string;
/*     */   }
/*     */   
/*     */   public void setDirection(Location center) throws CivException
/*     */   {
/* 460 */     this.dir = parseDirection(center);
/* 461 */     this.dir = invertDirection(this.dir);
/*     */     
/* 463 */     if (this.dir == null) {
/* 464 */       throw new CivException("Unknown direction.");
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getDirection(Location center) {
/* 469 */     String dir = parseDirection(center);
/* 470 */     dir = invertDirection(dir);
/* 471 */     return dir;
/*     */   }
/*     */   
/*     */   public void resumeTemplate(String templatePath, Buildable buildable) throws IOException, CivException {
/* 475 */     setFilepath(templatePath);
/* 476 */     load_template(templatePath);
/* 477 */     buildable.setTotalBlockCount(this.size_x * this.size_y * this.size_z);
/*     */   }
/*     */   
/*     */   public void initTemplate(Location center, ConfigBuildableInfo info, String theme) throws CivException, IOException {
/* 481 */     setDirection(center);
/* 482 */     String templatePath = getTemplateFilePath(info.template_base_name, this.dir, TemplateType.STRUCTURE, theme);
/* 483 */     setFilepath(templatePath);
/* 484 */     load_template(templatePath);
/*     */   }
/*     */   
/*     */   public void initTemplate(Location center, Buildable buildable, String theme) throws IOException, CivException
/*     */   {
/* 489 */     setDirection(center);
/*     */     
/* 491 */     if (!buildable.hasTemplate())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 496 */       this.dir = "";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 501 */     String templatePath = getTemplateFilePath(center, buildable, theme);
/* 502 */     setFilepath(templatePath);
/* 503 */     load_template(templatePath);
/* 504 */     buildable.setTotalBlockCount(this.size_x * this.size_y * this.size_z);
/*     */   }
/*     */   
/*     */   public void initTemplate(Location center, Buildable buildable) throws CivException, IOException {
/* 508 */     initTemplate(center, buildable, "default");
/*     */   }
/*     */   
/*     */   public static Template getTemplate(String filepath, Location dirLoc) throws IOException, CivException
/*     */   {
/* 513 */     Template tpl = (Template)templateCache.get(filepath);
/* 514 */     if (tpl == null)
/*     */     {
/* 516 */       tpl = new Template();
/* 517 */       tpl.load_template(filepath);
/*     */     }
/*     */     
/* 520 */     if (dirLoc != null) {
/* 521 */       tpl.setDirection(dirLoc);
/*     */     }
/* 523 */     return tpl;
/*     */   }
/*     */   
/*     */   public void load_template(String filepath) throws IOException, CivException {
/* 527 */     File templateFile = new File(filepath);
/* 528 */     BufferedReader reader = new BufferedReader(new FileReader(templateFile));
/*     */     
/*     */ 
/* 531 */     String line = null;
/* 532 */     line = reader.readLine();
/* 533 */     if (line == null) {
/* 534 */       reader.close();
/* 535 */       throw new CivException("Invalid template file:" + filepath);
/*     */     }
/*     */     
/* 538 */     String[] split = line.split(";");
/* 539 */     this.size_x = Integer.valueOf(split[0]).intValue();
/* 540 */     this.size_y = Integer.valueOf(split[1]).intValue();
/* 541 */     this.size_z = Integer.valueOf(split[2]).intValue();
/* 542 */     getTemplateBlocks(reader, this.size_x, this.size_y, this.size_z);
/* 543 */     this.filepath = filepath;
/* 544 */     reader.close();
/*     */   }
/*     */   
/*     */   private void getTemplateBlocks(BufferedReader reader, int regionX, int regionY, int regionZ)
/*     */     throws NumberFormatException, IOException
/*     */   {
/* 550 */     SimpleBlock[][][] blocks = new SimpleBlock[regionX][regionY][regionZ];
/*     */     
/*     */     String line;
/* 553 */     while ((line = reader.readLine()) != null) { String line;
/* 554 */       String[] locTypeSplit = line.split(",");
/* 555 */       String location = locTypeSplit[0];
/* 556 */       String type = locTypeSplit[1];
/*     */       
/*     */ 
/* 559 */       String[] locationSplit = location.split(":");
/*     */       
/* 561 */       int blockX = Integer.valueOf(locationSplit[0]).intValue();
/* 562 */       int blockY = Integer.valueOf(locationSplit[1]).intValue();
/* 563 */       int blockZ = Integer.valueOf(locationSplit[2]).intValue();
/*     */       
/*     */ 
/* 566 */       String[] typeSplit = type.split(":");
/*     */       
/* 568 */       int blockId = Integer.valueOf(typeSplit[0]).intValue();
/* 569 */       int blockData = Integer.valueOf(typeSplit[1]).intValue();
/*     */       
/* 571 */       SimpleBlock block = new SimpleBlock(blockId, blockData);
/*     */       
/* 573 */       if ((blockId == 64) || (blockId == 71)) {
/* 574 */         this.doorRelativeLocations.add(new BlockCoord("", blockX, blockY, blockZ));
/*     */       }
/*     */       
/*     */ 
/* 578 */       if ((blockId == 68) || (blockId == 63))
/*     */       {
/* 580 */         if (locTypeSplit.length > 2)
/*     */         {
/*     */ 
/* 583 */           if ((locTypeSplit[2] != null) && (!locTypeSplit[2].equals("")) && (locTypeSplit[2].charAt(0) == '/')) {
/* 584 */             block.specialType = SimpleBlock.Type.COMMAND;
/*     */             
/*     */ 
/* 587 */             block.command = locTypeSplit[2];
/*     */             
/*     */ 
/* 590 */             if (locTypeSplit.length > 3) {
/* 591 */               for (int i = 3; i < locTypeSplit.length; i++) {
/* 592 */                 if ((locTypeSplit[i] != null) && (!locTypeSplit[i].equals("")))
/*     */                 {
/*     */ 
/*     */ 
/* 596 */                   String[] keyvalue = locTypeSplit[i].split(":");
/* 597 */                   if (keyvalue.length < 2) {
/* 598 */                     CivLog.warning("Invalid keyvalue:" + locTypeSplit[i] + " in template:" + this.filepath);
/*     */                   }
/*     */                   else {
/* 601 */                     block.keyvalues.put(keyvalue[0].trim(), keyvalue[1].trim());
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/* 606 */             this.commandBlockRelativeLocations.add(new BlockCoord("", blockX, blockY, blockZ));
/*     */           }
/*     */           else {
/* 609 */             block.specialType = SimpleBlock.Type.LITERAL;
/*     */             
/*     */             try
/*     */             {
/* 613 */               block.message[0] = locTypeSplit[2];
/*     */             } catch (ArrayIndexOutOfBoundsException e) {
/* 615 */               block.message[0] = "";
/*     */             }
/*     */             try {
/* 618 */               block.message[1] = locTypeSplit[3];
/*     */             } catch (ArrayIndexOutOfBoundsException e) {
/* 620 */               block.message[1] = "";
/*     */             }
/*     */             try
/*     */             {
/* 624 */               block.message[2] = locTypeSplit[4];
/*     */             }
/*     */             catch (ArrayIndexOutOfBoundsException e) {
/* 627 */               block.message[2] = "";
/*     */             }
/*     */             try
/*     */             {
/* 631 */               block.message[3] = locTypeSplit[5];
/*     */             } catch (ArrayIndexOutOfBoundsException e) {
/* 633 */               block.message[3] = "";
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 639 */       blocks[blockX][blockY][blockZ] = block;
/*     */     }
/*     */     
/*     */ 
/* 643 */     this.blocks = blocks;
/*     */   }
/*     */   
/*     */   public static String parseDirection(Location loc)
/*     */   {
/* 648 */     double rotation = (loc.getYaw() - 90.0F) % 360.0F;
/* 649 */     if (rotation < 0.0D) {
/* 650 */       rotation += 360.0D;
/*     */     }
/* 652 */     if ((0.0D <= rotation) && (rotation < 22.5D))
/* 653 */       return "east";
/* 654 */     if ((22.5D <= rotation) && (rotation < 67.5D))
/* 655 */       return "east";
/* 656 */     if ((67.5D <= rotation) && (rotation < 112.5D))
/* 657 */       return "south";
/* 658 */     if ((112.5D <= rotation) && (rotation < 157.5D))
/* 659 */       return "west";
/* 660 */     if ((157.5D <= rotation) && (rotation < 202.5D))
/* 661 */       return "west";
/* 662 */     if ((202.5D <= rotation) && (rotation < 247.5D))
/* 663 */       return "west";
/* 664 */     if ((247.5D <= rotation) && (rotation < 292.5D))
/* 665 */       return "north";
/* 666 */     if ((292.5D <= rotation) && (rotation < 337.5D))
/* 667 */       return "east";
/* 668 */     if ((337.5D <= rotation) && (rotation < 360.0D)) {
/* 669 */       return "east";
/*     */     }
/* 671 */     return null;
/*     */   }
/*     */   
/*     */   public static String invertDirection(String dir)
/*     */   {
/* 676 */     if (dir.equalsIgnoreCase("east"))
/* 677 */       return "west";
/* 678 */     if (dir.equalsIgnoreCase("west")) {
/* 679 */       return "east";
/*     */     }
/* 681 */     if (dir.equalsIgnoreCase("north")) {
/* 682 */       return "south";
/*     */     }
/* 684 */     if (dir.equalsIgnoreCase("south")) {
/* 685 */       return "north";
/*     */     }
/* 687 */     return null;
/*     */   }
/*     */   
/*     */   public void deleteUndoTemplate(String string, String subdir) throws CivException, IOException {
/* 691 */     String filepath = "templates/undo/" + subdir + "/" + string;
/* 692 */     File templateFile = new File(filepath);
/* 693 */     templateFile.delete();
/*     */   }
/*     */   
/*     */   public void deleteInProgessTemplate(String string, Town town) {
/* 697 */     String filepath = "templates/inprogress/" + town.getName() + "/" + string;
/* 698 */     File templateFile = new File(filepath);
/* 699 */     templateFile.delete();
/*     */   }
/*     */   
/*     */   public String getFilepath() {
/* 703 */     return this.filepath;
/*     */   }
/*     */   
/*     */   public void setFilepath(String filepath) {
/* 707 */     this.filepath = filepath;
/*     */   }
/*     */   
/*     */ 
/*     */   public void previewEntireTemplate(Template tpl, Block cornerBlock, Player player)
/*     */   {
/* 713 */     PlayerBlockChangeUtil util = new PlayerBlockChangeUtil();
/* 714 */     for (int x = 0; x < tpl.size_x; x++) {
/* 715 */       for (int y = 0; y < tpl.size_y; y++) {
/* 716 */         for (int z = 0; z < tpl.size_z; z++) {
/* 717 */           Block b = cornerBlock.getRelative(x, y, z);
/*     */           try
/*     */           {
/* 720 */             util.addUpdateBlock("", new BlockCoord(b), tpl.blocks[x][y][z].getType(), tpl.blocks[x][y][z].getData());
/*     */ 
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 725 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 732 */     util.sendUpdate(player.getName());
/*     */   }
/*     */   
/*     */   public void buildUndoTemplate(Template tpl, Block centerBlock) {
/* 736 */     HashMap<Chunk, Chunk> chunkUpdates = new HashMap();
/*     */     
/* 738 */     for (int x = 0; x < tpl.size_x; x++) {
/* 739 */       for (int y = 0; y < tpl.size_y; y++)
/* 740 */         for (int z = 0; z < tpl.size_z; z++) {
/* 741 */           Block b = centerBlock.getRelative(x, y, z);
/* 742 */           if (!CivSettings.restrictedUndoBlocks.contains(Integer.valueOf(ItemManager.getId(b))))
/*     */           {
/*     */ 
/*     */ 
/* 746 */             ItemManager.setTypeIdAndData(b, tpl.blocks[x][y][z].getType(), (byte)tpl.blocks[x][y][z].getData(), false);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 755 */             chunkUpdates.put(b.getChunk(), b.getChunk());
/*     */             
/* 757 */             if ((ItemManager.getId(b) == 68) || (ItemManager.getId(b) == 63)) {
/* 758 */               Sign s2 = (Sign)b.getState();
/* 759 */               s2.setLine(0, tpl.blocks[x][y][z].message[0]);
/* 760 */               s2.setLine(1, tpl.blocks[x][y][z].message[1]);
/* 761 */               s2.setLine(2, tpl.blocks[x][y][z].message[2]);
/* 762 */               s2.setLine(3, tpl.blocks[x][y][z].message[3]);
/* 763 */               s2.update();
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */   
/*     */   public String dir() {
/* 771 */     return this.dir;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\template\Template.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */