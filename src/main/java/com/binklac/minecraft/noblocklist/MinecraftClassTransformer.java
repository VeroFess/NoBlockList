package com.binklac.minecraft.noblocklist;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MinecraftClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.replace("/", ".").equals("com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService")) {
            try {
                System.out.println("Successfully found YggdrasilSocialInteractionsService, ready to repair it ...");
                ClassPool minecraftClassPool = ClassPool.getDefault();
                CtClass yggdrasilSocialInteractionsServiceClass = minecraftClassPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtMethod isBlockedPlayer = yggdrasilSocialInteractionsServiceClass.getDeclaredMethod("isBlockedPlayer", new CtClass[]{minecraftClassPool.getCtClass("java.util.UUID")});
                isBlockedPlayer.setBody("{return false;}");
                CtMethod checkPrivileges = yggdrasilSocialInteractionsServiceClass.getDeclaredMethod("checkPrivileges");
                checkPrivileges.setBody("{chatAllowed = true; serversAllowed = true; realmsAllowed = true; return;}");
                System.out.println("Patch of YggdrasilSocialInteractionsService successfully applied.");
                return yggdrasilSocialInteractionsServiceClass.toBytecode();
            } catch (IOException | NotFoundException | CannotCompileException e) {
                System.out.println("Unable to apply the patch, is there an error?");
            }
        }

        return null;
    }
}
