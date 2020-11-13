package coffeecatrailway.soulpowered.common.command;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.capability.ISoulsHandler;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;
import java.util.Collections;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulsCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("souls").requires(source -> source.hasPermissionLevel(4));

        builder.then(Commands.literal("modify").then(Commands.literal("set").then(Commands.argument("souls", IntegerArgumentType.integer(0))
                .executes(source -> setValue(source, Collections.singleton(source.getSource().getEntity()), IntegerArgumentType.getInteger(source, "souls")))
                .then(Commands.argument("target", EntityArgument.entities())
                        .executes((source) -> setValue(source, EntityArgument.getEntities(source, "target"), IntegerArgumentType.getInteger(source, "souls"))))
        )).then(Commands.literal("add").then(Commands.argument("souls", IntegerArgumentType.integer(0))
                .executes(source -> changeValue(source, Collections.singleton(source.getSource().getEntity()), IntegerArgumentType.getInteger(source, "souls"), false))
                .then(Commands.argument("target", EntityArgument.entities())
                        .executes((source) -> changeValue(source, EntityArgument.getEntities(source, "target"), IntegerArgumentType.getInteger(source, "souls"), false)))
        )).then(Commands.literal("remove").then(Commands.argument("souls", IntegerArgumentType.integer(0))
                .executes(source -> changeValue(source, Collections.singleton(source.getSource().getEntity()), IntegerArgumentType.getInteger(source, "souls"), true))
                .then(Commands.argument("target", EntityArgument.entities())
                        .executes((source) -> changeValue(source, EntityArgument.getEntities(source, "target"), IntegerArgumentType.getInteger(source, "souls"), true)))
        ))).then(Commands.literal("get").executes(source -> getValue(source, source.getSource().getEntity()))
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes((source) -> getValue(source, EntityArgument.getEntity(source, "target")))));

        dispatcher.register(builder);
    }

    private static int getValue(CommandContext<CommandSource> source, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            entity.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler ->
                    source.getSource().sendFeedback(new TranslationTextComponent("commands." + SoulPoweredMod.MOD_ID + ".souls.get", entity.getDisplayName(), handler.getSouls()), true));
            return 1;
        }
        return 0;
    }

    private static int setValue(CommandContext<CommandSource> source, Collection<? extends Entity> entities, int souls)
    {
        boolean canContinue = entities.stream().anyMatch(entity -> entity instanceof LivingEntity);
        if (canContinue)
        {
            int i = 0;
            for (Entity entity : entities)
            {
                LazyOptional<ISoulsHandler> cap = entity.getCapability(SoulsCapability.SOULS_CAP);
                if (cap.isPresent())
                {
                    cap.orElseThrow(NullPointerException::new).setSouls(souls);
                    source.getSource().sendFeedback(new TranslationTextComponent("commands." + SoulPoweredMod.MOD_ID + ".souls.modify.set", entity.getDisplayName(), souls), true);
                    i++;
                }
            }
            return i;
        }
        return 0;
    }

    private static int changeValue(CommandContext<CommandSource> source, Collection<? extends Entity> entities, int souls, boolean remove)
    {
        boolean canContinue = entities.stream().anyMatch(entity -> entity instanceof LivingEntity);
        if (canContinue)
        {
            int i = 0;
            for (Entity entity : entities)
            {
                LazyOptional<ISoulsHandler> cap = entity.getCapability(SoulsCapability.SOULS_CAP);
                if (cap.isPresent())
                {
                    if (remove)
                    {
                        cap.orElseThrow(NullPointerException::new).removeSouls(souls, true);
                        source.getSource().sendFeedback(new TranslationTextComponent("commands." + SoulPoweredMod.MOD_ID + ".souls.modify.remove", entity.getDisplayName(), souls), true);
                    } else
                    {
                        cap.orElseThrow(NullPointerException::new).addSouls(souls, true);
                        source.getSource().sendFeedback(new TranslationTextComponent("commands." + SoulPoweredMod.MOD_ID + ".souls.modify.add", entity.getDisplayName(), souls), true);
                    }
                    i++;
                }
            }
            return i;
        }
        return 0;
    }
}
