#!/bin/sh
mvn -s /opt/maven/conf/settings.addmp.xml clean assembly:assembly
if [ $? -eq 0 ];then
    echo "compile succeed"
else
    echo "compile failed!"
    exit -1;
fi
scp -r target/storm-test-0.0.1-SNAPSHOT.jar addmp@10.4.19.81:/data/addmp/dmp/
scp -r target/storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar addmp@10.4.19.81:/data/addmp/dmp/

if [ $? -eq 0 ];then
    echo "scp succeed"
else
    echo "scp failed!"
    exit -1;
fi

#ssh addmp@10.4.19.81 "/data/addmp/apache-storm-0.9.1-incubating/bin/storm jar /data/addmp/dmp/storm-test-0.0.1-SNAPSHOT.jar com.renren.storm.test.Test"
ssh addmp@10.4.19.81 "/data/addmp/apache-storm-0.9.1-incubating/bin/storm jar /data/addmp/dmp/storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.renren.storm.test.Test"

if [ $? -eq 0 ];then
    echo "submit storm job succeed"
else
    echo "submit storm job failed!"
    exit -1;
fi
