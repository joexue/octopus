
/*
 * Octopus.g4
 *
 * Antlr gramma file for octopus 8 bit CPU
 *
 * Copyright (c) Joe Xue (lgxue@hotmail.com) 2026. All rights reserved
*/

grammar Octopus;

asm
    : (string | db | label | instruction)+ EOF
    ;

instruction
    : nop
    | hlt
    | ldi
    | ldr
    | str
    | mov
    | push
    | pop
    | add
    | sub
    | mul
    | div
    | and
    | or
    | xor
    | not
    | inc
    | dec
    | cmp
    | shr
    | shl
    | in
    | out
    | jnz
    | jz
    | jmp
    | jmpr
    | call
    | callr
    | ret
    ;

string
    : '.STRING' STRING
    ;

db
    : '.DB' NUMBER+
    ;

label
    : LABEL ':'
    ;

nop
    : 'NOP'
    ;

hlt
    : 'HLT'
    ;

ldi
    : 'LDI' REG (NUMBER | LABEL)
    ;

ldr
    : 'LDR' REG REG
    ;

str
    : 'STR' REG REG
    ;

mov
    : 'MOV' REG REG
    ;

push
    : 'PUSH' REG
    ;

pop
    : 'POP' REG
    ;

add
    : 'ADD' REG REG
    ;

sub
    : 'SUB' REG REG
    ;

mul
    : 'MUL' REG REG
    ;

div
    : 'DIV' REG REG
    ;

and
    : 'AND' REG REG
    ;

or
    : 'OR' REG REG
    ;

xor
    : 'XOR' REG REG
    ;

not
    : 'NOT' REG
    ;

inc
    : 'INC' REG
    ;

dec
    : 'DEC' REG
    ;

cmp
    : 'CMP' REG REG
    ;

shl
    : 'SHL' REG
    ;

shr
    : 'SHR' REG
    ;

in
    : 'IN' REG
    ;

out
    : 'OUT' REG
    ;

jnz
    : 'JNZ' (NUMBER | LABEL)
    ;

jz
    : 'JZ' (NUMBER | LABEL)
    ;

jmp
    : 'JMP' (NUMBER | LABEL)
    ;

jmpr
    : 'JMPR' REG
    ;

call
    : 'CALL' (NUMBER | LABEL)
    ;

callr
    : 'CALLR' REG
    ;

ret
    : 'RET'
    ;


NUMBER
    : '0' | [1-9] | [1-9][0-9] | '1'[0-9][0-9] | '2'[0-5][0-5] | '0x'[0-9A-F][0-9A-F]*
    ;

STRING
    : '"' (~["\\\r\n] | ('\\' [bstnfr"'\\]))* '"'
    ;

REG
    : 'R'[0-9] | 'R1'[0-5]
    ;

LABEL
    : [a-zA-Z_][a-zA-Z_\-0-9]*
    ;

COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\n\r]+ -> skip
    ;
