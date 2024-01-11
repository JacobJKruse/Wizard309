package com.example.wizard309.entities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.example.wizard309.entities.cardClass;
import com.example.wizard309.R;
import com.example.wizard309.main.MainActivity;

import java.util.Random;

/**
 * Card Object Class, generates the interactive card object
 */
public class Card {

    private final int element;
    private String elementStr;
    private ValueAnimator opacityAnimator;

    private final String extension;
    private int attackPower;
    private final int manaCost;
    private String attackType = null;
    private float x,y;
    private boolean clicked = false;

    private String cardName = null;
    private int cardBackgroundval;
    private int cardboarderint;
    private float r,g,b;

    private int cardClassValue; // Renamed variable

    private int id;

    /**
     * x and y snapping positions
     */
    public float xSnap,ySnap;
    private boolean isFlashing = false;
//    public Card(int x, int y, String name, int cardClass){
//        this.x = x;
//        this.y = y;
//        this.xSnap = this.x;
//        this.ySnap = this.y;
//        this.cardClass = cardClass;
//        this.cardName = name;
//        Random rand = new Random();
//        cardBackgroundval = rand.nextInt(5);
//        cardboarderint = rand.nextInt(3);
//        //onCreate();
//
//    }

    /**
     * New public constructor for card, this implements the cards from the deck
     * @param id
     * @param extension
     * @param spellName
     * @param element
     * @param attackPower
     * @param manaCost
     * @param attackType
     */
    public Card(int id, String extension, String spellName, String element, int attackPower, int manaCost, String attackType){
    this.id = id;
    this.extension = extension;
    elementStr = element;
    this.cardName = spellName;
    this.element = setCardClass(element);
    this.attackPower = attackPower;
    this.manaCost = manaCost;
    this.attackType = attackType;
    Random rand = new Random();
    cardBackgroundval = rand.nextInt(5);
    cardboarderint = rand.nextInt(3);
}

    private int setCardClass(String element) {
        switch(element){
            case "Earth":
                return cardClass.GRASS.getValue();
            case "Fire":
                return cardClass.FIRE.getValue();
            case "Water":
                return cardClass.WATER.getValue();
            case "Rock":
                return cardClass.ROCK.getValue();
            case "Storm":
                return cardClass.ELECTRIC.getValue();
            case "Ice":
                return cardClass.ICE.getValue();
            case "Death":
                return cardClass.DEATH.getValue();
            case "Life":
                return cardClass.LIFE.getValue();
            default:
                throw new IllegalArgumentException("Invalid element: " + element);
        }
    }

    /**
     * returns the id of the card
     * @return id
     */
    public int getID(){
        return id;
    }

    /**
     * returns the extension of the card image
     * @return
     */
    public String getExtension(){return  extension;}

    /**
     * returns the element string
     * @return ellementstr
     */
    public String  getElement(){return elementStr;}

    /**
     * returns the manacost of a card
     * @return mana cost
     */
    public int getManaCost(){return manaCost;}

    /**
     * returns the attack type of the card
     * @return attack type
     */
    public String getType(){
        return attackType;
    }
    /**
     * sets card spot depending on param spot
     * @param spot
     */
    public void setCardSpot(int spot){
        switch(spot){
            case 1:
                this.x = MainActivity.screenWidth/2-800- (103*2);
                this.y = MainActivity.screenHeight-300;
                this.xSnap = this.x;
                this.ySnap = this.y;
                break;
            case 2:
                this.x = MainActivity.screenWidth/2-400-(103*2);
                this.y = MainActivity.screenHeight-300;
                this.xSnap = this.x;
                this.ySnap = this.y;
                break;
            case 3:
                this.x = MainActivity.screenWidth/2-(103*2);
                this.y = MainActivity.screenHeight-300;
                this.xSnap = this.x;
                this.ySnap = this.y;
                break;
            case 4:
                this.x = MainActivity.screenWidth/2+400-(103*2);
                this.y = MainActivity.screenHeight-300;
                this.xSnap = this.x;
                this.ySnap = this.y;
                break;
            case 5:
                this.x = MainActivity.screenWidth/2+800-(103*2);
                this.y = MainActivity.screenHeight-300;
                this.xSnap = this.x;
                this.ySnap = this.y;
                break;
        }
    }

    private void onCreate() {
        float finalY = y;
        y = 1200;
        while(y>= finalY){
        y-= 10;
        }
        y = finalY;

    }

    /**
     * return the name of the card
     * @return card name
     */
    public String getName(){
        return cardName;
    }

    /**
     * returns the x position of the card
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * returns the y pos of the card
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Loads the pixel font
     * @param context
     * @param fontResId
     * @return
     */
    public static Typeface loadFont(Context context, int fontResId) {
        return context.getResources().getFont(fontResId);
    }

    /**
     * draws the current card selected
     * @param c Main canvas for the card to be drawn on
     */
    public void draw(Canvas c){
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(0,0,0));
        textPaint.setTextSize(24);
        textPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));

        // Create a new paint for the card with opacity
        textPaint.setAlpha(isFlashing ? (int) opacityAnimator.getAnimatedValue() : 255); // Use the animated value

        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.rgb(255,255,255));
        mTextPaint.setTextSize(28);
        mTextPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));

        // Create a new paint for the card with opacity
        mTextPaint.setAlpha(isFlashing ? (int) opacityAnimator.getAnimatedValue() : 255); // Use the animated value

        c.drawBitmap(CardIcons.cardBackgrounds.getSprite(0,element), x , y, textPaint); //example
        c.drawBitmap(CardIcons.cardBoarders.getSprite(0,cardboarderint), x , y, textPaint); //example

        c.drawBitmap(CardIcons.cardTitles.getSprite(0,cardboarderint),x+60,y+320,textPaint);
        c.drawBitmap(getImageExt(extension),x+100,y+50,textPaint);
        c.drawBitmap(CardIcons.cardIconBoarder.getSprite(0,cardboarderint),x+90,y+43,textPaint);


        c.drawBitmap(CardIcons.manaBackground.getSprite(0,0),x+280,y+30,textPaint);
        c.drawBitmap(CardIcons.attackBackground.getSprite(0,0),x+30,y+30,textPaint);
        c.drawText(String.valueOf(attackPower),x+40,y+90,mTextPaint);
        c.drawText(String.valueOf(manaCost),x+305,y+90,mTextPaint);

        // Break the card name into multiple lines if it's longer than 11 characters
        String[] words = this.cardName.split(" ");
        StringBuilder currentLine = new StringBuilder(words[0]);
        int yPosition = 360;
        for (int i = 1; i < words.length; i++) {
            if (currentLine.length() + words[i].length() > 10) {
                c.drawText(currentLine.toString(), x+80, y+yPosition, textPaint);
                currentLine = new StringBuilder(words[i]);
                yPosition += 24; // Adjust this value based on the desired line spacing
            } else {
                currentLine.append(" ").append(words[i]);
            }
        }
        c.drawText(currentLine.toString(), x+80, y+yPosition, textPaint);
    }

    /**
     * scaled version of the cards, used for the menus to reduce the size of the cards
     * @param c
     * @param scale
     */
    public void draw(Canvas c ,float scale){
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(0,0,0));
        textPaint.setTextSize(16);
        textPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));

        // Create a new paint for the card with opacity
        textPaint.setAlpha(isFlashing ? (int) opacityAnimator.getAnimatedValue() : 255); // Use the animated value

        Paint mTextPaint = new Paint();
        mTextPaint.setColor(Color.rgb(255,255,255));
        mTextPaint.setTextSize(20);
        mTextPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));

        // Create a new paint for the card with opacity
        mTextPaint.setAlpha(isFlashing ? (int) opacityAnimator.getAnimatedValue() : 255); // Use the animated value


        Bitmap back = Bitmap.createScaledBitmap(CardIcons.cardBackgrounds.getSprite(0,element ),
                (int) (CardIcons.cardBackgrounds.getSprite(0,element ).getWidth() * scale),
                (int) (CardIcons.cardBackgrounds.getSprite(0,element ).getHeight()*scale),false);
        Bitmap boarder = Bitmap.createScaledBitmap(CardIcons.cardBoarders.getSprite(0,cardboarderint),
                (int) (CardIcons.cardBoarders.getSprite(0,cardboarderint).getWidth() * scale),
                (int) (CardIcons.cardBoarders.getSprite(0,cardboarderint).getHeight()*scale),false);
        Bitmap title = Bitmap.createScaledBitmap(CardIcons.cardTitles.getSprite(0,cardboarderint),
                (int) (CardIcons.cardTitles.getSprite(0,cardboarderint).getWidth() * scale),
                (int) (CardIcons.cardTitles.getSprite(0,cardboarderint).getHeight()*scale),false);
        Bitmap image = Bitmap.createScaledBitmap(getImageExt(extension),
                (int) (getImageExt(extension).getWidth() * scale),
                (int) (getImageExt(extension).getHeight()*scale),false);
        Bitmap iconBorder = Bitmap.createScaledBitmap(CardIcons.cardIconBoarder.getSprite(0,cardboarderint ),
                (int) (CardIcons.cardIconBoarder.getSprite(0,cardboarderint ).getWidth() * scale),
                (int) (CardIcons.cardIconBoarder.getSprite(0,cardboarderint ).getHeight()*scale),false);
        Bitmap manaBack = Bitmap.createScaledBitmap(CardIcons.manaBackground.getSprite(0,0),
                (int)(CardIcons.manaBackground.getSprite(0,0).getWidth() * scale),
                (int)(CardIcons.manaBackground.getSprite(0,0).getHeight() * scale),false);
        Bitmap attackBack = Bitmap.createScaledBitmap(CardIcons.attackBackground.getSprite(0,0),
                (int)(CardIcons.attackBackground.getSprite(0,0).getWidth() * scale),
                (int)(CardIcons.attackBackground.getSprite(0,0).getHeight() * scale),false);

        c.drawBitmap(back, x , y,null);//exampl
        c.drawBitmap(boarder, x , y,null);//exampl

        c.drawBitmap(title,x+(60*scale),y+(320*scale),null);
        c.drawBitmap(image,x+100*scale,y+50*scale,null);
        c.drawBitmap(iconBorder,x+90*scale,y+43*scale,null);


        c.drawBitmap(manaBack,x+(280*scale),y+30*scale,textPaint);
        c.drawBitmap(attackBack,x+30*scale,y+30*scale,textPaint);


        c.drawText(String.valueOf(attackPower),x+40*scale,y+90*scale,mTextPaint);
        c.drawText(String.valueOf(manaCost),x+305*scale,y+90*scale,mTextPaint);

        String[] words = this.cardName.split(" ");
        StringBuilder currentLine = new StringBuilder(words[0]);
        int yPosition = (int) (360 *scale);
        for (int i = 1; i < words.length; i++) {
            if (currentLine.length() + words[i].length() > 10) {
                c.drawText(currentLine.toString(), x+80*scale, y+yPosition, textPaint);
                currentLine = new StringBuilder(words[i]);
                yPosition += 34 * scale; // Adjust this value based on the desired line spacing
            } else {
                currentLine.append(" ").append(words[i]);
            }
        }
        c.drawText(currentLine.toString(), x+80*scale, y+yPosition, textPaint);

    }

    /**
     * changes the damage of the card to a new damage
     * @param damage
     */
    public void setDamage(int damage){attackPower = damage;}

    /**
     * returns the attack power of the card
     * @return
     */
    public int getDamage(){return attackPower;}


    /**
     * sets the card to flash when snapped to a battle character
     * @param flashing
     */
    public void setFlashing(boolean flashing) {
        isFlashing = flashing;
        if (flashing) {
            // Start the animation
            opacityAnimator = ValueAnimator.ofInt(128, 255);
            opacityAnimator.setDuration(1000); // Change this to control the speed of the flashing
            opacityAnimator.setRepeatMode(ValueAnimator.REVERSE);
            opacityAnimator.setRepeatCount(ValueAnimator.INFINITE);
            opacityAnimator.start();
        } else {
            // Stop the animation
            if (opacityAnimator != null) {
                opacityAnimator.cancel();
            }
        }
    }


    private Bitmap getImageExt(String extension) {
        switch (extension){
            case "avalanche_elemental.jpg":
                return Icons.AVALANCHELEMENTAL.getSpriteLogo();
            case "beetle.jpg":
                return Icons.BEETLE.getSpriteLogo();
            case "blizzard_owl.jpg":
                return Icons.OWL.getSpriteLogo();
            case "celestial_protector.jpg":
                return Icons.CELESTIALPROTECTOR.getSpriteLogo();
            case "crystal_shaper.jpg":
                return Icons.CRYSTALSHAPER.getSpriteLogo();
            case "cyclone_siren.jpg":
                return Icons.CYCLONESIREN.getSpriteLogo();
            case "Earthquake.jpg":
                return Icons.EARTHQUAKE.getSpriteLogo();
            case "electric_eel.jpg":
                return Icons.ELECTRIC_EEL.getSpriteLogo();
            case "electrostatic_elemental.jpg":
                return Icons.ELECTROSTATICELEMENTAL.getSpriteLogo();
            case "ent.jpg":
                return Icons.ENT.getSpriteLogo();
            case "eternal_grove.jpg":
                return Icons.ETERNALGROVE.getSpriteLogo();
            case "fire_frog.jpg":
                return Icons.FIREFROG.getSpriteLogo();
            case "firefox.jpg":
                return Icons.FIREFOX.getSpriteLogo();
            case "firegolem.jpg":
                return Icons.FIREGOLEM.getSpriteLogo();
            case "firestorm_phoenix.jpg":
                return Icons.FIRESTORM_PHOENIX.getSpriteLogo();
            case "frostbite_yeti.jpg":
                return Icons.YETI.getSpriteLogo();
            case "frostbiteDragon.jpg":
                return Icons.FROSTBITEDRAGON.getSpriteLogo();
            case "garden_serpent.jpg":
                return Icons.GARDENSERPENT.getSpriteLogo();
            case "guardian_unicorn.jpg":
                return Icons.UNICORN.getSpriteLogo();
            case "healing_alchemist.jpg":
                return Icons.HEALINGALC.getSpriteLogo();
            case "holy_cow.jpg":
                return Icons.HOLY_COW.getSpriteLogo();
            case "inferno_drake.jpg":
                return Icons.INFERNO_DRAKE.getSpriteLogo();
            case "lich_king.jpg":
                return Icons.LICHKING.getSpriteLogo();
            case "lightning_bug.jpg":
                return Icons.LIGHTNINGBUG.getSpriteLogo();
            case "lightning_wyvern.jpg":
                return Icons.WYVERN.getSpriteLogo();
            case "magma_golem.jpg":
                return Icons.MAGAMAGOLEM.getSpriteLogo();
            case "Mamoth.jpg":
                return Icons.MAMOTH.getSpriteLogo();
            case "mountain_guardian.jpg":
                return Icons.MOUNTAINGAURDIAN.getSpriteLogo();
            case "necrotic_banshee.jpg":
                return Icons.NECROTICBANSHEE.getSpriteLogo();
            case "penguin.jpg":
                return Icons.PENGUIN.getSpriteLogo();
            case "plague_wraith.jpg":
                return Icons.PLAGUEWRAITH.getSpriteLogo();
            case "polar_bear.jpg":
                return Icons.POLARBEAR.getSpriteLogo();
            case "posnus_decay.jpg":
                return Icons.POSNUS_DECAY.getSpriteLogo();
            case "pyroclasmic_dragon.jpg":
                return Icons.PYRODRAGON.getSpriteLogo();
            case "quake_elemental.jpg":
                return Icons.QUAKE.getSpriteLogo();
            case "rock_golem.jpg":
                return Icons.ROCKGOLEM.getSpriteLogo();
            case "salamander_summoner.jpg":
                return Icons.SALAMANDER.getSpriteLogo();
            case "sandstorm_djinn.jpg":
                return Icons.SANDSTORMDIJIN.getSpriteLogo();
            case "skeleton.jpg":
                return Icons.SKELETON.getSpriteLogo();
            case "skyward_griffin.jpg":
                return Icons.SKYWARDGRIFFIN.getSpriteLogo();
            case "soul_reaper.jpg":
                return Icons.SOULREAPER.getSpriteLogo();
            case "stone_goliath.jpg":
                return Icons.STONEGOLIATH.getSpriteLogo();
            case "tempest_serpent.jpg":
                return Icons.SERPENT.getSpriteLogo();
            case "terraform_gnome.jpg":
                return Icons.TERAFORMGNOME.getSpriteLogo();
            case "Thunderbolt.jpg":
                return Icons.THUNDERBOLT.getSpriteLogo();
            case "touch_of_life.jpg":
                return Icons.TOUCHOFLIFE.getSpriteLogo();
            case "vampire_bat.jpg":
                return Icons.VAPAMPIREBAT.getSpriteLogo();
            case "wraith_assassin.jpg":
                return Icons.WRAITHASSASSIN.getSpriteLogo();
            default:
                return Icons.FIREFOX.getSpriteLogo();
        }
    }



    /**
     * checks when clicked
     * @param action
     */
    public void setClicked(boolean action) {
        this.clicked  = action;
    }

    /**
     * gets the bool if clicked
     * @return
     */
    public boolean getClicked() {
        return clicked;
    }

    /**
     * sets the card position even when moving
     * @param x
     * @param y
     */

    public void setPosition(float x, float y) {
        this.x = x-150;
        this.y = y-250;
    }

    /**
     * sets the scaled position of the card
     * @param x
     * @param y
     * @param scale
     */
    public void setPosition(float x, float y,float scale) {
        this.x = x-150*scale;
        this.y = y-250*scale;
    }

    /**
     * checks the boundry box if clicked on
     * @param touchX
     * @param touchY
     * @return
     */
    public boolean isTouched(float touchX, float touchY) {
        boolean xInside =  touchX > this.x && touchX < this.x+300;
        boolean yInside =  touchY > this.y && touchY < this.y+500;
        return xInside && yInside;
    }

    /**
     * returns the to the snap position
     */
    public void returnToPos() {
        this.x = xSnap;
        this.y = ySnap;
    }
}
