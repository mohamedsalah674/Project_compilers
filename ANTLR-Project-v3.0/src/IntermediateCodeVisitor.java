import org.antlr.v4.runtime.TokenStreamRewriter;

public class IntermediateCodeVisitor extends JavaParserBaseVisitor {
    public IntermediateCodeVisitor(TokenStreamRewriter interCodeRewriter) {
        this.interCodeRewriter = interCodeRewriter;
    }

    TokenStreamRewriter interCodeRewriter;

    int blockCount = 0;
    String checkStatements;

    String imports = """
            import java.util.ArrayList;
            import java.io.FileWriter;
            import java.io.IOException;
            import java.util.HashMap;
            import java.io.FileOutputStream;
            import java.io.ObjectOutputStream;
            
            """;

    String listDeclaration = """
                                            
            static ArrayList<Integer> executedBlocks = new ArrayList<Integer>();
            static HashMap<Integer, Integer> checkedBlocks = new HashMap<>();
            static ArrayList<Integer> order = new ArrayList<>();
                        
            """;

    String writingOutputFile = """
                    
                    String outputFileName = "./Output-Files/Visited-Blocks.txt";
                    try (FileWriter fileWriter = new FileWriter(outputFileName)) {
                        int previous = executedBlocks.get(0);
                        int count = 1;
                        for (int i = 1; i < executedBlocks.size(); i++) {
                            if (executedBlocks.get(i) == previous) {
                                count++;
                            } else {
                                if(count == 1){
                                    fileWriter.write("Block number " + previous + " is visited only once\\n");
                                } else {
                                    fileWriter.write("Block number " + previous + " is visited " + count + " times\\n");
                                }
                                count = 1;
                            }
                            previous = executedBlocks.get(i);
                        }
                        if(count == 1){
                            fileWriter.write("Block number " + previous + " is visited only once\\n");
                        } else {
                            fileWriter.write("Block number " + previous + " is visited " + count + " times\\n");
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
                    
            """;

    @Override
    public Object visitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        interCodeRewriter.insertBefore(ctx.getStart(), imports);
        return super.visitCompilationUnit(ctx);
    }

    @Override
    public Object visitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        interCodeRewriter.insertBefore(ctx.getStart(), listDeclaration);
        interCodeRewriter.insertBefore(ctx.getStop(), writingOutputFile);
        return super.visitClassBodyDeclaration(ctx);
    }
    @Override
    public Object visitBlock(JavaParser.BlockContext ctx) {
        interCodeRewriter.insertAfter(ctx.getStart()," /* Block number " + blockCount + " */\n");

        if (blockCount == 0) {
            interCodeRewriter.insertAfter(ctx.getStart(), "\ncheckedBlocks.put(0, 0);\n");
        } else {
            interCodeRewriter.insertBefore(ctx.getParent().getParent().getStart(), "\ncheckedBlocks.put(" + blockCount + ", 0);\n");
        }

        interCodeRewriter.insertAfter(ctx.getStart(), "\nexecutedBlocks.add(" + blockCount + ");\n"  +
                        "if(checkedBlocks.get(" + blockCount + ") == 0){\n" +
                        "   checkedBlocks.put(" + blockCount++ + ", 2);\n" +
                        "}\n");

        return super.visitBlock(ctx);
    }

    void checkConditions(JavaParser.ExpressionContext ctx) {
        if (ctx.getChildCount() != 1) {
            String middleOperator = ctx.getChild(1).getText();
            if (middleOperator.equals("||")) {
                checkConditions((JavaParser.ExpressionContext) ctx.getChild(0));
                checkConditions((JavaParser.ExpressionContext) ctx.getChild(2));
            }else if (middleOperator.equals("==")) {

                checkStatements = checkStatements + "\nif (" + ctx.getText() + ") {\n" +
                        "    if (checkedBlocks.get(" + blockCount + ") == 0) {\n" +
                        "        checkedBlocks.put(" + blockCount + ", 2);\n" +
                        "    } else if (checkedBlocks.get(" + blockCount + ") == 2) {\n" +
                        "        checkedBlocks.put(" + blockCount + ", 1);\n" +
                        "    }\n" +
                        "} else {\n" +
                        "    if (checkedBlocks.get(" + blockCount + ") == 2) {\n" +
                        "        checkedBlocks.put(" + blockCount + ", 1);\n" +
                        "    }\n" +
                        "}\n";
            }
        }
    }

    @Override
    public Object visitParExpression(JavaParser.ParExpressionContext ctx) {
        checkStatements = "";

        checkConditions((JavaParser.ExpressionContext) ctx.getChild(1));
        interCodeRewriter.insertBefore(ctx.getParent().getStart(), checkStatements);

        return super.visitParExpression(ctx);
    }
}
