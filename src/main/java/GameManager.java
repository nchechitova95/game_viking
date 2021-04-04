import java.io.*;
import java.util.*;
/**The island manager class contains fields and methods for
 * starting, playing and finishing game.
 * To start the game, implemented methods for reading a map from a file
 * and a method for reading information about the number of Vikings from the keyboard.
 * At the end of the game, information about the rest of the world is saved in the output file.
 * @author Nina Chechitova
 *  @see     Island
 *  @see     IslandsManager
 *  @see     Ship
 *  @since   JDK8
 * */
public class GameManager {
    private IslandsManager islandsManager;
    private  String inFileName;
    private  String outFileName;

    /**To start the game, implemented methods for reading a map from a file
     * and a method for reading information about the number of Vikings from the keyboard.*/
    public boolean startingGame(){
         islandsManager = IslandsManager.getInstance();
         System.out.println("Input input filename:");
         Scanner in = new Scanner(System.in);
         inFileName = in.next();
         System.out.println("Input output filename:");
         outFileName = in.next();
         //inFileName = "gameInput1.txt";
         //outFileName = "gameOutput.txt";
         if(!readMapFromFile(inFileName)){
             return false;
         }
         int count = readVikingsData();
         if(count<1) {
             System.err.println("number of viking must be bigest 0 and smaller " +  islandsManager.islandsMap.size());
             return false;
         }
         createVikings(count);
         return true;
    }
    /**A method that plays out one iteration of the game.
     It includes picking all Vikings in turn, calling a war among two Vikings on the same island,
     and checking if a Viking is stuck on an island.*/
    private void onePlayIterator(){
        Iterator<Ship> shipIterator = Ship.ships.keySet().iterator();
        while (shipIterator.hasNext()){
            Ship ship = shipIterator.next();
            if (ship.isLive()) {
                Island island = Ship.ships.get(ship);
                Island newIsland = IslandsManager.getNextIsland(island);
                if (newIsland != null) {
                    island.getHarbor().sailOff();
                    resettle(ship, newIsland);
                    ship.getViking().liveDay();
                    if (newIsland.isBattle()) {
                        shipIterator.remove();
                        kill(newIsland);
                    }
                } else {
                    stuck(island, ship);
                    shipIterator.remove();
                }
            }
            else shipIterator.remove();
        }
    }
    /**As long as there are live vikings, launches the next iteration of the game*/
    public void playGame(){
        int i = 0;
            while(!Ship.ships.isEmpty()){
                onePlayIterator();
            }
    }
    /** Reads data from a file and calls islandManager methods to build world map and islands*/
    private boolean readMapFromFile(String fileName){
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))){
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                String name = islandsManager.convertDataToName(tmp, islandsManager.getSpliteratorMap());
                String [] ways = islandsManager.convertDataToWays(tmp, islandsManager.getSpliteratorMap());
                islandsManager.drawIslandAndWay(name, ways);
            }
        } catch (IOException e) {
            System.err.println("Ошибка открытия файла: " + fileName + " " + e.getMessage());
            return false;
        }
        return true;
    }

    /**At the end of the game, information about the rest of the world is saved in the output file.*/
    private void writeMapFromFile(String fileName){

        //Arrays.stream(islandsMap.mapToStrings()).forEach(System.out::println);
        try(BufferedWriter writer =
                    new BufferedWriter(new BufferedWriter(new FileWriter(fileName)))){
                    Arrays.stream(islandsManager.mapToStrings()).forEach(s-> {
                        try {
                            writer.write(s);
                            writer.append(System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readVikingsData(){
        System.out.println("Inpun number of viking from 1 to " + islandsManager.islandsMap.size());
        Scanner in = new Scanner(System.in);
        int vikingQuantity = in.nextInt();
        if (vikingQuantity> islandsManager.islandsMap.size()){
            return -1;
        }
        return vikingQuantity;
    }
    /** Reading information about the number of Vikings from the keyboard.*/
    private void createVikings(int quantity){
        Iterator<Island> iterator = islandsManager.islandsMap.values().iterator();
        for(int i = 1; i<quantity + 1; i++){
            Ship ship = new Ship(
                    Ship.Viking.getVikingsNamePrefix()+i);
            if(iterator.hasNext()){
                Island island = iterator.next();
                resettle(ship, island);
                Ship.ships.put(ship, island);
                //System.out.println(Ship.ships.get(ship) + ship.getVikingName());
                ship.getViking().liveDay();
            }
        }
    }
    /**The method of relocating a Viking from one island to another*/
    private void resettle(Ship ship, Island island){
       island.getHarbor().moor(ship);
       Ship.ships.replace(ship, island);
    }

    /**The method destroys the lighthouse on the island, kills the Vikings and destroys their ships
     * and generates a message for display to the user*/
    private void kill(Island island){
        Ship[] ships = island.getHarbor().giveShips();
        island.destroyLighthouse();
        StringBuilder sb = new StringBuilder();
        sb.append("AGR!!! On " + island.getName() +" destroy lighthouse, thanks to ");
        for (Ship ship: ships){
            sb.append(ship.getVikingName() + " and ");
            ship.die();
        }
        int i = sb.lastIndexOf("a");
        sb.deleteCharAt(i);
        i = sb.lastIndexOf("n");
        sb.deleteCharAt(i);
        i = sb.lastIndexOf("d");
        sb.deleteCharAt(i);
        sayInfo(sb);
    }

    /**Method that prints a message to the console*/
    public void sayInfo(StringBuilder sb){
        System.out.println(sb);
    }
    /**Еру the method generates a message about stuck viking for display to the user*/
    private void stuck(Island island, Ship ship){
        StringBuilder sb = new StringBuilder();
        sb.append("AGR!!! ");
        sb.append(ship.getVikingName());
        sb.append(" stuck on  ");
        sb.append(island.getName());
        sb.append(" and no longer involved in the war");

        sayInfo(sb);
    }

    /**The method information about the rest of the world is saved in the output file*/
    public void finishGame(){
        writeMapFromFile(outFileName);
    }

    public static void main(String[] args) {
        GameManager g = new GameManager();
        if(g.startingGame()){
            g.playGame();
            g.finishGame();
        }
    }


}
