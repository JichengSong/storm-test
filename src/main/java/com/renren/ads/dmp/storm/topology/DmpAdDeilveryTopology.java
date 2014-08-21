package com.renren.ads.dmp.storm.topology;

import com.renren.ads.dmp.storm.DmpAdDeliveryBolt_Stage2_HBase;
import com.renren.ads.dmp.storm.DmpAdDeliverySpout;
import com.renren.ads.dmp.storm.bolt.DmpAdDeliveryBolt_Stage1;
import com.renren.ads.dmp.storm.bolt.DmpAdDeliveryBolt_Stage2_Redis;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class DmpAdDeilveryTopology {

	public void submitTopology() throws AlreadyAliveException, InvalidTopologyException {
		System.out.println("begin ");
		TopologyBuilder builder = new TopologyBuilder();
		// 设置Spout
		builder.setSpout("spout_1", new DmpAdDeliverySpout(), 1);

		// 设置Bolt
		builder.setBolt("bolt_stage1", new DmpAdDeliveryBolt_Stage1(), 2)
				.shuffleGrouping("spout_1");

		 builder.setBolt("bolt_stage2_redis", new
		 DmpAdDeliveryBolt_Stage2_Redis(), 1).shuffleGrouping(
		 "spout_1");

		builder.setBolt("bolt_stage2_hbase",
				new DmpAdDeliveryBolt_Stage2_HBase(), 1).shuffleGrouping(
				"bolt_stage1");

		//
		StormTopology topology = builder.createTopology();
		Config conf = new Config();

		conf.setDebug(false);
		conf.setNumWorkers(8);
		conf.setMaxSpoutPending(5000);
		StormSubmitter.submitTopology("DmpAdDeliveryTopology-01", conf,
				topology);

//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology("DmpAdDeliveryTopology", conf, topology);
		Utils.sleep(1000);

		System.out.println("end...");
	}

	public static void main(String[] args) {
		try {
			new DmpAdDeilveryTopology().submitTopology();
		} catch (AlreadyAliveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
