import java.io.FileNotFoundException;

public class Building {

    String name;
    String lootType;
    int lootTier;
    int lootAmount;
    public Building(String name, String lootType, int lootTier, int lootAmount) {
        this.name = name;
        this.lootType = lootType;
        this.lootTier = lootTier;
        this.lootAmount = lootAmount;
    }

    public void questCheck(Location currentLocation, int currentBuildingIndex) {
        for (Quest quest: MainClass.inactiveQuests) {
            if((quest.startLocation == currentLocation) && (quest.startBuildingIndex == currentBuildingIndex)){
                MainClass.inactiveQuests.remove(quest);
                MainClass.activeQuests.add(quest);
                quest.startQuest();
            }
        }
    }
}
