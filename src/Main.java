import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите пример (например, 5 + 3 или I * V):");
            String input = scanner.nextLine().trim();

            try {
                InputValidator.validateInput(input);
                String[] array = input.split("\\s+");
                int leftit = InputValidator.parseOperand(array[0]);
                int rightit = InputValidator.parseOperand(array[2]);
                String op = array[1];

                String result = Calculator.calculate(leftit, rightit, op, array[0]);
                System.out.println(result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.exit(1); // Завершение программы при ошибке валидации
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
                System.exit(1); // Завершение программы при непредвиденной ошибке
            }
        }
    }

    static class InputValidator {
        public static void validateInput(String input) {
            if (!input.matches("^\\w+\\s*[+\\-*/]\\s*\\w+$")) {
                throw new IllegalArgumentException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            }
        }

        public static int parseOperand(String operand) {
            boolean isRoman = isRoman(operand);
            int value;
            if (isRoman) {
                value = RomanNumeral.romanToArabic(operand);
            } else {
                try {
                    value = Integer.parseInt(operand);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Неверный формат числа: " + operand);
                }
            }

            if (value <= 0 || value > 10) {
                throw new IllegalArgumentException("Числа должны быть от 1 до 10");
            }

            return value;
        }

        public static boolean isRoman(String str) {
            return str.matches("[IVXLCDM]+");
        }
    }

    static class Calculator {
        public static String calculate(int leftit, int rightit, String op, String operand) {
            int result;
            switch (op) {
                case "+": result = leftit + rightit; break;
                case "-": result = leftit - rightit; break;
                case "*": result = leftit * rightit; break;
                case "/":
                    if (rightit == 0) throw new IllegalArgumentException("Делить на ноль нельзя");
                    result = leftit / rightit;
                    break;
                default: throw new IllegalArgumentException("Неизвестная операция: " + op);
            }

            if (result < 1 && InputValidator.isRoman(operand)) {
                throw new IllegalArgumentException("Результат не может быть отрицательным или равен нулю в римской системе");
            }

            return InputValidator.isRoman(operand) ? RomanNumeral.arabicToRoman(result) : String.valueOf(result);
        }
    }

    static class RomanNumeral {
        public static int romanToArabic(String roman) {
            int result = 0;
            for (int i = 0; i < roman.length(); i++) {
                int current = charToValue(roman.charAt(i));
                int next = (i + 1 < roman.length()) ? charToValue(roman.charAt(i + 1)) : 0;
                result += (current < next) ? -current : current;
            }
            return result;
        }

        private static int charToValue(char c) {
            switch (c) {
                case 'I': return 1;
                case 'V': return 5;
                case 'X': return 10;
                case 'L': return 50;
                case 'C': return 100;
                default: throw new IllegalArgumentException("Неверный символ в римском числе: " + c);
            }
        }

        public static String arabicToRoman(int number) {
            if (number <= 0) throw new IllegalArgumentException("Римские числа не могут быть меньше или равны нулю");

            StringBuilder roman = new StringBuilder();
            String[] romanSymbols = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
            int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};

            for (int i = 0; i < values.length; i++) {
                while (number >= values[i]) {
                    roman.append(romanSymbols[i]);
                    number -= values[i];
                }
            }

            return roman.toString();
        }
    }
}