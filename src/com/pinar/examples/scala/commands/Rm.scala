package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.Directory
import com.pinar.examples.scala.filesystem.State

class Rm(commandStr: String) extends Command {

  override def apply(state: State): State = {
    val tokens = commandStr.split(" ")
    if (tokens.length < 2) Command.incompleteCommand(tokens(0)).apply(state)
    else {
      val name = tokens(1)
      val wd = state.wd
      val absolutePath =
        if (name.startsWith(Directory.SEPERATOR)) name
        else if (wd.isRoot) wd.path + name
        else wd.path + Directory.SEPERATOR + name
      if (Directory.ROOT_PATH.equals(absolutePath)) state.setMessage("Nuclear war not supported yet!")
      else
        doRm(state, absolutePath)
    }
  }

  def doRm(state: State, path: String): State = {
    def doRmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if (!nextDirectory.isDirectory) currentDirectory
        else {
          val newNextDirectory = doRmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDirectory == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPERATOR).toList
    val newRoot: Directory = doRmHelper(state.root, tokens)
    if (newRoot == state.root) state.setMessage(s"$path: no such file or directory!")
    else State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }
}
