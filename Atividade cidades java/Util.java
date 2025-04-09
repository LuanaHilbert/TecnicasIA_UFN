import java.util.ArrayList;
import java.util.Collections;

public class Util {
    public static int[] gerarRota() {
        ArrayList<Integer> rota = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            rota.add(i);
        }
        Collections.shuffle(rota);
        
        int[] rotaArray = new int[rota.size()];
        for (int i = 0; i < rota.size(); i++) {
            rotaArray[i] = rota.get(i);
        }
        return rotaArray;
    }
}