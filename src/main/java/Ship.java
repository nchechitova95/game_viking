import java.util.*;
/**
 *The {@code Ship } class contains information about:
 * the {@code Viking} who sails on this ship
 *  All ships who sails and vikings in the world, not die.
 *  All information contains in the TreeMap:
 *  key - object Ship
 *  value - object Island where object ship moors.
 *  TreeMap is using because in game is important players moves
 *  @author  Nina Chechitova
 *  @since   JDK8
 *  */
public class Ship implements Comparable{
    public static TreeMap<Ship, Island> ships = new TreeMap<>();
    /**Viking sails on the ship*/
    private Viking viking;

    /**Create ship and viking*/
    public Ship(String vikingName) {
        this.viking = new Viking(vikingName);
    }
    /**Returns vikings name*/
    public String getVikingName(){
        return viking.name;
    }

    /**returns viking who sails on ths island*/
    public Viking getViking() {
        return viking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return Objects.equals(viking, ship.viking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viking);
    }

    /** Ship destroy where viking die*/
    public void die(){
       viking.die();
    }

    /** Ship not destroy where viking live*/
    public boolean isLive() {
        return viking.isLive;
    }

    @Override
    public int compareTo(Object o) {
        Ship s = (Ship) o;
        return this.viking.compareTo(s.viking);
    }

    /**
     *The {@code Viking } class Viking class contains information in format
     *  name, dayLife, live he or die.
     *  In this world viking can can only exist on ship, so class {@code Viking } is nested class {@code Ship}
     *  @author  Nina Chechitova
     *  @since   JDK8
     *  */
    public static class Viking implements Comparable{
        /** Viking name unique attribute of each viking. Two instances of the class {@code Island }
         * with the same name equals*/
        private final String name;
        /**All viking name starting for this substring*/
        private static final String vikingsNamePrefix = "Viking";
        /**Contain information about number of moves*/
        private int dayLife;
        /**Contain information about live or die viking*/
        private boolean isLive;
        /**Contain information about max number of moves*/
        private static final int lastDay = 10_000;

        public static String getVikingsNamePrefix() {
            return vikingsNamePrefix;
        }

        public Viking(String name) {
            this.name = name;
            dayLife = 0;
            isLive = true;
        }
        /**If number of moves equally max number of moves, viking will be dies*/
        public boolean isLastDay(){
            return dayLife >= lastDay;
        }
        /**change state vikings is live from die*/
        public void die(){
            isLive = false;
        }
        /**If the viking made a move, then the move counter increases by one until it reaches the maximum value*/
        public void liveDay(){
            dayLife++;
            if(isLastDay()){
                die();
                //System.out.println(name + " die before " + dayLife + " day");
            }
        }
        @Override
        public String toString() {
            return  name;
        }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;
             Viking viking = (Viking) o;
             return name.equals(viking.name);
         }

         @Override
         public int hashCode() {
             return Objects.hash(name);
         }

        @Override
        public int compareTo(Object o) {
            Viking v = (Viking) o;
            return this.name.compareTo(v.name);
        }
    }
}
