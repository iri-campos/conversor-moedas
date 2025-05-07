package br.com.alura.conversormoedas.modelos;

import api.TaxasCambioAPI;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Conversor {
    private final TaxasCambioAPI taxasCambio;

    public Conversor(TaxasCambioAPI taxasCambio) {
        this.taxasCambio = taxasCambio;
    }

    public void realizarConversao (String origem, String destino, Double valor) {
        try {
            Map<String, Double> taxas = taxasCambio.getTaxas(origem);

            if (!taxas.containsKey(destino)) {
                System.out.println("Moeda de destino inválida.");
                return;
            }

            double valorConvertido = taxasCambio.converte(origem, destino, valor);
            NumberFormat formatador = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
            String valorOriginalFormatado = formatador.format(valor);
            String valorConvertidoFormatado = formatador.format(valorConvertido);

            System.out.printf(
                    "O valor %s [%s] corresponde ao valor final de %s [%s]%n",
                    valorOriginalFormatado, origem,
                    valorConvertidoFormatado, destino
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao realizar a conversão: " + e.getMessage());
        }
    }
}
