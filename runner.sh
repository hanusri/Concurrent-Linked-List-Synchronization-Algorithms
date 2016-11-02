#!/bin/bash
#SBATCH -J Project2      # job name
#SBATCH -o Project2.out  # output and error file name (%j=jobID)
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
