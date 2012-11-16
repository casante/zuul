package zuul;


/**
 * Player Class.
 *
 * This class is in charge of the Player.
 * This class has a reference to the current room that the player is in.
 * As well, there is a playerHistory variable, in order to undo and redo certain moves.
 * This class also has a reference to the view, in order to print out error messages for bad commands.
 * This class implements a doCommand method which will take the input command words and correlate them to actual actions.
 *
 * @author  Jarred Linthorne-Shaw
 * @version 2012.10.23
 */

public class Player extends Humanoid  {

	private PlayerHistory playerHistory;
	private Room currentRoom;

	/**
	 * The Constructor for the player.
	 * Creates the player history.
	 * @param health : The health of the player.
	 * @param room : The current room of the player.
	 * @param name : The name of the player.
	 */
	public Player(int health, Room room, String name){
		super(health, name);
		currentRoom = room;
		playerHistory = new PlayerHistory();
	}

	/**
	 * Default constructor for the player.
	 * Creates the player history.
	 * @param room : The current room of the player.
	 */
	public Player(Room room){
		super();
		currentRoom = room;
		playerHistory = new PlayerHistory();
	}

	/**
	 * This method takes in a command, and executes the required instruction.
	 * @param c : The command to be performed
	 */
	public void doCommand(Command c){
		boolean b = false;	//boolean variable, used to keep undo/redo off of the stack when not wanted.
		if (c.getCommandWord().equals(CommandWords.UNDO)){	//If the undo command is called on another command(drop, pickup, go)
			c = playerHistory.undo();						//Undo the command
			b = true;
		} else if (c.getCommandWord().equals(CommandWords.REDO)){	//If the undo command is called on another command(drop, pickup, go)
			c = playerHistory.redo();								//Redo the command
			b = true;
		}

		if (b == true && c == null) { 						//If there is nothing on the stack
			return;
		}


		if (c.getCommandWord().equals(CommandWords.GO)){	//If the command is go
			Direction d = (Direction) c.getSecondWord();	//set the second word to be the direction
			if (d == null) {								//If the direction is null
				return;
			}

			Room r = currentRoom.getExit(d);				//Get the exit room in the specified direction
			if(r!=null){									//if the room isn't null
				currentRoom = r;							//the new room is the room in the specified direction
			} else {
				return;
			}
			if(b == false){									//If b is false, the command is not an undo or a redo
				playerHistory.addStep(c);					//Only add the step if this is the case, to prevent stack problems
			}

		} else if (c.getCommandWord().equals(CommandWords.FIGHT)){	//If the command is fight
			Monster m = currentRoom.getMonster();					//get the monster in the room
			if(m==null){											//If there are no monsters in the room, this will be null
			} else {
				m.removeHealth(this.getBestItem().getValue());					//Remove health from the monster depending on the value of the best item in inventory
				this.removeHealth((m.getBestItem().getValue()) * m.getLevel());	//Remove health from the player depending on the value of the best item on the monster multiplied with the monster's level
				if(m.getHealth()<=0){											//Monster has died if its health is less than or equal to zero
					m.dropItems();												//Drop all of the monster's items and add them to the room
					currentRoom.removeMonster(m);								//Remove the monster from the room
				}
			}

		} else if (c.getCommandWord().equals(CommandWords.PICKUP)){ //If the command is pickup
			Item i = (Item) c.getSecondWord();						//Get the item from the second word
			if (i == null) {										//If there is no input for the item
				return;
			}
			i = currentRoom.getRealItem(i);							//Determine if the item is real or not
			if ( i == null) {										//If the item is null, it is not a real item
				return;
			}
			if(currentRoom.hasItem(i)){								//If the real item is in the current room
				addItem(i);											//Add this item to your inventory
				currentRoom.removeItem(i);							//Remove this item from the room
			}
			if(b == false){											//If b is false, the command is not an undo or a redo
				playerHistory.addStep(c);							//Only add the step if this is the case, to prevent stack problems
			}

		} else if (c.getCommandWord().equals(CommandWords.DROP)){	//If the command is drop
			Item i = (Item) c.getSecondWord();						//Get the item from the second word
			if (i == null) {										//If there is no input for the item
				return;
			}

			i = getRealItem(i);										//Determine if the item is real or not

			if ( i == null) {										//If the item is null, it is not a real item
				return;
			}
			if(getInventory().contains(i)){							//If the item is in the player's inventory
				currentRoom.addItem(i);								//Add the item to the room
				removeItem(i);										//Remove the item from the player's inventory
			}

			if(b == false){											//If b is false, the command is not an undo or a redo
				playerHistory.addStep(c);							//Only add the step if this is the case, to prevent stack problems
			}

		} else if (c.getCommandWord().equals(CommandWords.EAT)){	//If the command is eat
			Item i = (Item) c.getSecondWord();						//Get the item from the second word
			if (i == null) {										//If there is no input for the item
				return;
			}

			if (!inventory.contains(i)) {							//If the inventory does not contain the item
				return;
			}

			for(Item in: inventory){								//For each of the items in the inventory
				if(in.equals(i)){									//If the item is in the inventory
						i=in;										//Then assign the item to the actual item object in the inventory
				}
			}
			if(!i.isWeapon()){										//If the item is not a weapon
				this.addHealth(i.getValue());						//Add the value of the item to the current health
				if(this.getHealth() > MAX_HEALTH){					//If the health is greater than the maximum health
					setHealth(MAX_HEALTH);							//Set the health to the maximum health (i.e. Your health cannot exceed the maximum health)
				}
				removeItem(i);										//Remove the item from the inventory, you can't eat it twice
				playerHistory.removeItem(i);
			}
		}

		return;
	}

	/**
	 * Getter for the current room.
	 * @return : The room the player is current in.
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	
	/**
	 * Asking the player if it can go back.
	 * Its a wrapper for invoking the history of the player
	 * @return <code> true </code> if the player go back,
	 * 		<code> false</code> otherwise
	 */
	public boolean canUndo() {
		return playerHistory.canUndo();
	}
	
	/**
	 * Asking the player if it can go one step forward.
	 * Its a wrapper for invoking the history of the player
	 * @return <code> true </code> if the player go step 
	 * forward,<code> false</code> otherwise
	 */
	public boolean canRedo() {
		return playerHistory.canRedo();
	}

	/**
	 * Return the player to its default state
	 * @return : The playerHistory of the player.
	 */
	public PlayerHistory getPlayerHistory(){
		return playerHistory;
	}

	public void reset(){
		playerHistory.clear();
		setHealth(MAX_HEALTH);
	}
}