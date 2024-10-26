public class PlayerInfo
{ // Created to keep the user's name tied to their score so that when the scores are sorted, the user names are also sorted according to their scores.
    private String name; // name of the user.
    private int score; // score of the user.

    public PlayerInfo ()
    { // default values
	name = "";
	score = 0;
    }


    public PlayerInfo (String n, int s)
    { // custom values
	name = n;
	score = s;
    }


    public void setName (String n)
    {
	name = n;
    }


    public void setScore (int s)
    {
	score = s;
    }


    public String getName ()
    {
	return name;
    }


    public int getScore ()
    {
	return score;
    }


    public String toString ()
    {
	return name + ": " + score;
    }


    public int compareTo (PlayerInfo pi)
    { // based on score in order to sort on the score screen.
	if (score > pi.getScore ())
	    return 1;
	else if (score < pi.getScore ())
	    return -1;
	else
	    return 0;
    }
}
