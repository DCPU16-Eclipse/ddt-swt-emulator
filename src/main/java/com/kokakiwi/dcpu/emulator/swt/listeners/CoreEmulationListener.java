package com.kokakiwi.dcpu.emulator.swt.listeners;

import java.io.PrintStream;
import java.util.List;

import de.codesourcery.jasm16.Address;
import de.codesourcery.jasm16.disassembler.DisassembledLine;
import de.codesourcery.jasm16.disassembler.Disassembler;
import de.codesourcery.jasm16.emulator.Breakpoint;
import de.codesourcery.jasm16.emulator.IEmulationListener;
import de.codesourcery.jasm16.emulator.IEmulator;
import de.codesourcery.jasm16.emulator.IEmulator.EmulationSpeed;

public class CoreEmulationListener implements IEmulationListener
{
    private final PrintStream out;
    
    public CoreEmulationListener(PrintStream out)
    {
        this.out = out;
    }
    
    public void beforeEmulatorIsDisposed(IEmulator emulator)
    {
        
    }
    
    public void onEmulationSpeedChange(EmulationSpeed oldSpeed,
            EmulationSpeed newSpeed)
    {
        
    }
    
    public void beforeContinuousExecution(IEmulator emulator)
    {
        
    }
    
    public void afterReset(IEmulator emulator)
    {
        
    }
    
    public void afterMemoryLoad(IEmulator emulator, Address startAddress,
            int lengthInBytes)
    {
        
    }
    
    public void breakpointAdded(IEmulator emulator, Breakpoint breakpoint)
    {
        
    }
    
    public void breakpointDeleted(IEmulator emulator, Breakpoint breakpoint)
    {
        
    }
    
    public void breakpointChanged(IEmulator emulator, Breakpoint breakpoint)
    {
        
    }
    
    public void onBreakpoint(IEmulator emulator, Breakpoint breakpoint)
    {
        out.println("Breakpoint@" + breakpoint.getAddress().toString());
        out.println("StackTrace :");
        Disassembler disassembler = new Disassembler();
        List<DisassembledLine> lines = disassembler.disassemble(
                emulator.getMemory(), breakpoint.getAddress(), 16, true);
        for (DisassembledLine line : lines)
        {
            out.println(line.getContents());
        }
    }
    
    public void beforeCommandExecution(IEmulator emulator)
    {
        
    }
    
    public boolean isInvokeBeforeCommandExecution()
    {
        return false;
    }
    
    public boolean isInvokeAfterCommandExecution()
    {
        return false;
    }
    
    public boolean isInvokeAfterAndBeforeCommandExecutionInContinuousMode()
    {
        return false;
    }
    
    public void afterCommandExecution(IEmulator emulator, int commandDuration)
    {
        
    }
    
    public void onStop(IEmulator emulator, Address previousPC,
            Throwable emulationError)
    {
        
    }
    
}
