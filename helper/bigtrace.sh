
adb -s 063d94d800609564 reboot
sleep 30s
adb -s 063d94d800609564 root
sleep 2m
i=1

for j in `seq 1 1`;
do
	adb -s 063d94d800609564 shell sh /data/removeBenchmarkData.sh
	adb -s 063d94d800609564 shell sh /data/preBenchmark.sh #create database 
	adb -s 063d94d800609564 shell sh /data/benchmark.sh #run queries
	adb -s 063d94d800609564 pull /data/trace.log
	mv trace.log 2YCSB_WorkloadF_TimingA.log
done
