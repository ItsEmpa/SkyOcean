package codes.cookies.skyocean.mixins;

import codes.cookies.skyocean.api.event.ListenForNameChange;
import codes.cookies.skyocean.api.event.NameChangedEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI;

import java.util.Optional;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleSetEntityData", at = @At(value = "RETURN", target = "Lnet/minecraft/world/entity/Entity;getEntityData()Lnet/minecraft/network/syncher/SynchedEntityData;"))
    public void handleSetEntityDate(ClientboundSetEntityDataPacket packet, CallbackInfo ci, @Local Entity entity) {
        if (entity == null) {
            return;
        }
        if (((ListenForNameChange) entity).ocean$isNameTag()) {
            for (SynchedEntityData.DataValue<?> packedItem : packet.packedItems()) {
                if (packedItem.id() == Entity.DATA_CUSTOM_NAME.id()) {
                    final Optional<Component> value;
                    try {
                        value = (Optional<Component>) packedItem.value();
                    } catch (ClassCastException e) {
                        return;
                    }
                    value.ifPresent(component -> new NameChangedEvent(
                            entity,
                            component).post(SkyBlockAPI.getEventBus()));
                }
            }
        }
    }
}
