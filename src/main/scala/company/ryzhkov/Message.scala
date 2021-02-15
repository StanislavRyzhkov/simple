package company.ryzhkov

import java.time.LocalDate

sealed trait Message {
  def `type`: Int
}

case class Foo(f1: String,
               f2: String,
               date: LocalDate,
               `type`: Int) extends Message

case class Bar(b1: Int,
               b2: Int,
               date: LocalDate,
               `type`: Int) extends Message
