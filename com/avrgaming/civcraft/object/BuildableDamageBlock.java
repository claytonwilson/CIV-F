package com.avrgaming.civcraft.object;

import com.avrgaming.civcraft.structure.Buildable;
import com.avrgaming.civcraft.util.BlockCoord;
import org.bukkit.entity.Player;

public abstract interface BuildableDamageBlock
{
  public abstract Buildable getOwner();
  
  public abstract void setOwner(Buildable paramBuildable);
  
  public abstract Town getTown();
  
  public abstract Civilization getCiv();
  
  public abstract BlockCoord getCoord();
  
  public abstract void setCoord(BlockCoord paramBlockCoord);
  
  public abstract int getX();
  
  public abstract int getY();
  
  public abstract int getZ();
  
  public abstract String getWorldname();
  
  public abstract boolean isDamageable();
  
  public abstract void setDamageable(boolean paramBoolean);
  
  public abstract boolean canDestroyOnlyDuringWar();
  
  public abstract boolean allowDamageNow(Player paramPlayer);
}


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\BuildableDamageBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */