import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import za.ac.wits.snake.DevelopmentAgent;

class Direction
{
	static int North = 0 ;
	static int South = 1 ;
	static int West = 2 ;
	static int East = 3 ;
	static int Left = 4 ;
	static int Straight = 5 ;
	static int Right = 6 ;
	
	public static int NorthOrSouth()
	{
		//return north or south randomly
		return new int []{North,South}[new Random().nextInt(2)] ;
	}
	
	public static int EastOrWest()
	{
		//return East or West randomly
		return new int []{East,West}[new Random().nextInt(2)] ;
	}
}

class Snake
{
	//keeps track of where the snake is headed cardinally
	private int direction = Direction.Straight ;
	//keeps track of existence of snake in game
	private boolean alive = true ;
	//true if this snake is a zombie
	private boolean zombie = true ;
	//length of snake defaults to 5 for zombies
	private int length = 5 ;
	//keeps number of kills of the snake
	private int kills = 0 ;
	//keeps position of it's head
	private int head[] ;
	
	//can either be player snake or zombie
	Snake ( boolean player , String[] snakeStats )
	{
		if ( player )
		{
			//set life state and length and kills
			zombie = !player ;
			
			if ( snakeStats[0].equals("dead") )
			{
				alive = false ;
			}
			else
			{
				length = Integer.parseInt(snakeStats[1]) ;
				kills = Integer.parseInt(snakeStats[2]) ;
				
				//set directions of player
				setDirection ( snakeStats[3] , snakeStats[4] ) ;
			}
		}
		else
		{
			//set direction of zombie
			setDirection ( snakeStats[0] , snakeStats[1] ) ;
		}
	}
	
	private void setDirection( String head , String kink )
	{
		int headA[] = Stream.of( head.split(",") ).mapToInt(Integer::parseInt).toArray() ;
		//set head member variable of the snake as well
		this.head = headA ;
		
		int kinkA[] = Stream.of( kink.split(",") ).mapToInt(Integer::parseInt).toArray() ;
		
		//determine direction of the snake
		if ( headA[1] == kinkA[1] )
		{
			//snake moving East or West
			if ( headA[0] > kinkA[0] )
			{
				//snake moving East
				this.direction = Direction.East ;
			}
			else
				this.direction = Direction.West ;
		}
		else
		{
			//Snake moving South or North
			if ( headA[1] < kinkA[1] )
			{
				//snake moving up
				this.direction = Direction.North ;
			}
			else
				this.direction = Direction.South ;
		}
	}
	
	public int getDirection()
	{
		return this.direction ;
	}
	
	public boolean isZombie()
	{
		return this.zombie ;
	}
	
	public boolean isAlive()
	{
		return this.alive ;
	}
	
	int getLength()
	{
		return this.length ;
	}
	
	int[] getHeadCoords()
	{
		return this.head ;
	}

	int getKills()
	{
		return kills ;
	}
}

class Obstacle
{
	//Snake obsacle = new Snake;
	
}

public class MyAgent extends DevelopmentAgent {

    public static void main(String args[]) throws IOException {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }
    
    public boolean isSafe(ArrayList<String> coor_list, String next_move)
    {
    	boolean safe = true;
    	for (int i=0; i<coor_list.size(); i++)
    	    if (next_move.equals(coor_list.get(i))) safe = false;
    	return safe;
    }
    
    public int getDirectionToGoal ( int [] target , Snake mySnake )
    {    	
    	if ( target[1] == mySnake.getHeadCoords()[1] )
    	{
    		if ( target[0] > mySnake.getHeadCoords()[0] )
    		{
    			//target is to our East
    			if ( mySnake.getDirection() == Direction.West )
    				//cannot move directly East
    				return Direction.NorthOrSouth() ;
    			else
    				return Direction.East ;
    		}
    		else
    		{
    			//target is to our West
    			if ( mySnake.getDirection() == Direction.East )
    				//cannot move directly West
    				return Direction.NorthOrSouth() ;
    			else
    				return Direction.West ;
    		}
    		
    	}else if ( target[1] < mySnake.getHeadCoords()[1])
    	{
    		//target above us
    		if ( mySnake.getDirection() == Direction.East || mySnake.getDirection() == Direction.West )
    			return Direction.North ;
    		else if ( mySnake.getDirection() == Direction.North )
    			return Direction.Straight ;
    		else
    			//we can't go directly up
    			return Direction.EastOrWest() ;
    	}
    	else
    	{
    		//target below us
    		if ( mySnake.getDirection() == Direction.East || mySnake.getDirection() == Direction.West )
    			return Direction.South ;
    		else if ( mySnake.getDirection() == Direction.South )
    			return Direction.Straight ;
    		else
    			//we can't go directly down
    			return Direction.EastOrWest() ;
    	}
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            Snake zombie;
        	ArrayList<String> obstacles = new ArrayList<String>();
        	for (int i = 0; i < temp.length; i++) {
				//System.out.println("log "+temp[i]);
			}
            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }
                //String apple = line;
                //storing apple into usable array
                int apple[] = Stream.of(line.split(" " )).mapToInt(Integer::parseInt).toArray() ;
                
                //do stuff with apple
                for (int i = 0; i < 3; i++) {
                    String zombieLine = br.readLine();
                    //obstacles.add(zombieLine);
                    String[] sLine = zombieLine.split(" ");
                    obstacles.add(sLine[1]);
                    //obstacles.add(sLine[2]);
                    obstacles.add(sLine[0]);

                    System.out.println("log "+obstacles.get(i));
                    
                } 
//                for (int i = 0; i < obstacles.size(); i++) {
//                	System.out.println("log "+obstacles.get(i));
//				}
                int mySnakeNum = Integer.parseInt(br.readLine());
                
                for (int i = 0; i < nSnakes; i++) {
                    String[] snakeLine = br.readLine().split(" ");

                    if (i == mySnakeNum) {
                        //hey! That's me :)
                    	
                    	//check for position relative to apple
                    	Snake mySnake = new Snake ( true , snakeLine ) ;
                    	String myNextMove = String.valueOf((mySnake.getHeadCoords()[0]+2) +","
                    	+(mySnake.getHeadCoords()[1]+2));
                    	//****Testing if we're getting the right snake directions
                        boolean safeMove = isSafe(obstacles, myNextMove);
                    	System.out.println ( "log moving " + mySnake.getDirection() );
                    	System.out.println ( "log supposed to move " + getDirectionToGoal ( apple , mySnake ) ) ;
                    	System.out.println("log movieei"+ String.valueOf(mySnake.getHeadCoords()[0]+2 
                    			+","+ (mySnake.getHeadCoords()[1]+2)));
                    	int moveTo = getDirectionToGoal ( apple , mySnake);
                    	if (safeMove) {
                    		System.out.println ( getDirectionToGoal ( apple , mySnake ) ) ;
						}/*else if (moveTo==Direction.East) {
							moveTo = Direction.NorthOrSouth();
							//System.out.println ( getDirectionToGoal ( apple , mySnake ) ) ;
						}else {
							if (moveTo==Direction.West) moveTo = Direction.NorthOrSouth();
							//else moveTo = Direction.East;
							
						}
                    	for (int j = 0; j < obstacles.size(); j++) {
							String[] zomCoord = obstacles.get(j).split(",");
							int fooX = Integer.parseInt(zomCoord[0]);
							int fooY = Integer.parseInt(zomCoord[1]);
							if (fooX > mySnake.getHeadCoords()[0]) {
								if (moveTo==Direction.East) {
									if (fooY>mySnake.getHeadCoords()[1]) {
										moveTo = Direction.
									}
								}
							}
						}*/
                    	/*
                    	 * int moveTo = getDirectionToGoal ( apple , mySnake);
                    	
                    	for (int j = 0; j < obstacles.size(); j++) {
							String[] zomCoord = obstacles.get(j).split(",");
							int fooX = Integer.parseInt(zomCoord[0]);
							int fooY = Integer.parseInt(zomCoord[1]);
							if (fooX == mySnake.getHeadCoords()[0]) {
								if (moveTo==Direction.East) {
									if (fooY==mySnake.getHeadCoords()[1]) {
										moveTo = Direction.South;
									}else moveTo = Direction.North;
								}
							}else {
								if (moveTo==Direction.West) {
									if (fooY==mySnake.getHeadCoords()[1]) {
										moveTo = Direction.South;
									}else moveTo = Direction.North;
								}
							}
						}
                    	System.out.println (moveTo) ;
                    	 */
                    	//System.out.println ( getDirectionToGoal ( apple , mySnake ) ) ;
						
                    }
                    //do stuff with snakes
                } 
                // finished reading, calculate move:
                //System.out.println("log calculating...");
                //System.out.println("log zombie ahead...");
                //int move = new Random().nextInt(4);
                //System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}