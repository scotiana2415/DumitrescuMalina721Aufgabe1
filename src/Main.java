import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        GameOfThronsApp app = new GameOfThronsApp();
        Scanner input = new Scanner(System.in);

        System.out.println("Running Game of Thrones App...");

        // Hardcoded file path and type
        String basePath = System.getProperty("user.dir") + "/src/";
        String filePath = basePath +  "data.xml";
        //String outputPath = basePath + "ergebnis.txt";
        List<Ereignis> ereignis = app.leseXML(filePath);
        System.out.println("Processing file: " + filePath);
        app.displayMembersByInitial(ereignis);
        app.displayMembersByInitial(ereignis);
    }
}
