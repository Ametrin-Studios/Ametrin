package com.ametrinstudios.ametrin.world.item;

import com.ametrinstudios.ametrin.world.block.PortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public class PortalCatalystItem extends Item{
    protected final Supplier<? extends PortalBlock> portalBlock;
    protected final ResourceKey<Level> targetLevel;
    public PortalCatalystItem(Supplier<? extends PortalBlock> portalBlock, ResourceKey<Level> targetLevel, Properties properties) {
        super(properties);
        this.portalBlock = portalBlock;
        this.targetLevel = targetLevel;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() != null) {
            if(context.getPlayer().level().dimension() == targetLevel || context.getPlayer().level().dimension() == Level.OVERWORLD){
                for(Direction direction : Direction.Plane.VERTICAL){
                    BlockPos framePos = context.getClickedPos().relative(direction);
                    if(portalBlock.get().trySpawnPortal(context.getLevel(), framePos)) {
                        context.getLevel().playSound(context.getPlayer(), framePos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1, 1);
                        return InteractionResult.CONSUME;
                    } else {
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> componentList, TooltipFlag isAdvanced) {
        componentList.add(Component.translatable(getDescriptionId(itemStack)+".desc"));
    }
}