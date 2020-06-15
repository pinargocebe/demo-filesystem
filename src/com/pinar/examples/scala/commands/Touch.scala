package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.{DirEntry, File}
import com.pinar.examples.scala.filesystem.State

class Touch(commandStr: String) extends CreateEntry(commandStr) {
  override def createSpecificEntry(state: State, newEntryName: String): DirEntry = File.empty(state.wd.path, newEntryName)
}
