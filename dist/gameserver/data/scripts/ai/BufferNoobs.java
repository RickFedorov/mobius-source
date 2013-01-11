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

import lineage2.commons.collections.LazyArrayList;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

public class BufferNoobs extends DefaultAI
{
	private static final int[] BUFFS =
	{
		4322,
		4323,
		4324,
		4325,
		4326,
		4327,
		4328
	};
	
	public BufferNoobs(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return true;
		}
		Skill skill;
		for (Player player : World.getAroundPlayers(actor, 200, 200))
		{
			if (checkBuff(player))
			{
				for (int skillId : BUFFS)
				{
					skill = SkillTable.getInstance().getInfo(skillId, 1);
					LazyArrayList<Creature> target = new LazyArrayList<>();
					target.add(player);
					actor.broadcastPacket(new MagicSkillUse(actor, player, skillId, 0, 0, 0));
					actor.callSkill(skill, target, true);
				}
				player.sendPacket(new ExShowScreenMessage(NpcString.NEWBIE_GUIDE_GIVE_YOU_THE_MAGIC_OF_SATTELITE_S1, 800, ScreenMessageAlign.TOP_CENTER, player.getName()));
			}
		}
		return true;
	}
	
	private boolean checkBuff(Player player)
	{
		for (int skillId : BUFFS)
		{
			if (player.getEffectList().getEffectsBySkillId(skillId) != null)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
