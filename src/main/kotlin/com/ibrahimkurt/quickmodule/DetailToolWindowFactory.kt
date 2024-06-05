package com.ibrahimkurt.quickmodule

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class DetailToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val detailPanel = Detail(project)
        val contentFactory = ContentFactory.getInstance()
        val content: Content = contentFactory.createContent(detailPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}


