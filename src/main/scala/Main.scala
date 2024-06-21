package Main

import Operations.RoverOperations.{moveForward, turnLeft, turnRight}

/** Represents the direction a Rover can face. */
enum Direction:
  case N, S, E, W

/** Represents the state of a Rover with its position and direction. */
case class Rover(x: Int, y: Int, direction: Direction)

/** Configuration object holding constants related to the Rover's operation. */
object Config {
  val maxX: Int = 10
  val maxY: Int = 10
  val initialRover: Rover = Rover(0, 0, Direction.N)
}

/** Computes the final state of the Rover based on a sequence of movement
  * commands.
  *
  * @param args
  *   The arguments passed to the program.
  * @return
  *   The final state of the Rover after executing all commands.
  */
@main
def computeRoverInstruction(args: String*): Rover = {
  val commands = parseAndValidateCommands(args*)

  val finalRover = commands.foldLeft(Config.initialRover) { (rover, command) =>
    command match {
      case 'F' => moveForward(rover)
      case 'L' => turnLeft(rover)
      case 'R' => turnRight(rover)
    }
  }

  println(finalRover)
  finalRover
}

/** Parses and validates the input commands ensuring they are valid before
  * processing.
  *
  * @param args
  *   A variable number of string arguments representing movement commands.
  * @return
  *   An array of characters representing the validated commands.
  */
def parseAndValidateCommands(args: String*): Array[Char] = {
  require(args.nonEmpty, "At least one argument must be provided")
  val instructions = args(0)
  val pattern = "^[FLR]+$".r
  require(
    pattern.matches(instructions),
    "Commands must only include F, L, R characters"
  )
  instructions.toCharArray
}
