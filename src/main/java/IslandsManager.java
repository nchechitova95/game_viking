import java.util.*;
/**The island manager class contains fields and methods for
 * forming new islands, building a general map of islands, and finding ship routes.
 * General map is a HashMap
 * key - island name in format String
 * value - object Island.
 * Еhe class is an implementation of the singleton pattern, because one game uses one map of the islands.
 * @author Nina Chechitova
 *  @see     Island
 *  @see     Ship
 *  @since   JDK8
 * */
public final class IslandsManager {
    /** The instance contains one object IslandsManager  on all life cycle of the program*/
    public static IslandsManager instance;
    /** General map is a HashMap key - island name in format String, uses from search way and saves games world*/
    public  Map <String, Island> islandsMap;
    /** Spliterators uses to reading and format world map from strings*/
    private final String SPLITERATOR_WAY = "=";
    private final String SPLITERATOR_MAP = " ";

    /** Creates one object IslandsManager  on all life cycle of the program*/
    private IslandsManager(){
        islandsMap = new HashMap<>();
    }
    /** Returns one object IslandsManager  on all life cycle of the program*/
    public static IslandsManager getInstance() {
        if (instance == null){
            instance = new IslandsManager();
        }
        return instance;
    }
    /**For an island contained in the input parameters, returns a random neighboring island */
    public static Island getNextIsland(Island island){
        int size = island.getWaysSize();
        if (size > 0){
            int wayNum =  new Random().nextInt(size);
            Island way = island.getWays()[wayNum];
            way = IslandsManager.getInstance().islandsMap.get(way.getName());
            return way;
        }
        else {
            return null;
        }
    }

    /** Spliterators uses to reading and format world map from strings*/
    public String getSpliteratorMap() {
        return SPLITERATOR_MAP;
    }
    /**Method return island name from string.
     * The input string contain island name in format name spliterator eny information */
    public String convertDataToName(String str, String spliterator){
        String [] mapInfo = str.split(spliterator);
        return  mapInfo[0];
    }
    /**Method return ways
     *  The input string contain island name in format way spliterator way...*/
    public String [] convertDataToWays(String str, String spliterator){
        String [] mapInfo = str.split(spliterator);
        return Arrays.stream(mapInfo).skip(1).toArray(String[]::new);
    }

    /** Method builds islands*/
    public Island drawIsland(String islandName){
        return new Island(islandName);
    }
    /**Method builds ways*/
    public Map<Island, Island.Direction> drawWay(String [] ways){
        if (ways.length > 4){
            System.err.println("Way info is uncorrect");
            return null;
        }
        else {
            Map<Island, Island.Direction> islandWay = new HashMap<>(ways.length);
            for (String wayName : ways){
                String way[] = wayName.split(SPLITERATOR_WAY);
                Island.Direction direction;
                switch (way[0].toUpperCase()){
                    case "ВОСТОК": case "EAST":direction = Island.Direction.EAST;
                    break;
                    case "ЗАПАД": case "WEST": direction = Island.Direction.WEST;
                    break;
                    case "СЕВЕР": case "NORTH": direction = Island.Direction.NORTH;
                    break;
                    case "ЮГ": case "SOUTH": direction = Island.Direction.SOUTH;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + way[0].toUpperCase());
                        //break;
                }
                Island island = new Island(way[1]);
                islandWay.put(island, direction);
            }
            return islandWay;
        }
    }
    /**Method saves island and ways info in words map*/
    public void drawIslandAndWay(String islandName, String[] ways){
        Island island = drawIsland(islandName);
        Map<Island, Island.Direction> wayMap = drawWay(ways);
        island.createIslandWay(wayMap);
        islandsMap.put(islandName, island);

    }
    /**Method save world map to string to print it more*/
    public String [] mapToStrings(){
        String [] map = new String[islandsMap.size()];
        int i = 0;
        for (Map.Entry<String, Island> entry: islandsMap.entrySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getValue().toString());
            map[i] = sb.toString();
            //System.out.println(map[i]);
            i++;
        }
       return map;
    }

}
