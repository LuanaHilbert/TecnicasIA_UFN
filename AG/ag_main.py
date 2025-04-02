from cromossomo import Cromossomo

#atributos

estado_final = input('Informe uma pallavra chave para o estado final: ')
tamanho_populacao = int(input('Tamanho da população: '))
taxa_selecao = int(input('Taxa de seleção [25 a 40%]: '))
taxa_reproducao = 100 - taxa_selecao
taxa_mutacao = int(input('Taxa de mitação [3 a 10%]: '))
quantidade_geracoes = int(input('Quantidade de gerações: '))
                          
populacao = list()
novapopulacao = list()

#fluxo
#1a geracao 
Cromossomo.gerar_populacao(populacao, tamanho_populacao, estado_final)
populacao.sort(key=lambda cromossomo: cromossomo.aptidao, reverse=True)
Cromossomo.exibir_populacao(populacao, 0)

