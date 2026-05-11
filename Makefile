# Makefile for octopus
#
# Copyright (c) Joe Xue (lgxue@hotmail.com) 2026. All rights reserved

Q           = @
PROJECT     = octopus
VERSION     = 0.0.1


JAVA        = java
JAVAC       = javac
JAR         = jar
ANTLR       = deps/antlr-4.13.2-complete.jar

SRC_DIR     = src
BUILD_DIR   = build
GEN_DIR     = $(BUILD_DIR)/_GEN
BIN_DIR     = $(BUILD_DIR)/bin
CLASS_DIR   = $(BUILD_DIR)/class
ST_DIR      = $(CLASS_DIR)/st

PROG        = $(BIN_DIR)/$(PROJECT)
PROG_JAR    = $(BIN_DIR)/$(PROJECT)-$(VERSION).jar

BNF_FILE    = $(SRC_DIR)/bnf/Octopus.g4
GEN_DONE    = $(BUILD_DIR)/gen.done

JAVA_FILES  = $(shell find $(SRC_DIR) -name "*.java")
JAVA_DONE   = $(BUILD_DIR)/java.done

CLASS_PATH  = ".:$(ANTLR):$(CLASS_DIR):$${CLASS_PATH}"

.PHONY: all clean install

all: $(PROG)

$(PROG): $(PROG_JAR)
	$Qecho '#!/bin/bash' > $@
	$Qecho '#' >> $@
	$Qecho '# octopus' >> $@
	$Qecho '# The script to run octopus java jar file' >> $@
	$Qecho '' >> $@
	$Qecho 'dir=$$(dirname $$0)' >> $@
	$Qecho 'java -jar $${dir}/$(PROJECT)-$(VERSION).jar $$*' >> $@
	$Qchmod +x $@

$(PROG_JAR): $(GEN_DONE) $(JAVA_DONE)
	$Qmkdir -p $(@D)
	$Qcp $(ANTLR) $@
	$Q$(JAR) --update --main-class=org.octopus.Octopus --file $@ -C $(CLASS_DIR) .

$(GEN_DONE): $(BNF_FILE)
	echo "Generating the java file from $< ..."
	$Q$(JAVA) -jar $(ANTLR) $< -Xexact-output-dir -o $(GEN_DIR) -package org.octopus
	echo "Compiling the generated java files..."
	$Q$(JAVAC) -cp $(CLASS_PATH) $(GEN_DIR)/*.java -d $(CLASS_DIR)
	$Qtouch $@

$(JAVA_DONE): $(JAVA_FILES) $(GEN_DIR)
	echo "Compiling the java files..."
	$Q$(JAVAC) -cp $(CLASS_PATH) $(JAVA_FILES) -d $(CLASS_DIR)
	$Qtouch $@

install:
	$Qinstall -m 755 -t /usr/local/bin/ $(PROG) $(PROG_JAR)

clean:
	$(Q)$(RM) -rf $(BUILD_DIR)
