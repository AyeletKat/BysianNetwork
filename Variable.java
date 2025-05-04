import java.util.ArrayList;
import java.util.List;
public class Variable {
    String name;
    List<String> parents;
    List<String> outcomes;
    int numOutcomes;
    CPT cpt;

    public Variable() {
        this.name = "";
        this.parents = new ArrayList<>();;
        this.outcomes = new ArrayList<>();;
        this.cpt = null;
        this.numOutcomes = 0;
    }
    public Variable(String name, List<String> parents, List<String> outcomes, CPT cpt) {
        this.name = name;
        this.parents = parents;
        this.outcomes = outcomes;
        this.cpt = cpt;
        this.numOutcomes = outcomes.size();
    }



}
