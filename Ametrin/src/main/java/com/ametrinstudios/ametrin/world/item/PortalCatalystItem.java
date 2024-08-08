package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.world.dimension.portal.PortalData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class PortalCatalystItem extends Item{
    protected final PortalData portalData;
    public PortalCatalystItem(PortalData portalData, Properties properties) {
        super(properties);
        this.portalData = portalData;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        var level = context.getLevel();

        if (!portalData.isValidDimension(level) || player == null) {
            return InteractionResult.FAIL;
        }

        var portalShape = portalData.findPortalShape(context.getLevel(), context.getClickedPos().above(), Direction.Axis.X);
        if (portalShape.isEmpty()) {
            return InteractionResult.FAIL;
        }

        portalShape.get().createPortalBlocks();
        level.playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 0.9f, 1);

        if(player instanceof ServerPlayer serverPlayer) {
            var itemStack = context.getItemInHand();
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, context.getClickedPos(), itemStack);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
