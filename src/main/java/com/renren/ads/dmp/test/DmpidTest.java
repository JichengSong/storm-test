/**
 * 
 */
package com.renren.ads.dmp.test;

import java.util.List;

import com.renren.ads.dmp.biz.DmpBizServiceFactory;
import com.renren.ads.dmp.biz.api.DmpBizServicePrx;
import com.renren.ads.dmp.biz.api.Identity;

/**
 * @author jicheng.song
 * @since 2014年8月20日
 */
public class DmpidTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DmpBizServiceFactory serviceFactory = DmpBizServiceFactory.getInstance();
		
		DmpBizServicePrx service= serviceFactory.getService();
		
		List<Identity> list=service.getListByDmpId(1000);
		
		for(Identity identity : list){
			System.out.println("type="+identity.type+",code="+identity.code);
		}
		System.out.println("end");
		
		serviceFactory.destory();
		
	}

}
