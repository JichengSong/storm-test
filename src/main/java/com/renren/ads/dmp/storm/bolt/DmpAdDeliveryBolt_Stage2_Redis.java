/**
 * 
 */
package com.renren.ads.dmp.storm.bolt;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;

/**
 * @author jicheng.song
 * @since 2014年8月20日
 */
public class DmpAdDeliveryBolt_Stage2_Redis extends BaseRichBolt {

	private OutputCollector _collector;
	RedisClusterPoolClient redisDmp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.task.IBolt#prepare(java.util.Map,
	 * backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
		
		//
		try {
			// init redisDmp
			redisDmp = new RedisClusterPoolClient("dmp",
					"webzk1.d.xiaonei.com:2181," + "webzk2.d.xiaonei.com:2181,"
							+ "webzk3.d.xiaonei.com:2181,"
							+ "webzk4.d.xiaonei.com:2181,"
							+ "webzk5.d.xiaonei.com:2181");
			// 这里的zk1:2181是在hosts文件中给zk1绑定了一个ip之后的写法.如果没有绑定hosts,就直接写"ip:端口号";
			redisDmp.init();
		} catch (ClusterConnException e) {
			e.printStackTrace();
			// retry or something
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.task.IBolt#execute(backtype.storm.tuple.Tuple)
	 */
	@Override
	public void execute(Tuple inputTupe) {
		// TODO Auto-generated method stub

		DmpMessage dmpMessage = (DmpMessage) inputTupe
				.getValueByField("DmpMessage");
		// 入redis
		System.out.println("入redis:"+dmpMessage.getDmpId()+","+dmpMessage.getCrowndId());
		this.redisDmp.sadd(dmpMessage.getDmpId(),
				String.valueOf(dmpMessage.getCrowndId()));
		//入hbase
		
		// System.out.println("bolt recive message:" + dmpMessage.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * backtype.storm.topology.IComponent#declareOutputFields(backtype.storm
	 * .topology.OutputFieldsDeclarer)
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
