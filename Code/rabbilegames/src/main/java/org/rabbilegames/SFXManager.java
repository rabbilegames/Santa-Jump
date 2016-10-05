package org.rabbilegames;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.debug.Debug;


public class SFXManager extends Object {
    // ====================================================
    // CONSTANTS
    // ====================================================
    private static final SFXManager INSTANCE = new SFXManager();
    GenericPool<SoundCommand> soundCommandPool;

    LinkedBlockingQueue<SoundCommand> soundCommands;
    Semaphore soundCommandsAvailable;

    public enum SoundOperation {
        Start,
        Stop,
    }

    public class SoundCommand {
        public String SoundFileName;
        public SoundOperation SoundOperation;
        public float Volume;
    }

    HashMap<String, Sound> sounds;

    public static SFXManager Get() {
        return INSTANCE;
    }

    public boolean mSoundsMuted;
    public boolean mMusicMuted;

    public SFXManager() {
        soundCommands = new LinkedBlockingQueue<SoundCommand>(10);
        soundCommandsAvailable = new Semaphore(0);
        sounds = new HashMap<String, Sound>();
        soundCommandPool = new GenericPool<SFXManager.SoundCommand>() {
            @Override
            protected SoundCommand onAllocatePoolItem() {
                return new SoundCommand();
            }

        };
        CreateSoundThread();
    }

    public void Load(String assetBasePath, String... soundFileNames) {
        SoundFactory.setAssetBasePath(assetBasePath);
        for (String soundFileName : soundFileNames) {
            sounds.put(soundFileName, loadSound(soundFileName));
        }
    }

    public static Sound loadSound(String resoucePath) {
        return loadSound(resoucePath, 0, 1);
    }

    public static Sound loadSound(String resoucePath, int loopCount, float volume) {
        Sound sound = null;
        try {
            sound = SoundFactory.createSoundFromAsset(ResourceManager.Get().activity
                            .getSoundManager(),
                    ResourceManager.Get().activity, resoucePath);
            sound.setLoopCount(loopCount);
            sound.setVolume(volume);
        } catch (final IOException e) {
            Debug.e(e);
        }
        return sound;
    }

    private static void setVolumeForAllSounds(final float pVolume) {
        //		GameSceneBackgroundMusic.setVolume(pVolume * 0.5f);
        //		MenuSceneBackgroundMusic.setVolume(pVolume * 0.5f);
        //CoinPickupSound.setVolume(pVolume);
    }

    public boolean IsSoundMuted() {
        return mSoundsMuted;
    }

    public void SetIsSoundMuted(final boolean pMuted) {
        mSoundsMuted = pMuted;
        setVolumeForAllSounds(mSoundsMuted ? 0f : 1f);
        //MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_SOUNDS_MUTED, (get().mSoundsMuted ? 1 : 0));
    }

    public boolean isMusicMuted() {
        return mMusicMuted;
    }

    public void Play(String soundFileName, final float volume) {
        if (!IsSoundMuted()) {
            SoundCommand soundCommand = soundCommandPool.obtainPoolItem();
            soundCommand.SoundFileName = soundFileName;
            soundCommand.SoundOperation = SoundOperation.Start;
            soundCommand.Volume = volume;
            soundCommands.add(soundCommand);

            soundCommandsAvailable.release();
        }
    }

    public void Stop(String soundFileName) {
        if (!IsSoundMuted()) {
            SoundCommand soundCommand = soundCommandPool.obtainPoolItem();
            soundCommand.SoundFileName = soundFileName;
            soundCommand.SoundOperation = SoundOperation.Stop;
            soundCommands.add(soundCommand);

            soundCommandsAvailable.release();
        }
    }

    void CreateSoundThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        soundCommandsAvailable.acquire();
                        SoundCommand soundCommand = soundCommands.poll();
                        switch (soundCommand.SoundOperation) {
                            case Start:
                                Sound sound = sounds.get(soundCommand.SoundFileName);
                                sound.setVolume(soundCommand.Volume);
                                sound.play();
                                break;
                            case Stop:
                                sounds.get(soundCommand.SoundFileName).stop();
                                break;
                        }
                        soundCommandPool.recyclePoolItem(soundCommand);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
