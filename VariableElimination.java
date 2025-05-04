

// public static class VariableElimination {
//     private List<Variable> variables;
//     private List<String> evidence;
//     private List<String> query;

//     public VariableElimination(List<Variable> variables, List<String> evidence, List<String> query) {
//         this.variables = variables;
//         this.evidence = evidence;
//         this.query = query;
//     }

//     public double[] run() {
//         // Initialize the factors
//         List<Factor> factors = new ArrayList<>();
//         for (Variable variable : variables) {
//             factors.add(new Factor(variable));
//         }

//         // Eliminate the non-evidence variables
//         for (String var : evidence) {
//             factors = eliminateVariable(factors, var);
//         }

//         // Multiply the remaining factors
//         Factor result = multiplyFactors(factors);

//         // Normalize the result
//         return normalize(result);
//     }