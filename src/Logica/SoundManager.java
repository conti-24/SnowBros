package Logica;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    // Instancia única del Singleton
    private static SoundManager instance;

    // Mapa para almacenar los clips de sonido cargados
    private Map<String, Clip> soundClips;

    private float masterVolume = 0.7f; // Volumen maestro (0.0f - 1.0f)
    private float musicVolume = 0.5f;  // Volumen de música
    private float sfxVolume = 0.8f;    // Volumen de efectos de sonido

    private Clip backgroundMusic;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;

    private SoundManager() {
        soundClips = new HashMap<>();
        initializeAudioSystem();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            synchronized (SoundManager.class) {
                if (instance == null) {
                    instance = new SoundManager();
                }
            }
        }
        return instance;
    }

    private void initializeAudioSystem() {
        try {
            AudioSystem.getMixer(null);
        } catch (Exception e) {
            System.err.println("Error inicializando sistema de audio: " + e.getMessage());
            musicEnabled = false;
            sfxEnabled = false;
        }
    }

    public void loadSound(String soundName, String resourcePath) {
        if (!sfxEnabled) return;

        try {
            URL soundURL = getClass().getResource(resourcePath);
            if (soundURL == null) {
                System.err.println("No se encontró el archivo de sonido: " + resourcePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            setClipVolume(clip, sfxVolume);

            soundClips.put(soundName, clip);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error cargando sonido " + soundName + ": " + e.getMessage());
        }
    }
    public void playSound(String soundName) {
        if (!sfxEnabled) return;

        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sonido no encontrado: " + soundName);
        }
    }
    public void playSoundLoop(String soundName) {
        if (!sfxEnabled) return;

        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Sonido no encontrado: " + soundName);
        }
    }

    public void stopSound(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.stop();
        }
    }

    public void playBackgroundMusic(String musicPath) {
        if (!musicEnabled) return;

        try {
            if (backgroundMusic != null) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }

            URL musicURL = getClass().getResource(musicPath);
            if (musicURL == null) {
                System.err.println("No se encontró el archivo de música: " + musicPath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicURL);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInput);
            setClipVolume(backgroundMusic, musicVolume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error reproduciendo música: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }

    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
        updateAllVolumes();
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            setClipVolume(backgroundMusic, musicVolume * masterVolume);
        }
    }

    public void setSFXVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
        updateAllVolumes();
    }

    private void updateAllVolumes() {
        for (Clip clip : soundClips.values()) {
            setClipVolume(clip, sfxVolume * masterVolume);
        }
        if (backgroundMusic != null) {
            setClipVolume(backgroundMusic, musicVolume * masterVolume);
        }
    }

    private void setClipVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume == 0.0f ? 0.0001f : volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(Math.max(dB, gainControl.getMinimum()));
            } catch (Exception e) {
                // Algunos clips pueden no tener control de volumen
            }
        }
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }

    public void setSFXEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }

    public void stopAllSounds() {
        for (Clip clip : soundClips.values()) {
            clip.stop();
        }
        stopBackgroundMusic();
    }

    public void cleanup() {
        stopAllSounds();

        for (Clip clip : soundClips.values()) {
            clip.close();
        }
        soundClips.clear();

        if (backgroundMusic != null) {
            backgroundMusic.close();
            backgroundMusic = null;
        }
    }

    public float getMasterVolume() { return masterVolume; }
    public float getMusicVolume() { return musicVolume; }
    public float getSFXVolume() { return sfxVolume; }
    public boolean isMusicEnabled() { return musicEnabled; }
    public boolean isSFXEnabled() { return sfxEnabled; }
}