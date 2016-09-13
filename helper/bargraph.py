#usage: python bargraph.py filename

import sys
import matplotlib.pyplot as plt
import numpy as np

filename = sys.argv[1]

time_elapsed = []
query_type = []
query_list = []

def ret_color(query_type):
	if(query_type == 'SELECT'):
		return 'y'
	if(query_type == 'UPDATE'):
		return 'g'
	if(query_type == 'INSERT'):
		return 'b'

with open(filename, 'r') as log:
	#f = open('writing', 'w')
	for line in log:
		columns = line.split()
		columns[0] = columns[0].replace(':','')
		if(columns[0] == '0.0'):
			columns[0] = columns[0] + '001'
		time_elapsed.append(columns[0])
		query_type.append(columns[1])
		#f.write(columns[0] + '\n')
	#f.close()

with open(filename, 'r') as log:
        for line in log:
                columns = line.split(':')
		columns[1] = columns[1].replace('\n', '')
                query_list.append(columns[1])

#f2 = open('writing2','w')
#for query in query_list:
#	f2.write(query + '\n')
#f2.close

max_time = 0
index = 0
count = 0
for time in time_elapsed:
	i = int(float(time)*1000)
	if(i > max_time):
		max_time = i
		index = count
	count += 1

count = 0
worst_query = []
for query in query_list:
	if(count == index):
		worst_query.append(query)
	count += 1

N = 1800
ind = np.arange(N)

fig, ax = plt.subplots()
#i = 1
#for time in time_elapsed:
#	plt.bar(ind,i,int(float(time)*10000),color=ret_color(query_type[i-1]))
#	i+=1

#height = []
#for i in range(1,1801):
#	height.append(i)

count = 0
time_elapsed2 = []
for time in time_elapsed:
	time_elapsed2.append(int(float(time)*1000))
	count+=1

ax.bar(ind,time_elapsed2,1, alpha = 0.3) #color=ret_color(query_type[i-1]))

if(index > 1000):
	ax.annotate('Time spent (ms): ' + str(max_time), xy=(index,max_time), xytext=(index-1000, 
	max_time+1),arrowprops=dict(facecolor='b',shrink=0.05))
else:
	ax.annotate('Time spend (ms): ' + str(max_time), xy=(index,max_time), xytext=(index+330, 
        max_time+1),arrowprops=dict(facecolor='b',shrink=0.05))

#plt.suptitle('* ' + worst_query[0])
plt.xlabel('Number of Queries Ran (' + str(count)  + ' total)')
plt.ylabel('Length of Query (milliseconds)')
plt.title(filename)
#plt.xticks(ind,'')

plt.show()

