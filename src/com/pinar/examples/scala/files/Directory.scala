package com.pinar.examples.scala.files

import com.pinar.examples.scala.exceptions.FileSystemException
import com.pinar.examples.scala.filesystem.State

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory = {
    new Directory(parentPath, name, contents.filter(p => !p.name.equals(entryName)) :+ newEntry)
  }

  def findEntry(name: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry = {
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)
    }

    findEntryHelper(name, contents)
  }


  def addEntry(newEntry: DirEntry): Directory = {
    new Directory(parentPath, name, contents :+ newEntry)
  }

  def removeEntry(entry: String): Directory = {
    if (!hasEntry(entry)) this
    else
      new Directory(parentPath, name, contents.filter(p => !p.name.equals(entry)))
  }

  def findDescendant(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def findDescendant(relativePath: String): Directory =
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPERATOR).toList)

  def getAllFoldersInPath: List[String] = {
    path.substring(1).split(Directory.SEPERATOR).toList.filter(p => !p.isEmpty)
  }

  def hasEntry(name: String): Boolean = findEntry(name) != null

  def isRoot: Boolean = parentPath.isEmpty

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  override def asFile: File = throw new FileSystemException("A directory cannot be converted to a file")

  override def isDirectory: Boolean = true

  override def isFile: Boolean = false

}

object Directory {
  val SEPERATOR = "/"
  val ROOT_PATH = "/"

  def ROOT = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}