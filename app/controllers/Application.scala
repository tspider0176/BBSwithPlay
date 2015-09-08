package controllers

import play.api.db.DB
import play.api.mvc._
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import java.util.Calendar
import java.text.SimpleDateFormat
import play.api.libs.json.Json

import views.html.defaultpages.badRequest

class Application extends Controller {
  // GET
  // refresh Web page
  def get() = Action {
    val res = DB.withConnection { implicit con =>
       SQL("SELECT * FROM thread ORDER BY id").as(str("date") ~ str("text") ~ int("id") *).map(flatten)
    }

    Ok(Json.toJson(
        res.map { tup =>
          Json.obj(
            "id" -> tup._3,
            "date" -> tup._1,
            "text" -> tup._2
          )
        }
      )
    )
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
        val insert = "INSERT into thread(date, text, id)"
        SQL(insert + " values (\"" + sdf.format(c.getTime) + "\",\"" + text + "\"," + (numOfRes(0)+1) + ")").executeUpdate()
      }

      Ok("POST\n")
    }getOrElse {
      BadRequest("400 BAD REQUEST")
    }
  }

  // DELETE curl command
  // curl -X DELETE http://localhost:9000/bbs/delete/[id number]
  def delete(id: String) = Action{
    DB.withConnection { implicit con =>
      SQL(" UPDATE thread SET date=\"DELETED\", text=\"DELETED\" where id=" + id.toInt).executeUpdate()
    }

    Ok("DELETE\n")
  }
}
