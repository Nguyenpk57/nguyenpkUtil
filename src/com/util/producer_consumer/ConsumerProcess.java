package com.util.producer_consumer;

import com.util.bean.ProcessBean;
import com.util.func.GsonUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import utils.BlockQueue;

public class ConsumerProcess implements Runnable {
    ILogger logger = LoggerImpl.getInstance(this.getClass());

    private final BlockQueue recvQ;

    private String threadName = null;

    private boolean running = false;

    public ConsumerProcess(String threadName, BlockQueue recvQ) {
        this.threadName = threadName;
        this.recvQ = recvQ;
    }

    public void start() {
        if (!running) {
            Thread thread = new Thread(this);
            thread.setName(threadName);
            thread.start();
            running = true;
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                ProcessBean ussdMessage = dequeue();
                if (ussdMessage == null) {
                    Thread.sleep(100);
                    continue;
                }
                logger.info("recvice ussd: " + GsonUtils.getInstance().to(ussdMessage));
                //TODO business here


            } catch (Exception ex) {
                logger.error("something wrong", ex);
            }
        }
    }

    private ProcessBean dequeue() {
        Object object = recvQ.dequeue();
        if (object != null) {
            return (ProcessBean) object;
        }

        return null;
    }
}
