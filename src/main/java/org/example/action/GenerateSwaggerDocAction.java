package org.example.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang3.StringUtils;
import org.example.util.PsiClassUtils;
import org.example.util.PsiDocUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * 生成Swagger文档
 *
 * @date 2022/10/16
 */
public class GenerateSwaggerDocAction extends AnAction {

    private static final Logger logger = Logger.getInstance(GenerateSwaggerDocAction.class);

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
        //PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        // 查找当前光标停留的元素
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
        PsiAnnotation psiAnnotation = existAnnotation(annotations, SWAGGER_CLASS_ANNO);
        PsiDocComment docComment = psiClass.getDocComment();
        String comment = PsiDocUtils.getComment(docComment, true);
        createAnnotation(SWAGGER_CLASS_ANNO, comment + " " + psiClass.getName(), psiClass, psiAnnotation, e);
    }

    private void createAnnotation(String anno, String comment, PsiElement element, PsiAnnotation psiAnnotation, AnActionEvent e) {
        Project project = e.getProject();
        PsiElementFactory factory = PsiElementFactory.getInstance(project);
        PsiAnnotation annotation = factory.createAnnotationFromText(String.format(anno, comment), element);
        // 获取修饰符元素
        PsiElement modifierElement = getModifierElement(element.getChildren());
        if (Objects.isNull(modifierElement)) {
            return;
        }
        WriteCommandAction.runWriteCommandAction(project, () -> {
            if (psiAnnotation == null) {
                element.getNode().addChild(annotation.getNode(), modifierElement.getNode());
            } else {
                psiAnnotation.replace(annotation);
            }
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
        PsiDocumentManager.getInstance(e.getProject()).doPostponedOperationsAndUnblockDocument(editor.getDocument());
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
            logger.warn("field: " + field.getName());
            PsiAnnotation psiAnnotation = existAnnotation(annotations, SWAGGER_FIELD_ANNO);
            PsiDocComment comment = field.getDocComment();
            String finalComment = PsiDocUtils.getComment(comment, false);
            // 注解存在，但注释为空，不更新
            if (psiAnnotation != null && StringUtils.isEmpty(finalComment)) {
                continue;
            }
            createAnnotation(SWAGGER_FIELD_ANNO, finalComment, field, psiAnnotation, e);
        }
    }

    private boolean isAnnotationExist(PsiAnnotation[] annotations, String annotationFullName) {
        PsiAnnotation psiAnnotation = existAnnotation(annotations, annotationFullName);
        return psiAnnotation != null;
    }

    private PsiAnnotation existAnnotation(PsiAnnotation[] annotations, String annotationFullName) {
        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();
            if (StringUtils.isEmpty(qualifiedName)) {
                continue;
            }
            logger.warn("qualifiedName = " + qualifiedName);
            String annotationName = qualifiedName;
            if (qualifiedName.contains(".")) {
                annotationName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
                logger.warn("annotationName = " + annotationName);
            }
            if (annotationFullName.contains(annotationName)) {
                logger.warn("annotationName exist = " + true);
                return annotation;
            }
        }
        return null;
    }
}
