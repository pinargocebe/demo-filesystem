package com.pinar.examples.scala.filesystem

import java.util.Scanner

import com.pinar.examples.scala.commands.Command
import com.pinar.examples.scala.files.Directory

object Filesystem extends App {

  val root = Directory.ROOT
  //  val scanner = new Scanner(System.in)
//  var state = State(root, root)

  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
    currentState.show
    Command.from(newLine).apply(currentState)
  })
  //  while (true) {
  //    state.show
  //    val input = scanner.nextLine()
  //    state = Command.from(input).apply(state)
  //  }
}
