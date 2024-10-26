public class Dice
{
    private char face; // face or letter of the dice that will be shown to the player.
    private boolean clicked; // if the user clicked on it or not.
    private int point; // the points associated with the letter.
    private int pressedAmount; // the amount of times that the user has clicked on the dice.
    private int row; // the row that the dice is in.
    private int column; // the column that the dice is in.

    public Dice (int r, int c)
    {
	clicked = false; // the dice is not clicked.
	pressedAmount = 0; // the dice has not been pressed.
	row = r;
	column = c;
	// freqRanges array is an array of the percentages of each letter's frequency rounded to four decimal places. The next element is the sum of the previous elements.
	// A random number between 1 and 1 million (rounded to four decimal places but I made them all integers) is generated. Whatever range it falls between, the letter associated with the minimum of that range becomes the letter.
	// points is an array that corresponds a point for each letter. Adapted from Scrabble.
	int freqRanges[] = {111607, 196573, 272382, 347830, 419465, 488974, 555518, 612869, 667762, 713150, 749458, 783302, 814973, 845102, 875136, 899841, 920561, 938682, 956461, 969360, 980376, 990450, 993352, 996074, 998039, 1000000};
	char letters[] = {'E', 'A', 'R', 'I', 'O', 'T', 'N', 'S', 'L', 'C', 'U', 'D', 'P', 'M', 'H', 'G', 'B', 'F', 'Y', 'W', 'K', 'V', 'X', 'Z', 'J', 'Q'};
	int points[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 2, 3, 3, 4, 2, 3, 4, 4, 4, 5, 4, 8, 10, 8, 10};
	int rand = (int) (Math.random () * 1000000) + 1;
	for (int i = 0 ; i < letters.length - 1 ; i++)
	{
	    if (rand <= freqRanges [0])
	    { // handles the special case for when rand is in the 'E' range.
		face = letters [0];
		point = points [0];
	    }
	    else if (rand >= freqRanges [i] && rand <= freqRanges [i + 1])
	    {
		face = letters [i];
		point = points [i];
	    }
	}
    }


    public Dice (char f, boolean c, int p, int pa, int r, int co)
    { // custom values
	face = f;
	clicked = c;
	point = p;
	pressedAmount = pa;
	row = r;
	column = co;
    }



    public void setRow (int r)
    { // changes the row.
	row = r;
    }


    public void setColumn (int co)
    { // changes the column.
	column = co;
    }


    public void setPressedAmount (int pa)
    { // updates the amount of time it has been pressed.
	pressedAmount = pa;
    }


    public void setPoint (int p)
    { // changes the points associated with the letter.
	point = p;
    }


    public void setFace (char f)
    { // changes the face of the dice that is shown on the grid.
	face = f;
    }


    public void setClicked (boolean c)
    { // updates the clicked status.
	clicked = c;
    }


    public int getRow ()
    { // retrieves the row.
	return row;
    }


    public int getColumn ()
    { // retreives the column.
	return column;
    }


    public int getPressedAmount ()
    { // retreives the amount of times that it has been pressed.
	return pressedAmount;
    }


    public int getPoint ()
    { // retreives the points associated with the dice.
	return point;
    }


    public char getFace ()
    { // retreives the face of the dice that is shown on the grid.
	return face;
    }


    public boolean getClicked ()
    { // retrieves the clicked status.
	return clicked;
    }


    public String toString ()
    { // returns the info of the dice.
	return "the die rolled " + face + " and the clicked status is " + clicked + ". It has a value of " + point + " points, it has been pressed " + pressedAmount + " times and it is in row " + row + " and column " + column + ".";
    }


    public boolean equals (Dice d)
    { // based on the letter
	return face == d.getFace ();
    }


    public int compareTo (Dice d)
    { // based on the letter
	if (face > d.getFace ())
	    return 1;
	else if (face < d.getFace ())
	    return -1;
	else
	    return 0;
    }


    public String getPic ()
    { // retreives the picture name for each face. The selected one if its selected and the unselected one if unselected.
	if (clicked)
	    return face + "selected.png";
	else
	    return face + "unselected.png";
    }
}


