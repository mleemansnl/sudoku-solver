package main

import "os"

func main() {
	if !ProcessSudoku(os.Stdin, os.Stdout) {
		os.Exit(1)
	}
}
