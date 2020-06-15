package com.pinar.examples.scala.commands

import com.pinar.examples.scala.filesystem.State

trait Command extends (State => State)

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")
    if (input.isEmpty || tokens.isEmpty) {
      emptyCommand
    } else {
      tokens(0) match {
        case MKDIR => new Mkdir(input)
        case TOUCH => new Touch(input)
        case LS => new Ls
        case PWD => new Pwd
        case CD => new Cd(input)
        case RM => new Rm(input)
        case ECHO => new Echo(input)
        case CAT => new Cat(input)
        case _ => new UnknownCommand
      }
    }
  }

  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State = state.setMessage(s"$name incomplete command")
  }

}