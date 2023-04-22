package com.ametrinstudios.ametrin.world.gen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;

public class StructurePieces {
    private final SimpleWeightedRandomList<Piece> pieces;

    protected StructurePieces(final Builder builder) {this(builder.buildList());}
    protected StructurePieces(final SimpleWeightedRandomList<Piece> pieces){
        if(pieces.isEmpty()) {
            throw new IllegalArgumentException("The Structure Pieces builder is empty");
        }
        this.pieces = pieces;
    }

    public Piece getRandomPiece(RandomSource random) {return pieces.getRandomValue(random).get();}

    public static class Piece{
        public final ResourceLocation Resource;
        public final BlockPos Offset;
        public Piece(final ResourceLocation resource, final BlockPos offset){
            Resource = resource;
            Offset = offset;
        }
    }

    public static class Builder{
        private final SimpleWeightedRandomList.Builder<Piece> pieces;
        private BlockPos offset;
        private int weight;

        public Builder(){
            this.offset = BlockPos.ZERO;
            this.weight = 1;
            pieces = SimpleWeightedRandomList.builder();
        }

        public Builder weight(int weight){
            this.weight = weight;
            return this;
        }
        public Builder offset(BlockPos offset){
            this.offset = offset;
            return this;
        }

        public Builder offset(int x, int y, int z) {return offset(new BlockPos(x, y, z));}
        public Builder offsetY(int y) {return offset(0, y, 0);}

        public Builder add(ResourceLocation resource){
            pieces.add(new Piece(resource, offset), weight);
            return this;
        }

        private SimpleWeightedRandomList<Piece> buildList() {return pieces.build();}

        public StructurePieces build() {return new StructurePieces(this);}
    }
}