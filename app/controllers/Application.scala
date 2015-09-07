package controllers

import play.api.db.DB
import play.api.mvc._
import java.io.{ FileOutputStream=>FileStream, OutputStreamWriter=>StreamWriter }
import java.sql._
import anorm._

class Application extends Controller {

  // curl -H "Content-Type: text/plain" -d 'abc' http://localhost:9000/bbs
  def get() = Action {
    /*
    DB.withConnection { implicit conn =>
      val result: Boolean = SQL("Select * From detail").execute()
    }
    */

    Ok("GET")
  }

  def post() = Action {/* request =>
    val body: AnyContent = request.body
    val textBody: Option[String] = body.asText

    // Expecting text body
    textBody.map { text =>
      val fileOutPutStream = new FileStream("db.txt", true)
      val writer = new StreamWriter(fileOutPutStream, "UTF-8")

      writer.write(s"$text\n")
      writer.close()
      }
    */
      Ok("Post")
  }

  def delete(id: String) = Action{
    /*
    val source = scala.io.Source.fromFile("db.txt", "UTF-8")
    val lines = source.getLines().toList.drop(id.toInt)

    val fileOutPutStream = new FileStream("db.txt", true)
    val writer = new StreamWriter(fileOutPutStream, "UTF-8")

    writer.write(s"$lines\n")
    writer.close()
    */
    Ok("Delete")
  }
}
