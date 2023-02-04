import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Npc {
    File text = new File("src\\Player.txt");
    String name;
    int buildingIndex;
    int strength;
    int longRangeAim;
    int shortRangeAim;
    int maxHealth;
    int health;
    Weapon weapon;
    Armor[] armor;
    int targetLocation = -1;
    boolean aggressive = false;
    boolean alive = true;

    public Npc(String locationType, int buildingIndex) throws FileNotFoundException {
        Scanner fileReader = new Scanner(this.text);

        String currentLine = fileReader.nextLine().trim();
        ArrayList<Integer> commaLocation = new ArrayList<>();
        commaLocation.add(0);
        for(int i = 0; i < currentLine.length(); i++) {
            if (currentLine.charAt(i) == ',') {
                commaLocation.add(i);
            }
        }
        int random_int = (int)Math.floor(Math.random()*(commaLocation.size()));
        this.name = currentLine.substring(random_int, random_int+1);

        this.buildingIndex = buildingIndex;
        double difficultyMultiplier = 1.0;
        if (locationType.equals("military")){
            difficultyMultiplier = 3;
        } else if (locationType.equals("capital, city")) {
            difficultyMultiplier = 2;
        } else if (("woods, lake, field").contains(locationType)) {
            difficultyMultiplier = 0.5;
        }

        int difficulty = (int)Math.ceil(((8 - (Player.playerX))*difficultyMultiplier)); //difficulty max = 21, min = 1

        this.strength = difficulty;
        this.maxHealth = difficulty*10;
        this.health = maxHealth;
        this.shortRangeAim = difficulty;
        this.longRangeAim = difficulty;
        this.weapon = MainClass.getRandomWeapon(difficulty);
        this.armor = MainClass.getRandomArmor(difficulty);
    }

    public void action(Location currentLocation) throws FileNotFoundException {

        System.out.println(Math.round(Math.log10((11-Player.stealth)*4)+1)); //calculates max distance to see player

        if (Math.round(Math.log10((11-Player.stealth)*4)+1) >= Math.abs(Player.playerBuildingIndex-this.buildingIndex)) { //if npc sees player
            this.targetLocation = Player.playerBuildingIndex;
            this.aggressive = true;
        } else if (this.targetLocation < 0 || this.targetLocation > currentLocation.buildings.length-1) { //if target location is outside of town
            this.targetLocation = (int)Math.floor(Math.random()*currentLocation.buildings.length-1);
            this.aggressive = false;
        }

        int distanceToTarget = this.buildingIndex - this.targetLocation;

        if (aggressive) {
            if (Math.abs(distanceToTarget) == this.weapon.range) { //If player is at perfect range from npc
                if (!this.shoot(this.targetLocation, currentLocation)) { //If it shoots at nothing it is no longer aggressive
                    aggressive = false;
                }

            } else if (Math.abs(distanceToTarget) < this.weapon.range) { // if player is too close to npc
                if ((int)Math.floor(Math.random()*2) == 0) { //1/2 chance of shooting or running away from player
                    if (!this.shoot(this.targetLocation, currentLocation)) { //If it shoots at nothing it is no longer aggressive
                        aggressive = false;
                    }

                } else {
                    if(this.buildingIndex ++ < currentLocation.buildings.length) {
                        this.move(false, distanceToTarget);
                    } else {
                        this.shoot(this.targetLocation, currentLocation);
                    }
                }
            } else {
                this.move(true, distanceToTarget);
            }
        } else {
            this.move(true, distanceToTarget);
        }
    }
    private void move(boolean towardsTarget, int distanceToTarget) {
        System.out.println(this.buildingIndex + " moves");

        if (distanceToTarget > 0) { //if player is to the left
            if (towardsTarget) {
                this.buildingIndex --; //move npc left
            } else {
                this.buildingIndex ++; //move npc right
            }
        } else { //if player is to the right
            if (towardsTarget) {
                this.buildingIndex ++; //move npc right
            } else {
                this.buildingIndex --; //move npc left
            }
        }
    }

    private boolean shoot(int targetIndex, Location currentLocation) throws FileNotFoundException {
        System.out.println(this.buildingIndex + "shoots");
        return Player.takeDamage(targetIndex, this.weapon, this.buildingIndex, currentLocation);
    }

    public void takeDamage(Weapon currentWeapon, double hitChance, double meleeDamageMultiplier, Location currentlocation) throws FileNotFoundException {

        int hitLocation;
        ArrayList<Integer> hitLocations = new ArrayList<>();
        double damageMultiplier;

        for(int i = 0; i < currentWeapon.rpm; i++) {
            double damage = currentWeapon.damage;
            if(hitChance > Math.random()) {
                double randNum = (int)(Math.floor(Math.random()*4));
                if (randNum == 0) { //headshot
                    hitLocation = 1;
                    damageMultiplier = 2.5;
                    hitLocations.add(0);
                } else if (randNum == 1) { //legshot
                    hitLocation = 2;
                    damageMultiplier = 0.25;
                    hitLocations.add(2);
                } else { //body shot
                    hitLocation = 0;
                    hitLocations.add(1);
                    damageMultiplier = 1;
                }

                if (this.armor[hitLocation] != null) {
                    double firstDamage = damage;
                    damage = (damage/3*2 - (this.armor[hitLocation].armorHealth * (1 - this.weapon.armorPen)));
                    if(damage < 0) {
                        damage = 0;
                    }
                    damage = ((damage + (firstDamage/3)) * damageMultiplier);
                } else {
                    damage *= damageMultiplier;
                }

                if (currentWeapon.range == 0) {
                    damage *= meleeDamageMultiplier;
                }

                if (damage > 0) {
                    this.health -= (int)damage;
                }
                System.out.println("You hit: " + damage);
            } else {
                System.out.println("You missed: 0");
            }
        }

        MainClass.drawHitLocations(hitLocations);
        if (this.health > 0) {
            System.out.println("[" + this.health + "/" + this.maxHealth + "]");
        } else if (this.health < -200){
            System.out.println("[" + this.health + "/" + this.maxHealth + "]");
            System.out.println("Way out of pocket, don't disrespect my mans family tree like that");
        } else {
            System.out.println("[0/" + this.maxHealth + "]");
        }
        this.healthCheck(currentlocation);
    }

    private void healthCheck(Location currentLocation) {
        if (this.health <= 0) {
            System.out.println(this.name + "is dead");
            this.alive = false;
            currentLocation.lootedBuildings[this.buildingIndex] = false;
        }
    }
}