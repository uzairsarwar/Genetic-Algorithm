# Genetic-Algorithm Readme
The initial setup of the project is as follows:
1.	The number of generation for the algorithm is 50.
2.	The chromosome size is 100,000.
3.	The number of lists/combinations in a generation is 60.
4.	The mutation rate is fixed at 0.001.
5.	And the acceptable solution is in the range 0 – 20.

The data structure used in this project are 2 dimensional array of type ArrayList. In addition, single and double List are also implemented of type Integers. 

To start off, we randomly generate 100 numbers and store them into a single List of type Integer. We make sure that, no number is repeated in the list. We then calculate the sum of the entire set and use it as the benchmark to calculate fitness. The GA algorithm is located inside a while statement which terminates when these two conditions match:
1.	If the difference between two subsets is within the acceptable range
2.	If the number of generations/iterations are exhausted.
To begin the GA process, we first create the parents based on the list size. In our case, we use the size of 60. The parents are a lists of chromosomes each having 100000 numbers, and also from our original list of 100 distinct numbers. The next step in the algorithm is to select distinct numbers that also exist in the original list and shuffle them. We then proceed to find the fitness of these selected shuffled 100 numbers by splitting the set into two and then taking the difference of their sums. We then take the difference of the max sum and the difference of the sums, thus obtaining the fitness. This leads to our next step to find the probability. We then again check if any of the two termination categories have been matched or not. If one of them satisfy, then we exit the loop and display the two sets along with the difference. Otherwise, we move to the next phase where we select randomly the next pair of elements based on their fitness using the roulette wheel method. Then we pair the selected lists and crossover to generate next generation. We then run the mutation algorithm and mutate only those list whose probability lie under the specified mutation rate. We replace the last element of the chromosome with one of the element from the original 100 numbers list. The termination conditions are again checked for the above mentioned conditions.

	Genetic Algorithm are a class of exploration and exploitation algorithms which do not always give you the exact solution but gives you the close to the perfect solution.  
