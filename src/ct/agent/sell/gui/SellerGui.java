package ct.agent.sell.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ct.agent.sell.SellerAgentGui;
import ct.model.car.Car;
import ct.model.car.CarBody;
import ct.model.car.EngineType;

public class SellerGui extends JFrame {
	private static final long serialVersionUID = 245652286836026278L;
	private SellerAgentGui sellerAgent;
	private JTextField brandField, modelField, engineCapacity, priceField, additionalFeesDescription, additionalFeesCost;
	private JComboBox carBodyList, engineTypeList, productionYearList;
	
	
	public SellerGui(SellerAgentGui agent) {
		super(agent.getLocalName());

		this.initializeFields(agent);
		this.initializeGui();
	}
	

	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
	private void initializeFields(SellerAgentGui agent) {
		this.sellerAgent = agent;
		
		this.brandField = new JTextField(15);
		this.modelField = new JTextField(15);
		this.carBodyList = new JComboBox<CarBody>(CarBody.values());
		this.engineTypeList = new JComboBox<EngineType>(EngineType.values());
		this.engineCapacity = new JTextField(15);
		this.productionYearList = new JComboBox<Integer>(productionYears());
		this.priceField = new JTextField(15);
		this.additionalFeesDescription = new JTextField(15);
		this.additionalFeesCost = new JTextField(15);
	}
	private void initializeGui() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(9, 2));
		
		addComponentsGui(panel, FieldName.Marka, brandField);
		addComponentsGui(panel, FieldName.Model, modelField);
		addComponentsGui(panel, FieldName.Typ_nadwozia, carBodyList);
		addComponentsGui(panel, FieldName.Typ_silnika, engineTypeList);
		addComponentsGui(panel, FieldName.Pojemnosc_silnika, engineCapacity);
		addComponentsGui(panel, FieldName.Rok_produkcji, productionYearList);
		addComponentsGui(panel, FieldName.Cena, priceField);
		addComponentsGui(panel, FieldName.Koszty_dodatkowe_opis, additionalFeesDescription);
		addComponentsGui(panel, FieldName.Koszty_dodatkowe_cena, additionalFeesCost);
		
		getContentPane().add(panel, BorderLayout.CENTER);		
		JButton addButton = new JButton("Dodaj do katalogu");
		addButton.addActionListener(actionListenerForAddButton());		
		panel = new JPanel();
		panel.add(addButton);
		
		getContentPane().add(panel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sellerAgent.doDelete();
			}
		});

		setResizable(false);
	}
	private ActionListener actionListenerForAddButton() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String brand = brandField.getText().trim();
					String model = modelField.getText().trim();
					String engineType = String.valueOf(engineTypeList.getSelectedItem());
					CarBody carBody = CarBody.valueOf(String.valueOf(carBodyList.getSelectedItem()));
					Integer productionYear = Integer.valueOf((Integer)productionYearList.getSelectedItem());
					Float engineCap = Float.parseFloat(engineCapacity.getText());
					Float price = Float.parseFloat(priceField.getText());
					String additionalDescription = additionalFeesDescription.getText().trim();
					Float additionalPrice = Float.parseFloat(additionalFeesCost.getText());
					
					HashMap<String, Float> additionals = new HashMap<String, Float>();
					additionals.put(additionalDescription, additionalPrice);
					
					Car car = new Car(engineCap, engineType, carBody, brand, model, productionYear, price);
					car.setAdditionalFees(additionals);
					
					String carName = brand.concat(" ").concat(model);
					sellerAgent.updateCatalogue(car, carName);
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(SellerGui.this, "Niepoprawna wartosc. " + e.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};
	}
	private void addComponentsGui(JPanel panel, FieldName fieldName, Component component) {
		panel.add(new JLabel(fieldName.getName(":")));
		panel.add(component);
	}
	private Integer[] productionYears() {
		int currentYear = 2018;
		int baseYear = 1900;
		int counter = currentYear - baseYear;
		Integer[] list = new Integer[counter + 1];
		
		for(int i = counter; i >= 0; i--) {
			list[i] = new Integer(baseYear + i);
		}
		
		Arrays.sort(list, Collections.reverseOrder());
		return list;
	}
	private enum FieldName {
		Marka("Marka"), Model("Model"), 
		Typ_nadwozia("Typ nadwozia"), Typ_silnika("Typ silnika"), 
		Pojemnosc_silnika("Pojemnosc silnika"), Rok_produkcji("Rok produkcji"), 
		Cena("Cena $"), Koszty_dodatkowe_opis("Koszty dodatkowe(opis)"),
		Koszty_dodatkowe_cena("Koszty dodatkowe(cena $)");
		
		private String name;
		FieldName(String name) {
			this.name= name;
		}
		
		public String getName() {
			return name;
		}
		public String getName(String addText) {
			return name.concat(addText);
		}
	}
}