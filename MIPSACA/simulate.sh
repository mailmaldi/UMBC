#! /bin/sh
#echo $0 $1 $2 $3 $4 $5 $6
if test $# -lt 5; then
    echo "Not enough arguments"
    echo "Usage: simulator inst.txt data.txt reg.txt config.txt result.txt (optional)PIPELINE|MEMORY"
    echo
    echo "Please ensure the order of files for correct results"
    echo
    exit 1
fi
MODE=${6:-"MEMORY"}
#echo $MODE
java -jar MIPSACA-1.0.jar "$1" "$2" "$3" "$4" "$5" $MODE > debug.log 2>&1 