import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Ex2
{	
	private String type;
	private int amount;
	private int num_of_points;
	private int points[][];
	
	public Ex2(String input_file)
	{
		try
		{
			String[] input_lines = readLines(input_file);
			
			this.type = input_lines[0];
			
			this.amount = Integer.parseInt(input_lines[1]);
			
			this.num_of_points = input_lines.length - 2;
			
			this.points = new int[this.num_of_points][2];
			
			for(int i = 0; i < this.num_of_points; i ++)
			{
				String current_points[] = input_lines[2 + i].split(",");
				this.points[i][0] = Integer.parseInt(current_points[0]); 
				this.points[i][1] = Integer.parseInt(current_points[1]); 
			}
			
			System.out.println(this.type);
			System.out.println(this.amount);
			
			for(int i = 0; i < this.num_of_points; i ++)
			{
				System.out.println(this.points[i][0] + "," + this.points[i][1]);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
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
		Ex2 ex2 = new Ex2("C:\\dan\\AI\\HW\\HW2_3\\hw3\\input.txt");
	}

}
