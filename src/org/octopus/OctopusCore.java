/*
 * OctopusCore.java
 *
 * Copyright (c) Joe Xue (lgxue@hotmail.com) 2026. All rights reserved
 */

package org.octopus;

class OctopusCore
{
    static final byte NOP = 0;
    static final byte HLT = 1;
    static final byte LDI = (byte)(0x2 << 4);
    static final byte LDR = 3;
    static final byte STR = 4;
    static final byte MOV = 5;
    static final byte PUSH = 6;
    static final byte POP = 7;
    static final byte ADD = 8;
    static final byte SUB = 9;
    static final byte MUL = 0xa;
    static final byte DIV = 0xb;
    static final byte AND = 0xc;
    static final byte OR = 0xd;
    static final byte XOR = 0xe;
    static final byte NOT = 0xf;
    static final byte INC = 0x10;
    static final byte DEC = 0x11;
    static final byte CMP = 0x12;
    static final byte SHL = 0x13;
    static final byte SHR = 0x14;
    static final byte IN = 0x15;
    static final byte OUT = 0x16;
    static final byte JNZ = 0x17;
    static final byte JZ = 0x18;
    static final byte JMP = 0x19;
    static final byte JMPR = 0x1a;
    static final byte CALL = 0x1b;
    static final byte CALLR = 0x1c;
    static final byte RET = 0x1d;

    final String[] instName = {
        "NOP",
        "HLT",
        "LDI",
        "LDR",
        "STR",
        "MOV",
        "PUSH",
        "POP",
        "ADD",
        "SUB",
        "MUL",
        "DIV",
        "AND",
        "OR",
        "XOR",
        "NOT",
        "INC",
        "DEC",
        "CMP",
        "SHL",
        "SHR",
        "IN",
        "OUT",
        "JNZ",
        "JZ",
        "JMP",
        "JMPR",
        "CALL",
        "CALLR",
        "RET"
    };

    private int pc;
    private int sp;
    private boolean zFlag;
    private boolean debugFlag;

    private byte[] regs;
    private byte[] ram;

    private int inst1, inst2;
    private int instantNum;
    private int inst;
    private int rs;
    private int rd;

    public OctopusCore(byte[] ram, boolean debug) {
        pc = 0;
        sp = 0xff;
        zFlag = false;
        debugFlag = debug;
        regs = new byte[16];
        this.ram = ram;
    }

    private byte inByte() {
        return 0;
    }

    private void outByte(int b) {
        System.out.write(b);
        System.out.flush();
    }

    private void fetch() {
        inst1 = Byte.toUnsignedInt(ram[pc]);
        inst2 = Byte.toUnsignedInt(ram[pc + 1]);
        pc += 2;

        if (pc > sp) {
            System.err.println("Run out of code area");
            System.exit(-1);
        }
    }

    private void decode() {
        if ((inst1 & 0xe0) > 0) {
            inst = (inst1 & 0xf0) >> 4;
            rd = inst1 & 0xf;
        } else {
            inst = inst1;
            rd = (inst2 & 0xf0) >> 4;
        }

        rs = inst2 & 0x0f;
        instantNum = inst2;
    }

    private boolean execute() {
        boolean rc = false;
        int tmp;

        switch(inst) {
            case NOP:
                break;
            case HLT:
                rc = true;
                break;
            case LDI >> 4:
                regs[rd] = (byte)instantNum;
                break;
            case LDR:
                tmp = Byte.toUnsignedInt(regs[rs]);
                regs[rd] = ram[tmp];
                break;
            case STR:
                ram[rd] = (byte)rs;
                break;
            case MOV:
                regs[rd] = regs[rs];
                break;
            case PUSH:
                ram[sp--] = (byte)rs;
                break;
            case POP:
                regs[rd] = ram[sp++];
                break;
            case ADD:
                tmp = Byte.toUnsignedInt(regs[rd]) + Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case SUB:
                tmp = Byte.toUnsignedInt(regs[rd]) - Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case MUL:
                tmp = Byte.toUnsignedInt(regs[rd]) * Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case DIV:
                tmp = Byte.toUnsignedInt(regs[rd]) / Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case AND:
                tmp = Byte.toUnsignedInt(regs[rd]) & Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case OR:
                tmp = Byte.toUnsignedInt(regs[rd]) | Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case XOR:
                tmp = Byte.toUnsignedInt(regs[rd]) ^ Byte.toUnsignedInt(regs[rs]);
                regs[rd] = (byte)tmp;
                break;
            case NOT:
                tmp = Byte.toUnsignedInt(regs[rd]);
                regs[rd] = (tmp > 0) ? (byte)0 : (byte)1;
                break;
            case INC:
                tmp = Byte.toUnsignedInt(regs[rd]);
                tmp++;
                regs[rd] = (byte)tmp;
                break;
            case DEC:
                tmp = Byte.toUnsignedInt(regs[rd]);
                tmp--;
                regs[rd] = (byte)tmp;
                break;
            case CMP:
                if (regs[rd] == regs[rs]) {
                    zFlag = true;
                } else {
                    zFlag = false;
                }
                break;
            case SHL:
                tmp = Byte.toUnsignedInt(regs[rd]);
                tmp = tmp << 1;
                regs[rd] = (byte)tmp;
                break;
            case SHR:
                tmp = Byte.toUnsignedInt(regs[rd]);
                tmp = tmp >> 1;
                regs[rd] = (byte)tmp;
                break;
            case IN:
                regs[rd] = inByte();
                break;
            case OUT:
                tmp = Byte.toUnsignedInt(regs[rs]);
                outByte(tmp);
                break;
            case JNZ:
                if (!zFlag) {
                    pc = instantNum;
                }
                break;
            case JZ:
                if (zFlag) {
                    pc = instantNum;
                }
                break;
            case JMP:
                    pc = instantNum;
                break;
            case JMPR:
                    tmp = Byte.toUnsignedInt(regs[rs]);
                    pc = tmp;
                break;
            case CALL:
                    ram[sp--] = (byte)pc;
                    pc = instantNum;
                break;
            case CALLR:
                    tmp = Byte.toUnsignedInt(regs[rs]);
                    ram[sp--] = (byte)pc;
                    pc = tmp;
                break;
            case RET:
                    tmp = ram[sp++];
                    pc = tmp;
                break;
            default:
                rc = true;
                break;
        }

        return rc;
    }

    private void debugInfo() {
        switch(inst) {
            case NOP:
            case HLT:
            case RET:
                System.out.printf("Inst: %s\n", instName[inst]);
                break;
            case LDI >> 4:
                System.out.printf("Inst: %s R%d %x\n", instName[inst], rd, instantNum);
                break;
            case JNZ:
            case JZ:
            case JMP:
            case CALL:
                System.out.printf("Inst: %s %x\n", instName[inst], instantNum);
                break;
            case LDR:
            case MOV:
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case AND:
            case OR:
            case XOR:
            case STR:
            case CMP:
                System.out.printf("Inst: %s R%d R%d\n", instName[inst], rd, rs);
                break;
            case PUSH:
            case IN:
            case JMPR:
            case CALLR:
            case OUT:
                System.out.printf("Inst: %s R%d\n", instName[inst], rs);
                break;
            case POP:
            case INC:
            case DEC:
            case NOT:
            case SHL:
            case SHR:
                System.out.printf("Inst: %s R%d\n", instName[inst], rd);
                break;
            default:
                System.out.printf("Unsupported inst: %x\n", inst);
                break;
        }

        System.out.printf("PC = %02x SP = %02x zFlag = %b\n", pc, sp, zFlag);
        for (int i = 0; i < 16; i++) {
            System.out.printf("R%-5d", i);
        }

        System.out.printf("\n");
        for (int i = 0; i < 16; i++) {
            System.out.printf("0x%02x  ", regs[i]);
        }
        System.out.println("");
        System.out.println("");
    }

    public void run() {
        if (debugFlag) {
            for (int i = 0; i< 256; i += 16) {
                System.out.printf("%02X: ", i);
                for (int j = 0; j < 16; j++) {
                    System.out.printf("%02X ", ram[i + j]);
                }
                System.out.println("");
            }
        }
        while (true) {
            fetch();
            decode();

            boolean quit = execute();
            if (debugFlag) {
                debugInfo();
            }

            if (quit) {
                break;
            }
        }
    }
}
