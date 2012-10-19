

public class View {
	private Player player;
	public View (Player p) {
		player=p;
	}
	
	public void update() {
		System.out.println(player + " has a health of " + player.getHealth());//TODO
		System.out.println(player.getInventoryString());
		System.out.println(player.getCurrentRoom().getLongDescription());
		System.out.print("> ");
		
		
	}
	
	public void displayHelp() {
		System.out.println("You are lost. You are alone. You wander around in a cave.\n");
        System.out.println("Your command words are:");
        System.out.println("GO, PICKUP, DROP, UNDO, REDO, FIGHT, HELP, QUIT\n");
        
	}
	
	public void monsterMissing() {
		System.out.println("Nothing to Fight!");
	}


}
