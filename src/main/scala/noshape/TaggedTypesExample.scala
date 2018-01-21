package noshape

import shapeless.tag.@@
import shapeless.tag

trait NameTag

trait SurnameTag

trait NicknameTag



object TaggedTypesExample extends App {
  type Name = String @@ NameTag

  case class JustStrings(name: Name, surname: String @@ SurnameTag, nickname: String @@ NicknameTag)

  implicit class TagOps[A](val in: A) extends AnyVal {
    import shapeless.{tag => sTag}
    def tag[T] = sTag[T](in)
    def test: String = s"$in and then abc"
  }

  val name = tag[NameTag]("pawel")
  val surname = tag[SurnameTag]("slow")

  val nickname = "slowikps".tag[NicknameTag]

  val justStrings =
    JustStrings(name, surname, nickname)


  println(justStrings)
  println("Test".test)

}
