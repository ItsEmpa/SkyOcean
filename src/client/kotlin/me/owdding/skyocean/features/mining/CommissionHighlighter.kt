package me.owdding.skyocean.features.mining

import me.owdding.ktmodules.Module
import me.owdding.skyocean.config.features.mining.MiningConfig
import me.owdding.skyocean.utils.ChatUtils
import me.owdding.skyocean.utils.Utils.contains
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.screen.InventoryChangeEvent
import tech.thatgravyboat.skyblockapi.api.item.replaceVisually
import tech.thatgravyboat.skyblockapi.utils.extentions.getLore
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@Module
object CommissionHighlighter {

    @Subscription
    fun onInventoryChange(event: InventoryChangeEvent) {
        if (!MiningConfig.modifyCommissions) return
        if (event.title != "Commissions") return
        if (event.isInPlayerInventory) return
        if (event.item !in Items.WRITABLE_BOOK) return

        val lore = event.item.getLore()
        val stripped = lore.last().stripped.trim()
        event.item.replaceVisually {
            copyFrom(event.item)
            namePrefix(ChatUtils.ICON_SPACE_COMPONENT)
            item = when (stripped) {
                "Click to claim rewards!" -> Items.KNOWLEDGE_BOOK
                "0%" -> Items.WRITTEN_BOOK
                else -> Items.WRITABLE_BOOK
            }

            set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false)
        }
    }

}
