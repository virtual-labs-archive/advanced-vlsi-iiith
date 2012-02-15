public class StringArray {

	public static void main(String args[]) {

		// This allocates the array but they are all null
		String arr2[] = new String[3];

		// here you start to stuff in the values.
		arr2[0] = "Zero";
		arr2[1] = "One";
		arr2[2] = "Two";

		System.out.println("\nString Array arr2...");
		for (int i = 0; i < arr2.length; i++) {
			System.out.println(arr2[i]);
		}
	}
}
