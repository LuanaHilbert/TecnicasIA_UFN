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

public class LabirintoObstaculos implements Estado, Heuristica {

    @Override
    public String getDescricao() {
        return "O jogo do labirinto é uma matriz NxM, onde são sorteadas duas peças:\n"
                + "\n"
                + "peça que representa o portal de entrada no labirinto;\n"
                + "peça que representa o portal de saída no labirinto.\n"
                + "A Entrada é o portal em que um personagem qualquer inicia no "
                + "labirinto e precisa se movimentar até a Saída. "
                + "O foco aqui, é chegar na Saída pelo menor número de movimentos (células). "
                + "Entretanto, não pode ser nas diagonais.";
    }

    final char matriz[][]; // preferir "immutable objects"
    int linhaEntrada, colunaEntrada; //guarda a posição do Entrada/E
    int linhaEntrada2, colunaEntrada2; // segunda entrada
    int linhaSaida, colunaSaida;
    final String op; // operacao que gerou o estado


    //atenção.... matrizes precisam ser clonadas ao gerarmos novos estados
    char [][]clonar(char origem[][]) {
        char destino[][] = new char[origem.length][origem.length];
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem.length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
        return destino;
    }

    /**
     * construtor para o estado gerado na evolução/resolução do problema, recebe cada valor de atributo
     */
    public LabirintoObstaculos(char m[][], int linhaEntrada, int colunaEntrada, int linhaEntrada2, int colunaEntrada2, int linhaSaida, int colunaSaida, String o) {
        this.matriz = m; //ter certeza que m foi clonado antes de entrar no construtor
        this.linhaEntrada = linhaEntrada;
        this.colunaEntrada = colunaEntrada;
        this.linhaEntrada2 = linhaEntrada2;
        this.colunaEntrada2 = colunaEntrada2;
        this.linhaSaida = linhaSaida;
        this.colunaSaida = colunaSaida;
        this.op = o;
    }

    /**
     * construtor para o estado INICIAL
     */
    public LabirintoObstaculos(int dimensao, String o, int porcentagemObstaculos) {
        this.matriz = new char[dimensao][dimensao];
        this.op = o;

        int quantidadeObstaculos = (dimensao*dimensao)* porcentagemObstaculos/100;
        System.out.println(quantidadeObstaculos);

        Random gerador = new Random();

        int entrada1 = gerador.nextInt(dimensao * dimensao); // primeira entrada
        int entrada2;
        do {
            entrada2 = gerador.nextInt(dimensao * dimensao); // segunda entrada
        } while (entrada1 == entrada2);

        int saida;
        do {
            saida = gerador.nextInt(dimensao * dimensao); // saída
        } while (entrada1 == saida || entrada2 == saida);

        int contaPosicoes = 0;
        for (int i = 0; i < dimensao; i++) {
            for (int j = 0; j < dimensao; j++) {
                if (contaPosicoes == entrada1) {
                    this.matriz[i][j] = 'E';
                    this.linhaEntrada = i;
                    this.colunaEntrada = j;
                } else if (contaPosicoes == entrada2) {
                    this.matriz[i][j] = 'E';
                    this.linhaEntrada2 = i;
                    this.colunaEntrada2 = j;
                } else if (contaPosicoes == saida) {
                    this.matriz[i][j] = 'S';
                    this.linhaSaida = i;
                    this.colunaSaida = j;
                } else if (quantidadeObstaculos > 0 && gerador.nextInt(3) == 1) {
                    quantidadeObstaculos--;
                    this.matriz[i][j] = '@';
                } else {
                    this.matriz[i][j] = 'O';
                }
                contaPosicoes++;
            }
        }
    }

    /**
     * verifica se o estado e meta
     */
    @Override
    public boolean ehMeta() {
        return (this.linhaEntrada == this.linhaSaida && this.colunaEntrada == this.colunaSaida) ||
                (this.linhaEntrada2 == this.linhaSaida && this.colunaEntrada2 == this.colunaSaida);
    }

    /**
     * ???
     *
     * @return Distancia
     */
    @Override
    public int custo() {
        return 1;
    }

    /**
     * ?????
     *
     * @return Quantidade
     */
    @Override
    public int h() {
        int qtd = 0;

        //será que temos heurística
        return qtd;
    }

    /**
     * gera uma lista de sucessores do nodo.
     */
    @Override
    public List<Estado> sucessores() {
        List<Estado> visitados = new LinkedList<Estado>(); // a lista de sucessores

        // Gera movimentos para a primeira entrada
        paraCima(visitados, 1);
        paraBaixo(visitados, 1);
        paraEsquerda(visitados, 1);
        paraDireita(visitados, 1);

        // Gera movimentos para a segunda entrada
        paraCima(visitados, 2);
        paraBaixo(visitados, 2);
        paraEsquerda(visitados, 2);
        paraDireita(visitados, 2);

        return visitados;
    }

    private void paraCima(List<Estado> visitados, int entrada) {
        int linhaAtual, colunaAtual;
        if (entrada == 1) {
            linhaAtual = this.linhaEntrada;
            colunaAtual = this.colunaEntrada;
        } else {
            linhaAtual = this.linhaEntrada2;
            colunaAtual = this.colunaEntrada2;
        }

        // Verifica se o movimento é válido
        if (linhaAtual == 0 || this.matriz[linhaAtual - 1][colunaAtual] == '@') return;

        // Clona a matriz para evitar alterar o estado atual
        char mTemp[][] = clonar(this.matriz);

        // Move a entrada para cima
        mTemp[linhaAtual][colunaAtual] = 'O';
        mTemp[linhaAtual - 1][colunaAtual] = 'E';

        // Cria um novo estado com a entrada movida
        LabirintoObstaculos novo;
        if (entrada == 1) {
            novo = new LabirintoObstaculos(mTemp, linhaAtual - 1, colunaAtual, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo entrada 1 para cima");
        } else {
            novo = new LabirintoObstaculos(mTemp, this.linhaEntrada, this.colunaEntrada, linhaAtual - 1, colunaAtual, this.linhaSaida, this.colunaSaida, "Movendo entrada 2 para cima");
        }

        // Adiciona o novo estado à lista de sucessores, se não estiver presente
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraBaixo(List<Estado> visitados, int entrada) {
        int linhaAtual, colunaAtual;
        if (entrada == 1) {
            linhaAtual = this.linhaEntrada;
            colunaAtual = this.colunaEntrada;
        } else {
            linhaAtual = this.linhaEntrada2;
            colunaAtual = this.colunaEntrada2;
        }

        // Verifica se o movimento é válido
        if (linhaAtual == this.matriz.length - 1 || this.matriz[linhaAtual + 1][colunaAtual] == '@') return;

        // Clona a matriz para evitar alterar o estado atual
        char mTemp[][] = clonar(this.matriz);

        // Move a entrada para baixo
        mTemp[linhaAtual][colunaAtual] = 'O';
        mTemp[linhaAtual + 1][colunaAtual] = 'E';

        // Cria um novo estado com a entrada movida
        LabirintoObstaculos novo;
        if (entrada == 1) {
            novo = new LabirintoObstaculos(mTemp, linhaAtual + 1, colunaAtual, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo entrada 1 para baixo");
        } else {
            novo = new LabirintoObstaculos(mTemp, this.linhaEntrada, this.colunaEntrada, linhaAtual + 1, colunaAtual, this.linhaSaida, this.colunaSaida, "Movendo entrada 2 para baixo");
        }

        // Adiciona o novo estado à lista de sucessores, se não estiver presente
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraEsquerda(List<Estado> visitados, int entrada) {
        int linhaAtual, colunaAtual;
        if (entrada == 1) {
            linhaAtual = this.linhaEntrada;
            colunaAtual = this.colunaEntrada;
        } else {
            linhaAtual = this.linhaEntrada2;
            colunaAtual = this.colunaEntrada2;
        }

        // Verifica se o movimento é válido
        if (colunaAtual == 0 || this.matriz[linhaAtual][colunaAtual - 1] == '@') return;

        // Clona a matriz para evitar alterar o estado atual
        char mTemp[][] = clonar(this.matriz);

        // Move a entrada para a esquerda
        mTemp[linhaAtual][colunaAtual] = 'O';
        mTemp[linhaAtual][colunaAtual - 1] = 'E';

        // Cria um novo estado com a entrada movida
        LabirintoObstaculos novo;
        if (entrada == 1) {
            novo = new LabirintoObstaculos(mTemp, linhaAtual, colunaAtual - 1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo entrada 1 para esquerda");
        } else {
            novo = new LabirintoObstaculos(mTemp, this.linhaEntrada, this.colunaEntrada, linhaAtual, colunaAtual - 1, this.linhaSaida, this.colunaSaida, "Movendo entrada 2 para esquerda");
        }

        // Adiciona o novo estado à lista de sucessores, se não estiver presente
        if (!visitados.contains(novo)) visitados.add(novo);
    }

    private void paraDireita(List<Estado> visitados, int entrada) {
        int linhaAtual, colunaAtual;
        if (entrada == 1) {
            linhaAtual = this.linhaEntrada;
            colunaAtual = this.colunaEntrada;
        } else {
            linhaAtual = this.linhaEntrada2;
            colunaAtual = this.colunaEntrada2;
        }

        // Verifica se o movimento é válido
        if (colunaAtual == this.matriz.length - 1 || this.matriz[linhaAtual][colunaAtual + 1] == '@') return;

        // Clona a matriz para evitar alterar o estado atual
        char mTemp[][] = clonar(this.matriz);

        // Move a entrada para a direita
        mTemp[linhaAtual][colunaAtual] = 'O';
        mTemp[linhaAtual][colunaAtual + 1] = 'E';

        // Cria um novo estado com a entrada movida
        LabirintoObstaculos novo;
        if (entrada == 1) {
            novo = new LabirintoObstaculos(mTemp, linhaAtual, colunaAtual + 1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo entrada 1 para direita");
        } else {
            novo = new LabirintoObstaculos(mTemp, this.linhaEntrada, this.colunaEntrada, linhaAtual, colunaAtual + 1, this.linhaSaida, this.colunaSaida, "Movendo entrada 2 para direita");
        }

        // Adiciona o novo estado à lista de sucessores, se não estiver presente
        if (!visitados.contains(novo)) visitados.add(novo);
    }


    /**
     * verifica se um estado e igual a outro (usado para poda)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof LabirintoObstaculos) {
            LabirintoObstaculos e = (LabirintoObstaculos) o;
            for (int i = 0; i < e.matriz.length; i++) {
                for (int j = 0; j < e.matriz.length; j++) {
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
        String estado = "";

        for (int i = 0; i < this.matriz.length; i++) {
            for (int j = 0; j < this.matriz.length; j++) {
                estado = estado + this.matriz[i][j];
            }
        }
        return estado.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer resultado = new StringBuffer();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                resultado.append(this.matriz[i][j]);
                resultado.append("\t");
            }
            resultado.append("\n");
        }
        resultado.append("Posição Entrada: " + this.linhaEntrada + "," + this.colunaEntrada +"\n");
        resultado.append("Posição Saida: " + this.linhaSaida + "," + this.colunaSaida +"\n");
        return "\n"+ op + "\n" + resultado + "\n\n";
    }

    public static void main(String[] a) {
        LabirintoObstaculos estadoInicial = null;
        int dimensao;
        int porcentagemObstaculos;
        int qualMetodo1, qualMetodo2;
        Nodo n1, n2;
        try {
            dimensao = Integer.parseInt(JOptionPane.showInputDialog(null,"Entre com a dimensão do Puzzle!"));
            porcentagemObstaculos = Integer.parseInt(JOptionPane.showInputDialog(null,"Porcentagem de obstáculos!"));
            qualMetodo1 = Integer.parseInt(JOptionPane.showInputDialog(null,"1 - Profundidade\n2 - Largura\nEscolha o método para a primeira entrada:"));
            qualMetodo2 = Integer.parseInt(JOptionPane.showInputDialog(null,"1 - Profundidade\n2 - Largura\nEscolha o método para a segunda entrada:"));
            estadoInicial = new LabirintoObstaculos(dimensao, "estado inicial", porcentagemObstaculos);

            // Busca para a primeira entrada
            switch (qualMetodo1) {
                case 1:
                    System.out.println("busca em PROFUNDIDADE para a primeira entrada");
                    n1 = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                case 2:
                    System.out.println("busca em LARGURA para a primeira entrada");
                    n1 = new BuscaLargura(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                default:
                    n1 = null;
                    JOptionPane.showMessageDialog(null, "Método não implementado para a primeira entrada");
            }

            // Busca para a segunda entrada
            switch (qualMetodo2) {
                case 1:
                    System.out.println("busca em PROFUNDIDADE para a segunda entrada");
                    n2 = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                case 2:
                    System.out.println("busca em LARGURA para a segunda entrada");
                    n2 = new BuscaLargura(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                default:
                    n2 = null;
                    JOptionPane.showMessageDialog(null, "Método não implementado para a segunda entrada");
            }

            // Comparação das soluções
            if (n1 == null || n2 == null) {
                System.out.println("sem solucao para uma ou ambas as entradas!");
                System.out.println(estadoInicial);
            } else {
                System.out.println("solucao para a primeira entrada:\n" + n1.montaCaminho() + "\n\n");
                System.out.println("solucao para a segunda entrada:\n" + n2.montaCaminho() + "\n\n");

                // Comparação do número de passos
                int passos1 = n1.montaCaminho().split("\n").length;
                int passos2 = n2.montaCaminho().split("\n").length;

                if (passos1 < passos2) {
                    System.out.println("A primeira entrada encontrou uma solução mais rápida!");
                } else if (passos1 > passos2) {
                    System.out.println("A segunda entrada encontrou uma solução mais rápida!");
                } else {
                    System.out.println("Ambas as entradas encontraram soluções com o mesmo número de passos!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        System.exit(0);
    }
}