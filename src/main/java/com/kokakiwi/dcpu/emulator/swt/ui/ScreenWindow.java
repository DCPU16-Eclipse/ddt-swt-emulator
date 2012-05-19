package com.kokakiwi.dcpu.emulator.swt.ui;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.kokakiwi.dcpu.emulator.swt.CoreEmulator;

import de.codesourcery.jasm16.emulator.devices.impl.DefaultScreen;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;

public class ScreenWindow extends Shell
{
    private final CoreEmulator emulator;
    private final ScreenPanel  screen;
    
    public ScreenWindow(Display display, final CoreEmulator emulator)
    {
        super(display, SWT.CLOSE | SWT.TITLE);
        createContents();
        
        this.emulator = emulator;
        setLayout(new FillLayout());
        
        Composite composite = new Composite(this, SWT.EMBEDDED);
        Frame frame = SWT_AWT.new_Frame(composite);
        
        screen = new ScreenPanel(emulator);
        
        emulator.getKeyboard().bind(this);
        
        frame.add(screen);
        
        addShellListener(new ShellAdapter() {
            
            public void shellClosed(ShellEvent e)
            {
                emulator.terminate();
            }
        });
    }
    
    protected void createContents()
    {
        setText("Screen");
        setSize(DefaultScreen.STANDARD_SCREEN_COLUMNS
                * DefaultScreen.GLYPH_WIDTH * 4,
                DefaultScreen.STANDARD_SCREEN_ROWS * DefaultScreen.GLYPH_HEIGHT
                        * 4);
    }
    
    @Override
    protected void checkSubclass()
    {
        
    }
    
    public CoreEmulator getEmulator()
    {
        return emulator;
    }
    
    public ScreenPanel getScreen()
    {
        return screen;
    }
    
}
