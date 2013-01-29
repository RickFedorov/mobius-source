/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.ai;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Summon;

public class SummonAI extends PlayableAI
{
	public SummonAI(Summon actor)
	{
		super(actor);
	}
	
	@Override
	protected void thinkActive()
	{
		Summon actor = getActor();
		clearNextAction();
		if (actor.isDepressed())
		{
			setAttackTarget(actor.getPlayer());
			changeIntention(CtrlIntention.AI_INTENTION_ATTACK, actor.getPlayer(), null);
			thinkAttack(true);
		}
		else if (actor.isFollowMode())
		{
			changeIntention(CtrlIntention.AI_INTENTION_FOLLOW, actor.getPlayer(), Config.FOLLOW_RANGE);
			thinkFollow();
		}
		super.thinkActive();
	}
	
	@Override
	protected void thinkAttack(boolean checkRange)
	{
		Summon actor = getActor();
		if (actor.isDepressed())
		{
			setAttackTarget(actor.getPlayer());
		}
		super.thinkAttack(checkRange);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		Summon actor = getActor();
		if ((attacker != null) && actor.getPlayer().isDead() && !actor.isDepressed())
		{
			Attack(attacker, false, false);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	public Summon getActor()
	{
		return (Summon) super.getActor();
	}
}