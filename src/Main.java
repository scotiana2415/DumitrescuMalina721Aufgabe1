import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        GameOfThronsApp app = new GameOfThronsApp();
        Scanner input = new Scanner(System.in);

        System.out.println("Running Game of Thrones App...");

        // Hardcoded file path and type
        String basePath = System.getProperty("user.dir") + "/src/";
        String filePath = basePath +  "data.xml"; // Change this to process a different file type
        //String outputPath = basePath + "ergebnis.txt";
        List<Ereignis> ereignis = app.leseXML(filePath); // Change `leseCSV` to the corresponding method for the file type
        System.out.println("Processing file: " + filePath);
        app.displayMembersByInitial(ereignis);

    }
}
