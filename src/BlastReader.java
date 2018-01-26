import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by students on 05.12.17.
 */
public class BlastReader {
    CutProtContainer container = null;
    HashMap<String, CutProtContainer> hmap = new HashMap<String, CutProtContainer>(1000000);

    public void read(String filename){
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //System.out.println(line);
                line = line.replace("\n", "");
                String[] fields = line.split("\t");
                String plainOrgName = fields[0].replaceAll("_[0-9]\\.[0-9]", "");
                if(!hmap.containsKey(plainOrgName)){
                    container = new CutProtContainer();
                    hmap.put(plainOrgName, container);
                }
                container = hmap.get(plainOrgName);
                container.addProtein(plainOrgName,fields);
                hmap.put(plainOrgName, container);
            }
        } catch (IOException ex){
            System.err.println("READ ERROR");
        }
    }

    public void printAllElements(BufferedWriter bw) {
        for (CutProtContainer cutProtContainer : hmap.values()) {
            cutProtContainer.printAll(bw);
        }
    }
}
