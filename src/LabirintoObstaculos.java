import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import busca.Heuristica;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.AEstrela;
import busca.Estado;
import busca.MostraStatusConsole;
import busca.Nodo;
import javax.swing.JOptionPane;

// ... (mesmos imports)
import java.util.HashSet;

public class LabirintoObstaculos implements Estado, Heuristica {

    // Agora a matriz é compartilhada entre os dois caminhos
    final char matriz[][];
    int linhaEntrada, colunaEntrada; // primeira entrada
    int linhaEntrada2, colunaEntrada2; // segunda entrada
    int linhaSaida, colunaSaida;
    final String op;

    char [][]clonar(char origem[][]) {
        char destino[][] = new char[origem.length][origem[0].length];
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem[i].length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
        return destino;
    }

    public LabirintoObstaculos(char m[][], int linhaEntrada, int colunaEntrada, int linhaSaida, int colunaSaida, String o) {
        this.matriz = m;
        this.linhaEntrada = linhaEntrada;
        this.colunaEntrada = colunaEntrada;
        this.linhaSaida = linhaSaida;
        this.colunaSaida = colunaSaida;
        this.op = o;
    }

    public LabirintoObstaculos(int dimensao, String o, int porcentagemObstaculos) {
        this.matriz = new char[dimensao][dimensao];
        this.op = o;

        int totalCelulas = dimensao * dimensao;
        int quantidadeObstaculos = totalCelulas * porcentagemObstaculos / 100;

        Random gerador = new Random();
        HashSet<Integer> usados = new HashSet<>();

        int entrada1, entrada2, saida;
        do {
            entrada1 = gerador.nextInt(totalCelulas);
        } while (!usados.add(entrada1));
        do {
            entrada2 = gerador.nextInt(totalCelulas);
        } while (!usados.add(entrada2));
        do {
            saida = gerador.nextInt(totalCelulas);
        } while (!usados.add(saida));

        int pos = 0;
        for (int i = 0; i < dimensao; i++) {
            for (int j = 0; j < dimensao; j++) {
                if (pos == entrada1) {
                    this.matriz[i][j] = 'A'; // entrada A
                    this.linhaEntrada = i;
                    this.colunaEntrada = j;
                } else if (pos == entrada2) {
                    this.matriz[i][j] = 'B'; // entrada B
                    this.linhaEntrada2 = i;
                    this.colunaEntrada2 = j;
                } else if (pos == saida) {
                    this.matriz[i][j] = 'S';
                    this.linhaSaida = i;
                    this.colunaSaida = j;
                } else if (quantidadeObstaculos > 0 && gerador.nextInt(3) == 1) {
                    quantidadeObstaculos--;
                    this.matriz[i][j] = '@';
                } else {
                    this.matriz[i][j] = 'O';
                }
                pos++;
            }
        }
    }

    @Override
    public boolean ehMeta() {
        return this.linhaEntrada == this.linhaSaida && this.colunaEntrada == this.colunaSaida;
    }

    @Override
    public int custo() {
        return 1;
    }

    @Override
    public int h() {
        return Math.abs(linhaEntrada - linhaSaida) + Math.abs(colunaEntrada - colunaSaida);
    }

    @Override
    public List<Estado> sucessores() {
        List<Estado> visitados = new LinkedList<>();
        paraCima(visitados);
        paraBaixo(visitados);
        paraEsquerda(visitados);
        paraDireita(visitados);
        return visitados;
    }

    private void paraCima(List<Estado> visitados) {
        if (this.linhaEntrada == 0 || this.matriz[this.linhaEntrada - 1][this.colunaEntrada] == '@') return;

        char mTemp[][];
        mTemp = clonar(this.matriz);
        int linhaTemp = this.linhaEntrada - 1;
        int colunaTemp = this.colunaEntrada;

        mTemp[this.linhaEntrada][this.colunaEntrada] = 'O';
        mTemp[linhaTemp][colunaTemp] = 'E';

        LabirintoObstaculos novo = new LabirintoObstaculos(mTemp, linhaTemp, colunaTemp, this.linhaSaida, this.colunaSaida, "Movendo para cima");
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraBaixo(List<Estado> visitados) {
        if (this.linhaEntrada == this.matriz.length-1 || this.matriz[this.linhaEntrada + 1][this.colunaEntrada] == '@') return;

        char mTemp[][];
        mTemp = clonar(this.matriz);
        int linhaTemp = this.linhaEntrada + 1;
        int colunaTemp = this.colunaEntrada;

        mTemp[this.linhaEntrada][this.colunaEntrada] = 'O';
        mTemp[linhaTemp][colunaTemp] = 'E';

        LabirintoObstaculos novo = new LabirintoObstaculos(mTemp, linhaTemp, colunaTemp, this.linhaSaida, this.colunaSaida, "Movendo para baixo");
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraEsquerda(List<Estado> visitados) {
        if (this.colunaEntrada == 0 || this.matriz[this.linhaEntrada][this.colunaEntrada - 1] == '@') return;

        char mTemp[][];
        mTemp = clonar(this.matriz);
        int linhaTemp = this.linhaEntrada;
        int colunaTemp = this.colunaEntrada - 1;

        mTemp[this.linhaEntrada][this.colunaEntrada] = 'O';
        mTemp[linhaTemp][colunaTemp] = 'E';

        LabirintoObstaculos novo = new LabirintoObstaculos(mTemp, linhaTemp, colunaTemp, this.linhaSaida, this.colunaSaida,"Movendo para esquerda");
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraDireita(List<Estado> visitados) {
        if (this.colunaEntrada == this.matriz.length-1 || this.matriz[this.linhaEntrada][this.colunaEntrada + 1] == '@') return;

        char mTemp[][];
        mTemp = clonar(this.matriz);
        int linhaTemp = this.linhaEntrada;
        int colunaTemp = this.colunaEntrada + 1;

        mTemp[this.linhaEntrada][this.colunaEntrada] = 'O';
        mTemp[linhaTemp][colunaTemp] = 'E';

        LabirintoObstaculos novo = new LabirintoObstaculos(mTemp, linhaTemp, colunaTemp, this.linhaSaida, this.colunaSaida,"Movendo para direita");
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LabirintoObstaculos) {
            LabirintoObstaculos e = (LabirintoObstaculos) o;
            for (int i = 0; i < e.matriz.length; i++) {
                for (int j = 0; j < e.matriz[i].length; j++) {
                    if (e.matriz[i][j] != this.matriz[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        StringBuilder estado = new StringBuilder();
        for (char[] linha : matriz) {
            for (char c : linha) {
                estado.append(c);
            }
        }
        return estado.toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        for (char[] linha : matriz) {
            for (char c : linha) {
                resultado.append(c).append("\t");
            }
            resultado.append("\n");
        }
        resultado.append("Entrada A: ").append(linhaEntrada).append(",").append(colunaEntrada).append("\n");
        resultado.append("Entrada B: ").append(linhaEntrada2).append(",").append(colunaEntrada2).append("\n");
        resultado.append("Saída: ").append(linhaSaida).append(",").append(colunaSaida).append("\n");
        return "\n" + op + "\n" + resultado + "\n";
    }

    @Override
    public String getDescricao() {
        return toString(); // ou qualquer outra descrição do estado
    }

    public static void main(String[] args) {
        try {
            int dimensao = Integer.parseInt(JOptionPane.showInputDialog("Entre com a dimensão do labirinto:"));
            int porcentagemObstaculos = Integer.parseInt(JOptionPane.showInputDialog("Porcentagem de obstáculos:"));
            int metodoA = Integer.parseInt(JOptionPane.showInputDialog("Método para entrada A:\n1 - Profundidade\n2 - Largura"));
            int metodoB = Integer.parseInt(JOptionPane.showInputDialog("Método para entrada B:\n1 - Profundidade\n2 - Largura"));

            LabirintoObstaculos labirintoCompleto = new LabirintoObstaculos(dimensao, "estado inicial", porcentagemObstaculos);

            // Criar estados separados para cada entrada
            char[][] m1 = labirintoCompleto.clonar(labirintoCompleto.matriz);
            m1[labirintoCompleto.linhaEntrada2][labirintoCompleto.colunaEntrada2] = 'O'; // remove entrada B
            LabirintoObstaculos estadoA = new LabirintoObstaculos(m1, labirintoCompleto.linhaEntrada, labirintoCompleto.colunaEntrada, labirintoCompleto.linhaSaida, labirintoCompleto.colunaSaida, "Entrada A");

            char[][] m2 = labirintoCompleto.clonar(labirintoCompleto.matriz);
            m2[labirintoCompleto.linhaEntrada][labirintoCompleto.colunaEntrada] = 'O'; // remove entrada A
            LabirintoObstaculos estadoB = new LabirintoObstaculos(m2, labirintoCompleto.linhaEntrada2, labirintoCompleto.colunaEntrada2, labirintoCompleto.linhaSaida, labirintoCompleto.colunaSaida, "Entrada B");

            Nodo nA = null, nB = null;
            System.out.println("Solução para entrada A usando método " + (metodoA == 1 ? "Profundidade" : "Largura") + ":");
            switch (metodoA) {
                case 1 -> nA = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoA);
                case 2 -> nA = new BuscaLargura(new MostraStatusConsole()).busca(estadoA);
                default -> JOptionPane.showMessageDialog(null, "Método A inválido.");
            }

            System.out.println("Solução para entrada B usando método " + (metodoB == 1 ? "Profundidade" : "Largura") + ":");
            switch (metodoB) {
                case 1 -> nB = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoB);
                case 2 -> nB = new BuscaLargura(new MostraStatusConsole()).busca(estadoB);
                default -> JOptionPane.showMessageDialog(null, "Método B inválido.");
            }

            System.out.println("Solução para entrada A:");
            System.out.println((nA != null) ? nA.montaCaminho() : "Sem solução");

            System.out.println("Solução para entrada B:");
            System.out.println((nB != null) ? nB.montaCaminho() : "Sem solução");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        System.exit(0);
    }
}
