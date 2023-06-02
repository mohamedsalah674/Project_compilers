import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.Scanner;

import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        HashMap<Integer, Integer> checkedBlocks = null;

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
        TokenStreamRewriter interCodeRewriter = new TokenStreamRewriter(tokens);

        // Create a parser that feeds off the tokens buffer
        JavaParser parser = new JavaParser(tokens);

        // Begin parsing at init rule
        ParseTree tree = parser.compilationUnit();

        IntermediateCodeVisitor intermediateCodeVisitor = new IntermediateCodeVisitor(interCodeRewriter);
        intermediateCodeVisitor.visit(tree);

        System.out.println(interCodeRewriter.getText() + "\n/* Hence, there are " + --intermediateCodeVisitor.blockCount + " code blocks */\n");

        String outputFileName = "./Output-Files/Intermediate-Code.java";
        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
            fileWriter.write(interCodeRewriter.getText() + "\n/* Hence, there are " + intermediateCodeVisitor.blockCount + " code blocks */\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print("Enter the Hash-Map path: ");
//        String hashMapPath = sc.nextLine();

        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec("java ./Output-Files/Intermediate-Code.java");
        try {
            // Wait for the process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Intermediate code executed successfully!");

                try {
                    FileInputStream fileIn = new FileInputStream("./Output-Files/Blocks-HashMap.ser");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    checkedBlocks = (HashMap<Integer, Integer>) in.readObject();
                    in.close();
                    fileIn.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                TokenStreamRewriter htmlWriterRewriter = new TokenStreamRewriter(tokens);
                HTMLWriterVisitor htmlWriterVisitor = new HTMLWriterVisitor(htmlWriterRewriter, checkedBlocks);
                htmlWriterVisitor.visit(tree);

                String htmlFileName = "./Output-Files/index.html";
                try (FileWriter htmlWriter = new FileWriter(htmlFileName)) {
                    htmlWriter.write(htmlWriterRewriter.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.err.println("Intermediate code did not execute successfully.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}