package com.renren.storm.test;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DoubleAndTripleBolt extends BaseRichBolt {

	private OutputCollector _collector;
	
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector=collector;
		
	}

	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		int val = input.getInteger(0);
		this._collector.emit(input,new Values(val*2,val*3));
		this._collector.ack(input);

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("double","triple"));

	}

}
