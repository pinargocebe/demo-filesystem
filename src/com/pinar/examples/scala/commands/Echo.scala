package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.{Directory, File}
import com.pinar.examples.scala.filesystem.State

import scala.annotation.tailrec

class Echo(commandStr: String) extends Command {
  override def apply(state: State): State = {
    val tokens = commandStr.split(" ")
    if (tokens.length < 2) Command.incompleteCommand(tokens(0)).apply(state)
    else {
      val args = tokens.tail
      if (args.isEmpty) state
      else if (args.length == 1) state.setMessage(args(0))
      else {
        val operator = args(args.length - 2)
        val fileName = args(args.length - 1)
        val content = createcontent(args, args.length - 2)
        if (">>".equals(operator)) doEcho(state, content, fileName, true)
        else if (">".equals(operator)) doEcho(state, content, fileName, false)
        else state.setMessage(createcontent(args, args.length))
      }
    }
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    /*
      if path is empty, then fail (currentDirectory)
      else if no more things to explore = path.tail.isEmpty
        find the file to create/add content to
        if file not found, create file
        else if the entry is actually a directory, then fail
        else
          replace or append content to the file
          replace the entry with the filename with the NEW file
      else
        find the next directory to navigate
        call gRAE recursively on that

        if recursive call failed, fail
        else replace entry with the NEW directory after the recursive call
     */
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if (dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
      else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, content: String, fileName: String, append: Boolean): State = {
    if (fileName.contains(Directory.SEPERATOR))
      state.setMessage("Echo: filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ fileName, content, append)
      if (newRoot == state.root)
        state.setMessage(fileName + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
  }

  def createcontent(args: Array[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }
}
