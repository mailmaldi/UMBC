README:
This is a gradle project (think maven but no xml) & the build script compiles it for Java 1.7!
To build a jar, from a linux/mac machine,
just goto MIPSACA folder and run the following

gadlew is the wrapper for gradle binary, so it needs to be an executable
chmod +x gradlew

The following command will clean, compile and build a jar of my code
./gradlew clean eclipse build assemble jar

If you have gradle installed on your machine (awesome!), then just
gradle clean eclipse build assemble jar

The jar is found in build/libs

To execute, simply do (paths are relative):
java -jar MIPSACA-1.0.jar inst.txt data.txt reg.txt config.txt results.txt

I recommend copying the jar to the folder where you want to test

BUILD:

NOTES AND ASSUMPTIONS IN CODE:
- DIVD can have a divide by zero, i'm just going to log it rather than cease execution