package com.example.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class MathTools {

    @Tool(description = "Adds two numbers")
    public static double add(double a, double b) {
        return a + b;
    }

    @Tool(description = "Subtracts second number from first number")
    public static double subtract(double a, double b) {
        return a - b;
    }

    @Tool(description = "Multiplies two numbers")
    public static double multiply(double a, double b) {
        return a * b;
    }

    @Tool(description = "Divides first number by second number")
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    @Tool(description = "Raises base number to the given exponent")
    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    @Tool(description = "Calculates square root of a number")
    public static double squareRoot(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of negative number");
        }
        return Math.sqrt(number);
    }

    @Tool(description = "Calculates factorial of a non-negative integer")
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot calculate factorial of negative number");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    @Tool(description = "Finds minimum value among given numbers")
    public static double min(double... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("Empty array");
        }
        double min = numbers[0];
        for (double number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    @Tool(description = "Finds maximum value among given numbers")
    public static double max(double... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("Empty array");
        }
        double max = numbers[0];
        for (double number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }
}
