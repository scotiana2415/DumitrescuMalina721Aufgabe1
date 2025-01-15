import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameOfThronsApp {
    public static final Pattern ID_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<id>(.*)</id>");
    public static final Pattern MITGLIEDSNAME_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<mitgliedsname>(.*)</mitgliedsname>");
    public static final Pattern HAUS_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<haus>(" +
            Haus.Stark + "|" + Haus.Lannister + "|" + Haus.Targaryen + "|" + Haus.Baratheon + "|" +
            Haus.Greyjoy + "|" + Haus.Martell + "|" + Haus.Tyrell + ")</haus>");
    public static final Pattern EREIGNIS_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<ereignis>(.*)</ereignis>");
    public static final Pattern DATUM_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<datum>(.*)</datum>");

    public List<Ereignis> leseTSV(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Ereignis> ereignisse = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String header = reader.readLine();
            String[] fields = header.split("\t");

            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split("\t");

                Ereignis ereignis = new Ereignis(0, null, null, null, null);

                for (int i = 0; i < fields.length; i++) {
                    String value = values[i].replace("\"", "");
                    switch (fields[i]) {
                        case "id": {
                            ereignis.setId(Integer.parseInt(value));
                            break;
                        }
                        case "mitgliedsname": {
                            ereignis.setMitgliedsname(value);
                            break;
                        }
                        case "haus": {
                            ereignis.setHaus(Haus.valueOf(value));
                            break;
                        }
                        case "ereignis": {
                            ereignis.setEreignis(value);
                            break;
                        }
                        case "datum": {
                            ereignis.setDatum(LocalDate.parse(value));
                            break;
                        }
                    }
                }

                ereignisse.add(ereignis);
                line = reader.readLine();
            }
        }
        return ereignisse;
    }

    public List<Ereignis> leseXML(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Ereignis> ereignisse = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            if (!reader.readLine().equals("<logs>")) {
                throw new IOException("Invalid XML file");
            }

            String line = reader.readLine();

            while (!line.equals("</logs>")) {
                Ereignis ereignis = new Ereignis(0, null, null, null, null);

                if (!line.contains("<log>")) {
                    throw new IOException("Invalid XML file");
                }

                String nextLine = reader.readLine();
                while (!nextLine.contains("</log>")) {
                    Matcher idMatcher = ID_PATTERN.matcher(nextLine);
                    if (idMatcher.matches()) {
                        ereignis.setId(Integer.parseInt(idMatcher.group(1)));
                    }

                    Matcher mitgliedsnameMatcher = MITGLIEDSNAME_PATTERN.matcher(nextLine);
                    if (mitgliedsnameMatcher.matches()) {
                        ereignis.setMitgliedsname(mitgliedsnameMatcher.group(1));
                    }

                    Matcher hausMatcher = HAUS_PATTERN.matcher(nextLine);
                    if (hausMatcher.matches()) {
                        ereignis.setHaus(Haus.valueOf(hausMatcher.group(1)));
                    }

                    Matcher ereignisMatcher = EREIGNIS_PATTERN.matcher(nextLine);
                    if (ereignisMatcher.matches()) {
                        ereignis.setEreignis(ereignisMatcher.group(1));
                    }

                    Matcher datumMatcher = DATUM_PATTERN.matcher(nextLine);
                    if (datumMatcher.matches()) {
                        ereignis.setDatum(LocalDate.parse(datumMatcher.group(1)));
                    }

                    nextLine = reader.readLine();
                }

                ereignisse.add(ereignis);
                line = reader.readLine();
            }
        }
        return ereignisse;
    }

    public void displayMembersByInitial(List<Ereignis> ereignisse) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an uppercase letter: ");
        String input = scanner.nextLine();

        if (input.length() != 1 || !Character.isUpperCase(input.charAt(0))) {
            System.out.println("Invalid input. Please enter a single uppercase letter.");
            return;
        }

        char initial = input.charAt(0);
        Set<String> uniqueNames = new HashSet<>();

        for (Ereignis ereignis : ereignisse) {
            String name = ereignis.getMitgliedsname();
            if (name != null && name.charAt(0) == initial) {
                uniqueNames.add(name);
            }
        }

        for (String name : uniqueNames) {
            System.out.println(name);
        }
    }

    public void displayStarkEventsSortedByDate(List<Ereignis> ereignisse) {
        List<Ereignis> starkEvents = ereignisse.stream()
                .filter(e -> e.getHaus() == Haus.Stark)
                .sorted(Comparator.comparing(Ereignis::getDatum))
                .collect(Collectors.toList());

        for (Ereignis ereignis : starkEvents) {
            System.out.println(ereignis.getDatum() + ": " + ereignis.getMitgliedsname() + " - " + ereignis.getEreignis());
        }
    }
}


