package traingame.test;

import java.util.*;
import traingame.*;

public class Main {
    public static void main(String[] args) {
        List<Company> companyList = new ArrayList<>();
        companyList.add(Company.makeRed());
        companyList.add(Company.makeBlue());
        companyList.add(Company.makeYellow());
        companyList.add(Company.makeGreen());
        System.out.println("Generating world with " + companyList.size() + " companies.");

        World world = new World(companyList);
        printInfo(world);
    }

    private static void printInfo(World world) {
        System.out.println();
        System.out.println("Condensed city info:");
        for (City c : world.cities) {
            System.out.println(c.toString());
        }

        System.out.println();
        System.out.println("Companies (with updated trainQ and trainR)");
        for (Company c : world.companies) {
            System.out.println(c.toString());
            System.out.println();
        }

        System.out.println();
        System.out.println("Test Cargo Order Generation:");
        for (int i=0; i<15; i++) {
            System.out.println(CargoOrder.getRandom(world.cities).toString());
        }
    }
}
