public class PigSolver
{

	int target;  // target score for the game
	int p1_score;  // Player 1's score
	int p2_score;  // Player 2's score

	int iterations; // number of iterations required to converge


	/**
	 * Calculates the expected number of wins for player 1 when there are a given 
	 * number of turns remaining
	 * @param turns = number of turns remaining
	 * @param p1, player 1's score
	 * @param p2, player 2's score
	 * @return number of wins
	 */
	public double wins()
	{
		for (int i = 0; i < target; i ++)
		{

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
			int p1_score = Integer.parseInt(args[1]);
			int p2_score = Integer.parseInt(args[2]);

			// System.out.println(target);
		}
	}
}
