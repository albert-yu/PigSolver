import java.lang.Math;
import java.util.Arrays;


public class PigSolver
{

    /**
     * The target score for the game. Usually something like 100.
     */
    private int target; 

    /**
     * Player 1's score
     */
    private int p1Score;  

    /**
     * Player 2's score
     */
    private int p2Score;

    /**
     * Keep track of the number of iterations required for convergence
     */
    private int iterations; 

    /** 
     * Maximum number of iterations allowed, for testing.
     */
    private static int maxIterations = 500;

    /**
     * Array to hold the results of expected wins.
     */
    private Double[][][] expectedWins; 
    private int arraySize;


    /**
     * The turn total we should attempt to roll
     */
    private int turnTotal;


    public PigSolver(int tgt, int p1, int p2)
    {
        target = tgt;
        p1Score = p1;
        p2Score = p2;

        iterations = 0;

        arraySize = target + 7;
        expectedWins = new Double[4][arraySize][arraySize];

        turnTotal = 0;
    }


    public int getTurnTotal()
    {
        return turnTotal;
    }

    /**
     * Prints out a somewhat legible representation of the array at a given
     * iteration
     * @param iteration, (integer) the iteration at which we are printing
     */
    public void printArray(int iteration)
    {
        System.out.println("");
        System.out.println(String.format("Array at iteration %d", iteration));

        for (int i = 0; i < arraySize; i ++)
        {   
            System.out.println(Arrays.toString(expectedWins[iteration % 4][i]));
        }
        System.out.println("");
    }


    public void setExpectedWins(int iteration, Double[][] sample)
    {
        expectedWins[iteration] = sample; 
    }


    /**
     * Calculates the expected number of wins for player 1 when there are a given 
     * number of turns remaining.
     * Iteratively updates expectedWins[n][i][j] until convergence, i.e.
     * until expectedWins[n][i][j] = expectedWins[n-2][i][j] for all i, j
     */
    public void wins()
    {

        PigProbabilities prob = new PigProbabilities(target);

        for (int n = 0; n < 4; n ++)
        {
            for (int i = 0; i < arraySize; i ++)
            {
                for (int j = 0; j < arraySize; j ++)
                {
                    if (i >= target)
                    {
                        expectedWins[n][i][j] = 1.0;
                    }

                    else if (j >= target)
                    {
                        expectedWins[n][i][j] = 0.0;
                    }
                }
            }
        }

        // base case n = 0

        for (int i = 0; i < target; i ++)
        {
            for (int j = 0; j < target; j ++)
            {

                expectedWins[0][i][j] = 0.5;
    
            }
        }


        iterations += 1;

        while (!converged() && iterations < maxIterations)
        {
            for (int i = 0; i < target; i ++)
            {
                for (int j = 0; j < target; j ++)
                {

                    if (iterations % 2 == 0)
                    {
                        expectedWins[iterations % 4][i][j] = winsEvenCase(i, j, prob);
                    }

                    else 
                    {
                        expectedWins[iterations % 4][i][j] = winsOddCase(i, j, prob);
                    }

                }
            }

            iterations ++;
        }
    }


    /**
     * Determines if convergence has been reached.
     * @return true if converged, false otherwise
     */
    private boolean converged()
    {

        for (int i = p1Score; i < target; i ++)
        {
            for (int j = p2Score; j < target; j ++)
            {

                if (expectedWins[0][i][j] == null || expectedWins[2][i][j] == null)
                {
                    return false;
                }

                else if (expectedWins[0][i][j] - expectedWins[2][i][j] > 0.0000000000000001)
                {
                    return false;
                }

            }
        }

        return true;

    }


    /**
     * If n is odd, computes expectedWins[n][i][j].
     * @param i, the index of Player 1 we are currently at
     * @param j, the index of Player 2 we are currently at
     * @param prob, the PigProbabilities object that contains the precomputed probabilities.
     */
    private double winsOddCase(int i, int j, PigProbabilities prob)
    {
        int upperBoundOnTurn = Math.max(2, (target - j));

        double currentMin = 1.0;

        for (int s = 2; s <= upperBoundOnTurn; s ++)
        {
            double reachOrExceed = 0.0; 

            for (int m = s; m <= s + 5; m ++)
            {
                reachOrExceed += expectedWins[(iterations - 1) % 4][i][j + m] * prob.pEndAt(s, m);
            }

            double expectedResult = expectedWins[(iterations - 1) % 4][i][j] * prob.pEndAt(s, 0) + reachOrExceed; 

            if (expectedResult < currentMin)
            {
                currentMin = expectedResult;
            }
        }

        return currentMin;
    }


    /**
     * If n is even, computes expectedWins[n][i][j]. Also updates turnTotal.
     * @param i, the index of Player 1 we are currently at
     * @param j, the index of Player 2 we are currently at
     * @param prob, the PigProbabilities object that contains the precomputed probabilities.
     */
    private double winsEvenCase(int i, int j, PigProbabilities prob)
    {
        int upperBoundOnTurn = Math.max(2, target - i);

        double currentMax = 0.0;

        for (int s = 2; s <= upperBoundOnTurn; s ++)
        {
            double reachOrExceed = 0.0; 
            for (int n = s; n <= s + 5; n ++)
            {
                reachOrExceed += expectedWins[(iterations - 1) % 4][i + n][j] * prob.pEndAt(s, n);
            }

            double expectedResult = expectedWins[(iterations - 1) % 4][i][j] * prob.pEndAt(s, 0) + reachOrExceed; 

            if (expectedResult > currentMax)
            {
                currentMax = expectedResult;
                if (i == p1Score && j == p2Score)
                {
                    turnTotal = s;
                }
            }
        }

        return currentMax;
    }


    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            throw new IllegalArgumentException("Need three arguments: target score, p1 score, p2 score");
        }

        else
        {
            int target = Integer.parseInt(args[0]);
            int p1Score = Integer.parseInt(args[1]);
            int p2Score = Integer.parseInt(args[2]);

            PigSolver solver = new PigSolver(target, p1Score, p2Score);

            solver.wins();
            System.out.println(String.format("%d iterations", solver.iterations));
            System.out.print(solver.expectedWins[(solver.iterations) % 4][p1Score][p2Score]);
            System.out.print(" ");
            System.out.println(solver.getTurnTotal());
        }
    }
}
