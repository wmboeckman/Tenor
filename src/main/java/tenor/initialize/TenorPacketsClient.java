package tenor.initialize;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tenor.block.WireNodeBlock;
import tenor.block.entity.WireNodeBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class TenorPacketsClient {
	public static void initialize() {
		ClientSidePacketRegistry.INSTANCE.register(TenorPacketsCommon.CONNECTOR_REMOVAL_PACKET, ((context, bytes) -> {
			World world = context.getPlayer().getEntityWorld();
			BlockPos connectorPosition = bytes.readBlockPos();
			int size = bytes.readInt();
			List<BlockPos> positions = new ArrayList<>();
			for (int i = 0; i < size; ++i) {
				positions.add(bytes.readBlockPos());
			}

			context.getTaskQueue().execute(() -> {
				WireNodeBlock.onConnectorBroken(connectorPosition, positions, world);

				for (BlockPos position : positions) {
					BlockEntity blockEntity = world.getBlockEntity(position);

					if (blockEntity != null) {
						WireNodeBlockEntity be = (WireNodeBlockEntity) blockEntity;

						be.sync();
					}
				}
			});
		}));
	}
}
