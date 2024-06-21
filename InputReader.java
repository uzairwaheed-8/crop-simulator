
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import java.util.HashMap;

public class InputReader {
    private static final String VALID_EXPR = "\s*[A-Za-z]+[A-Za-z0-9]*\s*=\s*-*(([0-9]+[.]?[0-9]*)|([.][0-9]+))\s*";

    public static String[] readInput(String fileName) throws IOException {
        StringBuilder contents = new StringBuilder(); // holds all the data present in file
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String readLine; // stores one line at a time
        while ((readLine = reader.readLine()) != null) {
            readLine = readLine.trim(); // removing any lines having only whitespaces
            if (readLine.length() > 0) // excluding any empty lines
                contents.append(readLine).append("\n");
        }
        reader.close();
        return (contents.toString()).split("\n"); // separating all expressions
    }

    public static int populateInput(HashMap<String, Double> inputVariables,JTextArea txtField) throws IOException {
        String selectedFile = "";
        String fileExt = "";

        JFileChooser fileChooser = new JFileChooser();

        while (!fileExt.equalsIgnoreCase("inp")) {

            int chooserStatus = fileChooser.showOpenDialog(null);
            if (chooserStatus == JFileChooser.APPROVE_OPTION) {

                selectedFile = (fileChooser.getSelectedFile()).getAbsolutePath();

                int extIndex = selectedFile.lastIndexOf('.');
                if (extIndex > 0) {
                    fileExt = selectedFile.substring(extIndex + 1);
                }
            } else if (chooserStatus == JFileChooser.CANCEL_OPTION) {
                return 9;
            }
        }
        return populateInput(inputVariables,txtField ,selectedFile);
    }

    public static int populateInput(HashMap<String, Double> inputVariables,JTextArea txtField ,String fileName)
            throws IOException, NumberFormatException {

        String[] inputData = InputReader.readInput(fileName);

        StringBuilder extraVariables = new StringBuilder();
        StringBuilder missingVariables = new StringBuilder();

        for (String input : inputData) { // iterating each expression
            if (!input.matches(VALID_EXPR)) // checking if expression is valid
                throw new IllegalArgumentException("Input Format is incorrect.\n---> " + input+"\n");

            String[] parts = input.split("="); // separating variable name and value
            String name = parts[0].trim(); // left hand side of expression

            try {
                Double value = Double.parseDouble(parts[1].trim()); // right hand side of expression

                if (inputVariables.containsKey(name)) { // if name exists in provided hashMap then its input value is
                                                        // added
                    inputVariables.put(name, value);
                } else { // extra variables will give warning
                    extraVariables.append(name).append(" ");
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException(input);
            }
        }

        if (extraVariables.length() > 0)
        	txtField.setText("Warning: Following variable are mentioned in input file, however they are not needed.\n"
                    + extraVariables+"\n");

        inputVariables.forEach((n, v) -> {
            if (v == null) {
                missingVariables.append(n).append(" ");
                inputVariables.put(n, 0.0);
            }
        });

        if (missingVariables.length() > 0) {
        	txtField.setText("Warning: Following variable values are missing, assuming zero.\n" + missingVariables+"\n");
        	return 1;
        }
        return 0;
    }
}

