package net.blay09.mods.waystones.network.handler;

import net.blay09.mods.waystones.network.message.MessageConfig;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerConfig implements IMessageHandler<MessageConfig, IMessage>
{
    @Override
    @Nullable
    public IMessage onMessage(final MessageConfig message, MessageContext ctx)
    {
        return null;
    }
}
