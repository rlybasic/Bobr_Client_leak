/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextFormatting
 */
package ru.terrar.bobr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.text.TextFormatting;
import ru.terrar.bobr.bobr;
import ru.terrar.bobr.util.ChatUtil;

public abstract class Command {
    private final String name;
    private final String desc;
    private final String[] syntax;
    private final List<String> aliases = new ArrayList<String>();

    public Command(String name, String desc, String ... syntax) {
        this.name = name;
        this.desc = desc;
        this.syntax = syntax;
    }

    public void syntaxError() {
        ChatUtil.clientMessage("Incorrect Syntax: ");
        for (String syntax : this.getSyntax()) {
            ChatUtil.clientMessage((Object)TextFormatting.YELLOW + bobr.getGate().commandManager.getPrefix() + syntax);
        }
    }

    public void addAliases(String ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public abstract void onCommand(String[] var1);

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String[] getSyntax() {
        return this.syntax;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}
