/**
 * 
 */
package com.renren.ads.dmp.test;

import com.alibaba.fastjson.JSON;
import com.xiaonei.ads.dmp.kafka.model.DmpMessage;
import com.xiaonei.ads.dmp.kafka.model.DmpOperation;

/**
 * @author jicheng.song
 * @since 2014年8月19日
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String source =
		// "{\"dmpId\":\"258095212\",\"opt\":\"AD_DELIVERY\",\"crowndId\":328}\"";

		System.out.println("begin.");

		DmpMessage m1 = new DmpMessage();

		m1.setOpt(DmpOperation.AD_DELIVERY);
		m1.setCrowndId(243);
		m1.setFeatures(null);
		m1.setDmpId("dmp123");

		try {
			DmpMessage m2 = JSON.parseObject(JSON.toJSONString(m1)+"a",
					DmpMessage.class);
			
			System.out.println("m2.toString=" + m2.toString());

		} catch (com.alibaba.fastjson.JSONException e) {
			e.printStackTrace();
		}

		// DmpMessage dmpMessage=(DmpMessage) JSON.parseObject(source,
		// DmpMessage.class);
		//
		// if(dmpMessage==null){
		// System.out.println("dmpMessage is null ");
		// System.exit(-1);
		// }else{
		// System.out.println("dmpMessage="+dmpMessage.toString());
		// }
		System.out.println("end.");
	}

}
