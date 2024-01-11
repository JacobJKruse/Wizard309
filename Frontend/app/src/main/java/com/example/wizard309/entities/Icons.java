package com.example.wizard309.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.main.MainActivity;
import com.example.wizard309.R;

/**
 * static images from card images, and ui elements
 */
public enum Icons {

    LOGO(R.drawable.sprite_logo,3),
    SHADOW(R.drawable.shadow,12),
    JOYSTICKINNER(R.drawable.joystick_inner, 15),
    JOYSTICKOUTTER(R.drawable.joystick_outer, 15),
    HPTEXTICON(R.drawable.hp_text_icon,9),
    HPBAR(R.drawable.hp_bar,9),
    MPTEXTICON(R.drawable.mp_text_icon,9),
    MPBAR(R.drawable.mp_bar,9),
    FIREICON(R.drawable.fire_element,3),
    ICEICON(R.drawable.ice_element,3),
    STORMICON(R.drawable.storm_element,3),
    LIFEICON(R.drawable.life_element,3),
    DEATHICON(R.drawable.death_element,3),
    EARTHICON(R.drawable.earth_element,3),

    CARDHOLDER(R.drawable.cardholder,26),

    SHOPKEEPER(R.drawable.shop_keeper,MainActivity.screenWidth/200),
    MENUBACK(R.drawable.menuback,MainActivity.screenWidth/150),
    MENUWIZARDFRAME(R.drawable.menu_wizard_frame,MainActivity.screenWidth/150),
    MENUNAMEBACK(R.drawable.menu_name_back,MainActivity.screenWidth/150),
    MENUSTATBACK(R.drawable.menu_stat_back,MainActivity.screenWidth/150),
    MENUBUTTONTEXT(R.drawable.menu_deck_button_text,MainActivity.screenWidth/150),
    MENUEXPBACK(R.drawable.menu_exp_back,MainActivity.screenWidth/150),

    TURNBACKGROUND(R.drawable.sprite_charbackground,MainActivity.screenWidth/450),
    TURNLOGO(R.drawable.sprite_turnlogo,MainActivity.screenWidth/450),

    DIALOGBOX(R.drawable.dialogbox,MainActivity.screenWidth/150),
    //BUTTON(R.drawable.buttonIcon); example of adding another sprite asset to the game

    EARTHQUAKE(R.drawable.cardasset_earthquake,.2),
    ENT(R.drawable.cardasset_ent,.2),
    FIREFOX(R.drawable.cardasset_firefox,.2),
    FIREGOLEM(R.drawable.cardasset_firegolem,.2),
    LIGHTNINGBUG(R.drawable.cardasset_lightningbug,.2),
    MAMOTH(R.drawable.cardasset_mamoth,.2),
    PENGUIN(R.drawable.cardasset_penguin,.2),
    POLARBEAR(R.drawable.cardasset_polarbear,.2),
    POISON(R.drawable.cardasset_posin,.2),
    ROCKGOLEM(R.drawable.cardasset_rockgolem,.2),
    THUNDERBOLT(R.drawable.cardasset_thunderbolt,.2),
    TOUCHOFLIFE(R.drawable.cardasset_touchoflife,.2),
    VAPAMPIREBAT(R.drawable.cardasset_vampirebat,.2),
    FIREFROG(R.drawable.cardasset_firefrog,.2),
    ELECTRIC_EEL(R.drawable.cardasset_electric_eel,.2),
    BEETLE(R.drawable.cardasset_beetle,.2),
    
    HOLY_COW(R.drawable.cardasset_holy_cow,.2),
    POSNUS_DECAY(R.drawable.cardasset_posin,.2),
    SKELETON(R.drawable.cardasset_skeleton,.2),
    INFERNO_DRAKE(R.drawable.cardasset_inferno_drake,.2),
    FIRESTORM_PHOENIX(R.drawable.cardasset_firestore_phinox,.2),
    YETI(R.drawable.cardasset_yeti,.2),
    SERPENT(R.drawable.cardasset_serpent,.2),
    QUAKE(R.drawable.cardasset_quake,.2),
    OWL(R.drawable.cardasset_owl,.2),

    WYVERN(R.drawable.cardasset_wyvern,.2),

    STONEGOLIATH(R.drawable.cardasset_stone_goliath,.2),
    ETERNALGROVE(R.drawable.cardasset_eternal_grove,.2),
    UNICORN(R.drawable.cardasset_gaurdian_unicorn,.2),
    PLAGUEWRAITH(R.drawable.cardasset_plauge_wraith,.2),
    SOULREAPER(R.drawable.cardasset_soul_reaper,.2),
    SALAMANDER(R.drawable.cardasset_salamander,.2),
    MAGAMAGOLEM(R.drawable.cardasset_magma_golem,.2),
    CRYSTALSHAPER(R.drawable.cardasset_crystral_shaper,.2),
    AVALANCHELEMENTAL(R.drawable.cardasset_avalanch_elemental,.2),
    FROSTBITEDRAGON(R.drawable.cardasset_frostbite_dragon,.2),
    CYCLONESIREN(R.drawable.cardasset_cyclone_siren,.2),
    ELECTROSTATICELEMENTAL(R.drawable.cardasset_electrostatic_elemental,.2),
    SKYWARDGRIFFIN(R.drawable.cardasset_skyward_griffin,.2),
    SANDSTORMDIJIN(R.drawable.cardasset_sandstorm_djinn,.2),
    TERAFORMGNOME(R.drawable.cardasset_terraform_gnome,.2),
    MOUNTAINGAURDIAN(R.drawable.cardasset_mountain_guardian,.2),
    HEALINGALC(R.drawable.cardasset_healing_alchemist,.2),
    GARDENSERPENT(R.drawable.cardasset_garden_serpent,.2),
    PYRODRAGON(R.drawable.cardasset_pyrodragon,.2),
    CELESTIALPROTECTOR(R.drawable.cardasset_celestial_protector,.2),
    WRAITHASSASSIN(R.drawable.cardasset_wraith_assassin,.2),
    NECROTICBANSHEE(R.drawable.cardasset_necrotic_banshee,.2),
    LICHKING(R.drawable.cardasset_lich_king,.2);







    private Bitmap spriteLogo;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    /**
     * Icons constructor place the image file and the scale of the image
     * @param ID
     * @param scale
     */
    Icons(int ID,double scale){
        options.inScaled = false;
        spriteLogo = getScale(BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),ID,options),scale);
    }

    /**
     *
     * @return sprite of icon
     */
    public Bitmap getSpriteLogo(){
        return spriteLogo;
    }

    /**
     * return the width of the bitmap icon
     * @return
     */
    public int getBitmapWidth() {
        return spriteLogo.getWidth();
    }

    /**
     * returns the width of the bitmap
     * @return
     */
    public int getBitmapHeight() {
        return spriteLogo.getHeight();
    }


    /**
     *
     * @param bitmap
     * @param scale
     * @return scaled icon
     */
    private Bitmap getScale(Bitmap bitmap,double scale){
        return Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * scale), (int)(bitmap.getHeight() * scale), false);//temp hardcoded scale
    }
}
