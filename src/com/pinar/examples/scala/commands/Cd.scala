package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.{DirEntry, Directory}
import com.pinar.examples.scala.filesystem.State

import scala.annotation.tailrec

class Cd(commandStr: String) extends Command {

  override def apply(state: State): State = {
    val tokens = commandStr.split(" ")
    if (tokens.length < 2) Command.incompleteCommand(tokens(0)).apply(state)
    else {
      val dir = tokens(1)
      val root = state.root;
      val wd = state.wd;

      val absolutePath =
        if (dir.startsWith(Directory.SEPERATOR)) dir
        else if (wd.isRoot) wd.path + dir
        else wd.path + Directory.SEPERATOR + dir

      val destinationDir = doFindEntry(root, absolutePath)

      if (destinationDir == null || !destinationDir.isDirectory)
        state.setMessage(s"$dir: no such directory")
      else
        State(root, destinationDir.asDirectory)
    }
  }

  def doFindEntry(root: Directory, absolutePath: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDir: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDir
      else if (path.tail.isEmpty) currentDir.findEntry(path.head)
      else {
        val nextDir = currentDir.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    def collapseRelativetokens(path: List[String], result: List[String]): List[String] = {
      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativetokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativetokens(path.tail, result.init)
      } else {
        collapseRelativetokens(path.tail, result :+ path.head)
      }
    }

    val tokens = absolutePath.substring(1).split(Directory.SEPERATOR).toList
    val finalTokens = collapseRelativetokens(tokens, List())
    if (finalTokens == null) null
    else findEntryHelper(root, finalTokens)
  }

}
