package com.mj.framework.implementation;

import android.app.Activity;
//import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
//import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.util.DisplayMetrics;

import com.mj.framework.Audio;
import com.mj.framework.FileIO;
import com.mj.framework.Game;
import com.mj.framework.Graphics;
import com.mj.framework.Input;
import com.mj.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
//an activity is usually an interactive window. For a typical application,
//you might have multiple activities (usually one for each screen).
//For example, you might have an activity for the login screen,
//another activity for the settings page, and so on.
    AndroidFastRenderView renderView; //usermade class
    Graphics graphics; //usermade class
    Audio audio; //usermade class
    Input input; //usermade class
    FileIO fileIO; //usermade class
    Screen screen; //usermade class
    WakeLock wakeLock; //import android.os.PowerManager.WakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    //Bundles- lets you pass information between multiple activities.
    //Going back to the example of a login screen, a Bundle might
    //transmit your login information to another activity
    //that checks it to grant access
        super.onCreate(savedInstanceState);
        //we first call the "super" method -->that is the default onCreate method in the activity class
        //method belongs to the superclass?

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //removes the title of our application
        
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*///deprecated
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //alternative to deprecated code
        //sets application to fullscreen
               

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //code for checking the orientation
        
        int frameBufferWidth = isPortrait ? 800: 1280;
        //sets value according to orientation
        int frameBufferHeight = isPortrait ? 1280: 800;
        //sets value according to orientation
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        //import android.graphics.Bitmap;
        //Returns a mutable bitmap with the specified width and height
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //A structure describing general information about a display, such as its size, density, and font scaling
        //This is the code needed to access the displaymetrics members (initialize an object like this)
        //members sample-->metrics.widthPixels, metrics.heightPixels

        float scaleX = (float) frameBufferWidth / metrics.widthPixels;
        float scaleY = (float) frameBufferHeight / metrics.heightPixels;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        //object created by user created class
        
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        //object created by user created class
        fileIO = new AndroidFileIO(this);
        //object created by user created class
        audio = new AndroidAudio(this);
        //object created by user created class
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        //object created by user created class
        screen = getInitScreen();
        //object created by user created class
        //getInitScreen() has no code?
        setContentView(renderView);
        //Activity class takes care of creating a window for you in which you can place your UI
        //with setContentView(View)
        
        
        //PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {//
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {

        return screen;
    }
}
