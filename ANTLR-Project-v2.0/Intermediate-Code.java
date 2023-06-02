import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
static ArrayList<Integer> executedBlocks = new ArrayList<Integer>();

public static void main(String[] args) { /* Block number 0 */
executedBlocks.add(0);
        int x = 0;
        int y = 1;
        for (int i = 0; i < 5; i++){ /* Block number 1 */
executedBlocks.add(1);
            }
        if (x == 0 || y == 4) { /* Block number 2 */
executedBlocks.add(2);
            x++;
            y = 10;
        }
        while (x == -1) { /* Block number 3 */
executedBlocks.add(3);
            x ++;
            break;
        }
    
String outputFileName = "Visited-Blocks.txt";
try (FileWriter fileWriter = new FileWriter(outputFileName)) {
    int previous = executedBlocks.get(0);
    int count = 1;
    for (int i = 1; i < executedBlocks.size(); i++) {
        if (executedBlocks.get(i) == previous) {
            count++;
        } else {
            if(count == 1){
                fileWriter.write("Block number " + previous + " is visited only once\n");
            } else {
                fileWriter.write("Block number " + previous + " is visited " + count + " times\n");
            }
            count = 1;
        }
        previous = executedBlocks.get(i);
    }
    if(count == 1){
        fileWriter.write("Block number " + previous + " is visited only once\n");
    } else {
        fileWriter.write("Block number " + previous + " is visited " + count + " times\n");
    }
} catch (IOException e) {
    e.printStackTrace();
}

}
}
/* Hence, there are 4 code blocks */
