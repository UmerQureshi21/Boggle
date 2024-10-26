public class DiceStack
{
    private int count = 0; // length of the dice array
    private Dice data[] = new Dice [50];

    public DiceStack ()
    {
	count = 0;
    }


    public void push (Dice addMe)
    {
	data [count] = addMe; // adds a dice to the array
	count++; // increases so that a dice can be pushed again
    }


    public int size ()
    {
	return count; // returns the amount of dice in the array
    }


    public boolean isFull ()
    {
	return count == data.length; // checks if the stack is full or not
    }


    public Dice pop ()
    {
	if (count > 0)
	{ // pops the most recent dice from the stack
	    count--;
	    return data [count];
	}
	return data [count];
    }


    public Dice peek ()
    {
	if (count > 0)
	    return data [count - 1]; // changed from count-- because count - 1 does not change the value of count
	return null; // if count is zero then return nothing
    }


    public boolean isEmpty ()
    {
	return count == 0; // checks if there is no dice in the stack
    }


    public void clear ()
    {
	count = 0;
	data = new Dice [50]; // erases all the dice in the array
    }


    public boolean exists (int row, int col)
    { // checks if a certain dice in the grid of dices has already been pushed onto the stack
	if (count == 0)
	    return false;
	for (int i = 0 ; i < count ; i++)
	{
	    if (data [i].getRow () == row && data [i].getColumn () == col)
		return true;
	}
	return false;
    }
}

