package traingame.engine;

public record SoftwareVersion (int major, int minor, int patch) {
    public String toString() {
        return "" + major + "." + minor + "." + patch;
    }
}

