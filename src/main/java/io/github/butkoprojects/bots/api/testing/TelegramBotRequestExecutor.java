package io.github.butkoprojects.bots.api.testing;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.SneakyThrows;

public class TelegramBotRequestExecutor {
    private String chatId;
    private String botId;

    public TelegramBotRequestExecutor( final String chatId,
                                       final String botId ) {
        this.chatId = chatId;
        this.botId = botId;
    }

    public String sendTextToBot(final String textToSend ) throws UnirestException {
        String url = "https://api.telegram.org";
        url += "/bot" + botId;
        url += "/sendMessage?chat_id=" + chatId;
        url += "&text=" + textToSend;
        return Unirest.get( url ).asString().getBody();
    }

    public static void main(String[] args) throws UnirestException {
        TelegramBotRequestExecutor executor = new TelegramBotRequestExecutor(
                "382136565",
                "1136149470:AAEycB71ZvqlqgfWcwYOxtRdqE32rzLUKx4");

        System.out.println( executor.sendTextToBot( "/home" ) );
    }
}
