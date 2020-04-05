package tenor.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.particle.ParticleTypes;
import org.lwjgl.opengl.GL11;
import tenor.block.WireNodeBlock;
import tenor.block.entity.WireNodeBlockEntity;
import tenor.initialize.TenorEnergies;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class WireNodeBlockEntityRenderer extends BlockEntityRenderer<WireNodeBlockEntity> {
	public WireNodeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(WireNodeBlockEntity be, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		for (WireNodeBlockEntity ch : be.children) {

			LinkedHashSet<Vector3f> segments = WireNodeBlockEntity.getSegments(be, ch);

			Vector3f origin = segments.iterator().next();
			Vector3f previous = origin;

			matrixStack.push();

			VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());

			Integer[] RGBA = WireNodeBlock.COLORS.get(be.tier);

			for (Vector3f vector : segments) {
				if (vector != origin) {
					float xA = vector.getX() - be.getPos().getX();
					float xB = previous.getX() - be.getPos().getX();

					float yA = vector.getY() - be.getPos().getY();
					float yB = previous.getY() - be.getPos().getY();

					float zA = vector.getZ() - be.getPos().getZ();
					float zB = previous.getZ() - be.getPos().getZ();

					consumer.vertex(matrixStack.peek().getModel(), xA, yA, zA).color(RGBA[0], RGBA[1], RGBA[2], RGBA[3]).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(255).normal(matrixStack.peek().getNormal(), 0, 1, 0).next();
					consumer.vertex(matrixStack.peek().getModel(), xB, yB, zB).color(RGBA[0], RGBA[1], RGBA[2], RGBA[3]).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(255).normal(matrixStack.peek().getNormal(), 0, 1, 0).next();
				}
				previous = vector;
			}

			matrixStack.pop();
		}
	}
}
