package org.example.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class PsiClassUtils {

    public static PsiClass getPsiClass(PsiFile psiFile){
        for (PsiElement psiElement : psiFile.getChildren()) {
            if (!(psiElement instanceof PsiClass)) {
                continue;
            }
            // PsiClass需要在gradle引入，plugins = ['com.intellij.java']
            // PsiClass相当于Java中的Class对象
            return (PsiClass) psiElement;
        }
        return null;
    }
}
