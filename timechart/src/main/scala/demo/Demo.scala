package demo

import org.scalajs.dom
import slinky.web.ReactDOM

object Demo {
  def main(argv: Array[String]): Unit =
    ReactDOM.render(SinusoidalWaves(), dom.document.getElementById("container"))
}
