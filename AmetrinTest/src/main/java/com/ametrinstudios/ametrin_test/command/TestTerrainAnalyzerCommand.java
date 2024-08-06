package com.ametrinstudios.ametrin_test.command;

import com.ametrinstudios.ametrin.world.gen.util.TerrainAnalyzer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class TestTerrainAnalyzerCommand {

    public TestTerrainAnalyzerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("ametrin_test")
                .then(Commands.literal("isFlatEnough")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(command -> lol(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"), command.getSource().getLevel()))
                                )));
    }

    private int lol(CommandSourceStack source, BlockPos pos, ServerLevel level){
        source.sendSuccess(()-> Component.literal(String.valueOf(TerrainAnalyzer.isFlatEnough(pos, new Vec3i(8, 1, 8), 1, 2, new TerrainAnalyzer.Context(level.getChunkSource().getGenerator(), level, level.getChunkSource().randomState())).getSecond())), true);
        return 1;
    }
}