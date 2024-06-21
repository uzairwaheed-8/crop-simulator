import java.io.*;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JTextArea;

public class SoilWater {
    private static final String filePath = "D:\\SemProject\\src\\inputs\\soilWater.inp";

    // input variables
    private int CN;
    private double DP, DRNp, FCp, STp, SWC, WPp;
    private double IRR;

    // calculated variables
    private double THE; // Threshold Soil Water Constant,
    private double WP, ESp; // Wilting Point, //Potential Soil Evaporation
    private double FC, EEQ; // Field Capacity, //Equilibrium Evaporation
    private double ST, Tmed; // Saturation Content, //Average Temperature
    private double POTINF;
    private double SWFAC1, SWFAC2; //Stress factors
    private double DRN, S; // Vertical Drainage, //Soil Storage Capacity
    private double ROF, EPp, ESa; // Daily Surface Water Runoff Rate, //Plant Transpiration, //Daily Soil
    // Evaporation Rate
    private double INF, ALB, ETp; // Infiltration, //Surface Albedo, //Daily Potential Evapotranspiration Rate
    private double EPa; // Actual plant transpiration rate
    // initialize variables
    private double TRAIN = 0.0;
    private double TIRR = 0.0;
    private double TESA = 0.0;
    private double TEPA = 0.0;
    private double TROF = 0.0;
    private double TDRN = 0.0;
    private double TINF = 0.0;
    private double SWC_ADJ = 0.0;
    private double DWT = 0.0; // depth to the water table
    private double WTABLE; // thickness of water table
    private double STRESS_DEPTH = 250; // unit mm

    public SoilWater() {
       
    }

    public SoilWater(int CN, double DP, double DRNp, double FCp, double STp, double SWC, double WPp, double IRR) {
        initialize(CN, DP, DRNp, FCp, STp, SWC, WPp, IRR);
    }

    public void initialize(double CN, double DP, double DRNp, double FCp, double STp, double SWC, double WPp, double IRR) {
        this.CN = (int) CN;
        this.DP = DP;
        this.DRNp = DRNp;
        this.FCp = FCp;
        this.STp = STp;
        this.SWC = SWC;
        this.WPp = WPp;
        this.IRR = IRR;
    }

    public int initialize(JTextArea txtField){
    	int status =0;
        HashMap<String, Double> inputVariables = new HashMap<String, Double>();
        // initializing hashMap with input variable as keys
        inputVariables.put("CN", null);
        inputVariables.put("DP", null);
        inputVariables.put("DRNp", null);
        inputVariables.put("FCp", null);
        inputVariables.put("STp", null);
        inputVariables.put("SWC", null);
        inputVariables.put("WPp", null);
        inputVariables.put("IRR", null);

        try {
        	
        	status = InputReader.populateInput(inputVariables,txtField ,filePath); // populating hashMap with input values

            initialize(inputVariables.get("CN"), inputVariables.get("DP"), inputVariables.get("DRNp"),
                    inputVariables.get("FCp"), inputVariables.get("STp"), inputVariables.get("SWC"),
                    inputVariables.get("WPp"), inputVariables.get("IRR")); // settings the values of all variables
            
        } catch (IOException e) {
        	txtField.setText("Error: Something related to input file went wrong.\n" + e.getMessage());
        } catch (NumberFormatException e) {
        	txtField.setText("Error: values are not correct. " + e.getMessage());
        } catch (NullPointerException e) {
        	txtField.setText("File not chosen!");
        } catch(IllegalArgumentException e) {
        	txtField.setText("Error: "+e.getMessage());
        	return 2;
        } 
        catch (Exception e) {
        	txtField.setText("Error " + e.getMessage());
        }
        return status;
    }

    public void rateCalc(double LAI, double SRAD, double RAIN, double TMAX, double TMIN) {
        // Wilting Point
        WP = DP * WPp * 10;
        // Field Capacity
        FC = DP * FCp * 10;
        // Saturation Content
        ST = DP * STp * 10;
        // Potential Infiltration
        POTINF = RAIN + IRR;
        // Surface Albedo
        ALB = (0.1 * Math.exp(-0.7 * LAI)) + (0.2 * (1 - Math.exp(-0.7 * LAI)));
        // median temperature
        Tmed = 0.6 * TMAX + 0.6 * TMIN;
        // Equilibrium Evaporation
        EEQ = SRAD * (4.88 * Math.pow(10, -3) - (4.37 * Math.pow(10, -3) * ALB)) * (Tmed + 29);
        // Threshold soil water content (THE)
        THE = WP + 0.75 * (FC - WP);

        DRAINF();
        // Method EPpS calculates Daily Potential Evapotranspiration rate(ETp)
        ETpS();
        // Potential Soil Evaporation
        ESp = ETp * Math.exp(-0.7 * LAI);
        // Plant Transpiration
        EPp = ETp * (1 - Math.exp(-0.7 * LAI));
        // Actual Plant Transpiration rate
        EPa = ((Math.min(SWFAC1, SWFAC2)) * EPp);
        // S = 254 * ((100 / CN) - 1);
        RUNOFF();
        // INFILTRATION
        INF = POTINF - ROF;
        ESaS();
     // Updating values //
        TRAIN += RAIN;
        TIRR += IRR;
        TINF += INF;
        TESA += ESa;
        TEPA += EPa;
        TDRN += DRN;
        TROF += ROF;
        
    }

    // ------ Integration -------//

    public void integration() {
        SWC = SWC + (INF - ESa - EPa - DRN);
        /*
         * Updating ROF and SWC
         * if SWC exceeds ST
         */
        if (SWC > ST) {
            ROF = ROF + (SWC - ST);
            SWC = ST;
        } else if (SWC < 0) {
            SWC_ADJ -= SWC;
        }
        
        /*
         * STRESS method that finds the value of SWFAC1
         * which is then send to other classes.
         */
        STRESS();
        // calculate Water Table
        WTABLE = (SWC - FC) / (ST - FC) * DP * 10;
        // calculate Depth of Water Table
        DWT = (DP * 10) - WTABLE;

        if (WTABLE == 0)
            SWFAC2 = 1.0; /// minimum soil water stress
        else if (DWT > STRESS_DEPTH)
            SWFAC2 = 0.0; /// maximum stress
        else
            SWFAC2 = DWT / STRESS_DEPTH;
    }

    public String output(){
        String output = ("CN = "+CN+"\nDP = "+DP+"\nDRNp = "+DRNp+"\nFCp = "+FCp+"\nSTp = "+STp+"\nSWC = "+SWC+"\nWPp = "+WPp+"\nIRR = "+IRR);
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(output);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred...");
            e.printStackTrace();
        }
        return output;
    }

    public String summary(){
    	String summary = ("Total Rainfall = "+TRAIN+"\nTotal Irrigation = "+TIRR+"\nTotal Infiltration = "+TINF+"\nTotal Soil Evaporation = "+TESA+"\nTotal Plant Transpiration = "+TEPA
    			+"\nTotal Runoff = "+TROF+"\nTotal Vertical Drainage = "+TDRN);
    	return summary;
    }

    // ------ Methods of Rate Calculation ------ ///
    // Vertical Drainage //
    private void DRAINF() {
        DRN = (SWC - FC) * DRNp;
    }

    // potential evapotranspiration //
    private void ETpS() {
        final double f = 1.26; // Equilibrium Evaporation rate Coefficient
        ETp = f * EEQ;
    }

    // daily surface water runoff rate //
    private void RUNOFF() {
        S = 254 * ((100.0 / CN) - 1); // soil storage capacity
        if (POTINF > 0.2 * S)
            ROF = (Math.pow((POTINF - 0.2 * S), 2) / (POTINF + 0.8 * S));
        else
            ROF = 0;
    }

    // daily soil evaporation rate //
    private void ESaS() {
        double a;
        if (SWC < WP) {
            a = 0;
        } else if (SWC > FC) {
            a = 1;
        } else {
            a = (SWC - WP) / (FC - WP);
        }
        ESa = ESp * a;
    }

    // ------ Methods of Integration ------ ///
    private void STRESS() {
        if (SWC < WP)
            SWFAC1 = 0.0;
        else if (SWC > THE)
            SWFAC1 = 1.0;
        else
            SWFAC1 = (SWC - WP) / (THE - WP);
        SWFAC1 = Math.max(Math.min(SWFAC1, 1.0), 0.0);
    }

    public double getSWFAC1(){
        return SWFAC1;
    }

    public double getSWFAC2(){
        return SWFAC1;
    }
}