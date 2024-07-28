package validation;

import algorithms.BubbleSort;
import algorithms.QuickSort;
import algorithms.SelectionSort;
import model.Algorithms;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class InputValidation<T extends Comparable<T>> {

    private final Set<String> POSSIBLE_ARGS = Set.of("a", "t", "o", "in", "v", "s");
    private boolean isRandom = false; // Indica se os valores serão gerados aleatoriamente
    private String orderType = "AZ"; // Tipo de ordenação padrão
    private String algoType = "S"; // Tipo de algoritmo padrão
    private T[] validatedArray; // Array validado

    // Construtor que recebe os argumentos e valida
    public InputValidation(String[] args) {
        validateArgs(args);
    }

    // Método para validar os argumentos
    public void validateArgs(String[] args) {
        boolean allValid = true; // Indicador de validade geral
        Set<String> argumentTypes = new HashSet<>(); // Conjunto de tipos de argumentos recebidos

        // Verifica se nenhum argumento foi fornecido
        if (args.length == 0) {
            System.err.println("Nenhum argumento fornecido!");
            allValid = false;
        }

        // Loop através dos argumentos fornecidos
        for (String arg : args) {
            String[] splitArg = arg.split("="); // Divide argumento em chave e valor
            if (splitArg.length != 2) {
                System.err.println("Argumento inválido: " + arg);
                allValid = false;
                continue;
            }

            String key = splitArg[0]; // Chave do argumento
            String value = splitArg[1]; // Valor do argumento

            // Verifica se a chave está nos argumentos possíveis
            if (!POSSIBLE_ARGS.contains(key)) {
                System.err.println("Argumento não reconhecido: " + key);
                allValid = false;
                continue;
            }

            // Validação baseada na chave do argumento
            switch (key) {
                case "s":
                    try {
                        int speed = Integer.parseInt(value); // Converte valor para inteiro
                        if (speed < 100 || speed > 1000) { // Verifica intervalo válido
                            System.err.println("Valor inválido para 's': " + value);
                            allValid = false;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Valor inválido para 's': " + value);
                        allValid = false;
                    }
                    break;
                case "in":
                    // Verifica valores válidos para 'in'
                    if (!(value.equals("M") || value.equals("m") || value.equals("r"))) {
                        System.err.println("Valor inválido para 'in': " + value);
                        allValid = false;
                    } else if (value.equals("r")) {
                        isRandom = true; // Define random se valor for 'r'
                    }
                    break;
                case "t":
                    // Verifica valores válidos para 't'
                    if (!(value.equals("N") || value.equals("n") || value.equals("C") || value.equals("c"))) {
                        System.err.println("Valor inválido para 't': " + value);
                        allValid = false;
                    }
                    break;
                case "o":
                    // Verifica valores válidos para 'o'
                    if (!(value.equals("AZ") || value.equals("az") || value.equals("ZA") || value.equals("za"))) {
                        System.err.println("Valor inválido para 'o': " + value);
                        allValid = false;
                    } else {
                        orderType = value; // Define tipo de ordenação
                    }
                    break;
                case "a":
                    // Verifica valores válidos para 'a'
                    if (!(value.equals("S") || value.equals("s") || value.equals("Q") || value.equals("q") || value.equals("B") || value.equals("b"))) {
                        System.err.println("Valor inválido para 'a': " + value);
                        allValid = false;
                    } else {
                        algoType = value; // Define tipo de algoritmo
                    }
                    break;
                case "v":
                    // Valida o array 'v'
                    String[] values = value.split(",");
                    Class<?> type = null;

                    // Verifica o tipo dos valores no array
                    for (String v : values) {
                        if (type == null) {
                            type = getType(v);
                        } else if (!type.equals(getType(v))) {
                            System.err.println("Array 'v' deve conter apenas um tipo de dado.");
                            allValid = false;
                            break;
                        }
                    }

                    if (allValid) {
                        // Inicializa o array validado com o tipo apropriado
                        if (type == Integer.class) {
                            validatedArray = (T[]) new Integer[values.length];
                            for (int i = 0; i < values.length; i++) {
                                validatedArray[i] = (T) Integer.valueOf(values[i]);
                            }
                        } else if (type == Character.class) {
                            validatedArray = (T[]) new Character[values.length];
                            for (int i = 0; i < values.length; i++) {
                                validatedArray[i] = (T) Character.valueOf(values[i].charAt(0));
                            }
                        }
                    }
                    break;
                default:
                    System.err.println("Argumento não reconhecido: " + key);
                    allValid = false;
            }
        }

        // Se todos os argumentos são válidos, prossegue com a ordenação
        if (allValid) {
            System.out.println("Todos os argumentos são válidos:");
            for (String arg : args) {
                System.out.println(arg);
            }

            // Passa os argumentos validados para o algoritmo de ordenação
            sortArray();
        }
    }

    // Método para obter o tipo do valor (inteiro ou char)
    private Class<?> getType(String value) {
        try {
            Integer.parseInt(value);
            return Integer.class;
        } catch (NumberFormatException e) {
            return Character.class;
        }
    }

    // Método para ordenar o array validado
    private void sortArray() {
        if (validatedArray == null && !isRandom) {
            System.err.println("Array 'v' não foi fornecido ou contém dados inválidos.");
            return;
        }

        // Gera array random
        if (isRandom) {
            validatedArray = (T[]) new Integer[10]; // define o tamanho desejado do array random
            Random random = new Random();
            for (int i = 0; i < validatedArray.length; i++) {
                validatedArray[i] = (T) Integer.valueOf(random.nextInt(40) + 1);
            }
        }

        Algorithms<T> algorithm;

        // Seleciona o algoritmo de ordenação com base no tipo
        switch (algoType.toLowerCase()) {
            case "s":
                algorithm = new SelectionSort<>();
                break;
            case "q":
                algorithm = new QuickSort<>();
                break;
            case "b":
                algorithm = new BubbleSort<>();
                break;
            default:
                throw new IllegalStateException("Tipo de algoritmo inválido: " + algoType);
        }

        algorithm.sort(validatedArray); // Ordena o array

        // Inverte a ordem se necessário
        if (orderType.equalsIgnoreCase("ZA")) {
            for (int i = 0; i < validatedArray.length / 2; i++) {
                T temp = validatedArray[i];
                validatedArray[i] = validatedArray[validatedArray.length - 1 - i];
                validatedArray[validatedArray.length - 1 - i] = temp;
            }
        }

        algorithm.printArray(validatedArray); // Imprime o array ordenado
    }
}