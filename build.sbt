import sbt._

resolvers += Resolver.sonatypeRepo("releases")
//resolvers += Resolver.sonatypeRepo("snapshots")

name := "slug-monocle"

version := "1.0"

scalaVersion := "2.11.7"

val monocleVersion = "1.2.0"     // or "1.3.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"    % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-generic" % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-state"   % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-refined" % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-law"     % monocleVersion % "test",
  "org.specs2" %% "specs2-core" % "3.7" % "test"
)

// for @Lenses macro support
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)

scalacOptions in Test ++= Seq("-Yrangepos")
