/*
 * REMAINING THINGS:
 *      if (maturityInt >= intot) then end simulation
 *      File Input
 *      Integration Method
 *      initialize return type int?
 */

import java.io.*;
import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;
import javax.swing.JTextArea;

public class Plant {
    private static final String filePath = "D:\\SemProject\\src\\inputs\\plant.inp";
    // Input variables
    private double EMP1, EMP2, fc, intot, LAI, nb, p1, PD, sla, tb, w, wc, wr;
    private int Lfmax, n, rm;

    // Assuming following as input as not details were given
    private double ROWSPC;

    // Calculated Variables
    private double dw; // change in total weight
    private double dwc; // change in canopy weight
    private double dwr; // change in root weight
    private double dwf; // change in grain weight
    private double di; // diff of mean and base temperature
    private double dLAI; // change in LAI
    private double dN; // change in number of leaves (n)

    // variables used in other calculations
    private double PT; // Growth rate reduction factor

    // self created variables
    private double wf;
    private double maturityInt;

    public Plant(double EMP1, double EMP2, double fc, double intot, double LAI, double nb, double p1, double PD,
            double sla, double tb, double w, double wc, double wr, int Lfmax, int n, int rm, double ROWSPC) {
        initialize(EMP1, EMP2, fc, intot, LAI, nb, p1, PD, sla, tb, w, wc, wr, Lfmax, n, rm, ROWSPC);
    }

    public Plant() {
       
    }

    private void initialize(double EMP1, double EMP2, double fc, double intot, double LAI, double nb, double p1,
            double PD,
            double sla, double tb, double w, double wc, double wr, double Lfmax, double n, double rm, double ROWSPC) {
        this.EMP1 = EMP1;
        this.EMP2 = EMP2;
        this.fc = fc;
        this.intot = intot;
        this.LAI = LAI;
        this.nb = nb;
        this.p1 = p1;
        this.PD = PD;
        this.sla = sla;
        this.tb = tb;
        this.w = w;
        this.wc = wc;
        this.wr = wr;
        this.Lfmax = (int) Lfmax;
        this.n = (int) n;
        this.rm = (int) rm;
        this.ROWSPC = (int) ROWSPC;
    }

    public int initialize(JTextArea txtField) {
    	int status=0;
        HashMap<String, Double> inputVariables = new HashMap<String, Double>();
        // initializing hashMap with input variable as keys
        inputVariables.put("EMP1", null);
        inputVariables.put("EMP2", null);
        inputVariables.put("fc", null);
        inputVariables.put("intot", null);
        inputVariables.put("LAI", null);
        inputVariables.put("nb", null);
        inputVariables.put("p1", null);
        inputVariables.put("PD", null);
        inputVariables.put("sla", null);
        inputVariables.put("tb", null);
        inputVariables.put("w", null);
        inputVariables.put("wc", null);
        inputVariables.put("wr", null);
        inputVariables.put("Lfmax", null);
        inputVariables.put("n", null);
        inputVariables.put("rm", null);
        inputVariables.put("ROWSPC", null);

        try {
            status =InputReader.populateInput(inputVariables,txtField ,filePath); // populating hashMap with input values

            initialize(inputVariables.get("EMP1"), inputVariables.get("EMP2"), inputVariables.get("fc"),
                    inputVariables.get("intot"), inputVariables.get("LAI"), inputVariables.get("nb"),
                    inputVariables.get("p1"), inputVariables.get("PD"), inputVariables.get("sla"),
                    inputVariables.get("tb"), inputVariables.get("w"), inputVariables.get("wc"),
                    inputVariables.get("wr"), inputVariables.get("Lfmax"), inputVariables.get("n"),
                    inputVariables.get("rm"), inputVariables.get("ROWSPC")); // settings the values of all variables

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
        	txtField.setText("Error: " + e.getMessage());
        }
        return status;
    }

    public double getIntot() {
        return intot;
    }

    public double getMaturityInt(){
        return maturityInt;
    }

    public double getLAI() {
        return LAI;
    }

    public void rateCalc(double TMIN, double TMAX, double SWFAC1, double SWFAC2, double SRAD) {
        PTS(TMIN, TMAX); // calculation for PT
        dw = PGS(SRAD) * PD; // calculation of total plant weight change

        /*
         * Determination of Plant Phase
         * Calculation of Weight Distribution (dwc, dwr, dwf) and dLAI based on phase
         */

        if (n < Lfmax) // number of leaves less than max then vegetative
            vegetativePhase(SWFAC1, SWFAC2);
        else // number of leaves more than max then reproductive
            reproductivePhase(TMIN, TMAX);
    }

    private void PTS(Double TMIN, Double TMAX) {
        PT = (1 - 0.0025 * (Math.pow(((0.25 * TMIN) + (0.75 * TMAX) - 26), 2)));
    }

    private void vegetativePhase(double SWFAC1, double SWFAC2) {
        /*
         * total plant weight change 'dw' is divided into 'dwc' (canopy) and 'dwr'
         * (roots)
         * no grain (dwf) is produced in vegetative phase
         */

        dwc = (dw * (fc / 100.0)); // change in canopy weight
        dwr = dw * (100.0 - (fc / 100.0)); // change in root weight
        dwf = 0; // grain weight is zero

        double a = Math.exp(EMP2 * (n - nb)); // value of 'a' used in dLAI calculation
        dN = rm * PT; // change in number of leaves(N) calculation

        // change in LAI calculation
        dLAI = (Math.min(SWFAC1, SWFAC2) * PT * PD * EMP1 * dN * (a / (a + 1))); // SWFAC with lower value will be the
        // limiting factor
    }

    private void reproductivePhase(double TMIN, double TMAX) {
        /*
         * total plant weight change 'dw' is directly converted into grain weight 'dwf'
         * 'dwc' (canopy) and 'dwr' (roots) will be zero
         */

        dwf = dw; // calculation of grain weight
        dwc = 0; // canopy weight is zero
        dwr = 0; // root weight is zero
        dN = 0;
        double Tmean = (TMAX + TMIN) / 2.0; // calculation of mean temperature
        di = Math.abs(Tmean - tb); // difference of mean and base temperature

        dLAI = (-PD) * (di) * (p1) * (sla); // calculation of decrease (change) in LAI
    }

    // Methods which are part of calculation methods/formulas
    private double PGS(double SRAD) {
        double Y1 = ((1.5 - 0.786) * (Math.pow((Math.pow((ROWSPC * 0.01), 2) * (PD)), 0.1)));
        return (SRAD / PD) * (1 - Math.exp(-Y1 * LAI));
    }

    public void integration() {
        w += dw;
        n += dN;
        LAI += dLAI;
        wc += dwc;
        wr += dwr;
        wf += dwf;
        maturityInt += di;
    }

    public String output() {
        String output = ("EMP1 = "+EMP1+"\nEMP2 = "+EMP2+"\nfc = "+fc+"\nintot = "+intot+"\nLAI = "+LAI+"\nLfmax = "+Lfmax
        		+"\nn = "+n+"\nnb = "+nb+"\np1 = "+ p1+"\nPD = "+ PD+"\nrm = "+rm+"\nsla = " +sla +"\ntb = "+tb+"\nw = "+ w+"\nwc = "+wc+"\nwr = "+wr+"\nROWSPC = "+ROWSPC);
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
        String summary = ("No. of Leaves = "+n+"\nPlant Weight = "+w+"\nCanopy Weight = "+wc
        		+"\nRoot Weight = "+wr+"\nGrain Weight = "+wf+"\nLeaf Area Index = "+LAI);
        return summary;}
}