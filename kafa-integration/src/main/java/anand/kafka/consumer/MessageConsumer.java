package anand.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

	@KafkaListener(topics = "my-topic", groupId = "anand-consumer-group")
	public void listen(String message) {
		System.out.println("Received message: " + message);
	}

}