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


### Resources

- https://docs.scala-lang.org/getting-started/index.html#using-vscode-with-metals