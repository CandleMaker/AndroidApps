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
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    //Bundles- lets you pass information between multiple activities.
    //Going back to the example of a login screen, a Bundle might
    //transmit your login information to another activity
    //that checks it to grant access
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
               

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth = isPortrait ? 800: 1280;
        int frameBufferHeight = isPortrait ? 1280: 800;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float scaleX = (float) frameBufferWidth / metrics.widthPixels;
        float scaleY = (float) frameBufferHeight / metrics.heightPixels;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);
        
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
    public void onPause() {
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
