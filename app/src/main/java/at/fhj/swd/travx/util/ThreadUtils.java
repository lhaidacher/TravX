package at.fhj.swd.travx.util;

public final class ThreadUtils {
    public static void run(Runnable runnable) {
        new Thread(runnable).start();
    }
}
