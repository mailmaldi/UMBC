README:
This is a gradle project (think maven but no xml) & the build script compiles it for Java 1.7!
I have not tested it with Java 1.5 or 1.6 , but doing so should be easy by changing source compatibility to 1.5/1.6 in the build.gradle file.

I tested this project before submitting on linuxserver1.cs.umbc.edu (gl.umbc.edu ssh connection for me is very slow)
Both have the same java version, so hopefully you should not run into any issues running this project.

============================================================================================================================

BUILDING THE SOURCE:

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