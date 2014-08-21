/**
 * 
 */
package com.renren.ads.dmp.storm.bolt;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.renren.ads.dmp.biz.DmpBizServiceFactory;
import com.renren.ads.dmp.biz.api.DmpBizServicePrx;
import com.renren.ads.dmp.biz.api.Identity;
import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.xiaonei.ads.dmp.hbase.utils.RowkeyUtils;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;
import com.xiaonei.ads.dmp.kafka.model.DmpOperation;

/**
 * @author jicheng.song
 * @since 2014年8月20日
 */
public class DmpAdDeliveryBolt_Stage1 extends BaseRichBolt {

	private OutputCollector _collector;
	DmpBizServiceFactory serviceFactory;
	DmpBizServicePrx service;

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
		// init dmpid mapping
		this.serviceFactory = DmpBizServiceFactory.getInstance();
		this.service = serviceFactory.getService();
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
		// 获取dmpid对应的id列表(userid,jebeid,deviceid等)
		List<Identity> identityList = this.service.getListByDmpId(RowkeyUtils
				.reverseHexString2Long(dmpMessage.getDmpId()));
		// 发射给下一个bolt
//		for (Identity identity : identityList) {
//			//create 
//			DmpMessage dm = new DmpMessage();
//			dm.setDmpId(identity.code);
//			dm.setCrowndId(dmpMessage.getCrowndId());
//			dm.setFeatures(dmpMessage.getFeatures());
//			dm.setOpt((DmpOperation) dmpMessage.getOpt());
//			//emit
//			this._collector.emit(new Values(dm));
//		}
		this._collector.emit(new Values(dmpMessage));

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
