public class Main {
    public static void main(String[] args) { /* Block number 0 */
        int x = 0;
        int y = 1;
        for (int i = 0; i < 5; i++){ /* Block number 1 */
            }
        if (x == 0 || y == 4) { /* Block number 2 */
            x++;
            y = 10;
        }
        while (x == -1) { /* Block number 3 */
            x ++;
            break;
        }
    }
}
// Hence, there are 4 code blocks.