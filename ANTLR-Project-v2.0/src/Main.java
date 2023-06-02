import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.print("Enter the input file path: ");
        Scanner sc = new Scanner(System.in);
        String filePath = sc.nextLine();

        // Open file
        File file = new File(filePath);
        FileInputStream inputFile = new FileInputStream(file);
        // fis stands for file stream

        // Create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(inputFile);

        // Create a lexer that feeds off of input CharStream
        JavaLexer lexer = new JavaLexer(input);

        // Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create token stream rewriter to inject code snippets
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

        // Create a parser that feeds off the tokens buffer
        JavaParser parser = new JavaParser(tokens);

        // Begin parsing at init rule
        ParseTree tree = parser.compilationUnit();

        MyJavaVisitor myJavaVisitor = new MyJavaVisitor(rewriter);
        myJavaVisitor.visit(tree);

        System.out.println(rewriter.getText() + "\n/* Hence, there are " + myJavaVisitor.blockCount + " code blocks */\n");

        String outputFileName = "Intermediate-Code.java";
        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
            fileWriter.write(rewriter.getText() + "\n/* Hence, there are " + myJavaVisitor.blockCount + " code blocks */\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec("java ./Intermediate-Code.java");
        try {
            // Wait for the process to finish
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Intermediate code executed successfully!");
            } else {
                System.err.println("Intermediate code did not execute successfully.");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}