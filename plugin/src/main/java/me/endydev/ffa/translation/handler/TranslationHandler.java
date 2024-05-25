package me.endydev.ffa.translation.handler;

import me.yushust.message.MessageHandler;
import me.yushust.message.config.ConfigurationModule;
import me.yushust.message.source.MessageSource;
import me.yushust.message.source.MessageSourceDecorator;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.ReplacePack;

import java.util.Collections;

public class TranslationHandler {
    protected final MessageHandler delegate;

    protected final String prefixNode;

    public static TranslationHandler fromSource(String fallbackLang,
                                                String prefixNode,
                                                MessageSource source,
                                                ConfigurationModule handle) {
        MessageSourceDecorator decorator = MessageSourceDecorator.decorate(source);
        decorator.addFallbackLanguage(fallbackLang);
        return fromSource(prefixNode,
                decorator.get(),
                handle);
    }

    public static TranslationHandler fromSource(String prefixNode,
                                                MessageSource source,
                                                ConfigurationModule handle) {
        return new TranslationHandler(
                MessageHandler.of(source, handle), prefixNode);
    }

    public TranslationHandler(MessageHandler delegate,
                              String prefixNode) {
        this.delegate = delegate;
        this.prefixNode = prefixNode;
    }

    public String format(Object entity, String node) {
        return delegate.replacing(entity, node);
    }

    public String format(Object entity, String path, Object... placeholders) {
        return delegate.replacing(entity, path, placeholders);
    }

    public String formatLang(String language, String path, Object... placeholders) {
        TrackingContext context = new TrackingContext(null,
                language,
                placeholders,
                ReplacePack.make(),
                Collections.emptyMap(),
                delegate);
        return delegate.format(context, path);
    }

    public void send(Object entity, String node, boolean formal, Object... entities) {
        String prefix = formal
                ? format(entity, prefixNode)
                : "";
        delegate.dispatch(entity,
                node,
                prefix,
                ReplacePack.make(entities),
                entities);
    }

    public String getPrefix(Object entity) {
        return format(entity, prefixNode);
    }

    public String getPrefix(String lang) {
        return formatLang(lang, prefixNode);
    }

    public MessageHandler getDelegate() {
        return delegate;
    }
}
