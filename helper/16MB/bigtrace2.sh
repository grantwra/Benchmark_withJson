# Workloads D and F are a priority

adb shell pm disable com.example.benchmark_withjson
sleep 1
adb shell pm enable com.example.benchmark_withjson

adb reboot
sleep 30s
adb root
sleep 2m
i=1

for j in `seq 1 1`;
do
	adb shell sh /data/removeBenchmarkData.sh
	adb shell sh /data/preBenchmark.sh #create database 
	adb shell pm disable com.example.benchmark_withjson
	sleep 5
	adb shell pm enable com.example.benchmark_withjson
	adb shell sh /data/benchmark.sh #run queries
	adb pull /data/trace.log
	mv trace.log YCSB_WorkloadA_TimingAsql.log
	sleep 1
	#sh pull_gc.sh
done
