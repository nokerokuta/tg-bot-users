package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatAdministratorRights;
import com.pengrad.telegrambot.request.GetMyDefaultAdministratorRights;
import com.pengrad.telegrambot.request.SetMyDefaultAdministratorRights;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.ChatConfig;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotInitializerService {
    
    private final ChatConfig chatConfig;
    private final TelegramBot bot;
    
    
    public void initialiseBot() {
        initialiseAdministratorRights();
        initialiseGroups();
    }
    
    private void initialiseAdministratorRights() {
        if (!administratorRightsIsCorrect()) {
            setAdministratorRights();
        }
    }
    
    private boolean administratorRightsIsCorrect() {
        var getRights = new GetMyDefaultAdministratorRights();
        var response = bot.execute(getRights);
        var rights = response.result();
        return rights.canInviteUsers();
    }
    
    private void setAdministratorRights() {
        var chatAdministratorRights = new ChatAdministratorRights();    // TODO add isAnonymous(boolean)
        chatAdministratorRights.canInviteUsers(true);
        
        var setMyDefaultAdministratorRights = new SetMyDefaultAdministratorRights();
        setMyDefaultAdministratorRights.rights(chatAdministratorRights);
        bot.execute(setMyDefaultAdministratorRights);
    }
    
    
    private void initialiseGroups() {
    
    }

    
}