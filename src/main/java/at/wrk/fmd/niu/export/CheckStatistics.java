package at.wrk.fmd.niu.export;

public class CheckStatistics {
    private final int validNumbers;
    private final int invalidNumbers;
    private final String info;

    public CheckStatistics(final int validNumbers, final int invalidNumbers, final String info) {
        this.validNumbers = validNumbers;
        this.invalidNumbers = invalidNumbers;
        this.info = info;
    }

    public int getValidNumbers() {
        return validNumbers;
    }

    public int getInvalidNumbers() {
        return invalidNumbers;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "CheckStatistics{" +
                "validNumbers=" + validNumbers +
                ", invalidNumbers=" + invalidNumbers +
                ", info='" + info + '\'' +
                '}';
    }
}
