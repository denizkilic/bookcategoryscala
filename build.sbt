name := "bookcategoryscala"

version := "0.1"

scalaVersion := "2.12.8"


/*
  Here is project dependencies declaration
  `libraryDependencies` is magic object of SBT - widely-used build tool for Scala projects
  We are "adding" (++= <collection>) dependencies here, because some dependencies may be already defined "implicitly"
  by compiler plugins from meta-project definition (see ./project directory)
  
  Also, some parametrization may be performed
 */

val http4sVersion = "0.20.0-M6"
val circeVersion = "0.11.1"

libraryDependencies ++= List(
  /* comment :)
    Here is one of mainly used dependency: Cats Effect
    Cats is a kind of "ecosystem" for functional programming in Scala
    And Cats Effect is a library of code for dealing with "side-effectfull" computations
    
    Cats is implicit dependency of Cats Effect. You can check dependency of library in few places:
     * on mvnrepository (or other repository): https://mvnrepository.com/artifact/org.typelevel/cats-effect_2.12/1.2.0
     * on source code: https://github.com/typelevel/cats-effect/blob/master/build.sbt#L258
                       https://github.com/typelevel/cats-effect/commit/8b1ba209fde878296eade713ca651aca3b626ea1
   */
  "org.typelevel" %% "cats-effect" % "1.2.0",
  /*                  
    Good practice is, when you use implicit dependency in your code, making this dependency explicit.
    So here is Cats library. Alternative to Cats is Scalaz. Check out the comparison of these and join discussions:
    https://github.com/fosskers/scalaz-and-cats
   */
  "org.typelevel" %% "cats-core" % "1.6.0",

  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "org.typelevel" %% "jawn-parser" % "0.14.0",

  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
)


/*
  Here is some options for compiler.
  You will know what does it mean when you meet library which requires specific options to be set explicitly by you.
  You will see something like this on "getting-started" of library:
    > Cats relies on improved type inference via the fix for SI-2712,
    > which is not enabled by default. For Scala 2.11.9 or later you
    > should add the following to your build.sbt:
 */
scalacOptions ++= List(
  "-Ypartial-unification"
)