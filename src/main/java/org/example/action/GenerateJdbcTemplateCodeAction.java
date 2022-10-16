package org.example.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang.StringUtils;
import org.example.dialog.PopUpDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        String text = editor.getSelectionModel().getSelectedText();
        // 查找当前光标停留在的元素
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiMethod selectMethod = getSelectedMethod(text, psiFile);
        if (Objects.isNull(selectMethod)) {
            new PopUpDialog(project, "请选中方法").show();
            return;
        }
        String generic = getReturnTypeGeneric(selectMethod);
        List<String> lines = getCodeLines(selectMethod, generic);
        // 光标所在的行，取光标所在行的下一行
        int lineOffset = document.getLineNumber(editor.getCaretModel().getOffset()) + 1;
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
        String genericClass = "";
        if (StringUtils.isNotEmpty(generic)) {
            genericClass = generic + ".class";
        }
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
        lines.add(String.format("List<%s> list = jdbcTemplate.query(sql, source, BeanPropertyRowMapper.newInstance(%s));\n", generic, genericClass));
        return lines;
    }

    @NotNull
    private String getReturnTypeGeneric(PsiMethod selectMethod) {
        String generic = "";
        PsiClassType type = (PsiClassType) selectMethod.getReturnType();
        if (Objects.nonNull(type)) {
            PsiType[] parameters = type.getParameters();
            if (parameters.length > 0) {
                PsiClassType parameter = (PsiClassType) parameters[0];
                generic = parameter.getName();
            }
        }
        return generic;
    }

    @Nullable
    private PsiMethod getSelectedMethod(String text, PsiFile psiFile) {
        PsiMethod selectMethod = null;
        for (PsiElement psiElement : psiFile.getChildren()) {
            if (!(psiElement instanceof PsiClass)) {
                continue;
            }
            // PsiClass需要在gradle引入，plugins = ['com.intellij.java']
            // PsiClass相当于Java中的Class对象
            PsiClass psiClass = (PsiClass) psiElement;
            PsiMethod[] methods = psiClass.getMethods();
            for (PsiMethod method : methods) {
                if (method.getName().equals(text)) {
                    selectMethod = method;
                    break;
                }
            }
        }
        return selectMethod;
    }
}
