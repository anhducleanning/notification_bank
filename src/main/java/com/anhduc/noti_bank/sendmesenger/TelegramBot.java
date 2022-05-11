package com.anhduc.noti_bank.sendmesenger;

import com.anhduc.noti_bank.model.AccountBalance;
import com.anhduc.noti_bank.serivce.ServiceAccountBalance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static Logger logger = Logger.getLogger(TelegramBot.class);
    @Autowired
    ServiceAccountBalance blance;
    @Value("${telegram.bot.username}")
    private String user;
    @Value("${telegram.bot.token}")
    private String token;


    @Override
    public String getBotUsername() {
        return user;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
       if(update.hasMessage()){
           System.out.println(update);
            Message message = update.getMessage();
            if(message.hasText()){
                SendMessage sm = new SendMessage();
                List<AccountBalance> courses = blance.getNoti();
                for (AccountBalance s : courses) {
                    sm.setText("Thông Báo : " +s );
                }
                logger.info("Send Bot Telegram");

                sm.setChatId(String.valueOf(message.getChatId()));
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    logger.error("faili tele bot");
                    e.printStackTrace();
                }
            }
       }
    }
}
