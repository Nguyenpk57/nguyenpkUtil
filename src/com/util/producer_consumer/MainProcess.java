package com.util.producer_consumer;


import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import utils.BlockQueue;

public class MainProcess {
    private static MainProcess MAIN_PROCESS;

    private final static int QUEUE_SIZE = 10000;
    private final static int NUM_SEND_THREAD = 1000;
    private final static int NUM_WORKER_THREAD = 1000;

    private final BlockQueue recvQ;
    private final BlockQueue sendQ;

    ILogger logger = LoggerImpl.getInstance(this.getClass());

    private MainProcess() {
        recvQ = new BlockQueue(2000, QUEUE_SIZE);
        sendQ = new BlockQueue(2000, QUEUE_SIZE);
    }

    public static MainProcess getInstance() {
        if (MAIN_PROCESS == null) {
            MAIN_PROCESS = new MainProcess();
            MAIN_PROCESS.startConnection();
        }
        return MAIN_PROCESS;
    }

    private void startConnection() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        try {
            //tao thread pool cho gui cac ban tin toi ussdGW
            logger.info("make threads for send");
            int size = NUM_SEND_THREAD;
            logger.info("num thread send: " + size);
            for (int i = 0; i < size; i++) {
                ProducerProcess producerProcess = new ProducerProcess("UssdProcessSender" + i, i, sendQ, recvQ);
                producerProcess.start();
            }

            logger.info("make threads for Receive");
            int sizeReceive = NUM_WORKER_THREAD;
            logger.info("num thread Receive: " + sizeReceive);
            for (int i = 0; i < sizeReceive; i++) {
                ConsumerProcess consumerProcess = new ConsumerProcess("ConsumerProcess" + i, recvQ);
                consumerProcess.start();
            }

        } catch (Exception ex) {
            logger.error(ex);
        }

    }

}
