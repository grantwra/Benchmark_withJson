
#adb -s 063d94d800609564 reboot
#sleep 30s
#adb -s 063d94d800609564 root
#sleep 2m
i=1

for j in `seq 1 1`;
do
	adb -s 063d94d800609564 shell sh /data/removeBenchmarkData.sh
	adb -s 063d94d800609564 shell sh /data/benchmark.sh #create database & run queries
	#adb shell sh /data/sql.sh #run the queries
	adb -s 063d94d800609564 pull /data/trace.log
	mv trace.log 2YCSB_WorkloadC_TimingA.log
done
