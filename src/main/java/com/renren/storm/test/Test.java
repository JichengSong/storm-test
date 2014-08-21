package com.renren.storm.test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class Test {

	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub

		System.out.println("begin ");
		TopologyBuilder builder = new TopologyBuilder(); 
		//设置Spout
		builder.setSpout("spout_01", new TestWordSpout(), 1); 
		//设置Bolt
		builder.setBolt("bolt_01", new ExclamationBolt(), 1)
		        .shuffleGrouping("spout_01");
		builder.setBolt("bolt_02", new ExclamationBolt(), 1)
		        .shuffleGrouping("bolt_01");
		//
		StormTopology topology = builder.createTopology();
		Config conf = new Config();
		
		conf.setDebug(false); 
		conf.setNumWorkers(8);
		conf.setMaxSpoutPending(5000);
		StormSubmitter.submitTopology("test01_topology", conf, topology);
		
		
		//StormSubmitter.submitTopology("asdfasdf", conf, builder.createTopology());
		Utils.sleep(1000);
		// cluster.killTopology("test");
		// cluster.shutdown();

		System.out.println("end...");
	}

}
