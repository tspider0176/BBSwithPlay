package controllers

import play.api.db.DB
import play.api.mvc._
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import java.util.Calendar
import java.text.SimpleDateFormat

import views.html.defaultpages.badRequest

class Application extends Controller {
  // GET
  // refresh Web page
  def get() = Action{
    val res = DB.withConnection { implicit con =>
       SQL("SELECT * FROM thread").as(str("date") ~ str("text") ~ int("id") *).map(flatten)
    }

    Ok(res.mkString("\n"))
  }

  // POST curl command
  // curl -H "Content-Type: text/plain" -d 'abc' http://localhost:9000/bbs
  def post() = Action { request =>
    val body: AnyContent = request.body
    val textBody: Option[String] = body.asText

    textBody.map {text =>
      val c = Calendar.getInstance()
      val sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:S")

      val numOfRes = DB.withConnection { implicit con =>
        SQL("SELECT COUNT(*) from thread").as(int("count(*)") *)
      }

      DB.withConnection { implicit con =>
        SQL("INSERT into thread(date, text, id) values (\"" + sdf.format(c.getTime) + "\",\"" + text + "\"," + (numOfRes(0)+1) + ")").executeUpdate()
      }

      Ok("POST")
    }getOrElse {
      BadRequest("400 BAD REQUEST")
    }
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
