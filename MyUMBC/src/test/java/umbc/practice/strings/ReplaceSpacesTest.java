package umbc.practice.strings;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReplaceSpacesTest {

	@Test
	public final void test1() {

		char[] expected = { '\\', 'n', 'a', 'b', 'c', '\\', 'n', '\\', 'n',
				'd', 'e', '\\', 'n', 'f' };
		char[] actual = { ' ', 'a', 'b', 'c', ' ', ' ', 'd', 'e', ' ', 'f',
				' ', ' ', ' ', ' ' };
		assertArrayEquals(expected, ReplaceSpaces.replaceSpaces(actual));
	}

	@Test
	public final void test2() {

		char[] expected = { ' ' };
		char[] actual = { ' ' };
		assertArrayEquals(expected, ReplaceSpaces.replaceSpaces(actual));
	}

	@Test
	public final void test3() {

		char[] expected = { ' ', ' ', ' ', ' ', ' ' };
		char[] actual = { ' ', ' ', ' ', ' ', ' ' };
		assertArrayEquals(expected, ReplaceSpaces.replaceSpaces(actual));
	}

	@Test
	public final void test4() {

		char[] actual = { ' ', 'a', 'b', 'c', ' ', ' ', 'd', 'e', ' ', 'f',
				' ', ' ', ' ' };
		char[] expected = { ' ', 'a', 'b', 'c', ' ', ' ', 'd', 'e', ' ', 'f',
				' ', ' ', ' ' };

		assertArrayEquals(expected, ReplaceSpaces.replaceSpaces(actual));
	}

}
