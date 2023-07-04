package com.canclini.rolegame.gameplay;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Card implements CardCombatSystem{

    // INFORMATION
    private String name;
    private String nickname;
    private String birthdate;

    // ATRIBUTES
    // byte uses only 8 bits (-128 hasta 128) - int uses 16 bits
    protected Integer age;
    private Byte health;
    private Byte speed;
    private Byte dexterity;
    private Byte strength;
    private Byte level;
    private Byte armor;

    // ATTRIBUTES MIN AND MAX VALUES

    public static final Integer ageMaxValue = 300;
    public static final Byte healthMaxValue = 100;
    public static final Byte speedMaxValue = 10;

    public static final Byte dexterityMaxValue = 5;

    public static final Byte strengthMaxValue = 10;

    public static final Byte levelMaxValue = 10;

    public static final Byte armorMaxValue = 10;

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

    public Byte getHealth() {
        return health;
    }

    public void setHealth(byte health) {
        if (health <= 0 || health > healthMaxValue) {
            throw new Error("Error Trying to Set the Health Atrribute");
        }
        this.health = health;
    }

    public Byte getSpeed() {
        return speed;
    }

    public void setSpeed(byte speed) {
        if (speed <= 0 || speed > speedMaxValue) {
            throw new Error("Error Trying to Set the Speed Atrribute");
        }
        this.speed = speed;
    }

    public Byte getDexterity() {
        return dexterity;
    }

    public void setDexterity(byte dexterity) {
        if (dexterity <= 0 || dexterity > dexterityMaxValue) {
            throw new Error("Error Trying to Set the Dexterity Atrribute");
        }
        this.dexterity = dexterity;
    }

    public Byte getStrength() {
        return strength;
    }

    public void setStrength(byte strength) {
        if (strength <= 0 || strength > strengthMaxValue) {
            throw new Error("Error Trying to Set the Strength Atrribute");
        }
        this.strength = strength;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        if (level <= 0 || level > levelMaxValue) {
            throw new Error("Error Trying to Set the Level Atrribute");
        }
        this.level = level;
    }

    public Byte getArmor() {
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

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", age=" + age +
                ", health=" + health +
                ", speed=" + speed +
                ", dexterity=" + dexterity +
                ", strength=" + strength +
                ", level=" + level +
                ", armor=" + armor +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
