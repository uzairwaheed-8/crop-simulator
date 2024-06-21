import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;

public class SimulationGUI {

	private JFrame frame;
	private JFrame frame1;
	private JFrame frame2;
	private JTextField textField;
	private static Simulation sim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulationGUI window = new SimulationGUI();
					window.frame.setVisible(true);
					SimulationGUI window1 = new SimulationGUI();
					window1.frame1.setVisible(false);
					SimulationGUI window2 = new SimulationGUI();
					window2.frame2.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		sim = new Simulation(); // initialization

		
	}

	/**
	 * Create the application.
	 */
	public SimulationGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Plant Simulation");
		
		frame1 = new JFrame();
		frame1.setBounds(100, 100, 800, 600);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);
		frame1.setTitle("Plant Simulation");
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 200, 764, 350);
		frame.getContentPane().add(textArea);

		
		JButton btnNewButton = new JButton("Check");

		btnNewButton.setBounds(238, 166, 89, 23);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Plant");
		lblNewLabel.setBounds(35, 11, 59, 19);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("SoilWater");
		lblNewLabel_1.setBounds(35, 41, 72, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Weather");
		lblNewLabel_2.setBounds(35, 67, 59, 14);
		frame.getContentPane().add(lblNewLabel_2);

		JCheckBox chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setEnabled(false);
		
		chckbxNewCheckBox.setBounds(105, 11, 97, 23);
		frame.getContentPane().add(chckbxNewCheckBox);

		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("");
		chckbxNewCheckBox_1.setEnabled(false);
		chckbxNewCheckBox_1.setBounds(105, 62, 97, 23);
		frame.getContentPane().add(chckbxNewCheckBox_1);

		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("");
		chckbxNewCheckBox_2.setEnabled(false);
		chckbxNewCheckBox_2.setBounds(105, 37, 97, 23);
		frame.getContentPane().add(chckbxNewCheckBox_2);

		// Second GUI
		JButton btnNewButton_3 = new JButton("Back");
		btnNewButton_3.setBounds(10, 527, 89, 23);
		frame1.getContentPane().add(btnNewButton_3);

		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame1.setVisible(false);
				frame.toFront();
				frame.setVisible(true);
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Plant Summary");
		lblNewLabel_3.setBounds(71, 11, 105, 14);
		frame1.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("SoilWater Summary");
		lblNewLabel_4.setBounds(335, 11, 116, 14);
		frame1.getContentPane().add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Weather Summary");
		lblNewLabel_5.setBounds(579, 11, 124, 14);
		frame1.getContentPane().add(lblNewLabel_5);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setBounds(24, 36, 215, 392);
		frame1.getContentPane().add(textArea_1);

		JTextArea textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		textArea_2.setBounds(286, 36, 215, 392);
		frame1.getContentPane().add(textArea_2);

		JTextArea textArea_3 = new JTextArea();
		textArea_3.setEditable(false);
		textArea_3.setBounds(547, 36, 215, 392);
		frame1.getContentPane().add(textArea_3);
		
		
		
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(213, 478, 409, 14);
		frame1.getContentPane().add(lblNewLabel_6);
		
		//3rd GUI
		frame2 = new JFrame();
		frame2.setResizable(false);
		frame2.setBounds(100, 100, 400, 200);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.getContentPane().setLayout(null);
		frame2.setTitle("Confirm close");
		JLabel lblNewLabel_7 = new JLabel("Do you want to end simulation?");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(10, 35, 348, 14);
		frame2.getContentPane().add(lblNewLabel_7);
		
		JButton btnNewButton_6= new JButton("Yes");
		btnNewButton_6.setBounds(65, 80, 89, 23);
		frame2.getContentPane().add(btnNewButton_6);
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JButton btnNewButton_7 = new JButton("No");
		btnNewButton_7.setBounds(229, 80, 89, 23);
		frame2.getContentPane().add(btnNewButton_7);
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame1.setEnabled(true);
				frame2.setVisible(false);
				
			}
		});
		
		JButton btnNewButton_4 = new JButton("Finish");
		btnNewButton_4.setBounds(685, 527, 89, 23);
		frame1.getContentPane().add(btnNewButton_4);

		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame1.setEnabled(false);
				frame2.setVisible(true);
			}
		});

		JButton btnNewButton_5 = new JButton("Next Day");
		btnNewButton_5.setBounds(579, 527, 89, 23);
		frame1.getContentPane().add(btnNewButton_5);

		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.setVisible(false);
				frame1.toFront();
				frame1.setVisible(true);

				if (sim.plant.getMaturityInt() >= sim.plant.getIntot()) {
					btnNewButton_5.setEnabled(false);
					lblNewLabel_6.setText("Simulation Done!");
					return;
				}

				sim.rateCalc();
				sim.integration();
				sim.output();
				textArea_1.setText(sim.plant.summary());
				textArea_2.setText(sim.soilWater.summary());
				textArea_3.setText(sim.weather.summary());
			}
		});

		JButton btnNewButton_2 = new JButton("Next");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.setVisible(false);
				frame1.toFront();
				frame1.setVisible(true);

				sim.rateCalc();
				sim.integration();
				sim.output();
				textArea_1.setText(sim.plant.summary());
				textArea_2.setText(sim.soilWater.summary());
				textArea_3.setText(sim.weather.summary());
			}
		});

		btnNewButton_2.setBounds(430, 166, 89, 23);
		frame.getContentPane().add(btnNewButton_2);
		btnNewButton_2.setEnabled(false);
		
		

		// check button
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnNewButton_2.setEnabled(true);
				int[] status = sim.initialize(textArea);
				String[] fileNames = { "plant.inp", "soilWater.inp", "weather.inp" };
				JCheckBox[] jBox = { chckbxNewCheckBox, chckbxNewCheckBox_1, chckbxNewCheckBox_2 };
				for (int i = 0; i < 3; i++) {
					if (status[i] == 0) {
						jBox[i].setSelected(true);
						textArea.append(fileNames[i] + " has been successfuly loaded!\n");
					} else if (status[i] == 2) {
						btnNewButton_2.setEnabled(false);
					}
					else
						jBox[i].setSelected(false);

				}

			}
		});
		

	}
}
