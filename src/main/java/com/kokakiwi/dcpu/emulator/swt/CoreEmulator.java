package com.kokakiwi.dcpu.emulator.swt;

import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;

import com.kokakiwi.dcpu.emulator.swt.core.DefaultSWTKeyboard;
import com.kokakiwi.dcpu.emulator.swt.listeners.CoreEmulationListener;
import com.kokakiwi.dcpu.emulator.swt.ui.ScreenWindow;

import de.codesourcery.jasm16.WordAddress;
import de.codesourcery.jasm16.emulator.Emulator;
import de.codesourcery.jasm16.emulator.IEmulationListener;
import de.codesourcery.jasm16.emulator.IEmulator;
import de.codesourcery.jasm16.emulator.devices.impl.DefaultClock;

public class CoreEmulator
{
    private final IEmulator          emulator;
    private PrintStream              out;
    private final IEmulationListener emulationListener;
    private final DefaultSWTKeyboard keyboard = new DefaultSWTKeyboard();
    
    public CoreEmulator()
    {
        this(System.out);
    }
    
    public CoreEmulator(PrintStream out)
    {
        emulator = new Emulator();
        
        emulationListener = new CoreEmulationListener(out);
        emulator.addEmulationListener(emulationListener);
        
        emulator.setOutput(out);
        emulator.setEmulationSpeed(IEmulator.EmulationSpeed.REAL_SPEED);
        
        this.out = out;
        
        emulator.addDevice(new DefaultClock());
        Display.getDefault().syncExec(new Runnable() {
            
            public void run()
            {
                ScreenWindow window = new ScreenWindow(Display.getDefault(),
                        CoreEmulator.this);
                window.open();
            }
        });
    }
    
    public void run(byte[] program)
    {
        emulator.reset(true);
        
        // emulator.addBreakpoint(new Breakpoint(Address.wordAddress(0x002b)));
        
        emulator.loadMemory(WordAddress.ZERO, program);
        
        emulator.start();
    }
    
    public void terminate()
    {
        emulator.stop();
    }
    
    public IEmulator getEmulator()
    {
        return emulator;
    }
    
    public PrintStream getOutput()
    {
        return out;
    }
    
    public void setOutput(PrintStream out)
    {
        this.out = out;
    }
    
    public DefaultSWTKeyboard getKeyboard()
    {
        return keyboard;
    }
}
