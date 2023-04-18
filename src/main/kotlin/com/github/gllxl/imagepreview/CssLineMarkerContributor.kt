package com.github.gllxl.imagepreview

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.css.impl.CssElementTypes.*
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.startOffset

fun getCSSBackgroundVariableContent (element: PsiElement): String? {
  return element.children.find { it.elementType === CSS_STRING }?.text
}

class CssLineMarkerContributor : LineMakerContributor() {
  override fun getInfo(element: PsiElement): Info? {
    if (element.elementType === CSS_URI) {
      val imageUrl = getCSSBackgroundVariableContent(element)

      if (imageUrl != null ) {

        val document = PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile) ?: return null
        val lineNumber = document.getLineNumber(element.startOffset)
        ImageMapping.setLineMapping(element.containingFile.virtualFile, lineNumber, removeUrlQuotes(imageUrl))

        return getLineMaker(removeUrlQuotes(imageUrl))
      }
      return null
    }
    return null
  }

}