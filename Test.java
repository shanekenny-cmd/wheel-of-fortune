/* Test.java
 */


public class Test {
	public static void main(String [] args) {
		int[][] nums = new int[5][5];
		int k = 0, w = 5;
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				System.out.println("");
				System.out.println("j: " + j + " i: " + i + " k: " + k);
				System.out.println("x: " + (k % w) + " y: " + (k / w));
				k++;
			}
		}

		int padding = 10, unitSize = 25;
		w = 600 - (2 * padding) - unitSize;
		for (int i = 0; i < 26; i++) {
			System.out.println("x: " + (padding + ((unitSize * i) % w)) + " y: " + ((unitSize * i) / w));
		}
	}
}