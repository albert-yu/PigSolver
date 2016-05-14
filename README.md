# Pig Solver

Full version at: http://www3.amherst.edu/~jrglenn92/201/S2016/Projects/P3-Pig/pig.html

## Introduction
Pig is a dice game for two players. The players take turns rolling a six-sided die. On each turn, the current player rolls, adding up the total of the rolls. The player can stop rolling at any time; when the current player ends the turn voluntarily then the current turn total is added to the player's score. However, if the current player rolls a 1 then the turn is over with no points gained. The first player to a predetermined target (often 100) wins the game.

## Assignment
Write a program that computes the optimal strategy for two-player Pig. The optimal strategy should specify, for any given score, the optimal turn total to roll until.

### Summary
Write a program with the following inputs and outputs.

* Inputs
  * _T_, the score needed to win the game.
  * _p<sub>1</sub>_, Player 1's current score
  * _p<sub>2</sub>_, Player 2's current score
* Outputs
  * _wins(infinity, p<sub>1</sub>, p<sub>2</sub>)_, the expected probability of Player 1 winning when the score is _p<sub>1</sub>_ to _p<sub>2</sub>_
  * The optimal turn total until which to roll

<!--### Details-->

<!--We can determine the optimal strategy by approximating the standard game by one that ends in a tie after a predetermined number of turns. As the maximum number of turns goes to inifinity the optimal strategy for the modified game will converge to the optimal strategy for the standard game. The optimal strategy for the modified game can be calculated using the following recurrence for _wins(n, x, y)_, the expected number of wins for Player 1 when there are _n_ turns remaining and the score is _x_ to _y_ (when _n_ is even, it is Player 1's turn).-->


