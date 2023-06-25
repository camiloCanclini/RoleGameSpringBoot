package com.canclini.rolegame.gameplay;

public abstract class Card implements CardCombatSystem{

    // INFORMATION
    private String name;
    private String nickname;
    private String birthdate;

    // ATRIBUTES
    // byte uses only 8 bits (-128 hasta 128) - int uses 16 bits
    protected int age;
    private byte health;
    private byte speed;
    private byte dexterity;
    private byte strength;
    private byte level;
    private byte armor;

    // ATTRIBUTES MIN AND MAX VALUES

    public static final int ageMaxValue = 300;
    public static final int healthMaxValue = 100;
    public static final int speedMaxValue = 10;

    public static final int dexterityMaxValue = 5;

    public static final int strengthMaxValue = 10;

    public static final int levelMaxValue = 10;

    public static final int armorMaxValue = 10;

    // IMAGE
    private String imageSrc;

    public Card(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte level, byte armor, String imageSrc) {
        this.name = name;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.setAge(age); // checks...
        this.setHealth(health);
        this.setSpeed(speed);
        this.setDexterity(dexterity);
        this.setStrength(strength);
        this.setLevel(level);
        this.setArmor(armor);
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > ageMaxValue) {
            throw new Error("Error Trying to Set the Age Atrribute");
        }
        this.age = age;
    }

    public byte getHealth() {
        return health;
    }

    public void setHealth(byte health) {
        if (health <= 0 || health > healthMaxValue) {
            throw new Error("Error Trying to Set the Health Atrribute");
        }
        this.health = health;
    }

    public byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) {
        if (speed <= 0 || speed > speedMaxValue) {
            throw new Error("Error Trying to Set the Speed Atrribute");
        }
        this.speed = speed;
    }

    public byte getDexterity() {
        return dexterity;
    }

    public void setDexterity(byte dexterity) {
        if (dexterity <= 0 || dexterity > dexterityMaxValue) {
            throw new Error("Error Trying to Set the Dexterity Atrribute");
        }
        this.dexterity = dexterity;
    }

    public byte getStrength() {
        return strength;
    }

    public void setStrength(byte strength) {
        if (strength <= 0 || strength > strengthMaxValue) {
            throw new Error("Error Trying to Set the Strength Atrribute");
        }
        this.strength = strength;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        if (level <= 0 || level > levelMaxValue) {
            throw new Error("Error Trying to Set the Level Atrribute");
        }
        this.level = level;
    }

    public byte getArmor() {
        return armor;
    }

    public void setArmor(byte armor) {
        if (armor <= 0 || armor > armorMaxValue) {
            throw new Error("Error Trying to Set the Armor Atrribute");
        }
        this.armor = armor;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
