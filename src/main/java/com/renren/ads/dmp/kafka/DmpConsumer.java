/**
 * 
 */
package com.renren.ads.dmp.kafka;

import com.alibaba.fastjson.JSON;
import com.renren.ads.kafka.consumer.InfraKafkaHighConsumer;
import com.renren.ads.kafka.exception.InfraKafkaException;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;

/**
 * @author jicheng.song
 * @since 2014年8月14日
 */
public class DmpConsumer {

	private String topic;
	private String group;
	InfraKafkaHighConsumer consumer;

	public DmpConsumer(String topic, String group) {
		this.topic = topic;
		this.group = group;
		this.internalInit();

	}
	/**
	 * 从kafka里读取一个DmpMessage
	 * @return
	 */
	public DmpMessage getMessage() {
		DmpMessage dmpMessage = null;
		while (dmpMessage == null) {
			String jsonStr = this.consumer.getMessage();
			dmpMessage = JSON.parseObject(jsonStr, DmpMessage.class);
			try {
				dmpMessage = (DmpMessage) JSON.parseObject(jsonStr,
						DmpMessage.class);

			} catch (com.alibaba.fastjson.JSONException e) {
				e.printStackTrace();
			}
		}
		return dmpMessage;
	}

	private void internalInit() {
		try {
			this.consumer = new InfraKafkaHighConsumer(topic, group);
		} catch (InfraKafkaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		DmpConsumer consumer =new DmpConsumer("ad_delivery","test-dmp-group");
		DmpMessage message = consumer.getMessage();
		
		System.out.println(message.toString());
		System.out.println(message.getDmpId()+","+message.getCrowndId());
		
		// // 1.初始化consumer
		// // (1.1)设置consumer属性
		// // specify some consumer properties
		// Properties props = new Properties();
		// props.put("zk.connect", "10.3.16.225:2181/kafka/test");
		// props.put("zk.connectiontimeout.ms", "1000000");
		// //
		// // props.put("autooffset.reset", "smallest");
		//
		// props.put("groupid", "test-dmp-group");
		//
		// // (1.2)创建连接
		// // Create the connection to the cluster
		// ConsumerConfig consumerConfig = new ConsumerConfig(props);
		//
		// System.out.println("autooffset=" + consumerConfig.autoOffsetReset());
		//
		// ConsumerConnector consumerConnector = Consumer
		// .createJavaConsumerConnector(consumerConfig);
		//
		// consumerConnector.commitOffsets();
		//
		// // 2.消费
		// // (2.1)将test-dmp-topic的stream流分成两个partition,允许两个线程去消费
		// // create 2 partitions of the stream for topic “test-dmp-topic”, to
		// // allow 2 (注: parallel = 2)
		// // threads to consume
		// int parallel = 1;
		// String topic = DmpOperation.AD_DELIVERY.getName();
		//
		// Map<String, List<KafkaStream<Message>>> topicMessageStreams =
		// consumerConnector
		// .createMessageStreams(ImmutableMap.of(topic, parallel));
		// List<KafkaStream<Message>> streams = topicMessageStreams.get(topic);
		// // (2.2)创建2个线程，去消费各自的partition
		// // create list of 4 threads to consume from each of the partitions
		// ExecutorService executor = Executors.newFixedThreadPool(parallel);
		// // (2.3) 在线程中对partition进行消费
		// // consume the messages in the threads
		// for (final KafkaStream<Message> stream : streams) {
		// executor.submit(new Runnable() {
		//
		// public void run() {
		// for (MessageAndMetadata messageAndMetadata : stream) {
		// // process message (msgAndMetadata.message())
		// Message message = (Message) messageAndMetadata
		// .message();
		//
		// String value = InfraKafkaMessageUtils
		// .getMessage(message);
		// try {
		// DmpMessage dmpMessage = (DmpMessage) JSON
		// .parseObject(value, DmpMessage.class);
		// System.out.println("parse successed!");
		// System.out.println(dmpMessage.toString());
		//
		// } catch (com.alibaba.fastjson.JSONException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// });
		// }
		System.out.println("end.");
	}
}
