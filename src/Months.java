public enum Months {
    JANUARY("Jan", 1, 31),
    FEBRUARY("Feb", 32, 59),
    MARCH("Mar", 60, 90),
    APRIL("Apr", 91, 120),
    MAY("May", 121, 151),
    JUNE("Jun", 152, 181),
    JULY("Jul", 182, 212),
    AUGUST("Aug", 213, 243),
    SEPTEMBER("Sep", 244, 273),
    OCTOMBER("Oct", 274, 304),
    NOVEMBER("Nov", 305, 334),
    DECEMBER("Dec", 335, 365);

    private final String label;
    private final int startDay;
    private final int endDay;

    Months(String label, int startDay, int endDay) {
        this.label = label;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public String getLabel() {
        return this.label;
    }

    public int getStartDay() {
        return this.startDay;
    }

    public int getEndDay() {
        return this.endDay;
    }
    
     public static Months fromLabel(String label) {
        if (label == null || label.length() != 3) {
            throw new IllegalArgumentException("Label must be 3 characters");
        }
        
        String normalizedLabel = label.substring(0, 3).toLowerCase();
        
        for (Months month : values()) {
            if (month.label.toLowerCase().equals(normalizedLabel)) {
                return month;
            }
        }
        throw new IllegalArgumentException("No month found for label: " + label);
    }
}