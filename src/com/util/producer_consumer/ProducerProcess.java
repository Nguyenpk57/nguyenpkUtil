package com.util.producer_consumer;

import com.util.bean.ProcessBean;
import com.util.func.GsonUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import utils.BlockQueue;

public class ProducerProcess implements Runnable {
    ILogger logger = LoggerImpl.getInstance(this.getClass());

    private final BlockQueue sendQueue;
    private final BlockQueue errQueue;

    private String threadName = null;
    private int threadIdex;

    private boolean running = false;

    public ProducerProcess(String threadName, int threadIdex, BlockQueue sendQueue, BlockQueue errorQueue) {
        this.threadIdex = threadIdex;
        this.threadName = threadName;
        this.sendQueue = sendQueue;
        this.errQueue = errorQueue;
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
            ProcessBean msg = null;
            try {
                //lay ban tin tu trong queue ra
                msg = (ProcessBean) sendQueue.dequeue();
                if (msg == null) {
                    Thread.sleep(100);
                    logger.info("no message to received");
                    continue;
                }
                logger.info(threadName + "-received a ussd message:" + GsonUtils.getInstance().to(msg));
                //TODO business here


            } catch (Exception ex) {
                logger.error("send to gateway fail: ", ex);
                //gui thong bao loi lai giai phong giao dich
                if (msg != null) {
                    notifyErr2Transaction(msg, "can't send to UssdGW");
                }
            }
            logger.debug("sent complete");
        }
    }

    private void notifyErr2Transaction(ProcessBean msg, String reason) {
        //TODO handle transaction not finish
    }

}
