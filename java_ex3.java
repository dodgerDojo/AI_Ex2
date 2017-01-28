import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class java_ex3
{	
	private String type;
	private int target_amount;
	private int num_of_points;
	private double points[][];
	ArrayList<List<Integer>> clusters; 

	/*
	 * Calcs the metric distance between 2 2D points.
	 */
	private double calcDistance(int index1, int index2)
	{
		return Math.sqrt((this.points[index1][0] - this.points[index2][0]) * 
						 (this.points[index1][0] - this.points[index2][0]) + 
						 (this.points[index1][1] - this.points[index2][1]) *
						 (this.points[index1][1] - this.points[index2][1]));
	}
	
	/*
	 * Returns the distance between 2 clusters according to the Single Link definition.
	 */
	private double getSingleLinkDistance(List<Integer> l1, List<Integer> l2)
	{
		boolean first_iteration = true;
		double min_distance = 0;
		
		// Iterate over each couple of points in the clusters
		for(Integer index1 : l1)
		{
			for(Integer index2 : l2)
			{
				// Calc the distance
				double current_dist = calcDistance(index1, index2);
				
				// Initialize the min. distance in case it's the first iteration
				if(first_iteration)
				{
					min_distance = current_dist;
					first_iteration = false;
					continue;
				}
				
				// Save the min. distance
				if(Double.compare(min_distance, current_dist) > 0)
				{
					min_distance = current_dist;
				}
			}
		}
		
		return min_distance;
	}
	
	/*
	 * Returns the distance between 2 clusters according to the Average Link definition.
	 */
	private double getAverageLinkDistance(List<Integer> l1, List<Integer> l2)
	{
		double dist_sum = 0;

		// Iterate over each couple of points in the clusters
		for(Integer index1 : l1)
		{
			for(Integer index2 : l2)
			{
				// Calculate the sum of all distances
				dist_sum += calcDistance(index1, index2);
			}
		}
		
		// Calculate the average of all distances
		double avg = dist_sum / (l1.size() * l2.size());

		return avg;
	}
	
	/*
	 * Returns the indexes [i,j] of 2 clusters to be merged according to the clustering algorithm.
	 * i -> cluster to merge into
	 * j -> cluster to be merged
	 */
	private int[] getClustersToMerge()
	{
		boolean first_iteration = true;
		double min_distance = 0;
		
		int i_j[] = new int[2];
		
		// Iterate over all existing couples of clusters.
		for(int i = 0; i < this.clusters.size(); i++)
		{
			for(int j = (i + 1); j < this.clusters.size(); j++)
			{
				double current_dist = 0;
				
				// Calculate each couple's distance according to the given algorithm.
				if(this.type.compareTo("single link") == 0)
				{
					current_dist = getSingleLinkDistance(this.clusters.get(i), this.clusters.get(j));
				}
				else
				{
					current_dist = getAverageLinkDistance(this.clusters.get(i), this.clusters.get(j));
				}
				
				// Initialize the min. distance in case it's the first iteration
				if(first_iteration)
				{
					min_distance = current_dist;
					first_iteration = false;
					i_j[0] = i;
					i_j[1] = j;
					continue;
				}
				
				// Save the min. distance
				if(Double.compare(min_distance, current_dist) > 0)
				{
					min_distance = current_dist;
					i_j[0] = i;
					i_j[1] = j;
				}				
			}
		}
		
		return i_j;
	}

	/*
	 * Finds the cluster which contains the given index
	 */	
	public int getClusterNumberOfPoint(int index)
	{
		// Iterate though all clusters
		for(int i = 0; i < this.clusters.size(); i++)
		{
			for(Integer point_index : this.clusters.get(i))
			{
				// When finding the given index -> return the cluster's position
				if(index == point_index)
				{
					return i+1;
				}
			}
		}

		return -1;
	}
	
	/*
	 * Reads text lines from a file
	 */
    public String[] readLines(String filename) throws IOException 
    {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }
	
	/*
	 * Writes text lines to a file
	 */
    public void writeStringToFile(String str, String file_path)
    {
        try
        {
            File newTextFile = new File(file_path);
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(str);
            fw.close();
        }
        
        catch (IOException iox) 
        {
            iox.printStackTrace();
        }	
    }
    
	/*
	 * This Class handles the execution of the exercise.
	 */	
	public java_ex3(String input_file) throws IOException
	{
			// Read the input file
			String[] input_lines = readLines(input_file);
			
			// Parse the input content: clusters, type, amount and all of the points given.
			
			this.clusters = new ArrayList<List<Integer>>();
		
			this.type = input_lines[0];
			
			this.target_amount = Integer.parseInt(input_lines[1]);
			
			this.num_of_points = input_lines.length - 2;
			
			this.points = new double[this.num_of_points][2];
			
			for(int i = 0; i < this.num_of_points; i ++)
			{
				String current_points[] = input_lines[2 + i].split(",");
				this.points[i][0] = Double.parseDouble(current_points[0]); 
				this.points[i][1] = Double.parseDouble(current_points[1]);
				
				// For each point created, insert it to its own new cluster.
				List<Integer> new_cluster = new ArrayList<Integer>();
				new_cluster.add(i);
				this.clusters.add(new_cluster);
			}
		}
	
	/*
	 * Runs the clustering algorithm over the given input
	 */	
	public void run()
	{
		// Merge clusters according to the clustering algorithm
		while(this.clusters.size() > this.target_amount)
		{
			int i_j[] = getClustersToMerge();
			this.clusters.get(i_j[0]).addAll(this.clusters.get(i_j[1]));
			this.clusters.remove(i_j[1]);
		}
		
		// Prepare the output result
		String output = "";
		for(int i = 0; i < this.points.length; i++)
		{
			output += getClusterNumberOfPoint(i) + String.format("%n");
		}
		
		// Write the result to the output file
		writeStringToFile(output, "output.txt");
	}
    
	/*
	 * Runs the exercise.
	 */
    public static void main(String[] args)
	{
		try
		{
			java_ex3 ex2 = new java_ex3("input.txt");
			ex2.run();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
