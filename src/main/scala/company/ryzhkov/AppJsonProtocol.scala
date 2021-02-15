package company.ryzhkov

import spray.json._

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object AppJsonProtocol extends DefaultJsonProtocol {
  private val parsingError = "Json parsing failed..."

  implicit object DateJsonFormat extends RootJsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = ???

    override def read(json: JsValue): LocalDate = json match {
      case JsString(value) => LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
      case _ => serializationError(parsingError)
    }
  }

  implicit val fooJsonFormat: RootJsonFormat[Foo] = jsonFormat4(Foo)
  implicit val barJsonFormat: RootJsonFormat[Bar] = jsonFormat4(Bar)

  implicit object messageJsonFormat extends RootJsonFormat[Message] {
    override def write(obj: Message): JsValue = ???

    override def read(json: JsValue): Message = {
      val msgType = json.asJsObject.fields("type").convertTo[Int]

      msgType match {
        case 1 => json.convertTo[Foo]
        case 2 => json.convertTo[Bar]
        case _ => deserializationError(parsingError)
      }
    }
  }
}
