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
package lineage2.gameserver.network.clientpackets;

import java.nio.BufferUnderflowException;
import java.util.List;

import lineage2.commons.net.nio.impl.ReceivablePacket;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class L2GameClientPacket extends ReceivablePacket<GameClient>
{
	private static final Logger _log = LoggerFactory.getLogger(L2GameClientPacket.class);
	
	@Override
	public final boolean read()
	{
		try
		{
			readImpl();
			return true;
		}
		catch (BufferUnderflowException e)
		{
			_client.onPacketReadFail();
			_log.error("Client: " + _client + " - Failed reading: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		catch (Exception e)
		{
			_log.error("Client: " + _client + " - Failed reading: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		return false;
	}
	
	protected abstract void readImpl() throws Exception;
	
	@Override
	public final void run()
	{
		GameClient client = getClient();
		try
		{
			runImpl();
		}
		catch (Exception e)
		{
			_log.error("Client: " + client + " - Failed running: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
	}
	
	protected abstract void runImpl() throws Exception;
	
	protected String readS(int len)
	{
		String ret = readS();
		return ret.length() > len ? ret.substring(0, len) : ret;
	}
	
	protected void sendPacket(L2GameServerPacket packet)
	{
		getClient().sendPacket(packet);
	}
	
	protected void sendPacket(L2GameServerPacket... packets)
	{
		getClient().sendPacket(packets);
	}
	
	protected void sendPackets(List<L2GameServerPacket> packets)
	{
		getClient().sendPackets(packets);
	}
	
	public String getType()
	{
		return "[C] " + getClass().getSimpleName();
	}
}
