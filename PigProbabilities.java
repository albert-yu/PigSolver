/**
 * Calculates the probabilities of ending a turn at each possible value
 * when rolling until a given target.
 */

public class PigProbabilities
{
    /**
     * The maximum possible turn target.  This should be the target for
     * the game.
     */
    private int maxTarget;

    /**
     * The probability of reaching at least the given value.
     */
    private double[] pReach;

    /**
     * The probability of ending at a given value.  pEnd[i][6] is the
     * probability of ending with 0.  pEnd[i][j] is the probability
     * of ending with i+j for j = 0...5
     */
    private double[][] pEnd;

    /**
     * Computes the probabilities for all possible turn targets up to
     * gameTarget.
     *
     * @param gameTarget an integer greater than or equal to 2
     */
    public PigProbabilities(int gameTarget)
    {
    	if (gameTarget < 2)
        {
    		throw new IllegalArgumentException("game target must be at least 2: " + gameTarget);
        }

        maxTarget = gameTarget;

	    pReach = new double[gameTarget + 1];
        pEnd = new double[gameTarget + 1][7];
        
        pReach[0] = 1.0;
        for (int i = 2; i <= maxTarget; i++)
        {
            // reach i by getting to i-2 and rolling a 2, i-3 and rolling a 3... 
            for (int j = 2; j <= 6; j++)
            {
                if (i - j >= 0)
                {
                    pReach[i] += pReach[i - j] / 6.0;
                }
            }
            
            pEnd[i][0] = pReach[i];
	        pEnd[i][6] = 1.0 - pEnd[i][0];

    	    for (int j = 1; j < 6; j++)
    		{
    		    for (int r = Math.min(6, i + j);
    			 r >= 2 && i + j - r < i && i + j - r >= 0;
    			 r--)
    			{
    			    pEnd[i][j] += pReach[i + j - r] / 6.0;
    			}
                
    		    pEnd[i][6] -= pEnd[i][j];
    		}
        }
    }

    /**
     * Returns the probability of ending a turn at the given score with
     * the given target.
     *
     * @param turnTarget an integer at least 2 and no more than the game
     * target
     * @param endScore an integer
     */
    public double pEndAt(int turnTarget, int endScore)
    {
    	if (turnTarget < 2)
    	{
    		throw new IllegalArgumentException("turn target must be at least 2: " + turnTarget);
        }

    	if (turnTarget > maxTarget)
	    {
    		throw new IllegalArgumentException("turn target must be no more than " + maxTarget + ": " + turnTarget);
    	}

    	if (endScore == 0)
    	{
    		return pEnd[turnTarget][6];
        }

    	else if (endScore >= turnTarget && endScore <= turnTarget + 5)
    	{
    		return pEnd[turnTarget][endScore - turnTarget];
    	}
    	else
    	{
    		// won't end turn < turnTarget or > turnTarget + 5
    		return 0.0;
    	}
    }

    public static void main(String[] args)
    {
    	int turnTarget = Integer.parseInt(args[0]);

    	PigProbabilities p = new PigProbabilities(turnTarget);
    	System.out.printf("p(0) = %f%n", p.pEndAt(turnTarget, 0));
    	for (int i = 0; i <= 5; i++)
    	{
    		System.out.printf("p(%d) = %f%n", turnTarget + i, p.pEndAt(turnTarget, turnTarget + i));
    	}
    }

}