import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Objects;

public class HotelConfig {
    private int id;
    private final String hashStructure;
    private final File structure;
    private final ArrayList<ArrayList<Integer>> roomDays;

    public HotelConfig(File structure, ArrayList<ArrayList<Integer>> roomDays) throws IllegalArgumentException {
        this.structure = Objects.requireNonNull(structure, "Structure file cannot be null");
        this.roomDays = Objects.requireNonNull(roomDays, "Room days data cannot be null");
        
        if (!structure.exists() || !structure.isFile()) {
            throw new IllegalArgumentException("Structure file must be a valid file");
        }

        try {
            this.hashStructure = generateHash();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to generate hash for structure file", e);
        }
    }

    private String generateHash() throws IOException {
        try (InputStream is = Files.newInputStream(this.structure.toPath())) {
            // Read entire file into a string
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            // Normalize the content (remove whitespace/comments)
            content = content.replaceAll("\\s+", "");  // Remove all whitespace
            content = content.replaceAll("#.*$", "");  // Remove comments (if using #)
            
            // Hash the normalized content
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
                throw new IOException("Failed to generate hash for file: " + structure.getAbsolutePath(), e);
            }
    }

    // Getters
    public String getHashStructure() {
        return this.hashStructure;
    }

    public File getStructure() {
        return this.structure;
    }

    public ArrayList<ArrayList<Integer>> getRoomDays() {
        return new ArrayList<>(this.roomDays); // Return defensive copy
    }

    // Equality based on hash
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelConfig that = (HotelConfig) o;
        return hashStructure.equals(that.hashStructure);
    }

    @Override
    public int hashCode() {
        return hashStructure.hashCode();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}