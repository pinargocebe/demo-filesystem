package com.pinar.examples.scala.filesystem

import com.pinar.examples.scala.files.Directory

class State(val root: Directory, val wd: Directory, val output: String) {
  def show: Unit = {
    println(output)
    print(State.SHELL_PROMPT)
  }

  def setMessage(message: String): State =
    State(root, wd, message)
}

object State {
  val SHELL_PROMPT = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State =
    new State(root, wd, output)
}