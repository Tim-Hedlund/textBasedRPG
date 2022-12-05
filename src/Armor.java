import java.util.ArrayList;

public class Armor {
    String name;        //name of the weapon
    ArrayList<String> lootType = new ArrayList<>();  //Loot type of weapon, aka which buildings it spawns in
    int lootTier;       //Tier of loot it spawns at and above
    double lootRarity;  //How rare it is that it spawns at given loot tier
    int armorHealth;    //how much health armor has
    int armorSlot;

    public Armor(String currentLine) {

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
            this.lootType.add(lootTypes.substring(plusLocation.get(i), plusLocation.get(i+1)));
        }

        this.lootTier = Integer.parseInt(currentLine.substring(commaLocation.get(2)+1, commaLocation.get(3)).trim());
        this.lootRarity = Double.parseDouble(currentLine.substring(commaLocation.get(3)+1, commaLocation.get(4)).trim());
        this.armorHealth = Integer.parseInt(currentLine.substring(commaLocation.get(4)+1, commaLocation.get(5)).trim());
        this.armorSlot = Integer.parseInt(currentLine.substring(commaLocation.get(5)+1, commaLocation.get(6)).trim());

    }
}
