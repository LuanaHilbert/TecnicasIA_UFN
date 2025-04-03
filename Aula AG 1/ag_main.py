from cromossomo import Cromossomo

if __name__ == '__main__':
    #atributos
    estado_final = input('Informe uma palavra chave para o estado final: ')
    tamanho_populacao = int(input('Tamanho da população: '))
    taxa_selecao = int(input('Taxa de seleção [25 a 40%]: '))
    taxa_reproducao = 100 - taxa_selecao
    taxa_mutacao = int(input('Taxa de mutação [3 a 10%]: '))
    quantidade_geracoes = int(input('Quantidade de gerações: '))
                            
    populacao = list()
    nova_populacao = list()

    #fluxo
    ##1a geracao - 100% aleatória
    Cromossomo.gerar_populacao(populacao, tamanho_populacao, estado_final)
    populacao.sort(key=lambda cromossomo: cromossomo.aptidao, reverse=True)
    print('Geração 0...')
    Cromossomo.exibir_populacao(populacao)


    ## demais gerações
    for i in range(1, quantidade_geracoes):
        Cromossomo.selecionar_por_torneio(populacao, nova_populacao, taxa_selecao)
        Cromossomo.reproduzir(populacao, nova_populacao, taxa_reproducao, estado_final)
        if i % taxa_mutacao == 0:
           Cromossomo.mutar(nova_populacao, estado_final)

        populacao.clear()
        populacao.extend(nova_populacao)
        nova_populacao.clear()
        populacao.sort(key=lambda cromossomo: cromossomo.aptidao, reverse=True)
        print('Geração ', i)
        Cromossomo.exibir_populacao(populacao)