//TODO: Add world boundaries
@main
def computeRoverInstruction(args: String*): Rover = {
  val commands = parseAndValidateCommands(args*)
  val initialRover = Rover(0, 0, Direction.N)

  val finalRover = commands.foldLeft(initialRover) { (rover, command) =>
    command match {
      case 'F' => rover.moveForward
      case 'L' => rover.turnLeft
      case 'R' => rover.turnRight
      case _ => throw new IllegalArgumentException(s"Invalid command: $command")
    }
  }

  println(finalRover)
  finalRover
}

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

enum Direction:
  case N, S, E, W

case class Rover(x: Int, y: Int, direction: Direction) {
  def turnRight: Rover = this.copy(direction = direction match {
    case Direction.N => Direction.E
    case Direction.E => Direction.S
    case Direction.S => Direction.W
    case Direction.W => Direction.N
  })

  def turnLeft: Rover = this.copy(direction = direction match {
    case Direction.N => Direction.W
    case Direction.W => Direction.S
    case Direction.S => Direction.E
    case Direction.E => Direction.N
  })

  def moveForward: Rover = direction match {
    case Direction.N => this.copy(y = y + 1)
    case Direction.S => this.copy(y = y - 1)
    case Direction.E => this.copy(x = x + 1)
    case Direction.W => this.copy(x = x - 1)
  }
}
