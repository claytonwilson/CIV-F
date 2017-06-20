/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import org.bukkit.ChatColor;
/*     */ 
/*     */ public class CivColor
/*     */ {
/*     */   public static final String Black = "§0";
/*     */   public static final String Navy = "§1";
/*     */   public static final String Green = "§2";
/*     */   public static final String Blue = "§3";
/*     */   public static final String Red = "§4";
/*     */   public static final String Purple = "§5";
/*     */   public static final String Gold = "§6";
/*     */   public static final String LightGray = "§7";
/*     */   public static final String Gray = "§8";
/*     */   public static final String DarkPurple = "§9";
/*     */   public static final String LightGreen = "§a";
/*     */   public static final String LightBlue = "§b";
/*     */   public static final String Rose = "§c";
/*     */   public static final String LightPurple = "§d";
/*     */   public static final String Yellow = "§e";
/*     */   public static final String White = "§f";
/*  23 */   public static final String BOLD = ChatColor.BOLD;
/*  24 */   public static final String ITALIC = ChatColor.ITALIC;
/*  25 */   public static final String MAGIC = ChatColor.MAGIC;
/*  26 */   public static final String STRIKETHROUGH = ChatColor.STRIKETHROUGH;
/*  27 */   public static final String RESET = ChatColor.RESET;
/*  28 */   public static final String UNDERLINE = ChatColor.UNDERLINE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String colorize(String input)
/*     */   {
/*  37 */     String output = input;
/*     */     
/*  39 */     output = output.replaceAll("<red>", "§4");
/*  40 */     output = output.replaceAll("<rose>", "§c");
/*  41 */     output = output.replaceAll("<gold>", "§6");
/*  42 */     output = output.replaceAll("<yellow>", "§e");
/*  43 */     output = output.replaceAll("<green>", "§2");
/*  44 */     output = output.replaceAll("<lightgreen>", "§a");
/*  45 */     output = output.replaceAll("<lightblue>", "§b");
/*  46 */     output = output.replaceAll("<blue>", "§3");
/*  47 */     output = output.replaceAll("<navy>", "§1");
/*  48 */     output = output.replaceAll("<darkpurple>", "§9");
/*  49 */     output = output.replaceAll("<lightpurple>", "§d");
/*  50 */     output = output.replaceAll("<purple>", "§5");
/*  51 */     output = output.replaceAll("<white>", "§f");
/*  52 */     output = output.replaceAll("<lightgray>", "§7");
/*  53 */     output = output.replaceAll("<gray>", "§8");
/*  54 */     output = output.replaceAll("<black>", "§0");
/*  55 */     output = output.replaceAll("<b>", ChatColor.BOLD);
/*  56 */     output = output.replaceAll("<u>", ChatColor.UNDERLINE);
/*  57 */     output = output.replaceAll("<i>", ChatColor.ITALIC);
/*  58 */     output = output.replaceAll("<magic>", ChatColor.MAGIC);
/*  59 */     output = output.replaceAll("<s>", ChatColor.STRIKETHROUGH);
/*  60 */     output = output.replaceAll("<r>", ChatColor.RESET);
/*     */     
/*  62 */     return output;
/*     */   }
/*     */   
/*     */   public static String strip(String line) {
/*     */     ChatColor[] arrayOfChatColor;
/*  67 */     int j = (arrayOfChatColor = ChatColor.values()).length; for (int i = 0; i < j; i++) { ChatColor cc = arrayOfChatColor[i];
/*  68 */       line.replaceAll(cc.toString(), ""); }
/*  69 */     return line;
/*     */   }
/*     */   
/*     */   public static String valueOf(String color) { String str;
/*  73 */     switch ((str = color.toLowerCase()).hashCode()) {case -1682598830:  if (str.equals("lightpurple")) {} break; case -1092352334:  if (str.equals("darkpurple")) {} break; case -976943172:  if (str.equals("purple")) {} break; case -734239628:  if (str.equals("yellow")) {} break; case -201238611:  if (str.equals("lightgreen")) {} break; case 112785:  if (str.equals("red")) {} break; case 3027034:  if (str.equals("blue")) {} break; case 3178592:  if (str.equals("gold")) {} break; case 3181155:  if (str.equals("gray")) {} break; case 3374006:  if (str.equals("navy")) {} break; case 3506511:  if (str.equals("rose")) {} break; case 93818879:  if (str.equals("black")) break; break; case 98619139:  if (str.equals("green")) {} break; case 113101865:  if (str.equals("white")) {} break; case 686090864:  if (str.equals("lightblue")) {} break; case 686244985:  if (!str.equals("lightgray")) {
/*     */         break label388;
/*  75 */         return "§0";
/*     */         
/*  77 */         return "§1";
/*     */         
/*  79 */         return "§2";
/*     */         
/*  81 */         return "§3";
/*     */         
/*  83 */         return "§4";
/*     */         
/*  85 */         return "§5";
/*     */         
/*  87 */         return "§6";
/*     */       } else {
/*  89 */         return "§7";
/*     */         
/*  91 */         return "§8";
/*     */         
/*  93 */         return "§9";
/*     */         
/*  95 */         return "§a";
/*     */         
/*  97 */         return "§b";
/*     */         
/*  99 */         return "§c";
/*     */         
/* 101 */         return "§d";
/*     */         
/* 103 */         return "§e";
/*     */         
/* 105 */         return "§f"; }
/*     */       break; }
/* 107 */     label388: return "§f";
/*     */   }
/*     */   
/*     */   public static String stripTags(String input)
/*     */   {
/* 112 */     String output = input;
/*     */     
/* 114 */     output = output.replaceAll("<red>", "");
/* 115 */     output = output.replaceAll("<rose>", "");
/* 116 */     output = output.replaceAll("<gold>", "");
/* 117 */     output = output.replaceAll("<yellow>", "");
/* 118 */     output = output.replaceAll("<green>", "");
/* 119 */     output = output.replaceAll("<lightgreen>", "");
/* 120 */     output = output.replaceAll("<lightblue>", "");
/* 121 */     output = output.replaceAll("<blue>", "");
/* 122 */     output = output.replaceAll("<navy>", "");
/* 123 */     output = output.replaceAll("<darkpurple>", "");
/* 124 */     output = output.replaceAll("<lightpurple>", "");
/* 125 */     output = output.replaceAll("<purple>", "");
/* 126 */     output = output.replaceAll("<white>", "");
/* 127 */     output = output.replaceAll("<lightgray>", "");
/* 128 */     output = output.replaceAll("<gray>", "");
/* 129 */     output = output.replaceAll("<black>", "");
/* 130 */     output = output.replaceAll("<b>", "");
/* 131 */     output = output.replaceAll("<u>", "");
/* 132 */     output = output.replaceAll("<i>", "");
/* 133 */     output = output.replaceAll("<magic>", "");
/* 134 */     output = output.replaceAll("<s>", "");
/* 135 */     output = output.replaceAll("<r>", "");
/*     */     
/* 137 */     return output;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\CivColor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */