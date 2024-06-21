import java.io.IOException;
import java.util.HashMap;

import javax.swing.JTextArea;


public class Weather {

    private static final String filePath = "D:\\SemProject\\src\\inputs\\weather.inp";

    // Input attributes
   
    private double PAR, rain, SRAD, TMIN, TMAX;

    public void initialize(double PAR, double rain, double SRAD, double TMIN, double TMAX) {
        this.PAR = PAR;
        this.rain = rain;
        this.SRAD = SRAD;
        this.TMIN = TMIN;
        this.TMAX = TMAX;
    }

    public int initialize(JTextArea txtField) {
    	int status=0;
        HashMap<String, Double> inputVariables = new HashMap<String, Double>();
        // initializing hashMap with input variable as keys
        inputVariables.put("PAR", null);
        inputVariables.put("rain", null);
        inputVariables.put("SRAD", null);
        inputVariables.put("TMIN", null);
        inputVariables.put("TMAX", null);

        try {
            status = InputReader.populateInput(inputVariables,txtField ,filePath); // populating hashMap with input values

            initialize(inputVariables.get("PAR"), inputVariables.get("rain"), inputVariables.get("SRAD"),
                    inputVariables.get("TMIN"), inputVariables.get("TMAX")); // settings the values of all variables

        } catch (IOException e) {
            txtField.setText("Error: Something related to input file went wrong.\n" + e.getMessage());
        } catch (NumberFormatException e) {
        	txtField.setText("Error: values are not correct. " + e.getMessage());
        } catch (NullPointerException e) {
        	txtField.setText("File not chosen!");
        }catch(IllegalArgumentException e) {
        	txtField.setText("Error: "+e.getMessage());
        	return 2;
        } catch (Exception e) {
           txtField.setText("Error " + e.getMessage());
        }
        return status;
    }

    public double getSRAD(){
        return  SRAD;
    }

    public double getTMIN(){
        return TMIN;
    }

    public double getTMAX(){
        return TMAX;
    }

    public double getRAIN(){
        return  rain;
    }

    public String summary() {
    	String summary =("Active Radiation = "+PAR+"\nRainfall = "+rain+"\nSolar Radation = "+SRAD+"\nMax Temp. = "+TMAX+"\nMin Temp. = "+TMIN); 
    	return  summary; 		
    }
}

