import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ex2
{	
	private String type;
	private int target_amount;
	private int num_of_points;
	private double points[][];
	ArrayList<List<Integer>> clusters; 

	private double calcDistance(int index1, int index2)
	{
		return Math.sqrt((this.points[index1][0] - this.points[index2][0]) * 
						 (this.points[index1][0] - this.points[index2][0]) + 
						 (this.points[index1][1] - this.points[index2][1]) *
						 (this.points[index1][1] - this.points[index2][1]));
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
		
		//System.out.println("local min dist: " + min_distance);
		return min_distance;
	}
	
	private double getAverageLinkDistance(List<Integer> l1, List<Integer> l2)
	{
		double dist_sum = 0;

		for(Integer index1 : l1)
		{
			for(Integer index2 : l2)
			{
				//System.out.println("dist: " + index1 + "," + index2 + " = " + calcDistance(index1, index2));
				dist_sum += calcDistance(index1, index2);
			}
		}
		
		//System.out.println("sizes: " + l1.size() + "," + l2.size());
		//System.out.println("dist_sum: " + dist_sum);
		double avg = dist_sum / (l1.size() * l2.size());
		//System.out.println("local avg: " + avg);
		return avg;
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
				double current_dist = 0;
				
				if(this.type.compareTo("single link") == 0)
				{
					//System.out.println("single link!");
					current_dist = getSingleLinkDistance(this.clusters.get(i), this.clusters.get(j));
				}
				else
				{
					//System.out.println("avg link!");
					current_dist = getAverageLinkDistance(this.clusters.get(i), this.clusters.get(j));
				}
				
				if(first_iteration)
				{
					min_distance = current_dist;
					first_iteration = false;
					i_j[0] = i;
					i_j[1] = j;
					continue;
				}
				
				//System.out.println("Comparing: " + current_dist + " , " + min_distance);
				
				if(Double.compare(min_distance, current_dist) > 0)
				{
					min_distance = current_dist;
					//System.out.println("~~~~~~GLOBAL New min!: " + min_distance);
					i_j[0] = i;
					i_j[1] = j;
				}				
			}
		}
		
		if(i_j[0] == 0 && i_j[1] == 0)
		{
			//System.out.println("EEEEEEEEEEEEEERRRRRRRRRRRRRRRROOOOOOOOOOORRRRRRRRR");
		}
		//System.out.println("******* final:  i: " + i_j[0] + " j: " + i_j[1] + " dist: " + min_distance);
		return i_j;
	}

	/*
	 * This Class handles the execution of the exercise.
	 */	
	
	public int getClusterNumberOfPoint(int index)
	{
		for(int i = 0; i < this.clusters.size(); i++)
		{
			for(Integer point_index : this.clusters.get(i))
			{
				if(index == point_index)
				{
					return i+1;
				}
			}
		}
		
		//System.out.println("FAILLLLLLLLLLLLLLLLLLLLLLL");
		return -1;
	}
	
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
    
	public Ex2(String input_file) throws IOException
	{
			String[] input_lines = readLines(input_file);
			
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
				
				List<Integer> new_cluster = new ArrayList<Integer>();
				new_cluster.add(i);
				this.clusters.add(new_cluster);
			}
			
			while(this.clusters.size() > this.target_amount)
			{
				int i_j[] = getClustersToMerge();
				this.clusters.get(i_j[0]).addAll(this.clusters.get(i_j[1]));
				this.clusters.remove(i_j[1]);
			}
			
			String output = "";
			for(int i = 0; i < this.points.length; i++)
			{
				output += getClusterNumberOfPoint(i) + String.format("%n");
			}
			
			writeStringToFile(output, "output.txt");
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
			Ex2 ex2 = new Ex2("input.txt");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
