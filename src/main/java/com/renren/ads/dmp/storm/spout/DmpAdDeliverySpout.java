/**
 * 
 */
package com.renren.ads.dmp.storm.spout;

import java.util.HashMap;
import java.util.Map;

import com.renren.ads.dmp.kafka.DmpConsumer;
import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * @author jicheng.song
 * @since 2014年8月20日
 */
public class DmpAdDeliverySpout extends BaseRichSpout {

	private SpoutOutputCollector _collector;
	private DmpConsumer consumer;
	RedisClusterPoolClient redisDau;

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#open(java.util.Map,
	 * backtype.storm.task.TopologyContext,
	 * backtype.storm.spout.SpoutOutputCollector)
	 */
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
		this.consumer = new DmpConsumer("ad_delivery", "ad_delivery_group");
		// init redis
		try {
			redisDau = new RedisClusterPoolClient("dmp.dau",
					"webzk1.d.xiaonei.com:2181," + "webzk2.d.xiaonei.com:2181,"
							+ "webzk3.d.xiaonei.com:2181,"
							+ "webzk4.d.xiaonei.com:2181,"
							+ "webzk5.d.xiaonei.com:2181");
			// 这里的zk1:2181是在hosts文件中给zk1绑定了一个ip之后的写法.如果没有绑定hosts,就直接写"ip:端口号";
			redisDau.init();
		} catch (ClusterConnException e) {
			e.printStackTrace();
			// retry or something
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#nextTuple()
	 */
	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		DmpMessage dmpMessage = this.consumer.getMessage();

		// 如果dmpid在7日dau里,则发送给bolt处理.
		if (redisDau.exists(dmpMessage.getDmpId())) {
			this._collector.emit(new Values(dmpMessage));
		}
		//
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
		declarer.declare(new Fields("DmpMessage"));
	}

}
