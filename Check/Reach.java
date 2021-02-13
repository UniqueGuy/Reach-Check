package me.uniqueman.ac.check.combat;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.uniqueman.ac.check.Check;
import me.uniqueman.ac.check.CheckData;
import me.uniqueman.ac.data.EntityData;
import me.uniqueman.ac.data.PlayerData;
import me.uniqueman.ac.packets.events.PacketPlayInUseEntity;

@CheckData(name = "Reach", type = "A", experimental = false)
public class Reach extends Check {
	
	@EventHandler
	public void useEntity(PacketPlayInUseEntity e) {
		Player player = e.getPlayer();
		int id = e.getId();
		PlayerData pData = dm.getData(player);
		EntityData data = pData.entityData.get(id);
		if(data != null && data.isSafe) {
			if(pData.clientGameMode == 0 || pData.clientGameMode == 2) {
				if (pData.isTeleporting || data.isRiding != 0 || pData.isRiding != 0) {
					return;
				}
				double finaldist = -1.0D;
				double selfX = pData.x;
				double selfY = pData.yHead;
				double selfZ = pData.z;
				for(int t=0; t<data.x.size(); t++) {
					//could create an AxisAlignedBB
					double maxX = data.x.get(t) + (double)0.4f;
					double maxY = data.y.get(t) + (double)1.9f;
					double maxZ = data.z.get(t) + (double)0.4f;
					double minX = data.x.get(t) - (double)0.4f;
					double minY = data.y.get(t) - (double)0.1f;
					double minZ = data.z.get(t) - (double)0.4f;
					
					double closestX = 0;
					double closestY = 0;
					double closestZ = 0;
					
					//Gets the closest distance from the player's head location to the outside of the hitbox
					if(selfX < minX) closestX = Math.abs(selfX - minX);
					if(selfX > maxX) closestX = Math.abs(selfX - maxX);
					if(selfY < minY) closestY = Math.abs(selfY - minY);
					if(selfY > maxY) closestY = Math.abs(selfY - maxY);
					if(selfZ < minZ) closestZ = Math.abs(selfZ - minZ);
					if(selfZ > maxZ) closestZ = Math.abs(selfZ - maxZ);
					double dist = Math.sqrt(closestX * closestX + closestY * closestY + closestZ * closestZ);
					
					if(finaldist == -1.0D) {
						finaldist = dist;
					}else if(finaldist > dist) {
						finaldist = dist;
					}
				}
				if(pData.lastMove > 0) finaldist -= 0.052D;
				if(pData.isSneaking == 2) finaldist -= 0.139D;
				if(finaldist > 3.000001D) {
					DecimalFormat df = new DecimalFormat("#.####");
					alert(player, "Reach", null, df.format(finaldist) + " Blocks");
				}
			}
		}
	}
}
