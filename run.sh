#!/usr/bin/env bash


info() {
	echo ""
	echo "---------------------------"
	echo $1
	echo "---------------------------"
}


# Checking Scala version.
info "Chekcing Scala. (2.11)"
scala -version

# Entering src dir.
info "Entering src/ dir."
cd src/


# Checking Permissions.i
info "Checking permission."
chmod a+x wordCount.scala
chmod a+x util.scala
chmod a+x runningMedian.scala


# Compiling files.
info "Compiling files."
scalac util.scala wordCount.scala runningMedian.scala 


# Executing Word Count
info "Executing Word Count":
scala cc.wordcount.WordCount # "../wc_input/stephen-hawking-quotes" "hawking-quotes.output"

# Executing Medians:
info "Executing Running Medians:"
scala cc.median.RunningMedian


info "Done."
