/**
 * 
 */
package com.renren.ads.dmp.storm.bolt;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

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
public class DmpAdDeliveryBolt_Stage2_HBase extends BaseRichBolt {

	private OutputCollector _collector;
	private final String tableName = "user_crowd";
	public static final String DELIMITER = ",";
	HTable table;

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
		// init table
		Configuration cfg = HBaseConfiguration.create();
		try {
			this.table = new HTable(cfg, tableName);
			//this.table.setAutoFlush(false);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		// 入hbase
		byte[] row = Bytes.toBytes(dmpMessage.getDmpId());
		byte[] family = Bytes.toBytes("f");
		byte[] qualifier = Bytes.toBytes("crowdIDs");
		Get get = new Get(row);
		get.addColumn(family, qualifier);

		try {
			Result result = this.table.get(new Get(row));
			String newCrowdId = String.valueOf(dmpMessage.getCrowndId());
			String newValues;
			String oldValues = Bytes.toString(result
					.getValue(family, qualifier));
			if (oldValues != null) {
				String[] oldValueArr = oldValues.split(DELIMITER);
				// 如果newCrowdId在hbase里已经存在了,则直接return
				for (int i = 0; i < oldValueArr.length; i++) {
					if (newCrowdId.equals(oldValueArr[i])) {
						return;
					}
				}
				// 生成新的value字符串
				newValues = oldValues + DELIMITER + newCrowdId;
			} else {
				newValues = newCrowdId;
			}
			//
			Put put = new Put(row);
			put.add(family, qualifier, Bytes.toBytes(newValues));
			//
			System.out.println("HBase Put:row=" + dmpMessage.getDmpId()
					+ ",value=" + newValues);
			//
			this.table.put(put);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
