package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.DirEntry
import com.pinar.examples.scala.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutput(contents)
    state.setMessage(niceOutput)
  }

  def createNiceOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      val niceOutput = s"${entry.name}[${entry.getType}]\n"
      niceOutput + createNiceOutput(contents.tail)
    }
  }
}
