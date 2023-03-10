package dev.aseef.aseefianmessenger.common;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

public class CAThreadFactory implements ThreadFactory {

    private long threadNumber = 0;
    private String namingFormat;

    public CAThreadFactory(String namingFormat) {
        this.namingFormat = namingFormat;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread thread = new Thread(() -> {
            try {
                r.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.setName(this.namingFormat.replace("%d", String.valueOf(threadNumber++)));
        return thread;
    }
}
