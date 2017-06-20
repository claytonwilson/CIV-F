/*     */ package gpl;
/*     */ 
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventorySerializer
/*     */ {
/*     */   private static String getSerializedItemStack(ItemStack is)
/*     */   {
/*  24 */     String serializedItemStack = new String();
/*     */     
/*  26 */     String isType = String.valueOf(ItemManager.getId(is.getType()));
/*  27 */     serializedItemStack = serializedItemStack + "t@" + isType;
/*     */     
/*  29 */     if (is.getDurability() != 0)
/*     */     {
/*  31 */       String isDurability = String.valueOf(is.getDurability());
/*  32 */       serializedItemStack = serializedItemStack + "&d@" + isDurability;
/*     */     }
/*     */     
/*  35 */     if (is.getAmount() != 1)
/*     */     {
/*  37 */       String isAmount = String.valueOf(is.getAmount());
/*  38 */       serializedItemStack = serializedItemStack + "&a@" + isAmount;
/*     */     }
/*     */     
/*  41 */     Map<Enchantment, Integer> isEnch = is.getEnchantments();
/*  42 */     if (isEnch.size() > 0)
/*     */     {
/*  44 */       for (Map.Entry<Enchantment, Integer> ench : isEnch.entrySet())
/*     */       {
/*  46 */         serializedItemStack = serializedItemStack + "&e@" + ItemManager.getId((Enchantment)ench.getKey()) + "@" + ench.getValue();
/*     */       }
/*     */     }
/*     */     
/*  50 */     ItemMeta meta = is.getItemMeta();
/*  51 */     if ((meta != null) && (meta.hasLore())) {
/*  52 */       for (String lore : meta.getLore()) {
/*  53 */         char[] encode = Base64Coder.encode(lore.getBytes());
/*  54 */         String encodedString = new String(encode);
/*  55 */         serializedItemStack = serializedItemStack + "&l@" + encodedString;
/*     */       }
/*     */     }
/*     */     
/*  59 */     if ((meta != null) && 
/*  60 */       (meta.getDisplayName() != null)) {
/*  61 */       serializedItemStack = serializedItemStack + "&D@" + meta.getDisplayName();
/*     */     }
/*     */     
/*     */ 
/*  65 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(is);
/*  66 */     if (craftMat != null) {
/*  67 */       serializedItemStack = serializedItemStack + "&C@" + craftMat.getConfigId();
/*     */       
/*  69 */       if (LoreCraftableMaterial.hasEnhancements(is)) {
/*  70 */         serializedItemStack = serializedItemStack + "&Enh@" + LoreCraftableMaterial.serializeEnhancements(is);
/*     */       }
/*     */     }
/*     */     
/*  74 */     AttributeUtil attrs = new AttributeUtil(is);
/*  75 */     if (attrs.hasColor()) {
/*  76 */       serializedItemStack = serializedItemStack + "&LC@" + attrs.getColor();
/*     */     }
/*     */     
/*  79 */     return serializedItemStack;
/*     */   }
/*     */   
/*     */   private static ItemStack getItemStackFromSerial(String serial) {
/*  83 */     ItemStack is = null;
/*  84 */     Boolean createdItemStack = Boolean.valueOf(false);
/*  85 */     List<String> lore = new LinkedList();
/*     */     
/*     */ 
/*  88 */     String[] serializedItemStack = serial.split("&");
/*  89 */     String[] arrayOfString1; int j = (arrayOfString1 = serializedItemStack).length; for (int i = 0; i < j; i++) { String itemInfo = arrayOfString1[i];
/*     */       
/*  91 */       String[] itemAttribute = itemInfo.split("@");
/*  92 */       if (itemAttribute[0].equals("t"))
/*     */       {
/*  94 */         is = ItemManager.createItemStack(Integer.valueOf(itemAttribute[1]).intValue(), 1);
/*  95 */         createdItemStack = Boolean.valueOf(true);
/*     */       }
/*  97 */       else if ((itemAttribute[0].equals("d")) && (createdItemStack.booleanValue()))
/*     */       {
/*  99 */         is.setDurability(Short.valueOf(itemAttribute[1]).shortValue());
/*     */       }
/* 101 */       else if ((itemAttribute[0].equals("a")) && (createdItemStack.booleanValue()))
/*     */       {
/* 103 */         is.setAmount(Integer.valueOf(itemAttribute[1]).intValue());
/*     */       }
/* 105 */       else if ((itemAttribute[0].equals("e")) && (createdItemStack.booleanValue()))
/*     */       {
/* 107 */         is.addEnchantment(ItemManager.getEnchantById(Integer.valueOf(itemAttribute[1]).intValue()), Integer.valueOf(itemAttribute[2]).intValue());
/*     */       }
/* 109 */       else if ((itemAttribute[0].equals("l")) && (createdItemStack.booleanValue()))
/*     */       {
/* 111 */         byte[] decode = Base64Coder.decode(itemAttribute[1]);
/* 112 */         String decodedString = new String(decode);
/* 113 */         lore.add(decodedString);
/*     */       }
/* 115 */       else if ((itemAttribute[0].equals("D")) && (createdItemStack.booleanValue())) {
/* 116 */         ItemMeta meta = is.getItemMeta();
/* 117 */         if (meta != null) {
/* 118 */           meta.setDisplayName(itemAttribute[1]);
/*     */         }
/* 120 */         is.setItemMeta(meta);
/* 121 */       } else if (itemAttribute[0].equals("C"))
/*     */       {
/* 123 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(itemAttribute[1]);
/*     */         try {
/* 125 */           AttributeUtil attrs = new AttributeUtil(is);
/* 126 */           LoreCraftableMaterial.setMIDAndName(attrs, itemAttribute[1], craftMat.getName());
/* 127 */           is = attrs.getStack();
/*     */         } catch (NullPointerException e) {
/* 129 */           e.printStackTrace();
/*     */         }
/* 131 */       } else if (itemAttribute[0].equals("Enh")) {
/* 132 */         is = LoreCraftableMaterial.deserializeEnhancements(is, itemAttribute[1]);
/* 133 */       } else if (itemAttribute[0].equals("LC")) {
/* 134 */         AttributeUtil attrs = new AttributeUtil(is);
/* 135 */         attrs.setColor(Long.valueOf(itemAttribute[1]));
/* 136 */         is = attrs.getStack();
/*     */       }
/*     */     }
/*     */     
/* 140 */     if (lore.size() > 0) {
/* 141 */       ItemMeta meta = is.getItemMeta();
/* 142 */       if (meta != null) {
/* 143 */         meta.setLore(lore);
/* 144 */         is.setItemMeta(meta);
/*     */       }
/*     */     }
/*     */     
/* 148 */     return is;
/*     */   }
/*     */   
/*     */   public static String InventoryToString(Inventory invInventory)
/*     */   {
/* 153 */     String serialization = invInventory.getSize() + ";";
/* 154 */     String serializedItemStack; for (int i = 0; i < invInventory.getSize(); i++)
/*     */     {
/* 156 */       ItemStack is = invInventory.getItem(i);
/* 157 */       if (is != null)
/*     */       {
/* 159 */         serializedItemStack = getSerializedItemStack(is);
/* 160 */         serialization = serialization + i + "#" + serializedItemStack + ";";
/*     */       }
/*     */     }
/*     */     
/* 164 */     if ((invInventory instanceof PlayerInventory)) {
/* 165 */       serialization = serialization + "&PINV@";
/* 166 */       PlayerInventory pInv = (PlayerInventory)invInventory;
/*     */       ItemStack[] arrayOfItemStack;
/* 168 */       String str1 = (arrayOfItemStack = pInv.getArmorContents()).length; for (serializedItemStack = 0; serializedItemStack < str1; serializedItemStack++) { ItemStack stack = arrayOfItemStack[serializedItemStack];
/* 169 */         if (stack != null) {
/* 170 */           serialization = serialization + getSerializedItemStack(stack) + ";";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 175 */     return serialization;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void StringToInventory(Inventory inv, String inString)
/*     */   {
/* 181 */     String[] inventorySplit = null;
/*     */     String invString;
/* 183 */     String invString; if ((inv instanceof PlayerInventory)) {
/* 184 */       inventorySplit = inString.split("&PINV@");
/* 185 */       invString = inventorySplit[0];
/*     */     } else {
/* 187 */       invString = inString;
/*     */     }
/*     */     
/* 190 */     String[] serializedBlocks = invString.split(";");
/* 191 */     inv.clear();
/*     */     
/* 193 */     for (int i = 1; i < serializedBlocks.length; i++)
/*     */     {
/* 195 */       String[] serializedBlock = serializedBlocks[i].split("#");
/* 196 */       int stackPosition = Integer.valueOf(serializedBlock[0]).intValue();
/*     */       
/* 198 */       if (stackPosition < inv.getSize())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 203 */         ItemStack is = getItemStackFromSerial(serializedBlock[1]);
/* 204 */         inv.setItem(stackPosition, is);
/*     */       }
/*     */     }
/* 207 */     if ((inv instanceof PlayerInventory)) {
/* 208 */       PlayerInventory pInv = (PlayerInventory)inv;
/* 209 */       invString = inventorySplit[1];
/* 210 */       String[] serializedBlocksArmor = invString.split(";");
/*     */       
/* 212 */       ItemStack[] contents = new ItemStack[4];
/* 213 */       for (int i = 0; i < serializedBlocksArmor.length; i++)
/*     */       {
/* 215 */         ItemStack is = getItemStackFromSerial(serializedBlocksArmor[i]);
/* 216 */         contents[i] = is;
/*     */       }
/*     */       
/* 219 */       pInv.setArmorContents(contents);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\gpl\InventorySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */