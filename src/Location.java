import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Location {
    String name;
    String type;
    int encounterChance;
    Building[] buildings;
    ArrayList<Npc> npcs = new ArrayList<>();
    ArrayList<Integer> checkedBuildings = new ArrayList<>();
    public Location(String name, String type, int encounterChance, Building[] buildings) {
        this.name = name;
        this.type = type;
        this.encounterChance = encounterChance;
        this.buildings = new Building[buildings.length];
        System.arraycopy(buildings, 0, this.buildings, 0, buildings.length);
    }

    public void generateEncounters() throws FileNotFoundException {
        final int randMax = 20; //regulates chance of generating encounter,
        System.out.println(this.name);
        for (int i = 2; i < this.buildings.length; i++) {
            if (this.encounterChance >= Math.floor(Math.random()*randMax)){ //(encounterChance/randMax % chance of generating enemy)
                this.npcs.add(new Npc(this.type, i));

            }
        }
    }
    public void showBuildings() {

        if (this.buildings.length != 0) {
            System.out.print("|| "); // prints the first row
            for (Building building : this.buildings) {
                System.out.print(building.name);
                System.out.print(" || ");
            }
            System.out.println();
            System.out.print("||=");

            int printCount;
            if(Player.playerBuildingIndex == 0) {
                printCount = this.buildings[0].name.length() - 5;
                locationPrintPiece(printCount, "=", "<You>");
            } else {
                MainClass.writeCharacter(this.buildings[0].name.length(), "=", false);
            }

            for (int i = 1; i<this.buildings.length; i++) {
                System.out.print("=||=");
                if(Player.playerBuildingIndex == i) {
                    printCount = this.buildings[i].name.length() - 5;
                    locationPrintPiece(printCount, "=", "<You>");
                } else {
                    MainClass.writeCharacter(this.buildings[i].name.length(), "=", false);
                }

            }
            System.out.println("=||");

            System.out.print("|| ");
            for (int i = 0; i<this.buildings.length; i++) {
                int NPCNum = 0;
                double aliveTest = 0;
                for (Npc npc: this.npcs) {
                    if (npc.buildingIndex == i) {
                        NPCNum += 1;
                        if (npc.alive) {
                            aliveTest += 1;
                        }
                    }
                }

                String enemyName;
                if(Player.sight >= Math.abs(Player.playerBuildingIndex-i)) {
                    if (NPCNum == 0) {
                        MainClass.writeCharacter(this.buildings[i].name.length(), " ", false);
                    } else {
                        if (NPCNum == 1) {
                            if (aliveTest == 1) {
                                enemyName = "Enemy";
                            } else {
                                enemyName = "Corpse";
                            }
                        } else {
                            if (NPCNum == aliveTest) {
                                enemyName = NPCNum + "X Enemy";
                            } else if (aliveTest == 0) {
                                enemyName = NPCNum + "X corpse";
                            } else {
                                enemyName = NPCNum + "X mixed";
                            }
                        }
                        printCount = this.buildings[i].name.length() - enemyName.length();

                        locationPrintPiece(printCount, " ", enemyName);
                    }
                } else {
                    printCount = this.buildings[i].name.length();
                    locationPrintPiece(printCount, "#", "");
                }
                System.out.print(" || ");
            }
        }
    }

    private void locationPrintPiece(int printCount, String printChar, String printString) {
        if (printCount >= 0) {
            if (printCount%2 != 0) {
                printCount -= 1;
                System.out.print(printChar);
            }
        } else {
            printCount = 0;
        }

        MainClass.writeCharacter(printCount/2, printChar, false);
        System.out.print(printString);
        MainClass.writeCharacter(printCount/2, printChar, false);
    }

    public void moveNpcs(Location currentLocation) throws FileNotFoundException {
        for(Npc npc: this.npcs) {
            if(npc.alive) {
                npc.action(currentLocation);
                System.out.println(npc.name + "takes action");
            } else {
                System.out.println(npc.name + "is 6 feet under");
            }
        }
    }
    public void takeDamage(int targetBuildingIndex, Weapon currentWeapon, double hitChance, double damageMultiplier) throws FileNotFoundException {
        for (Npc npc: this.npcs) {
            if (targetBuildingIndex == npc.buildingIndex) {
                npc.takeDamage(currentWeapon, hitChance, damageMultiplier);
                break;
            }
        }
    }
}