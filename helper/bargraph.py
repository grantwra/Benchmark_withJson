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
	f = open('writing', 'w')
	for line in log:
		columns = line.split()
		columns[0] = columns[0].replace(':','')
		if(columns[0] == '0.0'):
			columns[0] = columns[0] + '001'
		time_elapsed.append(columns[0])
		query_type.append(columns[1])
		f.write(columns[0] + '\n')
	f.close()

with open(filename, 'r') as log:
        for line in log:
                columns = line.split(':')
		columns[1] = columns[1].replace('\n', '')
                query_list.append(columns[1])

f2 = open('writing2','w')
for query in query_list:
	f2.write(query + '\n')
f2.close

N = 1800
ind = np.arange(N)

fig, ax = plt.subplots()
i = 1
for time in time_elapsed:
	ax.bar(ind,i,int(float(time)*10000),color=ret_color(query_type[i-1]))

plt.show()

