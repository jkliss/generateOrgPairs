import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by students on 05.12.17.
 *
 * Container -> Proteinname
 *              ___________
 *              Amount
 *              complete_set_avail
 *              kegg_of_cuts -> KEGG_Organism
 *                              _____________
 *                              Array with Explicit KEGGName (Index is Elementno.)
 *                              Is complete set? --> Pull up
 *
 *
 *
 */
public class CutProtContainer {
    int amount_cut;
    String protein_name = null;
    HashMap<String, KEGGCuts> kegg_of_cuts = new HashMap<String, KEGGCuts>();
    HashMap<String, String> strange_blast_output_errors = new HashMap<String, String>();

    public CutProtContainer(){
        //format error
        //blastp removes organism part of keggname for tpa and tsa
        strange_blast_output_errors.put("TP", "tpa:");
        strange_blast_output_errors.put("AciPR4", "tsa:");
    }

    public void addProtein(String pname, String[] blastFields){
        protein_name = pname;
        String[] cutIDs = blastFields[0].replaceAll(protein_name, "").split("\\.");
        amount_cut = Integer.parseInt(cutIDs[0].replace("_", ""));
        //System.out.println("Parsed Cut Count " + amount_cut);
        //catch described error from constructor
        String name_suffix = "";
        if(! blastFields[1].contains(":")){
            name_suffix = strange_blast_output_errors.get(blastFields[1]);
        }
        String kegg_name = name_suffix + blastFields[1].split(":")[0];
        if(! kegg_of_cuts.containsKey(kegg_name)){
            KEGGCuts keggCuts = new KEGGCuts(amount_cut);
            kegg_of_cuts.put(kegg_name,keggCuts);
        }
        kegg_of_cuts.get(kegg_name).addPart(Integer.parseInt(cutIDs[1]),blastFields[1]);
    }

    public void printAll(BufferedWriter bw){
        for (String s : kegg_of_cuts.keySet()) {
            kegg_of_cuts.get(s).getElementsAsStringOnlyIfPossiblePair(bw, protein_name);
        }
    }
}
