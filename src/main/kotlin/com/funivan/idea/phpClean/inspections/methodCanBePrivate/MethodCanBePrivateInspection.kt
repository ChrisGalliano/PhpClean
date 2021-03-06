package com.funivan.idea.phpClean.inspections.methodCanBePrivate

import com.funivan.idea.phpClean.qf.makeClassMemberPrivate.MakeClassMemberPrivateQF
import com.funivan.idea.phpClean.spl.PhpCleanInspection
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpModifierList
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MethodCanBePrivateInspection : PhpCleanInspection() {
    override fun getShortName() = "MethodCanBePrivateInspection"
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpClass(clazz: PhpClass) {
                if (clazz.isFinal && clazz.extendsList.referenceElements.isEmpty()) {
                    for (method in clazz.ownMethods.filter { it.modifier.isProtected }) {
                        holder.registerProblem(
                                method.nameIdentifier ?: method,
                                "Method can be private",
                                MakeClassMemberPrivateQF.create(
                                        method.firstChild as PhpModifierList?,
                                        "Make method private"
                                )
                        )
                    }
                }
            }
        }
    }
}
