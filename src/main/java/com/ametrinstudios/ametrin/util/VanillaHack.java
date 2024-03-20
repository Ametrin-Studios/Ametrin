package com.ametrinstudios.ametrin.util;

import com.mojang.logging.LogUtils;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public final class VanillaHack {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void overrideFoodProperties(Item item, FoodProperties foodProperties){
        try{
            overrideField(item, "foodProperties", foodProperties);
        }catch (Exception e){
            LOGGER.warn("Failed overriding food properties: " + e.getMessage());
        }
    }

    public static void overrideField(Object obj, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        var field = getField(obj.getClass(), fieldName);
        field.setAccessible(true);
        field.set(obj, newValue);
        field.setAccessible(false);
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
}
