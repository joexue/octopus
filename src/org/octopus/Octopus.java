/*
 * Octopus.java
 *
 * Copyright (c) Joe Xue (lgxue@hotmail.com) 2026. All rights reserved
 */

package org.octopus;

import java.io.PrintWriter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.misc.Interval;

class Octopus
{
    static void help() {
        System.out.println("Usage: Octopus <inputfile> -o <outout file>\n");
    }

    public static void main(String[] args) throws Exception {
        boolean debug = false;
        String outFile = "a.oct";
        String inFile = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-o")) {
                i++;
                if (i < args.length) {
                    outFile = args[i];
                }
            } else if (arg.equals("-d")) {
                debug = true;
            } else {
                inFile = arg;
            }
        }

        if (inFile == null) {
            help();
            return;
        }

        CharStream input;
        input = CharStreams.fromFileName(inFile);

        OctopusLexer lexer = new OctopusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        OctopusParser parser = new OctopusParser(tokens);
        ParseTree tree = parser.asm();

        OctopusHandler handler = new OctopusHandler();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(handler, tree);

        OctopusCore core = new OctopusCore(handler.getCode(), debug);
        core.run();
    }
}
