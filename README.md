# Octopus: A 8 bit CPU and its assembly language

## Octopus Core
* 16 general registers: R0 ~ R 15
* PC, SP registers
* ZERO flag
* 256 bytes RAM

## ASM language
* Comment: support /**/ and //
* label: your_label:
* db: .DB 0x01 0x02
* string .STRING "strings you put here\n"

* Example:
```
/*
 hello.s, the example of "hello world" program for octopus core
 Copyright (c) Joe Xue (lgxue@hotmail.com)
*/

JMP start

hello:
.STRING "Hello World!\n"

start:
    LDI R2 0
    LDI R0 hello
print:
    LDR R1 R0
    INC R0
    CMP R1 R2
    JZ stop
    OUT R1
    JMP print

stop:
    HLT
```

## 30 instructions
|Inst  | op1 | op2                     |Machine code byte1| Machine code byte2|Example|
|------|------|------------------------|------------------|-------------------|----------------------|
|NOP   |n/a  |n/a                     |0x00              |0x00                |NOP                   |
|HLT   |n/a  |n/a                     |0x01              |0x00                |HLT                   |
|LDI   |reg  |label or instance number|0x2X(X is REG)    |NUMBER              |LDI 0x4; LDI hello    |
|LDR   |reg  |reg                     |0x3               |rd << 4\| rs        |LDR R0 R1             |
|STR   |reg  |reg                     |0x4               |rd << 4\| rs        |STR R0 R1             |
|MOV   |reg  |reg                     |0x5               |rd << 4\| rs        |MOV R0 R1             |
|PUSH  |reg  |n/a                     |0x6               |rs                  |PUSH R0               |
|POP   |reg  |n/a                     |0x7               |rd << 4             |POP R0                |
|ADD   |reg  |reg                     |0x8               |rd << 4 \| rs       |ADD R0 R1             |
|SUB   |reg  |reg                     |0x9               |rd << 4 \| rs       |SUB R0 R1             |
|MUL   |reg  |reg                     |0xa               |rd << 4 \| rs       |MUL R0 R1             |
|DIV   |reg  |reg                     |0xb               |rd << 4 \| rs       |DIV R0 R1             |
|AND   |reg  |reg                     |0xc               |rd << 4 \| rs       |AND R0 R1             |
|OR    |reg  |reg                     |0xd               |rd << 4 \| rs       |OR  R0 R1             |
|XOR   |reg  |reg                     |0xe               |rd << 4 \| rs       |XOR R0 R1             |
|NOT   |reg  |n/a                     |0xf               |rd << 4             |NOT R0                |
|INC   |reg  |n/a                     |0x10              |rd << 4             |INC R0                |
|DEC   |reg  |n/a                     |0x11              |rd << 4             |DEC R0                |
|CMP   |reg  |reg                     |0x12              |rd << 4 \| rs       |CMP R0 R1             |
|SHL   |reg  |n/a                     |0x13              |rd << 4             |SHL R0                |
|SHR   |reg  |n/a                     |0x14              |rd << 4             |SHR R0                |
|IN    |reg  |n/a                     |0x15              |rd << 4             |IN R0                 |
|OUT   |reg  |n/a                     |0x16              |rs                  |OUT R0                |
|JNZ   |label or instance numbe  r|n/a|0x17              |NUMBER              |JNZ 0x4; JNZ loop     |
|JZ    |label or instance numbe  r|n/a|0x18              |NUMBER              |JZ 0x4; JZ  loop      |
|JMP   |label or instance numbe  r|n/a|0x19              |NUMBER              |JMP 0x4; JMP  loop    |
|JMPR  |reg  |n/a                     |0x1a              | rs                 |JMPR R0               |
|CALL  |label or instance numbe  r|n/a|0x1b              |NUMBER              |CALL 0x4; CALL  func  |
|CALLR |reg  |n/a                     |0x1c              | rs                 |CALLR R0              |
|RET   |n/a  |n/a                     |0x1d              |0x00                |RET                   |

## Screenshot

<img width="1486" height="1052" alt="Screenshot 2026-05-29 192915" src="https://github.com/user-attachments/assets/fd6c4ea6-4074-4094-8208-871bc42f741e" />

