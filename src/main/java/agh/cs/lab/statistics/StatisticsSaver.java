package agh.cs.lab.statistics;

import java.io.FileWriter;
import java.io.IOException;

public final class StatisticsSaver {

    private StatisticsSaver() {}

    public static void saveTotal(int epoch, SimulationStatisticsSnapshot snapshot) {
        try(FileWriter writer = new FileWriter("statistics.txt")) {
            writer.write(buildText(epoch, snapshot));
        } catch(IOException e){
            System.out.println("Could not save statistics to file - an unexpected error occured");
        }
    }

    private static String buildText(int epoch, SimulationStatisticsSnapshot snapshot) {
        var builder = new StringBuilder();
        builder.append("Epoka: ");
        builder.append(epoch);
        builder.append("\n");
        builder.append("Srednia zyjacych zwierzat: ");
        builder.append(snapshot.getTotalAverageLivingAnimals());
        builder.append("\n");
        builder.append("Srednia zyjacych roslin: ");
        builder.append(snapshot.getTotalAverageLivingPlants());
        builder.append("\n");
        builder.append("Najpopularniejszy gen: ");
        builder.append(snapshot.getTotalMostCommonGene());
        builder.append("\n");
        builder.append("Srednia energii: ");
        builder.append(snapshot.getTotalAverageEnergy());
        builder.append("\n");
        builder.append("Srednia dlugosci zycia: ");
        builder.append(snapshot.getTotalAverageLifeTime());
        builder.append("\n");
        builder.append("Srednia dzieci: ");
        builder.append(snapshot.getTotalAverageChildren());
        builder.append("\n");

        return builder.toString();
    }
}
