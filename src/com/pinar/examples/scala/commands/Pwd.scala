package com.pinar.examples.scala.commands

import com.pinar.examples.scala.files.Directory
import com.pinar.examples.scala.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State = {
    state.setMessage(state.wd.path)
  }

}
