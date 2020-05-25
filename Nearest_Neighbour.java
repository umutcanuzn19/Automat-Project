import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Nearest_Neighbour extends Canvas {
	
	public static int x_Coordinates = 0; 
	public static int y_Coordinates = 0; 
	public static ArrayList<Float> X_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> Y_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> xL_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> xR_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> yL_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> yR_Coordinates = new ArrayList<Float>();
	public static ArrayList<Float> xRemaningCoordinates = new ArrayList<Float>();
	public static ArrayList<Float> yRemaningCoordinates = new ArrayList<Float>();
	public static float minDist = 0 ;
	public static float ShortestDistance = 1000000000;
	public static float CurrentDistance = 0; 
	public static int CurrentIndex = 0;
	public static float NextCity_X = 0; 
	public static float NextCity_Y = 0; 
	public static ArrayList<Float> NextCityX = new ArrayList<Float>();  //Store the X locations of the next city 
	public static ArrayList<Float> NextCityY = new ArrayList<Float>();  //Store the Y locations of the next city
	public static float R1 , R2 ,R3 = 0;
	public static ArrayList<Float> ShortestDistances = new ArrayList<Float>();  //Stores the calculated shortest distances in the array 
	public static Stack<Float> CurrentShortestDistances = new Stack<Float>(); 
	public static Stack<Float> CurrentShortestCoordinateX = new Stack<Float>(); 
	public static Stack<Float> CurrentShortestCoordinateY = new Stack<Float>(); 
	public static Random RandomCity = new Random();
	public static float FirstCityX = 0; 
	public static float FirstCityY = 0; 
	public static float LastCityX = 0; 
	public static float LastCityY = 0; 
	public static float LastDistance = 0; 
	public static float TotalDistance1 = 0;
	public static float TotalDistance2 = 0;
	public static float LastTotalDistance = 0 ;
	public static float LastElementX = 0; 
	public static float LastElementY = 0; 
	public static boolean LastElementsRemoved = false;
	public static float temp = 0 ;
	public static float temp2 = 0 ;

	
	//float CurrentDistance = (float) Math.sqrt(Math.abs((X_Coordinates.get(j) - X_Coordinates.get(i)) * (X_Coordinates.get(j) - X_Coordinates.get(i))) + Math.abs((Y_Coordinates.get(j) - Y_Coordinates.get(i)) * (Y_Coordinates.get(j) - Y_Coordinates.get(i))));

	
	public static void main(String[] args) throws FileNotFoundException  {
		// TODO Auto-generated method stub
		
		
		float[] XYCoordinates = readFile("C:\\Users\\tasso\\OneDrive\\Masaüstü\\YeniDönem\\Otomat Teoremi\\proje\\att48_xy.txt");

		for(int i = 0; i < XYCoordinates.length; i = i + 2)
		{
			//Use this loop to store X Coordinates
			X_Coordinates.add(XYCoordinates[i]);
		}

		for(int j = 1; j < XYCoordinates.length; j = j + 2)
		{
			//Use this loop to store Y Coordinates
			Y_Coordinates.add(XYCoordinates[j]);
		}

		
		 Nearest_Neighbour_Window();
		
		
		divide_Conquer_Nearest_Neighbour_Algorithm();



	}
	

	public static float[] readFile(String file) throws FileNotFoundException
	{
		File Coordinate_File = new File("C:\\Users\\tasso\\OneDrive\\Masaüstü\\YeniDönem\\Otomat Teoremi\\proje\\att48_xy.txt");
		Scanner ScanFile = new Scanner(Coordinate_File);
		int ctr = 0; 
		while(ScanFile.hasNextInt())
		{
			ctr++; 
			ScanFile.nextInt(); 
		}
		float[] Coordinates = new float [ctr]; 
		
		Scanner Get_Numbers = new Scanner(Coordinate_File); 
		for(int i = 0; i < Coordinates.length; i++)
		{
			Coordinates[i] = Get_Numbers.nextFloat(); 
		}
		
		return Coordinates;
		
	}
	
	
	
	
	public static void Nearest_Neighbour_Window()
	{
		
		 JFrame Main_Window = new JFrame("Nearest_Neighbour");
		 Canvas canvas = new Nearest_Neighbour();
	     canvas.setSize(800, 500);
	     canvas.setBackground(Color.white);
	     Main_Window.add(canvas);
	     Main_Window.pack();
	     Main_Window.setVisible(true);
			
		
	}
	
	public void paint(Graphics g)
    {
        g.setColor(Color.blue);
        for(int i = 0; i < X_Coordinates.size(); i++){  //This looks at all the x and y coordinates at the same time in both arraylists
            g.fillOval((Math.round(X_Coordinates.get(i)) % 800),(Math.round(Y_Coordinates.get(i)) % 500), 10, 10);
            
        }
    }
	
	public static void divide_Conquer_Nearest_Neighbour_Algorithm()
	{
		for(int i= 0 ; i< X_Coordinates.size()/2;i++){
			xL_Coordinates.add(X_Coordinates.get(i));
		}
		for(int i= X_Coordinates.size()/2 ; i < X_Coordinates.size() - 1;i++){
			xR_Coordinates.add(X_Coordinates.get(i));
		}

		for(int i= 0 ; i< Y_Coordinates.size()/2;i++){
			yL_Coordinates.add(Y_Coordinates.get(i));
		}
		for(int i= Y_Coordinates.size()/2 ; i < Y_Coordinates.size() - 1;i++){
			yR_Coordinates.add(Y_Coordinates.get(i));
		}




		int RandomIndex = RandomCity.nextInt(xL_Coordinates.size());
		System.out.println("Random Index = " + RandomIndex);
		
		System.out.println("The first random city is : " + xL_Coordinates.get(RandomIndex) + ", " + yL_Coordinates.get(RandomIndex));
		
		if(NextCityX.isEmpty())
		{
			for(int i = 0; i < xL_Coordinates.size(); i++) //Calculate the shortest distance for the first city
			{
				float CurrentDistance = (float) Math.sqrt(Math.abs((xL_Coordinates.get(RandomIndex) - xL_Coordinates.get(i)) * (xL_Coordinates.get(RandomIndex) - xL_Coordinates.get(i))) + Math.abs((yL_Coordinates.get(RandomIndex) - yL_Coordinates.get(i)) * (yL_Coordinates.get(RandomIndex) - yL_Coordinates.get(i))));
				
				if(CurrentDistance == 0)
				{
					FirstCityX = xL_Coordinates.get(i);
					FirstCityY = yL_Coordinates.get(i);
					
					xL_Coordinates.remove(i); //Remove the coordinates from the array after storing it in another variable
					yL_Coordinates.remove(i);
					
				}
				else
				{
					if(CurrentDistance <= ShortestDistance && LastElementsRemoved == false)
					{
						ShortestDistance = CurrentDistance; 
						
						System.out.println("Shortest Distance so far : " + ShortestDistance + ", at " + xL_Coordinates.get(i) + ", " + yL_Coordinates.get(i));
						
						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xL_Coordinates.get(i));
						CurrentShortestCoordinateY.push(yL_Coordinates.get(i));
					}
				}
				
				
				
				if(i == xL_Coordinates.size() - 1)
				{
					ShortestDistances.add(CurrentShortestDistances.peek());
					NextCityX.add(CurrentShortestCoordinateX.peek());
					NextCityY.add(CurrentShortestCoordinateY.peek());
					
					CurrentShortestDistances.clear(); 
					CurrentShortestCoordinateX.clear();
					CurrentShortestCoordinateY.clear();
					
					ShortestDistance = 1000000000;
				}
			}
		}
		
		
		
		for(int i = 0; i < NextCityX.size(); i++)
		{
			for(int j = 0; j < xL_Coordinates.size(); j++)
			{
				float CurrentDistance = (float) Math.sqrt(Math.abs((NextCityX.get(i) - xL_Coordinates.get(j)) * (NextCityX.get(i) - xL_Coordinates.get(j))) + Math.abs((NextCityY.get(i) - yL_Coordinates.get(j)) * (NextCityY.get(i) - yL_Coordinates.get(j))));
				
				
				System.out.println( NextCityX.get(i) + ", " + NextCityY.get(i) + "   " + xL_Coordinates.get(j) + ", " + yL_Coordinates.get(j) + "     " + CurrentDistance);
				
				if(CurrentDistance == 0 && j < X_Coordinates.size() - 1)
				{
					
					xL_Coordinates.remove(j);
					yL_Coordinates.remove(j);
				}
				
				else if(CurrentDistance == 0 && j == xL_Coordinates.size() - 1)
				{
					
					LastElementsRemoved = true; 
					
				}
				
				if(LastElementsRemoved == false)
				{
					if(CurrentDistance <= ShortestDistance && CurrentDistance != 0 && CurrentDistance != ShortestDistances.get(ShortestDistances.size() - 1))
					{
						
						ShortestDistance = CurrentDistance; 
						System.out.println("Smallest Distance : " + ShortestDistance);
						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xL_Coordinates.get(j));
						CurrentShortestCoordinateY.push(yL_Coordinates.get(j));
					}
					
					if(j == xL_Coordinates.size() - 1)
					{
						
						ShortestDistances.add(CurrentShortestDistances.peek());
						NextCityX.add(CurrentShortestCoordinateX.peek());
						NextCityY.add(CurrentShortestCoordinateY.peek());
						
						ShortestDistance = 1000000000;
						
					}
				}
				else
				{
					if(CurrentDistance <= ShortestDistance && CurrentDistance != 0 && CurrentDistance != ShortestDistances.get(ShortestDistances.size() - 1) && j != xL_Coordinates.get(xL_Coordinates.size() - 1))
					{
						ShortestDistance = CurrentDistance; 
						System.out.println("Smallest Distance : " + ShortestDistance);
						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xL_Coordinates.get(j));
						CurrentShortestCoordinateY.push(yL_Coordinates.get(j));
					}
					if(j == xL_Coordinates.size() - 1)
					{
						
						ShortestDistances.add(CurrentShortestDistances.peek());
						NextCityX.add(CurrentShortestCoordinateX.peek());
						NextCityY.add(CurrentShortestCoordinateY.peek());
						
						ShortestDistance = 1000000000;
					}
				}
					
				
			}
			
			if(xL_Coordinates.size() == 2 && yL_Coordinates.size() == 2)
			{
				LastCityX = xL_Coordinates.get(1);
				LastCityY = yL_Coordinates.get(1);
				
				LastDistance = (float) Math.sqrt(Math.abs((LastCityX - FirstCityX) * (LastCityX - FirstCityX)) + Math.abs((LastCityY - FirstCityY) * (LastCityY - FirstCityY)));
				
				
				
				break;
			}
				
		}
		TotalDistance1 = TotalDistanceCalculator() + LastDistance;


		System.out.println("");
		System.out.println("_______________RESULTS______________");
		System.out.println("Remaining X and Y Coordinates = " + xL_Coordinates.size());
		System.out.println("Shortest distances : " + ShortestDistances);
		System.out.println("Remaining X Coordinates : " + xL_Coordinates);
		System.out.println("Remaining Y Coordinates : " + yL_Coordinates);

		System.out.println("Next City X : " + NextCityX);
		System.out.println("Next City Y : " + NextCityY);

		System.out.println("Final Distance : " + LastDistance);
		System.out.println("Total Distance half1 : " + TotalDistance1);

///////////////////////


		NextCityX.clear();
		NextCityY.clear();
		ShortestDistances.clear();

		CurrentShortestDistances.empty();
		CurrentShortestCoordinateX.empty();
		CurrentShortestCoordinateY.empty();

		CurrentDistance = 0 ;
		ShortestDistance = 1000000000;

		CurrentIndex = 0;
		NextCity_X = 0;

		NextCity_Y = 0;

		x_Coordinates = 0 ;
		y_Coordinates = 0 ;

		FirstCityX = 0;
		FirstCityY = 0;

		LastCityX = 0;
		LastCityY = 0;
		LastDistance = 0;

		LastElementsRemoved = false;


		int RandomIndex2 = RandomCity.nextInt(xR_Coordinates.size());
		System.out.println("Random Index 2 = " + RandomIndex2);

		System.out.println("The first random city is (second part) : " + xR_Coordinates.get(RandomIndex2) + ", " + yR_Coordinates.get(RandomIndex2));

		if(NextCityX.isEmpty())
		{
			for(int i = 0; i < xR_Coordinates.size(); i++) //Calculate the shortest distance for the first city
			{
				float CurrentDistance = (float) Math.sqrt(Math.abs((xR_Coordinates.get(RandomIndex2) - xR_Coordinates.get(i)) * (xR_Coordinates.get(RandomIndex2) - xR_Coordinates.get(i))) + Math.abs((yR_Coordinates.get(RandomIndex2) - yR_Coordinates.get(i)) * (yR_Coordinates.get(RandomIndex2) - yR_Coordinates.get(i))));

				if(CurrentDistance == 0)
				{
					FirstCityX = xR_Coordinates.get(i);
					FirstCityY = yR_Coordinates.get(i);

					xR_Coordinates.remove(i); //Remove the coordinates from the array after storing it in another variable
					yR_Coordinates.remove(i);

				}
				else
				{
					if(CurrentDistance <= ShortestDistance && LastElementsRemoved == false)
					{
						ShortestDistance = CurrentDistance;

						System.out.println("Shortest Distance so far : " + ShortestDistance + ", at " + xR_Coordinates.get(i) + ", " + yR_Coordinates.get(i));

						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xR_Coordinates.get(i));
						CurrentShortestCoordinateY.push(yR_Coordinates.get(i));
					}
				}



				if(i == xR_Coordinates.size() - 1)
				{
					ShortestDistances.add(CurrentShortestDistances.peek());
					NextCityX.add(CurrentShortestCoordinateX.peek());
					NextCityY.add(CurrentShortestCoordinateY.peek());

					CurrentShortestDistances.clear();
					CurrentShortestCoordinateX.clear();
					CurrentShortestCoordinateY.clear();

					ShortestDistance = 1000000000;
				}
			}
		}



		for(int i = 0; i < NextCityX.size(); i++)
		{
			for(int j = 0; j < xR_Coordinates.size(); j++)
			{
				float CurrentDistance = (float) Math.sqrt(Math.abs((NextCityX.get(i) - xR_Coordinates.get(j)) * (NextCityX.get(i) - xR_Coordinates.get(j))) + Math.abs((NextCityY.get(i) - yR_Coordinates.get(j)) * (NextCityY.get(i) - yR_Coordinates.get(j))));


				System.out.println( NextCityX.get(i) + ", " + NextCityY.get(i) + "   " + xR_Coordinates.get(j) + ", " + yR_Coordinates.get(j) + "     " + CurrentDistance);

				if(CurrentDistance == 0 && j < xR_Coordinates.size() - 1)
				{

					xR_Coordinates.remove(j);
					yR_Coordinates.remove(j);
				}

				else if(CurrentDistance == 0 && j == xR_Coordinates.size() - 1)
				{

					LastElementsRemoved = true;

				}

				if(LastElementsRemoved == false)
				{
					if(CurrentDistance <= ShortestDistance && CurrentDistance != 0 && CurrentDistance != ShortestDistances.get(ShortestDistances.size() - 1))
					{

						ShortestDistance = CurrentDistance;
						System.out.println("Smallest Distance : " + ShortestDistance);
						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xR_Coordinates.get(j));
						CurrentShortestCoordinateY.push(yR_Coordinates.get(j));
					}

					if(j == xR_Coordinates.size() - 1)
					{

						ShortestDistances.add(CurrentShortestDistances.peek());
						NextCityX.add(CurrentShortestCoordinateX.peek());
						NextCityY.add(CurrentShortestCoordinateY.peek());

						ShortestDistance = 1000000000;

					}
				}
				else
				{
					if(CurrentDistance <= ShortestDistance && CurrentDistance != 0 && CurrentDistance != ShortestDistances.get(ShortestDistances.size() - 1) && j != xR_Coordinates.get(xR_Coordinates.size() - 1))
					{
						ShortestDistance = CurrentDistance;
						System.out.println("Smallest Distance : " + ShortestDistance);
						CurrentShortestDistances.push(ShortestDistance);
						CurrentShortestCoordinateX.push(xR_Coordinates.get(j));
						CurrentShortestCoordinateY.push(yR_Coordinates.get(j));
					}
					if(j == xR_Coordinates.size() - 1)
					{

						ShortestDistances.add(CurrentShortestDistances.peek());
						NextCityX.add(CurrentShortestCoordinateX.peek());
						NextCityY.add(CurrentShortestCoordinateY.peek());

						ShortestDistance = 1000000000;
					}
				}


			}

			if(xR_Coordinates.size() == 2 && yR_Coordinates.size() == 2)
			{
				LastCityX = xR_Coordinates.get(1);
				LastCityY = yR_Coordinates.get(1);

				LastDistance = (float) Math.sqrt(Math.abs((LastCityX - FirstCityX) * (LastCityX - FirstCityX)) + Math.abs((LastCityY - FirstCityY) * (LastCityY - FirstCityY)));



				break;
			}

		}

		for(int i=0 ;i<xR_Coordinates.size();i++){
			for (int j = 0 ;i<xR_Coordinates.size();i++){
				temp = (float) Math.sqrt(Math.abs((xR_Coordinates.get(i) - xL_Coordinates.get(j)) * (xR_Coordinates.get(i) - xL_Coordinates.get(j))) + Math.abs((yR_Coordinates.get(i) - yL_Coordinates.get(j)) * (yR_Coordinates.get(i) - yL_Coordinates.get(j))));
				if(temp2==0){
					temp2 = temp ;
				}else if(temp<temp2){
					temp2 = temp ;
				}
			}
		}
		LastDistance = temp2 ;
		TotalDistance2 = TotalDistanceCalculator() + LastDistance;

		LastTotalDistance = TotalDistance1 + TotalDistance2 ;
		System.out.println("");
		System.out.println("_______________RESULTS______________");
		System.out.println("Remaining X and Y Coordinates = " + xR_Coordinates.size());
		System.out.println("Shortest distances : " + ShortestDistances);
		System.out.println("Remaining X Coordinates : " + xR_Coordinates);
		System.out.println("Remaining Y Coordinates : " + yR_Coordinates);

		System.out.println("Next City X : " + NextCityX);
		System.out.println("Next City Y : " + NextCityY);

		System.out.println("Final Distance : " + LastDistance);
		System.out.println("Total Distance half 2: " + TotalDistance2);
		System.out.println("Total Distance of halves together : "+LastTotalDistance);
	/*
		R1 = (float) Math.sqrt(Math.abs((xL_Coordinates.get(0)-xL_Coordinates.get(1))*(xL_Coordinates.get(0)-xL_Coordinates.get(1)+(yL_Coordinates.get(0)-yL_Coordinates.get(1))*(yL_Coordinates.get(0)-yL_Coordinates.get(1)))));
		// Distance of left remaining cities
		R2 = (float) Math.sqrt(Math.abs((xR_Coordinates.get(0)-xR_Coordinates.get(1))*(xR_Coordinates.get(0)-xR_Coordinates.get(1)+(yR_Coordinates.get(0)-yR_Coordinates.get(1))*(yR_Coordinates.get(0)-yR_Coordinates.get(1)))));
		// Distance of right remaining cities
		R3 = (float) Math.sqrt(Math.abs((xL_Coordinates.get(1)-xR_Coordinates.get(1))*(xL_Coordinates.get(1)-xR_Coordinates.get(1)+(yL_Coordinates.get(1)-yR_Coordinates.get(1))*(yL_Coordinates.get(1)-yR_Coordinates.get(1)))));
		// Combine of two areas
		LastTotalDistance = R1 + R2 + R3 + TotalDistance2 + TotalDistance1 ;
		//System.out.println("R1 " + R1 + " R2 "+ R2+ " R3 " + R3);
		System.out.println("Total Distance : "+ LastTotalDistance);

	*/
	/*
	 */
		/*
		for(int i= 0 ; i<xR_Coordinates.size();i++) {
			xRemaningCoordinates.add(xR_Coordinates.get(i));
		}
			for (int j=0;j<xR_Coordinates.size();j++){
				xRemaningCoordinates.add(xL_Coordinates.get(j));

		}
		for(int i= 0 ; i<xR_Coordinates.size();i++) {
			yRemaningCoordinates.add(yR_Coordinates.get(i));
		}
		for (int j=0;j<yR_Coordinates.size();j++){
			yRemaningCoordinates.add(yL_Coordinates.get(j));
		}
		System.out.println(xRemaningCoordinates + " "+ yRemaningCoordinates);
		*/



	}


	public static float TotalDistanceCalculator()
	{
		float SemiTotalDistance = 0; 
		for(int i = 0; i < ShortestDistances.size(); i++)
		{
			SemiTotalDistance = SemiTotalDistance + ShortestDistances.get(i);
		}
		
		
		
		return SemiTotalDistance;
	}
}
