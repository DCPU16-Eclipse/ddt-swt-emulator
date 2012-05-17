package com.kokakiwi.dcpu.emulator.swt.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;

import com.kokakiwi.dcpu.emulator.swt.Constants;

import de.codesourcery.jasm16.Register;
import de.codesourcery.jasm16.emulator.IEmulator;
import de.codesourcery.jasm16.emulator.devices.DeviceDescriptor;
import de.codesourcery.jasm16.emulator.devices.HardwareInterrupt;
import de.codesourcery.jasm16.emulator.devices.IDevice;

public class DefaultSWTKeyboard implements IDevice, KeyListener
{
    public final static DeviceDescriptor DESCRIPTOR  = new DeviceDescriptor(
                                                             "Generic Keyboard",
                                                             "SWT Generic Keyboard",
                                                             0x30cf7406L,
                                                             1,
                                                             Constants.MANUFACTURER);
    private IEmulator                    emulator;
    
    private final Map<Integer, KeyState> states      = new LinkedHashMap<Integer, DefaultSWTKeyboard.KeyState>();
    private final List<Control>          bindList    = new LinkedList<Control>();
    
    private final Semaphore              BUFFER_LOCK = new Semaphore(1);
    private final List<Integer>          buffer      = new LinkedList<Integer>();
    
    private int                          irq         = -1;
    
    public void afterAddDevice(IEmulator emulator)
    {
        this.emulator = emulator;
    }
    
    public void beforeRemoveDevice(IEmulator emulator)
    {
        this.emulator = null;
    }
    
    public DeviceDescriptor getDeviceDescriptor()
    {
        return DESCRIPTOR;
    }
    
    public int handleInterrupt(IEmulator emulator)
    {
        int a = emulator.getCPU().getRegisterValue(Register.A);
        int b = emulator.getCPU().getRegisterValue(Register.B);
        int value;
        
        //@formatter:off
        /*
         * Interrupts do different things depending on contents of the A register:
         * 
         *  A | BEHAVIOR
         * ---+----------------------------------------------------------------------------
         *  0 | Clear keyboard buffer
         *  1 | Store next key typed in C register, or 0 if the buffer is empty
         *  2 | Set C register to 1 if the key specified by the B register is pressed, or
         *    | 0 if it's not pressed
         *  3 | If register B is non-zero, turn on interrupts with message B. If B is zero,
         *    | disable interrupts
         * ---+----------------------------------------------------------------------------      
         */
        //@formatter:on
        switch (a)
        {
            case 0:
                buffer.clear();
                break;
            
            case 1:
                value = buffer.isEmpty() ? 0 : buffer.remove(0);
                emulator.getCPU().setRegisterValue(Register.C, value);
                break;
            
            case 2:
                value = getState(b, false).isPressed() ? 1 : 0;
                emulator.getCPU().setRegisterValue(Register.C, value);
                break;
            
            case 3:
                if (b > 0)
                {
                    irq = -1;
                }
                else
                {
                    irq = b;
                }
                break;
        }
        
        return 0;
    }
    
    public void keyPressed(KeyEvent e)
    {
        getState(keyCode(e.keyCode), true).setPressed(true);
        getState(keyCode(e.keyCode), true).setLastPressed(
                System.currentTimeMillis());
    }
    
    public void keyReleased(KeyEvent e)
    {
        getState(keyCode(e.keyCode), false).setPressed(false);
        long curr = System.currentTimeMillis();
        long last = getState(keyCode(e.keyCode), false).getLastPressed();
        long diff = curr - last;
        if (diff < 1000)
        {
            try
            {
                keyTyped(e);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }
    
    public void keyTyped(KeyEvent e) throws InterruptedException
    {
        BUFFER_LOCK.acquire();
        
        buffer.add(keyCode(e.keyCode));
        
        BUFFER_LOCK.release();
    }
    
    public int keyCode(int code)
    {
        int value = 0;
        
        return value;
    }
    
    protected void sendInterrupt()
    {
        if (irq > 0)
        {
            emulator.triggerInterrupt(new HardwareInterrupt(this, irq));
        }
    }
    
    public KeyState getState(int code, boolean pressed)
    {
        KeyState state = states.get(code);
        
        if (state == null)
        {
            state = new KeyState(pressed);
            states.put(code, state);
        }
        
        return state;
    }
    
    public void bind(Control control)
    {
        if (!bindList.contains(control))
        {
            control.addKeyListener(this);
            bindList.add(control);
        }
    }
    
    public static class KeyState
    {
        private boolean pressed;
        private long    lastPressed = 0;
        
        public KeyState(boolean pressed)
        {
            this.pressed = pressed;
        }
        
        public boolean isPressed()
        {
            return pressed;
        }
        
        public void setPressed(boolean pressed)
        {
            this.pressed = pressed;
        }
        
        public long getLastPressed()
        {
            return lastPressed;
        }
        
        public void setLastPressed(long lastPressed)
        {
            this.lastPressed = lastPressed;
        }
    }
    
    public void reset()
    {
        buffer.clear();
        states.clear();
        
        clearBinds();
    }
    
    public void clearBinds()
    {
        for (Control control : bindList)
        {
            control.removeKeyListener(this);
        }
        
        bindList.clear();
    }
    
}
