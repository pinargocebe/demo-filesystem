package com.pinar.examples.scala.files

abstract class DirEntry(val parentPath: String, val name: String) {

  def path = {
    val separatorIfNecessary =
      if (Directory.ROOT_PATH.equals(parentPath)) ""
      else Directory.SEPERATOR
    parentPath + separatorIfNecessary + name
  }

  def asDirectory: Directory

  def asFile: File

  def getType: String

  def isDirectory: Boolean

  def isFile: Boolean
}
