package org.example;

import org.example.ui.ConsoleUI;
import org.example.validator.CompatibilityValidator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(new Scanner(System.in));
        CompatibilityValidator validator = new CompatibilityValidator();
        new AssemblyProcess(ui, validator).run();
    }
}
