README:
This is a gradle project (think maven but no xml)
To build a jar, from a linux/mac machine,
just goto MIPSACA folder and run the following
./gradlew build
./gradlew assemble
./gradlew fatJar

this should resolve all dependencies and create 2 jar files in the MIPSACA/build/libs folder


BUILD:

NOTES AND ASSUMPTIONS IN CODE:
- DIVD can have a divide by zero, i'm just going to log it rather than cease execution