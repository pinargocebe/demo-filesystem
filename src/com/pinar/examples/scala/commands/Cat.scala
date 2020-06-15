package com.pinar.examples.scala.commands

import com.pinar.examples.scala.filesystem.State

class Cat(commandStr: String) extends Command {
  override def apply(state: State): State = {
    val tokens = commandStr.split(" ")
    if (tokens.length < 2) Command.incompleteCommand(tokens(0)).apply(state)
    else {
      val fileName = tokens(1)
      val wd = state.wd
      val entry = wd.findEntry(fileName)
      if (entry == null || !entry.isFile) {
        state.setMessage(s"$fileName: no such file")
      } else state.setMessage(entry.asFile.contents)

    }

  }
}
