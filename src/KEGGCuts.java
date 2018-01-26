import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class KEGGCuts {
    private String[] elements;
    private int numElements;
    private String blacklist = "";

    public KEGGCuts(int numElements){
        elements = new String[numElements];
        this.numElements = numElements;
    }

    public void addPart(int position, String KEGGName){
        //System.out.println(numElements);

        try{
            if(blacklist.contains(KEGGName)){
                //Blacklisted KEGG, do nothing
            } else if (doubleEntry(position, KEGGName)) {
                //Everything is done in subfunction
            } else if (elements[position-1] == null){
                elements[position-1] = KEGGName;
            } else if((!elements[position-1].contains(KEGGName)) && (elements[position-1].split("\\|").length < 10)){
                elements[position-1] = elements[position-1] + "|" + KEGGName;
            }
        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println(Integer.toString(position) + " " + KEGGName);
            ex.getStackTrace();
        }
        if(KEGGName.contains("bmaz")){
            System.out.println(KEGGName);
            System.out.println(getElementsAsString());
        }

    }

    public boolean doubleEntry(int position, String KEGGName){
        for (int i = 0; i<elements.length; i++) {
            if(i != position-1 && elements[i] != null && elements[i].contains(KEGGName)){
                ArrayList<String> sub_elements = new ArrayList<String>(Arrays.asList(elements[i].split("\\|")));
                if(sub_elements.contains(KEGGName)){
                    sub_elements.remove(KEGGName);
                    for(int k = 0; k < sub_elements.size()-1; k++){
                        elements[i] = sub_elements.get(k) + "|";
                    }
                    blacklist += KEGGName;
                    return true;
                }
                //System.out.println("Eliminating " + KEGGName + " from " + Integer.toString(i) + " Str " + elements[i] + " bc insert to " + Integer.toString(position-1));
                //elements[i] = elements[i].replace(KEGGName,"");
                //elements[i] = elements[i].replaceAll("\\|\\|","\\|");
            }
        }
        return false;
    }

    public String getElementsAsString(){
        String retString = "[";
        for (String element : elements) {
            retString += element + ",";
        }
        retString = retString.substring(0, retString.length() - 1);
        retString += "]";
        return retString;
    }

    public void getElementsAsStringOnlyIfPossiblePair(BufferedWriter bw, String protein_name){
        pairElements(bw, protein_name);
    }

    void pairElements(BufferedWriter bw, String protein_name){
        for(int i = 0; i < elements.length-1; i++){
            for(int j = i+1; j < elements.length; j++){
                pairKEGGs(elements[i], elements[j], bw, protein_name);
            }
        }
    }

    void pairKEGGs(String element1, String element2, BufferedWriter bw, String protein_name){
        if(element1 != null && element2 != null && (!element1.replaceAll("\\|", "").equals("")) &&  (!element2.replaceAll("\\|", "").equals(""))){
            String[] selement1 = element1.split("\\|");
            String[] selement2 = element2.split("\\|");
            for (int i = 0; i < selement1.length; i++) {
                for (int j = 0; j < selement2.length; j++) {
                    if(!selement1[i].equals("") && !selement2[j].equals("")) {
                        System.out.println(protein_name + "\t" + selement1[i] + "\t" + selement2[j]);
                        try {
                            bw.write( protein_name + "\t" + selement1[i] + "\t" + selement2[j] + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }
}
