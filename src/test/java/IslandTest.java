import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
    String islandName = "Island1";

    public Island initIsland(){
        return new Island(islandName);
    }
    @Test
    void testGetName() {
        assertEquals(initIsland().getName(), islandName);
    }
    @Test
    public void testEquals() {
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров1");
        assertNotEquals(i1, null);
        assertEquals(i1, i1);
        assertNotEquals(i1, i2);
        assertNotEquals(i2, i3);
        assertEquals(i1, i3);

    }
    @Test
    public void testHashCode() {
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров1");

        assertTrue(i1.hashCode() == i1.hashCode());
        assertTrue(i1.hashCode() == i3.hashCode());
        assertFalse(i1.hashCode() == i2.hashCode());
    }
    @Test
    void testCreateIslandWay() {
        Map<Island, Island.Direction> map = new HashMap<>(4);
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров3");

        map.put(i2, Island.Direction.EAST);
        map.put(i3, Island.Direction.NORTH);

        i1.createIslandWay(map);
        Map <Island, Island.Direction> mapOutput = i1.getHasMapIslandWay();
        assertEquals(map, mapOutput);
    }
    @Test
    void testGetWays() {

        Map<String, Island> map = IslandsManager.getInstance().islandsMap;
        Island i1 = initIsland();
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров3");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland3 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);
        mapIsland1.put(i3, Island.Direction.WEST);
        mapIsland3.put(i1, Island.Direction.EAST);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);
        i3.createIslandWay(mapIsland3);

        map.put(i1.getName(), i1);
        map.put(i2.getName(), i2);
        map.put(i3.getName(), i3);

        Island [] i1Way = {i2, i3};
        assertNotNull(i1.getWays());
        assertArrayEquals(i1.getWays(), i1Way);
    }

    @Test
    void testGetWaysSize() {
        Island i1 = initIsland();
        Island i2 = new Island("Остров2");

        assertTrue(i1.getWaysSize() == 0);

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);

        assertNotNull(i1.getWaysSize());
        assertFalse(i1.getWaysSize() == 0);
        assertTrue(i1.getWaysSize() == 1);
    }

    @Test
    void testGetHashMapIslandWay() {
        Map <Island, Island.Direction> map = new HashMap<>(4);
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров3");

        map.put(i2, Island.Direction.EAST);
        map.put(i3, Island.Direction.NORTH);

        Island i1 = new Island("Остров1", map);
        Map <Island, Island.Direction> mapOutput = i1.getHasMapIslandWay();
        assertEquals(map, mapOutput);
    }

    @Test
    void testDestroyWayWithMe() {
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);

        assertNotNull(mapIsland1.get(i2));

        i1.destroyWayWithMe(i2);
        mapIsland1 = i1.getHasMapIslandWay();

        assertNull(mapIsland1.get(i2));
    }

    @Test
    void testDestroyAllIslandWay() {
        Map<String, Island> map = IslandsManager.getInstance().islandsMap;
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров3");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland3 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);
        mapIsland1.put(i3, Island.Direction.WEST);
        mapIsland3.put(i1, Island.Direction.EAST);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);
        i3.createIslandWay(mapIsland3);

        map.put(i1.getName(), i1);
        map.put(i2.getName(), i2);
        map.put(i3.getName(), i3);
        assertNotNull(map.get(i1.getName()).getHasMapIslandWay());

        mapIsland2 = map.get(i2.getName()).getHasMapIslandWay();
        assertNotNull(mapIsland2.get(i1));
        mapIsland3 = map.get(i3.getName()).getHasMapIslandWay();
        assertNotNull(mapIsland3.get(i1));

        i1.destroyAllIslandWay();
        assertTrue(map.get(i1.getName()).getHasMapIslandWay().isEmpty());
        assertNull(map.get(i2.getName()).getHasMapIslandWay().get(i1));
        assertNull(map.get(i3.getName()).getHasMapIslandWay().get(i1));
    }

    @Test
    void isBattle() {
        Island i1 = new Island("Island1");
        Island.Harbor harbor = i1.getHarbor();
        harbor.moor(new Ship("Ship1"));
        assertFalse(i1.isBattle());

        harbor.moor(new Ship("Ship2"));
        assertTrue(i1.isBattle());
    }

    @Test
    void testDestroyLighthouse() {
        Map<String, Island> map = IslandsManager.getInstance().islandsMap;
        Island i1 = new Island("Остров1");
        Island i2 = new Island("Остров2");
        Island i3 = new Island("Остров3");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland3 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);
        mapIsland1.put(i3, Island.Direction.WEST);
        mapIsland3.put(i1, Island.Direction.EAST);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);
        i3.createIslandWay(mapIsland3);

        map.put(i1.getName(), i1);
        map.put(i2.getName(), i2);
        map.put(i3.getName(), i3);
        assertNotNull(map.get(i1.getName()).getHasMapIslandWay());

        mapIsland2 = map.get(i2.getName()).getHasMapIslandWay();
        assertNotNull(mapIsland2.get(i1));
        mapIsland3 = map.get(i3.getName()).getHasMapIslandWay();
        assertNotNull(mapIsland3.get(i1));

        i1.destroyLighthouse();

        assertTrue(i1.getHarbor().getMoorsCount() == 0);

        assertTrue(map.get(i1.getName()).getHasMapIslandWay().isEmpty());
        assertNull(map.get(i2.getName()).getHasMapIslandWay().get(i1));
        assertNull(map.get(i3.getName()).getHasMapIslandWay().get(i1));
    }

    @Test
    void testToString() {
        Island i = initIsland();
        assertEquals(i.toString(), islandName);

        i = new Island("Island1");
        Island i2 = new Island("Island2");
        Island i3 = new Island("Island3");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);


        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland1.put(i3, Island.Direction.WEST);

        i.createIslandWay(mapIsland1);
        assertTrue(i.toString().equals("Island1 South=Island2 West=Island3")|| i.toString().equals("Island1 West=Island3 South=Island2") );
    }
    Island i;
    @Test
    public void isMoor(){
        Island.Harbor harbor = initIsland().getHarbor();
        int i = harbor.getMoorsCount();
        if(i < 2){
            assertTrue(harbor.isMoor());
        }else {
            assertFalse(harbor.isMoor());
        }
    }
    @Test
    public void moor(){
        Island.Harbor harbor = initIsland().getHarbor();
        Ship s = new Ship("Ship");
        harbor.moor(s);
        assertTrue(harbor.getMoorsCount() == 1);
        assertEquals(harbor.giveShips()[0], s);
    }
    @Test
    public void giveShips(){
        Island.Harbor harbor = new Island("Остров1").getHarbor();
        Ship s = new Ship("Ship");
        harbor.moor(s);
        assertEquals(harbor.giveShips()[0], s);
    }
    @Test
    public void sailOff() {
        Island.Harbor harbor = new Island("Остров1").getHarbor();
        Ship s = new Ship("Ship");
        harbor.moor(s);
        assertTrue(harbor.getMoorsCount() == 1);
        harbor.sailOff();
        assertTrue(harbor.getMoorsCount() == 0);
    }
}