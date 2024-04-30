package org.example.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.example.dialog.PopUpDialog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 生成jdbc模板代码
 *
 * @date 2022/10/16
 */
public class GenerateJdbcTemplateCodeAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(GenerateJdbcTemplateCodeAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        String text = editor.getSelectionModel().getSelectedText();
        int caretOffset = editor.getCaretModel().getOffset();
        // 查找当前光标停留在的元素
//        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        PsiMethod selectedMethod = findMethodByName(psiFile,text );
//        PsiMethod selectedMethod = (PsiMethod) psiFile.findElementAt(caretOffset);
        if (Objects.isNull(selectedMethod)) {
            new PopUpDialog(project, "请选中方法名称").show();
            return;
        }
        String generic = getReturnTypeGeneric(selectedMethod);
        List<String> lines = getCodeLines(selectedMethod, generic);
        // 插入符号在文档的偏移量，取插入符号所在行的下一行
        int lineOffset = document.getLineNumber(caretOffset) + 1;
        // 偏移开始值
        int lineStartOffset = document.getLineStartOffset(lineOffset);
        String finalText = String.join("\n", lines);
        // 开始位置+文本长度=结束位置
        int endOffset = lineStartOffset + finalText.length();
        // 异步写入代码
        WriteCommandAction.runWriteCommandAction(project, () -> {
            // 插入文档
            document.insertString(lineStartOffset, finalText);
            // 格式化代码
            CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
            codeStyleManager.reformatText(psiFile, lineStartOffset, endOffset);
        });
    }

    @NotNull
    private List<String> getCodeLines(PsiMethod selectMethod, String generic) {
        String genericClass = generic + ".class";
        List<String> lines = new ArrayList<>();
        lines.add("MapSqlParameterSource source = new MapSqlParameterSource();");
        List<String> criteriaList = new ArrayList<>();
        // 方法参数
        PsiParameterList parameterList = selectMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        // 根据方法参数名称生成对应的代码
        for (PsiParameter parameter : parameters) {
            String parameterName = parameter.getName();
            lines.add(String.format("source.addValue(\"%s\", %s);", parameterName, parameterName));
            criteriaList.add(parameterName);
        }
        // TODO: 2022/10/16 增加多值查询
        String buildCriteria = criteriaList.stream()
                .map(l -> l + " = :" + l)
                .collect(Collectors.joining(" and "));
        String sql = String.format("String sql = \"select * from  where %s \";", buildCriteria);
        lines.add(sql);
        lines.add(String.format("return jdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(%s));\n", genericClass));
        return lines;
    }

    @NotNull
    private String getReturnTypeGeneric(PsiMethod selectMethod) {
        String generic = "";
        PsiClassType type = (PsiClassType) selectMethod.getReturnType();
        if (Objects.nonNull(type)) {
            PsiType[] parameters = type.getParameters();
            if (type.getParameterCount() == 0) {
                // 没有泛型
                return type.getName();
            }
            // 取出泛型
            PsiClassType parameter = (PsiClassType) parameters[0];
            generic = parameter.getName();
        }
        return generic;
    }

//    @Nullable
//    private PsiMethod getSelectedMethod(String text, PsiFile psiFile) {
//        List<PsiMethod> psiMethods = new ArrayList<>();
//        // 自顶向下，递归查找数据
//        psiFile.accept(new JavaRecursiveElementVisitor() {
//            @Override
//            public void visitMethod(PsiMethod method) {
//                if(method.getName().equals(text)){
//                    psiMethods.add(method);
//                }
//            }
//        });
//        if (CollectionUtils.isEmpty(psiMethods)) {
//            return null;
//        }
//        return psiMethods.get(0);
//    }

    public PsiMethod findMethodByName(PsiFile psiFile, String methodName) {
        if (psiFile instanceof PsiJavaFileImpl) {
            PsiJavaFile psiJavaFile = (PsiJavaFileImpl) psiFile;
            // 获取文件中所有的类
            for (PsiClass psiClass : psiJavaFile.getClasses()) {
                // 遍历类中的所有方法
                for (PsiMethod psiMethod : psiClass.getMethods()) {
                    if (psiMethod.getName().equals(methodName)) {
                        return psiMethod;  // 找到匹配的方法，返回它
                    }
                }
            }
        }else{
            System.out.println("psiFile.getClass().getName() = " + psiFile.getClass().getName());
        }
        return null;  // 如果没有找到，返回 null
    }
}
