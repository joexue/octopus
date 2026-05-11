/*
 * OctopusHandler.java
 *
 * Copyright (c) Joe Xue (lgxue@hotmail.com) 2026. All rights reserved
 */

package org.octopus;

import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class OctopusHandler extends OctopusBaseListener {

    int pos;
    byte[] macCode;
    HashMap<String, Integer> labels;
    HashMap<String, Integer> linkAddress;
    boolean err;

    public OctopusHandler() {
        pos = 0;
        err = false;
        macCode = new byte[256];
        labels = new HashMap<String, Integer>();
        linkAddress = new HashMap<String, Integer>();
    }

    public byte[] getCode() {
        return macCode;
    }

    private void PosUpdate(int offset) {
        pos += offset;
        if (pos > 256) {
            System.err.println("The code exceed 256 bytes");
            System.exit(-1);
        }
    }

    private void PosUpdate() {
        PosUpdate(2);
    }

	@Override
    public void enterString(OctopusParser.StringContext ctx) {
        String string = ctx.STRING().getText().translateEscapes();
        string = string.substring(1, string.length() - 1);
        byte[] str = string.getBytes();
        int size = str.length;
        System.arraycopy(str, 0, macCode, pos, size);
        pos += size;
        macCode[pos] = 0;
        pos += 1;
    }

	@Override
    public void enterDb(OctopusParser.DbContext ctx) {
        int size = ctx.NUMBER().size();
        for (int i = 0; i < size; i++) {
            byte number = (byte)(Integer.parseInt(ctx.NUMBER().get(i).getText()));
            macCode[pos++] = number;
        }
    }

	@Override
    public void enterLabel(OctopusParser.LabelContext ctx) {
        String label = ctx.LABEL().getText();
        labels.put(label, pos);
    }

	@Override
    public void enterNop(OctopusParser.NopContext ctx) {
        macCode[pos] = OctopusCore.NOP;
        PosUpdate();
    }

	@Override
    public void enterHlt(OctopusParser.HltContext ctx) {
        macCode[pos] = OctopusCore.HLT;
        PosUpdate();
    }

	@Override
    public void enterLdi(OctopusParser.LdiContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        int add = 0;
        if (ctx.NUMBER() != null) {
            add = Integer.parseInt(ctx.NUMBER().getText());
        } else {
            String label = ctx.LABEL().getText();
            add = 0;
            linkAddress.put(label, pos + 1);
        }

        macCode[pos] = (byte)(OctopusCore.LDI | rd);
        macCode[pos + 1] = (byte)add;
        PosUpdate();
    }

	@Override
    public void enterLdr(OctopusParser.LdrContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.LDR;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterStr(OctopusParser.StrContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.STR;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterMov(OctopusParser.MovContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.MOV;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterPush(OctopusParser.PushContext ctx) {
        byte rs = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.PUSH;
        macCode[pos + 1] = (byte)(rs);
        PosUpdate();
    }

	@Override
    public void enterPop(OctopusParser.PopContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.POP;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterAdd(OctopusParser.AddContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.ADD;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterSub(OctopusParser.SubContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.SUB;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterMul(OctopusParser.MulContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.MUL;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterDiv(OctopusParser.DivContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.DIV;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterAnd(OctopusParser.AndContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.AND;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterOr(OctopusParser.OrContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.OR;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterXor(OctopusParser.XorContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.XOR;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterNot(OctopusParser.NotContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.NOT;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterInc(OctopusParser.IncContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.INC;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterDec(OctopusParser.DecContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.DEC;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterCmp(OctopusParser.CmpContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().get(0).getText().substring(1));
        byte rs = Byte.parseByte(ctx.REG().get(1).getText().substring(1));
        macCode[pos] = OctopusCore.CMP;
        macCode[pos + 1] = (byte)(rd << 4 | rs);
        PosUpdate();
    }

	@Override
    public void enterShl(OctopusParser.ShlContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.SHL;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterShr(OctopusParser.ShrContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.SHR;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterIn(OctopusParser.InContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.IN;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterOut(OctopusParser.OutContext ctx) {
        byte rs = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.OUT;
        macCode[pos + 1] = (byte)(rs);
        PosUpdate();
    }

	@Override
    public void enterJnz(OctopusParser.JnzContext ctx) {
        int add = 0;
        if (ctx.NUMBER() != null) {
            add = Integer.parseInt(ctx.NUMBER().getText());
        } else {
            String label = ctx.LABEL().getText();
            add = 0;
            linkAddress.put(label, pos + 1);
        }

        macCode[pos] = OctopusCore.JNZ;
        macCode[pos + 1] = (byte)add;
        PosUpdate();
    }

	@Override
    public void enterJz(OctopusParser.JzContext ctx) {
        int add = 0;
        if (ctx.NUMBER() != null) {
            add = Integer.parseInt(ctx.NUMBER().getText());
        } else {
            String label = ctx.LABEL().getText();
            linkAddress.put(label, pos + 1);
            add = 0;
        }

        macCode[pos] = OctopusCore.JZ;
        macCode[pos + 1] = (byte)add;
        PosUpdate();
    }

	@Override
    public void enterJmp(OctopusParser.JmpContext ctx) {
        int add = 0;
        if (ctx.NUMBER() != null) {
            add = Integer.parseInt(ctx.NUMBER().getText());
        } else {
            String label = ctx.LABEL().getText();
            linkAddress.put(label, pos + 1);
            add = 0;
        }

        macCode[pos] = OctopusCore.JMP;
        macCode[pos + 1] = (byte)add;
        PosUpdate();
    }

	@Override
    public void enterJmpr(OctopusParser.JmprContext ctx) {
        byte rs = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.JMPR;
        macCode[pos + 1] = (byte)(rs);
        PosUpdate();
    }

	@Override
    public void enterCall(OctopusParser.CallContext ctx) {
        int add = 0;
        if (ctx.NUMBER() != null) {
            add = Integer.parseInt(ctx.NUMBER().getText());
        } else {
            String label = ctx.LABEL().getText();
            linkAddress.put(label, pos + 1);
            add = 0;
        }

        macCode[pos] = OctopusCore.CALL;
        macCode[pos + 1] = (byte)add;
        PosUpdate();
    }

	@Override
    public void enterCallr(OctopusParser.CallrContext ctx) {
        byte rd = Byte.parseByte(ctx.REG().getText().substring(1));
        macCode[pos] = OctopusCore.CALLR;
        macCode[pos + 1] = (byte)(rd << 4);
        PosUpdate();
    }

	@Override
    public void enterRet(OctopusParser.RetContext ctx) {
        macCode[pos] = OctopusCore.RET;
        PosUpdate();
    }

	@Override
    public void exitAsm(OctopusParser.AsmContext ctx) {
        linkAddress.forEach((key, value) -> {
            Integer resolveAdd = labels.get(key);
            if (resolveAdd == null) {
                System.err.println("No label: " + key);
                System.exit(-1);
            }
            macCode[value] = (byte)(int)resolveAdd;
        });
    }
}
