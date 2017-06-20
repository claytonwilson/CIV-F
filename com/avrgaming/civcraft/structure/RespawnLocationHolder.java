package com.avrgaming.civcraft.structure;

import com.avrgaming.civcraft.util.BlockCoord;
import java.util.List;

public abstract interface RespawnLocationHolder
{
  public abstract String getRespawnName();
  
  public abstract List<BlockCoord> getRespawnPoints();
  
  public abstract BlockCoord getRandomRevivePoint();
}


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\RespawnLocationHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */