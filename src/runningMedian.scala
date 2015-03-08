package cc.median

import cc.util.{text, info, arguments, SortedList}

import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.collection.mutable.PriorityQueue
import scala.io.Source

import java.io.File
import java.io.PrintStream




/**
 * Class for running the median of words for each line in a text file.
 *
 * @contructor calculates the median of wordsper line in a text file.
 */
class RunningMedian {

	/**
	 * Median of words per line. Each list index contains the number of words of its respective line. 
	 * It is updated every time we read a new line of a file, by incrementing the list size.
	 *
	 * Example:
	 * up to the 0th row -> median is median[0].
	 * up to the 1th row -> median is median[1].
	 * (...)
	 * up to the nth row -> median is median[n].
	 *
	 */
	var median = List[Double]()


	/**
	 * Sorted list representing the number of words for each line.
	 * As the list is sorted, we provide a constant-time  element access
	 * every time we need to get the meadian of words from the
	 * lines read so far.
	 * As we make N access costing O(1) and N insertions costing approximately O(logN) each, 
	 * the list structure does not affect running time so much. The total complexity
	 * is approximately O(NlogN) (disconsidering List.splitAt()), due to the insertions calls.
	 * Each element of this list represents the number of words of its respective line.
	 *
	 * SortedList.median() gives us the list median.
	 */
	var sortedMedians = new SortedList()


	/** @verrides toString. */
	override def toString = median.mkString("\n")


	/**
	 * Counts the number of words in a given text line.
	 * @param line The text line to be processed.
	 * @return the number fo words.
	 */
	private def wordCount(line: String): Int = text.clean(line).split("\\W+").length


	/**
	 * Helper function to calculates the median of words per line up to the given line.
	 * It first counts the number of words and then uses a sorted list to help keep
	 * tracking the current median given the text line.
	 * @param line The line to be processed.
	 * @return the median of words per line up to the given line.
	 * @private
	 */
	private def getMedian(line: String): Double = {
		// Calculates the number of words in this line and add to medians list.
		sortedMedians.add(wordCount(line))

		// Returns the words median up to the current line.
		sortedMedians.median()	
	}


	/**
	 * Lazily calculates the median of words per line given a list of lines.
	 * @param lines The text file list of lines.
	 * @return a list containing the medians of each line.
	 */
	private def runMedian(lines: Iterator[String]): List[Double] = {
		// For each non empty line, calculates the current median up to it.
		// Once all lines are processed, it returns a list representation of the medians
		// per line.
		lines.filter(!_.isEmpty).map(x => getMedian(x)).toList
	}


	/**
	 * Lazily reads a sorted set of input files from a directory
	 * and calculates the median of words per line.
	 * @param dir The directory containging the input files.
	 * @param folder The folder containinng the input files.
	 */
	def runFromDir(dir: File, folder: String) = {
		// Combine all files from the given directory in sorted order.
		info.print("Getting and combining input files' stream...")
		val filesStream: Iterator[String] = new File(dir, folder).listFiles  		// Get the dir files.
				.sortBy(f => (f.getName))									 		// Sort the files.
				.foldLeft(Iterator[String]())( _ ++ Source.fromFile(_).getLines())  // Combine their lines.

		// Calculates the lines medians.
		info.print("Calculating medians...")
		median = runMedian(filesStream)
	}
}



/**
 * Factory for [[RunningMedian]] instances.
 */
object RunningMedian {
	def main (args: Array[String]) {
		// Output file.
		var path = if (args.length > 0) "../wc_output/" + args(0) else "../wc_output/med_result.txt"
		val output = new PrintStream(path, "UTF-8")

		// Create a medians tracker.
		val median = new RunningMedian()

		// Calculates the medians of all files from dir.
		median.runFromDir(new File("../"), "wc_input")

		// Print median result to output file.
		info.print("Saving Medians in 'output' file...")

		output.print("Medians:\n")
		output.print(median.toString)
	}	
}