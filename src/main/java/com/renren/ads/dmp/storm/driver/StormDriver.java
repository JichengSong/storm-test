package com.renren.ads.dmp.storm.driver;

import org.apache.hadoop.util.ProgramDriver;

import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

import com.renren.ads.dmp.storm.topology.DmpAdDeilveryTopology;

public class StormDriver {

	/**
	 * @param args
	 * @throws InvalidTopologyException
	 * @throws AlreadyAliveException
	 */

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		// TODO Auto-generated method stub
		System.out.println("driver started ...");
		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("ad_delivery", DmpAdDeilveryTopology.class,
					"submit ad_delivery topology");
			pgd.driver(args);
			// Success
			exitCode = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("drive stoped ...");
	}

}
