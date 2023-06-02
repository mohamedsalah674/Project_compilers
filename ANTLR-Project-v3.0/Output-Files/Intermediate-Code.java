import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Main {
    
static ArrayList<Integer> executedBlocks = new ArrayList<Integer>();
static HashMap<Integer, Integer> checkedBlocks = new HashMap<>();
static ArrayList<Integer> order = new ArrayList<>();

public static void main(String[] args) { /* Block number 0 */

checkedBlocks.put(0, 0);

executedBlocks.add(0);
if(checkedBlocks.get(0) == 0){
   checkedBlocks.put(0, 2);
}

        int x = 0;
        int y = 1;
        
checkedBlocks.put(1, 0);
for (int i = 0; i < 5; i++){ /* Block number 1 */

executedBlocks.add(1);
if(checkedBlocks.get(1) == 0){
   checkedBlocks.put(1, 2);
}

            }
        
checkedBlocks.put(2, 0);

if (x==0) {
    if (checkedBlocks.get(2) == 0) {
        checkedBlocks.put(2, 2);
    } else if (checkedBlocks.get(2) == 2) {
        checkedBlocks.put(2, 1);
    }
} else {
    if (checkedBlocks.get(2) == 2) {
        checkedBlocks.put(2, 1);
    }
}

if (y==4) {
    if (checkedBlocks.get(2) == 0) {
        checkedBlocks.put(2, 2);
    } else if (checkedBlocks.get(2) == 2) {
        checkedBlocks.put(2, 1);
    }
} else {
    if (checkedBlocks.get(2) == 2) {
        checkedBlocks.put(2, 1);
    }
}
if (x == 0 || y == 4) { /* Block number 2 */

executedBlocks.add(2);
if(checkedBlocks.get(2) == 0){
   checkedBlocks.put(2, 2);
}

            x++;
            y = 10;
        }
        
checkedBlocks.put(3, 0);

if (x==-1) {
    if (checkedBlocks.get(3) == 0) {
        checkedBlocks.put(3, 2);
    } else if (checkedBlocks.get(3) == 2) {
        checkedBlocks.put(3, 1);
    }
} else {
    if (checkedBlocks.get(3) == 2) {
        checkedBlocks.put(3, 1);
    }
}
while (x == -1) { /* Block number 3 */

executedBlocks.add(3);
if(checkedBlocks.get(3) == 0){
   checkedBlocks.put(3, 2);
}

            x ++;
            break;
        }
    
        String outputFileName = "./Output-Files/Visited-Blocks.txt";
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

        try {
            FileOutputStream fileOut = new FileOutputStream("./Output-Files/Blocks-HashMap.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(checkedBlocks);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in Blocks-Hash-Map.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }

}
}
/* Hence, there are 3 code blocks */
