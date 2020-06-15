package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.{DirEntry, Directory}
import com.pinar.examples.scala.filesystem.{Filesystem, State}

class Mkdir(val commandStr: String) extends CreateEntry(commandStr) {
  override def createSpecificEntry(state: State, newEntryName: String): DirEntry = Directory.empty(state.wd.path, newEntryName)
}
