from cromossomo import Cromossomo

if __name__ == '__main__':
    tamanho_populacao = int(input('Tamanho da população: '))
    taxa_selecao = int(input('Taxa de seleção [25 a 40%]: '))
    taxa_reproducao = 100 - taxa_selecao
    taxa_mutacao = int(input('Taxa de mutação [3 a 10%]: '))
    quantidade_geracoes = int(input('Quantidade de gerações: '))
                            
    populacao = []
    nova_populacao = []

    Cromossomo.gerar_populacao(populacao, tamanho_populacao)
    populacao.sort(key=lambda cromossomo: cromossomo.aptidao)
    print('Geração 0...')
    Cromossomo.exibir_populacao(populacao)

    for i in range(1, quantidade_geracoes + 1):
        Cromossomo.selecionar_por_torneio(populacao, nova_populacao, taxa_selecao)
        Cromossomo.reproduzir(populacao, nova_populacao, taxa_reproducao)
        if i % taxa_mutacao == 0:
            Cromossomo.mutar(nova_populacao)

        populacao = nova_populacao[:]
        nova_populacao.clear()
        populacao.sort(key=lambda cromossomo: cromossomo.aptidao)
        print(f'Geração {i}...')
        Cromossomo.exibir_populacao(populacao)
