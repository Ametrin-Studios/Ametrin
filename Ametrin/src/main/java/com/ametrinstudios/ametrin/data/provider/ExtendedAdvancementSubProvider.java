package com.ametrinstudios.ametrin.data.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class ExtendedAdvancementSubProvider extends AdvancementSubProvider {
    protected String modId;

    protected ExtendedAdvancementSubProvider(BootstrapContext<Advancement> output, String modId) {
        super(output);
        this.modId = modId;
    }

    @Override
    public abstract void generate();

    protected AdvancementBuilder builder(HolderLookup.Provider registries, String key) {
        return new AdvancementBuilder(registries, modId, key);
    }

    public static class AdvancementBuilder {
        private final String namespace;
        private final String name;
        private final HolderLookup.Provider registries;
        @Nullable
        private ItemStackTemplate displayItem;
        @Nullable
        private AdvancementHolder parent = null;
        @Nullable
        private Identifier background = null;
        private AdvancementType type = AdvancementType.TASK;
        private boolean showToast = true;
        private boolean announceToChat = true;
        private boolean hidden = false;
        private AdvancementRequirements.Strategy criterionStrategy = AdvancementRequirements.Strategy.AND;
        private final List<Pair<String, Criterion<?>>> criteria = new ArrayList<>();

        public AdvancementBuilder(HolderLookup.Provider registries, String namespace, String name) {
            this.registries = registries;
            this.namespace = namespace;
            this.name = name;
        }

        public AdvancementBuilder parent(AdvancementHolder parent) {
            this.parent = parent;
            return this;
        }

        public AdvancementBuilder displayItem(ItemLike item) {
            return displayItem(new ItemStackTemplate(item.asItem()));
        }

        public AdvancementBuilder displayItem(ItemStackTemplate template) {
            this.displayItem = template;
            return this;
        }

        public AdvancementBuilder background(String background) {
            return background(Identifier.fromNamespaceAndPath(namespace, background));
        }

        public AdvancementBuilder background(Identifier background) {
            this.background = background;
            return this;
        }

        public AdvancementBuilder type(AdvancementType type) {
            this.type = type;
            return this;
        }

        public AdvancementBuilder hideCompletely() {
            return hideToast().hideInChat().hide();
        }

        public AdvancementBuilder hideToast() {
            return showToast(false);
        }

        public AdvancementBuilder showToast(boolean show) {
            showToast = show;
            return this;
        }

        public AdvancementBuilder hideInChat() {
            return announceToChat(false);
        }

        public AdvancementBuilder announceToChat(boolean announce) {
            announceToChat = announce;
            return this;
        }

        public AdvancementBuilder hide() {
            return hidden(true);
        }

        public AdvancementBuilder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public AdvancementBuilder orCriteria() {
            return criterionStrategy(AdvancementRequirements.Strategy.OR);
        }

        public AdvancementBuilder criterionStrategy(AdvancementRequirements.Strategy strategy) {
            criterionStrategy = strategy;
            return this;
        }

        /// add one criterion containing all structures (entering one of them satisfy the criteria, independent of [#criterionStrategy(net.minecraft.advancements.AdvancementRequirements.Strategy)]
        public AdvancementBuilder onEnterAnyStructure(String criterionName, Stream<ResourceKey<Structure>> structures) {
            var lookup = registries.lookupOrThrow(Registries.STRUCTURE);
            var holders = structures.map(lookup::getOrThrow);
            return addCriterion(criterionName, PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructures(HolderSet.direct(holders.toList()))));
        }

        /// add separate criteria for each structure
        public AdvancementBuilder onEnterAllStructures(Stream<ResourceKey<Structure>> structures) {
            var lookup = registries.lookupOrThrow(Registries.STRUCTURE);
            structures.map(lookup::getOrThrow).forEach(this::onEnterStructure);
            return this;
        }

        public AdvancementBuilder onEnterStructure(TagKey<Structure> structure) {
            var lookup = registries.lookupOrThrow(Registries.STRUCTURE);
            var structures = lookup.getOrThrow(structure);
            return addCriterion("entered_" + structure.location().getPath().replace('/', '_'), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructures(structures)));
        }

        public AdvancementBuilder onEnterStructure(ResourceKey<Structure> structure) {
            var lookup = registries.lookupOrThrow(Registries.STRUCTURE);
            return onEnterStructure(lookup.getOrThrow(structure));
        }

        public AdvancementBuilder onEnterStructure(Holder<Structure> structure) {
            return addCriterion("entered_" + Objects.requireNonNull(structure.key()).identifier().getPath().replace('/', '_'), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structure)));
        }

        public AdvancementBuilder addCriterion(String name, Criterion<?> criterion) {
            criteria.add(Pair.of(name, criterion));
            return this;
        }

        public AdvancementHolder save(BootstrapContext<Advancement> output) {
            if (displayItem == null) throw new IllegalStateException(namespace + ":" + name + " has no display item");
            var title = component(name + ".title");
            var description = component(name + ".description");
            var builder = background == null ?
                    new Advancement.Builder().display(displayItem, title, description, type, showToast, announceToChat, hidden).requirements(criterionStrategy)
                    : new Advancement.Builder().rootDisplay(displayItem, title, description, background, type, showToast, announceToChat, hidden).requirements(criterionStrategy);
            for (var pair : criteria) {
                builder.addCriterion(pair.getFirst(), pair.getSecond());
            }
            if (parent != null) builder.parent(parent);
            return builder.save(output, Identifier.fromNamespaceAndPath(namespace, name));
        }

        private Component component(String key) {
            return Component.translatable("advancements." + namespace + "." + key);
        }
    }
}
