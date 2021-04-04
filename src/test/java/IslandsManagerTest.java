import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class IslandsManagerTest {
    private static IslandsManager islandsManager = IslandsManager.getInstance();
    @Test
    void convertDataToName() {
        String str = "Island5 North=island6";
        String name = islandsManager.convertDataToName(str, islandsManager.getSpliteratorMap());
        //assertNotNull(name);
       assertEquals("Island5", name);
    }

    @Test
    void convertDataToWays() {
        String str = "Island5 North=Island6";
        String [] ways = islandsManager.convertDataToWays(str, islandsManager.getSpliteratorMap());
        String [] waysOutput = {"North=Island6"};
        assertArrayEquals(ways, waysOutput);
        ways=null;
        waysOutput = null;
    }

    @Test
    void drawIsland() {
        Island island1 = islandsManager.drawIsland("Island5");
        Island island2 = new Island("Island5");
        Assertions.assertEquals(island2, island1);
    }

    @Test
    void drawWay() {
        String [] waysOutput = {"North=Island6"};
        Map<Island, Island.Direction> islandWay1 = islandsManager.drawWay(waysOutput);
        Map <Island, Island.Direction> islandWay2 = new HashMap<>(2);
        islandWay2.put(new Island("Island6"), Island.Direction.NORTH);
        assertNotNull(islandWay1);
        assertEquals(islandWay1, islandWay2);
    }
    @Test
    void getInstance() {
        IslandsManager manager = IslandsManager.getInstance();
        assertNotNull(manager);
        assertEquals(manager, IslandsManager.getInstance());
    }
    @Test
    void getNextIsland() {
        Map<String, Island> map = IslandsManager.getInstance().islandsMap = new HashMap<>();
        Island i1 = new Island("Island6");
        Island i2 = new Island("Island5");

        Map <Island, Island.Direction> mapIsland1 = new HashMap<>(4);
        Map <Island, Island.Direction> mapIsland2 = new HashMap<>(4);

        mapIsland1.put(i2, Island.Direction.SOUTH);
        mapIsland2.put(i1, Island.Direction.NORTH);

        i1.createIslandWay(mapIsland1);
        i2.createIslandWay(mapIsland2);

        assertNull(map.get(i1.getName()));
        map.put(i1.getName(), i1);
        map.put(i2.getName(), i2);

        Island nextIsland1 = IslandsManager.getNextIsland(i1);
        Island nextIsland2 = IslandsManager.getNextIsland(i2);

        assertEquals(nextIsland1, map.get(i2.getName()));
        assertEquals(nextIsland2, map.get(i1.getName()));
    }
    @Test
    void
    drawIslandAndWay() {
        islandsManager.islandsMap = new HashMap<>();
        HashMap<String, Island> map = new HashMap<>();
        Map <Island, Island.Direction> islandWay = new HashMap<>(2);
        islandWay.put(new Island("Island6"), Island.Direction.NORTH);
        Island island = new Island("Island5", islandWay);
        map.put("Island5", island);
        String [] waysOutput = {"North=Island6"};
        islandsManager.drawIslandAndWay("Island5", waysOutput);

        assertNotNull(islandsManager.islandsMap);
        Assertions.assertEquals(map, islandsManager.islandsMap);
    }
}