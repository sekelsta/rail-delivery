package traingame;

import traingame.engine.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class World {
    // Map size in amount of hexagonal tiles in each dimension.
    public final int mapWidth = 102;
    public final int mapHeight = 89;

    private Random random = new Random();

    public final City[] cities;
    public final Company[] companies;
    private Terrain[][] map;

    private Point hoverHex = null;

    public World(List<Company> companies) {
        cities = readCitiesFromFile("/assets/data/map-EasternUS.txt").toArray(new City[0]);
        int availableCities = cities.length;
        assert(companies.size() <= Game.MAX_PLAYERS);
        if (Game.MAX_PLAYERS > availableCities) {
            String message = "Insufficient cities to allocate a city to each company. "
            + "Max players: " + Game.MAX_PLAYERS + " , starting cities: " + availableCities;
            throw new RuntimeException(message);
        }

        ArrayList<City> candidateStartCities = new ArrayList<>(Arrays.asList(cities));

        // Finish initializing Companies.
        for (Company c : companies) {
            int randomIndex = random.nextInt(candidateStartCities.size());
            City startingCity = candidateStartCities.get(randomIndex);
            Log.debug(startingCity.toString());
            candidateStartCities.remove(randomIndex);
            c.trainQ = startingCity.getSpawnPoint().q();
            c.trainR = startingCity.getSpawnPoint().r();
        }
        this.companies = companies.toArray(new Company[0]);


        map = new Terrain[mapWidth][mapHeight];
        //TODO: read values from a text file or make a better random map alogrithm.
        for (int x = 0; x < mapWidth; ++x) {
            map[x] = new Terrain[mapHeight];
            for (int y = 0; y < mapHeight; ++y) {
                // An actual program would initialize terrain in some more complicated way
                // This will just alternate between avaiable terrains.
                int ordinal = (x + y) % Terrain.values().length;
                map[x][y] = Terrain.values()[ordinal];
            }
        }
    }

    private List<City> readCitiesFromFile(String filePath) {
        List<City> theCitiesOnMap = new ArrayList<>();
        try (Scanner scanner = new Scanner(World.class.getResourceAsStream(filePath))) {

            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                String commentMarker = "###";
                if(!currentLine.startsWith(commentMarker)) {
                    String[] currentLineSplit = currentLine.split(";");
                    int entriesInLine = currentLineSplit.length;

                    //Prepare data to create Cities:
                    String currentCityName = "";
                    Product currentCityExport = null;
                    List<Point> localPointGroup = new ArrayList<>();

                    for (int i = 0; i < entriesInLine; i++) {
                        if (i == 0) {
                            currentCityName = currentLineSplit[i];
                        }
                        else if (i == 1) {
                            currentCityExport = Product.valueOf(currentLineSplit[i]);
                        }
                        else {
                            String[] partitionedCoordinateString = currentLineSplit[i].split(",");
                            int[] pointCoordinates = new int[partitionedCoordinateString.length];
                            for (int j=0; j<pointCoordinates.length; j++){
                                pointCoordinates[j] = Integer.parseInt(partitionedCoordinateString[j]);
                            }
                            Point somePoint = new Point(pointCoordinates[0], pointCoordinates[1]);
                            localPointGroup.add(somePoint);
                        }
                    }
                    Point[] currentPointGroup = localPointGroup.toArray(new Point[0]);

                    //Now use the data to make a city and add it to the City list.
                    City currentCity = new City(currentCityName, currentCityExport, currentPointGroup);
                    theCitiesOnMap.add(currentCity);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return theCitiesOnMap;
    }

    public Terrain getTerrainXY(int x, int y) {
        return map[x][y];
    }

    public Point getHoverLocation() {
        return hoverHex;
    }

    public void setHoverLocation(Point hover) {
        if (hover != null && (hover.x() < 0 || hover.x() >= mapWidth 
                              || hover.y() < 0 || hover.y() >= mapHeight)) {
            this.hoverHex = null;
            return;
        }
        this.hoverHex = hover;
    }
}
