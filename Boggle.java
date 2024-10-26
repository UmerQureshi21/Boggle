//Name: Umer Qureshi
//Date: 01/22/2024
//Purpose: Spring Themed Boggle
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class Boggle extends Applet implements ActionListener
{
    int time = 200; // The amount of time the user has to make as many valid words as they can.
    int wordPoints = 0; // The points given for each word that the user makes.
    int totalPoints = 0; // The total points of every word that the user makes.
    int wordCounter = 0; // The amount of words that the user has submitted.
    int x = -2; // The x coordinate of the user's click on the grid.
    int y = -2; // The y coordinate of the user's click on the grid.
    int row = 4; // The amount of rows that the dice grid has.
    int col = row; // The amount of columns that the dice grid has.
    int amountOfPlayers = 0; // The amount of users that have played this game.

    Dice Dices[] [] = new Dice [row] [col]; // The 2D array of Dices.

    DiceStack diceStack = new DiceStack (); // The stack of dice used for unselecting and selecting.

    CardLayout cdLayout = new CardLayout (); // This is called when the program is switches to a new screen.

    JButton gameGrid[] [] = new JButton [row] [col]; // The 2D array of the face of each Dice that is facing up.

    JLabel timerDisplay = new JLabel (); // Displays the time.
    JLabel displayedWord = new JLabel (); // Displays the word letter by letter that the user makes on the top of the game screen.
    JLabel playerPoints = new JLabel (); // Displays the user's points.
    JLabel scoreTitle; // The title for the score screen.
    JLabel topFiveScores[] = {new JLabel (""), new JLabel (""), new JLabel (""), new JLabel (""), new JLabel ("") }; // An array to hold and display the scores of the top five players of all time.

    JTextArea words1 = new JTextArea (10, 30); // Lists the user's words on the game screen.
    JTextArea topScores = new JTextArea (10, 23); // Displays the name and score of every user who played this game.

    Panel p_card;  // Holds all of the screens.
    Panel card1, card2, card3, card4, card5, card6; // All of the different screens.
    Panel gridPanel; // Panel that stores the grid of Dice. It is global because it can be changed to 3x3 or 5x5.

    String dictionary[] = new String [370105]; // An array containing all the words in the dictionary. Some words are still not valid even though I have added the updated dictionary.
    String name; // The user's name that they want to be displayed as in the score screen.
    String gridDimensions = "4x4"; // Changes depending on if the user wants to play the 3x3, 4x4, or 5x5 version.
    String playerWord = ""; // The current word that the user makes.
    String playerWords[] = new String [50]; // An array of every word that the user makes to check if the word has been made by them before.

    Timer timer; // A time limit coded to give the user pressure because pressure makes diamonds.

    /*
    Grade 12 concepts:
    -Two objects
    -Stack of objects
    -char functions (length(), charAt, toLowerCase, trim)
    -dictionary
    -ORATE
    -Save and open from a file
    -Neighbours
    -List (Mrs. Gorski said I can claim list if I get a working leaderboard which I have)
    -Quicksort (sorting, swapping, recursion)
    -New widgets used (JTextArea, JScrollPane)
    */

    public void init ()
    { // All of the screens that will be displayed.
	p_card = new Panel ();
	p_card.setLayout (cdLayout);
	introductionScreen ();
	gameScreen ();
	readInDictionary (); // Reads in from the dictionary file.
	instructionsScreen ();
	settingsScreen ();
	letterValueScreen ();
	scoreScreen ();
	resize (400, 550); // Dimensions of the game.
	setLayout (new BorderLayout ());
	add ("Center", p_card);
    }


    public void createButton (JButton b, String actionCommand, int fontSize, int x, int y, boolean clickable, boolean border, boolean text)
    { // Created to have consistency among the buttons and reduce redundancy within the code.
	b.setActionCommand (actionCommand);
	b.addActionListener (this);
	if (text) // If the button is a text then it changes the font. Otherwise it is assumed that it is a picture button.
	    b.setFont (new Font ("Arial", Font.BOLD, fontSize));
	b.setBackground (new Color (255, 247, 211));
	b.setPreferredSize (new Dimension (x, y));
	b.setEnabled (clickable);
	b.setBorderPainted (border);
    }


    public void createLabel (JLabel l, String label, int fontSize, int x)
    { // Created to reduce redundancy within the code.
	l.setText (label);
	l.setPreferredSize (new Dimension (x, 30));
	l.setHorizontalAlignment (0);
	l.setFont (new Font ("Arial", Font.BOLD, fontSize));
    }


    public void textAreas (JTextArea jta, int fontSize, int R, int G, int B)
    { // Created to redundancy within the code.
	jta.setFont (new Font ("Arial", Font.BOLD, fontSize));
	jta.setForeground (Color.black);
	jta.setBackground (new Color (R, G, B));
	jta.setLineWrap (true);
	jta.setEditable (false);
    }


    public void introductionScreen ()
    { // The screen that shows when the user opens the game.
	card1 = new Panel (null);
	JButton OpenScreen = new JButton (createImageIcon ("openingscreen.png")); // Picture that displays the intro screen
	OpenScreen.setActionCommand ("play");
	OpenScreen.addActionListener (this);
	OpenScreen.setBounds (0, 0, 400, 550);
	card1.add (OpenScreen);
	p_card.add ("1", card1);
    }


    public void settingsScreen ()
    { // The screen where the user enters their name and what size grid they would like to play on.
	card6 = new Panel (null);
	JButton OpenScreen = new JButton (createImageIcon ("SettingsScreen.png")); // Picture that displays the setting screen
	OpenScreen.setActionCommand ("gridSizes");
	OpenScreen.addActionListener (this);
	OpenScreen.setBounds (0, 0, 400, 550);
	card6.add (OpenScreen);
	p_card.add ("6", card6);
    }


    public void gameScreen ()
    {
	card2 = new Panel ();
	card2.setBackground (new Color (193, 255, 114));
	textAreas (words1, 14, 193, 255, 114); // Formats the textArea.
	words1.append ("Your words: "); // Appends a label so the user understands the purpose of this textArea.

	Panel p1 = new Panel ();
	createLabel (displayedWord, "Your Word Here", 30, 400); // Formats the current word that the user makes.
	p1.add (displayedWord);
	Panel p2 = new Panel ();
	createLabel (timerDisplay, "Time: 200", 20, 200); // Formats the timer.
	createLabel (playerPoints, "Points: " + wordPoints, 20, 200); // Formats the player's points.
	p2.add (timerDisplay);
	p2.add (playerPoints);

	gridPanel = new Panel ();
	addGrid ();
	Panel p4 = new Panel ();

	JButton nextWord = new JButton ("Submit Word");
	createButton (nextWord, "nextWord", 30, 290, 30, true, false, true); // Formats the submit button.
	p4.add (nextWord);

	Panel p5 = new Panel ();
	JButton ins = new JButton ("Instructions");
	createButton (ins, "ins", 15, 145, 20, true, false, true); // Formats the instructions button.
	JButton shuffle = new JButton ("Reconfigure");
	createButton (shuffle, "shuffle", 15, 145, 20, true, false, true); // Formats the reset and shuffle button.
	p5.add (shuffle);
	p5.add (ins);
	// Adds all the panels and the JTextArea to the screen.
	card2.add (p1);
	card2.add (p2);
	card2.add (gridPanel);
	card2.add (p4);
	card2.add (p5);
	card2.add (words1);

	p_card.add ("2", card2);
    }


    public void instructionsScreen ()
    { // Explains to the user how to play the game.
	card3 = new Panel (null);
	boggleTimer ();
	JLabel ins = new JLabel (createImageIcon ("Instructions.png")); // Instructions image.
	ins.setBounds (0, 0, 400, 550);

	JButton valueScreen = new JButton ("Points");
	createButton (valueScreen, "valuescreen", 20, 20, 20, true, true, true); // Formats the points button.
	valueScreen.setBounds (285, 485, 100, 50); // Moves the button to the bottom right.
	card3.add (valueScreen);
	card3.add (ins);
	// Adding to the screen.
	p_card.add ("3", card3);
    }


    public void letterValueScreen ()
    { // Shows the user the various values associated with each letter.
	card4 = new Panel (null);
	JLabel l = new JLabel (createImageIcon ("LetterValueScreen.png"));
	l.setBounds (0, 0, 400, 550);
	JButton back2 = new JButton ("Back");
	createButton (back2, "ins", 25, 20, 20, true, true, true); // Formats the back button.
	back2.setBounds (280, 400, 100, 50); // Moves the button to its correct place.
	JButton play2 = new JButton ("Play!");
	createButton (play2, "play2", 25, 20, 20, true, true, true); // Formats the play button.
	play2.setBounds (280, 330, 100, 50); // Moves the button to its correct place.
	// Adding to the screen.
	card4.add (back2);
	card4.add (play2);
	card4.add (l);

	p_card.add ("4", card4);
    }


    public void scoreScreen ()
    { // Screen that displays the leaderboard for either 3x3, 4x4, or 5x5.
	card5 = new Panel ();
	card5.setBackground (new Color (193, 255, 114));

	for (int i = 0 ; i < 5 ; i++) // Formats the top five score labels.
	    createLabel (topFiveScores [i], "", 25, 400);
	scoreTitle = new JLabel ("Players and Their " + gridDimensions + " Scores");
	scoreTitle.setFont (new Font ("Arial", Font.BOLD, 25));

	Panel score = new Panel ();
	textAreas (topScores, 20, 255, 247, 211); // Formats the textArea.
	score.add (topScores);
	JScrollPane scrollPane = new JScrollPane (topScores); //https://www.geeksforgeeks.org/java-jscrollpane/ Adds a scroller to the TextArea.
	JButton replay = new JButton ("Exit to Play Again!");
	createButton (replay, "replay", 25, 290, 40, true, true, true); // Formats the button.

	card5.add (scoreTitle);
	card5.add (scrollPane);
	card5.add (score);
	// Adds the widgets and the scores to the screen.
	for (int i = 0 ; i < 5 ; i++)
	    card5.add (topFiveScores [i]);
	card5.add (replay);

	p_card.add ("5", card5);
    }


    public void boggleTimer ()
    { // Declares the timer and decreases it if it is not zero.
	ActionListener listener = new ActionListener ()
	{
	    public void actionPerformed (ActionEvent ae)
	    {
		if (time > 0)
		    time--;
		timerDisplay.setText ("Time: " + time); // Updates the new time.
		try
		{
		    Thread.sleep (500);
		}
		catch (InterruptedException m)
		{
		    System.out.println (m);
		    ;
		}
	    }
	}
	;
	timer = new Timer (250, listener);
    }


    public void addGrid ()
    { // Adds the game grid to the game screen. The user can change the dimensions which is why it is in a method so that it can be called if the user decides to do so.
	Panel grid = new Panel (new GridLayout (row, col)); // Grid to hold the Dice faces.
	int m = 0; // The action command for each grid button.
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		Dices [i] [j] = new Dice (i, j); // Declares and constructs a Dice with its position on the grid.
		gameGrid [i] [j] = new JButton (createImageIcon (Dices [i] [j].getPic ())); // Retreives the picture of that dice and places it on the grid.
		createButton (gameGrid [i] [j], "" + m, 20, 72, 72, true, false, false); // Formats the picture.
		grid.add (gameGrid [i] [j]); // Adds the picture to the grid.
		m++; // Increases to be used as a new action command for the next addition to the grid and Dices array.
	    }
	}
	gridPanel.add (grid);
    }


    public void letterRing (int i, int j)
    { // This method enables all neighbours of the user's most recently selected letter.
	for (int m = 0 ; m < row ; m++)
	{
	    for (int n = 0 ; n < col ; n++)
	    { // If a letter has not been pressed then set it to be unclickable.
		if (Dices [m] [n].getPressedAmount () == 0)
		    gameGrid [m] [n].setEnabled (false);
		else
		    gameGrid [m] [n].setEnabled (true);
	    }
	}
	if (Dices [i] [j].getClicked ())
	{ // If the user's current click selects a new letter, then every letter around the clicked one will be enabled true.
	    if (i - 1 >= 0 && j - 1 >= 0) // Top Left.
		gameGrid [i - 1] [j - 1].setEnabled (true);
	    if (i - 1 >= 0) // Top Middle.
		gameGrid [i - 1] [j].setEnabled (true);
	    if (i - 1 >= 0 && j + 1 < col) // Top Right.
		gameGrid [i - 1] [j + 1].setEnabled (true);
	    if (j - 1 >= 0) // Middle Left.
		gameGrid [i] [j - 1].setEnabled (true);
	    if (j + 1 < col) // Middle Right.
		gameGrid [i] [j + 1].setEnabled (true);
	    if (i + 1 < row && j - 1 >= 0) // Bottom Left.
		gameGrid [i + 1] [j - 1].setEnabled (true);
	    if (i + 1 < row) // Bottom Middle.
		gameGrid [i + 1] [j].setEnabled (true);
	    if (i + 1 < row && j + 1 < col) // Bottom Right.
		gameGrid [i + 1] [j + 1].setEnabled (true);
	}
    }


    public void readInDictionary ()
    { // Reads in each line from the dictionary file and stores it into the dictionary array to be binary searched to check if the user's word is valid.
	try
	{
	    BufferedReader in = new BufferedReader (new FileReader ("dictionary.txt"));
	    for (int i = 0 ; i < dictionary.length ; i++)
		dictionary [i] = in.readLine ().trim ();
	    in.close ();
	}
	catch (IOException e)
	{
	    System.out.println ("Error opening file " + e);
	}
    }


    public void readInPoints (String fileName)
    { // Reads in from one of the score file twice. Once to get the number of players to be used as the length of the PlayerInfo array, the second time to input each user's info into each PlayerInfo object in the array.
	try
	{
	    BufferedReader totalPlayers = new BufferedReader (new FileReader (fileName));
	    String line; // Line for each player's name.
	    String line2; // Line for each player's score.
	    while ((line = totalPlayers.readLine ()) != null && (line2 = totalPlayers.readLine ()) != null)
		amountOfPlayers++; // Retrieves the number of players who have previously played this game.

	    BufferedReader readInfo = new BufferedReader (new FileReader (fileName));
	    PlayerInfo PIs[] = new PlayerInfo [amountOfPlayers]; // Declares a PlayerInfo array of which the length is the number of players who played this game.
	    String playerLine; // Stores the user's name.
	    String scoreLine; // Stores the user's points.
	    for (int i = 0 ; i < amountOfPlayers ; i++)
	    {
		if ((playerLine = readInfo.readLine ()) != null && (scoreLine = readInfo.readLine ()) != null) // If there is something to read in from the file
		    PIs [i] = new PlayerInfo (playerLine, Integer.parseInt (scoreLine)); // Declares and constructs a new PlayerInfo with the player name and score as the custom values and stores it in the array.
	    }
	    quickSort (PIs, 0, amountOfPlayers - 1); // Sorts the PlayerInfo array.
	    reverse (PIs); // Reverses the order of the array from descending to ascending order.
	    for (int i = 0 ; i < PIs.length ; i++) // Appends each PlayerInfo to the JTextArea on the score screen from top score to low score.
		topScores.append (PIs [i].toString () + "\n");
	    if (amountOfPlayers >= 5)
	    { // Displays the top five scores of all time if there are 5 or more user entries.
		for (int i = 0 ; i < 5 ; i++)
		    topFiveScores [i].setText ("Player # " + (i + 1) + ": " + PIs [i].getName ());
	    }
	}
	catch (IOException e)
	{
	    System.out.println ("Error opening file " + e);
	}
    }


    public void AppendToFile (String fileName, String rowxcol)
    { // Prints out to the 3x3, 4x4, or 5x5 file.
	gridDimensions = rowxcol;
	scoreTitle.setText ("Players And Their " + gridDimensions + " Scores!"); // Displays the relevant title for each score leaderboard.
	try
	{ //https://www.geeksforgeeks.org/filewriter-class-in-java/ <-- Got FileWriter idea from there.
	    FileWriter fileWriter = new FileWriter (fileName, true); // Set up FileWriter to append to the file.
	    PrintWriter printWriter = new PrintWriter (fileWriter); // Set up PrintWriter to write to the file.
	    printWriter.println (name); // Prints the name first.
	    printWriter.println ("" + totalPoints); // Then it prints the score underneath.
	    printWriter.close (); // Close the PrintWriter.
	    readInPoints (fileName); // Reads in from the file, inputs the data into an array, sorts it and displays the scores and players in descending order.
	}
	catch (IOException e)
	{
	    System.out.println ("Error");
	}
    }


    public void quickSort (PlayerInfo arr[], int begin, int end)  // Decided to implement quicksort as it is fast when dealing with randomized info which is precisely the case with user names and points.
    { //https://www.baeldung.com/java-quicksort
	if (begin < end) // ^ This website has the quicksort code for an int array but I adapted it to suit my object array.
	{ // If there are elements still to be sorted.
	    int partitionIndex = partition (arr, begin, end);
	    quickSort (arr, begin, partitionIndex - 1);
	    quickSort (arr, partitionIndex + 1, end);
	}
    }


    private int partition (PlayerInfo arr[], int begin, int end)
    { //https://www.baeldung.com/java-quicksort
	PlayerInfo pivot = arr [end]; // Chooses the last element as the pivot.
	int i = (begin - 1);
	// Adapted the code from the website to suit my object.
	for (int j = begin ; j < end ; j++)
	{
	    if (arr [j].compareTo (pivot) < 0)
	    { // Compares via points.
		i++;
		// Swaps if an element is smaller than the pivot.
		PlayerInfo swapTemp = arr [i];
		arr [i] = arr [j];
		arr [j] = swapTemp;
	    }
	}

	PlayerInfo swapTemp = arr [i + 1];
	arr [i + 1] = arr [end];
	arr [end] = swapTemp;

	return i + 1; // returns the sorted final position of the pivot.
    }


    public void reverse (PlayerInfo[] array)
    { // Added in order for the high scores list to be in descending order.
	int n = array.length;
	for (int i = 0 ; i < n / 2 ; i++) // Swapping the first half elements with last half elements.
	{
	    PlayerInfo temp = array [i]; // Storing the first half elements temporarily.
	    array [i] = array [n - i - 1]; // Assigning the first half to the last half.
	    array [n - i - 1] = temp; // Assigning the last half to the first half.
	}
    }


    public boolean BinarySearch (String find)
    { // Used to search for the user's word in the dictionary file.
	int high = dictionary.length;
	int low = 0;
	boolean foundit = false;
	int mid = 0;
	while (high >= low && !foundit)
	{
	    mid = (high + low) / 2;
	    if (dictionary [mid].equals (find))
		foundit = true;
	    else if (find.compareTo (dictionary [mid]) > 0)
		low = mid + 1;
	    else //If (find < array [mid]).
		high = mid - 1;
	}
	return foundit;
    }


    public void enableTrue ()
    { // Itterates through the grid and enables every button. Used for resetting.
	wordPoints = 0;
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
		gameGrid [i] [j].setEnabled (true);
	}
    }


    public void reset (String aboveMessage, String invalid, String invalidtitle)
    { // Resets the game screen.
	diceStack.clear (); // Empties the diceStack.
	playerWord = ""; // Deletes the user's word.
	displayedWord.setText (aboveMessage);

	for (int i = 0 ; i < row ; i++)
	{ // Resets the pressed amount of and clicked status of each Dice in the Dices array and then updates each picture on the grid.
	    for (int j = 0 ; j < col ; j++)
	    {
		Dices [i] [j].setPressedAmount (0);
		Dices [i] [j].setClicked (false);
		gameGrid [i] [j].setIcon (createImageIcon (new Dice (Dices [i] [j].getFace (), Dices [i] [j].getClicked (), Dices [i] [j].getPoint (), Dices [i] [j].getPressedAmount (), Dices [i] [j].getRow (), Dices [i] [j].getColumn ()).getPic ()));
	    }
	}
	enableTrue (); // Enables every button to be true.
	if (!aboveMessage.equals ("Your Word Here!")) // An error message pops up if the user tries to enter the same word twice or enter a word that does not exist.
	    JOptionPane.showMessageDialog (null, invalid, invalidtitle, JOptionPane.ERROR_MESSAGE);
    }


    public void finish ()
    {
	if (time == 0)
	{ // When the time runs out then the game brings them to the score screen.
	    JOptionPane.showMessageDialog (null, "Time's Up! Now Let's See How You Did Compared To The Other Players!", "", JOptionPane.ERROR_MESSAGE);
	    if (row == 3 && col == 3) // Appends the user's name and scores to the 3x3 file.
		AppendToFile ("3x3Scores.txt", "3x3");
	    else if (row == 4 && col == 4) // Appends the user's name and scores to the 4x4 file.
		AppendToFile ("4x4Scores.txt", "4x4");
	    else if (row == 5 && col == 5) // Appends the user's name and scores to the 5x5 file.
		AppendToFile ("5x5Scores.txt", "5x5");
	    timer.stop (); // Stops the timer in order to reduce lag.
	    cdLayout.show (p_card, "5"); // Brings the user to the score screen.
	}
    }


    public void actionPerformed (ActionEvent e)
    {
	if (e.getActionCommand ().equals ("replay"))
	    System.exit (0); // Closes the program.
	else if (e.getActionCommand ().equals ("settings"))
	    cdLayout.show (p_card, "6"); // Shows the settings screen.
	else if (e.getActionCommand ().equals ("play2"))
	{ // Shows the game screen and starts the timer indicating that the game has started.
	    cdLayout.show (p_card, "2");
	    timer.start ();
	}
	else if (e.getActionCommand ().equals ("nextWord"))
	{ // The button that is pressed when the user submits their word.
	    finish (); // Calls the finish method first to see if the time has run out or not.
	    playerWords [wordCounter] = playerWord; // Adds the user's word to the playerWords array.
	    wordCounter++; // Increases in order to add the user's next word to the array.
	    int sameWords = 0; // Counter for how many of the same words the user made.
	    for (int i = 0 ; i < playerWords.length ; i++)
	    { // Itterates through the array.
		if (playerWord.equals (playerWords [i]))
		    sameWords++; // Counts how many times the word has shown up in the array.
	    }
	    if (sameWords >= 2) // If the user has entered the same word more than once which is not allowed, the grid and word resets.
		reset ("", "You can only submit a word once!", "Sneaky!");
	    else if (BinarySearch (playerWord.toLowerCase ()) && playerWord.length () >= 3)
	    { // If the user's word is three letters or more and is valid according to the dictionary.
		totalPoints += wordPoints; // The points increases by the value of the user's word.
		playerPoints.setText ("Points: " + totalPoints); // Updates the points.
		words1.append (playerWord + ", "); // Adds the word to the word list.
		reset ("Your Word Here!", "", ""); // Resets the grid and word display.
	    }
	    else // The user has submitted a made up word.
		reset ("", "Not a valid word. Try Again.", "Stupid!");
	}
	else if (e.getActionCommand ().equals ("ins"))
	{
	    cdLayout.show (p_card, "3"); // Shows the instructions screen from the game screen.
	    timer.stop (); // Stops the timer in case the user forgets the instructions while playing the game.
	}
	else if (e.getActionCommand ().equals ("gridSizes"))
	{ // Called when the user wants to change the dimensions of the grid in settings.
	    name = JOptionPane.showInputDialog ("What Is Your Name? This Will Be Displayed On The Leaderboard.");
	    String[] possibleValues = {"3 x 3", "4 x 4", "5 x 5"};
	    String selectedValue = (String) JOptionPane.showInputDialog (null,
		    "What Grid Size Would You Like To Play On?", "Choose one", JOptionPane.INFORMATION_MESSAGE, null,
		    possibleValues, possibleValues [0]);
	    row = (int) (selectedValue.charAt (0)) - 48; // Retrives the row that the user wants from the pulldown box.
	    col = row; // Changes the column to be the same as the row.
	    Dices = new Dice [row] [col]; // Updates the dimensions.
	    gameGrid = new JButton [row] [col]; // Updates the dimensions.
	    gridPanel.removeAll (); // Removes the original grid.
	    addGrid (); // Adds the updated dimension grid.
	    cdLayout.show (p_card, "3");
	}
	else if (e.getActionCommand ().equals ("play"))
	    cdLayout.show (p_card, "6"); // Shows the instructions screen.
	else if (e.getActionCommand ().equals ("valuescreen"))
	    cdLayout.show (p_card, "4"); // Shows the letter value screen.
	else if (e.getActionCommand ().equals ("shuffle"))
	{
	    finish (); // Checks if the timer has reached zero yet.
	    reset ("Your Word Here!", "", ""); // Resets the grid.
	    wordPoints = 0; // Resets the user's word points.
	    totalPoints = 0; // Resets the user's total points.
	    for (int i = 0 ; i < playerWords.length ; i++)
		playerWords [i] = ""; // Resets all the words that the user made.
	    wordCounter = 0;
	    x = -2; // Resets the value of x.
	    y = -2; // Resets the value of y.
	    words1.setText ("Your Words: "); // Updates the JTextArea.
	    playerPoints.setText ("Points: " + wordPoints);

	    for (int i = 0 ; i < row ; i++)
	    {
		for (int j = 0 ; j < col ; j++)
		{
		    Dices [i] [j] = new Dice (i, j); // Declares and Constructs a new Dice with its coordinates and adds it to the array.
		    gameGrid [i] [j].setIcon ((createImageIcon (Dices [i] [j].getPic ()))); // Updates the grid with new random values.
		}
	    }
	}
	else
	{
	    finish (); // Checks if the timer has reached zero.
	    int x = Integer.parseInt (e.getActionCommand ()) / col; // Retrieves the row of the user's click.
	    int y = Integer.parseInt (e.getActionCommand ()) % col; // Retrieves the column of the user's click.
	    Dices [x] [y].setPressedAmount (Dices [x] [y].getPressedAmount () + 1); // Increases the amount of times that the dice has been clicked.
	    if (!diceStack.exists (x, y)) // If the user hasn't clicked the letter before. This prevents the user from deselecting letters that they clicked that weren't their last click.
		Dices [x] [y].setClicked (true);
	    if (!diceStack.isEmpty () && diceStack.exists (x, y))
	    { // If the coordinates you clicked on were already clicked aka deselecting.
		if (diceStack.peek ().getRow () == x && diceStack.peek ().getColumn () == y) // If your current click matches your previous click aka you selected a letter and now you want to unselect it.
		{ // If its the last one clicked.
		    Dices [x] [y].setClicked (false); // Set the current Dice that is clicked to be unselected.
		    Dices [x] [y].setPressedAmount (0); // Set the current Dice that is clicked to have its pressed amount be 0.
		    diceStack.pop (); // Pop the Dice from the stack.
		    if (Dices [x] [y].getFace () == 'Q') // Remove two characters from the user's word only if they click on a 'Q' because 'Q' comes with a 'U' to make it easier for the user to make words.
			playerWord = playerWord.substring (0, playerWord.length () - 2);
		    else
			playerWord = playerWord.substring (0, playerWord.length () - 1); // Removes the most recent character from the user's word.
		    wordPoints -= Dices [x] [y].getPoint (); // Subtracts the points associated with the unselected letter from the user's word points.
		    showStatus (""); // Clears the show status.
		}
		else // When a user tries to deselect a letter that they haven't recently selected.
		    showStatus ("You can only deselect your most recent click! Sneaky ._.");
	    }
	    else // Adding to the user's word.
	    {
		diceStack.push (Dices [x] [y]); // Pushes the clicked Dice onto the diceStack.
		if (Dices [x] [y].getClicked () && Dices [x] [y].getFace () == 'Q' && Dices [x] [y].getPressedAmount () == 1)
		    playerWord += Dices [x] [y].getFace () + "U"; // If the letter they clicked is a 'Q' then add a 'U' as well. A 'Q' has never showed up for me so feel free to go into the dice object and change the frequency of the 'Q' to test this feature.
		else if (Dices [x] [y].getClicked () && Dices [x] [y].getPressedAmount () == 1)
		    playerWord += Dices [x] [y].getFace (); // Add the letter to the user's word.
		wordPoints += Dices [x] [y].getPoint (); // Add the points associated with the letter to the user's word points.
	    }
	    if (!diceStack.isEmpty ())
	    { // Update the screen to enable letters that are neighbours of letters the user has clicked.
		gameGrid [x] [y].setIcon (createImageIcon (new Dice (Dices [x] [y].getFace (), Dices [x] [y].getClicked (), Dices [x] [y].getPoint (), Dices [x] [y].getPressedAmount (), Dices [x] [y].getRow (), Dices [x] [y].getColumn ()).getPic ()));
		letterRing (diceStack.peek ().getRow (), diceStack.peek ().getColumn ());
	    }
	    displayedWord.setText (playerWord); // Update the user's word on the screen.
	    if (playerWord.equals (""))
	    { // If nothing is clicked then enable every button to be true.
		gameGrid [x] [y].setIcon (createImageIcon (new Dice (Dices [x] [y].getFace (), Dices [x] [y].getClicked (), Dices [x] [y].getPoint (), Dices [x] [y].getPressedAmount (), Dices [x] [y].getRow (), Dices [x] [y].getColumn ()).getPic ()));
		enableTrue ();
	    }
	}
    }


    protected static ImageIcon createImageIcon (String path)
    {
	java.net.URL imgURL = Boggle.class.getResource (path);
	if (imgURL != null)
	    return new ImageIcon (imgURL);
	else
	    return null;
    }
}
