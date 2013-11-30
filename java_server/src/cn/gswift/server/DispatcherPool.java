package cn.gswift.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by davidlau on 13-11-29.
 */
public class DispatcherPool {

    public ExecutorService threadPool;

    public DispatcherPool(ExecutorService executorService) {
        this.threadPool = executorService;
    }

    public void execute(Runnable runnable){
        this.threadPool.execute(runnable);
    }

    public void shutdown() {
        this.threadPool.shutdown();
        this.waitForTermination();
        this.threadPool = null;
    }

    private void waitForTermination(){
        while (!this.threadPool.isTerminated()){
            try {
                 this.threadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                 e.printStackTrace();
            }
        }
    }
}
