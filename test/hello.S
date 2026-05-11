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
