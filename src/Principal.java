import br.com.alura.conversormoedas.api.MoedasAPI;
import br.com.alura.conversormoedas.api.TaxasCambioAPI;
import br.com.alura.conversormoedas.modelos.Conversor;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        try{
            TaxasCambioAPI taxasCambio = new TaxasCambioAPI();
            MoedasAPI moedasAPI = new MoedasAPI();
            Conversor conversor = new Conversor(taxasCambio);
            Scanner scanner = new Scanner(System.in);


            System.out.println("Seja bem-vindo ao conversor de moedas =]");

            int opcao = 0;

            while (true) {
                System.out.println("\nEscolha a opção que deseja");
                System.out.println("1 - Realizar conversão");
                System.out.println("2 - Verificar moedas disponíveis");
                System.out.println("3 - sair\n");

                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    if(opcao == 1) {

                        System.out.print("\nDigite a moeda de origem (ex.: USD, BRL): ");
                        String origem = scanner.nextLine().toUpperCase();

                        Map<String, Double> rates = taxasCambio.getTaxas(origem);

                        if (rates == null) {
                            System.out.println("Moeda de origem '" + origem + "'não encontrada.\nDigite '2' no menu para verificar as moedas disponíveis.");
                            continue;
                        }

                        String destino;

                        while (true) {
                            System.out.print("Digite a moeda de destino (ex.: USD, BRL): ");
                            destino = scanner.nextLine().toUpperCase();

                            if (rates.containsKey(destino)) {
                                break;
                            } else {
                                System.out.println("Moeda de destino '" + destino + "' não encontrada.");
                                System.out.println("Moedas disponíveis: " + rates.keySet());
                                System.out.println("Por favor, informe uma moeda válida.");
                            }
                        }
                        double valor = 0.0;

                        while (true) {
                            System.out.print("Digite o valor para converter: ");
                            String entrada = scanner.nextLine().replace(",", ".");

                            try {
                                valor = Double.parseDouble(entrada);
                                if(valor <= 0) {
                                    System.out.println("O valor deve ser maior que zero.");
                                    continue;
                                }
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Entrada inválida! Informe um valor válido (exemplo: 100,50).");
                            }
                        }

                        conversor.realizarConversao(origem, destino, valor);


                    } else if (opcao == 2) {
                        Map<String, String> moedas = moedasAPI.getMoedas();
                        System.out.println("Moedas disponíveis:");
                        moedas.forEach((codigo, nome) -> System.out.println(codigo + " - " + nome));

                    } else if (opcao == 3) {
                        System.out.println("Programa encerrado.");
                        break;
                    } else {
                        System.out.println("Escolha uma opção válida do menu");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida! Por favor, digite uma opção válida.");
                    scanner.nextLine();
                }

            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao iniciar o programa: " + e.getMessage());
        }
    }
}

