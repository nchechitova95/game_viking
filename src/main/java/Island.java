
import java.util.*;
/**
 *The {@code Island } class contains information about:
 * the name in String format.Name is unique attribute of each islands. Two instances
 * of the class {@code Island } with the same name equals;
 * the {@code Harbor} contains methods for interacting with the class {@code Ship};
 * the map of the neighboring available {@code Island} in the format {@code HashMap},
 * where key - the class {@code Island}
 * value - the enum {Direction}
 * <pre>
 *     enum Direction{ NORTH("north), SOUTH("south"), WEST("west"), EAST("east")};
 * </pre>
 *  Ways map contains only neighboring available, without information about their neighbors.
 *  To resettle the {@code Ships} to the next {@code Island }, on оусе need to contact the class {@code IslandsManager}
 *  @author  Nina Chechitova
 *  @see     game.IslandsManager
 *  @see     game.Ship
 *  @since   JDK8
 *  */
public class Island{
    /** The name use the {@code IslandsManager} for search way*/
    private final String name;
    /** the harbor need to interaction from instances of the class {@code Ship }*/
    private final Harbor harbor;
    /** islandWay contains information about neighboring islands*/
    private final HashMap <Island, Direction> islandWay;

    public Island(String name, Map<Island, Direction> map) {
        this.name = name;
        harbor = new Harbor();
        this.islandWay = new HashMap<>(map.size());
        islandWay.putAll(map);
    }
     /** This constructor is used when there was no information about neighbors when creating an object with
      * method createIslandWay( Map<Island, Direction> map), or without if does not neighbors*/
    public Island(String name) {
        this.name = name;
        harbor = new Harbor();
        this.islandWay = new HashMap<>(4);

    }

    public String getName() {
        return name;
    }
    /** method return array instances of the class {@code Island} who are neighboring to this island*/
    public Island [] getWays(){
        return islandWay.keySet().stream().toArray(Island[]::new);
    }
    /** method return value about  instances of the class {@code Island} who are neighboring to this island*/
    public int getWaysSize(){
        return islandWay.size();
    }
    /** method initializes created Island object so that it represents the same Map<Island, Direction>
     * as the argument;  created island is a copy of the map content.
     * It is used when there was no information about neighbors when creating an object.*/
    public void createIslandWay( Map<Island, Direction> map){
        islandWay.putAll(map);
    }
    /** If the inside object needs to know about the island neighbors, it must uses this method.
     * Method returns object clone because inside object can not change wayMap.*/
    public HashMap<Island, Direction> getHasMapIslandWay() {
        return (HashMap<Island, Direction>) islandWay.clone();
    }
    /** If the inside Island object destroy, it must uses this method.
     * Method remove island from its map*/
    public void destroyWayWithMe(Island island){
        islandWay.remove(island);
    }
    /** If the Island object destroy, it must uses this method.
     * Method remove all way with it. Method call method {@code destroyWayWithMe(Island island)}
     * from all islands in map, and clear map */
    public void destroyAllIslandWay(){
        if(islandWay.isEmpty()) return;
        Set<Island> islands = islandWay.keySet();
        for (Island islandName: islands){
            Island island = IslandsManager.getInstance().islandsMap.get(islandName.getName());
            island.destroyWayWithMe(this);
        }
        islandWay.clear();
    }
    public Harbor getHarbor() {
        return harbor;
    }
    /** Method returns information (in format true, false) about Battle from island
     * Battle starting if all harbor berths are busy. */
    public boolean isBattle(){
        return (!harbor.isMoor());
    }
    /** If Battle starting on Island, need calls this method.
     * On method destroys all way withs this island and harbor.  (See below for
     *  destroyAllIslandWay() and class Harbor*/
    public void destroyLighthouse(){
        destroyAllIslandWay();
        harbor.destroy();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return name.toLowerCase().equals(island.name.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (Map.Entry islandEntry: islandWay.entrySet()){
            Direction d = (Direction)islandEntry.getValue();
            sb.append(" " +  d.getName() + "=" +islandEntry.getKey());
        }
        return sb.toString();

    }
    /**
     *The {@code Harbor } class contains information and methods for interacting with the class {@code Ship};
     *  @author  Nina Chechitova
     *  @see     game.Ship
     *  @since   JDK8
     *  */
    public class Harbor {
        /** Array contains ships. if the array is full, then a battle will begin on the island */
        private Ship[] dock;
        /** max harbor berths quantity*/
        private final int DOCK_SIZE = 2;
        /** quantity of busy harbor berths */
        private int moorsCount;

        public Harbor() {
            moorsCount = 0;
            this.dock = new Ship[DOCK_SIZE];
        }
        /** if quantity of busy harbor berths equals max quantity harbor berths,
         * ships does not moor*/
        public boolean isMoor(){
            return moorsCount<DOCK_SIZE;
        }

        /** Ships may be moor on harbor if there are free harbor berths*/
        public void moor(Ship ship){
            if (isMoor()) dock[moorsCount++] = ship;
        }
        /** The method returns an array of ships who moor on harbor*/
       public Ship[] giveShips(){
           return Arrays.copyOf(dock, moorsCount);
       }
       /** The method frees up harbor berths*/
       private void destroy(){
          moorsCount = 0;
       }
        /** The method frees up one harbor berths*/
        public void sailOff(){
            moorsCount --;
        }
        /** The method returns quantity of busy harbor berths*/
        public int getMoorsCount(){
            return moorsCount;
        }
    }
    /**The enumeration contains information about the direction of the island paths.*/
    public enum Direction{
        NORTH("North"), SOUTH("South"), WEST("West"), EAST("East");
        String name;

        Direction(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}



