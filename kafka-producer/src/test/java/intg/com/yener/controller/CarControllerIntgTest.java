package intg.com.yener.controller;

import com.yener.kafkaproducer.controller.CarController;
import com.yener.kafkaproducer.model.Car;
import com.yener.kafkaproducer.model.CarEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = CarController.class)
@EmbeddedKafka(topics = {"car-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class CarControllerIntgTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void postCarEvent() {

        //given
        Car car = Car.builder()
                .id(1)
                .name("BM")
                .build();
        CarEvent event = CarEvent.builder()
                .carEventId(null)
                .car(car)
                .build();

        HttpHeaders header = new HttpHeaders();
        header.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<CarEvent> request = new HttpEntity<>(event, header);


        //when
        ResponseEntity<CarEvent> responseEntity = restTemplate.exchange("/api/v1/car/saveCar", HttpMethod.POST, request, CarEvent.class);

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
