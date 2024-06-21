import javax.swing.JTextArea;
public class Simulation {
    public Plant plant;
    public SoilWater soilWater;
    public Weather weather;

    public Simulation() {
        plant = new Plant();
        soilWater = new SoilWater();
        weather = new Weather();
    }

    public void rateCalc() {
        soilWater.rateCalc(plant.getLAI(), weather.getSRAD(), weather.getRAIN(), weather.getTMAX(), weather.getTMIN());
        plant.rateCalc(weather.getTMIN(), weather.getTMAX(), soilWater.getSWFAC1(), soilWater.getSWFAC2(), weather.getSRAD());
    }

    public int[] initialize(JTextArea txtfield) {
    	int [] status = new int[3];
        status[0] = plant.initialize(txtfield);
        status[1] = soilWater.initialize(txtfield);
        status[2] = weather.initialize(txtfield);
        return status;
    }

    public void integration() {
        plant.integration();
        soilWater.integration();
    }

    public void output() {
        plant.output();
        soilWater.output(); 
    }


   }


