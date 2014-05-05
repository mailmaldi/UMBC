README:
This is a gradle project (think maven but no xml)
To build a jar, from a linux/mac machine,
just goto MIPSACA folder and run the following
./gradlew build
./gradlew assemble
./gradlew fatJar

If you have gradle installed on your machine (awesome!), then
gradle clean build assemble fatJar

this should resolve all dependencies and create 2 jar files in the MIPSACA/build/libs folder

 ../resources/main/inst.txt ../resources/main/data.txt ../resources/main/reg.txt ../resources/main/config.txt ../resources/main/result.txt

BUILD:

NOTES AND ASSUMPTIONS IN CODE:
- DIVD can have a divide by zero, i'm just going to log it rather than cease execution