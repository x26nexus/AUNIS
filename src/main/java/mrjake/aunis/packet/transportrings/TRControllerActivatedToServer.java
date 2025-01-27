package mrjake.aunis.packet.transportrings;

import io.netty.buffer.ByteBuf;
import mrjake.aunis.Aunis;
import mrjake.aunis.packet.PositionedPacket;
import mrjake.aunis.sound.AunisSoundHelper;
import mrjake.aunis.sound.EnumAunisSoundEvent;
import mrjake.aunis.tileentity.TRControllerTile;
import mrjake.aunis.tileentity.TransportRingsTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TRControllerActivatedToServer extends PositionedPacket {
	public TRControllerActivatedToServer() {}
	
	public int address;
	
	public TRControllerActivatedToServer(BlockPos pos, int address) {
		super(pos);
		
		this.address = address;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		
		buf.writeInt(address);
	}

	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		
		address = buf.readInt();
	}

	
	public static class TRControllerActivatedServerHandler implements IMessageHandler<TRControllerActivatedToServer, IMessage> {

		@Override
		public IMessage onMessage(TRControllerActivatedToServer message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			WorldServer world = player.getServerWorld();
			
			world.addScheduledTask(() -> {
				TRControllerTile controllerTile = (TRControllerTile) world.getTileEntity(message.pos);
				TransportRingsTile ringsTile = controllerTile.getLinkedRingsTile(world);
				
				if (ringsTile != null) {
					AunisSoundHelper.playSoundEvent(world, message.pos, EnumAunisSoundEvent.RINGS_CONTROLLER_BUTTON, 0.5f);
					
					ringsTile.attemptTransportTo(player, message.address);
				}
				
				else
					player.sendStatusMessage(new TextComponentString(Aunis.proxy.localize("tile.aunis.transportrings_controller_block.not_linked")), true);
			});
			
			return null;
		}
		
	}
}
