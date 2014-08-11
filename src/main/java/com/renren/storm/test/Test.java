package com.renren.storm.test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.testing.TestWordSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("begin ");
		TopologyBuilder builder = new TopologyBuilder(); 
		//设置Spout
		builder.setSpout("words", new TestWordSpout(), 2); 
		//设置Bolt
		builder.setBolt("exclaim1", new ExclamationBolt(), 3)
		        .shuffleGrouping("words");
		builder.setBolt("exclaim2", new ExclamationBolt(), 2)
		        .shuffleGrouping("exclaim1");
		//
		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(2);
		//StormSubmitter.submitTopology("asdfasdf", conf, builder.createTopology());
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", conf, builder.createTopology());
		Utils.sleep(10000);
		cluster.killTopology("test");
		cluster.shutdown();

		System.out.println("end...");
	}

}
