Instruction to run the application
----------------------------------

Below is the script to run the application in stampede machine

#!/bin/bash
#SBATCH -J Project3     # job name
#SBATCH -o Project3.out # output and error file name (%j=jobID)
#SBATCH -n 32           # total number of cpus requested
#SBATCH -p development  # queue -- normal, development, etc.
#SBATCH -t 00:30:00     # run time (hh:mm:ss) - 1.5 hours
#SBATCH --mail-user=gxe150130@utdallas.edu
#SBATCH --mail-type=begin  # email me when the job starts
#SBATCH --mail-type=end    # email me when the job finishes
cd "$HOME/MCP P03"
javac ApplicationRunner.java
javac TestRunner.java

Performance Testing command
echo "50,50,0 0"
java ApplicationRunner 32 1000000 50,50,0 0

echo "40,40,20 0"
java ApplicationRunner 32 1000000 40,40,20 0

echo "50,50,0 1000"
java ApplicationRunner 32 1000000 50,50,0 1000

echo "40,40,20 1000"
java ApplicationRunner 32 1000000 40,40,20 1000

Automated Testing command
java TestRunner 32 1000000



Following are the understanding about the above script

a.	Project3 directory created under my home location in Stampede
b.	ApplicationRunner.java has the main method as starting point
c.	While executing, the application expects four parameters
	#1	Maximum number of threads to run
		Experiments are run for thread counts from 2 to arg#1 in increments of 2
	#2	Total number of operations per thread
	#3	Probability distribution over the three operation
	#4	Initial size of the queue to work on
c.	While executing, the TestRunner.java expects two parameters
	#1	Maximum number of threads to run
		Experiments are run for thread counts from 2 to arg#1 in increments of 2
	#2	Total number of operations per thread
	