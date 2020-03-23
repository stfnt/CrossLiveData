package com.baimaisu.process.processor;

import com.baimaisu.process.processor.impl.ContentProcessorImpl;

public class ContentProcessorFactory {
    private static ContentProcessorFactory contentProcessorFactory = new ContentProcessorFactory();

    public static ContentProcessorFactory get() {
        return contentProcessorFactory;
    }

    public ContentProcessor getContentProcessor() {
        return new ContentProcessorImpl();
    }
}
