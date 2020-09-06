package org.zhadok.poe.controller.config.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.ControllerEventListener;
import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.util.LimitedSizeQueue;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

import net.java.games.input.Component;
import net.java.games.input.Event;

public class ConfigMappingUI implements Loggable, ControllerEventListener {

	private JLabel labelListeningStatus;

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}

	private JFrame frame;
	private final App app;

	private Config configCopy;
	private LimitedSizeQueue<StringBuilder> eventLog = new LimitedSizeQueue<StringBuilder>(4);
	private Map<Mapping, MappingRow> mapMappingToElement = new HashMap<>();

	private JPanel panelMappings;
	private JScrollPane scrollPaneMappings;
	private JPanel panelBottom;
	private JButton buttonSaveConfig;
	private JButton buttonResetConfig;
	private JPanel panelEast;
	private JButton buttonAddNewMapping;
	private JTextArea textAreaLastInputDetected;
	private JPanel panelEastTopButtons;
	private JButton buttonMapCharacterMovement;
	private JButton buttonMapMouseMovement;
	private JPanel panelEastBottom;
	private JPanel panelOffsetCharacterScreenCenterY;
	private JLabel labelOffsetCharacterScreenCenterY;
	private JNumberTextField textFieldOffsetCharacterScreenCenterY;
	private JPanel panelEastBottomCharacterMovementTitle;
	private JLabel labelCharacterMovementSettingsTitle;
	private JPanel panelCharacterMovementRadius;
	private JLabel labelCharacterMovementRadius;
	private JNumberTextField textFieldCharacterMovementRadius;
	private JPanel panelEastBottomMouseMovementTitle;
	private JLabel labelMouseMovementTitle;
	private JPanel panelMouseMovementSensitivity;
	private JLabel labelMouseMovementSensitivity;
	private JNumberTextField textFieldMouseMovementSensitivity;

	/**
	 * Create the application.
	 */
	public ConfigMappingUI(App app) {
		this.app = app;
		this.loadConfigCopy();
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("poe-controller");

		int paddingY = 100;
		int frameWidth = (int) (0.6 * Util.getScreenSize().width); 
		int paddingX = (Util.getScreenSize().width - frameWidth) / 2; 
		
		frame.setBounds(paddingX, paddingY, frameWidth, Util.getScreenSize().height - 2 * paddingY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		labelListeningStatus = new JLabel("Listening status");
		labelListeningStatus.setHorizontalAlignment(SwingConstants.CENTER);
		labelListeningStatus.setFont(new Font("Tahoma", Font.PLAIN, 22));
		frame.getContentPane().add(labelListeningStatus, BorderLayout.NORTH);

		panelMappings = new JPanel();
		JPanel panelMappingsContainer = new JPanel(new BorderLayout()); 
		scrollPaneMappings = new JScrollPane(panelMappingsContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//panelMappings.setLayout(new BoxLayout(panelMappings, BoxLayout.X_AXIS));
//		panelMappings.setLayout(new WrapLayout(FlowLayout.LEFT));
		GridBagLayout gridBagLayout = new GridBagLayout(); 
		panelMappings.setLayout(gridBagLayout);
		// Add this to the NORTH of the container, else it is centered in the scroll pane
		panelMappingsContainer.add(panelMappings, BorderLayout.NORTH); 
		
		frame.getContentPane().add(scrollPaneMappings, BorderLayout.CENTER);

		panelBottom = new JPanel();
		panelBottom.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new GridLayout(0, 2, 0, 0));

		buttonSaveConfig = new JButton("Save config");
		buttonSaveConfig.setFont(new Font("Tahoma", Font.PLAIN, 22));
		buttonSaveConfig.addActionListener((event) -> this.saveConfig());
		panelBottom.add(buttonSaveConfig);

		buttonResetConfig = new JButton("Reset config");
		buttonResetConfig.setFont(new Font("Tahoma", Font.PLAIN, 22));
		buttonResetConfig.addActionListener((event) -> this.resetConfig());
		panelBottom.add(buttonResetConfig);

		panelEast = new JPanel();
		panelEast.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(panelEast, BorderLayout.EAST);

		buttonAddNewMapping = new JButton("Add new mapping");
		buttonAddNewMapping.setFont(new Font("Tahoma", Font.PLAIN, 22));
		buttonAddNewMapping.addActionListener((event) -> this.addNewConfigMapping());
		panelEast.setLayout(new BorderLayout(0, 0));

		textAreaLastInputDetected = new JTextArea();
		textAreaLastInputDetected.setRows(5);
		textAreaLastInputDetected.setDisabledTextColor(Color.BLACK);
		textAreaLastInputDetected.setEnabled(false);

		panelEast.add(textAreaLastInputDetected);
		
		panelEastTopButtons = new JPanel();
		panelEast.add(panelEastTopButtons, BorderLayout.NORTH);
		panelEastTopButtons.setLayout(new GridLayout(0, 1, 0, 0));
		panelEastTopButtons.add(buttonAddNewMapping);
		
		buttonMapCharacterMovement = new JButton("Map character movement");
		buttonMapCharacterMovement.setFont(new Font("Tahoma", Font.PLAIN, 22));
		buttonMapCharacterMovement.addActionListener((event) -> startMapStickListener(MacroName.MacroCharacterMovement)); 
		panelEastTopButtons.add(buttonMapCharacterMovement);
		
		buttonMapMouseMovement = new JButton("Map mouse movement");
		buttonMapMouseMovement.setFont(new Font("Tahoma", Font.PLAIN, 22));
		buttonMapMouseMovement.addActionListener((event) -> startMapStickListener(MacroName.MacroMouseMovement)); 
		
		panelEastTopButtons.add(buttonMapMouseMovement);
		
		panelEastBottom = new JPanel();
		panelEast.add(panelEastBottom, BorderLayout.SOUTH);
		panelEastBottom.setLayout(new BoxLayout(panelEastBottom, BoxLayout.Y_AXIS));
		
		panelEastBottomCharacterMovementTitle = new JPanel();
		panelEastBottom.add(panelEastBottomCharacterMovementTitle);
		
		labelCharacterMovementSettingsTitle = new JLabel("Character movement settings");
		labelCharacterMovementSettingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelCharacterMovementSettingsTitle.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panelEastBottomCharacterMovementTitle.add(labelCharacterMovementSettingsTitle);
		
		panelCharacterMovementRadius = new JPanel();
		panelEastBottom.add(panelCharacterMovementRadius);
		panelCharacterMovementRadius.setLayout(new BorderLayout(0, 0));
		
		labelCharacterMovementRadius = new JLabel("<html>\r\nRadius of circle for character movement: <br>\r\n- Use a value between 0 and 0.5 <br>\r\n- If not sure, try 0.4 <br>\r\n- Hover over the input field for more information\r\n</html>");
		labelCharacterMovementRadius.setToolTipText("");
		panelCharacterMovementRadius.add(labelCharacterMovementRadius, BorderLayout.CENTER);
		
		textFieldCharacterMovementRadius = new JNumberTextField();
		textFieldCharacterMovementRadius.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldCharacterMovementRadius.setColumns(5);
		textFieldCharacterMovementRadius.setToolTipText("<html>\r\n" + 
				"- The value is used as a multiplier of min(screenWidth,screenHeight)<br>\r\n" + 
				"- For example, if your screen is 1920x1080 and the multiplier is 0.4 <br>\r\n" + 
				"the radius of the character movement circle will be 432 pixels<br>\r\n" + 
				"- A higher value will lead to a larger circle for the character movement\r\n" + 
				"</html>");
		textFieldCharacterMovementRadius.addNumberListener((number) -> {
			if (number == null) return; 
			log(1, "Setting character movement radius multiplier to " + number); 
			configCopy.getCharacterMovement().setMouseDistance_ScreenSizeMultiplier(number); 
		});
		
		panelCharacterMovementRadius.add(textFieldCharacterMovementRadius, BorderLayout.EAST);
		
		
		panelOffsetCharacterScreenCenterY = new JPanel();
		panelEastBottom.add(panelOffsetCharacterScreenCenterY);
		panelOffsetCharacterScreenCenterY.setLayout(new BorderLayout(0, 0));
		
		labelOffsetCharacterScreenCenterY = new JLabel("<html>\r\nDistance from middle of screen to middle of character (in pixels):<br>\r\n- Used as offset for the radius of the character movement<br>\r\n- Should be about half the height of your character\r\n</html>");
		panelOffsetCharacterScreenCenterY.add(labelOffsetCharacterScreenCenterY, BorderLayout.CENTER);
		
		textFieldOffsetCharacterScreenCenterY = new JNumberTextField();
		labelOffsetCharacterScreenCenterY.setLabelFor(textFieldOffsetCharacterScreenCenterY);
		textFieldOffsetCharacterScreenCenterY.setColumns(5);
		textFieldOffsetCharacterScreenCenterY.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldOffsetCharacterScreenCenterY.addNumberListener((number) -> {
			if (number == null) return; 
			log(1, "Setting mouse offset character to screen center y to " + number); 
			configCopy.getCharacterMovement().setMouseOffsetCharacterToScreenCenterY(number.intValue()); 
		});
		
		panelOffsetCharacterScreenCenterY.add(textFieldOffsetCharacterScreenCenterY, BorderLayout.EAST);
		
		panelEastBottomMouseMovementTitle = new JPanel();
		panelEastBottom.add(panelEastBottomMouseMovementTitle);
		panelEastBottomMouseMovementTitle.setLayout(new BorderLayout(0, 0));
		
		labelMouseMovementTitle = new JLabel("Mouse movement settings");
		labelMouseMovementTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelMouseMovementTitle.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panelEastBottomMouseMovementTitle.add(labelMouseMovementTitle, BorderLayout.CENTER);
		
		panelMouseMovementSensitivity = new JPanel();
		panelEastBottom.add(panelMouseMovementSensitivity);
		panelMouseMovementSensitivity.setLayout(new BorderLayout(0, 0));
		
		labelMouseMovementSensitivity = new JLabel("<html>\r\nMouse movement sensitivity: <br>\r\n- A higher number means the cursor moves faster\r\n</html>");
		panelMouseMovementSensitivity.add(labelMouseMovementSensitivity, BorderLayout.CENTER);
		
		textFieldMouseMovementSensitivity = new JNumberTextField();
		textFieldMouseMovementSensitivity.setColumns(5);
		textFieldMouseMovementSensitivity.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldMouseMovementSensitivity.addNumberListener((number) -> {
			if (number == null) return; 
			log(1, "Setting mouse movement sensitivity to " + number); 
			configCopy.getMouseMovement().setMouseMoveSensitivity(number); 
		});
		
		panelMouseMovementSensitivity.add(textFieldMouseMovementSensitivity, BorderLayout.EAST);
		
		this.resetConfigElements();
		
		frame.setVisible(true);
	}

	private void resetConfigElements() {
		this.panelMappings.removeAll();
		this.mapMappingToElement.clear(); 
//		panelMappings.revalidate();
//		panelMappings.repaint();
		this.createAllConfigElements();
		
		textFieldOffsetCharacterScreenCenterY.setText(configCopy.getCharacterMovement().getMouseOffsetCharacterToScreenCenterY() + "");
		textFieldCharacterMovementRadius.setText(configCopy.getCharacterMovement().getMouseDistance_ScreenSizeMultiplier() + ""); 
		textFieldMouseMovementSensitivity.setText(configCopy.getMouseMovement().getMouseMoveSensitivity() + ""); 
	}

	private void addConfigMappingElement(Mapping mapping, int mappingIndex) {
			MappingRow panelMapping = new MappingRow(mapping, new NextInputMappingHandler(mapping),
					(event) -> startOutputListener(mapping), (event) -> startMacroSelectionListener(mapping),
					(deleteEvent) -> removeConfigMapping(mapping));
	
			mapMappingToElement.put(mapping, panelMapping);
	//		panelMappings.add(panelMapping, mappingIndex);
			GridBagConstraints constraints = new GridBagConstraints(); 
			constraints.fill = GridBagConstraints.HORIZONTAL; // Don't stretch vertically
	//		constraints.anchor = GridBagConstraints.WEST;  // 
	//		constraints.gridwidth = 1; 
			constraints.gridx = 0; 
	//		constraints.gridy = 1; 
			panelMappings.add(panelMapping, constraints, mappingIndex); 
		}

	/**
	 * Convience method to call {@link addConfigMappingElement(Mapping mapping, int mappingIndex} with index -1,
	 * which appends the new element at the end of the list
	 * @param mapping
	 */
	private void addConfigMappingElement(Mapping mapping) {
		this.addConfigMappingElement(mapping, -1);
	}
	
	private void startOutputListener(Mapping mapping) {
		disableAllMappingButtons();
		log(1, "Listening for next keyboard or mouse input...");
		setStatusText("Listening for next keyboard input or mouse click...");
		
		NextOutputMappingHandler handler = new NextOutputMappingHandler();
		handler.setFinishHandler((newConfigAction) -> {
			mapping.setAction(newConfigAction);
			mapMappingToElement.get(mapping).updateTexts();
			frame.removeMouseListener(handler);
			frame.removeKeyListener(handler);
			enableAllMappingButtons();
			setStatusText("Changed mapping action: " + mapping.getAction().toStringUI());
		});
		frame.addMouseListener(handler);
		frame.addKeyListener(handler);
		frame.requestFocus();
	}

	private String startMacroSelectionListener(Mapping mapping) {
		String[] options = new String[] {
				MacroName.MacroCharacterMovement + " (x axis)",
				MacroName.MacroCharacterMovement + " (y axis)",
				MacroName.MacroMouseMovement + " (x axis)",
				MacroName.MacroMouseMovement + " (y axis)",
				MacroName.MacroExitProgram.name()
		}; 
		
		String input = (String) JOptionPane.showInputDialog(frame, "Select a macro...", "Macro selection",
				JOptionPane.INFORMATION_MESSAGE, null, 
				options, // Array of choices
				options[0]); // Initial choice
		return input;
	}
	
	private void startMapStickListener(MacroName macroName) {
		disableAllMappingButtons();
		String message = "Listening for next controller input..."; 
		log(1, message);
		setStatusText(message);
		
		List<String> eventNames = new ArrayList<>();
		int nEvents = 2; 
		app.setEventsToBeSkipped(1);
		app.registerForNextEvents(nEvents, (inputEvent) -> {
			log(1, "Received next event: " + inputEvent + " (analog=" + inputEvent.getComponent().isAnalog() + ")");
			eventNames.add(inputEvent.getComponent().getName()); 
			if (eventNames.size() >= nEvents) {
				
				// Check that exactly 2 events are passed and that they are different
				List<String> uniqueNames = eventNames.stream()
						.distinct()
						.collect(Collectors.toList()); 
				if (uniqueNames.size() != 2) {
					System.err.println("Did not receive two unique events, instead got " + uniqueNames); 
					enableAllMappingButtons();
					return; 
				}
				
				onMapMovementEventsRecorded(macroName, uniqueNames); 
			}
		});
	}
	
	private void onMapMovementEventsRecorded(MacroName macroName, List<String> uniqueNames) {
		// First, check if movement mappings already exist
		Mapping mappingX; 
		if ((mappingX = configCopy.getMovementMapping(macroName, "x")) == null) {
			mappingX = configCopy.addDefaultMovementMapping(macroName, "x"); 
			this.addConfigMappingElement(mappingX);
		}
		Mapping mappingY; 
		if ((mappingY = configCopy.getMovementMapping(macroName, "y")) == null) {
			mappingY = configCopy.addDefaultMovementMapping(macroName, "y"); 
			this.addConfigMappingElement(mappingY);
		}
		mappingX.setButtonValue(null);
		mappingY.setButtonValue(null);
		
		// Next, map recorded events to the mappings... "best guess"
		// Sample event names: "X Axis", "Y Axis", "Z axis", "Z rotation"
		try {
			configCopy.mapStickEventsToMovement(mappingX, mappingY, uniqueNames.get(0), uniqueNames.get(1));
		} catch (IllegalArgumentException e) {
			System.out.println("Unable to map events to x and y for character movement");
			e.printStackTrace();
		}
		mapMappingToElement.get(mappingX).updateTexts();
		mapMappingToElement.get(mappingY).updateTexts();
		enableAllMappingButtons();
		
		String message = "Successfully mapped input events to " + macroName.name().replace("Macro", ""); 
		log(1, message); 
		setStatusText(message);
	}
	
	/**
	 * 
	 * @param mapping
	 * @return Index of the removed component
	 */
	private int removeConfigMappingElement(Mapping mapping) {
		log(1, "removing mapping: " + mapping);
		JPanel panelMapping = this.mapMappingToElement.get(mapping);
		int mappingIndex = Arrays.asList(panelMappings.getComponents()).indexOf(panelMapping);

		this.panelMappings.remove(mapMappingToElement.get(mapping));
		this.mapMappingToElement.remove(mapping);

		return mappingIndex;
	}

	private void createAllConfigElements() {
		for (Mapping mapping : configCopy.getMapping()) {
			this.addConfigMappingElement(mapping);
		}
		this.validateConfig(); 
	}

	private void setStatusText(String text) {
		labelListeningStatus.setText(text);
	}

	private void setLastInputDetectedText(Event event) {
		Component comp = event.getComponent();

		StringBuilder eventText = new StringBuilder("Component name: ").append(comp.getName()).append("\n")
				.append("Component id: ").append(comp.getIdentifier()).append("\n").append("Component value: ")
				.append(event.getValue()).append("\n").append("Component isAnalog: ").append(comp.isAnalog());
		eventLog.add(0, eventText);

		final StringBuilder text = new StringBuilder("Last input detected:\n");
		eventLog.forEach((s) -> text.append(s).append("\n\n"));
		textAreaLastInputDetected.setText(text.toString());
	}

	private void addNewConfigMapping() {
		Mapping newMapping = new Mapping();
		this.configCopy.getMapping().add(newMapping);
		this.addConfigMappingElement(newMapping);
		this.validateConfig(); 
	}
	
	private void removeConfigMapping(Mapping mapping) {
		this.removeConfigMappingElement(mapping);
		this.configCopy.getMapping().remove(mapping);
		this.validateConfig(); 
	}

	private void loadConfigCopy() {
		this.configCopy = ConfigManager.getInstance().loadConfigFromFile(); 
	}

	/**
	 * Resets the config to the config found in the file
	 */
	private void resetConfig() {
		this.loadConfigCopy();
		this.resetConfigElements();
		this.enableAllMappingButtons();
	}

	/**
	 * Writes the changed config to file Resets the loaded config in ConfigManager
	 * Creates a new instance of ControllerMapping()
	 */
	public void saveConfig() {
		ConfigManager.getInstance().saveConfig(this.configCopy);
		ConfigManager.getInstance().resetLoadedConfig();
		app.resetControllerMappingListener();
	}

	private abstract class NextControllerMappingHandler implements ChangeListener {
		private boolean buttonPressed = false; // holds the last pressed state of the button
		protected final Mapping mapping;
		private final int nEvents; 
		
		public NextControllerMappingHandler(Mapping mapping, int nEvents) {
			this.mapping = mapping;
			this.nEvents = nEvents; 
		}

		@Override
		public void stateChanged(ChangeEvent event) {
			JButton button = (JButton) event.getSource();

			// if the current state differs from the previous state
			if (button.getModel().isPressed() != buttonPressed) {
				buttonPressed = button.getModel().isPressed();
				// On release
				if (buttonPressed == false) {
					disableAllMappingButtons();

					// Need to skip one event: The next event will be the release key of clicking
					// the button
					// (e.g. mouse released)
					log(1, "Registering for next event...");
					setStatusText("Listening for next input event...");
					app.setEventsToBeSkipped(1);
					app.registerForNextEvents(nEvents, (inputEvent) -> {
						log(1, "Received next event: " + inputEvent + " (analog=" + inputEvent.getComponent().isAnalog()
								+ ")");
						setStatusText("Received event: " + inputEvent.toString());
						onInputEventReceived(inputEvent);

						mapMappingToElement.get(mapping).updateTexts();
						enableAllMappingButtons();
					});
				}
			}
		}

		public abstract void onInputEventReceived(Event event);
	}

	public class NextInputMappingHandler extends NextControllerMappingHandler {

		public NextInputMappingHandler(Mapping mapping) {
			super(mapping, 1);
		}

		@Override
		public void onInputEventReceived(Event event) {
			if (event.getComponent().isAnalog() == false) {
				log(1, "Setting mapping button name to '" + event.getComponent().getName() + "'");
				this.mapping.setButtonName(event.getComponent().getName());
				this.mapping.setButtonValue(event.getValue());
			}
		}
	}

	private void enableAllMappingButtons() {
		this.mapMappingToElement.forEach((mapping, mappingRow) -> mappingRow.setButtonsEnabled(true));
		this.buttonAddNewMapping.setEnabled(true); 
		this.buttonMapCharacterMovement.setEnabled(true);
		this.buttonMapMouseMovement.setEnabled(true);
		
		this.validateConfig(); 
	}

	private void disableAllMappingButtons() {
		this.mapMappingToElement.forEach((mapping, mappingRow) -> mappingRow.setButtonsEnabled(false));
		this.buttonAddNewMapping.setEnabled(false); 
		this.buttonMapCharacterMovement.setEnabled(false);
		this.buttonMapMouseMovement.setEnabled(false);
	}
	
	private void validateConfig() {
		try {
			this.configCopy.sanityCheckMapping();
			
			if (this.buttonSaveConfig.isEnabled() == false) {
				setStatusText("Valid config."); 
				this.buttonSaveConfig.setEnabled(true);
			}
		} catch (Exception e) {
			String errorMessage = "Config is not valid: " + e.getMessage();  
			log(1, errorMessage);
			setStatusText(errorMessage);
			this.buttonSaveConfig.setEnabled(false);
		}
		
	}

	@Override
	public void handleEvent(Event event) {
		this.setLastInputDetectedText(event);
	}

}
