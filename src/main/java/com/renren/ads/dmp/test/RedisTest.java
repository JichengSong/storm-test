/**
 * 
 */
package com.renren.ads.dmp.test;

import backtype.storm.generated.DistributedRPC.Client;

import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.ClusterException.ClusterOpException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;

/**
 * @author jicheng.song
 * @since 2014年8月19日
 */
public class RedisTest {
	public static void main(String[] args) {
		System.out.println("begin.");
		// 1.init client
		RedisClusterPoolClient redis = null;
		try {
			redis = new RedisClusterPoolClient("dmp.dau",
					"webzk1.d.xiaonei.com:2181," + "webzk2.d.xiaonei.com:2181,"
							+ "webzk3.d.xiaonei.com:2181,"
							+ "webzk4.d.xiaonei.com:2181,"
							+ "webzk5.d.xiaonei.com:2181");
			// 这里的zk1:2181是在hosts文件中给zk1绑定了一个ip之后的写法.如果没有绑定hosts,就直接写"ip:端口号";
			redis.init();
		} catch (ClusterConnException e) {
			e.printStackTrace();
			// retry or something
		}
		
		redis.sadd("1", "crowd_001");
		redis.sadd("1", "crowd02");
		redis.sadd("1", "crowd09");
		System.out.println(redis.smembers("1").toString());
		
		System.exit(0);
		
		// 2.test put/get

		for (int i = 0; i < 100000; i++) {
			try {
				// client.set("key", "value");
				// 此处的set方法只能写入String类型,如果想要存储复杂的类型,则需要把要存储的类型转化为String,并且转化之后还能方便的解析回来.
				// 可以使用json格式.见下方的详述.
				if (redis.exists(String.valueOf(i))) {
					System.out.println("i=" + i);
				}
				
			} catch (ClusterOpException e) {
				// retry or something
			}

		}

		System.out.println("end.");
	}
}
