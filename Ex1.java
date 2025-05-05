import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
// import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
// import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// TODO: Add written output to file

public class Ex1 {
    static int curr_num_mul_operations = 0;
    static int curr_num_add_operations = 0;
    public static void main(String[] args) throws Exception {

        // read xml file
        // File inputFile = new File("input.txt");
        // BufferedReader file = new BufferedReader(new FileReader("input.txt"));
        List <String> allLines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("input.txt"));
        String xmlName = allLines.get(0);
        Map<String, Variable> network = readXML(xmlName);

        // read input.txt (queries part) and send input to correct functions
        // write output to output.txt 
        FileWriter writer = new FileWriter("output.txt");       
        for (int i=1; i<allLines.size(); i++) {
            String line = allLines.get(i);
            if (line.startsWith("P(") && !line.contains("|")) { // joint probability query
                double result = jointProbability(line, network);
                //System.out.printf("Joint Probability for: %s = %.5f, 0 addition operations, %d mul operations.", line, result, curr_num_mul_operations);
                // write result to output.txt file
                writer.write(String.format("%.5f,0,%d\n", result, curr_num_mul_operations));
                curr_num_mul_operations = 0; // reset for next query
                curr_num_add_operations = 0;
            }
            // else if (line.startsWith("P(") && line.contains("|") && line.endsWith("1")) {} // algo 1
            else if (line.startsWith("P(") && line.contains("|") && line.endsWith("2")) { // algo 2
                //System.out.println("\nPROCESSING VE QUERY \n:");
                double[] result = algo2(line, network);
                if (result[0] == -1 && result[1] == -1 && result[2] == -1) { return;}
                writer.write(String.format("%.5f,%d,%d\n", result[0], (int)result[1], (int)result[2]));
                //System.out.printf("Variable Elimination for: %s = %.5f, %.0f addition operations, %.0f mul operations.", line, result[0], result[1], result[2]);
                // System.out.printf("Variable Elimination for: %s = %.5f, %d addition operations, %d mul operations.", line, result[0], curr_num_add_operations, curr_num_mul_operations);
                // curr_num_mul_operations = 0; // reset for next query
                // curr_num_add_operations = 0;
                // write result to output.txt file
            // }
            // else if (line.startsWith("P(") && line.contains("|") && line.endsWith("3")) { // algo 3
            //     double[] result = algo2(line, network);
            //     System.out.printf("Variable Elimination for: %s = %.5f, %d addition operations, %d mul operations.", line, result[0], curr_num_add_operations, curr_num_mul_operations);
            //     curr_num_mul_operations = 0; // reset for next query
            //     curr_num_add_operations = 0;
            //     // write result to output.txt file
            // }
            
            // else throw new Exception("Invalid query format: " + line);
            }
            else if (line.startsWith("P(") && line.contains("|") && line.endsWith("3")) { // algo 3
                //System.out.println("\nPROCESSING VE QUERY \n:");
                double[] result = algo3(line, network);
                if (result[0] == -1 && result[1] == -1 && result[2] == -1) { return;}
                writer.write(String.format("%.5f,%d,%d\n", result[0], (int)result[1], (int)result[2]));
                //System.out.printf("Variable Elimination for: %s = %.5f, %.0f addition operations, %.0f mul operations.", line, result[0], result[1], result[2]);
            }
        /////////////// trying to join factors
        // List<Factor> factors = new ArrayList<>();
        // for (String var : network.keySet()) {
        //     Variable currVar = network.get(var);
        //     if (currVar != null && currVar.cpt != null) {
        //         // Create a factor for the current variable
        //         Factor factor = new Factor();
        //         // Add all parents and the variable itself
        //         factor.variables.addAll(currVar.parents);
        //         factor.variables.add(currVar.name);
        //         factor.table = currVar.cpt.Table;
        //         factors.add(factor);
        //     }
        // }
        // // Find the factor with variables [E, A, B]
        // Factor factorEAB = null;
        // for (Factor factor : factors) {
        //     List<String> vars = factor.variables;
        //     if (vars.contains("E") && vars.contains("A") && vars.contains("B") && vars.size() == 3) {
        //         factorEAB = factor;
        //         break;
        //     }
        // }

        // // Find the factor with variables [A, J]
        // Factor factorAJ = null;
        // for (Factor factor : factors) {
        //     List<String> vars = factor.variables;
        //     if (vars.contains("A") && vars.contains("J") && vars.size() == 2) {
        //         factorAJ = factor;
        //         break;
        //     }
        // }

        // Factor newFactor = null; // Declare newFactor outside the if block
        // if (factorEAB == null || factorAJ == null) {
        //     System.err.println("Required factors not found!");
        // } else {
        //     newFactor = factorEAB.Join(network, factorAJ, "A");
        //     factors.remove(factorEAB);
        //     factors.remove(factorAJ);
        //     factors.add(newFactor);
        // }
        // // Print the resulting factor
        // System.out.println("Resulting factor after join:");
        // for (Factor factor : factors) {
        //     System.out.println("Variables: " + factor.variables);
        //     System.out.println("Table:");
        //     for (String[] row : factor.table) {
        //         for (String value : row) {
        //             System.out.print(value + " ");
        //         }
        //         System.out.println();
        //     }
        // }
        // // testing Elimination
        // Factor afterElimination = newFactor.Eliminate(network, "A");
        // System.out.println("Resulting factor after elimination:");
        // System.out.println("Variables: " + afterElimination.variables);
        // System.out.println("Table:");
        // for (String[] row : afterElimination.table) {
        //     for (String value : row) {
        //         System.out.print(value + " ");
        //     }
        //     System.out.println();
        // }
        }
        //////////////////////////////////
        // close file output.txt
        writer.close();
        // close file input.txt

        // file.close();
        // FileWriter writer = new FileWriter("output.txt");
    }

    // public static Map<String, Variable> readXML(String filePath) throws Exception{ 
    // // filePath- xml file path
    // Map<String, Variable> network = new HashMap<>();
    //
    // File inputFile = new File(filePath);
    // DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    // DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    // Document doc = dBuilder.parse(inputFile);
    // doc.getDocumentElement().normalize();
    //
    // // Parse VARIABLES
    // NodeList variableList = doc.getElementsByTagName("VARIABLE");
    // for (int i = 0; i < variableList.getLength(); i++) { // go variable variable
    // Node varNode = variableList.item(i);
    // if (varNode.getNodeType() == Node.ELEMENT_NODE) {
    // Element varElement = (Element) varNode;
    // String name =
    // varElement.getElementsByTagName("NAME").item(0).getTextContent();
    // Variable data = new Variable();
    //
    // NodeList outcomeList = varElement.getElementsByTagName("OUTCOME");
    // data.numOutcomes = outcomeList.getLength();
    // for (int j = 0; j < outcomeList.getLength(); j++) {
    // data.outcomes.add(outcomeList.item(j).getTextContent());
    // }
    // network.put(name, data);
    // }
    // }
    //
    // // Parse DEFINITIONS
    // NodeList definitionList = doc.getElementsByTagName("DEFINITION");
    // for (int i = 0; i < definitionList.getLength(); i++) {
    // Node defNode = definitionList.item(i);
    // if (defNode.getNodeType() == Node.ELEMENT_NODE) { //???
    // Element defElement = (Element) defNode;
    // String varName =
    // defElement.getElementsByTagName("FOR").item(0).getTextContent();
    // Variable data = network.get(varName);
    //
    // // Add parents
    // NodeList givenList = defElement.getElementsByTagName("GIVEN");
    // for (int j = 0; j < givenList.getLength(); j++) {
    // data.parents.add(givenList.item(j).getTextContent());
    // }
    //
    // // Add probabilities
    // String tableContent =
    // defElement.getElementsByTagName("TABLE").item(0).getTextContent();
    // String[] probs = tableContent.trim().split("\\s+"); // split by whitespace \
    // tab \ newline
    // for (String p : probs) {
    // data.outcomes.add(String.valueOf(Double.parseDouble(p))); // added string
    // value of
    // }
    //
    // // Create CPT //////////////////////////////////////////
    // CPT cpt = new CPT(); // -> need to send to is String [][] table
    // cpt.variable = varName;
    // data.cpt = cpt; // add the cpt to the variable
    // int numRows; // now lets calculate the number of rows in the table =
    // multipication of parents' and variable's outcomes options
    // if (data.parents.size() == 0) {numRows = 1;}
    // else {
    // numRows = 1;
    // for (String parent : data.parents) {
    // Variable parentVar = network.get(parent);
    // numRows *= parentVar.numOutcomes;
    // }
    // }
    // String[][] table = new String[numRows][data.parents.size() + 2]; // +2 for
    // the variable itself, outcome and probability
    // // now fill the table with the probabilities and outcomes combinations
    // for (int j = 0; j < numRows; j++) {
    // table[j][data.parents.size() + 1] =
    // String.valueOf(Double.parseDouble(probs[j])); // fill the probability, order
    // like in xml list
    // }
    // for (int j = 0; j < numRows; j++) {
    // table[j][data.parents.size()] = data.outcomes.get(j % data.numOutcomes); //
    // fill the outcomes, order one by one, variable's outcomes
    // }
    // for (int j = 0; j < numRows; j++) { // j=row index, k=0 first parent
    // int parentVarIndex = j / (numRows /
    // network.get(data.parents.get(0)).numOutcomes);
    // table[j][0] = network.get(data.parents.get(0)).outcomes.get(parentVarIndex);
    // }
    //
    //
    //
    //
    //
    // }
    // }
    // return network;
    // }

    /**
     * Parses query of type P(E1=e1, E2=e2, ..., En=en) 
     * and calculates the joint probability of this combination.
     * @param query String formated like P(E1=e1, E2=e2, ..., En=en)
     * @param network Map of variable names to their Variable objects
     * @return the double probability (rounded to 5 digits after the decimal point), or -1.0 if an error occurs
     */
    public static double jointProbability(String query, Map<String, Variable> network) {
        // Parsing the query string to extract variable names and their values to varNames array and assignments map
        String substr = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
        String[] parts = substr.split(",");
        String[] varNames = new String[parts.length];
        Map<String, String> assignments = new HashMap<>();

        for (int i = 0; i < parts.length; i++) {
            String[] pair = parts[i].split("=");
            varNames[i] = pair[0].trim();
            assignments.put(pair[0].trim(), pair[1].trim());
        }
        // here - calculating the joint probability using the CPTs of the variables with given assignments
        double jointProb = 1.0;
        // iterating through all variables, pulling out relevant probability from CPT, and adding it to the multipilication of joint probability
        for (String varname : varNames) {// "iterating through all variables"
            Variable currVar = network.get(varname);
            if (currVar != null && currVar.cpt != null) {
                // finding the corresponding row in the CPT for the given variable and value
                double prob = findProb(currVar, assignments); // "pulling out relevant probability from CPT"
                if (prob != -1.0) { jointProb *= prob; } // "and adding it to the multipilication of joint probability"
                else { 
                    System.err.println("Error: Probability not found for variable " + varname + " with assignments " + assignments);
                    return -1.0; // error
                }
            }
        }
        curr_num_mul_operations = varNames.length - 1; // number of multiplications is number of variables - 1
        return jointProb;
    }
    /**
     * For a given variable and a map of variable values assignments from 
     * joint probability query, finds variable's probability from CPT.
     * Note: searches only in the CPT of the qyuery variable.
     * 
     * @param var - the variable for which we want to find the probability
     * @param assignments - a map of variable names to their outcomes
     * 
     * @return the probability of the variable given the assignments
     */
    private static double findProb(Variable var, Map<String, String> assignments) {
        int rowsnum = var.cpt.Table.length;
        // System.out.println("variable = " + var.name + " rowsnum: " + rowsnum); // debugging
        int colsnum = var.cpt.Table[0].length;

        for (int i = 0; i < rowsnum; i++) { // iterating through rows of cpt (outcome combinations)
            boolean match = true;
            // if variable has no parents, checking only last column (probability) with correct outcome
            if (var.parents.size() == 0) {
                // System.out.println("var: " + var.name + " - i: " +i); // debugging
                if (!var.cpt.Table[i][colsnum - 2].equals(assignments.get(var.name))) { // check if the value of the variable in current cpt line is equal to value in query
                    match = false; // happens only if there is an error
                }
            }
            else { // var has parents
                for (int j = 0; j < colsnum-2; j++) { // dont look at the actual probability yet, go through parents columns
                    String currParent = var.parents.get(j);
                    if (!var.cpt.Table[i][j].equals(assignments.get(currParent))) { // check if the value of the *PARENT* in current cpt line is equal to value in query
                        match = false;
                        break;
                    }
                }
                if (!var.cpt.Table[i][colsnum-2].equals(assignments.get(var.name))) { // check if the value of the *VAR* in current cpt line is equal to value in query
                    match = false;
                }
            }
            // found matching to query cpt row - return the probability of this row (outcomes combination)
            if (match) { return Double.parseDouble(var.cpt.Table[i][colsnum - 1]); }
        }
        return -1.0; // match wasnt found, error somewhere
    }


    public static Map<String, Variable> readXML(String filePath) throws Exception {
        Map<String, Variable> network = new LinkedHashMap<>();

        File inputFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        // Parse VARIABLES
        NodeList variableList = doc.getElementsByTagName("VARIABLE");
        for (int i = 0; i < variableList.getLength(); i++) {
            Node varNode = variableList.item(i);
            if (varNode.getNodeType() == Node.ELEMENT_NODE) {
                Element varElement = (Element) varNode;
                String name = varElement.getElementsByTagName("NAME").item(0).getTextContent();
                Variable data = new Variable();
                data.name = name;

                NodeList outcomeList = varElement.getElementsByTagName("OUTCOME");
                for (int j = 0; j < outcomeList.getLength(); j++) {
                    data.outcomes.add(outcomeList.item(j).getTextContent());
                }
                data.numOutcomes = data.outcomes.size();
                network.put(name, data);
            }
        }

        // Parse DEFINITIONS
        NodeList definitionList = doc.getElementsByTagName("DEFINITION");
        for (int i = 0; i < definitionList.getLength(); i++) {
            Node defNode = definitionList.item(i);
            if (defNode.getNodeType() == Node.ELEMENT_NODE) {
                Element defElement = (Element) defNode;
                String varName = defElement.getElementsByTagName("FOR").item(0).getTextContent();
                Variable data = network.get(varName);

                // Add parents
                NodeList givenList = defElement.getElementsByTagName("GIVEN");
                for (int j = 0; j < givenList.getLength(); j++) {
                    data.parents.add(givenList.item(j).getTextContent());
                }

                // Add probabilities
                String tableContent = defElement.getElementsByTagName("TABLE").item(0).getTextContent();
                String[] probs = tableContent.trim().split("\\s+");

                // Create CPT
                CPT cpt = new CPT();
                cpt.variable = varName;
                int numRows = probs.length;
                cpt.Table = new String[numRows][data.parents.size() + 2]; // parents outcomes + var outcome + probability

                // Generate Cartesian product of parent variables' outcomes
                List<String> allVars = new ArrayList<>(data.parents);
                allVars.add(varName); // Add the child variable last

                List<List<String>> domains = new ArrayList<>();
                for (String v : allVars) {
                    domains.add(network.get(v).outcomes);
                }

                for (int row = 0; row < numRows; row++) {
                    int divisor = numRows; // Start with the total number of rows
                    for (int col = 0; col < allVars.size(); col++) {
                        List<String> domain = domains.get(col);
                        divisor /= domain.size(); // Divide by the size of the current domain
                        int idx = (row / divisor) % domain.size(); // Calculate the index for the current domain
                        cpt.Table[row][col] = domain.get(idx);
                    }
                    // Assign the probability to the last column
                    cpt.Table[row][data.parents.size() + 1] = probs[row];
                }

                data.cpt = cpt;

                // Print the CPT for debugging
                // System.out.println("CPT for " + varName + ":");

                // // Print the column headers
                // for (String parent : data.parents) {
                //     System.out.print(parent + " "); // Print parent variable names
                // }
                // System.out.print(varName + " "); // Print the child variable name
                // System.out.println("Probability"); // Print the probability column header

                // // Print the rows of the CPT
                // for (String[] row : cpt.Table) {
                //     for (String value : row) {
                //         System.out.print(value + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println();

            }
        }

        return network;
    }

    // return int[3], probability, num_add_operations, num_mul_operations
    public static int[] algo1 (String query, Map<String, Variable> network) {
        String substr = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
        String[] twoparts = substr.split("|");
        String[] query_var_and_outcome = twoparts[0].split("=");
        String query_var_name = query_var_and_outcome[0].trim();
        String query_var_outcome = query_var_and_outcome[1].trim();

        String[] evidence_vars_outcomes = twoparts[1].split(",");
        Map <String, String> evidence = new HashMap<>();
        for (String evidence_var_outcome : evidence_vars_outcomes) {
            String[] pair = evidence_var_outcome.split("=");
            evidence.put(pair[0].trim(), pair[1].trim());
        }
        List <String> hiddenVars = new ArrayList<>();
        for (String var : network.keySet()) {
            if (!evidence.containsKey(var) && !var.equals(query_var_name)) {
                hiddenVars.add(var);
            }
        }


        return null;
    }

    /**
     * Variable Elimination, including elimination of unneeded hidden variables in the beggining.
     * Variable order of Elimination is alphabetical. A->B->C->D...
     * @param query
     * @param network
     * @return double[3] array containing probability, number of addition operations, number of multiplication operations.
     */
    public static double[] algo2 (String query, Map<String, Variable> network) {
        int num_add_operations = 0;
        int num_mul_operations = 0;
        // 1. parsing query & checking if query already has answer in CPT
        List<List<Object>> data = new ArrayList<>();
        try {
            data = PreprocessQuery(network, query);
        }
        catch (FoundInCPTException e){
            // if the query was found already in cpt, we had the exception and can finish checks here!
            return new double[] {e.getValue(), 0, 0};
        }
        if (data == null){
            System.err.println("Error: PreprocessQuery returned null.");
            return new double[] {-1, -1, -1};
        }
        // if preprocessing was sucsessful - now parsing from Object to relevant types
        List<Object> rawFactors = data.get(0);
        List<Factor> factors = new ArrayList<>();
        for (Object obj : rawFactors) {
            if (obj instanceof Factor) factors.add((Factor) obj);
            else throw new ClassCastException("Element in list is not of type Factor");
        }
        List<Object> rawHiddenVars = data.get(1);
        List<String> hiddenVars = new ArrayList<>();
        for (Object obj : rawHiddenVars) {
            if (obj instanceof String) hiddenVars.add((String) obj); 
            else throw new ClassCastException("Element in list is not of type String");
        }

        List<Object> rawEvidenceVars = data.get(2);
        List<String> evidenceVars = new ArrayList<>();
        for (Object obj : rawEvidenceVars) {
            if (obj instanceof String) evidenceVars.add((String) obj);
            else throw new ClassCastException("Element in list is not of type String");
        }
        String query_var_name = (String) data.get(3).get(0);
        String query_var_outcome = (String) data.get(4).get(0);


        // 2. sorting hidden variables by ABC order
        hiddenVars.sort(String::compareTo); // alphabetical sort

        // 3. MAIN LOOP - iterates on hidden variables, joins and eliminated factors (join, eliminate, discard one valued)
        while (!hiddenVars.isEmpty()) {
            String varToEliminate = hiddenVars.get(0); // get the first variable to eliminate
            hiddenVars.remove(0); // remove it from the list of hidden variables

            // 3.1 Find the factors that contain the variable to eliminate
            List<Factor> factorsToJoin = new ArrayList<>();
            for (Factor factor : factors) {
                if (factor.variables.contains(varToEliminate)) {
                    factorsToJoin.add(factor);
                }
            }
            
            // 3.2 Sort them by table size, then by sum of ASCII values of 
            // variables' namesn(in place lambda sorting function)
            factorsToJoin.sort((f1, f2) -> {
                int cmp = Integer.compare(f1.table.length, f2.table.length);
                if (cmp != 0) return cmp;
                int sum1 = f1.variables.stream()
                    .flatMapToInt(String::chars)
                    .sum();
                int sum2 = f2.variables.stream()
                    .flatMapToInt(String::chars)
                    .sum();
                return Integer.compare(sum1, sum2);
            });

            // 3.3 now join these factors that contain that hidden variable
            List<Factor> toJoin = new ArrayList<>(factorsToJoin); // defensive copy
            while (toJoin.size() > 1) {
                // Sort factors by table size, then by sum of ASCII values
                toJoin.sort((f1, f2) -> {
                    int cmp = Integer.compare(f1.table.length, f2.table.length);
                    if (cmp != 0) return cmp;
                    int sum1 = f1.variables.stream().flatMapToInt(String::chars).sum();
                    int sum2 = f2.variables.stream().flatMapToInt(String::chars).sum();
                    return Integer.compare(sum1, sum2);
                });
                // removing the two smallest
                Factor f1 = toJoin.remove(0);
                Factor f2 = toJoin.remove(0);

                Factor joined = f1.Join(network, f2, varToEliminate);// Joining them
                num_mul_operations += joined.table.length; // one probabilities multipication to get each row
                toJoin.add(joined); // adding the result back to list of factors

            }
            // joined result, last factor
            Factor joinedFactor = toJoin.get(0);

            // 3.4 now eliminate the hidden variable from the joined factor
            Factor eliminatedFactor = joinedFactor.Eliminate(network, varToEliminate);
            // num additions; for hidden var with n outcomes, to add them to one row, n-1 additions
            num_add_operations += (eliminatedFactor.table.length)*(network.get(varToEliminate).numOutcomes-1); 

            //3.5 remove the original factors from the list and add the new factor if it's not one valued
            factors.removeAll(factorsToJoin);
            if (eliminatedFactor.table.length > 1) factors.add(eliminatedFactor);
        }


        // 4. Join remaining factors of query variable
        while (factors.size() > 1) {
            // Sort factors. should be from the same size and same (query) variable but still.
            factors.sort((f1, f2) -> {
                int cmp = Integer.compare(f1.table.length, f2.table.length);
                if (cmp != 0) return cmp;
                int sum1 = f1.variables.stream().flatMapToInt(String::chars).sum();
                int sum2 = f2.variables.stream().flatMapToInt(String::chars).sum();
                return Integer.compare(sum1, sum2);
            });
            // Remove the two smallest
            Factor f1 = factors.remove(0);
            Factor f2 = factors.remove(0);
            // Join them
            Factor joinedH = f1.Join(network, f2, null); // no hidden variable to eliminate
            num_mul_operations += joinedH.table.length; // one probabilities multipication to get each row
            // Add the result back
            factors.add(joinedH);
        }

        // 5. Normalize the last factor
        Factor finalFactor = factors.get(0);
        double[] norm = finalFactor.Normalize();
        num_add_operations += (network.get(query_var_name).numOutcomes - 1); // sum n outcomes to normalize = n-1 add operations

        // 6. Pull out probability of query given outcome
        int indx = network.get(query_var_name).outcomes.indexOf(query_var_outcome);
        double prob = norm[indx];

        return new double[] {prob, (double)num_add_operations, (double)num_mul_operations};
    }


    /**
     * Algorithm 3 - Variable Elimination with min-fill heuristic.
     * @param query - String formatted like P(E1=e1|E2=e2, ..., En=en)
     * @param network - a map of variable names to their Variable objects
     * @return - returns double[3] - probability, number of addition operations, number of multiplication operations.
     */
    public static double[] algo3(String query, Map<String, Variable> network) {
        int num_add_operations = 0;
        int num_mul_operations = 0;
        // 1. parsing query & checking if query already has answer in CPT
        List<List<Object>> data = new ArrayList<>();
        try {
            data = PreprocessQuery(network, query);
        }
        catch (FoundInCPTException e){
            // if the query was found already in cpt, we had the exception and can finish checks here!
            return new double[] {e.getValue(), 0, 0};
        }
        if (data == null){
            System.err.println("Error: PreprocessQuery returned null.");
            return new double[] {-1, -1, -1};
        }
        // if preprocessing was sucsessful - now parsing from Object to relevant types
        List<Object> rawFactors = data.get(0);
        List<Factor> factors = new ArrayList<>();
        for (Object obj : rawFactors) {
            if (obj instanceof Factor) factors.add((Factor) obj);
            else throw new ClassCastException("Element in list is not of type Factor");
        }
        List<Object> rawHiddenVars = data.get(1);
        List<String> hiddenVariables = new ArrayList<>();
        for (Object obj : rawHiddenVars) {
            if (obj instanceof String) hiddenVariables.add((String) obj); 
            else throw new ClassCastException("Element in list is not of type String");
        }

        List<Object> rawEvidenceVars = data.get(2);
        List<String> evidenceVars = new ArrayList<>();
        for (Object obj : rawEvidenceVars) {
            if (obj instanceof String) evidenceVars.add((String) obj);
            else throw new ClassCastException("Element in list is not of type String");
        }
        String query_var_name = (String) data.get(3).get(0);
        String query_var_outcome = (String) data.get(4).get(0);

        List<String> hiddenVars = new ArrayList<>(hiddenVariables); // clone to work on it
    
        while (!hiddenVars.isEmpty()) {
            // Select variable with min-fill heuristic
            String bestVar = null;
            int minFill = Integer.MAX_VALUE;
    
            for (String var : hiddenVars) {
                // Find factors containing var
                List<Factor> related = new ArrayList<>();
                for (Factor f : factors) {
                    if (f.variables.contains(var)) {
                        related.add(f);
                    }
                }
                // Simulate join to count new edges
                Set<String> union = new HashSet<>();
                for (Factor f : related) {
                    union.addAll(f.variables);
                }
                union.remove(var); // hidden var will be eliminated
                int edges = 0;
                List<String> vars = new ArrayList<>(union);
                for (int i = 0; i < vars.size(); i++) {
                    for (int j = i + 1; j < vars.size(); j++) {
                        edges++;
                    }
                }
                if (edges < minFill) {
                    minFill = edges;
                    bestVar = var;
                }
            }
    
            // Eliminate bestVar
            List<Factor> toJoin = new ArrayList<>();
            Iterator<Factor> it = factors.iterator();///////////////////////////////////////////////////////
            while (it.hasNext()) {
                Factor f = it.next();
                if (f.variables.contains(bestVar)) {
                    toJoin.add(f);
                    it.remove();
                }
            }
    
            Factor joined = toJoin.get(0);
            for (int i = 1; i < toJoin.size(); i++) {
                joined = joined.Join(network, toJoin.get(i), bestVar);
                num_mul_operations += joined.table.length; // one probabilities multipication to get each row
            }
    
            Factor reduced = joined.Eliminate(network, bestVar);
            num_add_operations += (reduced.table.length) * (network.get(bestVar).numOutcomes - 1); // sum n outcomes to normalize = n-1 add operations
            if (reduced != null) {
                factors.add(reduced);
            }
    
            hiddenVars.remove(bestVar);
        }
    
        // Multiply remaining factors
        Factor finalFactor = factors.get(0);
        for (int i = 1; i < factors.size(); i++) {
            finalFactor = finalFactor.Join(network, factors.get(i), "");
            num_mul_operations += finalFactor.table.length; // one probabilities multipication to get each row
        }
        double[] norm = finalFactor.Normalize();
        num_add_operations += (network.get(query_var_name).numOutcomes - 1); // sum n outcomes to normalize = n-1 add operations

        // 6. Pull out probability of query given outcome
        int indx = network.get(query_var_name).outcomes.indexOf(query_var_outcome);
        double prob = norm[indx];

        return new double[] {prob, (double)num_add_operations, (double)num_mul_operations};
    
    }
        

    /**
     * Preprocesses the query by removing irrelevant variables, creating factors for relevant variables, and instantiating factors by evidence variables.
     * (This is a helper function for algo2 and algo3 functions.)
     * @param network Map of variable names to their Variable objects
     * @param query String formatted like P(E1=e1, E2=e2, ..., En=en)
     * @return List of factors and hidden variables
     */
    public static List<List<Object>> PreprocessQuery(Map<String, Variable> network, String query) {
        // לאתחל פקטורים לפי המשתנים האבידנס - להוריד שורות לא רלוונטיות 2 DONE
        // 1 עם הורדת משתנים מיותרים בהתחלה כמו שמתואר בשקף 91 במצגת!!! DONE
        // להוסיף שליפה ישירה אם זה קיים בשאילתה! TO DO

        // 1. parsing query
        String substr = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
        String[] twoparts = substr.split("\\|");
        String[] query_var_and_outcome = twoparts[0].split("=");
        String query_var_name = query_var_and_outcome[0].trim();
        String query_var_outcome = query_var_and_outcome[1].trim();

        String[] evidence_vars_outcomes = twoparts[1].split(",");
        Map <String, String> evidence = new LinkedHashMap<>();
        List<String> evidenceVars = new ArrayList<>();
        for (String evidence_var_outcome : evidence_vars_outcomes) {
            String[] pair = evidence_var_outcome.split("=");
            evidence.put(pair[0].trim(), pair[1].trim());
            evidenceVars.add(pair[0].trim());
        }
        List <String> hiddenVars = new ArrayList<>();
        for (String var : network.keySet()) {
            if (!evidence.containsKey(var) && !var.equals(query_var_name)) {
                hiddenVars.add(var);
            }
        }
        // 2. creating map of variabe to its parents
        Map<String, List<String>> parentsMap = new LinkedHashMap<>();
        for (String var : network.keySet()) {
            Variable currVar = network.get(var);
            if (currVar != null) {
                parentsMap.put(var, currVar.parents);
            }
        }
        // 3. before next steps check if query already appears in query's CPT
        boolean flag = true;
        Variable queryVar = network.get(query_var_name);
        if (queryVar != null && queryVar.cpt != null) {
            for (String evi : evidenceVars) {
                if (!queryVar.parents.contains(evi)) {
                    flag = false;
                    break;
                }
            }
            if (flag){// query should appear in CPT, now searching for it
                double pro = findProb(queryVar, evidence);
                throw new FoundInCPTException(pro);
            }
        }
        // 4. finding irelevant variables as in slide 91 (hidden vars who arent ansectors of query or evidence vars)
        Set<String> relevant = new LinkedHashSet<>();
        Queue<String> toProcess = new LinkedList<>();

        toProcess.add(query_var_name);
        toProcess.addAll(evidenceVars);

        while (!toProcess.isEmpty()) {
            String var = toProcess.poll();
            if (relevant.contains(var)) continue;
            relevant.add(var);
            List<String> parents = parentsMap.get(var);
            if (parents != null) toProcess.addAll(parents);
        }

        List<String> prunedVars = new ArrayList<>(); // list of irrelevant variables
        for (String var : network.keySet()) {
            if (!relevant.contains(var)) prunedVars.add(var);
        }

        // 5. creating factors for all rlevant variables
        List<Factor> factors = new ArrayList<>();
        for (String var : relevant) { // FIXME: פה נוצרו פקטורים לפי סיפיטי של משתנים רלוונטים, !לא נמחקו כל פקטור ש*מכיל* משתנה לא רלוונטי!
            Variable currVar = network.get(var);
            if (currVar != null && currVar.cpt != null) {
                // Create a factor for the current variable
                Factor factor = new Factor();
                // Add all parents and the variable itself
                factor.variables.addAll(currVar.parents);
                factor.variables.add(currVar.name);
                factor.table = currVar.cpt.Table;
                factors.add(factor);
            }
        }
        // 6. remove irrelevant variables from hidden list
        hiddenVars.removeIf(var -> !relevant.contains(var)); // remove irrelevant variables from hidden list

        // 7. instantiate factors by evidence variables given outcomes
        List<Factor> factorsToRemove = new ArrayList<>();
        for (Factor factor : factors) {
            for (String evidenceVar : evidenceVars) {
                if (factor.variables.contains(evidenceVar)) {
                    // Convert table to ArrayList for row removal
                    List<String[]> tableList = new ArrayList<>(Arrays.asList(factor.table));
                    int index = factor.variables.indexOf(evidenceVar);
                    int numRemoved = 0;
                    for (int i = 0; i < factor.table.length; i++) {
                        if (!factor.table[i][index].equals(evidence.get(evidenceVar))) {
                            tableList.remove(i - numRemoved);
                            numRemoved++;
                        }
                    }
                    // Remove the evidence variable's column from each row
                    List<String[]> newTableList = new ArrayList<>();
                    for (String[] row : tableList) {
                        String[] newRow = new String[row.length - 1];
                        int k = 0;
                        for (int j = 0; j < row.length; j++) {
                            if (j != index) {
                                newRow[k++] = row[j];
                            }
                        }
                        newTableList.add(newRow);
                    }
                    // Convert back to 2D array
                    String[][] updatedTable = newTableList.toArray(new String[newTableList.size()][factor.table[0].length - 1]);
                    factor.table = updatedTable;
                    // Remove the evidence variable from the variable list
                    factor.variables.remove(evidenceVar);
                }
            }
            // // print factors for debugging
            // System.out.println("Factor after evidence assignment:");
            // System.out.println("Variables: " + factor.variables);
            // System.out.println("Table:");
            // for (String[] row : factor.table) {
            //     for (String value : row) {
            //         System.out.print(value + " ");
            //     }
            //     System.out.println();
            // }

            if (factor.table.length == 1) { // if only one row left, factor should be removed
                factorsToRemove.add(factor);
            }
        }
        factors.removeAll(factorsToRemove); // remove factors with only one row left
        // for (Factor factor : factors) {
        //     // Print the resulting factor
        //     System.out.println("Resulting factor after removal of one valued:");
        //     System.out.println("Variables: " + factor.variables);
        //     System.out.println("Table:");
        //     for (String[] row : factor.table) {
        //         for (String value : row) {
        //             System.out.print(value + " ");
        //         }
        //         System.out.println();
        //     }
        // }
        // 8. casting as Object to return to algorithms
        List<List<Object>> result = new ArrayList<>();
        result.add(new ArrayList<Object>(factors));
        result.add(new ArrayList<Object>(hiddenVars));
        result.add(new ArrayList<Object>(evidenceVars));
        result.add(Arrays.asList(query_var_name));
        result.add(Arrays.asList(query_var_outcome));
        return result;
    
    }


    static class FoundInCPTException extends RuntimeException {
        private final double value;
    
        public FoundInCPTException(double value) {
            this.value = value;
        }
    
        public double getValue() {
            return value;
        }
    }

    

}
