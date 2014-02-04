package umbc.practice.strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplaceSpaces {

	private static final Logger logger = LoggerFactory
			.getLogger(ReplaceSpaces.class);

	// Problem statement: Replace blank spaces in the middle of a
	// string/char-array with \n i.e. the 2 chars \ and n
	// assume enough space at the end

	public static char[] replaceSpaces(char[] array) {
		// 2 iterations, 1st gets num of spaces, last char's index and size
		// remaining at end
		// 2nd starts from last char's index (if +ve)

		// first iteration
		int numSpaces = 0;
		int spacesContinuous = 0;
		int lastCharIndex = -1;

		for (int i = 0; i < array.length; i++) {

			if (array[i] == ' ') {
				spacesContinuous++; // if blank, just incr cont count
			} else {
				lastCharIndex = i; // non blank char found, so this is the
									// latest char we've encountered
				numSpaces += spacesContinuous; // so numspaces has to be
												// incremented
				spacesContinuous = 0; // and the chain reset to 0
			}
		}
		logger.info("input array = '{}'", new String(array));
		logger.info("lastCharIndex = {} numSpaces = {} spacesContinuous = {}",
				lastCharIndex, numSpaces, spacesContinuous);

		if (lastCharIndex < 0) {
			logger.info("Input is a blank char array");
		} else if (numSpaces > spacesContinuous) {
			logger.info("Input does not have enough space at the end to replace");
		} else if (numSpaces == 0) {
			logger.info("There are no blank spaces to be replaced");
		} else {

			// 2nd iteration
			for (int i = lastCharIndex + numSpaces, j = lastCharIndex; numSpaces > 0; i--, j--) {
				if (array[j] == ' ') {
					array[i--] = 'n';
					array[i] = '\\';
					numSpaces--;
				} else {
					array[i] = array[j];
				}
			}

		}

		logger.info("output array = '{}'", new String(array));

		return array;
	}

	public static void main(String[] args) {

		char[] arr1 = { ' ', 'a', 'b', 'c', ' ', ' ', 'd', 'e', ' ', 'f', ' ',
				' ', ' ', ' ' };
		replaceSpaces(arr1);

		char[] arr2 = {};
		replaceSpaces(arr2);

		char[] arr3 = { ' ', ' ', ' ', ' ', ' ' };
		replaceSpaces(arr3);

	}

}
