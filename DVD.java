public class DVD {

	public static final String[] VALID_RATINGS = {"G", "PG", "PG-13", "R", "NC-17", "UNRATED"};

	private String title;		// Title of this DVD
	private String rating;		// Rating of this DVD
	private int runningTime;	// Running time of this DVD in minutes
	private int stars;			// Reviews from one * to five ***** stars

	public DVD(String dvdTitle, String dvdRating, int dvdRunningTime, int dvdStars)
	{
		title = dvdTitle;
		rating = dvdRating;
		runningTime = dvdRunningTime;
		stars = dvdStars;
	}


	public static boolean isValidRating(String rating) {
		for (String s : VALID_RATINGS)
			if (s.equalsIgnoreCase(rating))
				return true;
		return false;
	}

	public static String validRatings() {
		String allRatings = "";
		for (String s : VALID_RATINGS)
			allRatings += s + " ";
		return allRatings;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public String getRating() 
	{
		return rating;
	}
	
	public int getRunningTime() 
	{
		return runningTime;
	}

	public void setTitle(String newTitle) {
		title = newTitle.toUpperCase();
	}

	public void setRating(String newRating) {
		rating = newRating;
	}

	public void setRunningTime(int newRunningTime) {
		runningTime = newRunningTime;
	}

	public String toString() {
		return title + "/" + rating + "/" + runningTime + "min" + "/" + stars;
	}


	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		if (stars <= 5 && stars >= 0)
			this.stars = stars;
		else this.stars = 0; // unrated
	}
}
