package com.ametrinstudios.ametrin.world.gen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class StructurePieces {

    private final Piece[] pieces;
    private final int maxWeight;

    public StructurePieces(Builder builder){
        this(builder.buildArray());
    }
    public StructurePieces(Piece[] pieces){
        this.pieces = pieces;
        maxWeight = getMaxWeight();
    }

    public Piece getRandomPiece(RandomSource rand){
        int piece = 0;
        if(pieces.length > 1) {
            int i = rand.nextInt(maxWeight);
            for (int j = 0; j < pieces.length; j++) {
                if (pieces[j].Weight >= i) {
                    piece = j;
                    break;
                } else {
                    i -= pieces[j].Weight;
                }
            }
        }
        return pieces[piece];
    }

    public int getMaxWeight(){
        int i = 0;
        for (Piece piece : pieces){
            i += piece.Weight;
        }
        return i;
    }

    public static class Piece{
        public final ResourceLocation Resource;
        public final BlockPos Offset;
        public final int Weight;
        public Piece(ResourceLocation resource, BlockPos offset, int weight){
            Resource = resource;
            Offset = offset;
            Weight = weight;
        }
    }

    public static class Builder{
        private final List<Piece> pieces;
        private BlockPos offset;
        private int weight;

        public Builder(){
            this.offset = BlockPos.ZERO;
            this.weight = 1;
            pieces = new ArrayList<>();
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

        public Builder add(ResourceLocation resource){
            pieces.add(new Piece(resource, offset, weight));
            return this;
        }

        private Piece[] buildArray() {return pieces.toArray(Piece[]::new);}

        public StructurePieces build() {return new StructurePieces(this);}
    }
}