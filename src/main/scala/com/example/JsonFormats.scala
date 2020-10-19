package com.example

import com.example.UserRegistry.ActionPerformed

import spray.json.DefaultJsonProtocol

object JsonFormats  {
    import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
