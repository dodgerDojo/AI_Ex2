import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ex2
{	
	private String type;
	private int target_amount;
	private int num_of_points;
	private Point points[];
	ArrayList<List<Integer>> clusters; 

	private double calcDistance(int index1, int index2)
	{
		return Math.sqrt((this.points[index1].x - this.points[index2].x) * 
						 (this.points[index1].x - this.points[index2].x) + 
						 (this.points[index1].y - this.points[index2].y) *
						 (this.points[index1].y - this.points[index2].y));
	}
	private double getSingleLinkDistance(List<Integer> l1, List<Integer> l2)
	{
		boolean first_iteration = true;
		double min_distance = 0;
		
		for(Integer index1 : l1)
		{
			for(Integer index2 : l2)
			{
				double current_dist = calcDistance(index1, index2);
				
				//System.out.println("Dist: " + index1 + "," + index2 + " = " + current_dist);
				
				if(first_iteration)
				{
					min_distance = current_dist;
					first_iteration = false;
					continue;
				}
				
				if(Double.compare(min_distance, current_dist) > 0)
				{
					min_distance = current_dist;
				}
			}
		}
		
		System.out.println("local min dist: " + min_distance);
		return min_distance;
	}

	private int[] getClustersToMerge()
	{
		boolean first_iteration = true;
		double min_distance = 0;
		
		int i_j[] = new int[2];
		
		for(int i = 0; i < this.clusters.size(); i++)
		{
			for(int j = (i + 1); j < this.clusters.size(); j++)
			{
				double current_dist = getSingleLinkDistance(this.clusters.get(i), this.clusters.get(j));
				
				if(first_iteration)
				{
					min_distance = current_dist;
					first_iteration = false;
					continue;
				}
				
				System.out.println("Comparing: " + current_dist + " , " + min_distance);
				
				if(Double.compare(min_distance, current_dist) > 0)
				{
					min_distance = current_dist;
					System.out.println("~~~~~~GLOBAL New min!: " + min_distance);
					i_j[0] = i;
					i_j[1] = j;
				}				
			}
		}
		
		System.out.println("******* final:  i: " + i_j[0] + " j: " + i_j[1] + " dist: " + min_distance);
		return i_j;
	}

	/*
	 * This Class handles the execution of the exercise.
	 */	
	public Ex2(String input_file) throws IOException
	{
			String[] input_lines = readLines(input_file);
			
			this.clusters = new ArrayList<List<Integer>>();
		
			this.type = input_lines[0];
			
			this.target_amount = Integer.parseInt(input_lines[1]);
			
			this.num_of_points = input_lines.length - 2;
			
			this.points = new Point[this.num_of_points];
			
			for(int i = 0; i < this.num_of_points; i ++)
			{
				String current_points[] = input_lines[2 + i].split(",");
				this.points[i] = new Point();
				this.points[i].x = Integer.parseInt(current_points[0]); 
				this.points[i].y = Integer.parseInt(current_points[1]);
				
				List<Integer> new_cluster = new ArrayList<Integer>();
				new_cluster.add(i);
				this.clusters.add(new_cluster);
			}
			
			System.out.println(this.type);
			System.out.println(this.target_amount);
			
			for(int i = 0; i < this.num_of_points; i++)
			{
				System.out.println(this.points[i].x + "," + this.points[i].y);
				System.out.println(this.clusters.get(i));
			}
			
			while(this.clusters.size() > this.target_amount)
			{
				int i_j[] = getClustersToMerge();
				this.clusters.get(i_j[0]).addAll(this.clusters.get(i_j[1]));
				this.clusters.remove(i_j[1]);
				
				for(int i = 0; i < this.clusters.size(); i++)
				{
					System.out.println(this.clusters.get(i));
				}
			}
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
	 * Runs the exercise.
	 */
    public static void main(String[] args)
	{
		try
		{
			Ex2 ex2 = new Ex2("C:\\dan\\AI\\HW\\HW2_3\\hw3\\input2.txt");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
