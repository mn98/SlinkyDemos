package demo

import org.scalajs.dom.html.Div
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.Hooks.{useEffect, useRef, useState}
import slinky.web.html._
import typings.std.global.performance
import typings.timechart.anon.Max
import typings.timechart.mod.{TimeChart, default => TimeChartConstructor}
import typings.timechart.renderModelMod.DataPoint
import typings.timechart.timechartStrings
import typings.timechart.typesOptionsMod.{
  AxisZoomOptions,
  ResolvedZoomOptions,
  TimeChartOptions,
  TimeChartSeriesOptions
}

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.timers.RawTimers.{clearInterval, setInterval}

@react
object SinusoidalWaves {

  type Props = Unit

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { _ =>
    val chartDivRef = useRef[Div](null)
    val (chart, setChart) = useState[TimeChart](() => null)
    val (pointCount, setPointCount) = useState(0)
    val (chartTime, setChartTime) = useState(performance.now() - 20 * 1000)

    useEffect(
      () =>
        if (chartDivRef.current != null) {
          println(s"Creating time chart")
          val newChart = new TimeChartConstructor(
            chartDivRef.current,
            TimeChartOptions().setSeries(js.Array())
          )
          val baseTime = Date.now() - performance.now()
          newChart.options.setBaseTime(baseTime)
          newChart.options.series.push(
            TimeChartSeriesOptions(
              "blue",
              js.Array(),
              "Sin",
              visible = true
            ),
            TimeChartSeriesOptions(
              "red",
              js.Array(),
              "Cos",
              visible = true
            )
          )
          newChart.options.setXRange(Max(20 * 1000, 0))
//          newChart.options.setXRange(timechartStrings.auto)
          newChart.options.setRealTime(true)
//          newChart.options.zoom.foreach { zoom =>
//            zoom.x.foreach(_.setMinDomainExtent(50))
//            zoom.y.foreach(_.setMinDomainExtent(1))
//          }
//          newChart.options.setZoom(
//            ResolvedZoomOptions()
//              .setX(AxisZoomOptions(autoRange = true, 0, 0, 0, 50))
//              .setY(AxisZoomOptions(autoRange = true, 0, 0, 0, 1))
//          )
          setChart(newChart)
        },
      Seq(chartDivRef)
    )

    useEffect(
      () => {
        println("Setting up interval")
        val interval = setInterval(
          () =>
            setChartTime {
              chartTime =>
                val time = performance.now()
                val n = time.toLong - chartTime.toLong
                //println(s"Adding another $n points between times $chartTime and $time")
                for (x <- chartTime.toLong until time.toLong) {
                  chart.options.series.find(_.name == "Sin").map { series =>
                    series.data.push(DataPoint(x.toDouble, Math.sin(x * 0.002) * 320))
                  }
                  chart.options.series.find(_.name == "Cos").map { series =>
                    series.data.push(DataPoint(x.toDouble, Math.cos(x * 0.002) * 200))
                  }
                }
                chart.options.series.find(_.name == "Sin").foreach(series => setPointCount(series.data.size))
                chart.update()
                time
            },
          interval = 5
        )
        () => clearInterval(interval)
      },
      Seq(chart)
    )

    div(
      h1("TimeChart Demo"),
      h2("Adding 1000 points per second to chart."),
      p(span(id := "point-count")(pointCount), " Points"),
      div(ref := chartDivRef, className := "chart"),
      button("Stop", id := "stop"),
      button("Follow Data", id := "follow"),
      button("Toggle Legend", id := "legend")
    )

  }

}
