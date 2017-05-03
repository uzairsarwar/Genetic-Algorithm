
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Project3 {

	//variables
	public static int size = 100;
	public static int range = 10000;
	public static int population = 60;
	public static int chromosomeSize = 100000;
	public static double mutationRate = 0.001;
	public static int final_index = -1;
	public static int possible_iterations = 50;
	public static int possibleSolutionRanged = 20;
	
	//structures
	public static List<Integer> lists = new ArrayList<Integer>();
	public static List<List<Integer>> chromosomes = new ArrayList<List<Integer>>();
	public static List<List<Integer>> newChromosomes = new ArrayList<List<Integer>>();
	public static List<List<Integer>> protein_bags = new ArrayList<List<Integer>>();
	public static List<Integer> fitness = new ArrayList<Integer>();
	public static List<Double> prob = new ArrayList<Double>();
	public static List<Integer> indices = new ArrayList<Integer>();
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[][] pairUp = new ArrayList[population][population];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		generateRandomNumbers(lists, range, 1);
		int maxSum = sumofArray(lists, 0, lists.size());
		print(lists);
		System.out.println("----------------------------------------");
		int iterations = 0;
		initializeElements();
		
		while(!solutionRanged(maxSum) && iterations < possible_iterations){
			cleanUp();
			createParents();
			System.out.println("Parents created for iteration "+iterations);
			filter();
			System.out.println("Filteration done for iteration "+iterations);
			/*print2Dlist(chromosomes);
			print2Dlist(protein_bags);*/
			findFitness(maxSum);
			System.out.println("Fitness found for iteration "+iterations);
			findProb();
			System.out.println("Probability calculated for iteration "+iterations);
			if(solutionRanged(maxSum)){
				System.out.println("-------------------------------------------");
				break;
			}
			while(indices.size() < ((population/2)+1)){
				int index = rouletteWheel();
				if(!indices.contains(index)){
					indices.add(index);
				}
			}//end of while1 loop
			System.out.println("parents selected for paring in iteration "+iterations);
			pairing();
			System.out.println("Pairing done for the future generation of iteration "+iterations);
			crossover();
			System.out.println("new generation created for iteration "+iterations);
			mutation();
			System.out.println("Iteration "+iterations+" done");
			System.out.println("-------------------------------------------");
			iterations++;
		}//end of while loop
		System.out.println("Solution:");
		displayList(maxSum);
	}
	
	public static void cleanUp(){
		protein_bags.clear();
		newChromosomes.clear();
		fitness.clear();
		prob.clear();
		indices.clear();
	}
	
	public static void initializeElements(){
		for(int i=0; i<population; i++){
			for(int j=0; j<population; j++){
				pairUp[i][j] = new ArrayList<Integer>();
				pairUp[i][j].add(0, 0);
			}
		}
	}
	
	public static void displayList(int max_sum){
		int min = 10000000;
		for(int p = 0; p<protein_bags.size(); p++){
			if(Math.abs(max_sum - fitness.get(p)) < min){
				min = Math.abs(max_sum - fitness.get(p));
				final_index = p;
			}
		}
		System.out.println("Set 1: "+protein_bags.get(final_index).subList(0, 50).toString()+" sum: "+sumofArray(protein_bags.get(final_index),0,50));
		System.out.println("Set 2: "+protein_bags.get(final_index).subList(50, 100).toString()+" sum: "+sumofArray(protein_bags.get(final_index),50,100));
		System.out.println("Differnece is "+Math.abs(max_sum - fitness.get(final_index)));
	}
	
	/*
	 * mutate the list
	 */
	public static void mutation(){
		Random ind = new Random();
		for(int y=0; y<prob.size(); y++){
			if(mutationRate >= prob.get(y)){
				int select_ind = ind.nextInt(size-1);
				System.out.println(y);
				chromosomes.get(y).set(chromosomes.get(y).size()-1, lists.get(select_ind));
			}
		}
	}
	
	/*
	 * crossover the chromosomes and generate new parents
	 */
	public static void crossover(){
		Random r = new Random();
		for(int i=0; i<pairUp.length; i++){
			for(int j=0; j<pairUp[i].length; j++){
				if(pairUp[i][j].get(0) == 1){
					pairUp[i][j].set(0, 0);
					pairUp[j][i].set(0, 0);
					int indx = r.nextInt(size)+1;
					List<Integer> tempList11 = new ArrayList<Integer>(), tempList12 = new ArrayList<Integer>();
					List<Integer> tempList21 = new ArrayList<Integer>(), tempList22 = new ArrayList<Integer>();
					Collections.copy(chromosomes.get(i).subList(0, indx), tempList11);
					Collections.copy(chromosomes.get(i).subList(indx, chromosomeSize), tempList12);
					Collections.copy(chromosomes.get(j).subList(0, indx), tempList21);
					Collections.copy(chromosomes.get(j).subList(indx, chromosomeSize), tempList22);
					List<Integer> newList1 = new ArrayList<Integer>(tempList11);
					newList1.addAll(tempList22);
					List<Integer> newList2 = new ArrayList<Integer>(tempList21);
					newList2.addAll(tempList12);
					newChromosomes.add(newList1);
					newChromosomes.add(newList2);
				}
			}//inside for loop
		}//exterior for loop
		chromosomes.clear();
		Collections.copy(newChromosomes, chromosomes);
	}
	
	/*
	 * pairing the lists/chromosomes to do the crossover to create children
	 */
	public static void pairing(){
		Random r = new Random();
		int index1 = 0, index2 = 0;
		for(int i=0; i<population;){
			if(i%2==1){
				index2 = r.nextInt((population/2)+1);
				if(pairUp[indices.get(index1)][indices.get(index2)].get(0) == 0 && index1 != index2){
					pairUp[indices.get(index1)][indices.get(index2)].add(1);
					pairUp[indices.get(index2)][indices.get(index1)].add(1);
					i++;
				}else{
					i--;
				}
			}else{
				index1 = r.nextInt((population/2)+1);
				i++;
			}
		}
	}
	
	/*
	 * sets the roulette wheel in motion to find the next pick for crossover
	 */
	public static int rouletteWheel(){
		int i = 0;
		int index = -1;
		int max = 0;
		while(i < fitness.size()){
			if(max <= fitness.get(i) && !indices.contains(i)){
				max = fitness.get(i);
				index = i;
			}
			i++;
		}
		return index;
	}
	
	/*
	 * finds the probability of the chromosome
	 */
	public static void findProb(){
		double sum = sumofArray(fitness,0,fitness.size());
		for(int i=0; i<fitness.size(); i++){
			prob.add(fitness.get(i)/sum);
		}
	}
	
	/*
	 * Finds the fitness of the list
	 */
	public static void findFitness(int maxSum){
		for(int i=0; i<protein_bags.size(); i++){
			int firstHS = sumofArray(protein_bags.get(i),0,size/2);
			int secondHS = sumofArray(protein_bags.get(i),size/2,size);
			int diff = Math.abs(firstHS-secondHS);
			fitness.add(Math.abs(maxSum-diff));
		}
	}
	
	/*
	 * creates protein bags which have randomly selected numbers from the chromosome list
	 */
	public static void filter(){
		for(int i=0; i<chromosomes.size(); i++){
			int k = 0;
			List<Integer> list = chromosomes.get(i);
			List<Integer> temp = new ArrayList<Integer>();
			for(int j=0; j<list.size(); j++){
				if(!temp.contains(list.get(j))&& lists.contains(list.get(j))){
					temp.add(list.get(j));
					k++;
				}
				if(k==size){
					Collections.shuffle(temp);
					protein_bags.add(temp);
					break;
				}
			}
		}
	}
	
	/*
	 * Makes random numbers for each list so that each of the numbers in the list repeat at least once
	 */
	public static void createParents(){
		Random r = new Random();
		for(int i=0; i<population; i++){
			List<Integer> temp = new ArrayList<Integer>();
			Collections.copy(lists, temp);
			Collections.shuffle(temp);
			for(int j=0; j<chromosomeSize-size; j++){
				int _int = r.nextInt(range)+1;
				temp.add(_int);
			}
			chromosomes.add(temp);
		}
	}
	
	/*
	 * check if the obtained fitness matches the derived fitness
	 */
	public static boolean solutionRanged(int total){
		for(int i=0; i<fitness.size(); i++){
			if(Math.abs(total - fitness.get(i)) < possibleSolutionRanged){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Gets the sum of the entire list
	 */
	public static int sumofArray(List<Integer> p, int from, int to){
		int sum = 0;
		for(int i=from; i<to; i++){
			sum += p.get(i);
		}
		return sum;
	}
	
	public static void print(List<Integer> list){
		System.out.print("Initial List: ");
		for(int s : list){
			System.out.print(s+" ");
		}
		System.out.println();
	}
	
	public static void print2Dlist(List<List<Integer>> list){
		for(int u=0; u<list.size(); u++){
			for(int v=0; v<list.get(u).size();v++){
				System.out.print(list.get(u).get(v)+" ");
			}
			System.out.println();
		}
	}
	
	/*
	 * Creates the list of distinct numbers for operation
	 */
	public static void generateRandomNumbers(List<Integer> numbers, int upto, int start){
		int num;
		Random r = new Random();
		for (int i = 0; i < size;) {
			num = r.nextInt(upto)+start;
			
			if(!numbers.contains(num)){
				numbers.add(num);
				i++;
			}
		}
	}

}

