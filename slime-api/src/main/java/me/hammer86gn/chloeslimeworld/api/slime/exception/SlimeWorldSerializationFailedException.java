package me.hammer86gn.chloeslimeworld.api.slime.exception;

public class SlimeWorldSerializationFailedException extends Exception {

    public SlimeWorldSerializationFailedException(String name, String message) {
        super("The SlimeWorld with name: %s, failed to save! %s".formatted(name, message));
    }

    public SlimeWorldSerializationFailedException(String name) {
        super("The SlimeWorld with name: %s, failed to save!".formatted(name));
    }

}
