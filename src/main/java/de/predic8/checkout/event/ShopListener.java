package de.predic8.checkout.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predic8.checkout.model.Price;
import de.predic8.checkout.api.CheckoutRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class ShopListener {

	private final Logger log = LoggerFactory.getLogger(CheckoutRestController.class);

	private final Map<String, BigDecimal> prices;
	private final ObjectMapper mapper;

	public ShopListener(Map<String, BigDecimal> prices, ObjectMapper mapper) {
		this.prices = prices;
		this.mapper = mapper;
	}

	@KafkaListener(id = "checkout",
		topicPartitions =
			{@TopicPartition(topic = "shop",
				partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
	public void listen(Operation op) throws InvocationTargetException, IllegalAccessException {

		if (!op.getBo().equals("article"))
			return;

		op.logReceive();

		Price price = mapper.convertValue(op.getObject(), Price.class);

		switch (op.getAction()) {
			case "upsert":

				if (price.getPrice() == null) return;

				BigDecimal old = prices.get(price.getUuid());

				if ( old == null)
					old = price.getPrice();

				prices.put(price.getUuid(), old);

				break;
			case "delete":
				prices.remove(price.getUuid());
		}


	}
}