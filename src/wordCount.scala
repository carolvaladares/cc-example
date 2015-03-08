package cc.wordcount 

import cc.util.{text, info, arguments}

import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.io.Source

import java.io.File
import java.io.PrintStream


/**
 * Class for counting word occurences in text file. The words are stored as hashmap keys and their counts
 * are stored as hashmap values.
 *
 * @constructor creates a map containing the words and their counts.
 */
class WordCount {

	/**
	 * Word counter map. Once initialized it can be updated through either {@code WordCount.update}
	 * or {@code WordCount.updateFromFile}.
	 *
	 * Example:
	 * counter[word] -> number of occurrences
	 *
	 * counter['love'] -> 21
	 * counter['is'] -> 34
	 * counter['enought'] -> 15
	 */
	var counter = Map[String, Int]()


	/** @verrides toString. Better way to make a string representation from a Map object. */
	override def toString = counter.map{ case(k, v) => k + " " + v}.toList.mkString("\n")


	/**
	 * Sort counter by keys.
	 */
	def sortKeys() = {
		counter = ListMap(counter.toSeq.sortBy(_._1):_*)
	}


	/**
	 * Helper function to count the word occurences from a list of sentences.
	 * @private
	 */
	private def wordCount(lines: Iterator[String]) = {
		lines.flatMap(text.clean(_).split("\\W+"))        	// Lazily turns lines into a flat map of words.
  			 .foldLeft(Map.empty[String, Int]){      		// Counts ocurrences folding over the words.
     			(count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
  			 } 
	}


	/**
	 * Helper function to update the map word counter.
	 * @param array The map of words just processed.
	 * @private
	 */
	private def update(currentMap: Map[String, Int]) = {
		// Appends new map to the counter by updating existing words values.
		counter = counter ++ currentMap.map{ case (k, v) => k -> (v + currentMap.getOrElse(k,0)) }
	}


	/**
	 * Update the map word counter by processing a set of text files.
	 * @param lines The list of text file to be processed.
	 */
	def updateFromFilesStream(lines: Iterator[String]) = {
		// Counts the word occurences from non empty lines.
		var currentMap = wordCount(lines.filter(!_.isEmpty))
	  			
	  	// Updates counter.
	  	info.print("Updating Counter...")
	  	update(currentMap)
	}

	/**
	 * Lazily reads a set of input files from a directory
	 * and calculates the frequence in which each word appers.
	 * @param dir The directory containging the input files.
	 * @param folder The folder containinng the input files.
	 */
	def runFromDir(dir: File, folder: String) = {
		// Combine all files from the given directory in sorted order.
		info.print("Getting and combining input files' stream...")
		val filesStream: Iterator[String] = new File(dir, folder).listFiles  		// Get the dir files.
				.foldLeft(Iterator[String]())( _ ++ Source.fromFile(_).getLines())  // Combine their lines.

		// Calculates the words frequency.filesStream
		info.print("Calculating word count...")
		updateFromFilesStream(filesStream)
	}	
}



/**
 * Factory for [[WordCount]] instances.
 */
object WordCount {
	def main (args: Array[String]) {
		// Output file.
		var path = if (args.length > 0) "../wc_output/" + args(0) else "../wc_output/wc_result.txt"
		val output = new PrintStream(path, "UTF-8")

		// Create new word counter.
		var counter = new WordCount()

		// Update counter.
		counter.runFromDir(new File("../"), "wc_input")

		// Sort counter by keys.
		info.print("Sorting Counter by key...")
		counter.sortKeys

		// Print counter result to output file.
		info.print("Saving Counter in 'output' file...")

		output.print("Word count:\n")
		output.print(counter.toString)
	}
}