package me.owdding.skyocean.utils.boundingboxes

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShapeRenderer
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB
import org.apache.commons.lang3.mutable.MutableInt
import tech.thatgravyboat.skyblockapi.api.events.render.RenderWorldEvent
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.helpers.McPlayer

data class OctreeDebugRenderer(val octree: Octree) {

    fun render(event: RenderWorldEvent) {
        val camX = McClient.self.gameRenderer.mainCamera?.position?.x ?: 0.0
        val camY = McClient.self.gameRenderer.mainCamera?.position?.y ?: 0.0
        val camZ = McClient.self.gameRenderer.mainCamera?.position?.z ?: 0.0
        octree.boxes.forEach {
            val vertexConsumer: VertexConsumer = event.buffer.getBuffer(RenderType.lines())
            ShapeRenderer.renderLineBox(
                event.poseStack,
                vertexConsumer,
                AABB.of(it).move(-camX, -camY, -camZ),
                1.0f,
                1.0f,
                1.0f,
                1f,
            )
        }

        val nodesRendered = MutableInt()
        val playerNode = octree.findLeaf(McPlayer.self?.blockPosition() ?: BlockPos.ZERO)
        octree.visitNode { node, depth ->
            visit(event, node, nodesRendered, depth, playerNode)
        }
    }

    fun visit(event: RenderWorldEvent, node: Node, nodesRendered: MutableInt, depth: Int, playerNode: Leaf?) {
        val aABB: AABB = AABB.of(node.getBox())
        val size = aABB.xsize
        val color = Math.round(size / 16.0)
        val vertexConsumer: VertexConsumer = event.buffer.getBuffer(RenderType.lines())
        val colorValue = color + 5L
        val camX = McClient.self.gameRenderer.mainCamera?.position?.x ?: 0.0
        val camY = McClient.self.gameRenderer.mainCamera?.position?.y ?: 0.0
        val camZ = McClient.self.gameRenderer.mainCamera?.position?.z ?: 0.0
        ShapeRenderer.renderLineBox(
            event.poseStack,
            vertexConsumer,
            aABB.move(-camX, -camY, -camZ),
            getColorComponent(colorValue, 0.3f),
            getColorComponent(colorValue, 0.8f),
            getColorComponent(colorValue, 0.5f),
            if (node != playerNode) 0.4f else 1.0f,
        )
    }

    private fun getColorComponent(value: Long, multiplier: Float): Float {
        return Mth.frac((multiplier * value.toFloat())) * 0.9f + 0.1f
    }

}
