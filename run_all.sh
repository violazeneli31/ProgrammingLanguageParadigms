#!/usr/bin/env bash
# Runs the Python, Kotlin and Scala versions and checks that each prints tests/expected_output.txt.
# A language is skipped if its compiler is not installed.
cd "$(dirname "$0")"
mkdir -p build
status=0

check() {   # $1 = language name, $2 = output of the program
    if diff <(echo "$2" | tail -n +2) tests/expected_output.txt > /dev/null; then
        echo "$1: OK - output matches tests/expected_output.txt"
    else
        echo "$1: DIFFERENT output"; status=1
    fi
}

if command -v python3 > /dev/null; then
    python3 -m unittest discover -s tests -q || status=1
    check "Python" "$(python3 code/python/hof_currying.py)"
else echo "Python: skipped (python3 not installed)"; fi

if command -v kotlinc > /dev/null; then
    kotlinc code/kotlin/HofCurrying.kt -include-runtime -d build/HofCurrying.jar 2> /dev/null
    check "Kotlin" "$(java -jar build/HofCurrying.jar)"
else echo "Kotlin: skipped (kotlinc not installed)"; fi

if command -v scala-cli > /dev/null; then
    check "Scala" "$(scala-cli run code/scala/HofCurrying.scala -q 2> /dev/null)"
elif command -v scalac > /dev/null; then
    scalac -d build code/scala/HofCurrying.scala
    check "Scala" "$(scala -classpath build HofCurrying 2> /dev/null || scala run code/scala/HofCurrying.scala -q 2> /dev/null)"
else echo "Scala: skipped (scala-cli / scalac not installed)"; fi

exit $status
