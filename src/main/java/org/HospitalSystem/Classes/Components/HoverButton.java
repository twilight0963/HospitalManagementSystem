package org.HospitalSystem.Classes.Components;
import java.awt.Color;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;

public class HoverButton extends JButton {
    private static final String RESOURCE_PATH = "src/main/java/org/HospitalSystem/Resources/";
    
    public HoverButton(String label, String IconPath, int width, int height, String normal_color, String hover_color){
        setText(label);
        setForeground(Color.white);
        if (IconPath != null) {
            setIcon(new PathImageIcon(RESOURCE_PATH + IconPath).resize((int)(height/1.1)));
        }
        
        // Set both preferred and minimum size to ensure consistent sizing
        setPreferredSize(new java.awt.Dimension(width, height));
        setMinimumSize(new java.awt.Dimension(width, height));
        setMaximumSize(new java.awt.Dimension(width, height));
        
        // Force the size
        setSize(width, height);
        
        setBackground(java.awt.Color.decode(normal_color));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isEnabled()) {
                    setBackground(java.awt.Color.decode(hover_color));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (isEnabled()) {
                    setBackground(java.awt.Color.decode(normal_color));
                }
            }
        });

        // Add property change listener to handle disabled state
        addPropertyChangeListener("enabled", evt -> {
            boolean enabled = (boolean) evt.getNewValue();
            setBackground(java.awt.Color.decode(enabled ? normal_color : "#cccccc"));
        });
    }
    
    public HoverButton(String label, String IconPath, int width, int height){
        this(label, IconPath, width, height, "#4d869c", "#2c4e5a");
    }
    public HoverButton(String label, int width, int height){
        this(label, null, width, height, "#4d869c", "#2c4e5a");
    }
    public HoverButton(String label, int width, int height, String normal_color, String hover_color){
        this(label, null, width, height, normal_color, hover_color);
    }
    
    @Override
    public java.awt.Dimension getPreferredSize() {
        java.awt.Dimension size = super.getPreferredSize();
        return new java.awt.Dimension(Math.max(size.width, getWidth()), 
                                    Math.max(size.height, getHeight()));
    }
}
