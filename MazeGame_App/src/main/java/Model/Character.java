package Model;


import View.ResourceManager;
import algorithms.mazeGenerators.Position;

import java.io.Serializable;

public class Character implements Serializable {

    public enum CHARACTER_TYPE { UNSELECTED, GUY, MEIR, ARIEL, ELIA, KOBE, YUVAL }
    public enum CHARACTER_MOVEMENT { IDLE        // -1
                                    , UP         // 0
                                    , DOWN       // 1
                                    , RIGHT      // 2
                                    , LEFT       // 3
                                    , RIGHT_UP   // 4
                                    , RIGHT_DOWN // 5
                                    , LEFT_DOWN  // 6
                                    , LEFT_UP}   // 7

    private static final int CHARACTER_SPEED = 3;

    private int currentAnimation;
    private int currentFrame;

    private int animationTimer;

    private CollisionMaze maze;
    private double characterX;
    private double characterY;
    private Position mazeTile;
    private CHARACTER_MOVEMENT currentMovement;
    private CHARACTER_TYPE type;

    public Character(CHARACTER_TYPE type, Position mazeStart, CollisionMaze maze) {
        this.maze = maze;
        this.currentAnimation = 0;
        this.currentFrame = 0;
        animationTimer = 0;
        this.mazeTile = mazeStart;
        this.characterX = mazeTile.getColumnIndex() * 32 + 16;
        this.characterY = mazeTile.getRowIndex() * 32 + 16;
        this.currentMovement = CHARACTER_MOVEMENT.IDLE;
        this.type = type;
    }

    public synchronized void setMovement(CHARACTER_MOVEMENT movement) {
        this.currentMovement = movement;
        int newAnimation = -1;
        switch (movement) {
            case IDLE:
                currentFrame = 0;
                newAnimation = currentAnimation;
                break;
            case UP:
            case LEFT_UP:
            case RIGHT_UP:
                newAnimation = ResourceManager.CHARACTER_ANIMATION_UP;
                break;
            case DOWN:
            case LEFT_DOWN:
            case RIGHT_DOWN:
                newAnimation = ResourceManager.CHARACTER_ANIMATION_DOWN;
                break;
            case LEFT:
                newAnimation = ResourceManager.CHARACTER_ANIMATION_LEFT;
                break;
            case RIGHT:
                newAnimation = ResourceManager.CHARACTER_ANIMATION_RIGHT;
                break;
        }
        if (newAnimation != currentAnimation) {
            currentAnimation = newAnimation;
            currentFrame = 0;
        }
    }

    public synchronized void update(int timer) {
            if (!checkCollision()) updateMove();
            if (timer - animationTimer > 5) {
                //update animation.
                if (this. currentMovement != CHARACTER_MOVEMENT.IDLE) this.currentFrame = (this.currentFrame + 1) % 4;
                animationTimer = timer;
        }
    }

    private boolean checkCollision() {
        boolean isColliding = false;
        switch (this.currentMovement) {
            case IDLE: return false;
            case UP: return checkBounds(this.characterX, this.characterY - CHARACTER_SPEED);
            case DOWN: return checkBounds(this.characterX, this.characterY + CHARACTER_SPEED);
            case RIGHT: return checkBounds(this.characterX + CHARACTER_SPEED, this.characterY);
            case LEFT: return checkBounds(this.characterX - CHARACTER_SPEED, this.characterY);
            case RIGHT_UP: return checkBounds(this.characterX + (CHARACTER_SPEED / 2), this.characterY - CHARACTER_SPEED);
            case LEFT_UP: return checkBounds(this.characterX - (CHARACTER_SPEED / 2), this.characterY - CHARACTER_SPEED);
            case RIGHT_DOWN: return checkBounds(this.characterX + (CHARACTER_SPEED / 2), this.characterY + CHARACTER_SPEED);
            case LEFT_DOWN: return checkBounds(this.characterX - (CHARACTER_SPEED / 2), this.characterY + CHARACTER_SPEED);
        }

        return isColliding;
    }

    private boolean checkBounds(double newX, double newY) {
        return this.maze.isColliding(newX, newY) //center of the character.
                || this.maze.isColliding(newX + 8, newY) //upper right corner.
                || this.maze.isColliding(newX - 8, newY) // upper left corner.
                || this.maze.isColliding(newX + 8, newY + 15) //bottom right corner.
                || this.maze.isColliding(newX - 8, newY + 15); //bottom left corner.

    }

    private void updateMove() {
        switch (this.currentMovement) {
            case IDLE:
                return;
            case DOWN:
                this.characterY += CHARACTER_SPEED;
                break;
            case UP:
                this.characterY -= CHARACTER_SPEED;
                break;
            case LEFT:
                this.characterX -= CHARACTER_SPEED;
                break;
            case RIGHT:
                this.characterX += CHARACTER_SPEED;
                break;
            case RIGHT_UP:
                this.characterX += CHARACTER_SPEED / 2;
                this.characterY -= CHARACTER_SPEED;
                break;
            case RIGHT_DOWN:
                this.characterX += CHARACTER_SPEED / 2;
                this.characterY += CHARACTER_SPEED;
                break;
            case LEFT_DOWN:
                this.characterX -= CHARACTER_SPEED / 2;
                this.characterY += CHARACTER_SPEED;
                break;
            case LEFT_UP:
                this.characterX -= CHARACTER_SPEED / 2;
                this.characterY -= CHARACTER_SPEED;
                break;
        }
        this.mazeTile = new Position((int)(this.characterY / 32), (int)(this.characterX / 32));
    }

    public int getFrame() { return this.currentAnimation * 4 + this.currentFrame; }

    public double getCharacterX() { return this.characterX;}
    public double getCharacterY() { return this.characterY; }
    public Position getMazeTile() { return this.mazeTile; }
    public CHARACTER_TYPE getType() { return type; }

}
