/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

public class RndTeleportFighter extends Fighter
{
	private long _lastTeleport;
	
	public RndTeleportFighter(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean maybeMoveToHome()
	{
		NpcInstance actor = getActor();
		if ((System.currentTimeMillis() - _lastTeleport) < 10000)
		{
			return false;
		}
		boolean randomWalk = actor.hasRandomWalk();
		Location sloc = actor.getSpawnedLoc();
		if (sloc == null)
		{
			return false;
		}
		if (randomWalk && (!Config.RND_WALK || Rnd.chance(Config.RND_WALK_RATE)))
		{
			return false;
		}
		if (!randomWalk && actor.isInRangeZ(sloc, Config.MAX_DRIFT_RANGE))
		{
			return false;
		}
		int x = sloc.x + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE);
		int y = sloc.y + Rnd.get(-Config.MAX_DRIFT_RANGE, Config.MAX_DRIFT_RANGE);
		int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());
		if ((sloc.z - z) > 64)
		{
			return false;
		}
		SpawnRange spawnRange = actor.getSpawnRange();
		boolean isInside = true;
		if ((spawnRange != null) && (spawnRange instanceof Territory))
		{
			isInside = ((Territory) spawnRange).isInside(x, y);
		}
		if (isInside)
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 4671, 1, 500, 0));
			ThreadPoolManager.getInstance().schedule(new Teleport(new Location(x, y, z)), 500);
			_lastTeleport = System.currentTimeMillis();
		}
		return isInside;
	}
}
