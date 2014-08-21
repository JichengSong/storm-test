#!/bin/sh
mvn -s /opt/maven/conf/settings.addmp.xml clean assembly:assembly
if [ $? -eq 0 ];then
    echo "compile succeed"
else
    echo "compile failed!"
    exit -1;
fi
#####

echo "1.1 rm tmp"
rm -rf tmp/*

echo "2.1.cd tmp"
cd tmp

echo "2.2 cp"
cp ../target/storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar ./ 
echo "2.3 jar -xf >/dev/null 2>&1 "
jar -xvf storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar
echo "2.4 rm "
rm storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar
rm -rf org/slf4j
echo "2.5 jar -cvf"
jar -cf storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar *
echo "2.6 cp"
cp storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar ../target/
echo "3. cd"
cd ../

####


scp -r target/storm-test-0.0.1-SNAPSHOT.jar addmp@10.4.19.81:/data/addmp/dmp/
scp -r target/storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar addmp@10.4.19.81:/data/addmp/dmp/

if [ $? -eq 0 ];then
    echo "scp succeed"
else
    echo "scp failed!"
    exit -1;
fi

#ssh addmp@10.4.19.81 "/data/addmp/apache-storm-0.9.1-incubating/bin/storm jar /data/addmp/dmp/storm-test-0.0.1-SNAPSHOT.jar com.renren.storm.test.Test"
ssh addmp@10.4.19.81 "/data/addmp/apache-storm-0.9.1-incubating/bin/storm jar /data/addmp/dmp/storm-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.renren.ads.dmp.storm.driver.StormDriver ad_delivery"

if [ $? -eq 0 ];then
    echo "submit storm job succeed"
else
    echo "submit storm job failed!"
    exit -1;
fi
