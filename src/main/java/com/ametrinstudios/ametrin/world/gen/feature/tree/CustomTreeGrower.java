package com.ametrinstudios.ametrin.world.gen.feature.tree;

/**
 * This is an alternative way for tree grower, if possible use the vanilla!
 */
//public class CustomTreeGrower extends AbstractTreeGrower {
//    protected final Supplier<? extends CustomTreeFeature> Tree;
//
//    public CustomTreeGrower(Supplier<? extends CustomTreeFeature> tree) {Tree = tree;}
//
//    @Override
//    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {return null;}
//
//    @Override @ParametersAreNonnullByDefault
//    public boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockState, RandomSource random) {
//        final Set<BlockPos> foliage = Sets.newHashSet();
//
//        BiConsumer<BlockPos, BlockState> placedLogs = (blockPos, state) -> TreeFeature.setBlockKnownShape(level, blockPos, state);
//        BiConsumer<BlockPos, BlockState> placedLeaves = (blockPos, state) -> TreeFeature.setBlockKnownShape(level, blockPos, state);
//
//        var foliageSetter = new FoliagePlacer.FoliageSetter() {
//            public void set(BlockPos blockPos, BlockState state) {
//                foliage.add(blockPos.immutable());
//                TreeFeature.setBlockKnownShape(level, blockPos, state);
//            }
//            public boolean isSet(BlockPos blockPos) {
//                return foliage.contains(blockPos);
//            }
//        };
//
//        return Tree.get().place(new TreePlaceContext(pos, level, random, placedLogs, placedLeaves, foliageSetter));
//    }
//}
