import Main.{Rover, Direction, computeRoverInstruction}

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("Low complexity tests") {
    assertEquals(computeRoverInstruction("L"), Rover(0, 0, Direction.W))
    assertEquals(computeRoverInstruction("R"), Rover(0, 0, Direction.E))
    assertEquals(computeRoverInstruction("F"), Rover(0, 1, Direction.N))
  }

  test("Middle complexity tests") {
    assertEquals(computeRoverInstruction("RFF"), Rover(2, 0, Direction.E))
    assertEquals(computeRoverInstruction("LFF"), Rover(8, 0, Direction.W))
    assertEquals(computeRoverInstruction("LLFF"), Rover(0, 8, Direction.S))
    assertEquals(computeRoverInstruction("FRFFR"), Rover(2, 1, Direction.S))
  }

  test("High complexity tests") {
    assertEquals(computeRoverInstruction("LFFRFF"), Rover(8, 2, Direction.N))
    assertEquals(computeRoverInstruction("LLFFFRF"), Rover(9, 7, Direction.W))
    assertEquals(computeRoverInstruction("FRFFRLRF"), Rover(2, 0, Direction.S))
    assertEquals(
      computeRoverInstruction("FFFFLRLLFFFFRR"),
      Rover(0, 0, Direction.N)
    )
  }
}
