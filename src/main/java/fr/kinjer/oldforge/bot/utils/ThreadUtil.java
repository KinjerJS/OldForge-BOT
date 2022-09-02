package fr.kinjer.oldforge.bot.utils;

public class ThreadUtil {

    public static void taskLater(int time, Runnable run) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run.run();
        }).start();
    }

}
