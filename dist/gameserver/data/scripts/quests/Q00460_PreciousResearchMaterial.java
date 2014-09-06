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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00460_PreciousResearchMaterial extends Quest implements ScriptFile
{
	private static final int Amer = 33092; // ok
	private static final int Filar = 30535;
	private static final int Egg = 18997; //
	private static final int POL = 19450; //
	private static final int PART_EGG = 17735;
	
	public Q00460_PreciousResearchMaterial()
	{
		super(PARTY_ALL);
		addStartNpc(Amer);
		addTalkId(Filar);
		addQuestItem(PART_EGG);
		addKillId(Egg);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		
		if (event.equalsIgnoreCase("4.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("reward"))
		{
			if (cond == 2)
			{
				htmltext = "finish.htm";
				st.takeItems(PART_EGG, -1);
				st.giveItems(POL, 3);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (npcId == Amer)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 85)
				{
					htmltext = "no-lvl.htm";
				}
				else if (st.isNowAvailable())
				{
					htmltext = "started.htm";
				}
				else
				{
					htmltext = "no_avaliable.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "taken.htm";
			}
			else if (cond == 2)
			{
				return "cond2.htm";
			}
		}
		else if (npcId == Filar)
		{
			if (cond == 2)
			{
				htmltext = "con_quest.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if ((cond == 1) && (st.getQuestItemsCount(PART_EGG) < 20))
		{
			st.giveItems(PART_EGG, 1);
		}
		else if ((cond == 1) && (st.getQuestItemsCount(PART_EGG) >= 20))
		{
			st.giveItems(PART_EGG, 1);
			st.setCond(2);
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
		// null
	}
	
	@Override
	public void onReload()
	{
		// null
	}
	
	@Override
	public void onShutdown()
	{
		// null
	}
}