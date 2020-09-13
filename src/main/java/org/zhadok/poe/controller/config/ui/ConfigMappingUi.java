package org.zhadok.poe.controller.config.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.ControllerEventHandler;
import org.zhadok.poe.controller.ControllerEventListener;
import org.zhadok.poe.controller.TemporaryControllerEventListener;
import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.config.pojo.MappingKey;
import org.zhadok.poe.controller.util.LimitedSizeQueue;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Event;

public class ConfigMappingUi implements Loggable, ControllerEventListener {


	@Override
	public int getVerbosity() {
		return App.verbosity;
	}

	private final ControllerEventHandler controllerEventHandler;
	private Config configCopy;
	private LimitedSizeQueue<StringBuilder> eventLog = new LimitedSizeQueue<StringBuilder>(4);
	private Map<Mapping, MappingRow> mapMappingToElement = new HashMap<>();
	private final ConfigMappingUiSettings uiSettings; 
	
	private JFrame frame;
	private JPanel panelMappings;
	private JScrollPane scrollPaneMappings;
	private JPanel panelBottom;
	private JButton buttonSaveConfig;
	private JButton buttonResetConfig;
	private JButton buttonRemoveAllMappings; 
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
	private JPanel panelMouseMovementStickThreshold;
	private JLabel labelMouseMovementStickThreshold;
	private JNumberTextField textFieldMouseMovementStickThreshold;

	public static final Color COLOR_PANEL_BACKGROUND = new Color(240, 248, 255); 
	public static final Color COLOR_BUTTON_BACKGROUND = new Color(240, 250, 255);
	public static final int TEXT_PADDING = 7; 
	public static final Font FONT_BIG_BUTTONS = new Font("Tahoma", Font.PLAIN, 22);
	private ConfigMappingUiTop panelTop;
	
	/**
	 * Create the application.
	 */
	public ConfigMappingUi(ControllerEventHandler controllerEventHandler) {
		this(controllerEventHandler, new ConfigMappingUiSettings()); 
	}
	
	public ConfigMappingUi(ControllerEventHandler controllerEventHandler, ConfigMappingUiSettings uiSettings) {
		this.controllerEventHandler = controllerEventHandler;
		this.uiSettings = uiSettings; 
		this.loadConfigCopy();
	}
	

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint 
	 */
	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			log(1, "Warning, while setting UI look and feel: " + e.getMessage());
		}
		
		frame = new JFrame();
		frame.setTitle("poe-controller (v" + Util.getPomVersion() + ")");
		frame.setFocusTraversalKeysEnabled(false); // Otherwise "Tab" does not get picked up when listening
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(COLOR_PANEL_BACKGROUND); 
		
		URL iconUrl = getClass().getResource("/img/icon-controller.png"); 
		frame.setIconImage(new ImageIcon(iconUrl).getImage());
		
		frame.setBounds(uiSettings.getUiX(), uiSettings.getUiY(), uiSettings.getUiWidth(), uiSettings.getUiHeight());
		frame.setExtendedState(uiSettings.getUiExtendedState());
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				onResize(componentEvent); 
		    }
			public void componentMoved(ComponentEvent componentEvent) {
				onResize(componentEvent); 
			}
		}); 
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	        	controllerEventHandler.triggerApplicationExit();
	        }
	    });
		
		panelMappings = new JPanel();
		panelMappings.setBackground(COLOR_PANEL_BACKGROUND);
		JPanel panelMappingsContainer = new JPanel(new BorderLayout()); 
		scrollPaneMappings = new JScrollPane(panelMappingsContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//panelMappings.setLayout(new BoxLayout(panelMappings, BoxLayout.X_AXIS));
//		panelMappings.setLayout(new WrapLayout(FlowLayout.LEFT));
		GridBagLayout gridBagLayout = new GridBagLayout(); 
		panelMappings.setLayout(gridBagLayout);
		// Add this to the NORTH of the container, else it is centered in the scroll pane
		panelMappingsContainer.add(panelMappings, BorderLayout.NORTH); 
		panelMappingsContainer.setBackground(COLOR_PANEL_BACKGROUND);
		
		frame.getContentPane().add(scrollPaneMappings, BorderLayout.CENTER);

		panelBottom = new JPanel();
		panelBottom.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new GridLayout(0, 3, 0, 0));
		panelBottom.setBackground(COLOR_PANEL_BACKGROUND);
		
		buttonSaveConfig = new JButtonWithIcon("/img/icon-save.png", "Save config", 
				FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonSaveConfig.addActionListener((event) -> this.saveConfig());
		panelBottom.add(buttonSaveConfig);

		buttonResetConfig = new JButtonWithIcon("/img/icon-delete.png", "Reset config", 
				FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonResetConfig.addActionListener((event) -> this.resetConfig());
		panelBottom.add(buttonResetConfig);

		buttonRemoveAllMappings = new JButtonWithIcon("/img/icon-delete-all.png", "Delete all mappings", 
				FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonRemoveAllMappings.addActionListener((event) -> this.removeAllMappings());
		panelBottom.add(buttonRemoveAllMappings);
		
		panelEast = new JPanel();
		panelEast.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(panelEast, BorderLayout.EAST);
		panelEast.setBackground(COLOR_PANEL_BACKGROUND);
		
		buttonAddNewMapping = new JButtonWithIcon("/img/icon-add.png", "Add new mapping", FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonAddNewMapping.addActionListener((event) -> this.addNewConfigMapping());
		buttonAddNewMapping.setBackground(COLOR_BUTTON_BACKGROUND);
		buttonAddNewMapping.setOpaque(true);
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
		
		buttonMapCharacterMovement = new JButtonWithIcon("/img/icon-controller.png", "Map character movement", 
				FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonMapCharacterMovement.addActionListener((event) -> startMapStickListener(MacroName.MacroCharacterMovement)); 
		panelEastTopButtons.add(buttonMapCharacterMovement);
		
		buttonMapMouseMovement = new JButtonWithIcon("/img/icon-controller.png", "Map mouse movement", 
				FONT_BIG_BUTTONS, TEXT_PADDING, 1);
		buttonMapMouseMovement.addActionListener((event) -> startMapStickListener(MacroName.MacroMouseMovement)); 
		
		panelEastTopButtons.add(buttonMapMouseMovement);
		
		panelEastBottom = new JPanel();
		panelEast.add(panelEastBottom, BorderLayout.SOUTH);
		panelEastBottom.setLayout(new BoxLayout(panelEastBottom, BoxLayout.Y_AXIS));
		panelEastBottom.setBackground(COLOR_PANEL_BACKGROUND);
		
		panelEastBottomCharacterMovementTitle = new JPanel();
		panelEastBottom.add(panelEastBottomCharacterMovementTitle);
		
		labelCharacterMovementSettingsTitle = new JLabel("Character movement settings");
		labelCharacterMovementSettingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelCharacterMovementSettingsTitle.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panelEastBottomCharacterMovementTitle.add(labelCharacterMovementSettingsTitle);
		panelEastBottomCharacterMovementTitle.setBackground(COLOR_PANEL_BACKGROUND);
		
		panelCharacterMovementRadius = new JPanel();
		panelEastBottom.add(panelCharacterMovementRadius);
		panelCharacterMovementRadius.setLayout(new BorderLayout(0, 0));
		panelCharacterMovementRadius.setBackground(COLOR_PANEL_BACKGROUND);
		
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
		panelOffsetCharacterScreenCenterY.setBackground(COLOR_PANEL_BACKGROUND);
		
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
		panelEastBottomMouseMovementTitle.setBackground(COLOR_PANEL_BACKGROUND);
		
		labelMouseMovementTitle = new JLabel("Mouse movement settings");
		labelMouseMovementTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelMouseMovementTitle.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panelEastBottomMouseMovementTitle.add(labelMouseMovementTitle, BorderLayout.CENTER);
		
		panelMouseMovementSensitivity = new JPanel();
		panelEastBottom.add(panelMouseMovementSensitivity);
		panelMouseMovementSensitivity.setLayout(new BorderLayout(0, 0));
		panelMouseMovementSensitivity.setBackground(COLOR_PANEL_BACKGROUND);
		
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
		
		panelMouseMovementStickThreshold = new JPanel();
		panelEastBottom.add(panelMouseMovementStickThreshold);
		panelMouseMovementStickThreshold.setLayout(new BorderLayout(0, 0));
		panelMouseMovementStickThreshold.setBackground(COLOR_PANEL_BACKGROUND);
		
		labelMouseMovementStickThreshold = new JLabel("<html>\r\nMouse movement stick threshold: <br>\r\n- Set a value between 0 and 1<br>\r\n- The mouse will be moved when the stick value is above this value<br>\r\n- If your mouse moves without using the mapped stick, try setting this value higher\r\n</html>");
		panelMouseMovementStickThreshold.add(labelMouseMovementStickThreshold, BorderLayout.CENTER);
		
		textFieldMouseMovementStickThreshold = new JNumberTextField();
		textFieldMouseMovementStickThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldMouseMovementStickThreshold.setColumns(5);
		textFieldMouseMovementStickThreshold.addNumberListener((number) -> {
			if (number == null) return; 
			log(1, "Setting mouse movement stick threshold to " + number); 
			configCopy.getMouseMovement().setStickThreshold(number); 
		});
		
		panelMouseMovementStickThreshold.add(textFieldMouseMovementStickThreshold, BorderLayout.EAST);
		
		panelTop = new ConfigMappingUiTop(); 
		frame.getContentPane().add(panelTop, BorderLayout.NORTH);
		
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
		textFieldMouseMovementStickThreshold.setText(configCopy.getMouseMovement().getStickThreshold() + ""); 
	}

	private void addConfigMappingElement(Mapping mapping, int mappingIndex) {
			MappingRow panelMapping = new MappingRow(mapping, new NextControllerMappingHandler(mapping, 2),
					(event) -> startOutputListener(mapping),
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
		
		NextOutputMappingHandler handler = new NextOutputMappingHandler(frame);
		handler.setFinishHandler((newConfigAction) -> {
			if (newConfigAction != null) {
				mapping.setAction(newConfigAction);
				mapMappingToElement.get(mapping).updateTexts();
				setStatusText("Changed mapping action: " + mapping.getAction().toStringUI());
			} else {
				setStatusText("Unable to record mapping action or recording was canceled."); 
			}
			Toolkit.getDefaultToolkit().removeAWTEventListener(handler);
			frame.removeKeyListener(handler);
			enableAllMappingButtons();
		});
		// Global mouse listener: 
		// https://stackoverflow.com/questions/11502619/see-when-a-mousebutton-is-clicked-without-adding-mouselisteners-to-all-component
	    Toolkit.getDefaultToolkit().addAWTEventListener(handler, AWTEvent.MOUSE_EVENT_MASK);
		frame.addKeyListener(handler);
		frame.requestFocus();
	}

	private void startMapStickListener(MacroName macroName) {
		disableAllMappingButtons();
		String message = "Listening for controller joystick... Move the joystick around in circles"; 
		log(1, message);
		setStatusText(message);
		
		// Mappings: 
		// x => "X Axis" or "X-Achse"
		Map<Component.Identifier.Axis,String> mapIdentifierToComponentName = new HashMap<>(); 
		Map<Component.Identifier.Axis,Integer> mapIdentifierNumberEvents = new HashMap<>(); 
		
		final int nEventsToRecord = 100; 
		controllerEventHandler.registerTemporaryListener(new TemporaryControllerEventListener(1, nEventsToRecord, false,
				(inputEvent, nEventsRecorded, isFinished) -> {
					
			log(2, "Received next event: " + inputEvent + " (analog=" + inputEvent.getComponent().isAnalog() + ")");
			log(2, inputEvent.getComponent().getIdentifier().getClass().getName());
					
			// inputEvent.getComponent().getName() ==> Returns "X Axis", "X Achse", "X-Axis", "X Rotation"
			// inputEvent.getComponent().getIdentifier() can check if instanceof Axis, and name of identifier is better
			if (inputEvent.getComponent().getIdentifier() instanceof Component.Identifier.Axis &&
				inputEvent.getComponent().isAnalog() == true) { 
				
				Axis axis = (Axis) inputEvent.getComponent().getIdentifier(); 
				
				mapIdentifierToComponentName.put(axis, inputEvent.getComponent().getName()); 
				mapIdentifierNumberEvents.compute(axis, (k, v) -> (v == null) ? 1 : v+1); 
			}		
			
			if (isFinished) {
				// Remove "dirty" events that were only recorded up to N times
				// For example, Stadia controller gives one "rz" event when starting the application
				List<Axis> axesToRemove = mapIdentifierNumberEvents.entrySet().stream()
						.filter(entry -> entry.getValue() <= 2)
						.map(entry -> entry.getKey())
						.collect(Collectors.toList());
				if (axesToRemove.isEmpty() == false) {
					log(1, "Removing axesToRemove=" + axesToRemove + ", n events recorded " + mapIdentifierNumberEvents); 
				}
				axesToRemove.forEach(axisToRemove -> mapIdentifierToComponentName.remove(axisToRemove));
				
				// Check that exactly 2 different events are passed
				if (mapIdentifierToComponentName.keySet().size() != 2) {
					String errorMessage = "Error mapping joystick: Did not receive exactly two unique events, " +
							"instead got " + mapIdentifierToComponentName + ", n events recorded: " + 
							mapIdentifierNumberEvents; 
					log(1, errorMessage); 
					setStatusText(errorMessage);
					enableAllMappingButtons();
					return; 
				}
				onMapMovementEventsRecorded(macroName, mapIdentifierToComponentName); 
			} else {
				setStatusText((nEventsToRecord - nEventsRecorded) + " events remaining, keep moving the joystick...");
			}
					
		}));
	}
	
	private void onMapMovementEventsRecorded(MacroName macroName, Map<Axis,String> mapIdentifierToComponentName) {
		log(1, "Mapping joystick " + mapIdentifierToComponentName + "  to " + macroName); 
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
		
		// Next, map recorded events to the mappings... "best guess"
		// Sample event names: "X Axis", "Y Axis", "Z axis", "Z rotation"
		try {
			configCopy.mapStickEventsToMovement(mappingX, mappingY, mapIdentifierToComponentName);
		} catch (IllegalArgumentException e) {
			System.out.println("Unable to map events to x and y for character movement");
			e.printStackTrace();
		}
		mapMappingToElement.get(mappingX).updateTexts();
		mapMappingToElement.get(mappingY).updateTexts();
		
		String message = "Successfully mapped input events to " + macroName.name().replace("Macro", ""); 
		log(1, message); 
		setStatusText(message);
		
		enableAllMappingButtons();
	}
	
	/**
	 * 
	 * @param mapping
	 * @return Index of the removed component
	 */
	private int removeConfigMappingElement(Mapping mapping) {
		log(1, "Removing mapping: " + mapping);
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
		panelTop.setStatusText(text);
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


	private void removeAllMappings() {
		// 0=ok, 2=cancel
		int confirmation = JOptionPane.showConfirmDialog(frame, "This will delete all mappings. Are you sure?",
				"Delete all mappings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE); 
		if (confirmation == 0) {
			log(1, "Removing all mappings..."); 
			this.configCopy.getMapping().forEach(mapping -> {
				removeConfigMappingElement(mapping);
			});
			this.configCopy.getMapping().clear();
			this.frame.getContentPane().revalidate();
			this.frame.getContentPane().repaint();
		}
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
		setStatusText("Config saved. New mapping activated");
		controllerEventHandler.resetControllerMappingListener();
	}

	/**
	 * Use to record the next digital (non-analog) controller input
	 * Example: Button 1, Button 2
	 */
	private class NextControllerMappingHandler implements ChangeListener {
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
					// THIS IS INCONSISTENT per platform
					log(1, "Registering for next event...");
					setStatusText("Listening for next input event...");
					List<Event> inputEvents = new ArrayList<>(); 
					controllerEventHandler.registerTemporaryListener(new TemporaryControllerEventListener(0, nEvents, true, 
							(inputEvent, nEventsRecorded, isFinished) -> {
								
						log(1, "Received next event: " + inputEvent + " (analog=" + inputEvent.getComponent().isAnalog()
								+ ")");
						setStatusText("Received event: " + inputEvent.toString());
						inputEvents.add(inputEvent); 
						
						if (isFinished) {
							onInputEventsReceived(inputEvents);

							if (mapMappingToElement.get(mapping) != null) {
								mapMappingToElement.get(mapping).updateTexts();
							}
							enableAllMappingButtons();
						}
					}));
				}
			}
		}
		
		public void onInputEventsReceived(List<Event> events) {
			// Pick the first event which has a value > 0 
			// On some machines, the first event is left mouse button released (==> value 0) 
			for (Event event : events) {
				if (event.getComponent().isAnalog() == false && event.getValue() > 0) {
					log(1, "Setting mapping button name to '" + event.getComponent().getName() + "'");
					this.mapping.setMappingKey(new MappingKey(event)); 
					return;
				}
			}
		}
	}


	private void enableAllMappingButtons() {
		this.mapMappingToElement.forEach((mapping, mappingRow) -> mappingRow.setButtonsEnabled(true));
		this.buttonAddNewMapping.setEnabled(true); 
		this.buttonMapCharacterMovement.setEnabled(true);
		this.buttonMapMouseMovement.setEnabled(true);
		this.panelTop.hideCancelButton();
		
		this.validateConfig(); 
	}

	private void disableAllMappingButtons() {
		this.mapMappingToElement.forEach((mapping, mappingRow) -> mappingRow.setButtonsEnabled(false));
		this.buttonAddNewMapping.setEnabled(false); 
		this.buttonMapCharacterMovement.setEnabled(false);
		this.buttonMapMouseMovement.setEnabled(false);
		this.panelTop.showCancelButton(event -> onCancelListen(event)); 
	}
	
	private void onCancelListen(ActionEvent event) {
		log(1, "Cancelling listen for next input/output.");
		this.enableAllMappingButtons();
		this.controllerEventHandler.unregisterTemporaryListener();
	}
	
	private void onResize(ComponentEvent componentEvent) {
		log(2, "Resizing frame and storing preferences: width=" + frame.getWidth() + ", height=" + 
				frame.getHeight() + ", x=" + frame.getX() + ", y=" + frame.getY() + ", extended state=" + 
				frame.getExtendedState()); 
		
		uiSettings.setUiWidth(frame.getWidth());
		uiSettings.setUiHeight(frame.getHeight());
		uiSettings.setUiX(frame.getX());
		uiSettings.setUiY(frame.getY());
		uiSettings.setUiExtendedState(frame.getExtendedState());
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

	@Override
	public void handleApplicationExit() {}

}
