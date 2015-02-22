package com.itrative.services

import akka.actor.Actor
import com.itrative.config.DBConfig
import com.itrative.model.{Task, Tasks}
import spray.http.MediaTypes
import spray.routing.HttpService

import scala.slick.driver.MySQLDriver.simple._

class TaskServiceActor extends Actor with TaskService {
  def actorRefFactory = context

  def receive = runRoute(route)
}

trait TaskService extends HttpService with DBConfig {
  implicit def executionContext = actorRefFactory.dispatcher

  val route = {
    import com.itrative.model.TaskProtocol._
    import spray.httpx.SprayJsonSupport.{sprayJsonMarshaller, sprayJsonUnmarshaller}

    pathPrefix("tasks") {
      path(LongNumber) { id =>
        get {
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              db.withSession {
                implicit session: Session =>
                  Tasks.q.filter(_.id === Option(id)).list.headOption
              }
            }
          }
        } ~
          delete {
            db.withSession {
              implicit session: Session =>
                Tasks.q.filter(_.id === Option(id)).delete
            }
            complete(200, "Entity deleted")
          }
      } ~
        pathEnd {
          get {
            respondWithMediaType(MediaTypes.`application/json`) {
              complete {
                db.withSession {
                  implicit session: Session =>
                    Tasks.q.list
                }
              }
            }
          } ~
            post {
              entity(as[Task]) {
                task =>
                  respondWithMediaType(MediaTypes.`application/json`) {
                    complete {
                      db.withSession {
                        implicit session: Session =>
                          val autoinc = (Tasks.q returning Tasks.q.map(_.id)) += task
                          Tasks.q.filter(_.id === autoinc).list.headOption
                      }
                    }
                  }
              }
            }
        }
    }
  }
}