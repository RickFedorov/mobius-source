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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _236_SeedsOfChaos extends Quest implements ScriptFile
{
	private final static int KEKROPUS = 32138;
	private final static int WIZARD = 31522;
	private final static int KATENAR = 32333;
	private final static int ROCK = 32238;
	private final static int HARKILGAMED = 32236;
	private final static int MAO = 32190;
	private final static int RODENPICULA = 32237;
	private final static int NORNIL = 32239;
	private final static int[] NEEDLE_STAKATO_DRONES =
	{
		21516,
		21517
	};
	private final static int[] SPLENDOR_MOBS =
	{
		21520,
		21521,
		21522,
		21523,
		21524,
		21525,
		21526,
		21527,
		21528,
		21529,
		21530,
		21531,
		21532,
		21533,
		21534,
		21535,
		21536,
		21537,
		21538,
		21539,
		21540,
		21541
	};
	private final static int STAR_OF_DESTINY = 5011;
	private final static int SCROLL_ENCHANT_WEAPON_A = 729;
	private final static int SHINING_MEDALLION = 9743;
	private final static int BLACK_ECHO_CRYSTAL = 9745;
	private final static int BLACK_ECHO_CRYSTAL_CHANCE = (int) (15 * Config.RATE_QUESTS_DROP);
	private final static int SHINING_MEDALLION_CHANCE = (int) (20 * Config.RATE_QUESTS_DROP);
	static boolean KATENAR_SPAWNED = false;
	static boolean HARKILGAMED_SPAWNED = false;
	
	public _236_SeedsOfChaos()
	{
		super(false);
		addStartNpc(KEKROPUS);
		addTalkId(WIZARD);
		addTalkId(KATENAR);
		addTalkId(ROCK);
		addTalkId(HARKILGAMED);
		addTalkId(MAO);
		addTalkId(RODENPICULA);
		addTalkId(NORNIL);
		for (int kill_id : NEEDLE_STAKATO_DRONES)
		{
			addKillId(kill_id);
		}
		for (int kill_id : SPLENDOR_MOBS)
		{
			addKillId(kill_id);
		}
		addQuestItem(SHINING_MEDALLION);
		addQuestItem(BLACK_ECHO_CRYSTAL);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int _state = st.getState();
		int cond = st.getCond();
		if (event.equalsIgnoreCase("32138_02b.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31522_02.htm") && (_state == STARTED) && (cond == 1))
		{
			st.setCond(2);
		}
		else if (event.equalsIgnoreCase("32236_08.htm") && (_state == STARTED) && (cond == 13))
		{
			st.setCond(14);
		}
		else if (event.equalsIgnoreCase("32138_09.htm") && (_state == STARTED) && (cond == 14))
		{
			st.setCond(15);
		}
		else if (event.equalsIgnoreCase("32237_11.htm") && (_state == STARTED) && (cond == 16))
		{
			st.setCond(17);
		}
		else if (event.equalsIgnoreCase("32239_12.htm") && (_state == STARTED) && (cond == 17))
		{
			st.setCond(18);
		}
		else if (event.equalsIgnoreCase("32237_13.htm") && (_state == STARTED) && (cond == 18))
		{
			st.setCond(19);
		}
		else if (event.equalsIgnoreCase("32239_14.htm") && (_state == STARTED) && (cond == 19))
		{
			st.setCond(20);
		}
		else if (event.equalsIgnoreCase("31522_03b.htm") && (_state == STARTED) && (st.getQuestItemsCount(BLACK_ECHO_CRYSTAL) > 0))
		{
			st.takeItems(BLACK_ECHO_CRYSTAL, -1);
			st.set("echo", "1");
		}
		else if (event.equalsIgnoreCase("31522-ready") && (_state == STARTED) && ((cond == 3) || (cond == 4)) && (st.getInt("echo") == 1))
		{
			if (cond == 3)
			{
				st.setCond(4);
			}
			if (!KATENAR_SPAWNED)
			{
				st.addSpawn(KATENAR, 120000);
				ThreadPoolManager.getInstance().schedule(new OnDespawn(true), 120000);
				KATENAR_SPAWNED = true;
			}
			return null;
		}
		else if (event.equalsIgnoreCase("32238-harkil") && (_state == STARTED) && ((cond == 5) || (cond == 13)))
		{
			if (!HARKILGAMED_SPAWNED)
			{
				st.addSpawn(HARKILGAMED, 120000);
				ThreadPoolManager.getInstance().schedule(new OnDespawn(false), 120000);
				HARKILGAMED_SPAWNED = true;
			}
			return null;
		}
		else if (event.equalsIgnoreCase("32236-hunt") && (_state == STARTED) && (cond == 5))
		{
			st.setCond(12);
			return "32236_06.htm";
		}
		else if (event.equalsIgnoreCase("32333_02.htm") && (_state == STARTED) && (cond == 4))
		{
			st.setCond(5);
			st.unset("echo");
		}
		else if (event.equalsIgnoreCase("32190_02.htm") && (_state == STARTED) && ((cond == 15) || (cond == 16)))
		{
			if (cond == 15)
			{
				st.setCond(16);
			}
			st.getPlayer().teleToLocation(-119534, 87176, -12593);
		}
		else if (event.equalsIgnoreCase("32237_15.htm") && (_state == STARTED) && (cond == 20))
		{
			st.giveItems(SCROLL_ENCHANT_WEAPON_A, 1, true);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int _state = st.getState();
		int npcId = npc.getNpcId();
		if (_state == COMPLETED)
		{
			return "completed";
		}
		if (_state == CREATED)
		{
			if (npcId != KEKROPUS)
			{
				return "noquest";
			}
			if (st.getPlayer().getRace() != Race.kamael)
			{
				st.exitCurrentQuest(true);
				return "32138_00.htm";
			}
			if (st.getPlayer().getLevel() < 75)
			{
				st.exitCurrentQuest(true);
				return "32138_01.htm";
			}
			if (st.getQuestItemsCount(STAR_OF_DESTINY) > 0)
			{
				st.takeItems(STAR_OF_DESTINY, -1);
				st.setCond(0);
				return "32138_02.htm";
			}
			if ((st.getPlayer().getQuestState(_234_FatesWhisper.class) != null) && st.getPlayer().getQuestState(_234_FatesWhisper.class).isCompleted())
			{
				st.setCond(0);
				return "32138_02.htm";
			}
			st.exitCurrentQuest(true);
			return "32138_01a.htm";
		}
		if (_state != STARTED)
		{
			return "noquest";
		}
		int cond = st.getCond();
		if (npcId == KEKROPUS)
		{
			return cond < 14 ? "32138_02c.htm" : cond == 14 ? "32138_08.htm" : "32138_10.htm";
		}
		if (npcId == KATENAR)
		{
			return cond < 4 ? "noquest" : cond == 4 ? "32333_01.htm" : "32333_02.htm";
		}
		if (npcId == ROCK)
		{
			return (cond == 5) || (cond == 13) ? "32238-01.htm" : "32238-00.htm";
		}
		if (npcId == MAO)
		{
			return cond >= 15 ? "32190_01.htm" : "noquest";
		}
		if (npcId == WIZARD)
		{
			if (cond == 1)
			{
				return "31522_01.htm";
			}
			if (cond == 2)
			{
				return "31522_02a.htm";
			}
			if (cond == 3)
			{
				if (st.getQuestItemsCount(BLACK_ECHO_CRYSTAL) == 0)
				{
					st.setCond(2);
					return "31522_02a.htm";
				}
				return "31522_03.htm";
			}
			if ((cond == 4) && (st.getInt("echo") == 1) && !KATENAR_SPAWNED)
			{
				return "31522_03c.htm";
			}
			return "31522_04.htm";
		}
		if (npcId == HARKILGAMED)
		{
			if (cond == 5)
			{
				return "32236_05.htm";
			}
			if (cond == 12)
			{
				return "32236_06.htm";
			}
			if (cond == 13)
			{
				if (st.getQuestItemsCount(SHINING_MEDALLION) < 62)
				{
					st.setCond(12);
					return "32236_06.htm";
				}
				st.takeItems(SHINING_MEDALLION, -1);
				return "32236_07.htm";
			}
			if (cond > 13)
			{
				return "32236_09.htm";
			}
			return "noquest";
		}
		if (npcId == RODENPICULA)
		{
			if (cond == 16)
			{
				return "32237_10.htm";
			}
			if (cond == 17)
			{
				return "32237_11.htm";
			}
			if (cond == 18)
			{
				return "32237_12.htm";
			}
			if (cond == 19)
			{
				return "32237_13.htm";
			}
			if (cond == 20)
			{
				return "32237_14.htm";
			}
		}
		if (npcId == NORNIL)
		{
			if (cond == 17)
			{
				return "32239_11.htm";
			}
			if (cond == 18)
			{
				return "32239_12.htm";
			}
			if (cond == 19)
			{
				return "32239_13.htm";
			}
			if (cond == 20)
			{
				return "32239_14.htm";
			}
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		int npcId = npc.getNpcId();
		int cond = qs.getCond();
		if (IsInIntArray(npcId, NEEDLE_STAKATO_DRONES))
		{
			if ((cond == 2) && (qs.getQuestItemsCount(BLACK_ECHO_CRYSTAL) == 0) && Rnd.chance(BLACK_ECHO_CRYSTAL_CHANCE))
			{
				qs.giveItems(BLACK_ECHO_CRYSTAL, 1);
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
			}
		}
		else if (IsInIntArray(npcId, SPLENDOR_MOBS))
		{
			if ((cond == 12) && (qs.getQuestItemsCount(SHINING_MEDALLION) < 62) && Rnd.chance(SHINING_MEDALLION_CHANCE))
			{
				qs.giveItems(SHINING_MEDALLION, 1);
				if (qs.getQuestItemsCount(SHINING_MEDALLION) < 62)
				{
					qs.playSound(SOUND_ITEMGET);
				}
				else
				{
					qs.setCond(13);
					qs.playSound(SOUND_MIDDLE);
				}
			}
		}
		return null;
	}
	
	private static boolean IsInIntArray(int i, int[] a)
	{
		for (int _i : a)
		{
			if (_i == i)
			{
				return true;
			}
		}
		return false;
	}
	
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
	
	public static class OnDespawn extends RunnableImpl
	{
		private final boolean _SUBJ_KATENAR;
		
		public OnDespawn(boolean SUBJ_KATENAR)
		{
			_SUBJ_KATENAR = SUBJ_KATENAR;
		}
		
		@Override
		public void runImpl()
		{
			if (_SUBJ_KATENAR)
			{
				KATENAR_SPAWNED = false;
			}
			else
			{
				HARKILGAMED_SPAWNED = false;
			}
		}
	}
}
