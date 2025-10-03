package com.example.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MathTools {

    @Tool(description = "Adds two numbers")
    public static double add(double a, double b) {
        double result = a + b;
        log.info("Addition operation: {} + {} = {}", a, b, result);
        return result;
    }

    @Tool(description = "Subtracts second number from first number")
    public static double subtract(double a, double b) {
        double result = a - b;
        log.info("Subtraction operation: {} - {} = {}", a, b, result);
        return result;
    }

    @Tool(description = "Multiplies two numbers")
    public static double multiply(double a, double b) {
        double result = a * b;
        log.info("Multiplication operation: {} * {} = {}", a, b, result);
        return result;
    }

    @Tool(description = "Divides first number by second number")
    public static double divide(double a, double b) {
        if (b == 0) {
            log.error("Division by zero attempted");
            throw new ArithmeticException("Division by zero");
        }
        double result = a / b;
        log.info("Division operation: {} / {} = {}", a, b, result);
        return result;
    }

    @Tool(description = "Raises base number to the given exponent")
    public static double power(double base, double exponent) {
        double result = Math.pow(base, exponent);
        log.info("Power operation: {} ^ {} = {}", base, exponent, result);
        return result;
    }

    @Tool(description = "Calculates square root of a number")
    public static double squareRoot(double number) {
        if (number < 0) {
            log.error("Square root of negative number attempted: {}", number);
            throw new IllegalArgumentException("Cannot calculate square root of negative number");
        }
        double result = Math.sqrt(number);
        log.info("Square root operation: √{} = {}", number, result);
        return result;
    }

    @Tool(description = "Calculates factorial of a non-negative integer")
    public static long factorial(int n) {
        if (n < 0) {
            log.error("Factorial of negative number attempted: {}", n);
            throw new IllegalArgumentException("Cannot calculate factorial of negative number");
        }
        if (n == 0 || n == 1) {
            log.info("Factorial operation: {}! = 1", n);
            return 1;
        }
        long result = n * factorial(n - 1);
        log.info("Factorial operation: {}! = {}", n, result);
        return result;
    }

    @Tool(description = "Finds minimum value among given numbers")
    public static double min(double... numbers) {
        if (numbers.length == 0) {
            log.error("Minimum operation attempted on empty array");
            throw new IllegalArgumentException("Empty array");
        }
        double min = numbers[0];
        for (double number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        log.info("Minimum operation result: {}", min);
        return min;
    }

    @Tool(description = "Finds maximum value among given numbers")
    public static double max(double... numbers) {
        if (numbers.length == 0) {
            log.error("Maximum operation attempted on empty array");
            throw new IllegalArgumentException("Empty array");
        }
        double max = numbers[0];
        for (double number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        log.info("Maximum operation result: {}", max);
        return max;
    }
}
