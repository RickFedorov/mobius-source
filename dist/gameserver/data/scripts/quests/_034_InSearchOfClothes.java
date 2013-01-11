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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _034_InSearchOfClothes extends Quest implements ScriptFile
{
	int SPINNERET = 7528;
	int SUEDE = 1866;
	int THREAD = 1868;
	int SPIDERSILK = 1493;
	int MYSTERIOUS_CLOTH = 7076;
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	public _034_InSearchOfClothes()
	{
		super(false);
		addStartNpc(30088);
		addTalkId(30088);
		addTalkId(30165);
		addTalkId(30294);
		addKillId(20560);
		addQuestItem(SPINNERET);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if (event.equals("30088-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30294-1.htm") && (cond == 1))
		{
			st.setCond(2);
		}
		else if (event.equals("30088-3.htm") && (cond == 2))
		{
			st.setCond(3);
		}
		else if (event.equals("30165-1.htm") && (cond == 3))
		{
			st.setCond(4);
		}
		else if (event.equals("30165-3.htm") && (cond == 5))
		{
			if (st.getQuestItemsCount(SPINNERET) == 10)
			{
				st.takeItems(SPINNERET, 10);
				st.giveItems(SPIDERSILK, 1);
				st.setCond(6);
			}
			else
			{
				htmltext = "30165-1r.htm";
			}
		}
		else if (event.equals("30088-5.htm") && (cond == 6))
		{
			if ((st.getQuestItemsCount(SUEDE) >= 3000) && (st.getQuestItemsCount(THREAD) >= 5000) && (st.getQuestItemsCount(SPIDERSILK) == 1))
			{
				st.takeItems(SUEDE, 3000);
				st.takeItems(THREAD, 5000);
				st.takeItems(SPIDERSILK, 1);
				st.giveItems(MYSTERIOUS_CLOTH, 1);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30088-havent.htm";
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
		if (npcId == 30088)
		{
			if ((cond == 0) && (st.getQuestItemsCount(MYSTERIOUS_CLOTH) == 0))
			{
				if (st.getPlayer().getLevel() >= 60)
				{
					QuestState fwear = st.getPlayer().getQuestState(_037_PleaseMakeMeFormalWear.class);
					if ((fwear != null) && (fwear.getCond() == 6))
					{
						htmltext = "30088-0.htm";
					}
					else
					{
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30088-6.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "30088-1r.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30088-2.htm";
			}
			else if (cond == 3)
			{
				htmltext = "30088-3r.htm";
			}
			else if ((cond == 6) && ((st.getQuestItemsCount(SUEDE) < 3000) || (st.getQuestItemsCount(THREAD) < 5000) || (st.getQuestItemsCount(SPIDERSILK) < 1)))
			{
				htmltext = "30088-havent.htm";
			}
			else if (cond == 6)
			{
				htmltext = "30088-4.htm";
			}
		}
		else if (npcId == 30294)
		{
			if (cond == 1)
			{
				htmltext = "30294-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30294-1r.htm";
			}
		}
		else if (npcId == 30165)
		{
			if (cond == 3)
			{
				htmltext = "30165-0.htm";
			}
			else if ((cond == 4) && (st.getQuestItemsCount(SPINNERET) < 10))
			{
				htmltext = "30165-1r.htm";
			}
			else if (cond == 5)
			{
				htmltext = "30165-2.htm";
			}
			else if ((cond == 6) && ((st.getQuestItemsCount(SUEDE) < 3000) || (st.getQuestItemsCount(THREAD) < 5000) || (st.getQuestItemsCount(SPIDERSILK) < 1)))
			{
				htmltext = "30165-3r.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getQuestItemsCount(SPINNERET) < 10)
		{
			st.giveItems(SPINNERET, 1);
			if (st.getQuestItemsCount(SPINNERET) == 10)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
