Instruction to run the application
----------------------------------

Below is the script to run the application in stampede machine

#!/bin/bash
#SBATCH -J Project2     # job name
#SBATCH -o Project2.out # output and error file name (%j=jobID)
#SBATCH -n 32           # total number of cpus requested
#SBATCH -p development  # queue -- normal, development, etc.
#SBATCH -t 00:30:00     # run time (hh:mm:ss) - 1.5 hours
#SBATCH --mail-user=gxe150130@utdallas.edu
#SBATCH --mail-type=begin  # email me when the job starts
#SBATCH --mail-type=end    # email me when the job finishes
cd "$HOME/MCP P02"
javac ApplicationRunner.java

echo "90,9,1"
java ApplicationRunner 32 1000000 90,9,1

echo "70,20,10"
java ApplicationRunner 32 1000000 70,20,10

echo "0,50,50"
java ApplicationRunner 32 1000000 0,50,50



Following is the understanding about the above script

a.	Project2 directory created under my home location in Stampede
b.	ApplicationRunner.java has the main method as starting point
c.	While executing, the application expects three parameters
	#1	Maximum number of threads to run
		Experiments are run for thread counts from 2 to arg#1 in increments of 2
	#2	Total number of operations per thread
	#3	Probability distribution over the three operation
