package org.HospitalSystem.Classes.Components;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberField extends JTextField {
    private Integer minValue = null;
    private Integer maxValue = null;
    private boolean allowDecimals = false;  // Add this field

    public NumberField(Integer min, Integer max, int columns) {
        super(columns);
        this.minValue = min;
        this.maxValue = max;
        
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (isValidInput(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (isValidInput(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    public void setAllowDecimals(boolean allow) {
        this.allowDecimals = allow;
    }

    private boolean isValidInput(String text) {
        String newValue = getText() + text;
        if (newValue.isEmpty()) return true;
        
        try {
            if (allowDecimals) {
                // Allow one decimal point
                if (text.equals(".") && !getText().contains(".")) {
                    return true;
                }
                double value = Double.parseDouble(newValue);
                if (minValue != null && value < minValue) return false;
                return !(maxValue != null && value > maxValue);
            } else {
                int value = Integer.parseInt(newValue);
                if (minValue != null && value < minValue) return false;
                return !(maxValue != null && value > maxValue);
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Number getValue() {
        try {
            if (allowDecimals) {
                return Double.valueOf(getText());
            }
            return Integer.valueOf(getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}