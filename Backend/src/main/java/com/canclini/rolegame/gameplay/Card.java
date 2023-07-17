package com.canclini.rolegame.gameplay;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
public abstract class Card implements CardCombatSystem, Cloneable{

    @Override
    public Card clone() {
        try {
            Card clone = (Card) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public enum CardType {
        HUMAN,
        ORC,
        ELF
    }
    public enum MoveType {
        HIT,
        SPELL,
        DEFEND
    }
    // SE USA EN LA APLICACION CLIENTE
    private CardType typeCard; // 1-> Human, 2-> Orc, 3-> Elf

    // INFORMATION
    private String name;
    private String nickname;
    private String birthdate;

    protected Integer age;

    // ATRIBUTES
    // byte uses only 8 bits (-128 hasta 128) - int uses 16 bits

    private Byte health;
    private Byte speed;
    private Byte dexterity;
    private Byte strength;
    private Byte magic;
    private Byte level;
    private Byte armor;

    // ATTRIBUTES MIN AND MAX VALUES

    public static final Integer ageMaxValue = 300;
    public static final Byte healthMaxValue = 100;
    public static final Byte speedMaxValue = 10;

    public static final Byte dexterityMaxValue = 5;

    public static final Byte strengthMaxValue = 10;
    public static final Byte magicMaxValue = 10;

    public static final Byte levelMaxValue = 10;

    public static final Byte armorMaxValue = 10;

    // IMAGE
    private String imageSrc;

    public Integer getShootPower() {
        return shootPower;
    }

    public void setShootPower() {
        this.shootPower = this.strength * this.dexterity * this.level;
    }
    public Integer getMagicPower() {
        return magicPower;
    }

    public void setMagicPower() {
        this.magicPower = this.magic * this.dexterity * this.level;
    }

    public Double getShootEffectiveness() {
        return shootEffectiveness;
    }

    public void setShootEffectiveness() {
        Random randomizer = new Random();
        this.shootEffectiveness = Math.round(randomizer.nextFloat() * 100) / 100.0;
    }

    public Integer getHitValue() {
        return hitValue;
    }

    public void setHitValue() {
        this.hitValue = (int) (this.shootPower * this.shootEffectiveness);
    }

    public Integer getSpellValue() {
        return spellValue;
    }

    public void setSpellValue() {
        this.spellValue = (int) (this.magicPower *  this.shootEffectiveness);
    }

    public Integer getDefensePower() {
        return defensePower;
    }

    public void setDefensePower() {
        this.defensePower = this.armor * this.speed;
    }

    private Integer shootPower;
    private Integer magicPower;
    private Double shootEffectiveness;
    private Integer spellValue;
    private Integer hitValue;
    private Integer defensePower;

    public Card(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte magic, byte armor, String imageSrc) {
        this.name = name;
        this.nickname = nickname;
        this.birthdate = birthdate;
        this.setAge(age); // checks...
        this.setHealth(health);
        this.setSpeed(speed);
        this.setDexterity(dexterity);
        this.setStrength(strength);
        this.setMagic(magic);
        this.setLevel(level);
        this.setArmor(armor);
        this.imageSrc = imageSrc;

        setCombatStats();
    }



    public CardType getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(int typeCardNumber) {
        switch (typeCardNumber) {
            case 0 -> this.typeCard = CardType.HUMAN;
            case 1 -> this.typeCard = CardType.ORC;
            case 2 -> this.typeCard = CardType.ELF;
        }
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

    public Byte getMagic() {
        return magic;
    }

    public void setMagic(Byte magic) {
        if (magic <= 0 || magic > magicMaxValue) {
            throw new Error("Error Trying to Set the Strength Atrribute");
        }
        this.magic = magic;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        if (level <= 0) {
            throw new Error("Error Trying to Set the Level Atrribute");
        }
        if (level > levelMaxValue) {
            this.level = levelMaxValue;
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

    public void getDamage (byte damageReceived){
        if (this.health < damageReceived){
            this.health = 0;
        } else {
            this.setHealth((byte) (this.health - damageReceived));
        }
    }

    public void healCard (int healthReceived){
        if (healthReceived > healthMaxValue){
            this.health = 100;
            return;
        }
        if (healthReceived + this.health > 100) {
            this.health = 100;
            return;
        }
        if (healthReceived <= 0) {
            throw new RuntimeException("No se puede curar con valores negativos");
        }
        this.health = (byte) (this.health + healthReceived);

    }

    public void setCombatStats (){
        setShootPower();
        setMagicPower();
        setShootEffectiveness();
        setHitValue();
        setSpellValue();
        setDefensePower();
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
