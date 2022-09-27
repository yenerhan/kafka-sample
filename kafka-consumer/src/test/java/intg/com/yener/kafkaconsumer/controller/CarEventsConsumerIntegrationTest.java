package intg.com.yener.kafkaconsumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yener.kafkaconsumer.consumer.CarEventConsumer;
import com.yener.kafkaconsumer.entity.CarEvent;
import com.yener.kafkaconsumer.repository.CarEventRepository;
import com.yener.kafkaconsumer.service.CarEventService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = CarEventConsumer.class)
@EmbeddedKafka(topics = {"car-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class CarEventsConsumerIntegrationTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;

    @SpyBean
    CarEventConsumer carEventConsumerSpy;

    @SpyBean
    CarEventService carEventServiceSpy;

    @SpyBean
    CarEventRepository carEventRepositorySpy;


    @BeforeEach
    void setUp() {
        for (MessageListenerContainer messageListenerContainer : endpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @AfterEach
    void tearDown() {
        carEventRepositorySpy.deleteAll();
    }


    @Test
    void publishNewCarEvents() throws ExecutionException, InterruptedException, JsonProcessingException {
        //given
        String json = "{\n" +
                "    \"carEventId\": null,\n" +
                "    \"car\": {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"Test4\"\n" +
                "    }\n" +
                "}";
        kafkaTemplate.sendDefault(json).get();

        //when
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        //then
        verify(carEventConsumerSpy, times(1)).onMessage(isA(ConsumerRecord.class));
        verify(carEventServiceSpy, times(1)).processCarEvent(isA(ConsumerRecord.class));
        verify(carEventRepositorySpy, times(1)).save(isA(CarEvent.class));
    }
}
