package com.util.queue;

import com.util.func.config.ResourceUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

public class KafkaConsumer {
    Properties consumerConfig = getProperty();
    private static final String TOPIC_SUBCRIBER = "testad";
    ILogger logger = LoggerImpl.getInstance(this.getClass());

    private void receiveMessage() {
        try {
            org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerConfig);
            KafkaConsumerCallBack rebalanceListener = new KafkaConsumerCallBack();
            consumer.subscribe(Collections.singletonList(TOPIC_SUBCRIBER), rebalanceListener);
            int i = 0;
            Date beginTime = new Date();
            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(1000);
                    for (ConsumerRecord<String, String> record : records) {
                        //TODO logic here

                    }
                    if (records.count() > 0 && i == 0) {
                        logger.info("start consume");
                        beginTime = new Date();
                    }
                    i += records.count();
                    if (i > 100000) {
                        logger.info("Consume =" + i + "message");
                        i = 1;
                    }
                    if (records.count() == 0 && i > 0) {
                        logger.info("Consume =" + i + "message");
                        i = 0;
                        logger.info("Finish Consume, duration" + (new Date().getTime() - beginTime.getTime()) + " ms");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static class KafkaConsumerCallBack implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
        }
    }

    private static Properties getProperty() {
        String serverAndPort = ResourceUtils.getInstance().getResourceDefault("10.121.14.195:8593");

        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAndPort);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group2");
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return consumerConfig;
    }
}
