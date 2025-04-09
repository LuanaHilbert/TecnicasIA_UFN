import random
from util import Util

class Cromossomo:
    def __init__(self, rota):
        self.rota = rota
        self.aptidao = self.calcular_aptidao()

    def calcular_aptidao(self):
        penalidade = 0
        cidades_vistas = {}

        # Penalidade por inversões
        for i in range(len(self.rota)):
            for j in range(i+1, len(self.rota)):
                if self.rota[i] > self.rota[j]:
                    penalidade += 10

        # Penalidade por cidades repetidas
        for cidade in self.rota:
            cidades_vistas[cidade] = cidades_vistas.get(cidade, 0) + 1

        for cidade, count in cidades_vistas.items():
            if count > 1:
                # Cada par repetido adiciona 20
                pares_repetidos = (count * (count - 1)) // 2
                penalidade += 20 * pares_repetidos

        return penalidade

    def __str__(self):
        return f'Rota: {"".join(map(str, self.rota))} | Penalidade: {self.aptidao}'

    @staticmethod
    def gerar_populacao(populacao, tamanho_populacao):
        for _ in range(tamanho_populacao):
            rota = Util.gerar_rota()
            populacao.append(Cromossomo(rota))

    @staticmethod
    def exibir_populacao(populacao):
        for individuo in populacao:
            print(individuo)

    @staticmethod
    def selecionar_por_torneio(populacao, nova_populacao, taxa_selecao):
        torneio = []
        qtd_selecionados = int(taxa_selecao * len(populacao) / 100)
        nova_populacao.append(populacao[0])  # elitismo

        i = 1
        while i <= qtd_selecionados:
            c1 = random.choice(populacao)
            c2 = random.choice([c for c in populacao if c != c1])
            c3 = random.choice([c for c in populacao if c != c1 and c != c2])

            torneio = [c1, c2, c3]
            torneio.sort(key=lambda cromossomo: cromossomo.aptidao)  # Menor penalidade = melhor

            selecionado = torneio[0]
            if selecionado not in nova_populacao:
                nova_populacao.append(selecionado)
                i += 1

    @staticmethod
    def reproduzir(populacao, nova_populacao, taxa_reproducao):
        qtd_reproduzidos = int(taxa_reproducao * len(populacao) / 100)
        i = 0
        while i < qtd_reproduzidos:
            pai = random.choice(populacao)
            mae = random.choice([c for c in populacao if c != pai])

            ponto_corte = len(pai.rota) // 2
            filho1_rota = pai.rota[:ponto_corte] + mae.rota[ponto_corte:]
            filho2_rota = mae.rota[:ponto_corte] + pai.rota[ponto_corte:]

            nova_populacao.append(Cromossomo(filho1_rota))
            nova_populacao.append(Cromossomo(filho2_rota))
            i += 2

        # podar caso tenha ultrapassado
        while len(nova_populacao) > len(populacao):
            nova_populacao.pop()

    @staticmethod
    def mutar(populacao):
        qtd_mutantes = random.randint(1, int(len(populacao) / 5))
        for _ in range(qtd_mutantes):
            pos = random.randrange(len(populacao))
            mutante = populacao[pos]

            rota_mutada = mutante.rota[:]
            idx1 = random.randint(0, len(rota_mutada) - 1)
            idx2 = random.randint(0, len(rota_mutada) - 1)
            rota_mutada[idx1], rota_mutada[idx2] = rota_mutada[idx2], rota_mutada[idx1]

            populacao[pos] = Cromossomo(rota_mutada)
