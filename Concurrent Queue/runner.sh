#!/bin/bash
#SBATCH -J Project3      # job name
#SBATCH -o Project3.out  # output and error file name (%j=jobID)
#SBATCH -n 32           # total number of cpus requested
#SBATCH -p development  # queue -- normal, development, etc.
#SBATCH -t 00:30:00     # run time (hh:mm:ss) - 1.5 hours
#SBATCH --mail-user=gxe150130@utdallas.edu
#SBATCH --mail-type=begin  # email me when the job starts
#SBATCH --mail-type=end    # email me when the job finishes
cd "$HOME/MCP P03"
javac ApplicationRunner.java

echo "50,50,0 0"
java ApplicationRunner 32 1000000 50,50,0 0

echo "40,40,20 0"
java ApplicationRunner 32 1000000 40,40,20 0

echo "50,50,0 1000"
java ApplicationRunner 32 1000000 50,50,0 1000

echo "40,40,20 1000"
java ApplicationRunner 32 1000000 40,40,20 1000
