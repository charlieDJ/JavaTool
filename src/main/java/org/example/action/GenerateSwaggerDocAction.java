package org.example.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
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
import java.util.Arrays;
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
    private static String SWAGGER_CLASS_ANNO = "@ApiModel(\"%s\")";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //获取所有字段、字段上的注释，在字段上面插入swagger注解
        Project project = e.getProject();
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (Objects.isNull(editor) || Objects.isNull(project)) {
            return;
        }
        // 查找当前光标停留在的元素
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (Objects.isNull(psiFile)) {
            return;
        }
        PsiClass psiClass = PsiClassUtils.getPsiClass(psiFile);
        if (Objects.isNull(psiClass)) {
            return;
        }
        genClassSwaggerAnnotation(e, psiClass);
    }

    private void genClassSwaggerAnnotation(AnActionEvent e, PsiClass psiClass) {
        genClassModelSwaggerAnnotation(e, psiClass);
        PsiField[] fields = psiClass.getFields();
        genFieldSwaggerAnnotation(e, fields);
        PsiClass[] innerClasses = psiClass.getInnerClasses();
        Arrays.stream(innerClasses)
                .forEach(clz -> genClassSwaggerAnnotation(e, clz));
    }

    private void genClassModelSwaggerAnnotation(AnActionEvent e, PsiClass psiClass) {
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        if (isAnnotationExist(annotations, SWAGGER_CLASS_ANNO)) {
            return;
        }
        PsiDocComment docComment = psiClass.getDocComment();
        String comment = getComment(docComment,true);
        if (StringUtils.isEmpty(comment)) {
            return;
        }
        createAnnotation(SWAGGER_CLASS_ANNO, comment +" "+ psiClass.getName(), psiClass, e);
    }

    private void createAnnotation(String anno, String comment, PsiElement element, AnActionEvent e) {
        Project project = e.getProject();
        PsiElementFactory factory = PsiElementFactory.getInstance(project);
        PsiAnnotation annotation = factory.createAnnotationFromText(String.format(anno, comment), element);
        // 获取修饰符元素
        PsiElement modifierElement = getModifierElement(element.getChildren());
        if (Objects.isNull(modifierElement)) {
            return;
        }
        WriteCommandAction.runWriteCommandAction(project, () -> {
            element.getNode().addChild(annotation.getNode(), modifierElement.getNode());
            format(element, e);
        });
    }

    private void format(PsiElement element, AnActionEvent e) {
        // 格式化代码
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(e.getProject());
        // 第一个子元素一般是注释
        PsiElement firstChild = element.getFirstChild();
        // 最后一个子元素一般是分号
        PsiElement lastChild = element.getLastChild();
        int startOffset = firstChild.getTextOffset();
        int endOffset = lastChild.getTextOffset();
        // 增加空格解决代码无法被格式化的问题
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        editor.getDocument().insertString(endOffset, " ");
        codeStyleManager.reformatText(element.getContainingFile(), startOffset, endOffset + 1);
    }

    private PsiElement getModifierElement(PsiElement[] elements) {
        for (PsiElement element : elements) {
            if (element instanceof PsiModifierList) {
                return element;
            }
        }
        return null;
    }

    private void genFieldSwaggerAnnotation(AnActionEvent e, PsiField[] fields) {
        for (PsiField field : fields) {
            PsiAnnotation[] annotations = field.getAnnotations();
            // 如果有swagger注解，直接返回。用于更新swagger注解场景
            if (isAnnotationExist(annotations, SWAGGER_FIELD_ANNO)) {
                continue;
            }
            PsiDocComment comment = field.getDocComment();
            String finalComment = getComment(comment,false);
            if (StringUtils.isEmpty(finalComment)) {
                return;
            }
            createAnnotation(SWAGGER_FIELD_ANNO, finalComment, field, e);
        }
    }

    private String getComment(PsiDocComment comment, boolean isClass) {
        if (Objects.isNull(comment) || StringUtils.isEmpty(comment.getText())) {
            return "";
        }
        List<String> commentItems = new ArrayList<>();
        for (PsiElement child : comment.getChildren()) {
            String source = child.getText().replaceAll("[/* \n]+", StringUtils.EMPTY);
            if (isClass && child.getText().contains("@")) {
                continue;
            }
            commentItems.add(source);
        }
        return commentItems.stream()
                .filter(StringUtils::isNoneEmpty)
                .collect(Collectors.joining("。"));
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
