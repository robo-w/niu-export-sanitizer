package at.wrk.fmd.niu.export;

import at.wrk.coceso.alarm.text.configuration.AlarmTextConfiguration;
import at.wrk.coceso.alarm.text.service.normalizer.PhoneNumberNormalizer;
import at.wrk.coceso.niu.data.ExternalUser;
import at.wrk.coceso.niu.data.ExternalUserId;
import at.wrk.coceso.niu.parser.NiuExternalUserParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RunChecks {

    private static PhoneNumberNormalizer phoneNumberNormalizer;

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java RunChecks path/to/user-data.csv");
            throw new RuntimeException("No user data CSV file provided.");
        }

        AlarmTextConfiguration alarmTextConfiguration = new AlarmTextConfiguration(
                null,
                null,
                null,
                "+436",
                "+43",
                null
        );

        phoneNumberNormalizer = new PhoneNumberNormalizer(alarmTextConfiguration);
        NiuExternalUserParser externalUserParser = new NiuExternalUserParser();

        FileInputStream inputStream = new FileInputStream(args[0]);
        List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);

        String joinedLines = StringUtils.join(lines, "\n");
        Collection<ExternalUser> externalUsers = externalUserParser.parseExternalUsers(joinedLines);

        List<CheckStatistics> statistics = externalUsers
                .stream()
                .map(RunChecks::validate)
                .collect(Collectors.toList());

        printStatistics(statistics);

        printJsonOfEachFirstValidNumber(statistics);
    }

    private static void printJsonOfEachFirstValidNumber(final List<CheckStatistics> statistics) {
        System.out.println("JSON containing first valid number of each user: \n[");
        String joinedNumbers = statistics
                .stream()
                .filter(item -> item.getFirstValidNumber().isPresent())
                .map(item -> "  \"" + item.getFirstValidNumber().get() + "\"")
                .collect(Collectors.joining(",\n"));
        System.out.println(joinedNumbers);
        System.out.println("]");
    }

    private static void printStatistics(final List<CheckStatistics> statistics) {
        System.out.println();
        System.out.println();
        System.out.println("*** Invalid numbers ***");
        statistics.stream()
                .filter(x -> x.getInvalidNumbers() > 0)
                .forEach(x -> System.out.println(x.getInfo() + ": " + x.getInvalidNumbers() + " invalid number(s)"));

        System.out.println();
        System.out.println();
        System.out.println("*** No number ***");
        statistics.stream()
                .filter(x -> x.getValidNumbers() == 0)
                .forEach(x -> System.out.println(x.getInfo() + ": no valid numbers"));

        System.out.println();
        System.out.println();
        System.out.println("*** Multiple numbers ***");
        statistics.stream()
                .filter(x -> x.getValidNumbers() > 1)
                .forEach(x -> System.out.println(x.getInfo() + ": " + x.getValidNumbers() + " valid numbers set"));

        System.out.println();
        System.out.println();
        long usersWithAtLeastOneValidNumber = statistics.stream()
                .filter(x -> x.getValidNumbers() >= 1)
                .count();
        System.out.println("Users with at least one valid number: " + usersWithAtLeastOneValidNumber);
    }

    private static CheckStatistics validate(final ExternalUser externalUser) {
        int valid = 0;
        int invalid = 0;
        String firstValidNumber = null;

        for (String telephoneNumber : externalUser.getTelephoneNumbers()) {
            String normalized = phoneNumberNormalizer.normalize(telephoneNumber);
            if (StringUtils.isNotBlank(normalized)) {
                valid++;
                if (firstValidNumber == null) {
                    firstValidNumber = normalized;
                }
            } else {
                invalid++;
            }
        }

        ExternalUserId externalUserId = externalUser.getExternalUserId();
        return new CheckStatistics(
                valid,
                invalid,
                externalUserId.getPersonellId() + " - " + externalUserId.getLastname() + " " + externalUser.getTelephoneNumbers(),
                firstValidNumber);
    }
}
