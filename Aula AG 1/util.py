import random

class Util:
    @staticmethod
    def gerar_rota():
        rota = list(range(1, 10))  # cidades de 1 a 9
        random.shuffle(rota)
        return rota
