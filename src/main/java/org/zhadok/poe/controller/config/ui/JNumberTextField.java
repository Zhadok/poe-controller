package org.zhadok.poe.controller.config.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * A {@link JTextField} that skips all non-digit keys. The user is only able to enter numbers.
 * https://gist.github.com/gysel/4074617
 * @author Michi Gysel <michi@scythe.ch>
 * 
 */
public class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    public JNumberTextField() {
    	this.setBorder(new LineBorder(Color.BLACK));
    }
    
    @Override
    public void processKeyEvent(KeyEvent ev) {
//        if (Character.isDigit(ev.getKeyChar())) {
//            
//        }
        super.processKeyEvent(ev);
        
        if (getNumber() == null) {
        	this.setBorder(new LineBorder(Color.RED));
        } else {
        	this.setBorder(new LineBorder(Color.BLACK));
        }
    }

    /**
     * As the user is not even able to enter a dot ("."), only integers (whole numbers) may be entered.
     */
    public Double getNumber() {
        Double result = null;
        String text = getText();
        if (text != null && !"".equals(text)) {
        	try {
        		result = Double.valueOf(text);
        	} catch (NumberFormatException e) {}
        }
        return result;
    }

	public void setText(int number) {
		super.setText(number + "");
	}
	
	public void addNumberListener(Consumer<Double> handler) {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent keyEvent) {
				handler.accept(getNumber());
			}
		});
	}
}
