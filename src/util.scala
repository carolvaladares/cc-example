package cc.util 

import scala.io.Source
import java.io.File


/**
 * Class to maintain a sorted list upon new element additions.
 * It also return the list median.
 * It keeps a list sorted by using binary search to find the right
 * position of new elements.
 *
 * @constructor maintain a list sorted at every insertion.
 */
class SortedList {

	/**
	 * Sorted list. In order to facilitate the addition of new elements, 
	 * this list contains sentinels values at the first and last positions.
	 * Thus, every time we insert a new value to this list, we guarantee 
	 * its consistency.
	 * Since we are dealing with sentinels, this is private. Thus, 
	 * we can only access the list content through {@code SortedList.median()}
	 * @private
	 */
	private var list = List[Int](Int.MinValue, Int.MaxValue)


	/**
	 * Returns the list median.
	 * If list is even, the middle element is returned. Otherwise, 
	 * the mean of the 2 elements from the middle is returned.
	 * @return the list median.
	 */
	def median(): Double = {
		if (list.length % 2 != 0) 
			list(list.length/2) 
		else 
			(list(list.length/2 - 1) + list(list.length/2)).toDouble/2
	}

	/**
	 * Adds a new element to the list at its sorted position.
	 * It accepts duplicates and has O(logN) complexity (considering List.splitAt()
	 * constant-time).
	 * @param item The element to be added to the sorted list.
	 */
	def add(item: Int) = {
		// Adds new element discondisering the list sentinels.
		add_(item, 0, list.length - 2)
	}

	/**
	 * Private function that adds a new element into the sorted list.
	 * It uses binary search algorithm to find the righ position of the new element.
	 * It accepts duplicates and has O(logN) complexity (considering List.splitAt()
	 * constant-time).
	 * @param item The element to be inserted.
	 * @param i The list start index.
	 * @param e The list end index.
	 * @private
	 */
	private def add_(item: Int, i: Int, e: Int): Boolean = {
		// Return if start position is greater then the end.
		if (i > e) {
			return false
		}

		// The median.
		val m = (i + e)/2

		// If list(m) is smaller than item and list(m + 1) is greater,
		// insert item at position m.
		if (list(m) <= item && item <= list(m + 1)) {
			val (l, r) = list.splitAt(m + 1)
			list = l ++ List(item) ++ r
		} 
		// Binary search to find the right position of item.
		else if (list(m) < item) {
			add_(item, m + 1, e)
		} else {
			add_(item, i, m - 1)
		}
		return true
	}
}



/**
 * Helper object to pre-process a text. */
object text {
	/**
	 * Helper function to lowercase a word and remove punctuation.
	 */
	def clean(word: String): String  = word.trim.toLowerCase.replaceAll("[^a-zA-Z\\s]", "")
}



/**
 * Helper object to print console information.
 */
 object info {

 	/**
 	 * Prints information colored text. Green.
 	 * @param text Text to be printed.
 	 */
	def print(text: String) = {
		println(Console.GREEN +  "\n" + text + Console.RESET)
	}

 	/**
 	 * Prints error colored text. Red.
 	 * @param text Text to be printed.
 	 */
	def error(text: String) = {
		println(Console.RED +  "\n" + text + Console.RESET)
	}
}