package com.renren.ads.dmp.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.xiaonei.ads.dmp.kafka.model.DmpMessage;

public class TestReadHTable {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// System.out.println(RowkeyUtils.int2ReverseHexString(1718045614));
		// 1. init table
		String tableName = "user_crowd";

		Configuration cfg = HBaseConfiguration.create();
		HTablePool pool = new HTablePool(cfg, 100);
		HTableInterface table = pool.getTable(tableName);

		String DELIMITER = ",";

		DmpMessage dmpMessage = new DmpMessage();
		dmpMessage.setDmpId("0");
		dmpMessage.setCrowndId(2);
		// 入hbase
		byte[] row = Bytes.toBytes(dmpMessage.getDmpId());
		byte[] family = Bytes.toBytes("f");
		byte[] qualifier = Bytes.toBytes("crowdIDs");
		Get get = new Get(row);
		get.addColumn(family, qualifier);

		try {
			Result result = table.get(new Get(row));
			String newCrowdId = String.valueOf(dmpMessage.getCrowndId());
			String newValues;
			String oldValues = Bytes.toString(result.getValue(family,
					qualifier));
			if (oldValues != null) {
	
				System.out.println("oldValues="+oldValues);
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
			table.put(put);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// //2. query
		// //将要查询的行封装成get,放到list里
		// List<Get> gets =new ArrayList<Get>();
		// gets.add(new Get(Bytes.toBytes("0018a4a2")));
		// gets.add(new Get(Bytes.toBytes("00000001")));
		//
		// //查询
		// Result[] results=table.get(gets);
		// for(Result result:results){
		// System.out.println("row="+Bytes.toString(result.getRow())+".................");
		// for (KeyValue kv : result.raw()) {
		// System.out.println(Bytes.toString(kv.getQualifier()) + ","
		// + Bytes.toString(kv.getValue()));
		// }
		// }
		//
		// //3. close
		// table.close();
		// pool.close();
		// System.out.println("end...");
	}
}
