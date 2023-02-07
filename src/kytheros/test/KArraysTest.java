package kytheros.test;

import java.util.Arrays;

import kytheros.util.logger.Log;
import kytheros.util.misc.KArrays;

public class KArraysTest {

	public static void main(String[] args) {
		Integer[] arr_int = { 0, 1, 3, 2, 5, 4, 4, 6, 8, 7 };

		Log.println(Arrays.toString(arr_int));
		Log.println(Arrays.toString(KArrays.removeAll(arr_int, (Object) 4)));

	}

}
