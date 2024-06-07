package com.sudokusolver.main;

import java.io.BufferedReader;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class Main {

    private Main() {
    }

    public static void main(String args[]) {
        boolean result = true;

        // try with resources (ensures resources are closed afterwards)
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                Writer output = new BufferedWriter(new OutputStreamWriter(System.out));) {
            // Solve sudoku from input and write to output
            result = SudokuProcessor.processSudoku(input, output);
        } catch (IOException e) {
            result = false;
        }

        if (!result) {
            System.exit(1);
        }
    }
}
