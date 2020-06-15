package com.pinar.examples.scala.commands

import java.nio.file.{Files, Paths}

import com.pinar.examples.scala.filesystem.State

import scala.reflect.internal.util.FileUtils
import scala.reflect.io.File

class UnknownCommand extends Command {

  override def apply(state: State): State = {
    state.setMessage("Command not found")
  }
}
