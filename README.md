#                                       Compiler Project

It is a Java code analyzer that produces information on the code coverage of statements and branches.

Our features are developed in three deliveries or versions as required in the [**project description**](./Project-Description.pdf), which are represented in the following three folders:	[**1st** Delivery](./ANTLR-Project-v1.0)	-	[**2nd** Delivery](./ANTLR-Project-v2.0)	-	[**3rd** Delivery](./ANTLR-Project-v3.0)

Moreover, [**tree** code snippets ](./Input-Code-Snippets) were pushed to be used as input codes to check the functionality.

------



## Project Features

- Producing a modified intermediate java file after analyzing a java file as an input.

- Adding a comment to each block of this code that indicates the block's number.

  - The comment should read as follows:

    // Block number 3

- Determining which code blocks are visited following the execution of the modified intermediate Java file:

  - The file should seem as follows:

    ​	Block number 1 is visited only once

    ​	Block number 2 is visited two times
  
- Use the output from delivery 2 to generate an HTML with highlighted red/green lines

  - Orange lines are highlighted  for branch coverage implementation as following:

    > ​	If a Boolean expression comprises more than one condition, such as a || b, and the first condition always evaluates to true, then the second condition b never executed for any branches (if/else/for/while). In this scenario, the HTML file output must include this line highlighted in orange.

  - Green lines are highlighted for visited lines

    > ​	When both of the conditions in the previous example are evaluated, the output background color in the HTML file is green.

  - Red lines are highlighted for not visited lines

    > ​	The Boolean statement in the output HTML file is red if neither of the conditions an or b evaluate to true.


------



## Project Requirements

<a href="https://github.com/RamziMuhammad"></a>

- <a href="https://www.jetbrains.com/idea/download/#section=windows">**IntelliJ IDEA**</a>
- <a href="https://www.antlr.org/download.html">**ANTLR**</a>
- <a href="https://github.com/antlr/grammars-v4/tree/master/java/java">**Java ANTLR Grammar**</a>

------

### Parse Tree

<p align="center">
  <img src="https://github.com/RamziMuhammad/ANTLR-Project/blob/main/Parse-Trees/Parse-Tree(3).png" style="width:800px;"/>
</p>

------

### Modified Intermediate Java Code

- [x] First, using the following code, we add **comment** to each code block:


```java
@Override
    public Object visitBlock(JavaParser.BlockContext ctx) {
        rewriter.insertAfter(ctx.getStart(), " /* Block number " + blockCount + " */\n" +
                "executedBlocks.add(" + blockCount++ + ");");
        return super.visitBlock(ctx);
    }
```

- [x] We also add that number to a **list** that was declared at the beginning of the class:

```java
String imports = "import java.util.ArrayList;\n" +
            "import java.io.FileWriter;\n" +
            "import java.io.IOException;\n";

@Override
    public Object visitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        rewriter.insertBefore(ctx.getStart(), imports);
        return super.visitCompilationUnit(ctx);
    }
```

- [x] Finally, at the end of the class, we put a block of code that iterates through that list and **creates a file with details about which block was visited**:

```java
String writingOutputFile = "String outputFileName = \"Visited-Blocks.txt\";\n" +
            "try (FileWriter fileWriter = new FileWriter(outputFileName)) {\n" +
            "    int previous = executedBlocks.get(0);\n" +
            "    int count = 1;\n" +
            "    for (int i = 1; i < executedBlocks.size(); i++) {\n" +
            "        if (executedBlocks.get(i) == previous) {\n" +
            "            count++;\n" +
            "        } else {\n" +
            "            if(count == 1){\n" +
            "                fileWriter.write(\"Block number \" + previous + \" is visited only once\\n\");\n" +
            "            } else {\n" +
            "                fileWriter.write(\"Block number \" + previous + \" is visited \" + count + \" times\\n\");\n" +
            "            }\n" +
            "            count = 1;\n" +
            "        }\n" +
            "        previous = executedBlocks.get(i);\n" +
            "    }\n" +
            "    if(count == 1){\n" +
            "        fileWriter.write(\"Block number \" + previous + \" is visited only once\\n\");\n" +
            "    } else {\n" +
            "        fileWriter.write(\"Block number \" + previous + \" is visited \" + count + \" times\\n\");\n" +
            "    }\n" +
            "} catch (IOException e) {\n" +
            "    e.printStackTrace();\n" +
            "}\n";

@Override
    public Object visitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        rewriter.insertBefore(ctx.getStart(), arrayDeclaration);
        rewriter.insertBefore(ctx.getStop(), writingOutputFile);
        return super.visitClassBodyDeclaration(ctx);
    }
```

------

> #### [Intermediate Code](./ANTLR-Project-v2.0/Intermediate-Code.java)

```java
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
```

------

> #### [Analysis Output of 2nd Delivery](./ANTLR-Project-v2.0/Visited-Blocks.txt)

<p align="center">
  <img src="https://github.com/RamziMuhammad/ANTLR-Project/blob/main/Assets/Visited-Blocks.png" style="width:800px;"/>
</p>


> #### [Final Output of 3rd Delivery](./ANTLR-Project-v3.0/Output-Files/index.html)

<p align="center">
  <img src="https://github.com/RamziMuhammad/ANTLR-Project/blob/main/Assets/HTML-Output.png" style="width:800px;"/>
</p>


------

### Team Members

This Project was created due to the efforts of all the team members and their indescribable hard work.

<table>
  <tr>
    <td align="center"><a href="https://github.com/RamziMuhammad"><img src="https://avatars.githubusercontent.com/u/66510024?v=4" width="200px;" alt=""/><br /><sub><b><center>Ramzi Muhammad</b></sub></a><br /></td>
    <td align="center"><a href="https://github.com/Abdelrahman-Atef-Elsayed"><img src="https://avatars.githubusercontent.com/u/66162676?v=4" width="200px;" alt=""/><br /><sub><b><center>AbdElRahman Atef</b></sub></a><br /></td>
   <td align="center"><a href="https://github.com/Ola-Mohamed"><img src="https://avatars.githubusercontent.com/u/66176966?v=4" width="200px;" alt=""/><br /><sub><b><center>Ola Mohamed</b></sub></a><br /></td>
   <td align="center"><a href="https://github.com/mohamedsalah674"><img src="https://avatars.githubusercontent.com/u/66376551?v=4" width="200px;" alt=""/><br /><sub><b><center>Mohamed Salah</b></sub></a><br /></td>
   <td align="center"><a href="https://github.com/mohamedmahfouz3"><img src="https://avatars.githubusercontent.com/u/66581191?v=4" width="200px;" alt=""/><br /><sub><b><center>Mohamed Mahfouz</b></sub></a><br /></td>
    </tr>
</table>"# Project_compilers" 
