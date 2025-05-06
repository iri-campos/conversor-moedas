package br.com.alura.conversormoedas.modelos;

import java.io.IOException;
import java.util.Scanner;

public class ConversorDeMoedas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaTaxaDeCambio service = new ConsultaTaxaDeCambio();

        while (true) {
            System.out.println("\n=== Conversor de Moedas ===");
            System.out.println("1. USD → BRL");
            System.out.println("2. BRL → USD");
            System.out.println("3. EUR → BRL");
            System.out.println("4. BRL → EUR");
            System.out.println("5. GBP → BRL");
            System.out.println("6. BRL → GBP");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            if (opcao == 7) break;

            System.out.print("Digite o valor a ser convertido: ");
            double valor = scanner.nextDouble();

            String moedaOrigem = "", moedaDestino = "";

            switch (opcao) {
                case 1 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                case 2 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
                case 3 -> { moedaOrigem = "EUR"; moedaDestino = "BRL"; }
                case 4 -> { moedaOrigem = "BRL"; moedaDestino = "EUR"; }
                case 5 -> { moedaOrigem = "GBP"; moedaDestino = "BRL"; }
                case 6 -> { moedaOrigem = "BRL"; moedaDestino = "GBP"; }
                default -> {
                    System.out.println("Opção inválida.");
                    continue;
                }
            }

            try {
                double taxa = service.obterTaxa(moedaOrigem, moedaDestino);
                double convertido = valor * taxa;
                System.out.printf("%.2f %s = %.2f %s%n", valor, moedaOrigem, convertido, moedaDestino);
            } catch (IOException | InterruptedException e) {
                System.out.println("Erro ao obter taxa de câmbio: " + e.getMessage());
            }
        }

        System.out.println("Programa encerrado.");
    }
}
