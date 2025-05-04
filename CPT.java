import java.util.List;
import java.util.Map;

public class CPT {
    //Map<List<String>, Double> Table;
    String [][] Table; // 2D array for cpt table, order like in explanation file
    String variable;
    // List<String> givenVariables;
    //List<Double> probabilities;


    public CPT(String var, String [][]  table) {
        this.variable = var;
        this.Table = table;
        //this.givenVariables = givenVars;
        //this.probabilities = probs;
    }

    public CPT(){
        this.variable = "";
        this.Table = null;
        //this.givenVariables = null;
        //this.probabilities = null;
    }

}
