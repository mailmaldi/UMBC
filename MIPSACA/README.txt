README:
This is a gradle project (think maven but no xml) & the build script compiles it for Java 1.7!
I have not tested it with Java 1.5 or 1.6 , but doing so should be easy by changing source compatibility to 1.5/1.6 in the build.gradle file.

I tested this project before submitting on linuxserver1.cs.umbc.edu (gl.umbc.edu ssh connection for me is very slow)
Both have the same java version, so hopefully you should not run into any issues running this project.

Please be patient with building the first time, since the build script will first download the gradle build utility and then compile the jar.

============================================================================================================================

BUILDING THE SOURCE:

There are 2 shell files included: build.sh & simulate.sh
They are both executables. If per chance they are not, then please make them so.
chmod +x build.sh
chmod +x simulate.sh

The build.sh will clean, compile and build a jar (MIPSACA-1.0.jar) and also copy it to the base directory.
e.g.
./build.sh

The simulate.sh takes the 5 files as parameters and executes the jar passing them the parameters:
e.g.
./simulate.sh inst.txt data.txt reg.txt config.txt results.txt
 
 You may optionally send an additional parameter PIPELINE, to execute simulation without memory hierarchy
 e.g.
 ./simulate.sh inst.txt data.txt reg.txt config.txt results.txt PIPELINE
 
 The simulate script also redirects stdout & stderr to a file called debug.log
 
 FOLLOW THESE STEPS IF THE SIMULATION SHELL FILES DID NOT WORK:

I'm including the jar that I compiled myself but following steps are used
To build a jar, from a linux/mac machine, just goto MIPSACA folder and follow these steps;

0. Make sure that the JAVA_HOME environment path is available on your machine!
or at the very least java and javac are available in your machine's PATH.
This exists on linuxserver1.cs.umbc.edu & gl.umbc.edu, so ignore this step if on these machines.

1. gadlew is the wrapper for gradle binary, so it needs to be an executable
chmod +x gradlew

2. The following command will clean, compile and build a jar of my code
./gradlew clean eclipse build assemble jar

If you have gradle installed on your machine (awesome!), then just
gradle clean eclipse build assemble jar

3. The jar is found in build/libs directory,
To execute, simply do (paths are relative):
java -jar MIPSACA-1.0.jar inst.txt data.txt reg.txt config.txt results.txt

I recommend copying the jar to the folder where you want to test the input files

===============================================================================================================================

NOTES AND ASSUMPTIONS IN CODE:
- DIVD can have a divide by zero, i'm just going to log it rather than cease execution
- data.txt Parser finishes on encountering first empty line or EOF
- In a pipelined Functional Unit, like FPAdd, etc. If the last instruction cannot goto next stage, only that instr is marked with Structual hazard, the previous ones are not!
Example, if size[4] Pipeline has [ NOOP, I4, I5 , I6] , only I6 is marked struct hazard
I have written code to mark all 3 Stuct hazard as well, but I've commented it , since I think its incorrect to.
- Please Note that even though the 2nd HALT is fetched, it will never be issued, and since it will never be issued , I never set its exit cycle. Hence the blank in the output.
The entry cycle is marked, but since we're printing only exit cycle, an instruction that is never issued doesnt have the exit cycle
- And others...