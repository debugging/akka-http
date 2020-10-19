package com.example

import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route


import akka.actor.typed.ActorSystem

class HealthRoutes()(implicit val system: ActorSystem[_]) {

  val routes: Route =
    pathPrefix("health") {
      concat(
        pathEnd {
          concat(
            get {
              complete("PONG")
            },        
          )
        },
        )
    }    
}


