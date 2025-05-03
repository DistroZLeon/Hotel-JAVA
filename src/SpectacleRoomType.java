public enum SpectacleRoomType {
    INDIVIDUAL("Individual"),
    SCENE("Scene"),
    NONE("None");
    private final String label;
    private SpectacleRoomType(String label){
        this.label= label;
    }  
    public String getLabel(){
        return this.label;
    }
}
