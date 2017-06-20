/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import java.util.HashMap;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class ItemFrameStorage
/*     */ {
/*     */   private UUID frameID;
/*     */   private Location location;
/*  50 */   private Buildable buildable = null;
/*     */   
/*     */   private BlockCoord attachedBlock;
/*     */   
/*  54 */   public static HashMap<BlockCoord, ItemFrameStorage> attachedBlockMap = new HashMap();
/*     */   
/*     */   public ItemFrameStorage(ItemFrame frame, Location attachedLoc) throws CivException {
/*  57 */     if (frame != null) {
/*  58 */       this.frameID = frame.getUniqueId();
/*  59 */       this.location = frame.getLocation();
/*  60 */       this.attachedBlock = new BlockCoord(attachedLoc);
/*  61 */       CivGlobal.addProtectedItemFrame(this);
/*     */     } else {
/*  63 */       throw new CivException("Passed a null item frame to storage constructor.");
/*     */     }
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
/*     */   public ItemFrameStorage(Location location, BlockFace blockface)
/*     */   {
/*  77 */     ItemFrame frame = (ItemFrame)location.getWorld().spawnEntity(location, EntityType.ITEM_FRAME);
/*     */     
/*     */ 
/*  80 */     this.frameID = frame.getUniqueId();
/*  81 */     this.location = frame.getLocation();
/*  82 */     this.attachedBlock = new BlockCoord(location);
/*  83 */     CivGlobal.addProtectedItemFrame(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemFrame getItemFrame()
/*     */   {
/*  91 */     if ((!this.location.getChunk().isLoaded()) && 
/*  92 */       (!this.location.getChunk().load())) {
/*  93 */       CivLog.error("Could not load chunk to get item frame at:" + this.location);
/*  94 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  98 */     Entity ent = CivGlobal.getEntityClassFromUUID(this.location.getWorld(), ItemFrame.class, this.frameID);
/*  99 */     if (ent == null) {
/* 100 */       CivLog.error("Could not find frame from frame ID:" + this.frameID.toString());
/* 101 */       return null;
/*     */     }
/*     */     
/* 104 */     if (!(ent instanceof ItemFrame)) {
/* 105 */       CivLog.error("Could not get a frame with ID:" + this.frameID + " ... it was not a frame.");
/* 106 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 111 */     return (ItemFrame)ent;
/*     */   }
/*     */   
/*     */   public UUID getUUID()
/*     */   {
/* 116 */     return getFrameID();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFacingDirection(BlockFace blockface) {}
/*     */   
/*     */ 
/*     */   public void setItem(ItemStack stack)
/*     */   {
/* 126 */     ItemFrame frame = getItemFrame();
/* 127 */     if (frame != null) {
/* 128 */       ItemStack newStack = new ItemStack(stack.getType(), 1, stack.getDurability());
/* 129 */       newStack.setData(stack.getData());
/* 130 */       newStack.setItemMeta(stack.getItemMeta());
/* 131 */       frame.setItem(newStack);
/*     */     } else {
/* 133 */       CivLog.warning("Frame:" + this.frameID + " was null when trying to set to " + stack.getType().name());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearItem()
/*     */   {
/* 139 */     setItem(ItemManager.createItemStack(0, 1));
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 143 */     ItemFrame frame = getItemFrame();
/* 144 */     return frame.getItem();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() throws CivException {
/* 148 */     ItemFrame frame = getItemFrame();
/*     */     
/* 150 */     if (frame == null) {
/* 151 */       throw new CivException("Bad frame. Could not be found.");
/*     */     }
/*     */     
/* 154 */     if ((frame.getItem() == null) || (frame.getItem().getType().equals(Material.AIR))) {
/* 155 */       return true;
/*     */     }
/*     */     
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 162 */     return this.location;
/*     */   }
/*     */   
/*     */   public boolean isOurEntity(Entity entity) {
/* 166 */     return ((ItemFrame)entity).getUniqueId().equals(getUUID());
/*     */   }
/*     */   
/*     */   public boolean noFrame() {
/* 170 */     ItemFrame frame = getItemFrame();
/* 171 */     return frame == null;
/*     */   }
/*     */   
/*     */   public Object getCoord() {
/* 175 */     return new BlockCoord(getLocation());
/*     */   }
/*     */   
/*     */   public UUID getFrameID() {
/* 179 */     return this.frameID;
/*     */   }
/*     */   
/*     */   public void setFrameID(UUID frameID) {
/* 183 */     this.frameID = frameID;
/*     */   }
/*     */   
/*     */   public void setLocation(Location location) {
/* 187 */     this.location = location;
/*     */   }
/*     */   
/*     */   public Town getTown() {
/* 191 */     if (this.buildable != null) {
/* 192 */       return this.buildable.getTown();
/*     */     }
/* 194 */     return null;
/*     */   }
/*     */   
/*     */   public void setBuildable(Buildable buildable) {
/* 198 */     this.buildable = buildable;
/*     */   }
/*     */   
/*     */   public BlockCoord getAttachedBlock() {
/* 202 */     return this.attachedBlock;
/*     */   }
/*     */   
/*     */   public void setAttachedBlock(BlockCoord attachedBlock) {
/* 206 */     this.attachedBlock = attachedBlock;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\ItemFrameStorage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */