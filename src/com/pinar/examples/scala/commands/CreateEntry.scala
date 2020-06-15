package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.{DirEntry, Directory}
import com.pinar.examples.scala.filesystem.State

abstract class CreateEntry(commandStr: String) extends Command {

  override def apply(state: State): State = {
    val tokens = commandStr.split(" ")
    if (tokens.length < 2) Command.incompleteCommand(tokens(0)).apply(state)
    else {
      val newEntry = tokens(1)
      if (state.wd.hasEntry(newEntry)) {
        state.setMessage(s"Entry $newEntry already exist!")
      } else if (newEntry.contains(Directory.SEPERATOR)) {
        state.setMessage(s"$newEntry must not contain seperators!")
      } else if (isIllegal(newEntry)) {
        state.setMessage(s"$newEntry illegal entry name!")
      } else {
        createEntry(state, newEntry)
      }
    }
  }

  def isIllegal(name: String): Boolean = name.contains(".")

  def createEntry(state: State, newEntry: String): State = {
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd
    val allDirsInPath = wd.getAllFoldersInPath
    val newEntryObj = createSpecificEntry(state, newEntry)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntryObj)
    val newWd = newRoot.findDescendant(allDirsInPath)
    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State, entryName: String): DirEntry
}
