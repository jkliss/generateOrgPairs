import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by students on 05.12.17.
 */
public class GenerateOrgPairs {
    public static void main(String[] args) {
        String file = args[0];
        BlastReader blastReader = new BlastReader();
        blastReader.read(file);
        try {
            FileWriter fw = new FileWriter("outfile.sort");
            BufferedWriter bw = new BufferedWriter(fw);
            blastReader.printAllElements(bw);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
