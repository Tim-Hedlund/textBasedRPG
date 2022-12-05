import java.io.File;
public class Quest {

    Location startLocation;
    int startBuildingIndex;
    Location endLocation;
    int endBuildingIndex;
    String questLine;
    String name;
    boolean discoverEndLocation;
    File file;

    public Quest(String name, Location startLocation, int startBuildingIndex, Location endLocation, int endBuildingIndex, String questLine, boolean discoverEndLocation, File file) {
        this.startLocation = startLocation;
        this.startBuildingIndex = startBuildingIndex;
        this.endLocation = endLocation;
        this.endBuildingIndex = endBuildingIndex;
        this.questLine = questLine;
        this.name = name;
        this.file = file;
        this.discoverEndLocation = discoverEndLocation;
    }
    public Quest(String name, Location endLocation, int endBuildingIndex, String questLine, File file){
        this.name = name;
        this.endLocation = endLocation;
        this.endBuildingIndex = endBuildingIndex;
        this.questLine = questLine;
        this.file = file;
    }

    void startQuest(){
        System.out.println("You started " + this.name);
    }

}