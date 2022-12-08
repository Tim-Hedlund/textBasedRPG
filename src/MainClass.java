import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
public class MainClass {
    public static Scanner keyboard = new Scanner(System.in);
    public static ArrayList<Quest> inactiveQuests = new ArrayList<>();
    public static ArrayList<Quest> activeQuests = new ArrayList<>();
    public static ArrayList<Weapon> weapons = new ArrayList<>();
    public static ArrayList<Armor> armor = new ArrayList<>();
    static File printFile = new File("src\\MainClass.txt");
    static File artFile = new File("src\\Art.txt");

    static void printFromTxt(File file, int paragraph, boolean ln) throws FileNotFoundException {
        Scanner scanFile = new Scanner(file);

        for(int i = 0; i < paragraph;) {
            String currentLine = scanFile.nextLine();
            if (currentLine.trim().equals("")){
                i++;
            }
        }
        while (scanFile.hasNextLine()){
            String currentLine = scanFile.nextLine();
            if (currentLine.trim().equals("")){
                break;
            } else System.out.println(currentLine);
        }
        if (ln) System.out.println();
    }

    static void blank(int repetitions){

        for (int i = 0; i < repetitions; i++) {
            System.out.println();

        }
        //metod för att skapa (repetitions) tomma linjer för att skapa känslan av att byta sida
        //eller gå till en meny.
    }



    static void showLocationEncounterChance(Location location){
        if (location.encounterChance == 0){
            System.out.println("You won't run into somebody here");
        } else if (location.encounterChance <= 3){
            System.out.println("You have a very low chance of running into somebody here");
        } else if (location.encounterChance <= 6){
            System.out.println("You have a low chance of running into somebody here");
        } else if (location.encounterChance <= 9){
            System.out.println("You have a decent chance of running into somebody here");
        } else if (location.encounterChance <= 12) {
            System.out.println("You have a high chance of running into somebody here");
        } else if (location.encounterChance <= 15) {
            System.out.println("You have a very high chance of running into somebody here");
        } else if (location.encounterChance <= 20) {
            System.out.println("You have an extremely high chance of running into somebody here");
        } else {
            System.out.println("something strange happened");
        }
    }

    static void help() throws FileNotFoundException {
        printFromTxt(printFile, 1, false);
    }

    public static void writeCharacter(int count, String letter, boolean newLine){
        for (int i = 0; i < count; i++) {
            System.out.print(letter);
        }
        if (newLine) {
            System.out.println();
        }
    }

    public static void drawHitLocations(ArrayList<Integer> hitBodyParts) throws FileNotFoundException {

        int bodyParts = 3;
        int[] numberOfTargets = new int[bodyParts]; //number of '#' per body-part in Art.txt
        int[] numberOfHits = new int[bodyParts];    //number of shots hit per body-part

        Scanner scanFile = new Scanner(artFile); //gets the amount of '#' in each body part
        for(int i = 0; i < bodyParts; i++) {
            while (scanFile.hasNextLine()){
                String currentLine = scanFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                } else {
                    for(int j = 0; j < currentLine.length();  j++) {
                        if (currentLine.charAt(j) == '#') {
                            numberOfTargets[i] += 1;
                        }
                    }
                }
            }
        } //this code works as intended :)

        ArrayList<Integer> hitLocations = new ArrayList<>(); //what '#' should be turned into hit-marker in order body-part 1 first and so on

        for (int i = 0; i < bodyParts; i++) { //changes what body part is being checked
            numberOfHits[i] = Collections.frequency(hitBodyParts, i); //calculates number of hits in current body part

            for (int j = 0; j < numberOfHits[i]; j++) { //creates hit markers for current body part depending on number of hits
                hitLocations.add((int)Math.floor(Math.random()*numberOfTargets[i]));

                //for(int k = 0; k < numberOfTargets[i]; k++) { hitLocations.add(k); }
            }
            printBodyPart(i, hitLocations); //print out current body-part with the hit markers
            hitLocations.clear();

        }

    } //90% sure all this code works as intended

    private static void printBodyPart(int currentBodyPart, ArrayList<Integer> hitLocations) throws FileNotFoundException { //this code is super broken and needs rewriting. Almost never works...
        File fullBody = artFile;
        Scanner scanFile = new Scanner(fullBody);
        Collections.sort(hitLocations);
        hitLocations = removeDuplicates(hitLocations);

        for(int i = 0; i < currentBodyPart;) { //only print the current body part
            String currentLine = scanFile.nextLine();
            if (currentLine.trim().equals("")){
                i++;
            }
        }
        int currentHitMarker = 0;
        while (scanFile.hasNextLine()){
            String currentLine = scanFile.nextLine();
            String currentLineTrim = currentLine.trim();
            int charsToFirstHit = 0;

            if (currentLineTrim.contains("#")) {
                while(currentLine.charAt(charsToFirstHit) != '#') {
                    charsToFirstHit ++;
                }

                for(int i = charsToFirstHit; i<currentLine.length(); i++) {
                    if ((currentLine.charAt(i) == '#') && (hitLocations.size() != 0)) {
                        if (currentHitMarker == hitLocations.get(0)) {
                            currentLine = currentLine.substring(0,i) + 'X' + currentLine.substring(i+1); //works
                            hitLocations.remove(0);
                        }
                        currentHitMarker++;
                    }
                }

                currentLine = currentLine.replace('#', ' ');
            } else {
                if (currentLine.equals("")) {
                    break;
                }
            }
            System.out.println(currentLine);
        }
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> arrayList) {
        ArrayList<T> returnList = new ArrayList<>();

        for (T element: arrayList) {
            if (!returnList.contains(element)) {
                returnList.add(element);
            }
        }
        return returnList;
    }

    static void printMap(Location[][] map, boolean[][] shownLocationsMap) {

        int spacing = 8; //minimum 8
        int mapWidth = 0;
        int counter = 0;
        for (Location[] locations : map) {
            if (counter > mapWidth) {
                mapWidth = counter;
            }
            counter = 0;
            for (Location location : locations) {
                counter = counter + location.name.length() + spacing;
            }
        }
        writeCharacter(mapWidth+24, "=", true);
        System.out.print("||");
        writeCharacter((mapWidth+24)/2-11, " ", false);
        System.out.print("--<=={[ MAP ]}==>--");
        writeCharacter((mapWidth+24)/2-12, " ", false);
        System.out.print(" ||");

        for (int i = 0; i<map.length; i++) {
            System.out.println();
            writeCharacter(mapWidth+24, "=", true);
            System.out.print("|| ");
            for (int j = 0; j<map[i].length; j++) {
                if (shownLocationsMap[i][j]) {
                    System.out.print(map[i][j].name);
                    writeCharacter(mapWidth/7 - map[i][j].name.length(), " ", false);
                } else {
                    writeCharacter(mapWidth/7, "#", false);
                }
                if(i == Player.playerY) {
                    if (j + 1 == Player.playerX) {
                        System.out.print(" ||<");
                    } else if (j == Player.playerX) {
                        System.out.print(">|| ");
                    } else {
                        System.out.print(" || ");
                    }
                } else {
                    System.out.print(" || ");
                }
            }
        }
        System.out.println();
        writeCharacter(mapWidth+24, "=", true);
    }

    static void createItems() throws FileNotFoundException {
        Scanner fileReader = new Scanner(new File("src\\Weapon.txt")); //Weapon fileReader
        fileReader.nextLine();
        fileReader.nextLine(); //skip first two lines

        while (fileReader.hasNextLine()) { //loop through all lines
            weapons.add(new Weapon(fileReader.nextLine()));
        }

        fileReader = new Scanner(new File("src\\Armor.txt")); //Armor fileReader
        fileReader.nextLine();
        fileReader.nextLine(); //skip first two lines

        while (fileReader.hasNextLine()) { //loop through all lines
            armor.add(new Armor(fileReader.nextLine()));
        }

    }

    static Weapon getRandomWeapon(int difficulty) {
        ArrayList<Weapon> weaponCandidates = new ArrayList<>();

        if (difficulty <= 1) {
            for(Weapon weapon: weapons) { //for difficulties 1 and under, only bread knifes
                if (weapon.name.equals("bread knife")) {
                    return weapon;
                }
            }
        } else if (difficulty <= 3) { //for difficulties 3 and under, only knifes
            for(Weapon weapon: weapons) {
                if ((weapon.range == 0) && (weapon.lootTier <= 1) ){
                    weaponCandidates.add(weapon);
                }
            }

        } else if (difficulty <= 6) {
            for(Weapon weapon: weapons) {
                if ((weapon.lootTier <= 1) && !(weapon.lootType.contains("military"))) {
                    weaponCandidates.add(weapon);
                }
            }
        } else if (difficulty <= 10) {
            for (Weapon weapon : weapons) {
                if ((weapon.lootTier <= 1)) {
                    weaponCandidates.add(weapon);
                }
            }
        } else if (difficulty <= 16) {
            for (Weapon weapon : weapons) {
                if (weapon.lootTier == 2) {
                    weaponCandidates.add(weapon);
                }
            }
        } else if (difficulty <= 20) {
            for (Weapon weapon : weapons) {
                if (weapon.lootTier == 3) {
                    weaponCandidates.add(weapon);
                }
            }
        } else {
            for (Weapon weapon : weapons) {
                if (weapon.lootTier >= 3) {
                    weaponCandidates.add(weapon);
                }
            }
        }

        return weaponCandidates.get((int)Math.floor(Math.random()*weaponCandidates.size()));
    }

    public static Armor[] getRandomArmor(int difficulty) {

        ArrayList<Armor> armorCandidates = new ArrayList<>();

        if (difficulty <= 4) {
            for(Armor armor: armor) {
                if ((armor.lootType.contains("civil") && (armor.armorSlot == 1)) ){
                    armorCandidates.add(armor);
                }
            }
        } else if (difficulty <= 7) {
            for(Armor armor: armor) {
                if (armor.lootType.contains("civil")) {
                    armorCandidates.add(armor);
                }
            }
        } else if (difficulty <= 10) {
            for (Armor armor : armor) {
                if ((armor.lootType.contains("civil")) || (armor.lootTier <= 1)) {
                    armorCandidates.add(armor);
                }
            }
        } else if (difficulty <= 16) {
            for (Armor armor : armor) {
                if (armor.lootTier == 2) {
                    armorCandidates.add(armor);
                }
            }
        } else {
            for (Armor armor : armor) {
                if (armor.lootTier == 3) {
                    armorCandidates.add(armor);
                }
            }
        }

        int armorTypesCount = 3;

        ArrayList<Armor> slot0Candidates = new ArrayList<>();
        ArrayList<Armor> slot1Candidates = new ArrayList<>();

        for(Armor armor : armorCandidates) { //hardcoded for 2 pieces of armor
            if (armor.armorSlot == 0) {
                slot0Candidates.add(armor);
            } else if (armor.armorSlot == 1) {
                slot1Candidates.add(armor);
            }
        }

        Armor[] returns = new Armor[armorTypesCount];

        if (slot0Candidates.size() != 0) {
            returns[0] = slot0Candidates.get((int)Math.floor(Math.random()*slot0Candidates.size()));
        } else {
            returns[0] = null;
        }
        if (slot1Candidates.size() != 0) {
            returns[1] = slot1Candidates.get((int)Math.floor(Math.random()*slot1Candidates.size()));
        } else {
            returns[1] = null;
        }
        returns[2] = null;

        return returns;
    }

    static void printQuests (){
        System.out.println("Quests");
        System.out.println();
        System.out.println("Inactive Quests");
        for(Quest inactiveQuest : inactiveQuests){
            System.out.println(inactiveQuest.name);
        }
        System.out.println();
        System.out.println("Active Quests");
        for(Quest activeQuest : activeQuests){
            System.out.println(activeQuest.name);
        }

    }

    private static void exploreMap(int exploreRadius, boolean[][] shownLocationsMap) { //only works for exploreRadius of 0 or 1
        if (exploreRadius >= 0) {
            shownLocationsMap[Player.playerY][Player.playerX] = true;
            if (exploreRadius >= 1) {
                if(Player.playerX+1 < shownLocationsMap[Player.playerY].length) {
                    shownLocationsMap[Player.playerY][Player.playerX+1] = true;
                }
                if(Player.playerX-1 >= 0) {
                    shownLocationsMap[Player.playerY][Player.playerX-1] = true;
                }
                if(Player.playerY+1 < shownLocationsMap.length) {
                    shownLocationsMap[Player.playerY+1][Player.playerX] = true;
                }
                if(Player.playerY-1 >= 0) {
                    shownLocationsMap[Player.playerY-1][Player.playerX] = true;
                }
            }
        }
    }

    private static void lootCheck(Location currentLocation, int playerBuildingIndex) {
        String nonSpecialLootTypes = "civil, military, hunting";

        if (nonSpecialLootTypes.contains(currentLocation.buildings[playerBuildingIndex].lootType)){
            System.out.println("your mom");
        }
    }

    public static void endGame(int endingType) throws FileNotFoundException {
        if (endingType == 1) {
            printFromTxt(printFile, 0, true);
            System.out.println();
            System.out.println("-= You died =-");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        //Buildings
        //Special Buildings (max loot tier[1], max loot amount[1])
        Building lake = new Building("Lake", "lake", 0, 1);
        Building radioTower = new Building("Radio tower", "radio tower", 0, 1);
        Building prison = new Building("Prison", "prison", 0,1);
        Building skiResort = new Building("Ski resort", "ski", 0, 1);

        //Civilian Buildings: (max loot tier[3], max loot amount[7])
        //Special civilian buildings
        Building houseSpawn = new Building("Your home", "spawn", 0, -1); //-1 means everything that can spawn, spawns

        //Low-tier civilian buildings
        Building houseS1 = new Building("Run down one story house", "civil", 0, 1);
        Building houseS2 = new Building("One story house", "civil", 0, 2);
        Building houseS3 = new Building("Fenced one story house", "civil", 0, 3);
        Building houseL1 = new Building("Two story house", "civil", 0, 4);
        Building apartment1 = new Building("Run down apartment", "civil", 0, 7);

        //Mid-tier civilian buildings
        Building houseS4 = new Building("Barricaded one story house", "civil", 1, 2);
        Building houseL2 = new Building("Fenced two story house", "civil", 1, 3);
        Building apartment2 = new Building("Apartment", "civil", 1, 6);

        //High-tier civilian buildings
        Building houseS5 = new Building("Fancy one story house", "civil", 2, 1);
        Building houseS6 = new Building("Barricaded, fancy one story house", "civil", 2, 2);
        Building houseL3 = new Building("Fancy two story house", "civil", 2, 3);
        Building mansionS = new Building("Mansion", "civil", 2, 5);
        Building mansionL = new Building("Large Mansion", "civil", 2, 7);


        //Hunting Buildings: (max loot tier[2], max loot amount[5])
        //Low-tier hunting buildings
        Building huntingStand1 = new Building("Elevated hunting Stand", "hunting", 1, 1);
        Building huntingStand2 = new Building("Ground hunting Stand", "hunting", 1, 2);

        //High-tier hunting buildings
        Building huntingCabinS = new Building("Two person hunting cabin", "hunting", 2, 2);
        Building huntingCabinL = new Building("Four person hunting cabin", "hunting", 2, 3);
        Building huntingShop = new Building("Hunting Gun Shop", "hunting", 2, 5);


        //Military Buildings: (max loot tier[4], max loot amount[5])
        //Low-tier military buildings
        Building militaryTentS = new Building("Small military Tent", "military", 1, 1);
        Building militaryTentL = new Building("Big military Tent", "military", 1, 2);

        //Mid-tier military buildings
        Building militaryBarracksS = new Building("Small military Barracks", "military", 2, 1);
        Building militaryBarracksL = new Building("Two story military Barracks", "military", 2, 3);

        //High-tier military buildings
        Building militaryArmory = new Building("Military Armory", "military", 3, 3);
        Building militaryHeadquarters = new Building("Military Headquarters", "military", 3, 5);

        //Top-tier military buildings
        Building militaryBunker = new Building("Military Bunker", "military", 4, 5);



        //Locations
        //Ocean locations
        Location ocean = new Location("Ocean", "ocean", 1, new Building[]{});

        //Woods locations
        Location woods1 = new Location("Woods", "woods", 1, new Building[]{});
        Location woods2 = new Location("Woods", "woods", 1, new Building[]{huntingStand1});
        Location woods3 = new Location("Woods", "woods", 1, new Building[]{huntingStand2});

        //Lake locations
        Location lake1 = new Location("Lake", "lake", 2, new Building[]{lake, huntingCabinS});
        Location lake2 = new Location("Lake", "lake", 1, new Building[]{lake});

        //Fields locations
        Location fields1 = new Location("Fields", "field", 3, new Building[]{});
        Location fields2 = new Location("Fields", "field", 4, new Building[]{houseL1});
        Location fields3 = new Location("Fields", "field", 3, new Building[]{houseS3});

        //Mountains locations
        Location mountain = new Location("Mountains", "mountain", 1, new Building[]{});

        //Special locations
        Location skiResortL = new Location("Ski resort", "ski resort", 4, new Building[]{skiResort, huntingCabinL, houseS2});
        Location prisonL = new Location("Prison", "prison", 5, new Building[]{prison});
        Location radioTowerL = new Location("Radio tower", "radio tower", 4, new Building[]{radioTower, houseS1});

        //Village locations
        Location skalka = new Location("Skalka", "village", 2, new Building[]{houseS1, huntingStand1});
        Location zolotar = new Location("Zolotar", "village", 3, new Building[]{houseS1, houseS2, huntingStand1});
        Location zub = new Location("Zub", "village", 1, new Building[]{houseS1, houseS1, huntingStand2});

        //Town locations
        Location solnechny = new Location("Solnechny", "town", 2, new Building[]{houseL1, houseS1, houseSpawn, huntingStand1, houseS1});
        Location galkino = new Location("Galkino", "town", 3, new Building[]{houseS1, houseL2, houseS4, huntingCabinS});
        Location krashnostav = new Location("Krashnostav", "town", 4, new Building[]{mansionS, houseS5, houseL3});
        Location svetloyarsk = new Location("Svetloyarsk", "town", 4, new Building[]{mansionL, houseS6, houseS5});
        Location vybor = new Location("Vybor", "town", 5, new Building[]{houseS1, houseS4, houseL2, militaryTentS});
        Location starysobor = new Location("Starysobor", "town", 7, new Building[]{houseL3, houseS5, huntingShop, houseS3});

        //City locations
        Location zelenogorsk = new Location("zelenogorsk", "city", 5, new Building[]{apartment1, houseL1, houseS1, houseL1, houseS1});
        Location severograd = new Location("Severograd", "city", 7, new Building[]{apartment1, houseL2, houseL3, houseS4, houseS5, militaryTentS});
        Location gorka = new Location("Gorka", "city", 4, new Building[]{houseL3, mansionS, houseL2, houseS3, houseS2, huntingStand1});
        Location elektro = new Location("Elektrozavaodsk", "city", 9, new Building[]{houseL3, houseS1, apartment2, houseL1, apartment1, huntingShop, houseS1});
        Location berezino = new Location("Berezino", "city", 7, new Building[]{houseL1, houseL2, houseS6, apartment1, mansionL, militaryTentL, houseS1});

        //Capital locations
        Location novo = new Location("Novodmitrovsk", "capital", 10, new Building[]{houseL3, houseL2, houseS4, houseS2, houseS5, houseS1, houseL1, apartment1, apartment2, huntingCabinL, huntingShop, militaryTentL, militaryTentS, houseS6});
        Location cherno = new Location("Chernogorsk", "capital", 10, new Building[]{houseL1, houseL1, houseS2, houseS3, houseS4, houseS1, houseL1, apartment1, apartment1, apartment2, huntingCabinS, huntingShop, militaryTentL, militaryTentS});

        //Military locations
        Location nwMilitary = new Location("NW military", "military", 15, new Building[]{militaryTentS, militaryBarracksS, militaryBarracksL, militaryArmory});
        Location swMilitary = new Location("SW military", "military", 15, new Building[]{militaryBarracksL, militaryTentL, militaryTentS, militaryTentL});
        Location eMilitary = new Location("E military", "military", 18, new Building[]{militaryHeadquarters, militaryTentL, militaryBarracksS, militaryTentS, militaryTentS});
        Location airfieldN = new Location("Airfield (N)", "military", 20, new Building[]{militaryArmory, militaryBarracksL, militaryBarracksS, militaryTentS, militaryTentS});
        Location airfieldS = new Location("Airfield (S)", "military", 20, new Building[]{militaryBunker, militaryHeadquarters, militaryBarracksL, militaryTentL, militaryTentS});

        //Quests
        //Main quest line
        Quest mainQuest = new Quest("Find Weapons", solnechny, 0, "main", null);

        //Side quests
        Quest sideQuest1 = new Quest("Side Quest Test", solnechny, 2, nwMilitary, 3, "main", true, null);
        Quest sideQuest2 = new Quest("Other side Quest Test", berezino, 0, eMilitary,2 , "main", true, null);

        createItems();
        Player.weapons.add(weapons.get(15));

        activeQuests.add(mainQuest);
        inactiveQuests.add(sideQuest1);
        inactiveQuests.add(sideQuest2);

        Location[][] map = {
                {nwMilitary,    woods2,     skalka,     lake1,      novo,           svetloyarsk,    ocean},
                {woods3,        fields3,    severograd, fields1,    krashnostav,    woods1,         ocean},
                {skiResortL,    airfieldN,  fields2,    woods1,     woods2,         woods1,         ocean},
                {mountain,      airfieldS,  fields1,    zolotar,    woods3,         eMilitary,      woods3},
                {galkino,       fields1,    vybor,      radioTowerL,gorka,          woods2,         berezino},
                {mountain,      zelenogorsk,starysobor, woods3,     woods1,         woods3,         woods1},
                {swMilitary,    fields3,    lake2,      woods1,     zub,            woods1,         solnechny},
                {prisonL,       fields1,    cherno,     fields2,    ocean,          elektro,        ocean}
        };

        boolean[][] shownLocationsMap = new boolean[8][7];
        for (boolean[] booleans : shownLocationsMap) {
            Arrays.fill(booleans, false);
        }
        shownLocationsMap[Player.playerY][Player.playerX] = true;

        printFromTxt(printFile, 0, false);
        System.out.println("Welcome to Z-rpg, an RPG Survival game set in a Zombie apocalypse");
        System.out.print("To continue to character creation press Enter: ");

        keyboard.nextLine();
        blank(25);

        String skipCharacterCreator = keyboard.nextLine();

        if (skipCharacterCreator.equals("12")) {
            Player.name = "devMode";
            Player.strength = 10;
            Player.stealth = 10;
            Player.aim = 10;
            Player.sight = 10;
            Player.health = 10;
            Player.age = 10;
            Player.length = 10;
            Player.weight = 10;
        } else {
            Player.createCharacter();
        }
        Player.currentHealth = 25 + Player.health*5;
        Player.maxHealth = Player.currentHealth;

        blank(100);
        printFromTxt(printFile, 0, false);
        System.out.println("--<{ Now the real game begins }>--");
        System.out.println("Use \"help\" for help with commands");

        String generalInput;
        Location currentLocation;
        currentLocation = map[Player.playerY][Player.playerX];
        boolean usedInput = false; //if player uses a command that takes a turn this changes to true

        while (true) {  //game loop

            if (usedInput) { //decides if Npcs should move or not
                currentLocation.moveNpcs(currentLocation);
                currentLocation.showBuildings();
                Player.lowerWeaponCD();
                usedInput = false;
            }
            System.out.println();   //Takes input for each iteration of the game loop
            System.out.print("Input: ");
            generalInput = keyboard.nextLine().toLowerCase();
            blank(100);
            System.out.println("Input: [" + generalInput + "]");
            blank(2);



            if (generalInput.equals("help")) { //takes you to the help menu which explains and lists all commands
                help();

            } else if (generalInput.contains("travel")) { //changes the location of the player one unit in one direction,(might cost energy and/or take time)
                if (Player.playerBuildingIndex == currentLocation.buildings.length-1 || currentLocation.buildings.length == 0){
                    if (Player.travel(generalInput)) {
                        System.out.println("You have entered " + map[Player.playerY][Player.playerX].name);
                        currentLocation = map[Player.playerY][Player.playerX];
                        currentLocation.checkedBuildings.clear();
                        Player.playerBuildingIndex = 0;
                        currentLocation.generateEncounters();
                        exploreMap(1, shownLocationsMap);
                        usedInput = true;
                    }

                } else {
                    System.out.print("You have to be at the right end of a city to travel to another location");
                }

            } else if (generalInput.equals("location")) { //displays the current location of the player + useful location information
                System.out.println("You are currently at " + currentLocation.name);
                System.out.println(currentLocation.name + " is a " + currentLocation.type);
                showLocationEncounterChance(currentLocation);
                System.out.println();
                currentLocation.showBuildings();

            } else if (generalInput.equals("stats")) { //displays the stats of the player
                Player.showCharacterStats();


            } else if (generalInput.equals("map")) { //displays the map
                printMap(map, shownLocationsMap);

            } else if (generalInput.equals("exit")) { //breaks the loop and exits the game,(might make this take the player back to some menu)
                break;

            } else if (generalInput.contains("move")) {
                Player.move(generalInput, currentLocation);
                usedInput = true;

            } else if (generalInput.equals("check")) {
                System.out.println("check");

            } else if (generalInput.contains("shoot")) {
                if(Player.shoot(generalInput, currentLocation)) {
                    usedInput = true;
                }

            } else if (generalInput.equals("quests")) {
                printQuests();

            } else if (generalInput.equals("loot")) {
                lootCheck(currentLocation, Player.playerBuildingIndex);

            } else { //if the input is invalid this message is displayed
                System.out.println("ERROR: your input was invalid, type help for help with commands.");
            }
        }
    }
}