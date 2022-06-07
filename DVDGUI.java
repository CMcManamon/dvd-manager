import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses JOptionPane to display the menu of command choices. 
 */

public class DVDGUI extends JFrame  {
		private DVDCollection dvdlist;
		private JButton addButton, removeButton, exitButton, saveChangesButton;
		private JLabel totalTimeLabel, titleLabel, titleText, ratingLabel, durationLabel,
				ratingSelectLabel, starLabel, reviewSelectLabel, filmImage, feedbackArea;
		private JTextField durationText;
		private JComboBox ratingBox, ratingSelectBox, starBox, reviewSelectBox;
		private JList dvdJList;
		private DefaultListModel defaultList = new DefaultListModel();
		private JScrollPane dvdScroller;
		private String[] filmRating = DVD.VALID_RATINGS;
		private static final int ALL = DVD.VALID_RATINGS.length;
		private String[] filmStars = {"No Reviews", "*", "**", "***", "****", "*****"};
		private ListenForButton lForButton = new ListenForButton();
		private ArrayList<DVD> dvds = new ArrayList<DVD>();
		private int selectedRating = ALL;


	public DVDGUI()
	 {
		dvdlist = new DVDCollection();

		// Request file name
		String fileName = JOptionPane.showInputDialog("Enter name of data file to load");

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("DVD Manager");
		this.setResizable(false);

		JPanel thePanel = new JPanel();
		thePanel.setLayout(new GridBagLayout());


		// Selector for DVD ratings
		 JPanel ratingSelectPanel = new JPanel();
		 ratingSelectLabel = new JLabel("Show DVDs by Rating: ");
		 ratingSelectBox = new JComboBox(filmRating);
		 ratingSelectBox.addItem(makeObj("All"));
		 ratingSelectBox.setSelectedIndex(ALL);
		 ratingSelectBox.addItemListener(new ItemListener() {
			 @Override
			 public void itemStateChanged(ItemEvent e) {
				 selectedRating = ratingSelectBox.getSelectedIndex();
				 refreshDVDList();
			 }
		 });

		 ratingSelectPanel.add(ratingSelectLabel);
		 ratingSelectPanel.add(ratingSelectBox);
		 addComp(thePanel, ratingSelectPanel, 0,  0, 1, 1, 0, 0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);

		// set up the list of dvds
		dvdJList = new JList(defaultList);
		dvdJList.setVisibleRowCount(16);
		dvdJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dvdJList.addListSelectionListener(new ListenForList());
		dvdScroller = new JScrollPane(dvdJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dvdScroller.setPreferredSize(new Dimension(280, 400));
		addComp(thePanel, dvdScroller, 0, 1, 1, 10, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH);

		 // Blank spacer
		 JPanel blankPanel = new JPanel();
		 blankPanel.setPreferredSize(new Dimension(280, 400));
		 blankPanel.setOpaque(false);
		 Border blankBorder = BorderFactory.createLineBorder(Color.BLACK);
		 blankPanel.setBorder(BorderFactory.createCompoundBorder(blankBorder,
				 BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		 addComp(thePanel, blankPanel, 1,  1, 3, 10, 1, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH);

		 JPanel blankPanel2 = new JPanel();
		 blankPanel2.setPreferredSize(new Dimension(1, 400));
		 addComp(thePanel, blankPanel2, 1,  1, 1, 10, 0, 0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL);

		 // Title
		 titleLabel = new JLabel("Title: ");
		 addComp(thePanel, titleLabel, 2, 1, 2, 1, 0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE);
 		 titleText = new JLabel(" ");
		 //titleText.setMinimumSize(titleText.getPreferredSize());
		 addComp(thePanel, titleText, 2,  2, 2, 1, 0,0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);

		 // Rating
		 ratingLabel = new JLabel("Rating: ");
		 addComp(thePanel, ratingLabel, 2, 3, 1, 1, 0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE);
		 ratingBox = new JComboBox(filmRating);
		 ratingBox.setSelectedIndex(5);
		 addComp(thePanel, ratingBox, 2,  4, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);

		 // Star Reviews
		 starLabel = new JLabel("Reviews: ");
		 addComp(thePanel, starLabel, 2, 5, 1, 1, 0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE);
		 starBox = new JComboBox(filmStars);
		 starBox.setSelectedIndex(0);
		 addComp(thePanel, starBox, 2,  6, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);

		 // Duration
		 durationLabel = new JLabel("Duration: ");
		 addComp(thePanel, durationLabel, 2, 7, 1, 1, 0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE);
		 durationText = new JTextField(5);
		 addComp(thePanel, durationText, 2,  8, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
		 durationText.setMinimumSize(durationText.getPreferredSize());

		 // Film Image
		 filmImage = new JLabel("");
		 filmImage.setIcon(new ImageIcon("img\\no_image.jpg"));
		 filmImage.setPreferredSize(new Dimension(150, 225));
		 Border filmBorder = BorderFactory.createLineBorder(Color.BLACK);
		 blankPanel.setBorder(BorderFactory.createCompoundBorder(filmBorder,
				 BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		 addComp(thePanel, filmImage, 3, 3, 1, 6, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE);

		 // Save Changes Button
		 saveChangesButton = new JButton("Save Changes");
		 saveChangesButton.addActionListener(lForButton);
		 addComp(thePanel, saveChangesButton, 2,  9, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);


		 // display total running time under dvd list
		totalTimeLabel = new JLabel("Total Running Time: ");
		addComp(thePanel, totalTimeLabel, 0,  11, 4, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);

		 feedbackArea = new JLabel("");
		 feedbackArea.setText("DVD Manager Initialized");
		 feedbackArea.setBackground(Color.LIGHT_GRAY);
		 Border border = BorderFactory.createLineBorder(Color.BLACK);
		 feedbackArea.setBorder(BorderFactory.createCompoundBorder(border,
				 BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		 addComp(thePanel, feedbackArea, 0,  12, 4, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL);

		JPanel buttonPanel = new JPanel();
		addButton = new JButton("Add DVD");
		addButton.addActionListener(lForButton);
		removeButton = new JButton("Remove DVD");
		removeButton.addActionListener(lForButton);
		exitButton = new JButton("Exit and Save");
		exitButton.addActionListener(lForButton);

		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(exitButton);

		 addComp(thePanel, buttonPanel, 0,  13, 4, 1, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE);

		this.add(thePanel);
		this.setVisible(true);

		 try {
			 dvdlist.loadData(fileName);
			 refreshDVDList();
		 }
		 catch (Exception e) {
			 feedbackArea.setText(e.toString());
		 }

		 updateInfoPanel(-1); // disables info panel initially
		 updateTotalRunTime();

	 }

	private void doAddOrModifyDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();
		
		// Request the rating
		String rating;
		JComboBox ratingBox = new JComboBox(filmRating);
		ratingBox.setSelectedIndex(5);
		int input = JOptionPane.showConfirmDialog(this, ratingBox, "Enter rating for " + title, JOptionPane.DEFAULT_OPTION);
		if (input == JOptionPane.OK_OPTION)
			rating = (String)ratingBox.getSelectedItem();
		else
			return;		// dialog was cancelled
		
		// Request the running time
		String time = JOptionPane.showInputDialog("Enter running time for " + title);

		// Request the star reviews
		String stars;
		JComboBox getStarBox = new JComboBox(filmStars);
		getStarBox.setSelectedIndex(0);
		int starInput = JOptionPane.showConfirmDialog(this, getStarBox, "Enter star reviews for " + title, JOptionPane.DEFAULT_OPTION);
		if (starInput == JOptionPane.OK_OPTION)
			stars = String.valueOf(getStarBox.getSelectedIndex());
		else
			return;		// dialog was cancelled

		try {
			// Add or modify the DVD (assuming the rating and time are valid
			dvdlist.addOrModifyDVD(title, rating, time, stars);
			dvdlist.save();
			refreshDVDList();
			updateTotalRunTime();

			// Feedback to user
			feedbackArea.setText("Adding/Modifying: " + title + ", " + rating + ", " + time + ", " + stars);
		}
		catch (DVDPropertyException e) {
			feedbackArea.setText(e.toString());
		}
	}
	
	private void doRemoveDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();
		try {
			// Remove the matching DVD if found
			dvdlist.removeDVD(title);
			dvdlist.save();
			refreshDVDList();
			updateTotalRunTime();

			// Feedback to user
			feedbackArea.setText("Removed " + title);
		}
		catch (Exception e) {
			feedbackArea.setText(e.toString());
		}
	}


	private void doSave() {
		
		dvdlist.save();
		
	}

	private void saveChangesToDVD() {
		String title = titleText.getText();
		String time = durationText.getText();
		String rating = (String)ratingBox.getSelectedItem();
		String stars = String.valueOf(starBox.getSelectedIndex());

		try {
			// Add or modify the DVD (assuming the rating and time are valid
			dvdlist.addOrModifyDVD(title, rating, time, stars);
			dvdlist.save();
			refreshDVDList();
			updateTotalRunTime();

			// Feedback to user
			feedbackArea.setText("Modifying: " + title + ", " + rating + ", " + time + ", " + stars);
		}
		catch (DVDPropertyException e) {
			feedbackArea.setText(e.toString());
		}
	}

	// Listeners
	private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == exitButton) {
				doSave();
				System.exit(0);
			}
			else if (e.getSource() == addButton) {
				doAddOrModifyDVD();
			}
			else if (e.getSource() == removeButton) {
				doRemoveDVD();
			}
			else if (e.getSource() == saveChangesButton) {
				saveChangesToDVD();
			}

		}
	}

	private class ListenForList implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				int index = dvdJList.getSelectedIndex();
				updateInfoPanel(index);
			}
		}
	}


	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth,
						 int compHeight, int weightX, int weightY, int place, int stretch) {

		GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = weightX;
		gridConstraints.weighty  = weightY;
		gridConstraints.insets = new Insets(5,5,5,5);
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);
	}

	private void updateInfoPanel(int index) {
		if (index >= 0 && index < dvds.size()) {
			titleText.setText(dvds.get(index).getTitle());
			String rating = dvds.get(index).getRating();
			int stars = dvds.get(index).getStars();
			ratingBox.setSelectedIndex(Arrays.asList(filmRating).indexOf(rating));
			ratingBox.setEnabled(true);
			starBox.setSelectedIndex(stars);
			starBox.setEnabled(true);
			durationText.setText(String.valueOf(dvds.get(index).getRunningTime()));
			durationText.setEnabled(true);
			saveChangesButton.setEnabled(true);
			filmImage.setIcon(getImage(dvds.get(index).getTitle()));
		}
		else {
			titleText.setText(" ");
			ratingBox.setSelectedIndex(5);
			ratingBox.setEnabled(false);
			starBox.setSelectedIndex(0);
			starBox.setEnabled(false);
			durationText.setText("");
			durationText.setEnabled(false);
			saveChangesButton.setEnabled(false);
			filmImage.setIcon(getImage(null));
		}
	}

	private ImageIcon getImage(String title) {
		ImageIcon image;
		image = new ImageIcon("img\\" + title + ".jpg");

		if (image.getImageLoadStatus() != MediaTracker.COMPLETE)
			image = new ImageIcon("img\\no_image.jpg");
		return image;
	}

	private void updateTotalRunTime() {
		int totalTime;
		if (selectedRating == ALL)
			totalTime = dvdlist.getTotalRunningTime();
		else {
			totalTime = 0;
			for (DVD dvd : dvds) {
				totalTime += dvd.getRunningTime();
			}
		}

		int minutes = totalTime % 60;
		int hours = totalTime / 60;
		totalTimeLabel.setText("Total Running Time: " + hours + "h " + minutes + "m");
	}

	private void refreshDVDList() {
		if (selectedRating < 0 || selectedRating > DVD.VALID_RATINGS.length)
			feedbackArea.setText("Something went wrong.");

		// get all dvds
		DVD[] dvdArray = dvdlist.getDVDs();

		defaultList.clear(); // clear existing list
		dvds.clear(); // clear array list

		// add dvds to gui list
		if (selectedRating == ALL) {
			for (int i = 0; i < dvdlist.getNumDVDs(); i++) {
				dvds.add(dvdArray[i]);
				defaultList.addElement(dvdArray[i].getTitle());
			}
		}
		else { // picked rating
			for (int i = 0; i < dvdlist.getNumDVDs(); i++) {
				if (dvdArray[i].getRating().equals(filmRating[selectedRating])) {
					dvds.add(dvdArray[i]);
					defaultList.addElement(dvdArray[i].getTitle());
				}
			}
		}

		updateTotalRunTime();
	}

	private Object makeObj(final String item)  {
		return new Object() { public String toString() { return item; } };
	}
}
