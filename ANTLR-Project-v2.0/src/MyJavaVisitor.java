import org.antlr.v4.runtime.TokenStreamRewriter;
public class MyJavaVisitor extends JavaParserBaseVisitor{
    public MyJavaVisitor(TokenStreamRewriter rewriter){
        this.rewriter = rewriter;
    }
    TokenStreamRewriter rewriter;
    int blockCount = 0;

    String imports = """
            import java.util.ArrayList;
            import java.io.FileWriter;
            import java.io.IOException;
            
            """;

    String arrayDeclaration = """
            
            static ArrayList<Integer> executedBlocks = new ArrayList<Integer>();
            
            """;

    String writingOutputFile = """
            
                String outputFileName = "Visited-Blocks.txt";
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
                
            """;

    @Override
    public Object visitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        rewriter.insertBefore(ctx.getStart(), imports);
        return super.visitCompilationUnit(ctx);
    }

    @Override
    public Object visitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        rewriter.insertBefore(ctx.getStart(), arrayDeclaration);
        rewriter.insertBefore(ctx.getStop(), writingOutputFile);
        return super.visitClassBodyDeclaration(ctx);
    }

    @Override
    public Object visitBlock(JavaParser.BlockContext ctx) {
        rewriter.insertAfter(ctx.getStart(), " /* Block number " + blockCount + " */\n" +
                "executedBlocks.add(" + blockCount++ + ");");
        return super.visitBlock(ctx);
    }
}
