/**
 * Player Class.
 * This class is in charge of the Player.
 * This class has a reference to the current room that the player is in.
 * As well, there is a playerHistory variable, in order to undo and redo certain moves.
 * This class implements a doCommand method which will take the input command words and correlate them to actual actions.
 *
 */


public class Player extends Humanoid {

	private PlayerHistory playerHistory;
	private Room currentRoom;
	
	public Player(int health, Room r){
		super(health);
		currentRoom = r;
	}
	
	public Player(Room r){
		super();
		currentRoom = r;
	}
	
	public void undo(){
		Command c = playerHistory.undo();
		doCommand(c);
	}
	
	public void redo(){
		Command c = playerHistory.redo();
		doCommand(c);
	}
	
	public void doCommand(Command c){
		
		if (c.getCommandWord().equals("GO")){
			Direction d = (Direction) c.getSecondWord();
			Room r = currentRoom.getExit(d);
			if(r!=null){
				currentRoom = r;
			} // else error TODO
			playerHistory.addStep(c);
			
		} else if (c.getCommandWord().equals("FIGHT")){
			Monster m = currentRoom.getMonster();
			//if(this.getBestItem().compareTo(m.getBestItem()) == 1){
				m.updateHealth(this.getBestItem().getValue());
				this.updateHealth(m.getBestItem().getValue());
				if(m.getHealth()<=0){
					currentRoom.removeMonster(m);
				}
			//}
			
		} else if (c.getCommandWord().equals("HELP")){
	        System.out.println("You are lost. You are alone. You wander around in a cave.\n");
	        System.out.println("Your command words are:");
	        System.out.println("GO, PICKUP, DROP, UNDO, REDO, FIGHT, HELP, QUIT");
	        //HELP may be implemented in another class.
	        
		} else if (c.getCommandWord().equals("PICKUP")){
			Item i = (Item) c.getSecondWord();
			addItem(i);
			playerHistory.addStep(c);
		} else if (c.getCommandWord().equals("DROP")){
			Item i = (Item) c.getSecondWord();
			removeItem(i);
			playerHistory.addStep(c);
			
		} else if (c.getCommandWord().equals("UNDO")){
			Command c1 = playerHistory.undo();
			doCommand(c1);//TODO recursive add to stack
		} else if (c.getCommandWord().equals("REDO")){
			Command c2 = playerHistory.redo();
			doCommand(c2);
			
		} //QUIT command does not get passed to the player
		
	}
	
	public Room getCurrentRoom(){
		return currentRoom;
	}
	
}
