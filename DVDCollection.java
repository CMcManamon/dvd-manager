import java.io.*;
import java.util.Scanner;

public class DVDCollection {
	final int DVD_ARRAY_SIZE_INIT = 7;

	// Data fields
	/** The current number of DVDs in the array */
	private int numdvds;
	
	/** The array to contain the DVDs */
	private DVD[] dvdArray;
	
	/** The name of the data file that contains dvd data */
	private String sourceName;
	
	/** Boolean flag to indicate whether the DVD collection was
	    modified since it was last saved. */
	private boolean modified;
	
	/**
	 *  Constructs an empty directory as an array
	 *  with an initial capacity of 7. When we try to
	 *  insert into a full array, we will double the size of
	 *  the array first.
	 */
	public DVDCollection() {
		numdvds = 0;
		dvdArray = new DVD[DVD_ARRAY_SIZE_INIT];
	}
	
	public String toString() {
		// Return a string containing all the DVDs in the
		// order they are stored in the array along with
		// the values for numdvds and the length of the array.
		String dvdInfo = "numdvds = " + numdvds + "\n";
		dvdInfo += "dvdArray.length = " + dvdArray.length + "\n";
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i] == null)
				break;
			dvdInfo += "dvdArray[" + i + "] = " + dvdArray[i].toString() + "\n";
		}

		return dvdInfo;
	}

	public void addOrModifyDVD(String title, String rating, String runningTime, String review) {
		// Valid title? comma used as delimiter in data file
		if (title.equals("") || title.contains(","))
			throw new DVDPropertyException("Invalid Title");

		// Convert running time to integer
		// Check that time is valid
		int time;
		try {
			long t = Integer.parseInt(runningTime);
			if (t > 0 && t <= Integer.MAX_VALUE)
				time = Integer.parseInt(runningTime);
			else throw new DVDPropertyException("Invalid DVD Running Time");
		}
		catch (NumberFormatException e) {
			throw new DVDPropertyException("Invalid DVD Running Time");
		}

		// Test for valid rating
		if (!DVD.isValidRating(rating))
			throw new DVDPropertyException(rating + " is not a recognized movie rating");

		// Reviews
		int stars;
		try {
			int s = Integer.parseInt(review);
			if (s >= 0 && s <= 5)
				stars = s;
			else throw new DVDPropertyException("Invalid DVD Reviews");
		}
		catch (NumberFormatException e) {
			throw new DVDPropertyException("Invalid DVD Reviews");
		}

		// If DVD exists, update rating and run time and review
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getTitle().equals(title)) {
				dvdArray[i].setRating(rating);
				dvdArray[i].setRunningTime(time);
				dvdArray[i].setStars(stars);
				modified = true;
				return;
			}
		}

		// Ensure there's space for a new DVD
		// Double the array size if it's full
		if (dvdArray.length == numdvds) {
			DVD[] temp = new DVD[dvdArray.length * 2];
			for (int i = 0; i < dvdArray.length; i++) {
				temp[i] = dvdArray[i];
			}
			dvdArray = temp;
		}

		// Add a new dvd to the list
		dvdArray[numdvds] = new DVD(title, rating, time, stars);
		// Swap the dvd up the array until it's ordered
		int currPos = numdvds;
		while (currPos > 0 &&
				dvdArray[currPos].getTitle().compareTo(dvdArray[currPos - 1].getTitle()) < 0) {
			DVD temp = dvdArray[currPos - 1];
			dvdArray[currPos - 1] = dvdArray[currPos];
			dvdArray[currPos] = temp;
			currPos--;
		}
		numdvds++;
		modified = true;

	}
	
	public void removeDVD(String title) {
		title = title.toUpperCase();
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getTitle().equals(title)) {
				// found the dvd, move dvds up
				for (int j = i + 1; j < numdvds; j++) {
					dvdArray[j - 1] = dvdArray[j];
				}
				numdvds--;
				modified = true;
				return;
			}
		}
		throw new DVDPropertyException("DVD not found. No changes made.");
	}
	
	public String getDVDsByRating(String rating) {
		String moviesWithRating = "";
		for (int i = 0; i < numdvds; i++) {
			if (dvdArray[i].getRating().equalsIgnoreCase(rating)) {
				moviesWithRating += dvdArray[i].toString() + "\n";
			}
		}

		if (moviesWithRating.equals(""))
			moviesWithRating = "No movies found with rating " + rating;

		return moviesWithRating;
	}

	public int getTotalRunningTime() {
		int total = 0;
		for (int i = 0; i < numdvds; i++)
			total += dvdArray[i].getRunningTime();

		return total;
	}

	
	public void loadData(String filename) {
		if (filename == null) {
			sourceName = "new_list.txt";
			throw new DVDPropertyException("No file entered. Initializing empty collection to " + sourceName);
		}

		sourceName = filename;
		File file = new File(filename);
		String dvdTitle, dvdRating, dvdRunningTime, dvdStars;
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				try {
				String data[] = scanner.nextLine().split(",");
				dvdTitle = data[0];
				dvdRating = data[1];
				dvdRunningTime = data[2];
				dvdStars = data[3];
				addOrModifyDVD(dvdTitle, dvdRating, dvdRunningTime, dvdStars);
				}
				catch (Exception e) {
					throw new DVDPropertyException("Corrupted data detected. Stopped initializing collection.");
				}
			}
			scanner.close();
			modified = false;
		}
		catch (FileNotFoundException e) {
			sourceName = "new_list.txt";
			throw new DVDPropertyException("File not found. Initializing empty collection to " + sourceName);
		}
	}
	
	public void save() {
		if (!modified)
			return; // Don't save if nothing changed
		File file = new File(sourceName);
		try {
			PrintWriter output = new PrintWriter(file);
			for (int i = 0; i < numdvds; i++) {
				output.println(dvdArray[i].getTitle() + "," +
						dvdArray[i].getRating() + "," +
						dvdArray[i].getRunningTime() + "," +
						dvdArray[i].getStars());
			}
			output.close();
		}
		catch (IOException e) {
			throw new DVDPropertyException("Error occurred saving to file.");
		}

	}

	public DVD[] getDVDs() {
		return dvdArray;
	}

	public int getNumDVDs() { return numdvds; }

	// Additional private helper methods go here:

}
