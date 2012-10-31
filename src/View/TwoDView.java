package View;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import zuul.Command;
import zuul.CommandWords;
import zuul.Direction;
import zuul.Item;
import zuul.Monster;
import zuul.Player;
import zuul.Room;




public class TwoDView extends JFrame implements IView, ActionListener
{
	private JMenuItem resetGame, help, quit;
	private JMenuBar menuBar;
	private JButton undo, redo, northRoom, southRoom, eastRoom, westRoom, pickup, fight, eat, drop, inspect;
	private JLabel currentRoom;
	private JTextArea consoleField;
	private JPanel consolePanel, inventoryPanel, centralPanel, undoRedoPanel, emptyPanel;
	private Player p, reset;
	private JList inventoryList;
	private DefaultListModel inventoryModel;

	public TwoDView (Player p) {
		this.p = p;
		reset = p;
		p.addItem(new Item("Gold", false));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menuBar = new JMenuBar( );
	    setJMenuBar( menuBar );
	    setLayout(new GridLayout(3,3));
	    this.setExtendedState(this.MAXIMIZED_BOTH);

	    undo = new JButton("UNDO");
	    redo = new JButton("REDO");
	    northRoom = new JButton("North Room");
	    eastRoom = new JButton("East Room");
	    westRoom = new JButton("West Room");
	    southRoom = new JButton("South Room");
	    pickup = new JButton("Pickup");
	    fight = new JButton ("Fight");
	    eat = new JButton ("Eat");
	    drop = new JButton ("Drop");
	    inspect = new JButton ("Inspect");
	    

	    undo.addActionListener(this);
	    redo.addActionListener(this);
	    northRoom.addActionListener(this);
	    eastRoom.addActionListener(this);
	    westRoom.addActionListener(this);
	    southRoom.addActionListener(this);
	    pickup.addActionListener(this);
	    fight.addActionListener(this);
	    drop.addActionListener(this);
	    eat.addActionListener(this);
	    inspect.addActionListener(this);

	    consolePanel = new JPanel();
	    consolePanel.setLayout(new GridLayout(3, 2));
	    inventoryPanel = new JPanel();
	    inventoryPanel.setLayout(new GridLayout(1, 2));
	    centralPanel = new JPanel();
	    undoRedoPanel = new JPanel();
	    emptyPanel = new JPanel();
	    currentRoom = new JLabel("Current Room Actions:");
	    centralPanel.add(currentRoom);
	    centralPanel.add(pickup);
	    centralPanel.add(fight);
	    centralPanel.setBackground(new Color(255, 0, 0));
	    inventoryModel = new DefaultListModel();
	    inventoryList = new JList(inventoryModel);
	    
	    JScrollPane pane = new JScrollPane(inventoryList);
	    inventoryList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				Item selectedItem = (Item) inventoryList.getSelectedValue();
				if(selectedItem == null) {
					drop.setEnabled(false);
					eat.setEnabled(false);
					inspect.setEnabled(false);
				}
				else {
					drop.setEnabled(true);
					inspect.setEnabled(true);
					if(!selectedItem.isWeapon()) {
						eat.setEnabled(true);
					}
					else eat.setEnabled(false);
				}
			}
	    	
	    });
	    JPanel inventoryLeftPanel = new JPanel();
	    inventoryLeftPanel.setLayout(new GridLayout(1, 1));
	    JPanel inventoryRightPanel = new JPanel();
	    inventoryRightPanel.setLayout(new GridLayout(3, 1));
	    inventoryPanel.add(inventoryLeftPanel);
	    inventoryPanel.add(inventoryRightPanel); 
	    inventoryLeftPanel.add(pane);
	    inventoryRightPanel.add(eat);
	    inventoryRightPanel.add(drop);
	    inventoryRightPanel.add(inspect);

	    undoRedoPanel.add(undo);
	    undoRedoPanel.add(redo);
	    consoleField = new JTextArea();
	    consoleField.setEditable(false);
		consolePanel.add(consoleField);
		

	    add(undoRedoPanel);
	    add(northRoom);
	    add(emptyPanel);
	    add(westRoom);
	    add(centralPanel);
	    add(eastRoom);
	    add(consolePanel);
	    add(southRoom);
	    add(inventoryPanel);

	    JMenu addressMenu = new JMenu( "File" );
	    menuBar.add( addressMenu );


	    resetGame = new JMenuItem ( "Reset" );
	    addressMenu.add( resetGame );
	    resetGame.addActionListener(this);

	    help = new JMenuItem ( "Help" );
	    addressMenu.add( help );
	    help.addActionListener(this);

	    quit = new JMenuItem ( "Quit" );
	    addressMenu.add( quit );
	    quit.addActionListener(this);

	    update();
	}


	@Override
	public void update() {
		Room currentRoom = p.getCurrentRoom();
		if (currentRoom.getExit(Direction.NORTH) == null) {
			northRoom.setEnabled(false);
		} else {
			northRoom.setEnabled(true);
		}
		if (currentRoom.getExit(Direction.SOUTH) == null) {
			southRoom.setEnabled(false);
		} else {
			southRoom.setEnabled(true);
		}
		if (currentRoom.getExit(Direction.EAST)== null) {
			eastRoom.setEnabled(false);
		} else {
			eastRoom.setEnabled(true);
		}
		if (currentRoom.getExit(Direction.WEST) == null) {
			westRoom.setEnabled(false);
		} else {
			westRoom.setEnabled(true);
		}

		if (p.canUndo()) {
			undo.setEnabled(true);
		} else {
			undo.setEnabled(false);
		}
		if (p.canRedo()) {
			redo.setEnabled(true);
		} else {
			redo.setEnabled(false);
		}
		

		if(!p.getCurrentRoom().getItems().isEmpty()){
			pickup.setEnabled(true);
		} else {
			pickup.setEnabled(false);
		}

		inventoryModel.removeAllElements();
		for (Item i :p.getInventory())
			inventoryModel.addElement(i);
		
		if(p.getCurrentRoom().hasMonsters()) {
			fight.setEnabled(true);
		} else {
			fight.setEnabled(false);
		}
		drop.setEnabled(false);
		eat.setEnabled(false);
		inspect.setEnabled(false);
		consoleField.setText(updateConsole());
	}
	@Override
	public void displayHelp() {
//		System.out.println("You are lost. You are alone. You wander around in a cave.\n");
//		System.out.println("Your command words are:");
//		for (CommandWords commandWord : CommandWords.values()) {
//			System.out.print(commandWord + " ");
//		}
//		System.out.println("\n");
	}
	
	public String getHelp(){
		String str = "";
		str+="Welcome to the World of Zuul.\n Can you conquer the obstacles and beat the monsters?\n";
		str+="Your command words are: ";
		for (CommandWords commandWord : CommandWords.values()) {
			str+= commandWord + " ";
		}
		str+="\n";
		return str;
	}

	@Override
	public void monsterMissing() {
		// Checked by disabling the button, this will never be called

	}
	@Override
	public void garbageCommand() {
		// checked by using buttons, no way to enter garbage

	}
	@Override
	public void invalidRoom() {
		// Checked by disabling buttons

	}
	@Override
	public void gameDone() {
		JOptionPane.showMessageDialog(this, "You have been defeated!");
		quit();
	}
	@Override
	public void monsterDead(Monster m) {
		// TODO Auto-generated method stub

	}
	@Override
	public void eatingWeapon(Item i) {
		// Checked by disabling buttons

	}
	@Override
	public void noItem(Item i) {
		// Checked by disabling button

	}
	@Override
	public void itemInvalid(Item i) {
		// Checked by disabling button

	}
	@Override
	public void itemError(Item i) {
		// Checked by disabling button

	}
	@Override
	public void inCompleteCommand() {
		// Impossible with GUI

	}
	@Override
	public void undoRedoUnavailable(CommandWords commandWord) {
		// Disabling buttons when appropriate
	}
	
	public String updateConsole(){
		String s = "";
		s += ("Player Health: " + p.getHealth() + "\n");
		if(p.getCurrentRoom().getMonster()!=null){
			s+= ("Monster Health: " + p.getCurrentRoom().getMonster().getHealth());
		}
		return s;
	}
	
	@Override
	public void quit() {
		System.exit(0);
	}
	
	public void reset(){
		while(p.canUndo()){
			p.doCommand(Command.parse("Undo"));
		}
		p = reset;
		p.getPlayerHistory().clear();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Reset")) {
			reset();
		}
		else if (e.getActionCommand().equals("Help")) {
			JOptionPane.showMessageDialog(this, getHelp());
		}
		else if (e.getActionCommand().equals("North Room")) {
			p.doCommand(Command.parse("Go North"));
		}
		else if (e.getActionCommand().equals("East Room")) {
			p.doCommand(Command.parse("Go East"));
		}
		else if (e.getActionCommand().equals("West Room")) {
			p.doCommand(Command.parse("Go West"));
		}
		else if (e.getActionCommand().equals("South Room")) {
			p.doCommand(Command.parse("Go South"));
		}
		else if (e.getActionCommand().equals("Pickup")) {
			pickup.setEnabled(true);
				int popup = JOptionPane.showOptionDialog(this, "You are in the current room", "Current Room", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, p.getCurrentRoom().getItems().toArray(), null);
				if (popup != JOptionPane.CLOSED_OPTION) {
					p.doCommand(new Command(CommandWords.PICKUP, p.getCurrentRoom().getItems().get(popup)));
				}
		}
		else if (e.getActionCommand().equals("Drop")) {
			Item selectedItem = ((Item) inventoryList.getSelectedValue());
			if(selectedItem != null) {
				p.doCommand(new Command(CommandWords.DROP, selectedItem));
			}
		}
		else if (e.getActionCommand().equals("Fight")) {
			p.doCommand(Command.parse("Fight"));
			if(p.getHealth()<=0){
				this.gameDone();
			}
		}
		else if (e.getActionCommand().equals("Eat")) {
			Item selectedItem = ((Item) inventoryList.getSelectedValue());
			if(selectedItem != null) {
				p.doCommand(new Command(CommandWords.EAT, selectedItem));
			}
		}
		else if (e.getActionCommand().equals("Inspect")) {
			Item selectedItem = ((Item) inventoryList.getSelectedValue());
			if(selectedItem != null) {
				JOptionPane.showMessageDialog(this, selectedItem.getDescription());
			}
		}
		else if (e.getActionCommand().equals("UNDO")) {
			p.doCommand(Command.parse("UNDO"));
		}
		else if (e.getActionCommand().equals("REDO")) {
			p.doCommand(Command.parse("REDO"));
		}
		else if (e.getActionCommand().equals("Quit")) {
			quit();
		}
		update();


	}
}