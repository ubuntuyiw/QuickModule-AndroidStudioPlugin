package com.ibrahimkurt.quickmodule

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.treeStructure.Tree
import java.awt.BorderLayout
import java.awt.Component
import java.io.File
import java.io.IOException
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel

class Detail(val project: Project) : JPanel() {
    var name: JTextField = JTextField()
    var button: JButton = JButton("Click Me")
    var tree: JTree

    init {
        layout = BorderLayout()

        val moduleManager = ModuleManager.getInstance(project)
        val modules = moduleManager.modules

        val rootNode = DefaultMutableTreeNode("Modules")

        val moduleNames = getModulesFromSettingsGradle(project.basePath)
        moduleNames.forEach { module ->
            val parts = module.split(":").filter { it.isNotEmpty() }
            addModuleToTree(rootNode, parts)
        }

        val treeModel = DefaultTreeModel(rootNode)
        tree = Tree(treeModel)

        tree.cellRenderer = object : DefaultTreeCellRenderer() {
            private val folderIcon = UIManager.getIcon("FileView.directoryIcon")
            private val fileIcon = UIManager.getIcon("FileView.fileIcon")
            override fun getTreeCellRendererComponent(
                tree: JTree?,
                value: Any?,
                sel: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ): Component {
                val component = super.getTreeCellRendererComponent(
                    tree, value, sel, expanded, leaf, row, hasFocus
                )
                icon = if (leaf) fileIcon else folderIcon
                return component
            }
        }

        add(JScrollPane(tree), BorderLayout.CENTER)
        add(name, BorderLayout.NORTH)
        add(button, BorderLayout.SOUTH)

        button.addActionListener {
            val folderName = name.text.trim()
            if (folderName.isNotEmpty()) {
                ApplicationManager.getApplication().runWriteAction {
                    try {
                        val baseDir: VirtualFile = project.baseDir
                        baseDir.createChildDirectory(this, folderName)

                        Messages.showMessageDialog(
                            project,
                            "Folder '$folderName' was created successfully.",
                            "Information",
                            Messages.getInformationIcon()
                        )
                    } catch (e: IOException) {
                        Messages.showErrorDialog(
                            project,
                            "Failed to create folder '$folderName': ${e.message}",
                            "Error"
                        )
                    }
                }
            } else {
                Messages.showMessageDialog(
                    project,
                    "Please enter a valid folder name.",
                    "Warning",
                    Messages.getWarningIcon()
                )
            }
        }
    }

    private fun addModuleToTree(rootNode: DefaultMutableTreeNode, parts: List<String>) {
        var currentNode = rootNode
        for (part in parts) {
            val childNode = currentNode.children().toList().find {
                (it as DefaultMutableTreeNode).userObject == part
            } as? DefaultMutableTreeNode ?: DefaultMutableTreeNode(part).also {
                currentNode.add(it)
            }
            currentNode = childNode
        }
    }

    private fun getModulesFromSettingsGradle(basePath: String?): List<String> {
        val modules = mutableListOf<String>()
        basePath?.let {
            val settingsFile = File("$basePath/settings.gradle")
            val settingsKtsFile = File("$basePath/settings.gradle.kts")

            val lines = mutableListOf<String>()

            if (settingsFile.exists()) {
                settingsFile.forEachLine { line ->
                    lines.add(line)
                }
            } else if (settingsKtsFile.exists()) {
                settingsKtsFile.forEachLine { line ->
                    lines.add(line)
                }
            } else {
                println("Neither settings.gradle nor settings.gradle.kts file exists at path: $basePath")
            }

            lines.filter { it.trim().startsWith("include") }.forEach { line ->
                val includedModules = line.split("include")
                    .drop(1)
                    .flatMap { it.split(",") }
                    .map { it.trim().removeSurrounding("'").removeSurrounding("\"").removeSurrounding("(", ")").trim() }
                    .filter { it.isNotEmpty() }
                modules.addAll(includedModules)
            }
        } ?: run {
            println("Base path is null")
        }
        return modules
    }

    fun showDialog() {
        val dialog = JDialog()
        dialog.title = "QuickModule"

        val iconUrl = javaClass.getResource("/icons/pluginIcon.png")
        if (iconUrl != null) {
            val icon = ImageIcon(iconUrl)
            dialog.setIconImage(icon.image)
        } else {
            println("Icon not found at /icons/pluginIcon.png")
        }

        dialog.contentPane = this
        dialog.setSize(600, 900)
        dialog.setLocationRelativeTo(null)
        dialog.isVisible = true
    }
}
