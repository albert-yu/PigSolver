import java.lang.Math;
import java.util.Arrays;

public class PigSolver
{

	private int target;  // target score for the game
	private int p1Score;  // Player 1's score
	private int p2Score;  // Player 2's score

	private int iterations; // number of iterations required to converge

	// Array to hold computed expected wins
	private Double[][][] expectedWins; 


	public PigSolver(int tgt, int p1, int p2)
	{
		target = tgt;
		p1Score = p1;
		p2Score = p2;

		iterations = 0;
		// keep reusing the array so that one dimension does not exceed 4
		expectedWins = new Double[4][target][target];
	}


	public void printArray(int iteration)
	{
		System.out.println("");
		System.out.println(String.format("Array at iteration %d", iteration));
		for (int i = p1Score; i < target; i ++)
		{	
			System.out.println(Arrays.toString(expectedWins[iteration][i]));
		}
		System.out.println("");
		// System.out.println(Arrays.deepToString(expectedWins[iteration]));
	}

	private double winsOddCase(int i, int j, PigProbabilities prob)
	{
		System.out.println("function winsOddCase called");
		int upperBoundOnRoll = Math.max(2, target - p2Score);

		printArray(1);

		// double expectedResult = 0.0;
		double currentMin = 1.0;
		double reachOrExceed = 0.0; 

		// System.out.println(iterations);

		for (int s = 2; s <= upperBoundOnRoll; s ++)
		{
			for (int n = s; n < s + 5; n ++)
			{
				String template = "n is %d";
				System.out.println(String.format(template, n));
				// System.out.println(n);
				System.out.println(String.format("j + n = %d", j + n));
				reachOrExceed += expectedWins[(iterations - 1) % 4][i][j + n] * prob.pEndAt(s, n);
				System.out.println("reachOrExceed is");
				System.out.println(reachOrExceed);
				
			}

			double expectedResult = expectedWins[(iterations - 1) % 4][i][j] * prob.pEndAt(s, 0) + reachOrExceed; 
			System.out.println("expectedResult");
			System.out.println(expectedResult);
			// System.out.println("processed!");

			if (expectedResult < currentMin)
			{
				currentMin = expectedResult;
			}
		}

		return currentMin;
	}

	private double winsEvenCase(int i, int j, PigProbabilities prob)
	{
		System.out.println("function winsEvenCase called");

		int upperBoundOnRoll = Math.max(2, target - p2Score);
		System.out.println("Upper bound is");
		System.out.println(upperBoundOnRoll);

		// double expectedResult = 0.0;
		double currentMax = 0.0;
		double reachOrExceed = 0.0; 

		for (int s = 2; s <= upperBoundOnRoll; s ++)
		{
			for (int n = s; n < s + 5; n ++)
			{
				System.out.println("n is");
				System.out.println(n);
				reachOrExceed += expectedWins[(iterations - 1) % 4][i + n][j] * prob.pEndAt(s, n);
			}

			double expectedResult = expectedWins[(iterations - 1) % 4][i][j] * prob.pEndAt(s, 0) + reachOrExceed; 

			if (expectedResult > currentMax)
			{
				currentMax = expectedResult;
			}
		}

		return currentMax;
	}

	/**
	 * Calculates the expected number of wins for player 1 when there are a given 
	 * number of turns remaining
	 * 
	 */
	public void wins()
	{

		PigProbabilities prob = new PigProbabilities(target);

		// base case

		for (int i = p1Score; i < target; i ++)
		{
			for (int j = p2Score; j < target; j ++)
			{
				// n = 0 => 0.5
				expectedWins[0][i][j] = 0.5;


				// n = 1 and n = 2
				// int upperBoundOnRoll = Math.max(2, target - p2Score);

				// double currentMin = 1.0;
				// double currentMax = 0.0;

				// for (int s = 2; s <= upperBoundOnRoll; s ++)
				// {
				// 	// sum up probabilities of reaching or exceeding s
				// 	double reachOrExceed = 0.0; 
				// 	for (int n = s; n <= s + 5; n ++)
				// 	{
				// 		reachOrExceed += 0.5 * prob.pEndAt(s, n);
				// 	}

				// 	double expectedResult = 0.5 * prob.pEndAt(s, 0) + reachOrExceed; 

				// 	if (expectedResult < currentMin)
				// 	{
				// 		currentMin = expectedResult;
				// 	}

				// 	else if (expectedResult > currentMax)
				// 	{
				// 		currentMax = expectedResult;
				// 	}

				// }

				// expectedWins[1][i][j] = currentMin;  // n = 1

				// expectedWins[2][i][j] = currentMax; // n = 2
			}
		}


		iterations += 1;
		printArray(0);

		// System.out.println(expectedWins[2][7][7]);

		// System.out.println("Before loop iterations:");
		// System.out.println(iterations);

		// System.out.println("After loop:");

		outerloop: 
		while (true)
		{
			System.out.println("loop entered");
			// System.out.println(iterations);
			for (int i = p1Score; i < target; i ++)
			{
				for (int j = p2Score; j < target; j ++)
				{
					// means we've reached convergence
					if (expectedWins[iterations % 4][i][j] == expectedWins[((iterations % 4 - 2) + 4) % 4][i][j] 
							&& expectedWins[iterations % 4][i][j] != null)
					{
						break outerloop;
					}

					else
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
			}


			iterations ++;
		}
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

			// System.out.println(solver.iterations);

			solver.wins();
			System.out.println("Iterations");
			System.out.println(solver.iterations);
			System.out.println(solver.expectedWins[(solver.iterations) % 4][p1Score][p2Score]);

			// System.out.println(target);
		}
	}
}
