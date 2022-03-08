package com.util.queue;

import com.util.bean.FtpConfig;
import com.util.func.FtpUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import org.apache.kafka.clients.producer.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class KafkaProducer {
    public static final Properties kafkaProperty = getProperty();
    private static final String TOPIC_SEND = "testad";
    ILogger logger = LoggerImpl.getInstance(this.getClass());

    private void deliveryMessage() {
        Producer<String, String> producer = null;
        FtpConfig ftpConfig = new FtpConfig();
        FtpUtils ftpUltils = new FtpUtils(ftpConfig);
        if (ftpUltils.connectFtpServer() < 0) {
            logger.error("connect to ftp fail");
            return;
        }
        if (ftpUltils.getFileProcess("G:/Report Data") < 0) {
            logger.error("Process download file fail");
            return;
        }
        if (ftpUltils.clearFtpConnect() < 0) {
            logger.error("Clear ftp connection error");
        }
        if (ftpUltils.listFileDownloadSuccess.size() > 0) {
            try {
                producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(kafkaProperty);
                KafkaConsumerCallBack callback = new KafkaConsumerCallBack();
                Boolean isPingOK = false; // this parameter to allow for send ping
                for (String filePath : ftpUltils.listFileDownloadSuccess) {

                    System.out.println("begin push to kafka file:" + filePath);
                    try {
                        Path path = Paths.get(filePath);
                        BufferedReader reader = Files.newBufferedReader(path);
                        String line = null;
                        int i = 0;
                        while ((line = reader.readLine()) != null) {

                            ProducerRecord<String, String> data = new ProducerRecord<String, String>(TOPIC_SEND, line);
                            producer.send(data, callback);
                            i++;
                            if (i % 1000 == 0) {
                                producer.flush();
                                if (i % 100000 == 0) {
                                    logger.info("import to kafka=" + i);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                logger.errors("error: ", e.getMessage());
            } finally {
                producer.flush();
                if (producer != null) {
                    producer.close();
                }

            }
        }
    }

    private static class KafkaConsumerCallBack implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                System.out.println("Error while producing message to topic :" + recordMetadata);
                e.printStackTrace();
            } else {
            }
        }
    }

    private static Properties getProperty() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.121.14.195:8593");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "MibitelApp");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

}
