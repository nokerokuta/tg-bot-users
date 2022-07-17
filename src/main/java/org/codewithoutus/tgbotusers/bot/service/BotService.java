package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.bot.enums.BotStatus;
import org.codewithoutus.tgbotusers.bot.handler.CallbackQueryHandler;
import org.codewithoutus.tgbotusers.bot.handler.ChatJoinRequestHandler;
import org.codewithoutus.tgbotusers.bot.handler.PrivateMessageHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class BotService {

    private final Bot bot;
    private final CallbackQueryHandler callbackQueryHandler;
    private final ChatJoinRequestHandler chatJoinRequestHandler;
    private final PrivateMessageHandler privateMessageHandler;

    @PostConstruct
    private void botStart() {
        start();
    }

    public boolean start() {
        if (bot.getStatus() == BotStatus.START) {
            return false;
        }
        startUpdatePolling();
        bot.setStatus(BotStatus.START);
        return true;
    }

    public boolean stop() {
        if (bot.getStatus() == BotStatus.STOP) {
            return false;
        }
        stopUpdatePolling();
        bot.setStatus(BotStatus.STOP);
        return true;
    }

    public BotStatus getStatus() {
        return bot.getStatus();
    }

    private void startUpdatePolling() {
        GetUpdates timeout = new GetUpdates().timeout(bot.getBotSettings().getLongPollingTimeout());
        UpdatesListener updatesListener = (updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        bot.setUpdatesListener(updatesListener, timeout);
    }

    private void stopUpdatePolling() {
        bot.removeGetUpdatesListener();
    }

    private void handleUpdate(Update update) {
        if (privateMessageHandler.tryHandle(update)
                || callbackQueryHandler.tryHandle(update)
                || chatJoinRequestHandler.tryHandle(update)) {
            // update handled, return
        }
    }
}