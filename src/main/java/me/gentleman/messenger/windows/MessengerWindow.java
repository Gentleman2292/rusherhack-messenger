/*
 * Copyright (c) 2023 Rusher Development LLC. All Rights Reserved.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited, proprietary, and confidential.
 */

package me.gentleman.messenger.windows;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.rusherhack.client.api.Globals;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.network.EventPacket;
import org.rusherhack.client.api.ui.window.ResizeableWindow;
import org.rusherhack.client.api.ui.window.content.ComboContent;
import org.rusherhack.client.api.ui.window.content.component.ButtonComponent;
import org.rusherhack.client.api.ui.window.content.component.TextFieldComponent;
import org.rusherhack.client.api.ui.window.view.RichTextView;
import org.rusherhack.client.api.ui.window.view.TabbedView;
import org.rusherhack.client.api.ui.window.view.WindowView;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.awt.*;
import java.util.List;

/**
 * @author John200410 11/22/2023
 */
public class MessengerWindow extends ResizeableWindow {

    public static MessengerWindow INSTANCE;

    private final TabbedView rootView;

    private final RichTextView messageView;

    public MessengerWindow() {
        super("iMessage china edition", 150, 100, 250, 250);
        RusherHackAPI.getEventBus().subscribe(this);
        INSTANCE = this;

        this.setMinWidth(100);
        this.setMinHeight(100);

        this.messageView = new RichTextView("Messages", this);

        final ComboContent inputCombo = new ComboContent(this);

        final TextFieldComponent rawMessage = new TextFieldComponent(this, "enter message", 100);
        final ButtonComponent sendButton = new ButtonComponent(this, "send", () -> {
            final String input = rawMessage.getValue();

            if (input.isEmpty() || Globals.mc.player == null) {
                return;
            }

            Globals.mc.player.connection.sendChat(input);
            this.messageView.add(Component.literal("> " + input), Color.WHITE.getRGB());

            rawMessage.setValue("");
        });
        rawMessage.setReturnCallback((str) -> sendButton.onClick());
        inputCombo.addContent(rawMessage, ComboContent.AnchorSide.LEFT);
        inputCombo.addContent(sendButton, ComboContent.AnchorSide.RIGHT);

        this.rootView = new TabbedView(this, List.of(this.messageView, inputCombo));
    }

    @Subscribe
    public void onPacketReceive(EventPacket.Receive event) {
        //this works
        ChatUtils.print("event triggered");
        if (Globals.mc.player == null || Globals.mc.level == null) {
            return;
        }

        if (event.getPacket() instanceof ServerboundChatPacket chatPacket) {
            //this doesn't work why !!?????
            ChatUtils.print("event2 triggered");
            String minecraftChatMessage = chatPacket.message();

            this.messageView.add(Component.literal("> " + minecraftChatMessage), Color.lightGray.getRGB());
        }
    }

    @Override
    public WindowView getRootView() {
        return this.rootView;
    }

    public RichTextView getMessageView() {
        return this.messageView;
    }
}
