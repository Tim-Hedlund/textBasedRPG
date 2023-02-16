import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Weapon {

    File file = new File ("./Weapon.txt");
    String name;        //name of the weapon
    ArrayList<String> lootType = new ArrayList<>();  //Loot type of weapon, aka which buildings it spawns in
    int lootTier;       //Tier of loot it spawns at and above
    double lootRarity;  //How rare it is that it spawns at given loot tier
    int damage;         //damage per shot
    int rpm;            //bullets per shot calculation
    int range;          //how many buildings in a location it can shoot, 0 is same building, 1 is next building and so on
    double accuracy;    //Max 1, %chance of each bullet HITTING per building shot past, same building means X1.
    double armorPen;    //%of damage that makes it through armor
    int cooldown;       //round needed to be used again(reload time) 0 means active next round
    int currentCD = 0;

    public Weapon(String currentLine) {

        ArrayList<Integer> commaLocation = new ArrayList<>(); //create array of all commas on current line
        commaLocation.add(0);
        for(int i = 0; i < currentLine.length(); i++) {
            if (currentLine.charAt(i) == ',') {
                commaLocation.add(i);
            }
        }
        commaLocation.add(currentLine.length());

        this.name = currentLine.substring(commaLocation.get(0), commaLocation.get(1)); //set name

        String lootTypes = (currentLine.substring(commaLocation.get(1)+1, commaLocation.get(2))).trim();
        ArrayList<Integer> plusLocation = new ArrayList<>();
        plusLocation.add(0);

        for(int i = 0; i < lootTypes.length(); i++) {
            if (lootTypes.charAt(i) == '+') {
                plusLocation.add(i);
            }
        }
        plusLocation.add(lootTypes.length());

        for(int i = 0; i < plusLocation.size()-1; i++) {
            this.lootType.add(lootTypes.substring(plusLocation.get(i), plusLocation.get(i+1)).trim());
        }

        this.lootTier = Integer.parseInt(currentLine.substring(commaLocation.get(2)+1, commaLocation.get(3)).trim());
        this.lootRarity = Double.parseDouble(currentLine.substring(commaLocation.get(3)+1, commaLocation.get(4)).trim());
        this.damage = Integer.parseInt(currentLine.substring(commaLocation.get(4)+1, commaLocation.get(5)).trim());
        this.rpm = Integer.parseInt(currentLine.substring(commaLocation.get(5)+1, commaLocation.get(6)).trim());
        this.range = Integer.parseInt(currentLine.substring(commaLocation.get(6)+1, commaLocation.get(7)).trim());
        this.accuracy = Double.parseDouble(currentLine.substring(commaLocation.get(7)+1, commaLocation.get(8)).trim());
        this.armorPen = Double.parseDouble(currentLine.substring(commaLocation.get(8)+1, commaLocation.get(9)).trim());
        this.cooldown = Integer.parseInt(currentLine.substring(commaLocation.get(9)+1, commaLocation.get(10)).trim());

    }

    public Weapon(String name, ArrayList<String> lootType, int lootTier, double lootRarity, int damage, int rpm, int range, double accuracy, double armorPen, int cooldown){
        this.name = name;
        this.lootType = lootType;
        this.lootTier = lootTier;
        this.lootRarity = lootRarity;
        this.damage = damage;
        this.rpm = rpm;
        this.range = range;
        this.accuracy = accuracy;
        this.armorPen = armorPen;
        this.cooldown = cooldown;
    }

    public void lowerWeaponCD() { //lowers weaponCD by 1
        if (this.currentCD > 0) {
            this.currentCD -= 1;
        }
    }

    public void setWeaponCD() {
        this.currentCD = this.cooldown+1;
    }

    public boolean checkCD() {
        return this.currentCD == 0;
    }
}
