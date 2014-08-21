/**
 * 
 */
package com.renren.ads.dmp.test;

import com.renren.ads.dmp.kafka.DmpConsumer;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;

/**
 * @author jicheng.song
 * @since 2014年8月21日
 */
public class KafkaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DmpConsumer consumer = new DmpConsumer("ad_delivery", "test-dmp-group");
		DmpMessage message = consumer.getMessage();

		System.out.println(message.toString());
		System.out.println(message.getDmpId() + "," + message.getCrowndId());
	}

}
