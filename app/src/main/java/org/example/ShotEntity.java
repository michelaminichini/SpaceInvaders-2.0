package org.example;

/**
 * An entity representing a shot fired by the player's ship
 */
public class ShotEntity extends Entity {
    // The player's shot vertical speed
    public double moveSpeed = -300;
    // The game in which this entity exists
    public Game game;
    // True if this shot has been "used", for example if it hit something
    public boolean used = false;

    /**
     * Create a new shot from the player
     *
     * @param game   The game in which the shot has been created
     * @param sprite The sprite (image) representing this shot.
     * @param x      The initial x location of the shot
     * @param y      The initial y location of the shot
     */
    public ShotEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);

        this.game = game;

        dy = moveSpeed;
    }

    /**
     * Request that this shot moved based on time elapsed
     *
     * @param delta The time that has elapsed since last move
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);

        // if we shot off the screen, remove ourselves
        if (y < -100) {
            game.removeEntity(this);
        }
    }

    /**
     * Notification that this shot has collided with another entity
     *
     * @param other The other entity with which we've collided
     */
    public void collidedWith(Entity other) {
        // prevent double kills, if we already hit something, don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it.
        if (other instanceof AlienEntity) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }
    }
}