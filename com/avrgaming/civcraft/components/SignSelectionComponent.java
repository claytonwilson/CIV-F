/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.util.HashMap;
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
/*     */ public class SignSelectionComponent
/*     */   extends Component
/*     */ {
/*  33 */   private int selectedIndex = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BlockCoord actionSignCoord;
/*     */   
/*     */ 
/*     */ 
/*  42 */   private HashMap<Integer, SelectionItem> items = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */   public void onSave() {}
/*     */   
/*     */ 
/*     */   public void addItem(String[] message, SignSelectionActionInterface action)
/*     */   {
/*  54 */     SelectionItem item = new SelectionItem(null);
/*  55 */     item.message = message;
/*  56 */     item.action = action;
/*  57 */     this.items.put(Integer.valueOf(this.items.size()), item);
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
/*     */   public void updateActionSign()
/*     */   {
/*  90 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  64 */         if (SignSelectionComponent.this.actionSignCoord == null) {
/*  65 */           CivLog.warning("No action sign block coord found!");
/*  66 */           return;
/*     */         }
/*     */         
/*  69 */         Block block = SignSelectionComponent.this.actionSignCoord.getBlock();
/*     */         
/*  71 */         if ((block.getState() instanceof Sign)) {
/*  72 */           Sign sign = (Sign)block.getState();
/*  73 */           SignSelectionComponent.SelectionItem item = (SignSelectionComponent.SelectionItem)SignSelectionComponent.this.items.get(Integer.valueOf(SignSelectionComponent.this.selectedIndex));
/*  74 */           if (item != null) {
/*  75 */             sign.setLine(0, item.message[0]);
/*  76 */             sign.setLine(1, item.message[1]);
/*  77 */             sign.setLine(2, item.message[2]);
/*  78 */             sign.setLine(3, item.message[3]);
/*  79 */             sign.update();
/*     */           } else {
/*  81 */             sign.setLine(0, "");
/*  82 */             sign.setLine(1, "Nothing");
/*  83 */             sign.setLine(2, "Available");
/*  84 */             sign.setLine(3, "");
/*  85 */             sign.update();
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void processNext()
/*     */   {
/*  94 */     this.selectedIndex += 1;
/*     */     
/*  96 */     if (this.selectedIndex >= this.items.size()) {
/*  97 */       this.selectedIndex = 0;
/*     */     }
/*     */     
/* 100 */     updateActionSign();
/*     */   }
/*     */   
/*     */   public void processPrev() {
/* 104 */     this.selectedIndex -= 1;
/*     */     
/* 106 */     if (this.selectedIndex < 0) {
/* 107 */       this.selectedIndex = (this.items.size() - 1);
/*     */     }
/*     */     
/* 110 */     updateActionSign();
/*     */   }
/*     */   
/*     */   public void processAction(Player player) {
/* 114 */     SelectionItem item = (SelectionItem)this.items.get(Integer.valueOf(this.selectedIndex));
/* 115 */     if (item == null) {
/* 116 */       CivLog.warning("Selected index:" + this.selectedIndex + " has no selection item!");
/* 117 */       return;
/*     */     }
/*     */     
/* 120 */     item.action.process(player);
/*     */   }
/*     */   
/*     */   public void setActionSignCoord(BlockCoord absCoord) {
/* 124 */     this.actionSignCoord = absCoord;
/*     */   }
/*     */   
/*     */   public void setMessageAllItems(int i, String string) {
/* 128 */     for (SelectionItem item : this.items.values()) {
/* 129 */       item.message[i] = string;
/*     */     }
/* 131 */     updateActionSign();
/*     */   }
/*     */   
/*     */   private class SelectionItem
/*     */   {
/*     */     public String[] message;
/*     */     public SignSelectionActionInterface action;
/*     */     
/*     */     private SelectionItem() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\SignSelectionComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */