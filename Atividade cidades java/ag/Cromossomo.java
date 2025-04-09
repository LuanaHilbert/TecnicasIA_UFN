package ag;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Cromossomo {
    private final int[] rota;
    private final int aptidao;

    public Cromossomo(int[] rota) {
        this.rota = rota;
        this.aptidao = calcularAptidao();
    }

    private int calcularAptidao() {
        int penalidade = 0;
        
        // Penalidade por inversões
        for (int i = 0; i < rota.length; i++) {
            for (int j = i + 1; j < rota.length; j++) {
                if (rota[i] > rota[j]) {
                    penalidade += 10;
                }
            }
        }

        // Penalidade por cidades repetidas
        Map<Integer, Integer> cidadesVistas = new HashMap<>();
        for (int cidade : rota) {
            cidadesVistas.put(cidade, cidadesVistas.getOrDefault(cidade, 0) + 1);
        }

        for (int count : cidadesVistas.values()) {
            if (count > 1) {
                int paresRepetidos = (count * (count - 1)) / 2;
                penalidade += 20 * paresRepetidos;
            }
        }

        return penalidade;
    }

    public int[] getRota() {
        return rota;
    }

    public int getAptidao() {
        return aptidao;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int cidade : rota) {
            sb.append(cidade);
        }
        return "Rota: " + sb.toString() + " | Penalidade: " + aptidao;
    }

    public static void gerarPopulacao(List<Cromossomo> populacao, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            populacao.add(new Cromossomo(Util.gerarRota()));
        }
    }

    public static void exibirPopulacao(List<Cromossomo> populacao) {
        for (Cromossomo c : populacao) {
            System.out.println(c);
        }
    }

    public static void selecionarPorTorneio(List<Cromossomo> populacao, List<Cromossomo> novaPopulacao, int taxaSelecao) {
        Random random = new Random();
        int qtdSelecionados = (taxaSelecao * populacao.size()) / 100;
        novaPopulacao.add(populacao.get(0)); // Elitismo

        int i = 1;
        while (i <= qtdSelecionados) {
            Cromossomo c1 = populacao.get(random.nextInt(populacao.size()));
            Cromossomo c2 = populacao.get(random.nextInt(populacao.size()));
            while (c2 == c1) {
                c2 = populacao.get(random.nextInt(populacao.size()));
            }
            Cromossomo c3 = populacao.get(random.nextInt(populacao.size()));
            while (c3 == c1 || c3 == c2) {
                c3 = populacao.get(random.nextInt(populacao.size()));
            }

            List<Cromossomo> torneio = new ArrayList<>();
            torneio.add(c1);
            torneio.add(c2);
            torneio.add(c3);
            torneio.sort(Comparator.comparingInt(Cromossomo::getAptidao));

            Cromossomo selecionado = torneio.get(0);
            if (!novaPopulacao.contains(selecionado)) {
                novaPopulacao.add(selecionado);
                i++;
            }
        }
    }

    public static void reproduzir(List<Cromossomo> populacao, List<Cromossomo> novaPopulacao, int taxaReproducao) {
        Random random = new Random();
        int qtdReproduzidos = (taxaReproducao * populacao.size()) / 100;
        int i = 0;

        while (i < qtdReproduzidos) {
            Cromossomo pai = populacao.get(random.nextInt(populacao.size()));
            Cromossomo mae = populacao.get(random.nextInt(populacao.size()));
            while (mae == pai) {
                mae = populacao.get(random.nextInt(populacao.size()));
            }

            int pontoCorte = pai.getRota().length / 2;
            
            int[] filho1 = new int[pai.getRota().length];
            System.arraycopy(pai.getRota(), 0, filho1, 0, pontoCorte);
            System.arraycopy(mae.getRota(), pontoCorte, filho1, pontoCorte, mae.getRota().length - pontoCorte);

            int[] filho2 = new int[mae.getRota().length];
            System.arraycopy(mae.getRota(), 0, filho2, 0, pontoCorte);
            System.arraycopy(pai.getRota(), pontoCorte, filho2, pontoCorte, pai.getRota().length - pontoCorte);

            novaPopulacao.add(new Cromossomo(filho1));
            novaPopulacao.add(new Cromossomo(filho2));
            i += 2;
        }

        while (novaPopulacao.size() > populacao.size()) {
            novaPopulacao.remove(novaPopulacao.size() - 1);
        }
    }

    public static void mutar(List<Cromossomo> populacao) {
        Random random = new Random();
        int qtdMutantes = random.nextInt(populacao.size() / 5) + 1;

        for (int i = 0; i < qtdMutantes; i++) {
            int pos = random.nextInt(populacao.size());
            Cromossomo mutante = populacao.get(pos);
            int[] novaRota = mutante.getRota().clone();
            
            int idx1 = random.nextInt(novaRota.length);
            int idx2 = random.nextInt(novaRota.length);
            while (idx2 == idx1) {
                idx2 = random.nextInt(novaRota.length);
            }
            
            int temp = novaRota[idx1];
            novaRota[idx1] = novaRota[idx2];
            novaRota[idx2] = temp;
            
            populacao.set(pos, new Cromossomo(novaRota));
        }
    }
}