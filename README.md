## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

### Usage with VsCode

Use [this](https://marketplace.visualstudio.com/items?itemName=scalameta.metals) extension.

### How to run

1. `cd` into the project directory
2. Run `sbt`. This opens the sbt console
3. Type `~run.` The `~` is optional and causes sbt to re-run on every file save, allowing for a fast edit/run/debug cycle. sbt will also generate a target directory which you can ignore.

> When you’re finished experimenting with this project, press `[Enter]` to interrupt the `run` command. Then type `exit` or press `[Ctrl+D]` to exit sbt and return to your command line prompt.

### Test and debug

You can test and debug inside VsCode if you have the extension. You can also run `sbt test` from the command line.

# Kata: Mars exploration Rover

- **Objective**: Develop an API for remote-controlled vehicles on Mars.

- **Initialization**: The rover starts with a position (x,y) and a direction (N,S,E,W).

- **Commands**: The rover receives strings of commands like 'FFLR'.

  - F: Move forward.
  - L: Turn left.
  - R: Turn right.

- **Output**: Final location of the vehicle. Example: '2:3:W'.

## Rules and Examples

- If the rover reaches the end of the planet, it wraps around the coordinate axis. There are no negative coordinates.

- Examples for a rover initialized at coordinates 0,0 and oriented towards the north:

  - 'L' => '0:0:W'
  - 'R' => '0:0:E'
  - 'F' => '0:1:N'
  - 'RFF' => '2:0:E'
  - 'LFF' => '0:8:W'
  - 'LLFF' => '0:8:S'
  - 'FRFFR' => '2:1:S'
