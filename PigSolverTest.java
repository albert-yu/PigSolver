import java.lang.Math;

public class PigSolverTest
{
	public static void main(String[] args)
	{
		PigSolver solver = new PigSolver(4, 2, 2);

		Double[][] sampleArray = {{1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 0.0}, {1.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 0.0}};

		for (int i = 0; i < 4; i ++)
		{
			solver.setExpectedWins(i, sampleArray);
		}

		System.out.println(solver.converged());  // true
		System.out.println(1.0000000000000001 == 1.0000000000000002);  // false
		System.out.println(1.00000000000000001 == 1.00000000000000002);  // true

		System.out.println(Math.max(4, 6)); // 6
	}
}