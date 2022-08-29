package demo

import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.useEffect
import slinky.web.html._

@react
object VegaLiteTutorial {

  type Props = Unit

  private val spec: String =
    """
      |{
      |  $schema: 'https://vega.github.io/schema/vega-lite/v5.json',
      |  data: {
      |    values: [ {a: 'C', b: 2}, {a: 'C', b: 7}, {a: 'C', b: 4}, {a: 'D', b: 1}, {a: 'D', b: 2}, {a: 'D', b: 6}, {a: 'E', b: 8}, {a: 'E', b: 4}, {a: 'E', b: 7} ]
      |  },
      |  mark: 'bar',
      |  encoding: {
      |    y: {field: 'a', type: 'nominal'},
      |    x: {
      |      aggregate: 'average',
      |      field: 'b',
      |      type: 'quantitative',
      |      axis: {
      |        title: 'Average of b'
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  private val spec2 =
    typings.vegaTypings.specMod
      .Spec()
      .set$schema("https://vega.github.io/schema/vega-lite/v5.json")
      .set(
        "data",
        """
          |{
          |    values: [ {a: 'C', b: 2}, {a: 'C', b: 7}, {a: 'C', b: 4}, {a: 'D', b: 1}, {a: 'D', b: 2}, {a: 'D', b: 6}, {a: 'E', b: 8}, {a: 'E', b: 4}, {a: 'E', b: 7} ]
          |}""".stripMargin)
      .set("mark", "bar")
      .set(
        "encoding",
        """
            |{
            |    y: {field: 'a', type: 'nominal'},
            |    x: {
            |      aggregate: 'average',
            |      field: 'b',
            |      type: 'quantitative',
            |      axis: {
            |        title: 'Average of b'
            |      }
            |    }
            |}""".stripMargin
      )

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    useEffect(
      () => {
        println("Embedding vega-lite plot")
        typings.vegaEmbed.mod.default("#vega-lite-tutorial", spec2)
      },
      Seq.empty
    )

    div(id := "vega-lite-tutorial")

  }

}
