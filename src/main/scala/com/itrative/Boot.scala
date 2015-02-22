package com.itrative

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.itrative.config.DBConfig
import com.itrative.services.TaskServiceActor
import spray.can.Http

object Boot extends App with DBConfig {
  implicit val system = ActorSystem("spray-slick-rest")

  val service = system.actorOf(Props[TaskServiceActor], "task-service")

  IO(Http) ! Http.Bind(service, "localhost", port = 8080)
}