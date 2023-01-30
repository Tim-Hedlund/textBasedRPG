import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Player {

    static Scanner keyboard = new Scanner(System.in);
    //scanner

    static ArrayList<Weapon> weapons = new ArrayList<>();
    static ArrayList<Armor> armors = new ArrayList<>();
    static Armor[] currentArmor = new Armor[3];

    static String name;
    //name

    static boolean hasKey = false;

    static int strength;
    static int stealth;
    static int aim;
    static int sight;
    static int health;

    static int currentHealth;
    static int maxHealth;
    //main stats

    static int age;
    static int length;
    static int weight;
    //less important stats

    static int playerX = 6;
    static int playerY = 6;
    //current location, max y = 7, max x = 6, location 6 6 is Solnechny

    static int playerBuildingIndex = 2;


    static File printFile = new File("src\\Player.txt");

    static int intTest(String input){

        while(true) {
            int j = 0;
            for (int i = 0; i < input.length(); i++) { //loopen kommer hända lika länge som texten är lång

                if (!Character.isDigit(input.charAt(i))) { //checkar om karaktären vid en viss index(i) inte är ett nummer
                    System.out.println("ERROR: \"" + input.charAt(i) + "\" is not an Integer");

                } else {
                    j++; //lägger till ett värde till J varje gång karaktären som testas är ett nummer
                    if (i == j - 1 && j == input.length()) { //if statement som bara är sant om alla karaktärer är nummer
                        return Integer.parseInt(input); //returnar inputen som en integer
                    }
                }
            }
            System.out.println("Input an integer"); //ger ett error message varje gång return inte händer
            input = keyboard.nextLine(); //tar ny input om fel input gavs som input till intTest metoden

        }
        /*metod för att testa om en input som ska bli en int kan vara en int
        för att undvika krascher om man inte skriver in det man menar eller klickar
        enter när man inte menar det.*/
    }

    static void move(String input, Location currentLocation){
        if (input.contains("move ")){
            input = input.replace("move ", "").trim();
        }

        if(currentLocation.buildings.length != 0) {
            if ((input.equals("left") || input.equals("l")) && playerBuildingIndex != 0){
                playerBuildingIndex --;
            } else if ((input.equals("right") || input.equals("r")) && playerBuildingIndex != currentLocation.buildings.length-1){
                playerBuildingIndex ++;
            } else {
                System.out.println("There are no more buildings in this direction");
            }
        } else {
            System.out.println("There are no buildings at this location");
        }
    }

    static void blank(int repetitions){

        for (int i = 0; i < repetitions; i++) {
            System.out.println();

        }

        //metod för att skapa (repetitions) tomma linjer för att skapa känslan av att byta sida
        //eller gå till en meny.
    }

    static void showCharacterStats(){
        System.out.println("  -=={[ STATS ]}==-  ");
        System.out.println("=====================");
        System.out.println(" length:   " + length + "cm");
        System.out.println("---------------------");
        System.out.println(" weight:   " + weight + "kg");
        System.out.println("---------------------");
        System.out.println(" age:      " + age + " yrs");
        System.out.println("=====================");
        System.out.println(" Strength: " + strength);
        System.out.println("---------------------");
        System.out.println(" Stealth:  " + stealth);
        System.out.println("---------------------");
        System.out.println(" Aim:      " + aim);
        System.out.println("---------------------");
        System.out.println(" Sight:    " + sight);
        System.out.println("---------------------");
        System.out.println(" Health:   " + health);
        System.out.println("=====================");

    }
    static boolean travel(String direction) {
        if (direction.contains("travel ")){
            direction = direction.replace("travel ", "");
        }

        switch (direction) {
            case "north":
                if (playerY == 0) {
                    System.out.println("You are as far north as you can go");
                    return false;
                } else {
                    playerY--;
                }
                break;
            case "south":
                if (playerY == 7) {
                    System.out.println("You are as far south as you can go");
                    return false;
                } else {
                    playerY++;
                }
                break;
            case "west":
                if (playerX == 0) {
                    System.out.println("You are as far west top as you can go");
                    return false;
                } else {
                    playerX--;
                }
                break;
            case "east":
                if (playerX == 6) {
                    System.out.println("You are as far east as you can go");
                    return false;
                } else {
                    playerX++;
                }
                break;
            default:
                System.out.println("Your input is invalid, write a direction(north, west, south, east) after move to choose direction.");
                return false;
        }
        return true;
    }

    static void createCharacter() throws FileNotFoundException {

        MainClass.printFromTxt(printFile, 0, true);
        System.out.print  ("press Enter to continue: ");
        keyboard.nextLine();
        blank(5);

        MainClass.printFromTxt(printFile, 1, false);
        System.out.print("Age(yrs)    ");
        age = intTest(keyboard.nextLine());
        System.out.print("Length(cm): ");
        length = intTest(keyboard.nextLine());
        System.out.print("Weight(kg): ");
        weight = intTest(keyboard.nextLine());

        blank(4);
        MainClass.printFromTxt(printFile, 2, false);
        String strInput = keyboard.nextLine().toLowerCase();

        if (strInput.equals("help")){
            blank(10);
            System.out.println("there is nothing here yet");
            System.out.println("just press enter to exit");
            keyboard.nextLine();
            blank(10);

        } else {
            blank(20);
        }

        System.out.println("You rolled: ");
        System.out.print("| ");
        ArrayList<Integer> statRolls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            statRolls.add((int)(Math.floor(Math.random()*10)+1));
            System.out.print(statRolls.get(i) + " | ");
        }


        String mainStatsNames = ("| strength | stealth | aim | sight | health |");

        for (int i = 0; i < 5; i++) {
            blank(1);
            MainClass.writeCharacter(mainStatsNames.length(), "=", true);
            System.out.println(mainStatsNames);
            MainClass.writeCharacter(mainStatsNames.length(), "=", true);
            System.out.println("What stat would you like to be " + statRolls.get(i) + " points?");
            strInput = keyboard.nextLine().toLowerCase();
            while (!mainStatsNames.contains(strInput)) {
                System.out.println("ERROR: Your input is not a stat or has already been used");
                System.out.println("What stat would you like to be " + statRolls.get(i) + " points?");
                strInput = keyboard.nextLine().toLowerCase();
            }

            switch (strInput) {
                case "strength" -> {
                    strength = statRolls.get(i);
                    mainStatsNames = mainStatsNames.replace("strength", "        ");
                }
                case "stealth" -> {
                    stealth = statRolls.get(i);
                    mainStatsNames = mainStatsNames.replace("stealth", "       ");
                }
                case "aim" -> {
                    aim = statRolls.get(i);
                    mainStatsNames = mainStatsNames.replace("aim", "   ");
                }
                case "sight" -> {
                    sight = statRolls.get(i);
                    mainStatsNames = mainStatsNames.replace("sight", "     ");
                }
                case "health" -> {
                    health = statRolls.get(i);
                    mainStatsNames = mainStatsNames.replace("health", "      ");
                }
                default -> {
                    System.out.println("Error: your input is invalid");
                    i--;
                }
            }
        }
        blank(10);
        System.out.println("Your characters final stats are as follows:");
        showCharacterStats();
        System.out.println("Press Enter to continue");
        keyboard.nextLine();
        blank(10);

        MainClass.printFromTxt(printFile, 3, false);
        System.out.print("Input your characters name:");
        name = keyboard.nextLine();
        System.out.println();
        boolean confirmedName = false;
        while (!confirmedName){
            System.out.println("Are you sure you want your name to be: " + name);
            System.out.println("write \"confirm\" to confirm");
            if (keyboard.nextLine().equals("confirm")) {
                confirmedName = true;
            } else {
                System.out.print("You did not confirm with \"confirm\", enter a new name: ");
                name = keyboard.nextLine();
            }
        }
    }
    public static boolean shoot(String generalInput, Location currentLocation) throws FileNotFoundException {
        Weapon currentWeapon = null;
        String target = "";
        if (generalInput.length() > 6) {
            target = generalInput.trim().substring(6);
        }
        int targetBuildingIndex = -1;

        for(int i = 0; i < currentLocation.buildings.length; i++) { //gets the index of targeted building
            if (target.equalsIgnoreCase(currentLocation.buildings[i].name)){
                targetBuildingIndex = i;
                break;
            }
        }

        while (targetBuildingIndex == -1) { //if loop above does not find the target
            System.out.println("Choose location");
            target = keyboard.nextLine().trim().toLowerCase();
            System.out.println(target);
            for(int i = 0; i < currentLocation.buildings.length; i++) { //gets the name of targeted building
                if (target.equalsIgnoreCase(currentLocation.buildings[i].name)){
                    targetBuildingIndex = i;
                    break;
                }
            }
        }

        boolean loop = true;
        while (loop) {  //gets the weapon to shoot with
            System.out.println("Choose weapon");
            printWeapons();

            generalInput = keyboard.nextLine();
            if (generalInput.equals("cancel")) {
                break;
            } else {
                System.out.println(generalInput);
                for(Weapon weapon: weapons) {
                    if (generalInput.equalsIgnoreCase(weapon.name)) {
                        System.out.println(weapon);
                        if(weapon.checkCD()) {
                            currentWeapon = weapon;
                            loop = false;
                            break;
                        } else {
                            System.out.println("Weapon is not ready");
                        }

                    }
                }
            }
        }

        double damageMultiplier = 1;
        if(!loop) { //if the loop was broken after setting loop to false, (if weapon is got)
            double hitChance = ((Math.pow(currentWeapon.accuracy, 0.5 * Math.abs(targetBuildingIndex-Player.playerBuildingIndex)))*(0.5+(float)aim/10));
            System.out.println(hitChance);
            if(hitChance == 0) {
                if (currentWeapon.range == 0) {
                    damageMultiplier = (0.7+(double)Player.strength/10);
                    System.out.println(damageMultiplier);
                }
                hitChance = currentWeapon.accuracy;
            }
            currentLocation.takeDamage(targetBuildingIndex, currentWeapon, hitChance, damageMultiplier);
            currentWeapon.setWeaponCD();
        }
        return !loop;
    }

    static void printWeapons() {
        for (Weapon weapon: weapons) {
            System.out.println(weapon.name);
        }
    }

    public static void lowerWeaponCD() {
        for (Weapon weapon: weapons) {
            weapon.lowerWeaponCD();
        }
    }

    public static boolean takeDamage(int targetBuildingIndex, Weapon npcWeapon, int npcIndex) throws FileNotFoundException {

        if (targetBuildingIndex == Player.playerBuildingIndex) {
            double hitChance = Math.pow(npcWeapon.accuracy, 0.5 * Math.abs(Player.playerBuildingIndex-npcIndex));
            if(hitChance == 0 || npcWeapon.range == 0) {
                hitChance = npcWeapon.accuracy;
            }

            int hitLocation;
            double damageMultiplier;

            for(int i = 0; i < npcWeapon.rpm; i++) {
                double damage = npcWeapon.damage;
                if(hitChance > Math.random()) {
                    double randNum = (int)(Math.floor(Math.random()*4));
                    if (randNum == 0) { //headshot
                        hitLocation = 1;
                        damageMultiplier = 2.5;
                    } else if (randNum == 1) { //legshot
                        hitLocation = 2;
                        damageMultiplier = 0.25;
                    } else { //body shot
                        hitLocation = 0;
                        damageMultiplier = 1;
                    }

                    if (currentArmor[hitLocation] != null) {
                        double firstDamage = damage;
                        damage = (damage/3*2 - (currentArmor[hitLocation].armorHealth * (1 - npcWeapon.armorPen)));
                        if(damage < 0) {
                            damage = 0;
                        }
                        damage = ((damage + (firstDamage/3)) * damageMultiplier);
                    } else {
                        damage *= damageMultiplier;
                    }

                    if (damage > 0) {
                        currentHealth -= (int)damage;
                    }
                    System.out.println("You got hit: " + damage);
                    System.out.println();
                    System.out.println("[" + currentHealth + "/" +  maxHealth + "]");
                    healthCheck();
                }
            }
            return true;
        }
        return false;
    }

    private static void healthCheck() throws FileNotFoundException {
        if (currentHealth < 0) {
            MainClass.endGame(1);
        } else {
            currentHealth += maxHealth/10;
        }

        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public static void invCheck() throws FileNotFoundException {
        File artfile = new File("src\\Art.txt");
        if (currentArmor[1] != null) {
            MainClass.printFromTxt(artfile, 3, false);
        } else {
            MainClass.printFromTxt(artfile, 4, false);
        }
        if (currentArmor[0] != null) {
            MainClass.printFromTxt(artfile, 5, false);
        } else {
            MainClass.printFromTxt(artfile, 6, false);
        }
        MainClass.printFromTxt(artfile, 7, false);
        blank(2);

        System.out.println("--= inventory =--");
        System.out.println();
        System.out.println("armor:");
        for (Armor armor: armors) {
            System.out.println(armor.name + " -- health: " +  armor.armorHealth);
        }
        System.out.println();
        System.out.println("weapons:");
        for (Weapon weapon: weapons) {
            System.out.println(weapon.name + " -- damage: " +  weapon.damage + ", rpm: " + weapon.rpm + ", cd: " + weapon.currentCD);
        }
        blank(2);
    }

    public static void equip(String generalInput) {

        Armor targetArmor = null;
        for (Armor armor: armors) {
            if (generalInput.contains(armor.name.toLowerCase())) {
                targetArmor = armor;
            }
        }
        if (targetArmor == null) {
            System.out.println("You dont have an armor named " + generalInput.substring(5).trim());
        } else {
            currentArmor[targetArmor.armorSlot] = targetArmor;
            System.out.println("You equipped " + targetArmor.name);
        }
        blank(2);
    }
}