package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import com.example.UserRegistry._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import akka.http.scaladsl.server.{Directive, Directive1}

class UserRoutes(userRegistry: ActorRef[UserRegistry.Command])(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getUsers(): Future[Users] =
    userRegistry.ask(GetUsers)
  def getUser(name: String): Future[GetUserResponse] =
    userRegistry.ask(GetUser(name, _))
  def createUser(user: User): Future[ActionPerformed] =
    userRegistry.ask(CreateUser(user, _))
  def deleteUser(name: String): Future[ActionPerformed] =
    userRegistry.ask(DeleteUser(name, _))

  val routes: Route =
    pathPrefix("users") {
      concat(
        pathEnd {
          concat(
            get {
              complete(getUsers())
            },
            authenticated { userContext => 
            post {
              entity(as[User]) { user =>
                onSuccess(createUser(user)) { performed =>                  
                  complete((StatusCodes.Created, 
                  performed.copy(description = performed.description + userContext.username)))
                }
              }
            }
            }
          )
        },
        path(Segment) { name =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getUser(name)) { response =>
                  complete(response.maybeUser)
                }
              }
            },
            delete {
              onSuccess(deleteUser(name)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        }) 
    }
  case class UserContext(username: String)

  def authenticated: Directive1[UserContext] = {
  extractCredentials.flatMap { credentials =>
      Directive { inner =>
        ctx => {
          val result = credentials match {
            case Some(c) if c.scheme.equalsIgnoreCase("Bearer") => UserContext(c.token()) // authenticate(c.token)
            case _ => UserContext("anon") //rejectUnauthenticated(AuthenticationFailedRejection.CredentialsMissing)
          }
          inner(Tuple1(result))(ctx)
        }
      }
    }
  }
  
}