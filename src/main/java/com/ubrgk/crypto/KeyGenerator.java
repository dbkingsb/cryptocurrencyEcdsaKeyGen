package com.ubrgk.crypto;

import org.bitcoinj.core.ECKey;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
@SuppressWarnings({"PMD.AvoidThrowingRawExceptionTypes", "PMD.AvoidInstantiatingObjectsInLoops"})
final class KeyGenerator {
    private KeyGenerator() {}

    public static List<ECKey> generateKeys(final int numberOfAddress) {
        final SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }

        final List<Task> tasks = new ArrayList<>();
        for (int i=0; i<numberOfAddress; i++) {
            tasks.add(new Task(secureRandom));
        }

        final int nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Using " + nThreads + " threads for key generation.");

        final List<ECKey> ecKeys = new ArrayList<>();
        try {
            final ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            final List<Future<ECKey>> futures = executorService.invokeAll(tasks);
            for (final Future<ECKey> future : futures) {
                ecKeys.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return ecKeys;
    }

    static class Task implements Callable<ECKey> {
        private final SecureRandom rng;

        Task(final SecureRandom rng) {
            this.rng = rng;
        }

        @Override
        public ECKey call() {
            return new ECKey(rng);
        }
    }
}
