package com.fitpro.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * MULTITHREADING + SYNCHRONIZATION demo.
 * Notifications (welcome message, enrollment confirmation) are "sent" on a small
 * thread pool so the web request never waits for them. The shared counter is
 * protected with synchronized methods because several pool threads update it.
 *
 * In a real system the run() body would call an email/SMS gateway.
 */
public final class NotificationService {
    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());
    private static final NotificationService INSTANCE = new NotificationService();

    private final ExecutorService pool = Executors.newFixedThreadPool(2);
    private int sentCount = 0;   // shared state, guarded by 'this'

    private NotificationService() { }

    public static NotificationService getInstance() { return INSTANCE; }

    /** Queues a notification and returns immediately. */
    public void sendAsync(String toEmail, String message) {
        pool.submit(() -> {
            try {
                Thread.sleep(300);                       // simulate network latency
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            int n = increment();
            LOG.info("[" + Thread.currentThread().getName() + "] notification #" + n
                    + " to " + toEmail + ": " + message);
        });
    }

    private synchronized int increment() { return ++sentCount; }

    public synchronized int getSentCount() { return sentCount; }

    /** Called when the web app stops, so threads do not leak. */
    public void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(3, TimeUnit.SECONDS)) pool.shutdownNow();
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
