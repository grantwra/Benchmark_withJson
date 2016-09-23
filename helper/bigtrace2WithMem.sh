
adb -s 0567cbc33443c06b reboot
sleep 30s
adb -s 0567cbc33443c06b root
sleep 2m
i=1

for j in `seq 1 1`;
do
	adb -s 0567cbc33443c06b shell sh /data/removeBenchmarkData.sh
	adb -s 0567cbc33443c06b shell sh /data/preBenchmark.sh #create database 
	adb -s 0567cbc33443c06b shell sh /data/benchmark.sh #run queries
	adb -s 0567cbc33443c06b pull /data/trace.log
	adb -s 0567cbc33443c06b pull /data/data/com.example.benchmark_withjson/files/testSQL
	adb -s 0567cbc33443c06b pull /data/data/com.example.benchmark_withjson/files/testBDB
	adb -s 0567cbc33443c06b pull /data/data/com.example.benchmark_withjson/files/MemorySQL
	adb -s 0567cbc33443c06b pull /data/data/com.example.benchmark_withjson/files/MemoryBDB
	mv testSQL newTrace/WorkloadE_SQL
	mv testBDB newTrace/WorkloadE_BDB
	mv MemorySQL newTrace/
	mv MemoryBDB newTrace/
	mv trace.log newTrace/YCSB_WorkloadE_TimingA.log
done
