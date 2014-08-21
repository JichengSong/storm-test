/**
 * 
 */
package com.renren.ads.dmp.kafka;

import java.util.Calendar;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

/**
 * @author jicheng.song
 * @since 2014年8月14日
 */
public class DmpProducer {

	/**
 	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 1.init producer
		Properties props = new Properties();
		props.put("zk.connect", "10.3.16.225:2181/kafka/test");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		// 2. send message
		// The message is sent to a randomly selected partition registered in ZK
		Calendar calendar = Calendar.getInstance();

		ProducerData<String, String> data = new ProducerData<String, String>(
				"test-dmp-topic", "test-message_"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND));
		producer.send(data);
 
		// 3.
		// List<String> messages = new java.util.ArrayList<String> ();
		// messages.add("test-message1");
		// messages.add("test-message2");
		// ProducerData<String, String> data1 = new ProducerData<String,
		// String>("test-topic1", messages);
		// ProducerData<String, String> data2 = new ProducerData<String,
		// String>("test-topic2", messages);
		// List<ProducerData<String, String>> dataForMultipleTopics = new
		// ArrayList<ProducerData<String, String>>();
		// dataForMultipleTopics.add(data1);
		// dataForMultipleTopics.add(data2);
		// producer.send(dataForMultipleTopics);
		// 4 .
		// Send a message with a partition key. Messages with the same key are
		// sent to the same partition

		// ProducerData<String, String> data = new ProducerData<String, String>(
		// "test-topic", "test-key", "test-message");
		// producer.send(data);
		producer.close();

	}
}
