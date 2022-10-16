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
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang3.StringUtils;
import org.example.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 生成Swagger文档
 *
 * @date 2022/10/16
 */
public class GenerateSwaggerDocAction extends AnAction {

    private static String SWAGGER_FIELD_ANNO = "@ApiModelProperty(value = \"%s\", required = false, example = \"\") ";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //获取所有字段、字段上的注释，在字段上面插入swagger注解
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        // 查找当前光标停留在的元素
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (Objects.isNull(psiFile)) {
            return;
        }
        PsiClass psiClass = PsiClassUtils.getPsiClass(psiFile);
        if (Objects.isNull(psiClass)) {
            return;
        }
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiAnnotation[] annotations = field.getAnnotations();
            // 如果有swagger注解，直接返回。用于更新swagger注解场景
            if (isAnnotationExist(annotations, SWAGGER_FIELD_ANNO)) {
                continue;
            }
            PsiDocComment comment = field.getDocComment();
            if (Objects.isNull(comment) || StringUtils.isEmpty(comment.getText())) {
                return;
            }
            List<String> commentItems = new ArrayList<>();
            for (PsiElement child : comment.getChildren()) {
                String source = child.getText().replaceAll("[/* \n]+", StringUtils.EMPTY);
                commentItems.add(source);
            }
            String finalComment = commentItems.stream()
                    .filter(StringUtils::isNoneEmpty)
                    .collect(Collectors.joining("。"));
            PsiElementFactory factory = PsiElementFactory.getInstance(project);
            PsiAnnotation annotation = factory.createAnnotationFromText(String.format(SWAGGER_FIELD_ANNO, finalComment), psiClass);
            WriteCommandAction.runWriteCommandAction(project, () -> {
                PsiElement psiElement = null;
                PsiElement[] children = field.getChildren();
                for (PsiElement child : children) {
                    if (child instanceof PsiModifierList) {
                        psiElement = child;
                    }
                }
                if (Objects.isNull(psiElement)) {
                    return;
                }
                // 注解增加到字段节点上
                field.getNode().addChild(annotation.getNode(), psiElement.getNode());
                // 格式化代码
                CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
                // 第一个子元素一般是注释
                PsiElement firstChild = field.getFirstChild();
                // 最后一个子元素一般是分号
                PsiElement lastChild = field.getLastChild();
                int startOffset = firstChild.getTextOffset();
                int endOffset = lastChild.getTextOffset();
                // 增加空格解决代码无法被格式化的问题
                document.insertString(endOffset, " ");
                codeStyleManager.reformatText(psiFile, startOffset, endOffset + 1);
            });
        }
    }

    private boolean isAnnotationExist(PsiAnnotation[] annotations, String annotationFullName) {
        boolean exist = false;
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            if (StringUtils.isEmpty(qualifiedName)) {
                continue;
            }
            String annotationName = qualifiedName;
            if (qualifiedName.contains(".")) {
                annotationName = qualifiedName.substring(qualifiedName.lastIndexOf("."));
            }
            if (annotationFullName.contains(annotationName)) {
                exist = true;
            }
        }
        return exist;
    }
}
