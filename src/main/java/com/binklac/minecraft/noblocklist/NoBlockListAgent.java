package com.binklac.minecraft.noblocklist;

import java.lang.instrument.Instrumentation;

public class NoBlockListAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation){
        System.out.println("NoBlockList loaded successfully! Waiting for other classes to load.");
        instrumentation.addTransformer(new MinecraftClassTransformer());
    }
}
