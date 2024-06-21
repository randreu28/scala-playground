package Operations

import Main.{Rover, Direction, Config}

/** Object containing operations that can be performed on a Rover */
object RoverOperations {

  /** Moves the rover forward based on its current direction.
    *
    * @param rover
    *   the current state of the rover
    * @return
    *   a new rover instance with updated position
    */
  def moveForward(rover: Rover): Rover = rover.direction match {
    case Direction.N => rover.copy(y = (rover.y + 1) % Config.maxY)
    case Direction.S =>
      rover.copy(y = (rover.y - 1 + Config.maxY) % Config.maxY)
    case Direction.E => rover.copy(x = (rover.x + 1) % Config.maxX)
    case Direction.W =>
      rover.copy(x = (rover.x - 1 + Config.maxX) % Config.maxX)
  }

  /** Rotates the rover 90 degrees to the left.
    *
    * @param rover
    *   the current state of the rover
    * @return
    *   a new rover instance with updated direction
    */
  def turnLeft(rover: Rover): Rover = {
    val newDirection = rover.direction match {
      case Direction.N => Direction.W
      case Direction.S => Direction.E
      case Direction.E => Direction.N
      case Direction.W => Direction.S
    }
    rover.copy(direction = newDirection)
  }

  /** Rotates the rover 90 degrees to the right.
    *
    * @param rover
    *   the current state of the rover
    * @return
    *   a new rover instance with updated direction
    */
  def turnRight(rover: Rover): Rover = {
    val newDirection = rover.direction match {
      case Direction.N => Direction.E
      case Direction.S => Direction.W
      case Direction.E => Direction.S
      case Direction.W => Direction.N
    }
    rover.copy(direction = newDirection)
  }
}
