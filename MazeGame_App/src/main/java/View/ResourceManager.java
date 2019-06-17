package View;

import javafx.scene.image.Image;

import java.io.*;

import static Model.GamePersistenceManager.LOG;


import Model.Character.CHARACTER_TYPE;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

public class ResourceManager {
    private static ResourceManager instance = new ResourceManager();

    public static ResourceManager getInstance() {
        return instance;
    }

    public static final int WELCOME_CLIP    = 0,
                            BACKGROUND_CLIP = 1,
                            FINISH_CLIP     = 2;

    public static final int CHARACTER_ANIMATION_DOWN    = 0,
                            CHARACTER_ANIMATION_UP      = 1,
                            CHARACTER_ANIMATION_RIGHT   = 2,
                            CHARACTER_ANIMATION_LEFT    = 3;

    private final Image icon;

    private String helpText;
    private String aboutText;

    private Image[] characterAnimations;
    private Image[] tiles;
    private Media[] music;

    private ResourceManager() {
        icon = new Image("/images/icon.png");
        music = new Media[3];
        loadTiles();
    }

    public Image getIcon() { return this.icon; }

    public String getHelpText() {
        if (helpText == null) helpText = readTextFile("Help.txt");
        return helpText;
    }

    public String getAboutText() {
        if (aboutText == null) aboutText = readTextFile("About.txt");
        return aboutText;
    }

    public void loadCharacter(CHARACTER_TYPE type) {
        characterAnimations = new Image[4 * 4];

        int yOffset = 0;
        if (type == CHARACTER_TYPE.MEIR) yOffset = 32;
        if (type == CHARACTER_TYPE.ARIEL) yOffset = 32 * 2;
        if (type == CHARACTER_TYPE.ELIA) yOffset = 32 * 3;
        if (type == CHARACTER_TYPE.KOBE) yOffset = 32 * 4;
        if (type == CHARACTER_TYPE.YUVAL) yOffset = 32 * 5;

        Image spriteSheet = new Image("/images/SpriteSheet.png");
        PixelReader pixelReader = spriteSheet.getPixelReader();
        PixelWriter pixelWriter = null;
        for (int animation = 0; animation < 4; animation++) {

            for (int frame = 0; frame < 4; frame++) {
                WritableImage writableImage = new WritableImage(32, 32);
                pixelWriter = writableImage.getPixelWriter();
                int xOffset = (animation * 32 * 4) + 32 * frame;
                for (int y = 0; y < 32; y++) {
                    for (int x = 0; x < 32; x++) {
                        Color color = pixelReader.getColor(x + xOffset, y + yOffset);
                        pixelWriter.setColor(x, y, color);
                    }
                }
                this.characterAnimations[animation * 4 + frame] = writableImage;
            }
        }

    }

    public Image getCharacterFrame(int frame) { return characterAnimations[frame]; }

    public Image getTile(int tile) { return this.tiles[tile];}

    public Media getClip(int clip) {
        if (music[clip] == null) loadClip(clip);
        return music[clip];
    }

    private void loadClip(int clip) {
        String clipName = "";
        switch (clip) {
            case WELCOME_CLIP:
                clipName = "welcome.wav";
                break;
            case BACKGROUND_CLIP:
                clipName = "background.wav";
                break;
            case FINISH_CLIP:
                clipName = "finish.wav";
                break;
        }

        music[clip] = new Media(ClassLoader.getSystemResource("music/" + clipName).toString());
    }

    private String readTextFile(String path) {
        try {
            StringBuffer fileData = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/docs/" + path)));
            char[] buf = new char[1024];
            int numRead = 0;
            while((numRead = reader.read(buf)) != -1){
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            return fileData.toString();

        } catch (IOException e) {
            LOG.error("Error reading file: \n", e);
        }
        return null;
    }

    private void loadTiles() {
        tiles = new Image[5];
        String basePath = "/images";
        tiles[DisplayableMaze.TILE_WALL] = new Image(basePath + "/wall.png");
        tiles[DisplayableMaze.TILE_FLOOR] = new Image(basePath + "/floor.png");
        tiles[DisplayableMaze.TILE_START] = new Image(basePath + "/start.png");
        tiles[DisplayableMaze.TILE_END] = new Image(basePath + "/finish.png");
        tiles[DisplayableMaze.TILE_SOLUTION] = new Image(basePath + "/solution.png");
    }
}
