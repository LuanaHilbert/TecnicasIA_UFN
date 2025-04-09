import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AGMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Tamanho da população: ");
        int tamanhoPopulacao = scanner.nextInt();
        
        System.out.print("Taxa de seleção [25 a 40%]: ");
        int taxaSelecao = scanner.nextInt();
        int taxaReproducao = 100 - taxaSelecao;
        
        System.out.print("Taxa de mutação [3 a 10%]: ");
        int taxaMutacao = scanner.nextInt();
        
        System.out.print("Quantidade de gerações: ");
        int quantidadeGeracoes = scanner.nextInt();

        List<Cromossomo> populacao = new ArrayList<>();
        List<Cromossomo> novaPopulacao = new ArrayList<>();

        Cromossomo.gerarPopulacao(populacao, tamanhoPopulacao);
        populacao.sort(Comparator.comparingInt(Cromossomo::getAptidao));
        System.out.println("Geração 0...");
        Cromossomo.exibirPopulacao(populacao);

        for (int i = 1; i <= quantidadeGeracoes; i++) {
            Cromossomo.selecionarPorTorneio(populacao, novaPopulacao, taxaSelecao);
            Cromossomo.reproduzir(populacao, novaPopulacao, taxaReproducao);
            
            if (i % taxaMutacao == 0) {
                Cromossomo.mutar(novaPopulacao);
            }

            populacao = new ArrayList<>(novaPopulacao);
            novaPopulacao.clear();
            populacao.sort(Comparator.comparingInt(Cromossomo::getAptidao));
            
            System.out.println("Geração " + i + "...");
            Cromossomo.exibirPopulacao(populacao);
        }
        scanner.close();
    }
}