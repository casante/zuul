package Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import zuul.Item;
import zuul.Monster;
import zuul.Room;

public class MonsterTest {

  Room r;
  Monster m;

  @Before
  public void setUp() throws Exception {
    r = new Room("Current");
    m = new Monster(10, 10, "Bill", r);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDropItems() {
    Item i = new Item("Claws", true);
    m.addItem(i);
    m.dropItems();
    assertTrue(r.getItems().contains(i));
  }

  @Test
  public void testEqualsObject() {
    Monster m2 = new Monster(10, 10, "Bill", r);
    assertTrue(m.equals(m2));
    Monster m3 = new Monster(20, 5, "Frank", r);
    assertFalse(m.equals(m3));
  }
}
