/*     */ package com.avrgaming.civcraft.tutorial;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigIngredient;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterialCategory;
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.material.MaterialData;
/*     */ 
/*     */ 
/*     */ public class CivTutorial
/*     */ {
/*  30 */   public static Inventory tutorialInventory = null;
/*  31 */   public static Inventory craftingHelpInventory = null;
/*  32 */   public static Inventory guiInventory = null;
/*     */   public static final int MAX_CHEST_SIZE = 6;
/*     */   
/*     */   public static void showTutorialInventory(Player player) {
/*  36 */     if (tutorialInventory == null) {
/*  37 */       tutorialInventory = Bukkit.getServer().createInventory(player, 27, "CivCraft Tutorial");
/*     */       
/*     */ 
/*  40 */       tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "What is CivCraft?", ItemManager.getId(Material.WORKBENCH), 0, new String[] {
/*  41 */         ChatColor.RESET + "CivCraft is a game about building civilizations set in a large,", 
/*  42 */         ChatColor.RESET + "persistent world filled with players.", 
/*  43 */         ChatColor.RESET + "Players start out as nomads, gathering", 
/*  44 */         ChatColor.RESET + "resources and making allies until they can build a camp.", 
/*  45 */         ChatColor.RESET + "Gather more resources and allies and found a civilization!", 
/*  46 */         ChatColor.RESET + "§a" + "Research technology! Build structures! Conquer the world!" }) });
/*     */       
/*     */ 
/*  49 */       tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "Explore", ItemManager.getId(Material.COMPASS), 0, new String[] {
/*  50 */         ChatColor.RESET + "Venture outward from spawn into the wild", 
/*  51 */         ChatColor.RESET + "and find a spot to settle. You may encounter", 
/*  52 */         ChatColor.RESET + "trade resources, and other player towns which", 
/*  53 */         ChatColor.RESET + "will infulence your decision on where to settle.", 
/*  54 */         ChatColor.RESET + "Different biomes generate different resources." }) });
/*     */       
/*     */ 
/*  57 */       tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "Resources and Materials", ItemManager.getId(Material.DIAMOND_ORE), 0, new String[] {
/*  58 */         ChatColor.RESET + "CivCraft contains many new custom items.", 
/*  59 */         ChatColor.RESET + "These items are crafted using a crafting bench", 
/*  60 */         ChatColor.RESET + "and combining many more normal Minecraft items", 
/*  61 */         ChatColor.RESET + "into higher tier items. Certain items like iron, gold,", 
/*  62 */         ChatColor.RESET + "diamonds and emeralds can be exchanged for coins at " + "§e" + "Bank", 
/*  63 */         ChatColor.RESET + "structures. Coins can be traded for materials at the " + "§e" + "Market" }) });
/*     */       
/*     */ 
/*  66 */       tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "Towns", ItemManager.getId(Material.FENCE), 0, new String[] {
/*  67 */         ChatColor.RESET + "Towns can be created by players to protect", 
/*  68 */         ChatColor.RESET + "areas from outsiders. Inside a town the owners are", 
/*  69 */         ChatColor.RESET + "free to build creatively without interference from griefers", 
/*  70 */         ChatColor.RESET + "Towns cost materials to create and coins to maintain.", 
/*  71 */         ChatColor.RESET + "Towns can build functional structures which allow it's", 
/*  72 */         ChatColor.RESET + "residents access to more features. Towns can only be built", 
/*  73 */         ChatColor.RESET + "inside of a civilization." }) });
/*     */       
/*     */ 
/*  76 */       tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "Civilizations", ItemManager.getId(Material.GOLD_HELMET), 0, new String[] {
/*  77 */         ChatColor.RESET + "Civilizations are collections of towns", 
/*  78 */         ChatColor.RESET + "All towns inside of the civilization share technology", 
/*  79 */         ChatColor.RESET + "which is researched by the civ. Many items and structures", 
/*  80 */         ChatColor.RESET + "in CivCraft are only obtainable through the use of technology", 
/*  81 */         ChatColor.RESET + "Founding your own civ is a lot of work, you must be a natural", 
/*  82 */         ChatColor.RESET + "leader and bring people together in order for your civ to survive", 
/*  83 */         ChatColor.RESET + "and flourish." }) });
/*     */       
/*     */ 
/*  86 */       if (CivGlobal.isCasualMode()) {
/*  87 */         tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "Casual War!", ItemManager.getId(Material.FIREWORK), 0, new String[] {
/*  88 */           ChatColor.RESET + "War allows civilizations to settle their differences.", 
/*  89 */           ChatColor.RESET + "In casual mode, Civs have to the option to request war from", 
/*  90 */           ChatColor.RESET + "each other. The winner of a war is awarded a trophy which can be", 
/*  91 */           ChatColor.RESET + "displayed in an item frame for bragging rights.", 
/*  92 */           ChatColor.RESET + "After a civilization is defeated in war, war must be requested again." }) });
/*     */       }
/*     */       else {
/*  95 */         tutorialInventory.addItem(new ItemStack[] { LoreGuiItem.build("§b" + ChatColor.BOLD + "War!", ItemManager.getId(Material.IRON_SWORD), 0, new String[] {
/*  96 */           ChatColor.RESET + "War allows civilizations to settle their differences.", 
/*  97 */           ChatColor.RESET + "Normally, all structures inside a civilization are protected", 
/*  98 */           ChatColor.RESET + "from damage. However civs have to the option to declare war on", 
/*  99 */           ChatColor.RESET + "each other and do damage to each other's structures, and even capture", 
/* 100 */           ChatColor.RESET + "towns from each other. Each weekend, WarTime is enabled for two hours", 
/* 101 */           ChatColor.RESET + "during which players at war must defend their civ and conquer their enemies." }) });
/*     */       }
/*     */       
/*     */ 
/* 105 */       tutorialInventory.setItem(8, LoreGuiItem.build("§b" + ChatColor.BOLD + "More Info?", ItemManager.getId(Material.BOOK_AND_QUILL), 0, new String[] {
/* 106 */         ChatColor.RESET + "There is much more information you will require for your", 
/* 107 */         ChatColor.RESET + "journey into CivCraft. Please visit the wiki at ", 
/* 108 */         ChatColor.RESET + "§a" + ChatColor.BOLD + "http://civcraft.net/wiki", 
/* 109 */         ChatColor.RESET + "For more detailed information about CivCraft and it's features." }));
/*     */       
/*     */ 
/* 112 */       tutorialInventory.setItem(9, LoreGuiItem.build("§b" + ChatColor.BOLD + "QUEST: Build a Camp", ItemManager.getId(Material.BOOK_AND_QUILL), 0, new String[] {
/* 113 */         ChatColor.RESET + "First things first, in order to start your journey", 
/* 114 */         ChatColor.RESET + "you must first build a camp. Camps allow you to store", 
/* 115 */         ChatColor.RESET + "your materials safely, and allow you to obtain leadership", 
/* 116 */         ChatColor.RESET + "tokens which can be crafted into a civ. The recipe for a camp is below." }));
/*     */       
/*     */ 
/* 119 */       tutorialInventory.setItem(18, getInfoBookForItem("mat_found_camp"));
/*     */       
/* 121 */       tutorialInventory.setItem(10, LoreGuiItem.build("§b" + ChatColor.BOLD + "QUEST: Found a Civ", ItemManager.getId(Material.BOOK_AND_QUILL), 0, new String[] {
/* 122 */         ChatColor.RESET + "Next, you'll want to start a civilization.", 
/* 123 */         ChatColor.RESET + "To do this, you must first obtain leadership tokens", 
/* 124 */         ChatColor.RESET + "by feeding bread to your camp's longhouse.", 
/* 125 */         ChatColor.RESET + "Once you have enough leadership tokens.", 
/* 126 */         ChatColor.RESET + "You can craft the founding flag item below." }));
/*     */       
/*     */ 
/* 129 */       tutorialInventory.setItem(19, getInfoBookForItem("mat_found_civ"));
/*     */       
/* 131 */       tutorialInventory.setItem(11, LoreGuiItem.build("§b" + ChatColor.BOLD + "Need to know a recipe?", ItemManager.getId(Material.WORKBENCH), 0, new String[] {
/* 132 */         ChatColor.RESET + "Type /res book to obtain the tutorial book", 
/* 133 */         ChatColor.RESET + "and then click on 'Crafting Recipies'", 
/* 134 */         ChatColor.RESET + "Every new item in CivCraft is listed here", 
/* 135 */         ChatColor.RESET + "along with how to craft them.", 
/* 136 */         ChatColor.RESET + "Good luck!" }));
/*     */       
/*     */ 
/* 139 */       LoreGuiItemListener.guiInventories.put(tutorialInventory.getName(), tutorialInventory);
/*     */     }
/*     */     
/* 142 */     if ((player != null) && (player.isOnline()) && (player.isValid())) {
/* 143 */       player.openInventory(tutorialInventory);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ItemStack getInfoBookForItem(String matID) {
/* 148 */     LoreCraftableMaterial loreMat = LoreCraftableMaterial.getCraftMaterialFromId(matID);
/* 149 */     ItemStack stack = LoreMaterial.spawn(loreMat);
/*     */     
/* 151 */     if (!loreMat.isCraftable()) {
/* 152 */       return null;
/*     */     }
/*     */     
/* 155 */     AttributeUtil attrs = new AttributeUtil(stack);
/* 156 */     attrs.removeAll();
/* 157 */     LinkedList<String> lore = new LinkedList();
/*     */     
/* 159 */     if (!loreMat.isShaped()) {
/* 160 */       lore.add(ChatColor.RESET + "§6" + "Shapeless");
/* 161 */       for (ConfigIngredient cfgIngred : loreMat.getConfigMaterial().incredients.values()) {
/*     */         String name;
/*     */         String name;
/* 164 */         if (cfgIngred.custom_id == null) {
/* 165 */           MaterialData data = ItemManager.getMaterialData(cfgIngred.type_id, cfgIngred.data);
/* 166 */           name = data.getItemType().toString();
/*     */         } else {
/* 168 */           name = ((LoreMaterial)LoreMaterial.materialMap.get(cfgIngred.custom_id)).getName();
/*     */         }
/*     */         
/* 171 */         lore.add(ChatColor.RESET + "§f" + cfgIngred.count + " " + "§b" + name);
/*     */       }
/*     */     }
/*     */     else {
/* 175 */       lore.add(ChatColor.RESET + "§6" + "x" + "§f" + " is " + "§b" + "empty");
/*     */       MaterialData data;
/* 177 */       for (ConfigIngredient cfgIngred : loreMat.getConfigMaterial().incredients.values())
/*     */       {
/*     */         String name;
/* 180 */         if (cfgIngred.custom_id == null) {
/* 181 */           data = ItemManager.getMaterialData(cfgIngred.type_id, cfgIngred.data);
/* 182 */           name = data.getItemType().name();
/*     */         } else {
/* 184 */           name = ((LoreMaterial)LoreMaterial.materialMap.get(cfgIngred.custom_id)).getName();
/*     */         }
/*     */         
/* 187 */         lore.add(ChatColor.RESET + "§6" + cfgIngred.letter + "§f" + " is " + "§b" + name);
/*     */       }
/*     */       
/* 190 */       String name = (data = loreMat.getConfigMaterial().shape).length; for (String str1 = 0; str1 < name; str1++) { String shapeStr = data[str1];
/*     */         
/* 192 */         String line = "§a" + shapeStr.replace(" ", "§7x§a");
/* 193 */         lore.add(ChatColor.RESET + line);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 198 */     if (loreMat.getConfigMaterial().required_tech != null) {
/* 199 */       ConfigTech tech = (ConfigTech)CivSettings.techs.get(loreMat.getConfigMaterial().required_tech);
/* 200 */       if (tech != null) {
/* 201 */         lore.add("§cRequires: §b" + tech.name);
/*     */       }
/*     */     }
/*     */     
/* 205 */     attrs.setLore(lore);
/* 206 */     stack = attrs.getStack();
/* 207 */     return stack;
/*     */   }
/*     */   
/*     */   public static void showCraftingHelp(Player player) {
/* 211 */     if (craftingHelpInventory == null) {
/* 212 */       craftingHelpInventory = Bukkit.getServer().createInventory(player, 54, "CivCraft Custom Item Recipes");
/*     */       
/*     */ 
/* 215 */       for (ConfigMaterialCategory cat : ConfigMaterialCategory.getCategories()) {
/* 216 */         if (cat.craftableCount != 0)
/*     */         {
/*     */ 
/*     */ 
/* 220 */           ItemStack infoRec = LoreGuiItem.build(cat.name, 
/* 221 */             ItemManager.getId(Material.WRITTEN_BOOK), 
/* 222 */             0, new String[] {
/* 223 */             "§b" + cat.materials.size() + " Items", 
/* 224 */             "§6<Click To Open>" });
/* 225 */           infoRec = LoreGuiItem.setAction(infoRec, "openinv:showGuiInv:" + cat.name + " Recipies");
/* 226 */           craftingHelpInventory.addItem(new ItemStack[] { infoRec });
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
/* 237 */           Inventory inv = Bukkit.createInventory(player, 54, cat.name + " Recipies");
/* 238 */           for (ConfigMaterial mat : cat.materials.values())
/*     */           {
/* 240 */             ItemStack stack = getInfoBookForItem(mat.id);
/* 241 */             if (stack != null) {
/* 242 */               inv.addItem(new ItemStack[] { LoreGuiItem.asGuiItem(stack) });
/*     */             }
/*     */           }
/* 245 */           LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
/*     */         }
/*     */       }
/* 248 */       LoreGuiItemListener.guiInventories.put(craftingHelpInventory.getName(), craftingHelpInventory);
/*     */     }
/*     */     
/* 251 */     player.openInventory(craftingHelpInventory);
/*     */   }
/*     */   
/*     */   public static void spawnGuiBook(Player player) {
/* 255 */     if (guiInventory == null) {
/* 256 */       guiInventory = Bukkit.getServer().createInventory(player, 27, "CivCraft Information");
/*     */       
/* 258 */       ItemStack infoRec = LoreGuiItem.build("CivCraft Info", 
/* 259 */         ItemManager.getId(Material.WRITTEN_BOOK), 
/* 260 */         0, new String[] { "§6<Click To View>" });
/* 261 */       infoRec = LoreGuiItem.setAction(infoRec, "openinv:showTutorialInventory");
/* 262 */       guiInventory.addItem(new ItemStack[] { infoRec });
/*     */       
/* 264 */       ItemStack craftRec = LoreGuiItem.build("Crafting Recipes", 
/* 265 */         ItemManager.getId(Material.WRITTEN_BOOK), 
/* 266 */         0, new String[] { "§6<Click To View>" });
/* 267 */       craftRec = LoreGuiItem.setAction(craftRec, "openinv:showCraftingHelp");
/* 268 */       guiInventory.addItem(new ItemStack[] { craftRec });
/* 269 */       LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
/*     */     }
/*     */     
/* 272 */     player.openInventory(guiInventory);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\tutorial\CivTutorial.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */