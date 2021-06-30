package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.BotController;
import io.github.butkoprojects.bots.preprocess.annotation.InlineRequest;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.Arrays;

@BotController
public class InlineController {

    @InlineRequest( "default" )
    public BotApiMethod process( Update update ) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId( update.getInlineQuery().getId() );

        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.setDisableWebPagePreview( true );
        messageContent.setMessageText( "dick" );

        InlineQueryResultArticle article = new InlineQueryResultArticle();
        article.setInputMessageContent( messageContent );
        article.setId( String.valueOf( System.currentTimeMillis() ) );
        article.setTitle( "DICK" );
        article.setDescription( "dick is something" );
        article.setThumbUrl( "https://i.ibb.co/w0NN0yg/thumbnail.jpg" );
        answerInlineQuery.setResults( Arrays.asList( article ) );
        return answerInlineQuery;
    }
}
