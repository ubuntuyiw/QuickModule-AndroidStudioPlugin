package com.ibrahimkurt.quickmodule

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ShowDetailDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        if (project != null) {
            val detailDialog = Detail(project)
            detailDialog.showDialog()
        }
    }
}