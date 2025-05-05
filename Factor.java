import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Factor {
    List <String> variables; // List of variable names in the factor
    String[][] table; // 2D array for the factor table
    //int numRows; // Number of rows in the factor table
    //int numCols; // Number of columns in the factor table
    int numVariables; // Number of variables in the factor

    public Factor() {
        this.variables = new ArrayList<>();
        this.table = null;
        //this.numRows = 0;
        //this.numCols = 0;
        this.numVariables = 0;
    }
    public Factor(List<String> variables, String[][] table) {
        if (table.length == 0) {
            throw new IllegalArgumentException("Factor table must have at least one row.");
        }
        this.variables = variables;
        this.table = table;
        //this.numRows = table.length;
        //this.numCols = table[0].length;
        this.numVariables = variables.size();
        if (table[0].length != numVariables+1) {
            throw new IllegalArgumentException("Num factor columns != num Variables+1!");
        }
    }
    
    public Factor Join(Map<String, Variable> network, Factor other, String hiddenVar) {
        // checking correctness of the input
        if(this.table.length == 0 || other.table.length == 0) {
            throw new IllegalArgumentException("Cannot join empty factors.");
        }
        if (this.table.length == 1) return other;
        if (other.table.length == 1) return this;
        // 1. Union of variables
        List<String> newVariables = new ArrayList<>(this.variables);
        for (String var : other.variables) {
            if (!newVariables.contains(var)) {
                newVariables.add(var);
            }
        }

        // 2. Compute the number of rows for the new factor
        int newNumRows = 1;
        for (String varname : newVariables) {
            Variable var = network.get(varname);
            if (var != null) {
                newNumRows *= var.numOutcomes;
            } else {
                throw new IllegalArgumentException("Variable " + varname + " not found in network.");
            }
        }
        String[][] newTable = new String[newNumRows][newVariables.size() + 1];

        // 3. Prepare domains for outcome combinations to put in new factor table
        List<List<String>> domains = new ArrayList<>();
        for (String varname : newVariables) {
            domains.add(network.get(varname).outcomes);
        }/////////////////////////////////////
        // System.out.println("Joining factors:");
        // System.out.println("  this.variables: " + this.variables);
        // System.out.println("  other.variables: " + other.variables);
        // System.out.println("  newVariables: " + newVariables);
        /////////////////////////////////////
        // 4. For each assignment, look up the probability in both factors and multiply
        for (int row = 0; row < newNumRows; row++) {
            int divisor = newNumRows;
            String[] assignment = new String[newVariables.size()];
            for (int col = 0; col < newVariables.size(); col++) {// fills in new table with outcomes & prepares "assignment" for probs lookup
                List<String> domain = domains.get(col);
                divisor /= domain.size();
                int idx = (row / divisor) % domain.size();
                assignment[col] = domain.get(idx);
                newTable[row][col] = domain.get(idx);
            }

            // Build assignment maps for lookup
            Map<String, String> assignMap = new LinkedHashMap<>();
            for (int i = 0; i < newVariables.size(); i++) {
                assignMap.put(newVariables.get(i), assignment[i]);
            }

            // Extract assignment for this factor
            String[] thisAssign = new String[this.variables.size()];
            for (int i = 0; i < this.variables.size(); i++) {
                thisAssign[i] = assignMap.get(this.variables.get(i));
            }
            String[] otherAssign = new String[other.variables.size()];
            for (int i = 0; i < other.variables.size(); i++) {
                otherAssign[i] = assignMap.get(other.variables.get(i));
            }

            double prob1 = findProbability(this.table, this.variables, thisAssign);
            double prob2 = findProbability(other.table, other.variables, otherAssign);

            newTable[row][newVariables.size()] = String.valueOf(prob1 * prob2);
        }

        return new Factor(newVariables, newTable);
    }

    // Helper method: find probability in a String[][] factor table
    public static double findProbability(String[][] table, List<String> varNames, String[] valuesToMatch) {
        for (String[] row : table) {
            boolean match = true;
            for (int i = 0; i < varNames.size(); i++) {
                if (!row[i].equals(valuesToMatch[i])) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return Double.parseDouble(row[table[0].length - 1]);
            }
        }
        return 0.0;
    }

    public Factor Eliminate(Map<String, Variable> network, String hiddenVar) {
        if (this.table.length == 0) {
            throw new IllegalArgumentException("Cannot eliminate from an empty factor.");
        }
        if (this.table.length == 1) return null; // nothing to eliminate | RETURNS NULL!
        List<String> newVariables = new ArrayList<>(this.variables);
        newVariables.remove(hiddenVar);
        int newNumRows = this.table.length / (network.get(hiddenVar).numOutcomes);
        String[][] newTable = new String[newNumRows][newVariables.size() + 1]; // +1 for the probability column

        List<List<String>> domains = new ArrayList<>();
        for (String v : newVariables) {
            domains.add(network.get(v).outcomes);
        }

        for (int row = 0; row < newNumRows; row++) { // filling in table with outcome combinations (same code as previously)
            int divisor = newNumRows; 
            for (int col = 0; col < newVariables.size(); col++) {
                List<String> domain = domains.get(col);
                divisor /= domain.size();
                int idx = (row / divisor) % domain.size();
                newTable[row][col] = domain.get(idx);
            }
        }
        // Assign the probability to the last column // !!!!!!!!!!!!!!!!!!!!!
        List<String> hiddenDomains = network.get(hiddenVar).outcomes;
        for (int i = 0; i < newTable.length; i++) {// for each row in the new table
            double sum = 0.0;
            for (String outcome : hiddenDomains) { // for each outcome of the hidden variable to be summed
                String[] thisAssign = new String[this.variables.size()]; // find original rows
                for (int j = 0; j < this.variables.size(); j++) {
                    String varName = this.variables.get(j);
                    int idxInNew = newVariables.indexOf(varName);
                    if (idxInNew != -1) {
                        thisAssign[j] = newTable[i][idxInNew];
                    } else if (varName.equals(hiddenVar)) {
                        thisAssign[j] = outcome;
                    }
                }
                double prob = findProbability(this.table, this.variables, thisAssign);
                sum += prob;
            }
            newTable[i][newVariables.size()] = String.valueOf(sum);
        }



        return new Factor(newVariables, newTable);
    }

    public double[] Normalize() {
        double sum = 0.0;
        for (String[] row : this.table) {
            sum += Double.parseDouble(row[this.table[0].length - 1]);
        }
        double[] normalized = new double[this.table.length];
        for (int i = 0; i < this.table.length; i++) {
            normalized[i] = Double.parseDouble(this.table[i][this.table[0].length - 1]) / sum;
        }
        return normalized;
    }

}
