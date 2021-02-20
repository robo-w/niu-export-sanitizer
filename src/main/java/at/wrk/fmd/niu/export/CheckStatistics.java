package at.wrk.fmd.niu.export;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

public class CheckStatistics {
    private final int validNumbers;
    private final int invalidNumbers;
    private final String info;
    private final String firstValidNumber;

    public CheckStatistics(final int validNumbers, final int invalidNumbers, final String info, final String firstValidNumber) {
        this.validNumbers = validNumbers;
        this.invalidNumbers = invalidNumbers;
        this.info = info;
        this.firstValidNumber = firstValidNumber;
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

    public Optional<String> getFirstValidNumber() {
        return Optional.ofNullable(firstValidNumber);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("validNumbers", validNumbers)
                .append("invalidNumbers", invalidNumbers)
                .append("info", info)
                .append("firstValidNumber", firstValidNumber)
                .toString();
    }
}
