package com.hakobu.suncompassmod;
import com.hakobu.suncompassmod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = "suncompassmod", value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.sun_compass.get(),
                    ResourceLocation.fromNamespaceAndPath("suncompassmod", "sun_shadow"),
                    (stack, level, entity, seed) -> {
                        Entity holder = entity != null ? entity : stack.getEntityRepresentation();

                        // Safety check
                        if (holder == null) return 0.0f;
                        // If the item is in an Item Frame, use the frame's rotation, otherwise use the player's
                        if (level == null) {
                            level = (ClientLevel) holder.level();
                        }

                        // 2. Calculate Sun Position (0.0 to 1.0)
                        // getSunAngle returns Radians (0 is Noon/South)
                        float sunRadians = level.getSunAngle(1.0f);

                        // Convert to normalized 0.0 - 1.0 range
                        // In MC: Noon(0) -> Sunset(0.25) -> Midnight(0.5) -> Sunrise(0.75)
                        double sunTime01 = Mth.positiveModulo(sunRadians / (Math.PI * 2.0), 1.0);

                        // 3. Calculate Shadow Direction (World Relative)
                        // The Shadow is OPPOSITE to the Sun.
                        // If Sun is 0.0 (South), Shadow should be 0.5 (North).
                        double shadowWorldAngle = Mth.positiveModulo(sunTime01 + 0.5, 1.0);

                        // 4. Calculate Player Rotation
                        // We need the player's Yaw (Head rotation) normalized to 0.0 - 1.0
                        // MC Yaw: South=0, West=90, North=180, East=270
                        double playerYaw = Mth.positiveModulo(holder.getYRot() / 360.0, 1.0);

                        // IF in an Item Frame, use the frame's facing direction instead
                        if (holder instanceof ItemFrame frame) {
                            Direction direction = frame.getDirection();
                            int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                            playerYaw = Mth.positiveModulo((direction.toYRot() + i) / 360.0, 1.0);
                        }

                        // 5. Final Calculation: Shadow - Player
                        // This creates the "counter-rotation" effect.
                        double angle = Mth.positiveModulo(shadowWorldAngle - playerYaw, 1.0);

                        return (float) angle;
                    }
            );
        });
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {
                    // LAYER CHECK:
                    // Layer 0 (Board) & Layer 2 (Stick) -> Return -1 (No Tint/Original Colors)
                    // Layer 1 (Shadow) -> Apply Tint
                    if (tintIndex != 1) return -1;

                    // Safety check for level
                    if (Minecraft.getInstance().level == null) return -1;

                    // 1. Get Time & Distance from Noon
                    float time = Minecraft.getInstance().level.getTimeOfDay(1.0f);

                    // Normalize time to "Distance from Noon" (0.0 = Noon, 0.5 = Midnight)
                    // 0.9 (Morning) becomes 0.1 dist.
                    // 0.1 (Afternoon) becomes 0.1 dist.
                    float distFromNoon = Math.abs(time > 0.5 ? time - 1.0f : time);

                    // 2. Define "Leniency" Thresholds
                    // Sunset is at 0.25.
                    // fadeStart: 0.20 means it stays FULL DARK until roughly 4:30 PM.
                    // fadeEnd: 0.27 means it stays VISIBLE until slightly after the sun vanishes (Twilight).
                    float fadeStart = 0.20f;
                    float fadeEnd = 0.27f;

                    int maxAlpha = 190; // Maximum darkness (0-255). 190 is a good "strong shadow."
                    int currentAlpha = 0;

                    if (distFromNoon <= fadeStart) {
                        // It is broad daylight. Full Shadow.
                        currentAlpha = maxAlpha;
                    } else if (distFromNoon < fadeEnd) {
                        // It is Sunset/Sunrise. Calculate how far we are into the fade window.
                        // 0.0 = Start of fade, 1.0 = End of fade
                        float progress = (distFromNoon - fadeStart) / (fadeEnd - fadeStart);

                        // Inverse lerp: 1.0 -> 0.0
                        currentAlpha = (int) ((1.0f - progress) * maxAlpha);
                    }
                    // else: It is Night (dist > fadeEnd). currentAlpha remains 0.

                    // 3. Return ARGB Color
                    // We pack the Alpha into the top 8 bits. Color is Black (0x000000).
                    return (currentAlpha << 24);
                },
                ModItems.sun_compass.get()
        );
    }
}