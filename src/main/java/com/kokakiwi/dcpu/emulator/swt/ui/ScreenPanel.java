package com.kokakiwi.dcpu.emulator.swt.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.kokakiwi.dcpu.emulator.swt.CoreEmulator;
import de.codesourcery.jasm16.emulator.devices.impl.DefaultScreen;

public class ScreenPanel extends JPanel
{
    private static final long   serialVersionUID = -9068304858687292279L;
    
    private final CoreEmulator  emulator;
    private final DefaultScreen screen           = new DefaultScreen(true);
    
    public ScreenPanel(CoreEmulator emulator)
    {
        this.emulator = emulator;
        
        emulator.getEmulator().addDevice(screen);
        screen.attach(this);
        
        setDoubleBuffered(true);
    }
    
    public CoreEmulator getEmulator()
    {
        return emulator;
    }
    
    public DefaultScreen getScreen()
    {
        return screen;
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        BufferedImage image = screen.getScreenImage();
        Image scaled = image.getScaledInstance(getWidth(), getHeight(),
                Image.SCALE_FAST);
        g.drawImage(scaled, 0, 0, null);
    }
}
