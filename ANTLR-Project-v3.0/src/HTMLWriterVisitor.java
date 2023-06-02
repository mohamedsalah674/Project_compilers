import org.antlr.v4.runtime.TokenStreamRewriter;

import java.util.HashMap;

public class HTMLWriterVisitor extends JavaParserBaseVisitor{
    public HTMLWriterVisitor(TokenStreamRewriter htmlWriterRewriter, HashMap<Integer, Integer> checkedBlocks) {
        this.htmlWriterRewriter = htmlWriterRewriter;
        this.checkedBlocks = checkedBlocks;
    }
    HashMap<Integer, Integer> checkedBlocks;
    TokenStreamRewriter htmlWriterRewriter;

    int blockCount = 0;

    String htmlTemplate = """
                         <!DOCTYPE html>
                         <html>
                         <head>
                             <meta charset='utf-8'>
                             <meta http-equiv='X-UA-Compatible' content='IE=edge'>
                             <title>Page Title</title>
                             <meta name='viewport' content='width=device-width, initial-scale=1'>
                             <link rel='stylesheet' type='text/css' media='screen' href='main.css'>
                             <script src='main.js'></script>
                         </head>
                         <body>
                            <pre>
                         """;

    String templateEnd = """
                                </pre>
                            </body>
                            </html>
                            """;
    @Override
    public Object visitCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        htmlWriterRewriter.insertBefore(ctx.getStart(), htmlTemplate);
        htmlWriterRewriter.insertAfter(ctx.getStop(), templateEnd);
        return super.visitCompilationUnit(ctx);
    }

    @Override
    public Object visitBlock(JavaParser.BlockContext ctx) {
        if (checkedBlocks.get(blockCount) == null || checkedBlocks.get(blockCount) == 0){
            htmlWriterRewriter.insertBefore(ctx.getStart(),  "<pre style=\"background-color: red\">");
            htmlWriterRewriter.insertAfter(ctx.getStop(),  "</pre>");
        } else if (checkedBlocks.get(blockCount) == 1) {
            htmlWriterRewriter.insertBefore(ctx.getStart(),  "<pre style=\"background-color: orange\">");
            htmlWriterRewriter.insertAfter(ctx.getStop(),  "</pre>");
        } else if (checkedBlocks.get(blockCount) == 2) {
            htmlWriterRewriter.insertBefore(ctx.getStart(),  "<pre style=\"background-color: green\">");
            htmlWriterRewriter.insertAfter(ctx.getStop(),  "</pre>");
        }
        blockCount++;
        return super.visitBlock(ctx);
    }

}
